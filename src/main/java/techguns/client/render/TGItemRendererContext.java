package techguns.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayDeque;
import java.util.Deque;

@SideOnly(Side.CLIENT)
public final class TGItemRendererContext {

    private static final ThreadLocal<ItemCameraTransforms.TransformType> CURRENT_TRANSFORM = ThreadLocal.withInitial(() -> ItemCameraTransforms.TransformType.NONE);
    private static final ThreadLocal<Deque<EntityLivingBase>> ENTITY_STACK = ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<int[]> INTERNAL_RENDER_DEPTH = ThreadLocal.withInitial(() -> new int[1]);

    private TGItemRendererContext() {}

    public static void pushInternalRender() {
        INTERNAL_RENDER_DEPTH.get()[0]++;
    }

    public static void popInternalRender() {
        int[] depth = INTERNAL_RENDER_DEPTH.get();
        if (depth[0] > 0) {
            depth[0]--;
        }
    }

    public static boolean isInternalRender() {
        return INTERNAL_RENDER_DEPTH.get()[0] > 0;
    }

    public static void setTransform(ItemCameraTransforms.TransformType transform) {
        CURRENT_TRANSFORM.set(transform);
    }

    public static ItemCameraTransforms.TransformType getTransformType() {
        return CURRENT_TRANSFORM.get();
    }

    public static void clearTransform() {
        CURRENT_TRANSFORM.set(ItemCameraTransforms.TransformType.NONE);
    }

    public static void pushEntity(EntityLivingBase entity) {
        ENTITY_STACK.get().push(entity);
    }

    public static void popEntity(EntityLivingBase entity) {
        Deque<EntityLivingBase> stack = ENTITY_STACK.get();
        if (stack.isEmpty()) {
            return;
        }
        if (stack.peek() == entity) {
            stack.pop();
        } else {
            stack.remove(entity);
        }
    }

    public static EntityLivingBase getEntity(ItemCameraTransforms.TransformType transform) {
        EntityLivingBase current = ENTITY_STACK.get().peek();
        if (current != null) {
            return current;
        }
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            Entity view = Minecraft.getMinecraft().getRenderViewEntity();
            return view instanceof EntityLivingBase ? (EntityLivingBase) view : null;
        }
        return null;
    }

    public static boolean isLeftHand(ItemCameraTransforms.TransformType transform) {
        return transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
    }
}
