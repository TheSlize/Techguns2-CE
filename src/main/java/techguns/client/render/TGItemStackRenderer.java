package techguns.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.api.render.IItemRenderer;

@SideOnly(Side.CLIENT)
public class TGItemStackRenderer extends TileEntityItemStackRenderer {

    public static final TGItemStackRenderer INSTANCE = new TGItemStackRenderer();

    private TGItemStackRenderer() {}

    @Override
    public void renderByItem(ItemStack stack) {
        IItemRenderer renderer = ItemRenderHack.getRendererForItem(stack.getItem());
        if (renderer == null) {
            return;
        }

        ItemCameraTransforms.TransformType transform = TGItemRendererContext.getTransformType();
        EntityLivingBase entity = TGItemRendererContext.getEntity(transform);
        boolean leftHand = TGItemRendererContext.isLeftHand(transform);

        if (leftHand) {
            GlStateManager.scale(-1.0F, 1.0F, 1.0F);
        }

        if (transform == ItemCameraTransforms.TransformType.GROUND && !TGItemRendererContext.isInternalRender()) {
            ItemPhysicCompat.applyGroundRotationFix(stack);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableLighting();

        renderer.renderItem(transform, stack, entity, leftHand);

        TGItemRendererContext.clearTransform();
    }
}
