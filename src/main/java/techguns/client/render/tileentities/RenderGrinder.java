package techguns.client.render.tileentities;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import techguns.TGBlocks;
import techguns.*;
import techguns.api.render.IItemTGRenderer;
import techguns.client.render.TGItemRendererContext;
import techguns.items.guns.GenericGun;
import techguns.tileentities.GrinderTileEnt;

public class RenderGrinder extends TileEntitySpecialRenderer<GrinderTileEnt> {

    protected ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/blocks/grinder_roll.png");
    protected static final double UNIT = 0.0625;

    protected static final float TEX_X = (float) (2 * UNIT);
    protected static final float TEX_Z = (float) (2 * UNIT);
    protected static double len = 12 * UNIT;

    @Override
    public void render(GrinderTileEnt te, double x, double y, double z, float partialTicks, int destroyStage,
                       float alpha) {

        //Rotate y
        IBlockState bs = te.getWorld().getBlockState(te.getPos());

        if (bs.getBlock() == TGBlocks.SIMPLE_MACHINE2) {

            EnumFacing f = bs.getValue(BlockHorizontal.FACING);

            float rot = 45f;
            GlStateManager.pushMatrix();

            //translate to position
            GlStateManager.translate(x, y, z);

            if (te.isWorking()) {
                rot = ((45 + (1080 * te.getProgress())) % 360);
            }

            GlStateManager.pushMatrix();

            GlStateManager.rotate(f.getHorizontalAngle(), 0, 1, 0);
            switch (f) {
                case NORTH:
                    GlStateManager.translate(-1, 0, -1);
                    break;
                case EAST:
                    GlStateManager.translate(0, 0, -1);
                    break;
                case WEST:
                    GlStateManager.translate(-1, 0, 0);
                    break;
                default:
                    break;

            }

            Minecraft.getMinecraft().renderEngine.bindTexture(texture);

            //draw grinder
            BufferBuilder buf = Tessellator.getInstance().getBuffer();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 7.5 * UNIT, 6.5 * UNIT);
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

            GlStateManager.rotate(rot, 1, 0, 0);

            drawGrinderRoll(buf, 1.5f);

            Tessellator.getInstance().draw();
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 7.5 * UNIT, 9.5 * UNIT);
            buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

            GlStateManager.rotate(-rot, 1, 0, 0);
            drawGrinderRoll(buf, 1.5f);

            Tessellator.getInstance().draw();
            GlStateManager.popMatrix();


            //RENDER ITEM
            ItemStack item;
            if (te.isWorking()) {
                item = te.currentOperation.getItemInputI(0);

                if (!item.isEmpty()) {

                    boolean is3d = false;
                    boolean rot90 = false;
                    if (item.getItem() instanceof IItemTGRenderer) {
                        is3d = ((IItemTGRenderer) item.getItem()).shouldUseRenderHack(item);
                        if (item.getItem() instanceof GenericGun) {
                            rot90 = true;
                        }
                    }

                    double px = 0.5d;
                    double py = 0.6d - te.getProgress() * 0.4d + (is3d ? 0.15d : 0);
                    double pz = 0.5d;

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(px, py, pz);

                    if (is3d) {
                        GlStateManager.scale(0.5, 0.5, 0.5);
                    }
                    if (rot90) {
                        GlStateManager.rotate(90f, 0, 1, 0);
                    }

                    TGItemRendererContext.pushInternalRender();
                    Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.GROUND);
                    TGItemRendererContext.popInternalRender();

                    GlStateManager.popMatrix();
                }
            }


            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }

    protected void drawGrinderRoll(BufferBuilder buf, float r) {
        double d = 2 * r;
        r = (float) (UNIT * r);

        //draw top face
        buf.pos((float) 2 * UNIT, r, -r).tex(TEX_X, TEX_Z).normal(0, 1, 0).endVertex();
        buf.pos((float) 2 * UNIT, r, r).tex(TEX_X, TEX_Z + d * UNIT).normal(0, 1, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, r, r).tex(TEX_X + len, TEX_Z + d * UNIT).normal(0, 1, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, r, -r).tex(TEX_X + len, TEX_Z).normal(0, 1, 0).endVertex();

        //draw side face
        buf.pos((float) 2 * UNIT, r, r).tex(TEX_X, TEX_Z).normal(-1, 0, 0).endVertex();
        buf.pos((float) 2 * UNIT, -r, r).tex(TEX_X, TEX_Z + d * UNIT).normal(-1, 0, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, -r, r).tex(TEX_X + len, TEX_Z + d * UNIT).normal(-1, 0, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, r, r).tex(TEX_X + len, TEX_Z).normal(-1, 0, 0).endVertex();

        //draw down face
        buf.pos((float) 2 * UNIT, -r, -r).tex(TEX_X, TEX_Z + d * UNIT).normal(0, -1, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, -r, -r).tex(TEX_X + len, TEX_Z + d * UNIT).normal(0, -1, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, -r, r).tex(TEX_X + len, TEX_Z).normal(0, -1, 0).endVertex();
        buf.pos((float) 2 * UNIT, -r, r).tex(TEX_X, TEX_Z).normal(0, -1, 0).endVertex();

        //draw side face2
        buf.pos((float) 2 * UNIT, r, -r).tex(TEX_X, TEX_Z + d * UNIT).normal(1, 0, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, r, -r).tex(TEX_X + len, TEX_Z + d * UNIT).normal(1, 0, 0).endVertex();
        buf.pos((float) 2 * UNIT + len, -r, -r).tex(TEX_X + len, TEX_Z).normal(1, 0, 0).endVertex();
        buf.pos((float) 2 * UNIT, -r, -r).tex(TEX_X, TEX_Z).normal(1, 0, 0).endVertex();
    }
}