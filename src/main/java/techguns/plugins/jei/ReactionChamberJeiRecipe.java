package techguns.plugins.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import techguns.gui.PoweredTileEntGui;
import techguns.gui.TGBaseGui;
import techguns.tileentities.ReactionChamberTileEntMaster;
import techguns.tileentities.operation.ReactionChamberOperation;
import techguns.tileentities.operation.ReactionChamberRecipe;
import techguns.util.TextUtil;

public class ReactionChamberJeiRecipe extends BasicRecipeWrapper {

    public static final int BG_U = 4;
    public static final int BG_V = 4;

    public static final int TANK_X = 9 - BG_U;
    public static final int TANK_Y = 9 - BG_V;
    public static final int TANK_W = 12;
    public static final int TANK_H = 88;

    public static final int RISK_X = 107 - BG_U;
    public static final int RISK_Y = 16 - BG_V;
    public static final int RISK_W = 12;
    public static final int RISK_H = 12;

    public static final int COMPLETION_X = 55 - BG_U;
    public static final int COMPLETION_Y = 69 - BG_V;
    public static final int COMPLETION_W = 58;
    public static final int COMPLETION_H = 9;

    public static final int PROGRESS_X = 34 - BG_U;
    public static final int PROGRESS_Y = 96 - BG_V;
    public static final int PROGRESS_W = 100;
    public static final int PROGRESS_H = 3;

    public static final int INTENSITY_X = 43 - BG_U;
    public static final int INTENSITY_Y = 43 - BG_V;
    public static final int INTENSITY_STEP_X = 7;
    public static final int INTENSITY_DOT_W = 5;
    public static final int INTENSITY_DOT_H = 5;

    public static final int POWER_X = 163 - BG_U;
    public static final int POWER_Y = 9 - BG_V;
    public static final int POWER_W = 6;
    public static final int POWER_H = 88;

    public final ReactionChamberRecipe recipe;

