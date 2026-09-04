package techguns.gui.player.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import techguns.TGItems;

@SideOnly(Side.CLIENT)
public class TGCreativeInventoryTab {

    private static final int GUI_WIDTH = 195;
    private static final int GUI_HEIGHT = 136;

    private static boolean swallowNextRelease = false;

    // Th3_Sl1ze: in case you're curious, without cancelling mouse release after switching back to creative inventory
    // you'd also switch to a creative tab in most cases
    public static void swallowNextMouseRelease() {
        swallowNextRelease = true;
    }

    private TGCreativeTabButton button;

    private TGCreativeTabButton getButton(GuiContainerCreative gui) {
        if (this.button == null) {
            this.button = new TGCreativeTabButton(0, 0, 0, TGItems.PISTOL_ROUNDS, 0);
        }
        this.button.x = (gui.width - GUI_WIDTH) / 2 - 28;
        this.button.y = (gui.height - GUI_HEIGHT) / 2 + 8;
        return this.button;
    }

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.getGui() instanceof GuiContainerCreative gui)) {
            return;
        }
        this.getButton(gui).drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (Mouse.getEventButton() == 0) {
            if (!Mouse.getEventButtonState()) {
                if (swallowNextRelease) {
                    swallowNextRelease = false;
                    event.setCanceled(true);
                }
                return;
            }
            swallowNextRelease = false;
        }
        if (!(event.getGui() instanceof GuiContainerCreative gui) || Mouse.getEventButton() != 0) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        int mouseX = Mouse.getEventX() * gui.width / mc.displayWidth;
        int mouseY = gui.height - Mouse.getEventY() * gui.height / mc.displayHeight - 1;

        TGCreativeTabButton tab = this.getButton(gui);
        if (tab.mousePressed(mc, mouseX, mouseY)) {
            tab.playPressSound(mc.getSoundHandler());
            event.setCanceled(true);
        }
    }

}
