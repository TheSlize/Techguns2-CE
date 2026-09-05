package techguns.plugins.jei;

import org.jetbrains.annotations.NotNull;
import techguns.tileentities.operation.UpgradeBenchRecipes.UpgradeBenchRecipe;
import techguns.util.TextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import techguns.tileentities.operation.UpgradeBenchRecipes;
import techguns.TGConfig;

public class UpgradeBenchJeiRecipe extends BasicRecipeWrapper {
    protected UpgradeBenchRecipe recipe;

    public UpgradeBenchJeiRecipe(UpgradeBenchRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

    public static List<UpgradeBenchJeiRecipe> getRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();

        List<UpgradeBenchJeiRecipe> recipes = new ArrayList<>();

        UpgradeBenchRecipes.recipes.forEach(r -> recipes.add(new UpgradeBenchJeiRecipe(r)));

        return recipes;
    }

    @Override
    protected int getRFperTick() {
        return 0;
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int xp = this.recipe.getLevel() * TGConfig.misc.upgrade_xp_cost;
        minecraft.fontRenderer.drawStringWithShadow(TextUtil.transTG("gui.xpcost") + ": " + xp, 95 + BasicRecipeCategory.JEI_OFFSET_X, 17 + BasicRecipeCategory.JEI_OFFSET_Y, 8453920);
    }

}
