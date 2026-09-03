package techguns.client.render.tileentities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import techguns.*;
import techguns.client.render.TGItemRendererContext;
import techguns.client.render.TGRenderHelper;
import techguns.client.render.TGRenderHelper.RenderType;
import techguns.tileentities.ReactionChamberTileEntMaster;

import java.util.ArrayList;
import java.util.List;

public class RenderReactionChamber extends TileEntitySpecialRenderer<ReactionChamberTileEntMaster> {

    protected static final ResourceLocation heatrayTexture = new ResourceLocation(Tags.MOD_ID, "textures/fx/heatray.png");
    protected static final ResourceLocation uvrayTexture = new ResourceLocation(Tags.MOD_ID, "textures/fx/uvray.png");

    private static final double ONE_SIXTEENTH = 0.0625;

    private static final List<ReactionChamberTileEntMaster> PENDING_BEAMS = new ArrayList<>();

    @Override
    public void render(ReactionChamberTileEntMaster te, double x, double y, double z, float partialTicks,
                       int destroyStage, float alpha) {

        if (te.isFormed()) {
            BlockPos centerPos = te.getPos().offset(te.getMultiblockDirection()).up();

            int pass = net.minecraftforge.client.MinecraftForgeClient.getRenderPass();
            if (pass == 0) {
                ItemStack item;
                if (te.isWorking()) {
                    item = te.getCurrentReaction().getItemInputI(0);
                } else {
                    item = te.input.get();
                }

                if (!item.isEmpty()) {

                    double px = x + (-te.getPos().getX() + centerPos.getX()) + 0.5d;
                    double py = y + (-te.getPos().getY() + centerPos.getY()) + 0.5d;
                    double pz = z + (-te.getPos().getZ() + centerPos.getZ()) + 0.5d;

                    float lastBrightnessX = OpenGlHelper.lastBrightnessX;
                    float lastBrightnessY = OpenGlHelper.lastBrightnessY;
                    int light = te.getWorld().getCombinedLight(centerPos, 0);

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(px, py, pz);
                    GlStateManager.rotate(-90.0f * te.getMultiblockDirection().getOpposite().getHorizontalIndex(), 0f, 1f, 0f);

                    GlStateManager.color(1f, 1f, 1f, 1f);
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    RenderHelper.enableStandardItemLighting();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light % 65536, light / 65536);

                    TGItemRendererContext.pushInternalRender();
                    Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.GROUND);
                    TGItemRendererContext.popInternalRender();

                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.disableRescaleNormal();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
                    GlStateManager.color(1f, 1f, 1f, 1f);

                    GlStateManager.popMatrix();
                }
            } else if (pass == 1) {
                boolean hasBeam = te.isWorking()
                        && !te.getInventory().getStackInSlot(ReactionChamberTileEntMaster.SLOT_FOCUS).isEmpty();

                FluidStack liquid = te.inputTank.getFluid();
                if (liquid != null) {
                    this.renderFluid(te, x, y, z, false);

                    //Write the fluid depth so chambers behind this one get occluded, never in front of an own beam
                    if (!hasBeam) {
                        this.renderFluid(te, x, y, z, true);
                    }
                }

                //Queue the beam, it gets drawn after the world so no other chamber's fluid can blend over it
                if (hasBeam && !PENDING_BEAMS.contains(te)) {
                    PENDING_BEAMS.add(te);
                }
            }
        }

    }

    @SuppressWarnings("incomplete-switch")
    private void renderFluid(ReactionChamberTileEntMaster te, double x, double y, double z, boolean depthOnly) {
        TextureAtlasSprite tex;
        if (te.isWorking()) {
            tex = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(te.inputTank.getFluid().getFluid().getFlowing().toString());
        } else {
            tex = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(te.inputTank.getFluid().getFluid().getStill().toString());
        }
        if (tex == null) {
            return;
        }

        Fluid f = te.inputTank.getFluid().getFluid();
        int clr = f.getColor(te.inputTank.getFluid());

        double w = 1.875 * 0.5;
        double h1 = 0.5;
        double h2 = 2.9375;
        double level = (double) te.inputTank.getFluidAmount() / (double) te.inputTank.getCapacity();

        double dx = 0;
        double dz = 0;

        switch (te.getMultiblockDirection()) {
            case EAST:
                dx = 0.5 + ONE_SIXTEENTH;
                dz = -0.5 + ONE_SIXTEENTH;
                break;
            case WEST:
                dx = -1.5 + ONE_SIXTEENTH;
                dz = -0.5 + ONE_SIXTEENTH;
                break;
            case NORTH:
                dx = -0.5 + ONE_SIXTEENTH;
                dz = -1.5 + ONE_SIXTEENTH;
                break;
            case SOUTH:
                dx = -0.5 + ONE_SIXTEENTH;
                dz = 0.5 + ONE_SIXTEENTH;
                break;
        }

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.disableLighting();

        if (depthOnly) {
            GlStateManager.colorMask(false, false, false, false);
            GlStateManager.depthMask(true);
        } else {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.depthMask(false);
            TGRenderHelper.enableFluidGlow(f.getLuminosity(te.inputTank.getFluid()));
        }

        this.drawFluidCubeWithTesselator(tex, dx, h1, dz, 2 * w, h2 * level, h2, clr);

        if (depthOnly) {
            GlStateManager.colorMask(true, true, true, true);
        } else {
            TGRenderHelper.disableFluidGlow();
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
        }

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static void renderPendingBeams(float partialTicks) {
        if (PENDING_BEAMS.isEmpty()) {
            return;
        }

        double vx = TileEntityRendererDispatcher.staticPlayerX;
        double vy = TileEntityRendererDispatcher.staticPlayerY;
        double vz = TileEntityRendererDispatcher.staticPlayerZ;

        for (ReactionChamberTileEntMaster te : PENDING_BEAMS) {
            if (te.isInvalid() || !te.isFormed()) {
                continue;
            }

            BlockPos beamPos = te.getPos().offset(te.getMultiblockDirection());

            GlStateManager.pushMatrix();
            GlStateManager.translate(beamPos.getX() + 0.5d - vx, te.getPos().getY() + 0.5d - vy, beamPos.getZ() + 0.5d - vz);
            GlStateManager.depthMask(false);

            renderBeamFocusEffect(te, partialTicks);

            GlStateManager.depthMask(true);
            GlStateManager.popMatrix();
        }

        PENDING_BEAMS.clear();
    }

    private static ResourceLocation getBeamTexture(ReactionChamberTileEntMaster tile) {
        ItemStack focus = tile.getInventory().getStackInSlot(ReactionChamberTileEntMaster.SLOT_FOCUS);
        if (!focus.isEmpty() && focus.getItem() == TGItems.RC_UV_EMITTER.getItem()
                && focus.getItemDamage() == TGItems.RC_UV_EMITTER.getItemDamage()) {
            return uvrayTexture;
        }
        return heatrayTexture;
    }

    private static void renderBeamFocusEffect(ReactionChamberTileEntMaster tile, float ptt) {
        Tessellator tess = Tessellator.getInstance();

        //----------------
        //HEAT RAY
        TGRenderHelper.enableBlendMode(RenderType.ADDITIVE);
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.color(1f, 1f, 1f, 1f);

        Minecraft.getMinecraft().getTextureManager().bindTexture(getBeamTexture(tile));

        double d = ((double) (tile.progress % 10) + ptt) / 10.0;
        d = 0.875 + (0.125 * (1.0 - Math.cos(Math.PI * d * 2.0)));

        double w = 1.875 * 0.5 * d;
        double h1 = 0.5;
        double h2 = 2.9375;

        double x1 = TileEntityRendererDispatcher.staticPlayerX;
        double z1 = TileEntityRendererDispatcher.staticPlayerZ;

        BlockPos pos = tile.getPos().offset(tile.getMultiblockDirection());
        float angle = (float) (Math.atan2(pos.getX() + 0.5d - x1, pos.getZ() + 0.5d - z1) * 180.0 / Math.PI);

        GlStateManager.rotate(angle, 0f, 1f, 0f);
        GlStateManager.translate(0.0d, 0.0d, 0.3d);

        BufferBuilder buf = tess.getBuffer();


        buf.begin(7, DefaultVertexFormats.POSITION_TEX);
        buf.pos(-w, h1, 0.0D).tex(0, 1).endVertex();
        buf.pos(-w, h2, 0.0D).tex(0, 0).endVertex();
        buf.pos(w, h2, 0.0D).tex(1, 0).endVertex();
        buf.pos(w, h1, 0.0D).tex(1, 1).endVertex();
        tess.draw();

        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        TGRenderHelper.disableBlendMode(RenderType.ADDITIVE);

        /*Reactivate Alpha blending*/
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        //------------------

    }

    protected void drawFluidCubeWithTesselator(TextureAtlasSprite tex, double x, double y, double z, double width, double height, double maxHeight, int color) {
        if (tex == null) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        int interpV;

        if (height < maxHeight * 0.5d) {

            double factor = height / (maxHeight * 0.5d);
            interpV = (int) (factor * 16);

            this.drawFluidTankPlane(buffer, x, y, z, tex, width, height, 0, interpV, color);
            this.drawFluidTankPlane(buffer, x, y, z, tex, 0, height, width, interpV, color);
            this.drawFluidTankPlane(buffer, x + width, y, z + width, tex, -width, height, 0, interpV, color);
            this.drawFluidTankPlane(buffer, x + width, y, z + width, tex, 0, height, -width, interpV, color);
        } else {

            float mhalf = (float) (maxHeight * 0.5f);

            this.drawFluidTankPlane(buffer, x, y, z, tex, width, mhalf, 0, 16, color);
            this.drawFluidTankPlane(buffer, x, y, z, tex, 0, mhalf, width, 16, color);
            this.drawFluidTankPlane(buffer, x + width, y, z + width, tex, -width, mhalf, 0, 16, color);
            this.drawFluidTankPlane(buffer, x + width, y, z + width, tex, 0, mhalf, -width, 16, color);

            double h = (height - mhalf);
            double factor = h / mhalf;
            interpV = (int) (factor * 16);

            this.drawFluidTankPlane(buffer, x, y + mhalf, z, tex, width, h, 0, interpV, color);
            this.drawFluidTankPlane(buffer, x, y + mhalf, z, tex, 0, h, width, interpV, color);
            this.drawFluidTankPlane(buffer, x + width, y + mhalf, z + width, tex, -width, h, 0, interpV, color);
            this.drawFluidTankPlane(buffer, x + width, y + mhalf, z + width, tex, 0, h, -width, interpV, color);

        }

        int red = (color >> 16 & 0xFF);
        int green = (color >> 8 & 0xFF);
        int blue = (color & 0xFF);
        int alpha = ((color >> 24) & 0xFF);

        buffer.pos(x, height + 0.5f, z).tex(tex.getMinU(), tex.getMinV()).color(red, green, blue, alpha).endVertex();
        buffer.pos(x, height + 0.5f, z + width).tex(tex.getMinU(), tex.getMaxV()).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, height + 0.5f, z + width).tex(tex.getMaxU(), tex.getMaxV()).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, height + 0.5f, z).tex(tex.getMaxU(), tex.getMinV()).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
    }

    protected void drawFluidTankPlane(BufferBuilder buffer, double x, double y, double z, TextureAtlasSprite icon, double w_x, double h, double w_z, int interp_v, int color) {
        int red = (color >> 16 & 0xFF);
        int green = (color >> 8 & 0xFF);
        int blue = (color & 0xFF);
        int alpha = ((color >> 24) & 0xFF);

        buffer.pos(x + 0, y + h, z + w_z).tex(icon.getMinU(), icon.getInterpolatedV(interp_v)).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + w_x, (y + h), z).tex(icon.getMaxU(), icon.getInterpolatedV(interp_v)).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + w_x, y + 0, z).tex(icon.getMaxU(), icon.getMinV()).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + 0, y + 0, z + w_z).tex(icon.getMinU(), icon.getMinV()).color(red, green, blue, alpha).endVertex();
    }
}
