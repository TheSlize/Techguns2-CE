package techguns.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidTankInfo;
import techguns.TGPackets;
import techguns.Tags;
import techguns.gui.containers.ReactionChamberContainer;
import techguns.packets.PacketGuiButtonClick;
import techguns.tileentities.ReactionChamberTileEntMaster;
import techguns.util.TextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static techguns.tileentities.ReactionChamberTileEntMaster.*;

public class ReactionChamberGui extends PoweredTileEntGui {
    public static final ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/gui/ce_reaction_chamber_gui.png");
    public static final ResourceLocation ru_ru = new ResourceLocation(Tags.MOD_ID, "textures/gui/translations/ru_ru.png");
    ReactionChamberTileEntMaster tile;

    public ReactionChamberGui(InventoryPlayer ply, ReactionChamberTileEntMaster tile) {
        super(new ReactionChamberContainer(ply, tile), tile);
        this.tile = tile;
        this.tex = texture;
        this.showUpgradeSlot = false;
        this.showMachineName = false;
        this.showRedstone = false;
        this.heightSecurityButton = 19;
        this.xSize = 210;
        this.ySize = 238;
        this.appearanceType = EnumAppearanceType.ADVANCED;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        int mx = mouseX - k;
        int my = mouseY - l;

        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (this.tile.inputTank.getFluidAmount() > 0) {
            int px = this.tile.inputTank.getFluidAmount() * 86 / this.tile.inputTank.getCapacity();

            TextureAtlasSprite tex = mc.getTextureMapBlocks().getTextureExtry(tile.inputTank.getFluid().getFluid().getStill().toString());

            this.drawFluidWithTesselator(tex, k + 8, l + 31, 18, 86, px);
        }

        this.mc.getTextureManager().bindTexture(texture);
        if (this.tile.getFluidLimit() > 0) {
            int limitPx = Math.max(1, Math.min(86, this.tile.getFluidLimit() * 86 / this.tile.inputTank.getCapacity()));
            this.drawTexturedModalRect(k + 8, l + 31 + 86 - limitPx, 226, 136, 18, 1);
        }
        int reqIntens = this.tile.getCurrentReaction() != null ? this.tile.getCurrentReaction().required_intensity : 0;
        if (this.tile.getEnergyStorage().getEnergyStored() > 0) {
            this.drawTexturedModalRect(k + 190, l + 16, 246, 0, 9, 12);
        }
        for (int i = 1; i < 11; i++) {
            if (tile.getIntensity() >= i) {
                this.drawTexturedModalRect(k + 49 + i * 7, l + 59, 58, 239, 5, 6);
            }
            if (isInRect(mx, my, 49 + i * 7, 59, 5, 6)) {
                this.drawTexturedModalRect(k + 48 + i * 7, l + 58, 58, 245, 7, 8);
            }
            if (reqIntens != this.tile.getIntensity() && reqIntens == i) {
                this.drawTexturedModalRect(k + 49 + i * 7, l + 59, 53, 250, 5, 6);
            }
        }

        if (this.tile.isWorking()) {
            this.drawTexturedModalRect(k + 84, l + 68, 210, 95, 12, 16);
            this.drawTexturedModalRect(k + 84, l + 106, 210, 111, 12, 16);

            float compl = this.tile.getCurrentReaction() != null ? this.tile.getCurrentReaction().completion : 0f;
            float req_compl = this.tile.getCurrentReaction() != null ? this.tile.getCurrentReaction().getRecipe().requiredCompletion : 1.0f;

            float compl_px = compl / req_compl;

            int dur = this.tile.progress;
            int maxdur = this.tile.totaltime;

            float dur_px = (dur * 1.0f) / (maxdur * 1.0f);

            this.drawTexturedModalRect(k + 61, l + 89, 0, 239, (int) (58 * compl_px), 11);

            this.drawTexturedModalRect(k + 40, l + 125, 63, 241, (int) (100 * dur_px), 4);
        }

        if ("ru_ru".equalsIgnoreCase(Minecraft.getMinecraft().gameSettings.language)) {
            this.mc.getTextureManager().bindTexture(ru_ru);
            this.drawTexturedModalRect(k + 44, l, 0, 0, 88, 12);
            this.drawTexturedModalRect(k + 5, l + 120, 88, 0, 24, 9);
        }
    }

