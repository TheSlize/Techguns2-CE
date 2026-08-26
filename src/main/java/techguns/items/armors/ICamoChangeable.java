package techguns.items.armors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ICamoChangeable {
    int getCamoCount();

    default void switchCamo(ItemStack item) {
        this.switchCamo(item, false);
    }

    default void switchCamo(ItemStack item, boolean back) {
        ICamoChangeable it = (ICamoChangeable) item.getItem();
        int count = it.getCamoCount();
        if (count <= 0) {
            return;
        }

        NBTTagCompound tags = item.getTagCompound();
        byte camoID = 0;
        if (tags != null && tags.hasKey("camo")) {
            camoID = tags.getByte("camo");
        }

        if (back) {
            camoID--;
        } else {
            camoID++;
        }

        if (camoID >= count) {
            camoID = 0;
        } else if (camoID < 0) {
            camoID = (byte) (count - 1);
        }

        if (camoID == 0) {
            if (tags != null) {
                tags.removeTag("camo");
                if (tags.isEmpty()) {
                    item.setTagCompound(null);
                }
            }
        } else {
            if (tags == null) {
                tags = new NBTTagCompound();
                item.setTagCompound(tags);
            }
            tags.setByte("camo", camoID);
        }
    }

    default int getCurrentCamoIndex(ItemStack item) {
        NBTTagCompound tags = item.getTagCompound();
        byte camoID = 0;
        if (tags != null && tags.hasKey("camo")) {
            camoID = tags.getByte("camo");
        }
        return camoID;
    }

    String getCurrentCamoName(ItemStack item);

    default int getFirstItemCamoDamageValue() {
        return 0;
    }

    default boolean addBlockCamoChangeRecipes() {
        return true;
    }
}
