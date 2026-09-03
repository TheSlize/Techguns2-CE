package techguns.plugins.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import techguns.*;
import techguns.tileentities.MetalPressTileEnt;

public class MetalPressJeiRecipeCategory extends BasicRecipeCategory<MetalPressJeiRecipe> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/gui/jei/metal_press.png");

    private static final int BG_U = 4;
    private static final int BG_V = 4;

    private static final int SLOT_IN1 = 0;
    private static final int SLOT_IN2 = 1;
    private static final int SLOT_OUT = 2;

    private static final int SLOT_IN1_X = 39 - BG_U;
    private static final int SLOT_IN1_Y = 16 - BG_V;

    private static final int SLOT_IN2_X = 59 - BG_U;
    private static final int SLOT_IN2_Y = 16 - BG_V;

    private static final int SLOT_OUT_X = 49 - BG_U;
    private static final int SLOT_OUT_Y = 59 - BG_V;

    private static final int PROGRESS_X = 49 - BG_U;
    private static final int PROGRESS_Y = 36 - BG_V;

    private final IDrawableAnimated progress;

    public MetalPressJeiRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, TEXTURE, "metalpress", TGJeiPlugin.METAL_PRESS);
        this.background = guiHelper.createDrawable(TEXTURE, 4, 4, 106, 85);
        IDrawableStatic progress_static = guiHelper.createDrawable(TEXTURE, 238, 0, 18, 21);
        this.progress = guiHelper.createAnimatedDrawable(progress_static, 100, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull MetalPressJeiRecipe recipeWrapper, @NotNull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(SLOT_IN1, true, SLOT_IN1_X, SLOT_IN1_Y);
        guiItemStacks.init(SLOT_IN2, true, SLOT_IN2_X, SLOT_IN2_Y);
        guiItemStacks.init(SLOT_OUT, false, SLOT_OUT_X, SLOT_OUT_Y);

        guiItemStacks.set(ingredients);

        if (recipeWrapper.recipe.requiresSteam()) {
            IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
            guiFluidStacks.init(0, true, MetalPressJeiRecipe.TANK_X + 1, MetalPressJeiRecipe.TANK_Y + 1,
                    MetalPressJeiRecipe.TANK_W - 2, MetalPressJeiRecipe.TANK_H - 2, MetalPressTileEnt.STEAM_CAPACITY, false, null);
            guiFluidStacks.set(ingredients);
        }
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        this.powerbar.draw(minecraft, 7 - 3, 17 - 4);
        this.progress.draw(minecraft, PROGRESS_X, PROGRESS_Y);
    }
}
