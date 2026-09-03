package techguns.tileentities;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.jetbrains.annotations.NotNull;
import techguns.TGBlocks;
import techguns.TGPackets;
import techguns.TGSounds;
import techguns.*;
import techguns.api.tginventory.ITGSpecialSlot;
import techguns.api.tginventory.TGSlotType;
import techguns.blocks.machines.EnumMultiBlockMachineType;
import techguns.blocks.machines.MultiBlockMachine;
import techguns.blocks.machines.multiblocks.ReactionChamberDefinition;
import techguns.blocks.machines.multiblocks.SlavePos;
import techguns.gui.ButtonConstants;
import techguns.packets.PacketSpawnParticle;
import techguns.packets.PacketUpdateTileEntTanks;
import techguns.tileentities.operation.*;
import techguns.tileentities.operation.ReactionChamberRecipe.RiskType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class ReactionChamberTileEntMaster extends MultiBlockMachineTileEntMaster implements ITileEntityFluidTanks, SimpleComponent {

    public static final int CAPACITY_INPUT_TANK = 10 * Fluid.BUCKET_VOLUME;
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FOCUS = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int OUTPUT_SLOTS_COUNT = 4;
    public static final int BUTTON_ID_INTENSITY_INC = ButtonConstants.BUTTON_ID_REDSTONE + 3;
    public static final int BUTTON_ID_INTENSITY_DEC = ButtonConstants.BUTTON_ID_REDSTONE + 4;
    public static final int BUTTON_ID_DUMPTANK = ButtonConstants.BUTTON_ID_REDSTONE + 5;
    public static final int BUTTON_ID_SET_FLUID_LIMIT = ButtonConstants.BUTTON_ID_REDSTONE + 6;
    public static final byte MIN_INTENSITY = 1;
    public static final byte MAX_INTENSITY = 10;
    public FluidTank inputTank;
    public MachineSlotItem input;
    public boolean fluidsChanged = false;
    protected byte intensity = MIN_INTENSITY;
    protected int fluidLimit = 0;

    public ReactionChamberTileEntMaster() {
        super(6, 1000000);

        this.inputTank = new ReactionChamberFluidTank(this, CAPACITY_INPUT_TANK) {
            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if (!ReactionChamberTileEntMaster.this.isFormed()) return 0;
                return super.fill(resource, doFill);
            }

            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                if (!ReactionChamberTileEntMaster.this.isFormed()) return null;
                return super.drain(maxDrain, doDrain);
            }
        };

        this.inventory = new ItemStackHandlerPlus(6) {

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setContentsChanged(true);
            }

            @Override
            protected boolean allowItemInSlot(int slot, ItemStack stack) {
                if (!ReactionChamberTileEntMaster.this.isFormed()) {
                    return false;
                }
                if (slot <= SLOT_FOCUS) {
                    return isItemValidForSlot(slot, stack);
                } else {
                    return false;
                }
            }

            @Override
            protected boolean allowExtractFromSlot(int slot, int amount) {
                if (!ReactionChamberTileEntMaster.this.isFormed()) return false;
                return slot >= SLOT_OUTPUT && slot < SLOT_OUTPUT + OUTPUT_SLOTS_COUNT;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (!ReactionChamberTileEntMaster.this.isFormed()) return false;
                return super.isItemValid(slot, stack);
            }
        };

        input = new MachineSlotItem(this, SLOT_INPUT);

    }

    public boolean isItemValidForSlot(int slot, ItemStack item) {
        if (slot == SLOT_INPUT) {
            return true;
        } else if (slot == SLOT_FOCUS) {
            return ReactionBeamFocus.getBeamFocus(item) != null;
        }
        return false;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
        if (!this.isFormed()) return false;
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
        if (!this.isFormed()) return null;
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) inputTank : super.getCapability(capability, facing);
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if (this.isFormed()) {
            BlockPos p = this.getPos();
            EnumFacing left = multiblockDirection.rotateY();

            BlockPos other = p.offset(multiblockDirection, 3).offset(left, 2).offset(EnumFacing.UP, 4);
            BlockPos first = p.offset(multiblockDirection.getOpposite(), 1).offset(left.getOpposite(), 2).offset(EnumFacing.DOWN, 1);
            return new AxisAlignedBB(first, other);
        } else {
            return super.getRenderBoundingBox();
        }
    }

    @Override
    public void readClientDataFromNBT(NBTTagCompound tags) {
        super.readClientDataFromNBT(tags);

        NBTTagCompound inputTankTags = tags.getCompoundTag("inputTank");
        this.inputTank.readFromNBT(inputTankTags);

        this.setIntensity(tags.getByte("intensity"));
        this.setFluidLimit(tags.getInteger("fluidLimit"));

        if (tags.hasKey("inputSlot")) {
            ItemStack inputSlot = new ItemStack(tags.getCompoundTag("inputSlot"));
            this.inventory.setStackInSlot(SLOT_INPUT, inputSlot);
        } else {
            this.inventory.setStackInSlot(SLOT_INPUT, ItemStack.EMPTY);
        }

        if (tags.hasKey("focusSlot")) {
            ItemStack focusSlot = new ItemStack(tags.getCompoundTag("focusSlot"));
            this.inventory.setStackInSlot(SLOT_FOCUS, focusSlot);
        } else {
            this.inventory.setStackInSlot(SLOT_FOCUS, ItemStack.EMPTY);
        }
    }

    @Override
    public void writeClientDataToNBT(NBTTagCompound tags) {
        super.writeClientDataToNBT(tags);

        NBTTagCompound inputTankTags = new NBTTagCompound();
        this.inputTank.writeToNBT(inputTankTags);
        tags.setTag("inputTank", inputTankTags);

        tags.setByte("intensity", this.intensity);
        tags.setInteger("fluidLimit", this.fluidLimit);

        if (!this.input.get().isEmpty()) {
            NBTTagCompound inputSlot = new NBTTagCompound();
            this.input.get().writeToNBT(inputSlot);
            tags.setTag("inputSlot", inputSlot);
        }
        if (!this.inventory.getStackInSlot(SLOT_FOCUS).isEmpty()) {
            NBTTagCompound focusSlot = new NBTTagCompound();
            this.inventory.getStackInSlot(SLOT_FOCUS).writeToNBT(focusSlot);
            tags.setTag("focusSlot", focusSlot);
        }


    }

    @Override
    public void readOperationFromNBT(NBTTagCompound tags) {
        if (tags.hasKey("operation")) {
            this.currentOperation = new ReactionChamberOperation(tags);
        } else {
            this.currentOperation = null;
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(Tags.MOD_ID + ".container.reactionchamber");
    }

    @Override
    protected int getNeededPower() {
        return 0;
    }

    @Override
    public void update() {
        if (!world.isRemote && this.fluidsChanged) {
            this.fluidsChanged = false;
            this.needFluidUpdate();
        }
        if (this.isRedstoneEnabled()) {
            ReactionChamberOperation currentReaction = this.getCurrentReaction();
            if (currentReaction != null && currentReaction.getRecipe() != null) {

                this.progress++;
                int rfCost = TGConfig.general.machinesNeedNoPower ? 0 : currentReaction.getRecipe().RFTick;
                boolean state = currentReaction.tick(this.intensity, this.world.isRemote, this, rfCost);

                if (!this.world.isRemote && state) {
                    if (currentReaction.isSuccess()) {

                        if (!this.world.isRemote) {
                            this.finishedOperation();
                        }
                        this.progress = 0;
                        this.totaltime = 0;
                        this.currentOperation = null;

                        if (!this.world.isRemote) {
                            checkAndStartOperation();
                        }

                    } else if (currentReaction.isFailure(this)) {

                        RiskType risk = currentReaction.getRecipe().risk;

                        this.progress = 0;
                        this.totaltime = 0;
                        this.currentOperation = null;

                        if (risk == RiskType.EXPLOSION_LOW) {
                            this.explode(0);
                        } else if (risk == RiskType.EXPLOSION_MEDIUM) {
                            this.explode(1);
                        }


                        if (!RiskType.isFatal(risk)) {
                            if (!this.world.isRemote) {
                                checkAndStartOperation();
                            }
                        }

                    }
                    this.needUpdate();
                }
                if (this.world.isRemote) {
                    if (this.getCurrentReaction().nextTick == ReactionChamberOperation.RECIPE_TICKRATE - 1) {
                        this.playReactionTickSound(currentReaction.required_intensity == this.intensity);
                    }
                }

            } else {

                if (!this.world.isRemote) {
                    checkAndStartOperation();
                    if (this.currentOperation != null) {
                        this.needUpdate();
                    }
                }

            }
        }
    }

    @Override
    protected void checkAndStartOperation() {

        ItemStack inputStack = this.inventory.getStackInSlot(SLOT_INPUT);

        ReactionChamberRecipe rec = ReactionChamberRecipe.getMatchingRecipe(
                inputStack,
                this.inventory.getStackInSlot(SLOT_FOCUS),
                this.inputTank.getFluid(),
                this.intensity
        );

        if (rec != null) {
            if (inputStack.getCount() >= rec.input.stackSize) {
                if (TGConfig.general.machinesNeedNoPower || rec.RFTick <= this.energy.getEnergyStored()) {
                    int count = rec.input.stackSize;
                    this.currentOperation = new ReactionChamberOperation(rec, this);
                    this.input.consume(count);
                    this.totaltime = rec.ticks * ReactionChamberOperation.RECIPE_TICKRATE;
                    this.progress = 0;
                }
            }
        }
    }

    @Override
    protected void finishedOperation() {
        this.mergeOutput();
    }

    private void mergeOutput() {
        ArrayList<ItemStack> outputs = this.getCurrentReaction().getRecipe().outputs;

        for (ItemStack itemStack : outputs) {
            ItemStack output = itemStack.copy();


            int leftover = output.getCount();
            int s = SLOT_OUTPUT;
            ItemStack out = ItemStack.EMPTY;
            while (leftover > 0 && s < SLOT_OUTPUT + OUTPUT_SLOTS_COUNT) {
                out = this.inventory.insertItemNoCheck(s, output, false);
                if (!out.isEmpty()) {
                    leftover = out.getCount();
                    if (leftover > 0) {
                        output = out;
                        s++;
                    }
                } else {
                    leftover = 0;
                }
            }

            if (leftover > 0) {
                this.world.spawnEntity(new EntityItem(this.world, this.pos.getX() + 0.5d, this.pos.getY() + 0.5d, this.pos.getZ() + 0.5d, out));
            }

        }

        if (this.getCurrentReaction().getRecipe().liquidConsumtion > 0) {
            ((ReactionChamberFluidTank) this.inputTank).removeInternal(this.getCurrentReaction().getRecipe().liquidConsumtion);
        }

    }

    @Override
    protected void playAmbientSound() {
    }

    protected void playReactionTickSound(boolean goodTick) {

        ItemStack focusitem = this.inventory.getStackInSlot(SLOT_FOCUS);
        BlockPos pos = this.pos.offset(multiblockDirection);

        if (!focusitem.isEmpty()) {
            ReactionBeamFocus focus = ReactionBeamFocus.getBeamFocus(focusitem);
            if (focus != null) {
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), focus.getSound(), SoundCategory.BLOCKS, 1.0F, 1.0F, true);
            }
        }

        Random rng = new Random();
        for (int i = 0; i < 4; i++) {
            world.spawnParticle(EnumParticleTypes.SPELL, pos.getX() + rng.nextFloat(), pos.getY() + rng.nextFloat(), pos.getZ() + rng.nextFloat(), 0, 1, 0);
        }

        if (goodTick) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), TGSounds.REACTION_CHAMBER_BEEP, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
        } else {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), TGSounds.REACTION_CHAMBER_WARNING, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
        }
    }

    @Override
    protected MultiBlockMachine<EnumMultiBlockMachineType> getMachineBlockType() {
        return TGBlocks.MULTIBLOCK_MACHINE;
    }

    @Override
    public AxisAlignedBB getBBforSlave(BlockPos slavePos) {
        if (multiblockDirection != null) {
            BlockPos center = this.getPos().offset(multiblockDirection, 1);
            SlavePos sp = new SlavePos(slavePos, center);
            AxisAlignedBB bb = ReactionChamberDefinition.boundingBoxes.get(sp);
            if (bb != null) {
                return bb;
            }
        }
        return Block.FULL_BLOCK_AABB;
    }

    public int getValidSlotForItemInMachine(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ITGSpecialSlot itm) {
            if (itm.getSlot(stack) == TGSlotType.REACTION_CHAMBER_FOCUS && ReactionBeamFocus.getBeamFocus(stack) != null) {
                return SLOT_FOCUS;
            }
        }
        return SLOT_INPUT;
    }

    @Override
    public boolean consumePower(int amount) {
        return super.consumePower(amount);
    }

    public byte getIntensity() {
        return intensity;
    }

    public void setIntensity(byte intensity) {
        this.intensity = (byte) Math.max(MIN_INTENSITY, Math.min(intensity, MAX_INTENSITY));
    }

    public int getFluidLimit() {
        return fluidLimit;
    }

    public void setFluidLimit(int fluidLimit) {
        this.fluidLimit = Math.max(0, Math.min(fluidLimit, CAPACITY_INPUT_TANK));
    }

    public ReactionChamberOperation getCurrentReaction() {
        if (this.currentOperation == null) {
            return null;
        }
        return (ReactionChamberOperation) this.currentOperation;
    }

    protected void explode(int type) {
        EnumFacing dir = this.multiblockDirection;

        if (!this.world.isRemote) {

            Block fluidBlock = null;
            if (this.inputTank.getFluid() != null) {
                fluidBlock = this.inputTank.getFluid().getFluid().getBlock();
            }

            BlockPos masterPos = this.getPos().toImmutable();
            BlockPos centerPos = masterPos.offset(dir).up();

            this.onMultiBlockBreak();

            this.world.setBlockToAir(centerPos);

            ArrayList<BlockPos> blocksToRemove = new ArrayList<>();

            if (type == 0) {

                blocksToRemove.add(centerPos.offset(EnumFacing.SOUTH));
                blocksToRemove.add(centerPos.offset(EnumFacing.NORTH));
                blocksToRemove.add(centerPos.offset(EnumFacing.WEST));
                blocksToRemove.add(centerPos.offset(EnumFacing.EAST));

            } else if (type == 1) {
                BlockPos pos = masterPos.toImmutable();

                EnumFacing left = dir.rotateY();
                BlockPos pos1 = pos.offset(left).up();

                BlockPos pos2 = pos.offset(left.getOpposite()).offset(dir, 2).up(2);

                BlockPos.getAllInBox(pos1, pos2).forEach(blocksToRemove::add);
            }

            if (type == 0 && fluidBlock != null) {
                this.world.setBlockState(centerPos, fluidBlock.getDefaultState(), 3);
            } else if (type == 0) {
                this.world.setBlockState(centerPos, Blocks.AIR.getDefaultState(), 3);
            }

            blocksToRemove.forEach(b -> this.world.setBlockState(b, Blocks.AIR.getDefaultState(), 3));


            if (type == 0) {

                TGPackets.wrapper.sendToAllAround(new PacketSpawnParticle("FragGrenadeExplosion", centerPos.getX() + 0.5d, centerPos.getY() + 0.5d, centerPos.getZ() + 0.5d), new TargetPoint(this.world.provider.getDimension(), centerPos.getX() + 0.5d, centerPos.getY() + 0.5d, centerPos.getZ() + 0.5d, 50.0f));
                this.world.playSound(null, centerPos.getX() + 0.5d, centerPos.getY() + 0.5d, centerPos.getZ() + 0.5d, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

            } else if (type == 1) {
                this.world.createExplosion(null, centerPos.getX() + 0.5d, centerPos.getY() + 0.5d, centerPos.getZ() + 0.5d, 4, true);
            }

            if (type == 1 && fluidBlock != null) {
                this.world.setBlockState(centerPos, fluidBlock.getDefaultState(), 3);
                this.world.setBlockState(centerPos.up(), Blocks.AIR.getDefaultState(), 3);
                this.world.setBlockState(centerPos.down(), Blocks.GRAVEL.getDefaultState(), 3);
            }

            this.energy.setEnergyStored(0);
            this.inputTank.setFluid(null);

            ItemStack content = this.input.get();
            if (!content.isEmpty()) {
                this.inventory.setStackInSlot(SLOT_INPUT, ItemStack.EMPTY);
                this.world.spawnEntity(new EntityItem(this.world, centerPos.getX() + 0.5d, centerPos.getY() + 0.5d, centerPos.getZ() + 0.5d, content));
            }

            this.needUpdate();
        }
    }

    @Override
    public void buttonClicked(int id, EntityPlayer ply, String data) {
        if (id <= ButtonConstants.BUTTON_ID_REDSTONE) {
            super.buttonClicked(id, ply, data);
        } else {
            if (this.isUseableByPlayer(ply)) {
                switch (id) {
                    case BUTTON_ID_INTENSITY_INC:
                        this.setIntensity((byte) Integer.parseInt(data));
                        break;
                    case BUTTON_ID_INTENSITY_DEC:
                        this.setIntensity(MIN_INTENSITY);
                        break;
                    case BUTTON_ID_DUMPTANK: //drain input tank
                        dumpLiquid();
                        break;
                    case BUTTON_ID_SET_FLUID_LIMIT:
                        this.setFluidLimit(Integer.parseInt(data));
                        this.needUpdate();
                        break;
                }
            }
        }
    }

    public void dumpLiquid() {
        this.inputTank.setFluid(null);
        this.needUpdate();
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        if (this.formed) {
            if (pass == 1 && this.inputTank.getFluidAmount() > 0) {
                return true;
            }
            return pass == 0 && this.isWorking(); //return super.shouldRenderInPass(pass);
        }
        return false;
    }

    @Override
    public void saveTanksToNBT(NBTTagCompound tags) {
        NBTTagCompound inputTankTags = new NBTTagCompound();
        this.inputTank.writeToNBT(inputTankTags);
        tags.setTag("inputTank", inputTankTags);
    }

    @Override
    public void loadTanksFromNBT(NBTTagCompound tags) {
        NBTTagCompound inputTank = tags.getCompoundTag("inputTank");
        this.inputTank.readFromNBT(inputTank);
    }

    public void needFluidUpdate() {
        if (!this.world.isRemote) {
            ChunkPos cp = this.world.getChunk(getPos()).getPos();
            PlayerChunkMapEntry entry = ((WorldServer) this.world).getPlayerChunkMap().getEntry(cp.x, cp.z);
            if (entry != null) {
                List<EntityPlayerMP> players = entry.players;
                IMessage packet = new PacketUpdateTileEntTanks(this, this.getPos());
                for (EntityPlayerMP entityplayermp : players) {
                    TGPackets.wrapper.sendTo(packet, entityplayermp);
                }
            }
        }
    }

    @Override
    public void unform() {
        if (!this.world.isRemote && this.inventory != null && this.multiblockDirection != null) {
            BlockPos dropPos = this.pos.offset(this.multiblockDirection.getOpposite());
            for (int i = 0; i < this.inventory.getSlots(); i++) {
                ItemStack stack = this.inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    this.world.spawnEntity(new EntityItem(this.world, dropPos.getX() + 0.5, dropPos.getY() + 0.5, dropPos.getZ() + 0.5, stack));
                    this.inventory.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
        super.unform();
    }

    /* OpenComputers Integration */
    @Override
    @Optional.Method(modid = "opencomputers")
    public String getComponentName() {
        return "tg2_reactionchamber";
    }

    @Optional.Method(modid = "opencomputers")
    @Callback(doc = "function(int:intensity):boolean -- sets the intensity level", direct = false)
    public Object[] setIntensity(Context context, Arguments args) {
        if (args.count() < 1)
            return new Object[]{false, "no intensity level specified"};

        byte newIntensity = (byte) Math.max(MIN_INTENSITY, Math.min(args.checkInteger(0), MAX_INTENSITY));

        setIntensity(newIntensity);
        return new Object[]{getIntensity() == newIntensity};
    }

    @Optional.Method(modid = "opencomputers")
    @Callback(doc = "function():int -- returns the intensity level", direct = false)
    public Object[] getIntensity(Context context, Arguments args) {
        return new Object[]{getIntensity()};
    }

    @Optional.Method(modid = "opencomputers")
    @Callback(doc = "function():boolean -- dumps the liquid in the input tank", direct = false)
    public Object[] dumpLiquid(Context context, Arguments args) {
        dumpLiquid();
        return new Object[]{inputTank.getFluid() == null};
    }

    @Optional.Method(modid = "opencomputers")
    @Callback(doc = "function():boolean -- returns the stored energy", direct = false)
    public Object[] getEnergyStored(Context context, Arguments args) {
        return new Object[]{getEnergyStorage().getEnergyStored()};
    }

    @Optional.Method(modid = "opencomputers")
    @Callback(doc = "function():boolean -- returns the size of the internal energy buffer", direct = false)
    public Object[] getMaxEnergyStored(Context context, Arguments args) {
        return new Object[]{getEnergyStorage().getMaxEnergyStored()};
    }

    @Optional.Method(modid = "opencomputers")
    @Callback(doc = "function():table -- returns statistics about the current reaction", direct = false)
    public Object[] getReactionStats(Context context, Arguments args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("validRecipe", getCurrentReaction() != null);

        if (getCurrentReaction() != null) {
            data.put("preferedIntensity", getCurrentReaction().getCurrentPreferedIntensity());
            data.put("progress", progress);
            data.put("duration", totaltime);
            data.put("energyPerTick", TGConfig.general.machinesNeedNoPower ? 0 : getCurrentReaction().getPowerPerTick());
        }

        return new Object[]{data};
    }

    protected static class ReactionChamberFluidTank extends FluidTankPlus {

        protected ReactionChamberTileEntMaster tile;

        public ReactionChamberFluidTank(ReactionChamberTileEntMaster tile, int capacity) {
            super(tile, capacity);
            this.setTileEntity(tile);
            this.tile = tile;
            this.setCanDrain(false);
        }

        private int getFillCapacity() {
            int limit = tile != null ? tile.getFluidLimit() : 0;
            return limit > 0 ? Math.min(limit, CAPACITY_INPUT_TANK) : CAPACITY_INPUT_TANK;
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            tile.fluidsChanged = true;
        }

        @Override
        public int fillInternal(FluidStack resource, boolean doFill) {
            int fillCapacity = getFillCapacity();
            if (resource == null || resource.amount <= 0) {
                return 0;
            }

            if (!doFill) {
                if (fluid == null) {
                    return Math.min(fillCapacity, resource.amount);
                }

                if (!fluid.isFluidEqual(resource)) {
                    return 0;
                }

                return Math.max(0, Math.min(fillCapacity - fluid.amount, resource.amount));
            }

            if (fluid == null) {
                fluid = new FluidStack(resource, Math.min(fillCapacity, resource.amount));

                onContentsChanged();

                if (tile != null) {
                    FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, fluid.amount));
                }
                return fluid.amount;
            }

            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }
            int filled = fillCapacity - fluid.amount;

            if (filled <= 0) {
                return 0;
            }

            if (resource.amount < filled) {
                fluid.amount += resource.amount;
                filled = resource.amount;
            } else {
                fluid.amount = fillCapacity;
            }

            if (filled > 0) {
                onContentsChanged();
            }

            if (tile != null) {
                FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, filled));
            }
            return filled;
        }

        /**
         * remove passed amount from the tank
         *
         * @param amount
         */
        void removeInternal(int amount) {
            if (fluid != null) {
                fluid.amount -= amount;
                if (fluid.amount <= 0) {
                    fluid = null;
                }
            }
        }

        @Override
        public FluidStack drainInternal(int maxDrain, boolean doDrain) {
            if (fluid == null || maxDrain <= 0) {
                return null;
            }
            int maxDrainAmount = Math.max(fluid.amount - CAPACITY_INPUT_TANK, 0);
            int drainAmount = Math.min(maxDrainAmount, maxDrain);

            if (drainAmount <= 0) {
                return null;
            }

            int drained = drainAmount;
            if (fluid.amount < drained) {
                drained = fluid.amount;
            }

            FluidStack stack = new FluidStack(fluid, drained);
            if (doDrain) {
                fluid.amount -= drained;
                if (fluid.amount <= 0) {
                    fluid = null;
                }

                this.onContentsChanged();

                if (tile != null) {
                    FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorld(), tile.getPos(), this, drained));
                }
            }
            return stack;
        }

    }

}
