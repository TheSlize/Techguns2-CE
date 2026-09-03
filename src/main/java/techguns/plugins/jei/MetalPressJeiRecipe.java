package techguns.plugins.jei;

import com.mojang.realmsclient.gui.ChatFormatting;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import techguns.gui.MetalPressGui;
import techguns.gui.TGBaseGui;
import techguns.tileentities.MetalPressTileEnt;
import techguns.tileentities.operation.MetalPressRecipes;
import techguns.util.TextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MetalPressJeiRecipe extends BasicRecipeWrapper {

    public static final int TEX_W = 256;
    public static final int TEX_H = 256;

    public static final int BG_U = 4;
    public static final int BG_V = 4;

    public static final int TANK_X = 96 - BG_U;
    public static final int TANK_Y = 11 - BG_V;
    public static final int TANK_W = 12;
    public static final int TANK_H = 52;

    public static final int GAUGE_X = 92 - BG_U;
    public static final int GAUGE_Y = 68 - BG_V;
    public static final int GAUGE_W = 18;
    public static final int GAUGE_H = 18;

    public static final int TANK_EMPTY_U = 128;
    public static final int TANK_EMPTY_V = 0;
    public static final int TANK_OVERLAY_U = 116;
    public static final int TANK_OVERLAY_V = 0;

    public final MetalPressRecipes.MetalPressRecipe recipe;

    public MetalPressJeiRecipe(MetalPressRecipes.MetalPressRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

    @Override
    protected int getRFperTick() {
        return MetalPressTileEnt.POWER_PER_TICK;
    }

    public static List<MetalPressJeiRecipe> getRecipes() {
        List<MetalPressJeiRecipe> recipes = new ArrayList<>();
        ArrayList<MetalPressRecipes.MetalPressRecipe> m_recipes = MetalPressRecipes.getRecipes();
        m_recipes.forEach(r -> recipes.add(new MetalPressJeiRecipe(r)));
        return recipes;
    }

    @Override
    public void getIngredients(@NotNull IIngredients ingredients) {
        super.getIngredients(ingredients);

        if (this.recipe.requiresSteam()) {
            FluidStack steam = FluidRegistry.getFluidStack("steam", this.recipe.steamCost);
            if (steam != null && steam.amount > 0) {
                List<List<FluidStack>> in = new ArrayList<>();
                in.add(Collections.singletonList(steam));
                ingredients.setInputLists(VanillaTypes.FLUID, in);
            }
        }
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (!this.recipe.requiresSteam()) return;

        minecraft.getTextureManager().bindTexture(MetalPressJeiRecipeCategory.TEXTURE);
        Gui.drawModalRectWithCustomSizedTexture(TANK_X, TANK_Y, TANK_EMPTY_U, TANK_EMPTY_V, TANK_W, TANK_H, TEX_W, TEX_H);
        Gui.drawModalRectWithCustomSizedTexture(TANK_X, TANK_Y, TANK_OVERLAY_U, TANK_OVERLAY_V, TANK_W, TANK_H, TEX_W, TEX_H);

        minecraft.getTextureManager().bindTexture(MetalPressGui.gauge_texture);
        int pressure = this.recipe.requiredPressure;
        if (pressure < 0) pressure = 0;
        if (pressure > 12) pressure = 12;
        Gui.drawModalRectWithCustomSizedTexture(GAUGE_X, GAUGE_Y, 0, pressure * 18, GAUGE_W, GAUGE_H, 256, 256);
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (this.recipe.requiresSteam() && TGBaseGui.isInRect(mouseX, mouseY, GAUGE_X, GAUGE_Y, GAUGE_W, GAUGE_H)) {
            List<String> tooltip = new ArrayList<>();
            tooltip.add(TextUtil.trans("techguns.gui.pressure") + ": " + this.recipe.requiredPressure);
            tooltip.add(ChatFormatting.GRAY + TextUtil.trans("techguns.jei.recipe_requires",
                    TextUtil.trans("item.techguns.machine_upgrade_steam.name")));
            return tooltip;
        }

        return super.getTooltipStrings(mouseX, mouseY);
    }
}
