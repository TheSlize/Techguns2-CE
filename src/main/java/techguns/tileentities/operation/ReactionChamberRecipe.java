package techguns.tileentities.operation;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import techguns.tileentities.ReactionChamberTileEntMaster;
import techguns.util.ItemStackOreDict;
import techguns.util.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ReactionChamberRecipe implements IMachineRecipe {

    private static final HashMap<String, ReactionChamberRecipe> recipes = new HashMap<>();
    public String ID;
    public ItemStackOreDict input;
    public ReactionBeamFocus beamFocus;
    public Fluid liquidIn;
    public ArrayList<ItemStack> outputs;
    public byte ticks; //time of reaction in ReactionTicks
    public byte requiredCompletion; //Required completion level for success
    public byte preferredIntensity; //[0-10]
    public byte intensityMargin; //[0-10]
    public byte liquidLevel; //[0-10] in Buckets
    public int liquidConsumtion; // < LiquidLevel; in milliBuckets
    public float instability; //[0.0f-1.0f] chance to change preferredIntensity per ReactionTick
    public RiskType risk; //What happens when reaction fails.
    /**
     * RF per Recipe tick
     */
    public int RFTick;

    private ReactionChamberRecipe(String ID, ItemStackOreDict input,
                                  ItemStack beamFocus, Fluid liquidIn,
                                  ItemStack output, int ticks, int requiredCompletion,
                                  int preferredIntensity, int intensityMargin, int liquidLevel,
                                  short liquidConsumtion, float instability, RiskType risk, int RFTick) {
        this.input = input;
        this.beamFocus = ReactionBeamFocus.getBeamFocus(beamFocus);
        this.liquidIn = liquidIn;
        this.outputs = new ArrayList<>();
        this.outputs.add(output);
        this.ticks = (byte) ticks;
        this.requiredCompletion = (byte) requiredCompletion;
        this.preferredIntensity = (byte) Math.max(ReactionChamberTileEntMaster.MIN_INTENSITY, Math.min(preferredIntensity, ReactionChamberTileEntMaster.MAX_INTENSITY));
        this.intensityMargin = (byte) intensityMargin;
        this.liquidLevel = (byte) liquidLevel;
        this.liquidConsumtion = liquidConsumtion;
        this.instability = instability;
        this.risk = risk;
        this.RFTick = RFTick;

        this.ID = ID;
    }

    private ReactionChamberRecipe(String ID, ItemStackOreDict input,
                                  ItemStack beamFocus, Fluid liquidIn,
                                  ItemStack[] outputs, int ticks, int requiredCompletion,
                                  int preferredIntensity, int intensityMargin, int liquidLevel,
                                  short liquidConsumtion, float instability, RiskType risk, int RFTick) {
        this.input = input;
        this.beamFocus = ReactionBeamFocus.getBeamFocus(beamFocus);
        this.liquidIn = liquidIn;
        this.outputs = new ArrayList<>();
        this.outputs.addAll(Arrays.asList(outputs));
        this.ticks = (byte) ticks;
        this.requiredCompletion = (byte) requiredCompletion;
        this.preferredIntensity = (byte) Math.max(ReactionChamberTileEntMaster.MIN_INTENSITY, Math.min(preferredIntensity, ReactionChamberTileEntMaster.MAX_INTENSITY));
        this.intensityMargin = (byte) intensityMargin;
        this.liquidLevel = (byte) liquidLevel;
        this.liquidConsumtion = liquidConsumtion;
        this.instability = instability;
        this.risk = risk;
        this.RFTick = RFTick;

        this.ID = ID;
    }

    public static ReactionChamberRecipe getByKey(String key) {
        return recipes.get(key);
    }

    public static HashMap<String, ReactionChamberRecipe> getRecipes() {
        return recipes;
    }

    public static ReactionChamberRecipe addRecipe(String ID, ItemStackOreDict input,
                                                  ItemStack beamFocus, Fluid liquidIn,
                                                  ItemStack output, int ticks, int requiredCompletion,
                                                  int preferredIntensity, int intensityMargin, int liquidLevel,
                                                  int liquidConsumtion, float instability, RiskType risk, int RFTick) {
        ReactionChamberRecipe rec = new ReactionChamberRecipe(ID, input, beamFocus, liquidIn, output, ticks, requiredCompletion, preferredIntensity, intensityMargin, liquidLevel, (short) liquidConsumtion, instability, risk, RFTick);
        recipes.put(rec.ID, rec);
        return rec;
    }

    public static ReactionChamberRecipe addRecipe(String ID, ItemStackOreDict input,
                                                  ItemStack beamFocus, Fluid liquidIn,
                                                  ItemStack[] outputs, int ticks, int requiredCompletion,
                                                  int preferredIntensity, int intensityMargin, int liquidLevel,
                                                  int liquidConsumtion, float instability, RiskType risk, int RFTick) {
        ReactionChamberRecipe rec = new ReactionChamberRecipe(ID, input, beamFocus, liquidIn, outputs, ticks, requiredCompletion, preferredIntensity, intensityMargin, liquidLevel, (short) liquidConsumtion, instability, risk, RFTick);
        recipes.put(rec.ID, rec);
        return rec;
    }

    public static ReactionChamberRecipe getMatchingRecipe(ItemStack input, ItemStack focus, FluidStack tank, byte intensity) {
        for (String s : recipes.keySet()) {
            ReactionChamberRecipe rec = recipes.get(s);
            if (rec.matches(input, focus, tank, intensity)) {
                return rec;
            }
        }
        return null;
    }

    public boolean matches(ItemStack input, ItemStack focus, FluidStack tank, byte intensity) {

        if (!input.isEmpty() && this.input.isEqualWithOreDict(input)) {
            ReactionBeamFocus f = ReactionBeamFocus.getBeamFocus(focus);
            if (f != null && f == this.beamFocus) {
                if (tank != null && tank.getFluid() == this.liquidIn && tank.amount >= (this.liquidLevel * Fluid.BUCKET_VOLUME)) {
                    return intensity == this.preferredIntensity;
                }
            }
        }
        return false;
    }

    public String getID() {
        return ID;
    }

    public boolean isStable() {
        return this.instability != 0.0f && this.intensityMargin != 0;
    }

    @Override
    public List<List<ItemStack>> getItemInputs() {
        ArrayList<List<ItemStack>> list = new ArrayList<>();
        list.add(this.input.getItemStacks());

        ArrayList<ItemStack> focus = new ArrayList<>();
        focus.add(this.beamFocus.item);
        list.add(focus);

        return list;
    }

    @Override
    public List<List<ItemStack>> getItemOutputs() {
        ArrayList<List<ItemStack>> list = new ArrayList<>();
        this.outputs.forEach(s -> {
            ArrayList<ItemStack> list2 = new ArrayList<>();
            list2.add(s);
            list.add(list2);
        });
        return list;
    }

    @Override
    public List<List<FluidStack>> getFluidInputs() {
        ArrayList<List<FluidStack>> list = new ArrayList<>();
        ArrayList<FluidStack> list2 = new ArrayList<>();
        list2.add(new FluidStack(this.liquidIn, this.liquidLevel * Fluid.BUCKET_VOLUME));
        list.add(list2);
        return list;
    }

    public enum RiskType {
        NONE, BREAK_ITEM, RAD_LOW, RAD_MEDIUM, RAD_HIGH, EXPLOSION_LOW, EXPLOSION_MEDIUM, EXPLOSION_HIGH, UNFORSEEN_CONSEQUENCES;

        public static boolean isFatal(RiskType type) {
            return !(type == EXPLOSION_LOW || type == EXPLOSION_HIGH || type == EXPLOSION_MEDIUM || type == UNFORSEEN_CONSEQUENCES);
        }

        @Override
        public String toString() {
            switch (this) {

                case BREAK_ITEM:
                    return ChatFormatting.GREEN + TextUtil.trans("techguns.reactionChamber.RiskType.breakItem");
                case EXPLOSION_LOW:
                    return ChatFormatting.YELLOW + TextUtil.trans("techguns.reactionChamber.RiskType.expLow");
                case EXPLOSION_MEDIUM:
                    return ChatFormatting.RED + TextUtil.trans("techguns.reactionChamber.RiskType.expMed");
                case RAD_LOW:
                case RAD_MEDIUM:
                case RAD_HIGH:
                case EXPLOSION_HIGH:
                case UNFORSEEN_CONSEQUENCES:
                    return "NOT_YET_IMPLEMENTED";

                case NONE:
                default:
                    return ChatFormatting.WHITE + TextUtil.trans("techguns.reactionChamber.RiskType.none");
            }
        }
    }

}