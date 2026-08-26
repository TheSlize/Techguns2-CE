package techguns.tileentities;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.jetbrains.annotations.NotNull;
import techguns.TGBlocks;
import techguns.TGItems;
import techguns.TGSounds;
import techguns.*;
import techguns.blocks.machines.EnumMultiBlockMachineType;
import techguns.blocks.machines.MultiBlockMachine;
import techguns.tileentities.operation.FabricatorRecipe;
import techguns.tileentities.operation.FabricatorRecipe.RecipeData;
import techguns.tileentities.operation.ItemStackHandlerPlus;
import techguns.tileentities.operation.MachineSlotItem;

public class FabricatorTileEntMaster extends MultiBlockMachineTileEntMaster implements IControlReceiver {

    public static final int SLOT_INPUT1 = 0;
    public static final int SLOT_WIRES = 1;
    public static final int SLOT_POWDER = 2;
    public static final int SLOT_PLATE = 3;
    public static final int SLOT_OUTPUT = 4;
    public static final int SLOT_UPGRADE = 5;
    public static final int powerPerTick = 80;
    protected static final float SOUND_VOLUME = 0.5f;
    public MachineSlotItem input;
    public MachineSlotItem wireslot;
    public MachineSlotItem powderslot;
    public MachineSlotItem plateslot;
    public FabricatorRecipe currentRecipe;

    public FabricatorTileEntMaster() {
        super(6, 100000);

        input = new MachineSlotItem(this, SLOT_INPUT1);
        wireslot = new MachineSlotItem(this, SLOT_WIRES);
        powderslot = new MachineSlotItem(this, SLOT_POWDER);
        plateslot = new MachineSlotItem(this, SLOT_PLATE);

        this.inventory = new ItemStackHandlerPlus(6) {
            @Override
            protected boolean allowItemInSlot(int slot, ItemStack stack) {
                if (!FabricatorTileEntMaster.this.isFormed()) return false;
                return switch (slot) {
                    case SLOT_INPUT1, SLOT_WIRES, SLOT_POWDER, SLOT_PLATE -> isItemValidForRecipeSlot(slot, stack);
                    case SLOT_UPGRADE -> TGItems.isMachineUpgrade(stack);
                    default -> false;
                };
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (!FabricatorTileEntMaster.this.isFormed()) return false;
                return allowItemInSlot(slot, stack);
            }

            @Override
            protected boolean allowExtractFromSlot(int slot, int amount) {
                if (!FabricatorTileEntMaster.this.isFormed()) return false;
                return slot == SLOT_OUTPUT;
            }
        };
    }

    private boolean isItemValidForRecipeSlot(int slot, ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (this.currentRecipe == null) return false;

        return switch (slot) {
            case SLOT_INPUT1 -> this.currentRecipe.inputItem != null
                    && this.currentRecipe.inputItem.isEqualWithOreDict(stack);
            case SLOT_WIRES -> this.currentRecipe.wireSlot != null
                    && !this.currentRecipe.wireSlot.isEmpty()
                    && this.currentRecipe.wireSlot.isEqualWithOreDict(stack);
            case SLOT_POWDER -> this.currentRecipe.powderSlot != null
                    && !this.currentRecipe.powderSlot.isEmpty()
                    && this.currentRecipe.powderSlot.isEqualWithOreDict(stack);
            case SLOT_PLATE -> this.currentRecipe.plateSlot != null
                    && !this.currentRecipe.plateSlot.isEmpty()
                    && this.currentRecipe.plateSlot.isEqualWithOreDict(stack);
            default -> false;
        };
    }

    @Override
    public void writeClientDataToNBT(NBTTagCompound tags) {
        super.writeClientDataToNBT(tags);
        if (this.currentRecipe != null) {
            tags.setString("currentRecipe", this.currentRecipe.recName);
        }
    }

    @Override
    public void readClientDataFromNBT(NBTTagCompound tags) {
        super.readClientDataFromNBT(tags);
        if (tags.hasKey("currentRecipe")) {
            this.currentRecipe = FabricatorRecipe.searchRecByName(tags.getString("currentRecipe"));
        } else {
            this.currentRecipe = null;
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(Tags.MOD_ID + ".container.fabricator");
    }

    @Override
    public int getNeededPower() {
        return powerPerTick;
    }


    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        if (this.isFormed()) {
            BlockPos p = this.getPos();
            EnumFacing left = multiblockDirection.rotateY();

            BlockPos other = p.offset(multiblockDirection, 2).offset(left, 2).offset(EnumFacing.UP, 2);
            BlockPos first = p.offset(multiblockDirection.getOpposite(), 2).offset(left.getOpposite(), 1).offset(EnumFacing.DOWN, 1);
            return new AxisAlignedBB(first, other);
        } else {
            return super.getRenderBoundingBox();
        }
    }

    @Override
    protected void checkAndStartOperation() {
        RecipeData data = FabricatorRecipe.getRecipeDataFor(this.inventory.getStackInSlot(SLOT_INPUT1), this.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_WIRES), this.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_POWDER), this.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_PLATE), this.getMaxMachineUpgradeMultiplier(SLOT_UPGRADE), this.inventory.getStackInSlot(FabricatorTileEntMaster.SLOT_OUTPUT));
        if (data != null) {

            this.currentOperation = data.createOperation(this);
            this.input.consume(data.inputConsumption * data.stackMultiplier);
            this.wireslot.consume(data.wireConsumption * data.stackMultiplier);
            this.powderslot.consume(data.powderConsumption * data.stackMultiplier);
            this.plateslot.consume(data.plateConsumption * data.stackMultiplier);

            this.totaltime = 100;
            this.progress = 0;

            if (!this.world.isRemote) {
                this.needUpdate();
            }
        }
    }

    @Override
    protected void finishedOperation() {

        ItemStack itemOut = this.currentOperation.getItemOutput0();
        if (!itemOut.isEmpty()) {
            if (!this.inventory.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
                //this.inventory.getStackInSlot(SLOT_OUTPUT).grow(itemOut.getCount());
                this.inventory.insertItemNoCheck(SLOT_OUTPUT, itemOut, false);
            } else {
                this.inventory.setStackInSlot(SLOT_OUTPUT, itemOut);
            }
        }
    }

    @Override
    protected void playAmbientSound() {
        if (this.world.isRemote && this.progress == 1) {
            //machines.fabricatorWork
            world.playSound(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), TGSounds.FABRICATOR_WORK, SoundCategory.BLOCKS, SOUND_VOLUME, 1.0F, true);
        }
    }

    @Override
    protected MultiBlockMachine<EnumMultiBlockMachineType> getMachineBlockType() {
        return TGBlocks.MULTIBLOCK_MACHINE;
    }

    @Override
    public AxisAlignedBB getBBforSlave(BlockPos slavePos) {
        return Block.FULL_BLOCK_AABB;
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return this.isUseableByPlayer(player);
    }

    @Override
    public void receiveControl(NBTTagCompound data) {
        if (data.hasKey("index") && data.hasKey("selection")) {
            int index = data.getInteger("index");
            String selection = data.getString("selection");
            if (index == 0) {
                this.currentRecipe = FabricatorRecipe.searchRecByName(selection);
                this.markChanged();
                if (!this.world.isRemote) {
                    this.needUpdate();
                }
            }
        }
    }

    public void markChanged() {
        this.world.markChunkDirty(this.pos, this);
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

}