    public ReactionChamberJeiRecipe(ReactionChamberRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

    @Override
    protected int getDuration() {
        return this.recipe.ticks * ReactionChamberOperation.RECIPE_TICKRATE;
    }

    @Override
    protected int getRFperTick() {
        return this.recipe.RFTick;
    }

    public static List<ReactionChamberJeiRecipe> getRecipes() {
        List<ReactionChamberJeiRecipe> recipes = new ArrayList<>();
        for (ReactionChamberRecipe rec : ReactionChamberRecipe.getRecipes().values()) {
            recipes.add(new ReactionChamberJeiRecipe(rec));
        }
        return recipes;
    }

    @Override
    public void getIngredients(@NotNull IIngredients ingredients) {
        List<List<ItemStack>> inputs = recipe.getItemInputs();
        if (inputs != null && !inputs.isEmpty()) {
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        }

        List<List<ItemStack>> outputs = recipe.getItemOutputs();
        if (outputs != null && !outputs.isEmpty()) {
            if (outputs.size() > 4) {
                outputs = outputs.subList(0, 4);
            }
            ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
        }

        List<List<FluidStack>> fluidsIn = recipe.getFluidInputs();
        if (fluidsIn != null && !fluidsIn.isEmpty()) {
            ingredients.setInputLists(VanillaTypes.FLUID, fluidsIn);
        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {

        if (TGBaseGui.isInRect(mouseX, mouseY, COMPLETION_X, COMPLETION_Y, COMPLETION_W, COMPLETION_H)) {
            List<String> tooltip = new ArrayList<>();
            tooltip.add(TextUtil.trans("techguns.container.reactionchamber.completion") + ": " + com.mojang.realmsclient.gui.ChatFormatting.GREEN + this.recipe.requiredCompletion);
            tooltip.add(TextUtil.trans("techguns.container.reactionchamber.recipeticks") + ": " + com.mojang.realmsclient.gui.ChatFormatting.RED + this.recipe.ticks);
            tooltip.add(TextUtil.trans("techguns.container.reactionchamber.time") + ": " + (this.getDuration() / 20) + "s");
            return tooltip;
        }

        if (TGBaseGui.isInRect(mouseX, mouseY, INTENSITY_X, INTENSITY_Y, INTENSITY_STEP_X * 10, INTENSITY_DOT_H)) {
            List<String> tooltip = new ArrayList<>();
            tooltip.add(TextUtil.trans("techguns.container.reactionchamber.intensity") + ": " + this.recipe.preferredIntensity);
            if (this.recipe.isStable()) {
                tooltip.add(TextUtil.trans("techguns.container.reactionchamber.intensitymargin") + ": +/-" + this.recipe.intensityMargin);
                tooltip.add(TextUtil.trans("techguns.container.reactionchamber.chance") + ": " + ((int) (this.recipe.instability * 100)) + "%");
            } else {
                tooltip.add(com.mojang.realmsclient.gui.ChatFormatting.GREEN + TextUtil.trans("techguns.container.reactionchamber.stable"));
            }
            return tooltip;
        }

        if (TGBaseGui.isInRect(mouseX, mouseY, RISK_X, RISK_Y, RISK_W, RISK_H)) {
            List<String> tooltip = new ArrayList<>();
            tooltip.add(TextUtil.trans("techguns.container.reactionchamber.risk") + ": " + this.recipe.risk);
            return tooltip;
        }

        if (TGBaseGui.isInRect(mouseX, mouseY, POWER_X, POWER_Y, POWER_W, POWER_H)) {
            List<String> tooltip = new ArrayList<>();
            tooltip.add(TextUtil.trans("techguns.container.power") + ":");
            tooltip.add("-" + this.getRFperTick() + " " + PoweredTileEntGui.POWER_UNIT + "/" + TextUtil.trans("techguns.container.reactionchamber.recipetick"));
            tooltip.add("-" + (this.getRFperTick() * this.recipe.requiredCompletion) + " " + PoweredTileEntGui.POWER_UNIT);
            return tooltip;
        }

        return Collections.emptyList();
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(ReactionChamberJeiRecipeCategory.TEXTURE);

        drawRiskIcon(minecraft);

        drawCompletionBar(minecraft);
        drawProgressBar(minecraft);

        drawIntensityDots(minecraft);
    }

    private void drawRiskIcon(Minecraft minecraft) {
        int level = 0;
        switch (this.recipe.risk) {
            case RAD_LOW:
            case RAD_MEDIUM:
                level = 1;
                break;
            case EXPLOSION_LOW:
            case EXPLOSION_MEDIUM:
            case RAD_HIGH:
                level = 2;
                break;
            case EXPLOSION_HIGH:
            case UNFORSEEN_CONSEQUENCES:
                level = 3;
                break;
            default:
                break;
        }
        Gui.drawModalRectWithCustomSizedTexture(RISK_X, RISK_Y, 226, 88 + 12 * level, 12, 12, 256, 256);
    }

    private void drawCompletionBar(Minecraft minecraft) {
        int completionTotal = Math.max(1, (this.recipe.requiredCompletion & 0xFF) * ReactionChamberOperation.RECIPE_TICKRATE);
        long cycle = completionTotal * 50L;
        float prog = (Minecraft.getSystemTime() % cycle) / (float) cycle;
        if (prog < 0f) prog = 0f;
        if (prog > 1f) prog = 1f;

        int steps = (this.recipe.requiredCompletion & 0xFF);
        if (steps <= 0) steps = 1;

        int stepW = Math.max(1, COMPLETION_W / steps);
        int filledSteps = (int) Math.floor(prog * steps + 1.0e-4);
        if (filledSteps > steps) filledSteps = steps;

        int drawW = Math.min(COMPLETION_W, filledSteps * stepW);
        if (drawW > 0) {
            Gui.drawModalRectWithCustomSizedTexture(COMPLETION_X, COMPLETION_Y, 0, 240, drawW, COMPLETION_H, 256, 256);
        }
    }

    private void drawProgressBar(Minecraft minecraft) {
        int total = Math.max(1, this.getDuration());
        long cycle = total * 50L;
        float prog = (Minecraft.getSystemTime() % cycle) / (float) cycle;
        if (prog < 0f) prog = 0f;
        if (prog > 1f) prog = 1f;

        int drawW = (int) (PROGRESS_W * prog + 0.5f);
        if (drawW > 0) {
            Gui.drawModalRectWithCustomSizedTexture(PROGRESS_X, PROGRESS_Y, 63, 241, drawW, PROGRESS_H, 256, 256);
        }
    }

    private void drawIntensityDots(Minecraft minecraft) {
        int required = this.recipe.preferredIntensity;
        if (required < ReactionChamberTileEntMaster.MIN_INTENSITY) required = ReactionChamberTileEntMaster.MIN_INTENSITY;
        if (required > ReactionChamberTileEntMaster.MAX_INTENSITY) required = ReactionChamberTileEntMaster.MAX_INTENSITY;

        int shown = required;

        if (this.recipe.isStable() && this.recipe.intensityMargin > 0 && this.recipe.instability > 0f) {
            float t = (Minecraft.getSystemTime() % (3L * ReactionChamberOperation.RECIPE_TICKRATE * 50L)) / (float) (3L * ReactionChamberOperation.RECIPE_TICKRATE * 50L);
            int val = Math.round((float) Math.sin(t * 2.0f * (float) Math.PI) * this.recipe.intensityMargin);
            if (val > this.recipe.intensityMargin) val = this.recipe.intensityMargin;
            if (val < -this.recipe.intensityMargin) val = -this.recipe.intensityMargin;
            shown = required + val;
            if (shown < ReactionChamberTileEntMaster.MIN_INTENSITY) shown = ReactionChamberTileEntMaster.MIN_INTENSITY;
            if (shown > ReactionChamberTileEntMaster.MAX_INTENSITY) shown = ReactionChamberTileEntMaster.MAX_INTENSITY;
        }

        for (int i = 1; i <= 10; i++) {
            int x = INTENSITY_X + (i * INTENSITY_STEP_X);
            int y = INTENSITY_Y;

            if (i == required) {
                Gui.drawModalRectWithCustomSizedTexture(x, y, 53, 250, INTENSITY_DOT_W, INTENSITY_DOT_H, 256, 256);
            }

            if (shown >= i) {
                Gui.drawModalRectWithCustomSizedTexture(x, y, 58, 239, INTENSITY_DOT_W, INTENSITY_DOT_H, 256, 256);
            }
        }
    }
}
