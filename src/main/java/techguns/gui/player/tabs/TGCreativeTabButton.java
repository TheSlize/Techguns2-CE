package techguns.gui.player.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techguns.TGPackets;
import techguns.packets.PacketOpenPlayerGUI;

// Th3_Sl1ze: this whole class exists just to horizontally mirror the vanilla creative tab button lmao
@SideOnly(Side.CLIENT)
public class TGCreativeTabButton extends GuiButton {

    public static final int TAB_WIDTH = 28;
    public static final int TAB_HEIGHT = 28;

    private static final ResourceLocation TABS_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/creative_inventory/tabs.png");

    private final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    private final ItemStack iconItem;
    private final short guiID;

    public TGCreativeTabButton(int id, int xPos, int yPos, ItemStack iconItem, int guiID) {
        super(id, xPos, yPos, TAB_WIDTH, TAB_HEIGHT, "");
        this.iconItem = iconItem;
        this.guiID = (short) guiID;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            TGCreativeInventoryTab.swallowNextMouseRelease();
            TGPackets.wrapper.sendToServer(new PacketOpenPlayerGUI(this.guiID));
            return true;
        }
        return false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(TABS_TEXTURE);

        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) this.x, (float) this.y, 0.0f);
        GlStateManager.scale(1.0f, -1.0f, 1.0f);
        GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
        this.drawTexturedModalRect(0, 0, 0, 0, 28, 28);
        GlStateManager.popMatrix();
        GlStateManager.enableCull();

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        this.zLevel = 100.0f;
        this.renderItem.zLevel = 100.0f;
        this.renderItem.renderItemAndEffectIntoGUI(this.iconItem, this.x + 6, this.y + 6);
        this.renderItem.zLevel = 0.0f;
        this.zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

}
