package techguns.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.items.armors.GenericArmor;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public final class ItemPhysicCompat {

    private static Boolean loaded = null;
    private static boolean lookupDone = false;
    private static Field configField = null;
    private static Field vanillaRenderingField = null;

    private ItemPhysicCompat() {}

    public static boolean laysItemsFlat() {
        if (loaded == null) {
            loaded = Loader.isModLoaded("itemphysic") || Loader.isModLoaded("itemphysiclite");
        }
        return loaded && !isVanillaRendering();
    }

    public static void applyGroundRotationFix(ItemStack stack) {
        if (!laysItemsFlat()) {
            return;
        }
        GlStateManager.translate(0.5f, 0.5f, 0.5f);
        if(!(stack.getItem() instanceof GenericArmor)) {
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        }
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
    }

    private static boolean isVanillaRendering() {
        if (!lookupDone) {
            lookupDone = true;
            try {
                configField = Class.forName("com.creativemd.itemphysic.ItemDummyContainer").getDeclaredField("CONFIG_RENDERING");
                configField.setAccessible(true);
                vanillaRenderingField = configField.getType().getDeclaredField("vanillaRendering");
                vanillaRenderingField.setAccessible(true);
            } catch (ReflectiveOperationException | LinkageError e) {
                configField = null;
                vanillaRenderingField = null;
            }
        }
        if (configField == null || vanillaRenderingField == null) {
            return false;
        }
        try {
            Object config = configField.get(null);
            return config != null && vanillaRenderingField.getBoolean(config);
        } catch (ReflectiveOperationException | RuntimeException e) {
            return false;
        }
    }
}
