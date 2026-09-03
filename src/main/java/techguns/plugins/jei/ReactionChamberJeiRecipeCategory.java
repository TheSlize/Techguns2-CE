package techguns.plugins.jei;


import com.mojang.realmsclient.gui.ChatFormatting;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import org.jetbrains.annotations.NotNull;
import techguns.*;
import techguns.util.TextUtil;

public class ReactionChamberJeiRecipeCategory extends BasicRecipeCategory<ReactionChamberJeiRecipe> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/gui/jei/reaction_chamber.png");

    private static final int BG_U = 4;
    private static final int BG_V = 4;

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_FOCUS = 1;

    private static final int SLOT_OUT0 = 2;
    private static final int SLOT_OUT1 = 3;
    private static final int SLOT_OUT2 = 4;
    private static final int SLOT_OUT3 = 5;

    private static final int SLOT_INPUT_X = 30 - BG_U;
    private static final int SLOT_INPUT_Y = 65 - BG_V;

    private static final int SLOT_FOCUS_X = 75 - BG_U;
    private static final int SLOT_FOCUS_Y = 13 - BG_V;

    private static final int SLOT_OUT0_X = 120 - BG_U;
    private static final int SLOT_OUT0_Y = 65 - BG_V;

    private static final int SLOT_OUT1_X = 138 - BG_U;
    private static final int SLOT_OUT1_Y = 47 - BG_V;

    private static final int SLOT_OUT2_X = 138 - BG_U;
    private static final int SLOT_OUT2_Y = 65 - BG_V;

    private static final int SLOT_OUT3_X = 138 - BG_U;
    private static final int SLOT_OUT3_Y = 83 - BG_V;

    private final IDrawableStatic tankOverlay;

    public ReactionChamberJeiRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, TEXTURE, "reactionchamber", TGJeiPlugin.REACTION_CHAMBER);
        this.background = guiHelper.createDrawable(TEXTURE, 4, 4, 168, 100);
        this.tankOverlay = guiHelper.createDrawable(TEXTURE, 226, 0, 12, 88);
        this.powerbar_static = guiHelper.createDrawable(TEXTURE, 222, 1, 4, 86);
        this.powerbar = guiHelper.createAnimatedDrawable(powerbar_static, 100, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @NotNull ReactionChamberJeiRecipe recipeWrapper, @NotNull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(SLOT_INPUT, true, SLOT_INPUT_X, SLOT_INPUT_Y);
        guiItemStacks.init(SLOT_FOCUS, true, SLOT_FOCUS_X, SLOT_FOCUS_Y);

        guiItemStacks.init(SLOT_OUT0, false, SLOT_OUT0_X, SLOT_OUT0_Y);
        guiItemStacks.init(SLOT_OUT1, false, SLOT_OUT1_X, SLOT_OUT1_Y);
        guiItemStacks.init(SLOT_OUT2, false, SLOT_OUT2_X, SLOT_OUT2_Y);
        guiItemStacks.init(SLOT_OUT3, false, SLOT_OUT3_X, SLOT_OUT3_Y);

        guiFluidStacks.init(0, true,
                ReactionChamberJeiRecipe.TANK_X, ReactionChamberJeiRecipe.TANK_Y,
                ReactionChamberJeiRecipe.TANK_W, ReactionChamberJeiRecipe.TANK_H,
                10 * Fluid.BUCKET_VOLUME, false, tankOverlay);

        guiFluidStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> tooltip.add(tooltip.size() - 1,
                ChatFormatting.GRAY
                        + TextUtil.trans("techguns.container.reactionchamber.consumption") + ": "
                        + String.format("%,d", recipeWrapper.recipe.liquidConsumtion) + " mB"));

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }

    @Override
    public void drawExtras(@NotNull Minecraft minecraft) {
        this.powerbar.draw(minecraft, 164 - BG_U, 10 - BG_V);
    }
}
