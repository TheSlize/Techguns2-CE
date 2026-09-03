package techguns;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import techguns.blocks.EnumOreType;
import techguns.items.armors.ICamoChangeable;
import techguns.tileentities.operation.*;
import techguns.tileentities.operation.CamoBenchRecipes.CamoBenchRecipe;
import techguns.tileentities.operation.ReactionChamberRecipe.RiskType;
import techguns.util.ItemStackOreDict;
import techguns.util.ItemUtil;

import java.util.ArrayList;

/**
 * Class that contains recipes for machines
 *
 */
public class TGMachineRecipes {

    public static void addRecipes() {

        ItemStack GOLD_OR_ELECTRUM;
        if (OreDictionary.doesOreNameExist("ingotElectrum")) {
            NonNullList<ItemStack> list = OreDictionary.getOres("ingotElectrum");
            if (!list.isEmpty()) {
                GOLD_OR_ELECTRUM = list.get(0);
            } else {
                GOLD_OR_ELECTRUM = new ItemStack(Items.GOLD_INGOT, 1);
            }
        } else {
            GOLD_OR_ELECTRUM = new ItemStack(Items.GOLD_INGOT, 1);
        }

        //AMMO PRESS
        ArrayList<String> metal2 = new ArrayList<>();
        ArrayList<String> metal1 = new ArrayList<>();
        ArrayList<String> powders = new ArrayList<>();
        metal2.add("ingotCopper");
        metal2.add("ingotTin");
        metal2.add("ingotIron");
        metal2.add("ingotBronze");
        metal1.add("ingotSteel");
        metal1.add("ingotLead");
        powders.add("gunpowder");
        AmmoPressBuildPlans.init(metal1, metal2, powders);

        //METAL PRESS
        MetalPressRecipes.addRecipe("ingotTin", "ingotTin", new ItemStack(TGItems.PLATE_TIN.getItem(), 2, TGItems.PLATE_TIN.getItemDamage()), true);
        MetalPressRecipes.addRecipe("ingotCopper", "ingotCopper", new ItemStack(TGItems.PLATE_COPPER.getItem(), 2, TGItems.PLATE_COPPER.getItemDamage()), true);
        MetalPressRecipes.addRecipe("ingotBronze", "ingotBronze", new ItemStack(TGItems.PLATE_BRONZE.getItem(), 2, TGItems.PLATE_BRONZE.getItemDamage()), true);
        MetalPressRecipes.addRecipe("ingotIron", "ingotIron", new ItemStack(TGItems.PLATE_IRON.getItem(), 2, TGItems.PLATE_IRON.getItemDamage()), true);
        MetalPressRecipes.addRecipe("ingotSteel", "ingotSteel", new ItemStack(TGItems.PLATE_STEEL.getItem(), 2, TGItems.PLATE_STEEL.getItemDamage()), true);
        MetalPressRecipes.addRecipe("ingotLead", "ingotLead", new ItemStack(TGItems.PLATE_LEAD.getItem(), 2, TGItems.PLATE_LEAD.getItemDamage()), true);
        MetalPressRecipes.addRecipe("plateIron", new ItemStack(Items.FLINT, 1), new ItemStack(TGItems.MECHANICAL_PARTS_IRON.getItem(), 1, TGItems.MECHANICAL_PARTS_IRON.getItemDamage()), true);
        MetalPressRecipes.addRecipe("plateObsidianSteel", "gemQuartz", new ItemStack(TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItem(), 1, TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL.getItemDamage()), true);
        if (TGConfig.misc.addOreDicts) {
            MetalPressRecipes.addRecipe("plateCarbon", new ItemStack(Items.BLAZE_ROD), new ItemStack(TGItems.MECHANICAL_PARTS_CARBON.getItem(), 2, TGItems.MECHANICAL_PARTS_CARBON.getItemDamage()), true);
            MetalPressRecipes.addRecipe("fiberCarbon", "fiberCarbon", new ItemStack(TGItems.PLATE_CARBON.getItem(), 2, TGItems.PLATE_CARBON.getItemDamage()), true);
            MetalPressRecipes.addRecipe("ingotTitanium", "ingotTitanium", new ItemStack(TGItems.PLATE_TITANIUM.getItem(), 2, TGItems.PLATE_TITANIUM.getItemDamage()), true);
            MetalPressRecipes.addRecipe("plateObsidianSteel", "plateTitanium", new ItemStack(TGItems.GAUSSRIFLE_SLUGS.getItem(), 4, TGItems.GAUSSRIFLE_SLUGS.getItemDamage()), true);
        } else {
            MetalPressRecipes.addRecipe("plateCarbonTG", new ItemStack(Items.BLAZE_ROD), new ItemStack(TGItems.MECHANICAL_PARTS_CARBON.getItem(), 2, TGItems.MECHANICAL_PARTS_CARBON.getItemDamage()), true);
            MetalPressRecipes.addRecipe("fiberCarbonTG", "fiberCarbonTG", new ItemStack(TGItems.PLATE_CARBON.getItem(), 2, TGItems.PLATE_CARBON.getItemDamage()), true);
            MetalPressRecipes.addRecipe("ingotTitaniumTG", "ingotTitaniumTG", new ItemStack(TGItems.PLATE_TITANIUM.getItem(), 2, TGItems.PLATE_TITANIUM.getItemDamage()), true);
            MetalPressRecipes.addRecipe("plateObsidianSteel", "plateTitaniumTG", new ItemStack(TGItems.GAUSSRIFLE_SLUGS.getItem(), 4, TGItems.GAUSSRIFLE_SLUGS.getItemDamage()), true);
        }
        MetalPressRecipes.addRecipe("plateCopper", "plateCopper", new ItemStack(TGItems.WIRE_COPPER.getItem(), 8, TGItems.WIRE_COPPER.getItemDamage()), true);

        MetalPressRecipes.addRecipe("plateObsidianSteel", new ItemStack(TGItems.TGX.getItem(), 1, TGItems.TGX.getItemDamage()), new ItemStack(TGItems.ADVANCED_ROUNDS.getItem(), 16, TGItems.ADVANCED_ROUNDS.getItemDamage()), true);
        MetalPressRecipes.addRecipe("ingotObsidianSteel", "ingotObsidianSteel", new ItemStack(TGItems.PLATE_OBSIDIAN_STEEL.getItem(), 2, TGItems.PLATE_OBSIDIAN_STEEL.getItemDamage()), true);
        MetalPressRecipes.addRecipe(new ItemStackOreDict("plateSteel").setCount(2), new ItemStackOreDict("plateBronze"), new ItemStack(TGItems.STEAMARMOR_PLATE.getItem(), 1, TGItems.STEAMARMOR_PLATE.getItemDamage()), true, 4000, 7);
        MetalPressRecipes.addRecipe("ingotGold", "ingotGold", new ItemStack(TGItems.WIRE_GOLD.getItem(), 2, TGItems.WIRE_GOLD.getItemDamage()), true);
        MetalPressRecipes.addRecipe("plateIron", new ItemStack(Blocks.TNT, 1), new ItemStack(TGItems.GRENADE_40MM.getItem(), 16, TGItems.GRENADE_40MM.getItemDamage()), true);


        MetalPressRecipes.addRecipe(new ItemStack(TGItems.SNIPER_ROUNDS_INCENDIARY.getItem(), 1, TGItems.SNIPER_ROUNDS_INCENDIARY.getItemDamage()), new ItemStack(TGItems.TGX.getItem(), 1, TGItems.TGX.getItemDamage()), new ItemStack(TGItems.SNIPER_ROUNDS_EXPLOSIVE.getItem(), 1, TGItems.SNIPER_ROUNDS_EXPLOSIVE.getItemDamage()), true);

        //CHEM LAB

        if (ItemUtil.existsInOredict("dustCoal")) {
            ChemLabRecipes.addRecipe("dustRedstone", 1, "dustCoal", 1, null, 0, new FluidStack(FluidRegistry.WATER, 250), null, new ItemStack(Items.GUNPOWDER, 1), true, 5);
        } else {
            ChemLabRecipes.addRecipe("dustRedstone", 1, new ItemStack(Items.COAL), 1, null, 0, new FluidStack(FluidRegistry.WATER, 250), null, new ItemStack(Items.GUNPOWDER, 1), true, 5);
        }
        ItemStackOreDict nullStack = new ItemStackOreDict((ItemStack) null);

        ChemLabRecipes.addSteamRecipe(nullStack, 0, nullStack, 0, null, 0, new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(TGFluids.STEAM, 1500), null, true, 5);

        if (!TGFluids.oils.isEmpty()) {
            TGFluids.oils.forEach(f -> ChemLabRecipes.addRecipe("itemRawRubber", 1, (ItemStack) null, 0, null, 0, new FluidStack(f, 500), null, new ItemStack(TGItems.RAW_PLASTIC.getItem(), 1, TGItems.RAW_PLASTIC.getItemDamage()), false, 25));
        } else {
            ChemLabRecipes.addRecipe("itemRawRubber", 1, new ItemStack(Items.COAL, 1), 1, null, 0, new FluidStack(TGFluids.WATER, 1000), null, new ItemStack(TGItems.RAW_PLASTIC.getItem(), 1, TGItems.RAW_PLASTIC.getItemDamage()), true, 25);
        }

        ChemLabRecipes.addRecipe(TGItems.BIOMASS, 1, new ItemStack(Items.GUNPOWDER), 1, null, 0, new FluidStack(TGFluids.WATER, 1000), new FluidStack(TGFluids.ACID, 1000), null, true, 20);

        if (ItemUtil.existsInOredict("itemBioFuel")) {
            ChemLabRecipes.addRecipe("itemBioFuel", 4, new ItemStack(TGItems.BIO_TANK_EMPTY.getItem(), 1, TGItems.BIO_TANK_EMPTY.getItemDamage()), 1, null, 0, new FluidStack(TGFluids.WATER, 500), null, new ItemStack(TGItems.BIO_TANK.getItem(), 1, TGItems.BIO_TANK.getItemDamage()), true, 1);
            ChemLabRecipes.addRecipe("itemBioFuel", 4, new ItemStack(Items.GUNPOWDER), 1, null, 0, new FluidStack(TGFluids.WATER, 1000), new FluidStack(TGFluids.ACID, 1000), null, true, 20);

        }


        ItemStack fuelTank = new ItemStack(TGItems.FUEL_TANK.getItem(), 1, TGItems.FUEL_TANK.getItemDamage());
        ItemStack fuelTankEmpty = new ItemStack(TGItems.FUEL_TANK_EMPTY.getItem(), 1, TGItems.FUEL_TANK_EMPTY.getItemDamage());
        if (!TGFluids.fuels.isEmpty()) {
            TGFluids.fuels.forEach(f -> {
                ChemLabRecipes.addRecipe("gunpowder", 1, "gemLapis", 1, null, 0, new FluidStack(f, 250), null, new ItemStack(TGItems.TGX.getItem(), 1, TGItems.TGX.getItemDamage()), true, 20);
                ChemLabRecipes.addRecipe((ItemStack) null, 0, (ItemStack) null, 0, fuelTankEmpty, 1, new FluidStack(f, 250), null, fuelTank, false, 1);
                ChemLabRecipes.addRecipe(new ItemStack(TGItems.ROCKET.getItem(), 1, TGItems.ROCKET.getItemDamage()), 1, (ItemStack) null, 0, null, 0, new FluidStack(f, 125), null, new ItemStack(TGItems.ROCKET_HIGH_VELOCITY.getItem(), 1, TGItems.ROCKET_HIGH_VELOCITY.getItemDamage()), false, 5);
            });
        }
        if (TGFluids.fuels.isEmpty() || TGConfig.general.keepLavaRecipesWhenFuelIsPresent) {
            ChemLabRecipes.addRecipe("gunpowder", 1, "gemLapis", 1, null, 0, new FluidStack(FluidRegistry.LAVA, 500), null, new ItemStack(TGItems.TGX.getItem(), 1, TGItems.TGX.getItemDamage()), true, 20);
            ChemLabRecipes.addRecipe(fuelTankEmpty, 1, (ItemStack) null, 0, null, 0, new FluidStack(TGFluids.LAVA, 500), null, fuelTank, false, 1);
            ChemLabRecipes.addRecipe(new ItemStack(TGItems.ROCKET.getItem(), 1, TGItems.ROCKET.getItemDamage()), 1, (ItemStack) null, 0, null, 0, new FluidStack(TGFluids.LAVA, 250), null, new ItemStack(TGItems.ROCKET_HIGH_VELOCITY.getItem(), 1, TGItems.ROCKET_HIGH_VELOCITY.getItemDamage()), false, 5);

        }

        ChemLabRecipes.addRecipe("gemDiamond", 1, new ItemStack(Items.BLAZE_POWDER, 1), 1, null, 0, new FluidStack(TGFluids.LAVA, 1000), null, new ItemStack(TGItems.CARBON_FIBERS.getItem(), 2, TGItems.CARBON_FIBERS.getItemDamage()), true, 25);

        ItemStackOreDict stackLogWood = new ItemStackOreDict("logWood").setNoStrictMode();
        ChemLabRecipes.addRecipe(stackLogWood, 1, nullStack, 0, null, 0, new FluidStack(TGFluids.WATER, 1000), null, new ItemStack(TGItems.RAW_RUBBER.getItem(), 1, TGItems.RAW_RUBBER.getItemDamage()), false, 20);

        ChemLabRecipes.addRecipe(new ItemStack(TGItems.BIOMASS.getItem(), 1, TGItems.BIOMASS.getItemDamage()), 1, (ItemStack) null, 0, new ItemStack(TGItems.BIO_TANK_EMPTY.getItem(), 1, TGItems.BIO_TANK_EMPTY.getItemDamage()), 1, new FluidStack(TGFluids.WATER, 500), null, new ItemStack(TGItems.BIO_TANK.getItem(), 1, TGItems.BIO_TANK.getItemDamage()), false, 1);

        ChemLabRecipes.addRecipe(new ItemStack(Items.COAL, 1), 1, (ItemStack) null, 0, new ItemStack(TGItems.COMPRESSED_AIR_TANK_EMPTY.getItem(), 1, TGItems.COMPRESSED_AIR_TANK_EMPTY.getItemDamage()), 1, new FluidStack(TGFluids.WATER, 250), null, new ItemStack(TGItems.COMPRESSED_AIR_TANK.getItem(), 1, TGItems.COMPRESSED_AIR_TANK.getItemDamage()), true, 5);

        ChemLabRecipes.addRecipe(new ItemStack(Blocks.NETHERRACK), 1, new ItemStack(Blocks.SOUL_SAND), 1, null, 0, new FluidStack(TGFluids.LAVA, 1000), null, new ItemStack(TGItems.NETHER_CHARGE.getItem(), 4, TGItems.NETHER_CHARGE.getItemDamage()), true, 20);

        ItemStackOreDict uranium = new ItemStackOreDict("oreUranium").setNoStrictMode();

        ChemLabRecipes.addRecipe(uranium, 1, nullStack, 0, null, 0, new FluidStack(TGFluids.ACID, 250), null, new ItemStack(TGItems.YELLOWCAKE.getItem(), 3, TGItems.YELLOWCAKE.getItemDamage()), false, 20);

        ChemLabRecipes.addRecipe(TGItems.ENRICHED_URANIUM, 1, (ItemStack) null, 0, TGItems.NUCLEAR_POWERCELL_EMPTY, 1, new FluidStack(TGFluids.WATER, 1000), null, new ItemStack(TGItems.NUCLEAR_POWERCELL.getItem(), 1, TGItems.NUCLEAR_POWERCELL.getItemDamage()), true, 40);

        if (TGFluids.MILK != null) {
            ChemLabRecipes.addRecipe(new ItemStack(Items.DYE, 1, 2), 1, "itemRawRubber", 1, null, 0, new FluidStack(TGFluids.MILK, 500), null, new ItemStack(Items.SLIME_BALL), true, 25);
        }

        ChemLabRecipes.addRecipe(new ItemStack(Items.LEATHER, 2), 2, "slimeball", 1, null, 0, new FluidStack(TGFluids.ACID, 500), null, new ItemStack(TGItems.TREATED_LEATHER.getItem(), 2, TGItems.TREATED_LEATHER.getItemDamage()), true, 20);

        ChemLabRecipes.addRecipe(new ItemStack(Items.ROTTEN_FLESH), 1, (ItemStack) null, 0, null, 0, new FluidStack(TGFluids.WATER, 500), null, new ItemStack(Items.LEATHER, 1), false, 15);

        ChemLabRecipes.addRecipe(new ItemStack(Blocks.GRAVEL), 1, new ItemStack(Blocks.SAND), 1, null, 0, new FluidStack(TGFluids.WATER, 250), null, new ItemStack(Blocks.CONCRETE, 2, 8), false, 5);
        ChemLabRecipes.addRecipe(new ItemStack(Blocks.GRAVEL), 1, new ItemStack(Blocks.DIRT), 1, null, 0, new FluidStack(TGFluids.WATER, 250), null, new ItemStack(Blocks.CLAY, 2), false, 5);


        ChemLabRecipes.addRecipe(TGItems.RIFLE_ROUNDS, 1, new ItemStack(Items.BLAZE_POWDER), 1, null, 0, new FluidStack(TGFluids.LAVA, 250), null, new ItemStack(TGItems.RIFLE_ROUNDS_INCENDIARY.getItem(), 1, TGItems.RIFLE_ROUNDS_INCENDIARY.getItemDamage()), false, 25);
        ChemLabRecipes.addRecipe(TGItems.PISTOL_ROUNDS, 2, new ItemStack(Items.BLAZE_POWDER), 1, null, 0, new FluidStack(TGFluids.LAVA, 250), null, new ItemStack(TGItems.PISTOL_ROUNDS_INCENDIARY.getItem(), 2, TGItems.PISTOL_ROUNDS_INCENDIARY.getItemDamage()), false, 25);
        ChemLabRecipes.addRecipe(TGItems.SNIPER_ROUNDS, 1, new ItemStack(Items.BLAZE_POWDER), 1, null, 0, new FluidStack(TGFluids.LAVA, 250), null, new ItemStack(TGItems.SNIPER_ROUNDS_INCENDIARY.getItem(), 1, TGItems.SNIPER_ROUNDS_INCENDIARY.getItemDamage()), false, 25);
        ChemLabRecipes.addRecipe(TGItems.SHOTGUN_ROUNDS, 8, new ItemStack(Items.BLAZE_POWDER), 1, null, 0, new FluidStack(TGFluids.LAVA, 250), null, new ItemStack(TGItems.SHOTGUN_ROUNDS_INCENDIARY.getItem(), 8, TGItems.SHOTGUN_ROUNDS_INCENDIARY.getItemDamage()), false, 25);

        ChemLabRecipes.addRecipe(new ItemStack(Items.SUGAR), 4, new ItemStack(Items.SPECKLED_MELON), 1, new ItemStack(Items.GLASS_BOTTLE), 1, new FluidStack(TGFluids.MILK, 1000), null, new ItemStack(TGItems.RAD_PILLS, 4), true, 20);
        ChemLabRecipes.addRecipe(new ItemStack(Items.NETHER_WART), 1, new ItemStack(Items.SPECKLED_MELON), 1, new ItemStack(TGItems.INFUSION_BAG.getItem(), 1, TGItems.INFUSION_BAG.getItemDamage()), 1, new FluidStack(TGFluids.ACID, 250), null, new ItemStack(TGItems.RAD_AWAY, 1), true, 25);
		
		
		/*
		  FABRICATOR
		 */
        if (TGConfig.misc.addOreDicts) {
            FabricatorRecipe.addRecipe(new ItemStackOreDict("ingotTitanium"), 2, FabricatorRecipe.circuit_basic, 4, FabricatorRecipe.mechanicalPartsT3, 1, FabricatorRecipe.carbonPlate, 4, TGItems.POWER_ARMOR_PLATING, 2);
        } else {
            FabricatorRecipe.addRecipe(new ItemStackOreDict("ingotTitaniumTG"), 2, FabricatorRecipe.circuit_basic, 4, FabricatorRecipe.mechanicalPartsT3, 1, FabricatorRecipe.carbonPlate, 4, TGItems.POWER_ARMOR_PLATING, 2);
        }
        FabricatorRecipe.addRecipe(new ItemStackOreDict("ingotGold"), 1, FabricatorRecipe.copperWires, 1, FabricatorRecipe.redstone, 3, FabricatorRecipe.plastic, 1, TGItems.ENERGY_CELL_EMPTY, 1);
        FabricatorRecipe.addRecipe(new ItemStackOreDict(new ItemStack(Blocks.SOUL_SAND, 1)), 1, new ItemStackOreDict(TGItems.CYBERNETIC_WIRING), 3, FabricatorRecipe.redstone, 4, FabricatorRecipe.plastic, 2, TGItems.CYBERNETIC_PARTS, 1);
        FabricatorRecipe.addRecipe(new ItemStackOreDict(new ItemStack(TGItems.COIL.getItem(), 1, TGItems.COIL.getItemDamage())), 1, FabricatorRecipe.circuit_elite, 2, FabricatorRecipe.mechanicalPartsT3, 1, FabricatorRecipe.titaniumPlate, 1, TGItems.SONIC_EMITTER, 1);
        FabricatorRecipe.addRecipe(new ItemStackOreDict(new ItemStack(TGItems.ENRICHED_URANIUM.getItem(), 1, TGItems.ENRICHED_URANIUM.getItemDamage())), 1, FabricatorRecipe.circuit_elite, 2, FabricatorRecipe.mechanicalPartsT3, 2, FabricatorRecipe.leadPlate, 2, TGItems.RAD_EMITTER, 1);
        FabricatorRecipe.addRecipe(new ItemStackOreDict("ingotSteel", 2), 1, FabricatorRecipe.circuit_basic, 1, FabricatorRecipe.redstone, 4, FabricatorRecipe.leadPlate, 2, TGItems.NUCLEAR_POWERCELL_EMPTY, 1);
        FabricatorRecipe.addRecipe(new ItemStackOreDict(new ItemStack(TGItems.POWER_ARMOR_PLATING.getItem(), 2, TGItems.POWER_ARMOR_PLATING.getItemDamage())), 1, FabricatorRecipe.circuit_elite, 4, FabricatorRecipe.mechanicalPartsT3, 2, FabricatorRecipe.ingotHellish, 8, TGItems.MK2_POWER_PLATE, 2);
        FabricatorRecipe.addRecipe(new ItemStackOreDict(new ItemStack(TGItems.INGOT_HELLISH.getItem(), 4, TGItems.INGOT_HELLISH.getItemDamage())), 1, FabricatorRecipe.carbonFibers, 4, FabricatorRecipe.mechanicalPartsT3, 2, FabricatorRecipe.titaniumPlate, 2, TGItems.DOOM_PLATE, 2);

		/*
		  Camo Bench
		 */
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.WOOL));
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.CONCRETE));
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.CONCRETE_POWDER));
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.STAINED_HARDENED_CLAY));
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.STAINED_GLASS));
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.STAINED_GLASS_PANE));
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.STANDING_BANNER));
        CamoBenchRecipes.addRecipe(new CamoBenchRecipe(Blocks.CARPET));

        TGBlocks.BLOCKLIST.forEach(b -> {
            if (b instanceof ICamoChangeable) {
                if (((ICamoChangeable) b).addBlockCamoChangeRecipes()) {
                    CamoBenchRecipes.addRecipe(new CamoBenchRecipe((Block) b, (ICamoChangeable) b));
                }
            }
        });
        CamoBenchRecipes.addRecipe(new CamoBenchRecipes.TGLampCamoBenchRecipe(TGBlocks.LAMP_0, 0));
	
		/*
		  Blast Furnace
		 */
        if (TGConfig.misc.addSteelRecipe) {
            BlastFurnaceRecipes.addRecipe(new ItemStack(Items.IRON_INGOT, 4), new ItemStack(Items.COAL, 1), new ItemStack(TGItems.INGOT_STEEL.getItem(), 4, TGItems.INGOT_STEEL.getItemDamage()), 10, 800);
            BlastFurnaceRecipes.addRecipe(new ItemStack(Items.IRON_INGOT, 4), new ItemStack(Items.COAL, 1, 1), new ItemStack(TGItems.INGOT_STEEL.getItem(), 4, TGItems.INGOT_STEEL.getItemDamage()), 10, 800);
        }
        BlastFurnaceRecipes.addRecipe("ingotSteel", 1, new ItemStack(Blocks.OBSIDIAN, 1), new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 1, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), 10, 200);
        BlastFurnaceRecipes.addRecipe("ingotCopper", 3, "ingotTin", 1, new ItemStack(TGItems.INGOT_BRONZE.getItem(), 4, TGItems.INGOT_BRONZE.getItemDamage()), 10, 100);
		
		/*
		  CHARGING STATION
		 */
        ChargingStationRecipe.addRecipe(new ItemStackOreDict(TGItems.ENERGY_CELL_EMPTY), TGItems.ENERGY_CELL, 50000);
        ChargingStationRecipe.addRecipe(new ItemStackOreDict(TGItems.REDSTONE_BATTERY_EMPTY), TGItems.REDSTONE_BATTERY, 20000);


		/*
		  REACTION CHAMBER
		 */
        // Beam focus
        ReactionBeamFocus.addBeamFocus(TGItems.RC_HEAT_RAY, 100, TGSounds.REACTION_CHAMBER_HEATRAY_WORK);
        ReactionBeamFocus.addBeamFocus(TGItems.RC_UV_EMITTER, 100, TGSounds.CHEM_LAB_WORK);

        // Recipes
        // ID (STR), INPUT-STACK, FOCUS, FLUID_TYPE, ITEM_OUTPUTS, TICKS, REQ_COMPLETION, INTENSITY, INTENSITY_MARGIN, LIQUIDLEVEL, LIQUID_CONSUMPTION, INSTABILITY_CHANCE, RISK, RF_TICK
        ReactionChamberRecipe.addRecipe("RC_UV_WHEAT", new ItemStackOreDict(new ItemStack(Items.WHEAT_SEEDS, 1)), TGItems.RC_UV_EMITTER, FluidRegistry.WATER, new ItemStack[]{new ItemStack(Items.WHEAT, 3), new ItemStack(Items.WHEAT_SEEDS, 6)}, 10, 5, 3, 1, 1, 1000, 0.5f, RiskType.EXPLOSION_LOW, 30000);
        ReactionChamberRecipe.addRecipe("RC_UV_CARROT", new ItemStackOreDict(new ItemStack(Items.CARROT, 1)), TGItems.RC_UV_EMITTER, FluidRegistry.WATER, new ItemStack[]{new ItemStack(Items.CARROT, 8)}, 10, 5, 3, 1, 1, 1000, 0.5f, RiskType.EXPLOSION_LOW, 30000);
        ReactionChamberRecipe.addRecipe("RC_UV_POTATO", new ItemStackOreDict(new ItemStack(Items.POTATO, 1)), TGItems.RC_UV_EMITTER, FluidRegistry.WATER, new ItemStack[]{new ItemStack(Items.POTATO, 8)}, 10, 5, 3, 1, 1, 1000, 0.5f, RiskType.EXPLOSION_LOW, 30000);
        ReactionChamberRecipe.addRecipe("RC_UV_POTATOPOISONED", new ItemStackOreDict(new ItemStack(Items.POISONOUS_POTATO, 1)), TGItems.RC_UV_EMITTER, FluidRegistry.WATER, new ItemStack[]{new ItemStack(Items.POTATO, 4)}, 10, 5, 3, 1, 1, 1000, 0.5f, RiskType.EXPLOSION_LOW, 30000);

        ReactionChamberRecipe.addRecipe("RC_LASER_FOCUS", new ItemStackOreDict("gemDiamond", 1), TGItems.RC_HEAT_RAY, TGFluids.LIQUID_REDSTONE, new ItemStack[]{new ItemStack(TGItems.LASER_FOCUS.getItem(), 1, TGItems.LASER_FOCUS.getItemDamage())}, 10, 5, 3, 1, 4, 4000, 0.5f, RiskType.BREAK_ITEM, 100000);

        ReactionChamberRecipe.addRecipe("RC_TITANIUM", new ItemStackOreDict(new ItemStack(TGBlocks.TG_ORE, 1, EnumOreType.ORE_TITANIUM.ordinal())), TGItems.RC_HEAT_RAY, TGFluids.ACID, new ItemStack[]{new ItemStack(TGItems.ORE_TITANIUM.getItem(), 2, TGItems.ORE_TITANIUM.getItemDamage()), new ItemStack(Blocks.IRON_ORE, 1)}, 2, 1, 5, 0, 3, 100, 0f, RiskType.BREAK_ITEM, 25000);

        ReactionChamberRecipe.addRecipe("RC_BLAZEROD", new ItemStackOreDict(new ItemStack(TGItems.QUARTZ_ROD.getItem(), 1, TGItems.QUARTZ_ROD.getItemDamage())), TGItems.RC_HEAT_RAY, TGFluids.LAVA, new ItemStack[]{new ItemStack(Items.BLAZE_ROD, 1)}, 5, 3, 7, 2, 4, 1000, 0.5f, RiskType.BREAK_ITEM, 250000);

        ReactionChamberRecipe.addRecipe("RC_GLOWSTONE", new ItemStackOreDict("blockRedstone", 1), TGItems.RC_HEAT_RAY, TGFluids.LAVA, new ItemStack[]{new ItemStack(Blocks.GLOWSTONE, 1)}, 5, 3, 7, 2, 4, 1000, 0.5f, RiskType.EXPLOSION_MEDIUM, 250000);

        ReactionChamberRecipe.addRecipe("RC_ANTIGRAV", new ItemStackOreDict(new ItemStack(Items.NETHER_STAR, 1)), TGItems.RC_HEAT_RAY, TGFluids.LIQUID_ENDER, new ItemStack[]{new ItemStack(TGItems.ANTI_GRAV_CORE.getItem(), 1, TGItems.ANTI_GRAV_CORE.getItemDamage())}, 10, 7, 8, 2, 4, 4000, 1f, RiskType.EXPLOSION_LOW, 500000);

        ReactionChamberRecipe.addRecipe("RC_HELLISHINGOT", new ItemStackOreDict(new ItemStack(TGItems.INGOT_TITANIUM.getItem(), 1, TGItems.INGOT_TITANIUM.getItemDamage())), TGItems.RC_UV_EMITTER, TGFluids.LIQUID_TRITIUM, new ItemStack[]{new ItemStack(TGItems.INGOT_HELLISH.getItem(), 1, TGItems.INGOT_HELLISH.getItemDamage())}, 5, 3, 6, 0, 7, 2000, 0f, RiskType.EXPLOSION_MEDIUM, 100000);

        ReactionChamberRecipe.addRecipe("RC_URANIUM", new ItemStackOreDict(new ItemStack(TGItems.YELLOWCAKE.getItem(), 1, TGItems.YELLOWCAKE.getItemDamage())), TGItems.RC_HEAT_RAY, TGFluids.WATER, new ItemStack[]{new ItemStack(TGItems.ENRICHED_URANIUM.getItem(), 1, TGItems.ENRICHED_URANIUM.getItemDamage())}, 5, 4, 7, 0, 3, 1000, 0f, RiskType.BREAK_ITEM, 250000);


        //Smelting recipes
        GameRegistry.addSmelting(TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_COPPER), TGItems.INGOT_COPPER, 0.5f);
        GameRegistry.addSmelting(TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_TIN), TGItems.INGOT_TIN, 0.5f);
        GameRegistry.addSmelting(TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_LEAD), TGItems.INGOT_LEAD, 1.0f);

        GameRegistry.addSmelting(TGItems.TURRET_ARMOR_IRON, new ItemStack(Items.IRON_INGOT, 5), 0f);
        GameRegistry.addSmelting(TGItems.TURRET_ARMOR_STEEL, new ItemStack(TGItems.INGOT_STEEL.getItem(), 5, TGItems.INGOT_STEEL.getItemDamage()), 0f);
        GameRegistry.addSmelting(TGItems.TURRET_ARMOR_OBSIDIAN_STEEL, new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 5, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), 0f);

        GameRegistry.addSmelting(TGItems.BARREL_IRON, new ItemStack(Items.IRON_INGOT, 6), 0f);
        GameRegistry.addSmelting(TGItems.BARREL_OBSIDIAN_STEEL, new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 6, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), 0f);

        GameRegistry.addSmelting(TGItems.PLATE_BRONZE, TGItems.INGOT_BRONZE, 0f);
        GameRegistry.addSmelting(TGItems.PLATE_COPPER, TGItems.INGOT_COPPER, 0f);
        GameRegistry.addSmelting(TGItems.PLATE_TIN, TGItems.INGOT_TIN, 0f);
        GameRegistry.addSmelting(TGItems.PLATE_LEAD, TGItems.INGOT_LEAD, 0f);
        GameRegistry.addSmelting(TGItems.PLATE_IRON, new ItemStack(Items.IRON_INGOT, 1), 0f);
        GameRegistry.addSmelting(TGItems.PLATE_OBSIDIAN_STEEL, TGItems.INGOT_OBSIDIAN_STEEL, 0f);
        GameRegistry.addSmelting(TGItems.PLATE_TITANIUM, TGItems.INGOT_TITANIUM, 0f);
        GameRegistry.addSmelting(TGItems.PLATE_STEEL, TGItems.INGOT_STEEL, 0f);

        GameRegistry.addSmelting(TGItems.RAW_RUBBER, TGItems.RUBBER_BAR, 0);
        GameRegistry.addSmelting(TGItems.RAW_PLASTIC, TGItems.PLASTIC_SHEET, 0);

        GameRegistry.addSmelting(TGItems.ORE_TITANIUM, TGItems.INGOT_TITANIUM, 0);

        //brewing
        ItemStack awkwardPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD);
        ItemStack radPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TGRadiationSystem.RAD_POTION);

        BrewingRecipeRegistry.addRecipe(awkwardPotion, "dustUranium", radPotion);
        BrewingRecipeRegistry.addRecipe(awkwardPotion, new ItemStack(TGItems.ENRICHED_URANIUM.getItem(), 1, TGItems.ENRICHED_URANIUM.getItemDamage()), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TGRadiationSystem.RAD_POTION_SEVERE));

        BrewingRecipeRegistry.addRecipe(awkwardPotion, "ingotLead", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TGRadiationSystem.RAD_RESISTANCE_POTION));

        BrewingRecipeRegistry.addRecipe(radPotion, new ItemStack(Items.GOLDEN_CARROT), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TGRadiationSystem.RAD_REGENERATION_POTION));
        BrewingRecipeRegistry.addRecipe(radPotion, new ItemStack(Items.SPECKLED_MELON), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), TGRadiationSystem.RAD_REGENERATION_POTION));
	
		/*
		  Grinder recipes
		 */

        GrinderRecipes.addGenericArmorRecipes();

        //some pre-defined things
        ItemStack[] BARREL_STONE = {new ItemStack(Blocks.COBBLESTONE, 3)};
        ItemStack[] BARREL_OBSI = {new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 3, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage())};

        GrinderRecipes.addRecipe(TGuns.handcannon, BARREL_STONE, ironAndWood(0, 1, 4));
        GrinderRecipes.addRecipe(TGuns.sawedoff, ironAndWood(4, 1, 0), new ItemStack[]{new ItemStack(Items.FLINT, 1)});
        GrinderRecipes.addRecipe(TGuns.revolver, ironAndWood(3, 1, 0));
        GrinderRecipes.addRecipe(TGuns.goldenrevolver, ironAndWood(3, 1, 0), new ItemStack[]{new ItemStack(Items.GOLD_INGOT, 4)});
        GrinderRecipes.addRecipe(TGuns.thompson, ironAndWood(4, 1, 0));
        GrinderRecipes.addRecipe(TGuns.ak47, BARREL_OBSI, ironAndWood(4, 1, 0));
        GrinderRecipes.addRecipe(TGuns.boltaction, BARREL_OBSI, ironAndWood(4, 1, 0));
        GrinderRecipes.addRecipe(TGuns.m4, BARREL_OBSI, steelAndPlastic(2, 2, 0));
        GrinderRecipes.addRecipe(TGuns.m4_infiltrator, BARREL_OBSI, steelAndPlastic(3, 2, 0), new ItemStack[]{new ItemStack(Items.REDSTONE, 1)});
        GrinderRecipes.addRecipe(TGuns.pistol, BARREL_OBSI, steelAndPlastic(0, 1, 0), ironAndWood(2, 0, 0));
        GrinderRecipes.addRecipe(TGuns.combatshotgun, BARREL_OBSI, steelAndPlastic(2, 2, 0));
        GrinderRecipes.addRecipe(TGuns.mac10, BARREL_OBSI, steelAndPlastic(1, 0, 5), ironAndWood(1, 0, 0));
        GrinderRecipes.addRecipe(TGuns.flamethrower, steelAndPlastic(0, 2, 0), ironAndWood(2, 0, 0));
        GrinderRecipes.addRecipe(TGuns.rocketlauncher, new ItemStack[]{new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 6, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage())}, steelAndPlastic(1, 0, 5));
        GrinderRecipes.addRecipe(TGuns.grimreaper, new ItemStack[]{new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 32, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), new ItemStack(TGItems.CIRCUIT_BOARD_ELITE.getItem(), 1, TGItems.CIRCUIT_BOARD_ELITE.getItemDamage()), new ItemStack(TGItems.CARBON_FIBERS.getItem(), 1, TGItems.CARBON_FIBERS.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.grenadelauncher, BARREL_OBSI, steelAndPlastic(2, 2, 0));
        GrinderRecipes.addRecipe(TGuns.aug, BARREL_OBSI, steelAndPlastic(2, 2, 0));
        GrinderRecipes.addRecipe(TGuns.netherblaster, new ItemStack[]{new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 4, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), new ItemStack(TGItems.CYBERNETIC_PARTS.getItem(), 2, TGItems.CYBERNETIC_PARTS.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.biogun, steelAndPlastic(2, 2, 0), new ItemStack[]{new ItemStack(Blocks.GLASS, 2)});
        GrinderRecipes.addRecipe(TGuns.teslagun, carbonPlasticAndRedstone(3, 2, 2));
        GrinderRecipes.addRecipe(TGuns.lmg, new ItemStack[]{new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 6, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), new ItemStack(TGItems.INGOT_STEEL.getItem(), 4, TGItems.INGOT_STEEL.getItemDamage()), new ItemStack(TGItems.PLASTIC_SHEET.getItem(), 2, TGItems.PLASTIC_SHEET.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.minigun, new ItemStack[]{new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 20, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), new ItemStack(TGItems.INGOT_STEEL.getItem(), 2, TGItems.INGOT_STEEL.getItemDamage()), new ItemStack(TGItems.ELECTRIC_ENGINE.getItem(), 1, TGItems.ELECTRIC_ENGINE.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.as50, new ItemStack[]{new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 9, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), new ItemStack(TGItems.PLASTIC_SHEET.getItem(), 2, TGItems.PLASTIC_SHEET.getItemDamage()), new ItemStack(Items.DIAMOND, 1)});
        GrinderRecipes.addRecipe(TGuns.vector, obsiAndPlastic(5, 2));
        GrinderRecipes.addRecipe(TGuns.scar, obsiAndPlastic(5, 2));
        GrinderRecipes.addRecipe(TGuns.lasergun, obsiAndPlastic(2, 2), new ItemStack[]{new ItemStack(Items.REDSTONE, 20), new ItemStack(GOLD_OR_ELECTRUM.getItem(), 3, GOLD_OR_ELECTRUM.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.blasterrifle, carbonPlasticAndRedstone(3, 1, 20), new ItemStack[]{new ItemStack(GOLD_OR_ELECTRUM.getItem(), 3, GOLD_OR_ELECTRUM.getItemDamage())});
        //TODO: BlasterShotgun NoREcipe
        GrinderRecipes.addRecipe(TGuns.sonicshotgun, carbonPlasticAndRedstone(3, 2, 2), new ItemStack[]{new ItemStack(TGItems.INGOT_TITANIUM.getItem(), 1, TGItems.INGOT_TITANIUM.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.pdw, carbonPlasticAndRedstone(6, 1, 0), obsiAndPlastic(1, 0));
        GrinderRecipes.addRecipe(TGuns.pulserifle, carbonPlasticAndRedstone(6, 1, 0), obsiAndPlastic(1, 0));
        GrinderRecipes.addRecipe(TGuns.mibgun, new ItemStack[]{new ItemStack(GOLD_OR_ELECTRUM.getItem(), 3, GOLD_OR_ELECTRUM.getItemDamage()), new ItemStack(TGItems.INGOT_TITANIUM.getItem(), 1, TGItems.INGOT_TITANIUM.getItemDamage()), new ItemStack(Items.REDSTONE, 20)});
        GrinderRecipes.addRecipe(TGuns.powerhammer, ironAndWood(4, 0, 0), new ItemStack[]{new ItemStack(Items.FLINT, 1)});
        GrinderRecipes.addRecipe(TGuns.chainsaw, ironAndWood(5, 0, 0), new ItemStack[]{new ItemStack(TGItems.PLASTIC_SHEET.getItem(), 1, TGItems.PLASTIC_SHEET.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.nucleardeathray, new ItemStack[]{new ItemStack(TGItems.CARBON_FIBERS.getItem(), 6, TGItems.CARBON_FIBERS.getItemDamage()), new ItemStack(TGItems.INGOT_LEAD.getItem(), 3, TGItems.INGOT_LEAD.getItemDamage()), new ItemStack(TGItems.MECHANICAL_PARTS_CARBON.getItem(), 1, TGItems.MECHANICAL_PARTS_CARBON.getItemDamage()), new ItemStack(TGItems.CIRCUIT_BOARD_ELITE.getItem(), 1, TGItems.CIRCUIT_BOARD_ELITE.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.gaussrifle, new ItemStack[]{new ItemStack(TGItems.CARBON_FIBERS.getItem(), 9, TGItems.CARBON_FIBERS.getItemDamage()), new ItemStack(TGItems.INGOT_TITANIUM.getItem(), 1, TGItems.INGOT_TITANIUM.getItemDamage()), new ItemStack(TGItems.PLASTIC_SHEET.getItem(), 1, TGItems.PLASTIC_SHEET.getItemDamage()), new ItemStack(TGItems.CIRCUIT_BOARD_ELITE.getItem(), 1, TGItems.CIRCUIT_BOARD_ELITE.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.guidedmissilelauncher, new ItemStack[]{new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), 8, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage()), new ItemStack(TGItems.WIRE_COPPER.getItem(), 1, TGItems.WIRE_COPPER.getItemDamage())});
        GrinderRecipes.addRecipe(TGuns.miningdrill, obsiAndPlastic(6, 2));
        GrinderRecipes.addRecipe(TGuns.tfg, new ItemStack[]{new ItemStack(TGItems.INGOT_TITANIUM.getItem(), 5, TGItems.INGOT_TITANIUM.getItemDamage()), new ItemStack(TGItems.INGOT_LEAD.getItem(), 4, TGItems.INGOT_LEAD.getItemDamage()), new ItemStack(TGItems.CARBON_FIBERS.getItem(), 4, TGItems.CARBON_FIBERS.getItemDamage())});

        GrinderRecipes.addRecipe(TGItems.PLATE_CARBON, new ItemStack(TGItems.CARBON_FIBERS.getItem(), 1, TGItems.CARBON_FIBERS.getItemDamage()));

        //Ammo crush
        GrinderRecipes.addRecipeChance(TGItems.RIFLE_ROUNDS, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 1, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 2, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER)}, new double[]{1d, 1d, 0.125d});
        GrinderRecipes.addRecipeChance(TGItems.PISTOL_ROUNDS, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 1, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 1, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER)}, new double[]{0.75d, 1.5d, 1.0d / 12.0d});
        GrinderRecipes.addRecipeChance(TGItems.SNIPER_ROUNDS, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 2, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 4, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER)}, new double[]{1d, 1d, 0.25d});
        GrinderRecipes.addRecipeChance(TGItems.SHOTGUN_ROUNDS, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 1, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 1, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER)}, new double[]{0.5d, 1d, 0.0625d});

        GrinderRecipes.addRecipeChance(TGItems.RIFLE_ROUNDS_INCENDIARY, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 1, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 2, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.BLAZE_POWDER)}, new double[]{1d, 1d, 0.125d, 0.125d});
        GrinderRecipes.addRecipeChance(TGItems.PISTOL_ROUNDS_INCENDIARY, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 1, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 1, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.BLAZE_POWDER)}, new double[]{0.75d, 1.5d, 1.0d / 12.0d, 0.125d});
        GrinderRecipes.addRecipeChance(TGItems.SNIPER_ROUNDS_INCENDIARY, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 2, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 4, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.BLAZE_POWDER)}, new double[]{1d, 1d, 0.25d, 0.125d});
        GrinderRecipes.addRecipeChance(TGItems.SHOTGUN_ROUNDS_INCENDIARY, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 1, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 1, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.BLAZE_POWDER)}, new double[]{0.5d, 1d, 0.0625d, 0.125d});

        GrinderRecipes.addRecipeChance(TGItems.SNIPER_ROUNDS_EXPLOSIVE, new ItemStack[]{new ItemStack(TGItems.NUGGET_LEAD.getItem(), 2, TGItems.NUGGET_LEAD.getItemDamage()), new ItemStack(TGItems.NUGGET_COPPER.getItem(), 4, TGItems.NUGGET_COPPER.getItemDamage()), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.BLAZE_POWDER), new ItemStack(TGItems.TGX.getItem(), 1, TGItems.TGX.getItemDamage())}, new double[]{1d, 1d, 0.25d, 0.125d, 0.5d});

        GrinderRecipes.addRecipeChance(TGItems.CYBERDEMON_FLESH, new ItemStack[]{new ItemStack(Items.ROTTEN_FLESH, 1), new ItemStack(TGItems.CYBERNETIC_WIRING.getItem(), 1, TGItems.CYBERNETIC_WIRING.getItemDamage()), new ItemStack(Items.GOLD_NUGGET)}, new double[]{1d, 0.2d, 0.25d});

        GrinderRecipes.addAllVanillaStyleOreToDustRecipesAuto(2, 1, 0.20d);
        /**
         * Upgrade bench recipes
         */
        //UpgradeBenchRecipes.addRecipe(TGItems.PLATE_CARBON, Enchantments.PROTECTION, 4);

        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_PROTECTION_1, Enchantments.PROTECTION, 1);
        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_PROJECTILE_PROTECTION_1, Enchantments.PROJECTILE_PROTECTION, 1);
        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_BLAST_PROTECTION_1, Enchantments.BLAST_PROTECTION, 1);

        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_PROTECTION_2, Enchantments.PROTECTION, 2);
        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_PROJECTILE_PROTECTION_2, Enchantments.PROJECTILE_PROTECTION, 2);
        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_BLAST_PROTECTION_2, Enchantments.BLAST_PROTECTION, 2);

        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_PROTECTION_3, Enchantments.PROTECTION, 3);
        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_PROJECTILE_PROTECTION_3, Enchantments.PROJECTILE_PROTECTION, 3);
        UpgradeBenchRecipes.addRecipe(TGItems.UPGRADE_BLAST_PROTECTION_3, Enchantments.BLAST_PROTECTION, 3);
    }


    private static ItemStack[] ironAndWood(int iron, int wood, int nuggets) {
        int size = (iron > 0 ? 1 : 0) + (wood > 0 ? 1 : 0) + (nuggets > 0 ? 1 : 0);
        ItemStack[] ret = new ItemStack[size];
        int i = 0;
        if (iron > 0) {
            ret[i++] = new ItemStack(Items.IRON_INGOT, iron);
        }
        if (wood > 0) {
            ret[i++] = new ItemStack(Blocks.LOG, wood);
        }
        if (nuggets > 0) {
            ret[i++] = new ItemStack(Items.IRON_NUGGET, nuggets);
        }
        return ret;
    }

    private static ItemStack[] steelAndPlastic(int steel, int plastic, int nuggets) {
        int size = (steel > 0 ? 1 : 0) + (plastic > 0 ? 1 : 0) + (nuggets > 0 ? 1 : 0);
        ItemStack[] ret = new ItemStack[size];
        int i = 0;
        if (steel > 0) {
            ret[i++] = new ItemStack(TGItems.INGOT_STEEL.getItem(), steel, TGItems.INGOT_STEEL.getItemDamage());
        }
        if (plastic > 0) {
            ret[i++] = new ItemStack(TGItems.PLASTIC_SHEET.getItem(), plastic, TGItems.PLASTIC_SHEET.getItemDamage());
        }
        if (nuggets > 0) {
            ret[i++] = new ItemStack(TGItems.INGOT_STEEL.getItem(), nuggets, TGItems.INGOT_STEEL.getItemDamage());
        }
        return ret;
    }

    private static ItemStack[] obsiAndPlastic(int steel, int plastic) {
        int size = (steel > 0 ? 1 : 0) + (plastic > 0 ? 1 : 0);
        ItemStack[] ret = new ItemStack[size];
        int i = 0;
        if (steel > 0) {
            ret[i++] = new ItemStack(TGItems.INGOT_OBSIDIAN_STEEL.getItem(), steel, TGItems.INGOT_OBSIDIAN_STEEL.getItemDamage());
        }
        if (plastic > 0) {
            ret[i++] = new ItemStack(TGItems.PLASTIC_SHEET.getItem(), plastic, TGItems.PLASTIC_SHEET.getItemDamage());
        }
        return ret;
    }

    private static ItemStack[] carbonPlasticAndRedstone(int carbon, int plastic, int redstone) {
        int size = (carbon > 0 ? 1 : 0) + (plastic > 0 ? 1 : 0) + (redstone > 0 ? 1 : 0);
        ItemStack[] ret = new ItemStack[size];
        int i = 0;
        if (carbon > 0) {
            ret[i++] = new ItemStack(TGItems.CARBON_FIBERS.getItem(), carbon, TGItems.CARBON_FIBERS.getItemDamage());
        }
        if (plastic > 0) {
            ret[i++] = new ItemStack(TGItems.PLASTIC_SHEET.getItem(), plastic, TGItems.PLASTIC_SHEET.getItemDamage());
        }
        if (redstone > 0) {
            ret[i++] = new ItemStack(Items.REDSTONE, redstone);
        }
        return ret;
    }
}