package techguns.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import techguns.TGConfig;

public abstract class BasicPoweredTileEnt extends BasicRedstoneTileEnt {

    protected EnergyStoragePlus energy;

    public BasicPoweredTileEnt(int inventorySize, boolean hasRotation, int maximumPower) {
        super(inventorySize, hasRotation);
        energy = new EnergyStoragePlus(maximumPower);
    }

    public EnergyStoragePlus getEnergyStorage() {
        return energy;
    }

    /**
     * Drain amount power from internal storage, return if enough power left
     *
     * @param amount
     * @return
     */
    protected boolean consumePower(int amount) {
        if (TGConfig.general.machinesNeedNoPower) {
            return true;
        }
        if (this.energy.extractEnergyInternal(amount, true) >= amount) {
            return this.energy.extractEnergyInternal(amount, false) >= amount;
        } else {
            return false;
        }
    }

    @Override
    public void readClientDataFromNBT(NBTTagCompound tags) {
        super.readClientDataFromNBT(tags);
        this.energy.setEnergyStored(tags.getInteger("powerStored"));
    }

    @Override
    public void writeClientDataToNBT(NBTTagCompound tags) {
        super.writeClientDataToNBT(tags);
        tags.setInteger("powerStored", this.energy.getEnergyStored());
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY ? (T) energy : super.getCapability(capability, facing);
    }
}
