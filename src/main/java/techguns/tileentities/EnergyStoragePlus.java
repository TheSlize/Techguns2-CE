package techguns.tileentities;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStoragePlus extends EnergyStorage {

    public EnergyStoragePlus(int capacity) {
        super(capacity, capacity, 0);
    }

    public void setEnergyStored(int value) {
        this.energy = value;
    }

    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        if (maxExtract <= 0) {
            return 0;
        }
        int extracted = Math.min(this.energy, maxExtract);
        if (!simulate) {
            this.energy -= extracted;
        }
        return extracted;
    }

}
