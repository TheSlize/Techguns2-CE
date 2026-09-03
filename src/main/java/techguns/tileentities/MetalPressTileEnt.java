package techguns.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import techguns.TGFluids;
import techguns.TGItems;
import techguns.TGSounds;
import techguns.gui.ButtonConstants;
import techguns.tileentities.operation.ItemStackHandlerPlus;
import techguns.tileentities.operation.MachineOperation;
import techguns.tileentities.operation.MachineSlotItem;
import techguns.tileentities.operation.MetalPressRecipes;

public class MetalPressTileEnt extends BasicMachineTileEnt {

    public static final int SLOT_INPUT1 = 0;
    public static final int SLOT_INPUT2 = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_UPGRADE = 3;
    public static final int BUTTON_ID_AUTOSPLIT = ButtonConstants.BUTTON_ID_REDSTONE + 1;
    public static final int POWER_PER_TICK = 20;
    protected static final float SOUND_VOLUME = 0.5f;
    public static final int STEAM_CAPACITY = 16 * Fluid.BUCKET_VOLUME;
    protected static final int MAX_PRESSURE_LEVEL = 12;
    protected static final int PRESSURE_TICKS_PER_LEVEL = 40;
    public MachineSlotItem input1;
    public MachineSlotItem input2;
    protected int pressureLevel = 0;
    protected int pressureTickCounter = 0;
    protected final FluidTank steamTank = new FluidTank(STEAM_CAPACITY) {
        {
            this.setCanDrain(false);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return MetalPressTileEnt.this.canAcceptSteam(fluid);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (!MetalPressTileEnt.this.canAcceptSteam(resource)) {
                return 0;
            }

            FluidStack normalized = MetalPressTileEnt.this.normalizeSteamStack(resource);
            int filled = super.fill(normalized, doFill);
            if (filled > 0 && doFill) {
                MetalPressTileEnt.this.onSteamChanged();
            }
            return filled;
        }

        @Override
        public FluidStack drainInternal(int maxDrain, boolean doDrain) {
            FluidStack drained = super.drainInternal(maxDrain, doDrain);
            if (drained != null && doDrain) {
                MetalPressTileEnt.this.onSteamChanged();
            }
            return drained;
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            MetalPressTileEnt.this.markDirty();
            MetalPressTileEnt.this.setContentsChanged(true);
        }
    };
    byte autoSlitMode = 0;