    protected int getHoveredFluidLimit(int my) {
        int capacity = this.tile.inputTank.getCapacity();
        int fromBottom = Math.max(0, Math.min(86, 31 + 86 - my));
        int limit = Math.round(fromBottom * capacity / 8600.0f) * 100;
        return Math.max(100, Math.min(capacity, limit));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        int mx = mouseX - k;
        int my = mouseY - l;
        for (int i = 1; i < 11; i++) {
            if (isInRect(mx, my, 49 + i * 7, 59, 5, 6)) {
                if (mouseButton != 0) return;
                if (this.tile.getIntensity() == i) return;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(new SoundEvent(
                        new ResourceLocation(Tags.MOD_ID, "machines.rc_sel")
                ), 1.0F));
                TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.ownedTile, BUTTON_ID_INTENSITY_INC, "" + i));
                this.tile.setContentsChanged(true);
                return;
            }
        }
        if (isInRect(mx, my, 8, 31, 18, 86)) {
            if (mouseButton == 0) {
                int limit = getHoveredFluidLimit(my);
                playVanillaButtonSound();
                TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.ownedTile, BUTTON_ID_SET_FLUID_LIMIT, "" + limit));
                this.tile.setFluidLimit(limit);
            } else if (mouseButton == 1 && this.tile.getFluidLimit() > 0) {
                playVanillaButtonSound();
                TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.ownedTile, BUTTON_ID_SET_FLUID_LIMIT, "0"));
                this.tile.setFluidLimit(0);
            }
            return;
        }
        if (isInRect(mx, my, 5, 120, 24, 9)) {
            playVanillaButtonSound();
            TGPackets.wrapper.sendToServer(new PacketGuiButtonClick(this.ownedTile, BUTTON_ID_DUMPTANK));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        int mx = mouseX - (this.width - this.xSize) / 2;
        int my = mouseY - (this.height - this.ySize) / 2;

        if (isInRect(mx, my, 8, 31, 18, 86)) {
            FluidTankInfo info = this.tile.inputTank.getInfo();
            List<String> tooltip = new ArrayList<>();

            tooltip.add(TextUtil.trans(info.fluid != null ? info.fluid.getLocalizedName() : Tags.MOD_ID + ".gui.empty"));
            tooltip.add(tile.inputTank.getFluidAmount() + "/" + (info.capacity + " mB"));

            int limit = this.tile.getFluidLimit();
            if (limit > 0) {
                tooltip.add(TextFormatting.RED + Integer.toString(limit) + " mB");
            }
            tooltip.add(TextFormatting.GOLD + Integer.toString(getHoveredFluidLimit(my)) + " mB");
            tooltip.add(TextFormatting.GRAY + TextUtil.trans("techguns.reactionchamber.fluidlimit.set"));
            tooltip.add(TextFormatting.GRAY + TextUtil.trans("techguns.reactionchamber.fluidlimit.remove"));

            this.drawHoveringText(tooltip, mx, my);

        } else if (isInRect(mx, my, 5, 120, 24, 9)) {
            List<String> tooltip = new ArrayList<>();
            tooltip.add(TextUtil.trans("techguns.chemlab.dumpinput"));
            tooltip.add(TextUtil.trans("techguns.chemlab.dumpinput.tooltip"));

            this.drawHoveringText(tooltip, mx, my);
        }

    }

    @Override
    protected void drawDefaultEnergyBar() {
        drawEnergyBar(guiLeft, guiTop, 186, 30, 210, 0, 16, 95, texture);
    }

    @Override
    protected void drawDefaultEnergyTooltip(int mouseX, int mouseY) {
        drawEnergyTooltip(mouseX, mouseY, 186, 30, 16, 95);
    }

}