    public MetalPressTileEnt() {
        super(4, true, 20000);
        input1 = new MachineSlotItem(this, SLOT_INPUT1);
        input2 = new MachineSlotItem(this, SLOT_INPUT2);

        this.inventory = new ItemStackHandlerPlus(4) {

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setContentsChanged(true);
            }

            @Override
            protected boolean allowItemInSlot(int slot, ItemStack stack) {
                switch (slot) {
                    case SLOT_INPUT1:
                    case SLOT_INPUT2:
                        return isItemValidForSlot(slot, stack);
                    case SLOT_OUTPUT:
                        return false;
                    case SLOT_UPGRADE:
                        return TGItems.isMachineUpgrade(stack) || TGItems.isSteamUpgrade(stack);
                }
                return false;
            }

            @Override
            protected boolean allowExtractFromSlot(int slot, int amount) {
                return slot == SLOT_OUTPUT;
            }
        };

    }

    private FluidStack normalizeSteamStack(FluidStack stack) {
        if (stack == null) {
            return null;
        }
        Fluid canonical = this.getCanonicalSteamFluid();
        if (canonical == null) {
            return stack;
        }
        Fluid incoming = stack.getFluid();
        if (incoming != null && incoming != canonical && "steam".equalsIgnoreCase(incoming.getName())) {
            FluidStack converted = new FluidStack(canonical, stack.amount);
            if (stack.tag != null) {
                converted.tag = stack.tag.copy();
            }
            return converted;
        }
        return stack;
    }

    @Nullable
    private Fluid getCanonicalSteamFluid() {
        Fluid steam = TGFluids.STEAM;
        if (steam == null) {
            steam = FluidRegistry.getFluid("steam");
        }
        return steam;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("techguns.container.metalpress");
    }

    @Override
    public void readClientDataFromNBT(NBTTagCompound tags) {
        super.readClientDataFromNBT(tags);
        this.autoSlitMode = tags.getByte("autoSplitMode");
        if (tags.hasKey("steamTank")) {
            this.steamTank.readFromNBT(tags.getCompoundTag("steamTank"));
        } else {
            this.steamTank.setFluid(null);
        }
        this.pressureLevel = tags.getInteger("pressureLevel");
    }

    @Override
    public void writeClientDataToNBT(NBTTagCompound tags) {
        super.writeClientDataToNBT(tags);
        tags.setByte("autoSplitMode", this.autoSlitMode);
        tags.setTag("steamTank", this.steamTank.writeToNBT(new NBTTagCompound()));
        tags.setInteger("pressureLevel", this.pressureLevel);
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("SteamTank")) {
            this.steamTank.readFromNBT(compound.getCompoundTag("SteamTank"));
        } else {
            this.steamTank.setFluid(null);
        }
        this.pressureLevel = compound.getInteger("PressureLevel");
        this.pressureTickCounter = compound.getInteger("PressureTickCounter");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("SteamTank", this.steamTank.writeToNBT(new NBTTagCompound()));
        compound.setInteger("PressureLevel", this.pressureLevel);
        compound.setInteger("PressureTickCounter", this.pressureTickCounter);
        return compound;
    }

    @Override
    protected int getNeededPower() {
        if (this.currentOperation != null) {
            return POWER_PER_TICK * this.currentOperation.getStackMultiplier();
        }
        return 0;
    }

    protected void splitSlot(MachineSlotItem src, MachineSlotItem target) {
        int amount = src.get().getCount();
        int amount2 = amount / 2;
        int amount1 = amount - amount2;

        ItemStack targetStack = src.get().copy();
        src.get().setCount(amount1);
        targetStack.setCount(amount2);
        target.setStackInSlot(targetStack);
    }

    @Override
    protected void checkAndStartOperation() {

        //check for stackSplit
        if (this.autoSlitMode > 0) {
            if (!this.input1.get().isEmpty() && this.input2.get().isEmpty() && this.input1.get().getCount() > 1) {
                if (!MetalPressRecipes.getOutputFor(this.input1.get(), this.input1.get()).isEmpty()) {
                    this.splitSlot(input1, input2);
                }
            } else if (this.input1.get().isEmpty() && !this.input2.get().isEmpty() && this.input2.get().getCount() > 1) {
                if (!MetalPressRecipes.getOutputFor(this.input2.get(), this.input2.get()).isEmpty()) {
                    this.splitSlot(input2, input1);
                }
            }
        }

        ItemStack stackInput1 = this.input1.get();
        ItemStack stackInput2 = this.input2.get();

        MetalPressRecipes.MetalPressRecipe matchedRecipe = MetalPressRecipes.getRecipeForInputs(stackInput1, stackInput2);
        ItemStack output = matchedRecipe != null ? matchedRecipe.output : ItemStack.EMPTY;

        this.setContentsChanged(false);

        if (matchedRecipe == null || output.isEmpty() || !canOutput(output, SLOT_OUTPUT)) {
            return;
        }

        boolean requiresSteam = matchedRecipe.requiresSteam();
        if (requiresSteam) {
            if (!this.hasSteamUpgrade()) {
                return;
            }
            if (this.pressureLevel < matchedRecipe.requiredPressure) {
                return;
            }
        }

        int required1 = matchedRecipe.input1Count;
        int required2 = matchedRecipe.input2Count;

        int maxMultiplier = this.getMaxMachineUpgradeMultiplier(SLOT_UPGRADE);
        int inputMultiplier = Math.min(stackInput1.getCount() / required1, stackInput2.getCount() / required2);
        int outputMultiplier = 1;
        ItemStack stackInOutputSlot = this.inventory.getStackInSlot(SLOT_OUTPUT);
        if (stackInOutputSlot.isEmpty()) {
            outputMultiplier = this.inventory.getSlotLimit(SLOT_OUTPUT) / output.getCount();
        } else if (stackInOutputSlot.isItemEqual(output)) {
            outputMultiplier = (stackInOutputSlot.getMaxStackSize() - stackInOutputSlot.getCount()) / output.getCount();
        }

        int ioMultiplier = Math.min(inputMultiplier, outputMultiplier);
        int multiplier = Math.min(ioMultiplier, maxMultiplier);

        if (requiresSteam && matchedRecipe.steamCost > 0) {
            int availableSteam = this.steamTank.getFluidAmount();
            int maxBySteam = availableSteam / matchedRecipe.steamCost;
            if (maxBySteam <= 0) {
                return;
            }
            multiplier = Math.min(multiplier, maxBySteam);
        }

        if (multiplier <= 0) {
            return;
        }

        if (requiresSteam && matchedRecipe.steamCost > 0) {
            int totalSteamCost = matchedRecipe.steamCost * multiplier;
            FluidStack simulatedDrain = this.steamTank.drainInternal(totalSteamCost, false);
            if (simulatedDrain == null || simulatedDrain.amount < totalSteamCost) {
                return;
            }
            this.steamTank.drainInternal(totalSteamCost, true);
        }

        this.input1.consume(multiplier * required1);
        this.input2.consume(multiplier * required2);

        this.progress = 0;
        this.totaltime = 100;

        ItemStack input1Copy = this.input1.getTypeWithSize(1);
        ItemStack input2Copy = this.input2.getTypeWithSize(1);

        this.currentOperation = new MachineOperation(output, input1Copy, input2Copy);
        this.currentOperation.setStackMultiplier(multiplier);

        if (!this.world.isRemote) {
            this.needUpdate();
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.world != null && !this.world.isRemote) {
            tickPressure();
        }
    }

    @Override
    protected void finishedOperation() {
        if (this.inventory.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
            this.inventory.setStackInSlot(SLOT_OUTPUT, currentOperation.getItemOutput0());
        } else {
            this.inventory.insertItemNoCheck(SLOT_OUTPUT, currentOperation.getItemOutput0(), false);
        }
    }

    @Override
    protected void playAmbientSound() {
        int soundTick1 = Math.round((float) this.totaltime * 0.075f);
        int halfTime = (Math.round((float) totaltime * 0.5f));

        if (this.progress == soundTick1 || this.progress == soundTick1 + halfTime) {
            world.playSound(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), TGSounds.METAL_PRESS_WORK, SoundCategory.BLOCKS, SOUND_VOLUME, 1.0F, true);
        }

        if (this.hasSteamUpgrade()) {
            if (this.progress == soundTick1 + 10 || this.progress == soundTick1 + halfTime + 10) {
                world.playSound(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, SOUND_VOLUME, 1.0F, true);
            }
        }
    }

    public byte getAutoSplitMode() {
        return this.autoSlitMode;
    }

    public void setAutoSplitMode(byte mode) {
        this.autoSlitMode = mode;
    }

    public boolean isItemValidForSlot(int slot, ItemStack item) {

        boolean empty1 = this.input1.get().isEmpty();
        boolean empty2 = this.input2.get().isEmpty();

        if (empty1 && empty2) {
            return MetalPressRecipes.hasRecipeUsing(item);
        } else if (empty1 && slot == SLOT_INPUT1) {
            return !MetalPressRecipes.getOutputFor(item, this.input2.get()).isEmpty();
        } else if (empty2 && slot == SLOT_INPUT2) {
            return !MetalPressRecipes.getOutputFor(this.input1.get(), item).isEmpty();
        } else {
            return this.getInventory().getStackInSlot(slot).isItemEqual(item);
        }
    }

    public int getValidSlotForItemInMachine(ItemStack item) {
        if (!this.input1.get().isEmpty() && OreDictionary.itemMatches(this.input1.get(), item, true)) {
            return SLOT_INPUT1;
        } else if (!this.input2.get().isEmpty() && OreDictionary.itemMatches(this.input2.get(), item, true)) {
            return SLOT_INPUT2;
        } else if (this.input1.get().isEmpty() && MetalPressRecipes.hasRecipeUsing(item)) {
            return SLOT_INPUT1;
        } else if (!this.input1.get().isEmpty() && this.input2.get().isEmpty() && (!MetalPressRecipes.getOutputFor(this.input1.get(), item).isEmpty())) {
            return SLOT_INPUT2;
        } else if (TGItems.isMachineUpgrade(item) || TGItems.isSteamUpgrade(item)) {
            return SLOT_UPGRADE;
        }
        return -1;
    }

    @Override
    public void buttonClicked(int id, EntityPlayer ply, String data) {
        if (id == BUTTON_ID_AUTOSPLIT) {
            if (this.isUseableByPlayer(ply)) {
                this.changeAutoSplitMode();
            }
        } else {
            super.buttonClicked(id, ply, data);
        }
    }

    public void changeAutoSplitMode() {
        if (this.autoSlitMode == 0) {
            this.autoSlitMode = 1;
        } else {
            this.autoSlitMode = 0;
        }
        this.setContentsChanged(true);
    }

    protected void tickPressure() {
        if (this.world == null || this.world.isRemote) {
            return;
        }

        if (!this.hasSteamUpgrade()) {
            if (this.steamTank.getFluidAmount() > 0) {
                this.steamTank.setFluid(null);
            }
            if (this.pressureLevel != 0 || this.pressureTickCounter != 0) {
                this.pressureLevel = 0;
                this.pressureTickCounter = 0;
                this.markDirty();
                this.setContentsChanged(true);
                this.needUpdate();
            }
            return;
        }

        int maxPressureByFluid = Math.min(MAX_PRESSURE_LEVEL, this.steamTank.getFluidAmount() / Fluid.BUCKET_VOLUME);
        boolean changed = false;

        if (this.pressureLevel > maxPressureByFluid) {
            this.pressureLevel = maxPressureByFluid;
            this.pressureTickCounter = 0;
            changed = true;
        }

        if (maxPressureByFluid == 0) {
            if (this.pressureLevel != 0) {
                this.pressureLevel = 0;
                changed = true;
            }
            if (this.pressureTickCounter != 0) {
                this.pressureTickCounter = 0;
                changed = true;
            }
        } else if (this.pressureLevel < maxPressureByFluid) {
            this.pressureTickCounter++;
            if (this.pressureTickCounter >= PRESSURE_TICKS_PER_LEVEL) {
                this.pressureTickCounter = 0;
                this.pressureLevel++;
                if (this.pressureLevel > maxPressureByFluid) {
                    this.pressureLevel = maxPressureByFluid;
                }
                changed = true;
            }
        } else if (this.pressureTickCounter != 0) {
            this.pressureTickCounter = 0;
        }

        if (changed) {
            this.markDirty();
            this.setContentsChanged(true);
            this.needUpdate();
        }
    }

    protected void onSteamChanged() {
        if (this.world == null || this.world.isRemote) {
            return;
        }

        int maxPressureByFluid = Math.min(MAX_PRESSURE_LEVEL, this.steamTank.getFluidAmount() / Fluid.BUCKET_VOLUME);
        if (this.pressureLevel > maxPressureByFluid) {
            this.pressureLevel = maxPressureByFluid;
            this.pressureTickCounter = 0;
        }

        this.markDirty();
        this.setContentsChanged(true);
        this.needUpdate();
    }

    protected boolean canAcceptSteam(@Nullable FluidStack fluid) {
        if (fluid == null || !this.hasSteamUpgrade()) {
            return false;
        }
        Fluid incoming = fluid.getFluid();
        if (incoming == null) {
            return false;
        }

        Fluid canonical = getCanonicalSteamFluid();
        if (canonical != null && incoming == canonical) {
            return true;
        }

        String name = incoming.getName();
        return name != null && name.equalsIgnoreCase("steam");
    }

    public boolean hasSteamUpgrade() {
        ItemStack upgrade = this.inventory.getStackInSlot(SLOT_UPGRADE);
        return !upgrade.isEmpty() && TGItems.isSteamUpgrade(upgrade);
    }

    public int getPressureLevel() {
        return this.pressureLevel;
    }

    public FluidTank getSteamTank() {
        return this.steamTank;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.hasSteamUpgrade()) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.hasSteamUpgrade()) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.steamTank);
        }
        return super.getCapability(capability, facing);
    }


}
