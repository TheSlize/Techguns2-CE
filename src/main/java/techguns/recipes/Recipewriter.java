package techguns.recipes;

import static techguns.TGArmors.*;
import static techguns.TGItems.ADVANCED_MAGAZINE;
import static techguns.TGItems.ADVANCED_MAGAZINE_EMPTY;
import static techguns.TGItems.ADVANCED_ROUNDS;
import static techguns.TGItems.AS50_MAGAZINE;
import static techguns.TGItems.AS50_MAGAZINE_EMPTY;
import static techguns.TGItems.AS50_MAGAZINE_INCENDIARY;
import static techguns.TGItems.ASSAULTRIFLE_MAGAZINE;
import static techguns.TGItems.ASSAULTRIFLE_MAGAZINE_EMPTY;
import static techguns.TGItems.ASSAULTRIFLE_MAGAZINE_INCENDIARY;
import static techguns.TGItems.BARREL_CARBON;
import static techguns.TGItems.BARREL_IRON;
import static techguns.TGItems.BARREL_LASER;
import static techguns.TGItems.BARREL_OBSIDIAN_STEEL;
import static techguns.TGItems.BARREL_STONE;
import static techguns.TGItems.BIOMASS;
import static techguns.TGItems.BIO_TANK_EMPTY;
import static techguns.TGItems.CIRCUIT_BOARD_ELITE;
import static techguns.TGItems.COIL;
import static techguns.TGItems.COMPRESSED_AIR_TANK_EMPTY;
import static techguns.TGItems.ELECTRIC_ENGINE;
import static techguns.TGItems.FUEL_TANK_EMPTY;
import static techguns.TGItems.GLIDER;
import static techguns.TGItems.GLIDER_BACKBACK;
import static techguns.TGItems.GLIDER_WING;
import static techguns.TGItems.HEAVY_CLOTH;
import static techguns.TGItems.INGOT_OBSIDIAN_STEEL;
import static techguns.TGItems.LASER_FOCUS;
import static techguns.TGItems.LMG_MAGAZINE;
import static techguns.TGItems.LMG_MAGAZINE_EMPTY;
import static techguns.TGItems.LMG_MAGAZINE_INCENDIARY;
import static techguns.TGItems.MACHINE_UPGRADE_STACK;
import static techguns.TGItems.MECHANICAL_PARTS_CARBON;
import static techguns.TGItems.MECHANICAL_PARTS_IRON;
import static techguns.TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL;
import static techguns.TGItems.MINIGUN_DRUM;
import static techguns.TGItems.MINIGUN_DRUM_EMPTY;
import static techguns.TGItems.MINIGUN_DRUM_INCENDIARY;
import static techguns.TGItems.NIGHTVISION_GOGGLES;
import static techguns.TGItems.OXYGEN_MASK;
import static techguns.TGItems.PISTOL_MAGAZINE;
import static techguns.TGItems.PISTOL_MAGAZINE_EMPTY;
import static techguns.TGItems.PISTOL_MAGAZINE_INCENDIARY;
import static techguns.TGItems.PISTOL_ROUNDS;
import static techguns.TGItems.PISTOL_ROUNDS_INCENDIARY;
import static techguns.TGItems.PUMP_MECHANISM;
import static techguns.TGItems.RECEIVER_CARBON;
import static techguns.TGItems.RECEIVER_IRON;
import static techguns.TGItems.RECEIVER_OBSIDIAN_STEEL;
import static techguns.TGItems.RECEIVER_STEEL;
import static techguns.TGItems.RIFLE_ROUNDS;
import static techguns.TGItems.RIFLE_ROUNDS_INCENDIARY;
import static techguns.TGItems.RIFLE_ROUNDS_STACK;
import static techguns.TGItems.RIFLE_ROUNDS_STACK_INCENDIARY;
import static techguns.TGItems.ROCKET;
import static techguns.TGItems.SHOTGUN_ROUNDS;
import static techguns.TGItems.SMG_MAGAZINE;
import static techguns.TGItems.SMG_MAGAZINE_EMPTY;
import static techguns.TGItems.SMG_MAGAZINE_INCENDIARY;
import static techguns.TGItems.SNIPER_ROUNDS;
import static techguns.TGItems.SNIPER_ROUNDS_INCENDIARY;
import static techguns.TGItems.STOCK_CARBON;
import static techguns.TGItems.STOCK_PLASTIC;
import static techguns.TGItems.STOCK_WOOD;
import static techguns.TGItems.STONE_BULLETS;
import static techguns.TGItems.TURRET_ARMOR_CARBON;
import static techguns.TGItems.TURRET_ARMOR_IRON;
import static techguns.TGItems.TURRET_ARMOR_OBSIDIAN_STEEL;
import static techguns.TGItems.TURRET_ARMOR_STEEL;
import static techguns.TGItems.WIRE_COPPER;
import static techguns.TGItems.*;
import static techguns.TGuns.ak47;
import static techguns.TGuns.as50;
import static techguns.TGuns.aug;
import static techguns.TGuns.biogun;
import static techguns.TGuns.blasterrifle;
import static techguns.TGuns.boltaction;
import static techguns.TGuns.chainsaw;
import static techguns.TGuns.combatshotgun;
import static techguns.TGuns.flamethrower;
import static techguns.TGuns.fraggrenade;
import static techguns.TGuns.gaussrifle;
import static techguns.TGuns.goldenrevolver;
import static techguns.TGuns.grenadelauncher;
import static techguns.TGuns.grimreaper;
import static techguns.TGuns.guidedmissilelauncher;
import static techguns.TGuns.handcannon;
import static techguns.TGuns.lasergun;
import static techguns.TGuns.lmg;
import static techguns.TGuns.m4;
import static techguns.TGuns.m4_infiltrator;
import static techguns.TGuns.mac10;
import static techguns.TGuns.mibgun;
import static techguns.TGuns.minigun;
import static techguns.TGuns.miningdrill;
import static techguns.TGuns.netherblaster;
import static techguns.TGuns.nucleardeathray;
import static techguns.TGuns.pdw;
import static techguns.TGuns.pistol;
import static techguns.TGuns.powerhammer;
import static techguns.TGuns.pulserifle;
import static techguns.TGuns.revolver;
import static techguns.TGuns.rocketlauncher;
import static techguns.TGuns.sawedoff;
import static techguns.TGuns.scar;
import static techguns.TGuns.sonicshotgun;
import static techguns.TGuns.stielgranate;
import static techguns.TGuns.teslagun;
import static techguns.TGuns.thompson;
import static techguns.TGuns.vector;
import static techguns.TGuns.*;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techguns.TGArmors;
import techguns.TGBlocks;
import techguns.TGItems;
import techguns.TGuns;
import techguns.blocks.EnumConcreteType;
import techguns.blocks.EnumDoorType;
import techguns.blocks.TGMetalPanelType;
import techguns.blocks.machines.EnumExplosiveChargeType;
import techguns.blocks.machines.EnumMachineType;
import techguns.blocks.machines.EnumMultiBlockMachineType;
import techguns.blocks.machines.EnumOreDrillType;
import techguns.blocks.machines.EnumSimpleMachineType;
import techguns.blocks.machines.EnumSimpleMachineType2;
import techguns.items.guns.GenericGun;
import techguns.items.guns.ammo.AmmoType;

public class Recipewriter {

	public static final String hardenedGlassOrGlass = "hardenedGlassOrGlass";
	public static final String itemStackHasNBTInt = "itemstackHasNBTInt";
	
	public static void writeRecipes() {
		
		registerItemRecipes();
		
		
		RecipeJsonConverter.generateConstants();
		RecipeJsonConverter.generateFactories();
		
	}
	
	public static void registerItemRecipes(){
		RecipeJsonConverter.addShapedRecipe(new ItemStack(BARREL_STONE.getItem(), 1, BARREL_STONE.getItemDamage()), "sss","   ","sss", 's', "stone");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(STOCK_WOOD.getItem(), 1, STOCK_WOOD.getItemDamage()), "ww", " w", 'w', "logWood");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(BARREL_IRON.getItem(), 1, BARREL_IRON.getItemDamage()), "iii","   ","iii", 'i', "ingotIron");
   
        RecipeJsonConverter.addShapedRecipe(new ItemStack(MECHANICAL_PARTS_IRON.getItem(), 1, MECHANICAL_PARTS_IRON.getItemDamage())," i ", "ifi", " i ", 'f', Items.FLINT, 'i', "ingotIron");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(STOCK_PLASTIC.getItem(), 1, STOCK_PLASTIC.getItemDamage()),"ppp","  p", 'p', "sheetPlastic");
             
        RecipeJsonConverter.addShapedRecipe(new ItemStack(BARREL_OBSIDIAN_STEEL.getItem(), 1, BARREL_OBSIDIAN_STEEL.getItemDamage()), "ooo","   ","ooo", 'o', INGOT_OBSIDIAN_STEEL);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(PUMP_MECHANISM.getItem(), 1, PUMP_MECHANISM.getItemDamage()), "nnn", "gpg", "nnn", 'n', "nuggetIron", 'g', "blockGlass", 'p', Blocks.PISTON);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(BARREL_LASER.getItem(), 1, BARREL_LASER.getItemDamage()), "fff","ggl","fff", 'f', "ingotGold", 'g', hardenedGlassOrGlass, 'l', LASER_FOCUS);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(HEAVY_CLOTH.getItem(), 3, HEAVY_CLOTH.getItemDamage()), " w ","wlw"," w ", 'w', Blocks.WOOL, 'l', Items.LEATHER);
        
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(STONE_BULLETS.getItem(), 16, STONE_BULLETS.getItemDamage()), "cobblestone", "cobblestone", "cobblestone", Items.GUNPOWDER);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(PISTOL_ROUNDS.getItem(), 8, PISTOL_ROUNDS.getItemDamage()), "clc","cgc","ccc", 'c', "nuggetCopper", 'l', "ingotLead", 'g', Items.GUNPOWDER);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(SHOTGUN_ROUNDS.getItem(), 5, SHOTGUN_ROUNDS.getItemDamage()), "lll","cgc","ccc", 'c', "nuggetCopper", 'l', "nuggetLead", 'g', Items.GUNPOWDER);
        
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(PISTOL_MAGAZINE_EMPTY.getItem(), 4, PISTOL_MAGAZINE_EMPTY.getItemDamage()), "i i","imi","igi", 'i', "nuggetIron",'g', "ingotIron", 'm', MECHANICAL_PARTS_IRON);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(PISTOL_MAGAZINE.getItem(), 1, PISTOL_MAGAZINE.getItemDamage()), PISTOL_MAGAZINE_EMPTY, PISTOL_ROUNDS,PISTOL_ROUNDS,PISTOL_ROUNDS);
    
        RecipeJsonConverter.addShapedRecipe(new ItemStack(SMG_MAGAZINE_EMPTY.getItem(), 4, SMG_MAGAZINE_EMPTY.getItemDamage()), "i i","i i","imi", 'i', "nuggetIron", 'm', MECHANICAL_PARTS_IRON);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(SMG_MAGAZINE.getItem(), 1, SMG_MAGAZINE.getItemDamage()), SMG_MAGAZINE_EMPTY, PISTOL_ROUNDS, PISTOL_ROUNDS);
       
        RecipeJsonConverter.addShapedRecipe(new ItemStack(ASSAULTRIFLE_MAGAZINE_EMPTY.getItem(), 4, ASSAULTRIFLE_MAGAZINE_EMPTY.getItemDamage()),"s s","s s","sms", 's', "nuggetSteel",'m', MECHANICAL_PARTS_IRON);
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(ASSAULTRIFLE_MAGAZINE.getItem(), 1, ASSAULTRIFLE_MAGAZINE.getItemDamage()), ASSAULTRIFLE_MAGAZINE_EMPTY, RIFLE_ROUNDS, RIFLE_ROUNDS, RIFLE_ROUNDS);
        
       
        RecipeJsonConverter.addShapedRecipe(new ItemStack(BIO_TANK_EMPTY.getItem(), 1, BIO_TANK_EMPTY.getItemDamage()),"sss","sgs","sgs", 's', "nuggetSteel", 'g', "blockGlass");
 
        RecipeJsonConverter.addShapedRecipe(new ItemStack(BIOMASS.getItem(), 4, BIOMASS.getItemDamage())," g ","gbg"," g ", 'g', "dyeGreen", 'b', "slimeball");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(ROCKET.getItem(), 4, ROCKET.getItemDamage()), " s ","sts","sgs", 's', "nuggetIron", 't', Blocks.TNT, 'g', Items.GUNPOWDER);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(LMG_MAGAZINE_EMPTY.getItem(), 4, LMG_MAGAZINE_EMPTY.getItemDamage()), " ii", "imi","iii", 'i', "ingotSteel", 'm', MECHANICAL_PARTS_IRON);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(LMG_MAGAZINE.getItem(), 1, LMG_MAGAZINE.getItemDamage()), "bbb", "bmb","bbb", 'b', RIFLE_ROUNDS, 'm', LMG_MAGAZINE_EMPTY);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(LMG_MAGAZINE.getItem(), 1, LMG_MAGAZINE.getItemDamage()), LMG_MAGAZINE_EMPTY, RIFLE_ROUNDS_STACK, RIFLE_ROUNDS_STACK);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(AS50_MAGAZINE_EMPTY.getItem(), 4, AS50_MAGAZINE_EMPTY.getItemDamage()),"s s","s s","sms", 's', "ingotSteel", 'm', MECHANICAL_PARTS_IRON);
     
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(AS50_MAGAZINE.getItem(), 1, AS50_MAGAZINE.getItemDamage()),AS50_MAGAZINE_EMPTY, SNIPER_ROUNDS, SNIPER_ROUNDS);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(RECEIVER_IRON.getItem(), 1, RECEIVER_IRON.getItemDamage()), "iii"," mn","  n", 'i', "ingotIron", 'n', "nuggetIron",'m', MECHANICAL_PARTS_IRON);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(RECEIVER_STEEL.getItem(), 1, RECEIVER_STEEL.getItemDamage()), "iii"," mn","  n", 'i', "ingotSteel", 'n', "nuggetSteel",'m', MECHANICAL_PARTS_OBSIDIAN_STEEL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(RECEIVER_OBSIDIAN_STEEL.getItem(), 1, RECEIVER_OBSIDIAN_STEEL.getItemDamage()), "ppp"," mi","  i", 'i', "ingotObsidianSteel", 'p', "plateObsidianSteel",'m', MECHANICAL_PARTS_CARBON);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(RECEIVER_CARBON.getItem(), 1, RECEIVER_CARBON.getItemDamage()), "ppp"," mi","  i", 'i', "ingotObsidianSteel", 'p', "plateCarbon",'m', MECHANICAL_PARTS_CARBON);
        

        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(ADVANCED_MAGAZINE_EMPTY.getItem(), 4, ADVANCED_MAGAZINE_EMPTY.getItemDamage()),"i i","i i","imi",'i', "ingotObsidianSteel", 'm',MECHANICAL_PARTS_OBSIDIAN_STEEL);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(ADVANCED_MAGAZINE.getItem(), 1, ADVANCED_MAGAZINE.getItemDamage()), ADVANCED_MAGAZINE_EMPTY, ADVANCED_ROUNDS, ADVANCED_ROUNDS, ADVANCED_ROUNDS);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(WIRE_COPPER.getItem(), 1, WIRE_COPPER.getItemDamage())," cc"," c ","cc ",'c', "nuggetCopper");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(ELECTRIC_ENGINE.getItem(), 1, ELECTRIC_ENGINE.getItemDamage()), "wrw","imi","wrw", 'w', "wireCopper", 'i', "ingotIron", 'r', "dustRedstone", 'm', MECHANICAL_PARTS_IRON);
	
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.CIRCUIT_BOARD_BASIC.getItem(), 4, TGItems.CIRCUIT_BOARD_BASIC.getItemDamage()),"cgc","rir","cgc", 'i', "plateIron", 'c', "wireCopper", 'g', "dyeGreen", 'r', "dustRedstone");
	
       
        RecipeJsonConverter.addShapedRecipe(new ItemStack(BARREL_CARBON.getItem(), 1, BARREL_CARBON.getItemDamage()), "iii","   ","iii", 'i', "plateCarbon");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(STOCK_CARBON.getItem(), 1, STOCK_CARBON.getItemDamage()), "iii"," pp", 'i', "plateCarbon", 'p', "sheetPlastic");
        
      
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(CIRCUIT_BOARD_ELITE.getItem(), 1, CIRCUIT_BOARD_ELITE.getItemDamage()), "circuitBasic", "wireGold", "gemLapis");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(FUEL_TANK_EMPTY.getItem(), 4, FUEL_TANK_EMPTY.getItemDamage()), "p","g","p", 'g', "blockGlass", 'p', "sheetPlastic");
        
       
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(RIFLE_ROUNDS_STACK.getItem(), 1, RIFLE_ROUNDS_STACK.getItemDamage()), RIFLE_ROUNDS,RIFLE_ROUNDS,RIFLE_ROUNDS,RIFLE_ROUNDS);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(RIFLE_ROUNDS.getItem(), 4, RIFLE_ROUNDS.getItemDamage()), RIFLE_ROUNDS_STACK);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(MINIGUN_DRUM_EMPTY.getItem(), 4, MINIGUN_DRUM_EMPTY.getItemDamage()), "sss","pmp","sss",'s', "ingotSteel", 'p', "sheetPlastic", 'm', MECHANICAL_PARTS_OBSIDIAN_STEEL);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(MINIGUN_DRUM.getItem(), 1, MINIGUN_DRUM.getItemDamage()),MINIGUN_DRUM_EMPTY, RIFLE_ROUNDS_STACK,RIFLE_ROUNDS_STACK,RIFLE_ROUNDS_STACK,RIFLE_ROUNDS_STACK);
	
        RecipeJsonConverter.addShapedRecipe(new ItemStack(COIL.getItem(), 1, COIL.getItemDamage()), " wi","wiw","iw ", 'i', "ingotIron", 'w', "wireCopper");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(MACHINE_UPGRADE_STACK.getItem(), 1, MACHINE_UPGRADE_STACK.getItemDamage()), "pgp","ncn","pnp", 'p', "plateIron", 'n', "ingotGold",'g', "dyeGreen", 'c', new ItemStack(Blocks.CHEST,1));
    	
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TURRET_ARMOR_IRON.getItem(), 1, TURRET_ARMOR_IRON.getItemDamage()),"ppp"," p "," p ", 'p', "plateIron");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TURRET_ARMOR_STEEL.getItem(), 1, TURRET_ARMOR_STEEL.getItemDamage()),"ppp"," p "," p ", 'p', "plateSteel");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TURRET_ARMOR_OBSIDIAN_STEEL.getItem(), 1, TURRET_ARMOR_OBSIDIAN_STEEL.getItemDamage()),"ppp"," p "," p ", 'p', "plateObsidianSteel");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TURRET_ARMOR_CARBON.getItem(), 1, TURRET_ARMOR_CARBON.getItemDamage()),"ppp"," p "," p ", 'p', "plateCarbon");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(GLIDER_BACKBACK.getItem(), 1, GLIDER_BACKBACK.getItemDamage()), "hhh","shs","hhh", 'h', TGItems.HEAVY_CLOTH, 's', "ingotIron");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(GLIDER_WING.getItem(), 1, GLIDER_WING.getItemDamage()), "sss","hhh","hhh", 'h', TGItems.HEAVY_CLOTH, 's', "ingotIron");
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(GLIDER,1), TGItems.GLIDER_BACKBACK, TGItems.GLIDER_WING, TGItems.GLIDER_WING);
  
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.GAS_MASK_FILTER.getItem(), 1, TGItems.GAS_MASK_FILTER.getItemDamage()), "iii","ici","iii", 'i', "nuggetIron", 'c', Items.COAL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.GAS_MASK,1), "grg","rrr","rfr", 'r', "itemRubber", 'f', TGItems.GAS_MASK_FILTER, 'g', "paneGlass");
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.GAS_MASK,1,0), new ItemStack(TGItems.GAS_MASK,1,OreDictionary.WILDCARD_VALUE), TGItems.GAS_MASK_FILTER);
    
        RecipeJsonConverter.addShapedRecipe(new ItemStack(NIGHTVISION_GOGGLES,1), "rhr","cec","gsg", 'r', "itemRubber", 'h', TGItems.HEAVY_CLOTH, 'c',"circuitBasic",'e', TGItems.REDSTONE_BATTERY, 'g', "blockGlass", 's', "ingotIron");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(NIGHTVISION_GOGGLES,1,NIGHTVISION_GOGGLES.getMaxDamage()), "rhr","cec","gsg", 'r', "itemRubber", 'h', TGItems.HEAVY_CLOTH, 'c',"circuitBasic",'e', TGItems.REDSTONE_BATTERY_EMPTY, 'g', "blockGlass", 's', "ingotIron");
    
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.JETPACK,1,0), "f f","pgp","rmr",'f', TGItems.FUEL_TANK, 'p', "plateObsidianSteel", 'g', TGItems.GLIDER, 'r', TGItems.ROCKET, 'm', MECHANICAL_PARTS_OBSIDIAN_STEEL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.JETPACK,1,0), "f f","pgp","rmr",'f', TGItems.FUEL_TANK, 'p', "plateObsidianSteel", 'g', new ItemStack(TGItems.JUMPPACK,1,OreDictionary.WILDCARD_VALUE), 'r', TGItems.ROCKET, 'm', MECHANICAL_PARTS_OBSIDIAN_STEEL);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.ANTI_GRAV_PACK,1,0), "tet","cac","mbm",'t', "plateTitanium", 'e', TGItems.NUCLEAR_POWERCELL, 'b', TGItems.GLIDER_BACKBACK, 'c', "circuitElite", 'm', MECHANICAL_PARTS_CARBON, 'a', TGItems.ANTI_GRAV_CORE);
        
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.SCUBA_TANKS,1,0), "p p","aia","p p", 'p', "sheetPlastic", 'a', TGItems.COMPRESSED_AIR_TANK, 'i', "plateIron");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.JUMPPACK,1,0), "n n","pbp","c c", 'p', "plateIron", 'c', TGItems.COMPRESSED_AIR_TANK, 'n', "nuggetIron", 'b', new ItemStack(TGItems.GLIDER_BACKBACK.getItem(), 1, TGItems.GLIDER_BACKBACK.getItemDamage()));
           
        RecipeJsonConverter.addShapedRecipe(new ItemStack(OXYGEN_MASK.getItem(), 1, OXYGEN_MASK.getItemDamage()), " p ","rpr", 'p', "sheetPlastic", 'r', "itemRubber");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.TACTICAL_MASK,1,0),"ngo","cpc","pep", 'p', "sheetPlastic", 'n', new ItemStack(TGItems.NIGHTVISION_GOGGLES,1,OreDictionary.WILDCARD_VALUE), 'c', "circuitBasic", 'g', new ItemStack(TGItems.GAS_MASK,1,OreDictionary.WILDCARD_VALUE), 'o', TGItems.OXYGEN_MASK, 'e', TGItems.ENERGY_CELL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.TACTICAL_MASK,1,0),"ogn","cpc","pep", 'p', "sheetPlastic", 'n', new ItemStack(TGItems.NIGHTVISION_GOGGLES,1,OreDictionary.WILDCARD_VALUE), 'c', "circuitBasic", 'g', new ItemStack(TGItems.GAS_MASK,1,OreDictionary.WILDCARD_VALUE), 'o', TGItems.OXYGEN_MASK, 'e', TGItems.ENERGY_CELL);
        
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.COMBAT_KNIFE,1), " s","p ", 's', "plateSteel", 'p', "sheetPlastic");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.RC_HEAT_RAY.getItem(), 1, TGItems.RC_HEAT_RAY.getItemDamage()), "cwc","plp","plp", 'c', "circuitElite", 'w', "wireGold", 'p', "plateSteel", 'l', Blocks.REDSTONE_LAMP);
	
        //ingots/nugget
        RecipeJsonConverter.addShapelessRecipe(TGItems.INGOT_COPPER, "nuggetCopper","nuggetCopper","nuggetCopper","nuggetCopper","nuggetCopper","nuggetCopper","nuggetCopper","nuggetCopper","nuggetCopper");
        RecipeJsonConverter.addShapelessRecipe(TGItems.INGOT_LEAD, "nuggetLead","nuggetLead","nuggetLead","nuggetLead","nuggetLead","nuggetLead","nuggetLead","nuggetLead","nuggetLead");
        RecipeJsonConverter.addShapelessRecipe(TGItems.INGOT_STEEL, "nuggetSteel","nuggetSteel","nuggetSteel","nuggetSteel","nuggetSteel","nuggetSteel","nuggetSteel","nuggetSteel","nuggetSteel");
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.NUGGET_COPPER.getItem(), 9, TGItems.NUGGET_COPPER.getItemDamage()), "ingotCopper");
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.NUGGET_LEAD.getItem(), 9, TGItems.NUGGET_LEAD.getItemDamage()), "ingotLead");
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.NUGGET_STEEL.getItem(), 9, TGItems.NUGGET_STEEL.getItemDamage()), "ingotSteel");
        
        ItemStack rc = new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,1, EnumMultiBlockMachineType.REACTIONCHAMBER_HOUSING.getIndex());
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,9,EnumMultiBlockMachineType.REACTIONCHAMBER_HOUSING.getIndex()), "sms","pcp","ses", 's', "plateSteel", 'm', MECHANICAL_PARTS_CARBON, 'p', TGItems.CYBERNETIC_PARTS, 'e', "circuitElite",'c', new ItemStack(TGBlocks.BASIC_MACHINE,1, EnumMachineType.CHEM_LAB.getIndex()));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,1,EnumMultiBlockMachineType.REACTIONCHAMBER_CONTROLLER.getIndex()), " g ","crc"," g ", 'g', hardenedGlassOrGlass, 'c', "circuitElite",  'r', rc);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,6,EnumMultiBlockMachineType.REACTIONCHAMBER_GLASS.getIndex()), "rgr","rgr","rgr", 'r', rc, 'g', hardenedGlassOrGlass);
        
        ItemStack fab = new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,1, EnumMultiBlockMachineType.FABRICATOR_HOUSING.getIndex());
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,4,EnumMultiBlockMachineType.FABRICATOR_HOUSING.getIndex()), "sms","pep","scs", 's', "plateSteel", 'm', MECHANICAL_PARTS_CARBON, 'p', TGItems.CYBERNETIC_WIRING, 'e', TGItems.ELECTRIC_ENGINE, 'c', "circuitElite");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,1,EnumMultiBlockMachineType.FABRICATOR_CONTROLLER.getIndex()), " g ","cfc"," g ", 'g', hardenedGlassOrGlass, 'c', "circuitElite",  'f', fab);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.MULTIBLOCK_MACHINE,4,EnumMultiBlockMachineType.FABRICATOR_GLASS.getIndex()), "fgf","g g","fgf", 'f', fab, 'g', hardenedGlassOrGlass);
     

        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.CROWBAR,1), " np", " p ", "p  ", 'p', "plateSteel", 'n', "nuggetSteel");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.BASIC_MACHINE,1,EnumMachineType.AMMO_PRESS.getIndex()), "ili","cec","iri", 'i', "ingotIron", 'c', "ingotCopper", 'l', "ingotLead",'e', TGItems.ELECTRIC_ENGINE, 'r', "dustRedstone");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.BASIC_MACHINE,1,EnumMachineType.METAL_PRESS.getIndex()), "ibi","iei","iri", 'i', "ingotIron", 'b', "blockIron",'e', TGItems.ELECTRIC_ENGINE, 'r', "dustRedstone");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.BASIC_MACHINE,1,EnumMachineType.METAL_PRESS.getIndex()), "ibi","iei","iri", 'i', "ingotIron", 'b', "plateIron",'e', TGItems.ELECTRIC_ENGINE, 'r', "dustRedstone");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.BASIC_MACHINE,1,EnumMachineType.TURRET.getIndex()), "pip","cec","prp", 'p', "plateIron", 'c', new ItemStack(Blocks.CONCRETE, 1, OreDictionary.WILDCARD_VALUE), 'e', TGItems.ELECTRIC_ENGINE, 'r', "dustRedstone", 'i', "circuitBasic");
        
       
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.BASIC_MACHINE,1,EnumMachineType.CHEM_LAB.getIndex()), "igi","geg","iri", 'i', "ingotIron", 'g', new ItemStack(Items.GLASS_BOTTLE), 'e', TGItems.ELECTRIC_ENGINE, 'r', "dustRedstone");
       
        int repair_bench_meta = TGBlocks.SIMPLE_MACHINE.getMetaFromState(TGBlocks.SIMPLE_MACHINE.getDefaultState().withProperty(TGBlocks.SIMPLE_MACHINE.MACHINE_TYPE, EnumSimpleMachineType.REPAIR_BENCH));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.SIMPLE_MACHINE,1,repair_bench_meta), "pmp","ici","iii", 'm', MECHANICAL_PARTS_IRON, 'c', "workbench", 'i', "nuggetIron", 'p', "plateIron");
        
        int charging_station_meta = TGBlocks.SIMPLE_MACHINE.getMetaFromState(TGBlocks.SIMPLE_MACHINE.getDefaultState().withProperty(TGBlocks.SIMPLE_MACHINE.MACHINE_TYPE, EnumSimpleMachineType.CHARGING_STATION));    
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.SIMPLE_MACHINE,1,charging_station_meta), "sgs","cbc","sgs", 's', "plateSteel", 'g', "wireGold", 'b', "circuitBasic", 'c', TGItems.COIL);
    	
        int camo_bench_meta = TGBlocks.SIMPLE_MACHINE.getMetaFromState(TGBlocks.SIMPLE_MACHINE.getDefaultState().withProperty(TGBlocks.SIMPLE_MACHINE.MACHINE_TYPE, EnumSimpleMachineType.CAMO_BENCH));      
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.SIMPLE_MACHINE,1,camo_bench_meta), "ddd","ici","iii", 'd', "dye", 'c', "workbench", 'i', "nuggetIron");
        
        int blast_furnace_meta = TGBlocks.SIMPLE_MACHINE.getMetaFromState(TGBlocks.SIMPLE_MACHINE.getDefaultState().withProperty(TGBlocks.SIMPLE_MACHINE.MACHINE_TYPE, EnumSimpleMachineType.BLAST_FURNACE));      
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.SIMPLE_MACHINE,1,blast_furnace_meta), "iri","ifi","sbs", 'f', Blocks.FURNACE, 'i', "plateIron", 'b', "blockIron", 's', Blocks.STONEBRICK, 'r', "dustRedstone");
        
        int grinder_meta = TGBlocks.SIMPLE_MACHINE2.getMetaFromState(TGBlocks.SIMPLE_MACHINE2.getDefaultState().withProperty(TGBlocks.SIMPLE_MACHINE2.MACHINE_TYPE, EnumSimpleMachineType2.GRINDER));      
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.SIMPLE_MACHINE2,1,grinder_meta), "imi","mem","iri", 'i', "plateIron", 'm', TGItems.MECHANICAL_PARTS_IRON, 'e', TGItems.ELECTRIC_ENGINE, 'r', "dustRedstone");
        
        int armor_bench_meta = TGBlocks.SIMPLE_MACHINE2.getMetaFromState(TGBlocks.SIMPLE_MACHINE2.getDefaultState().withProperty(TGBlocks.SIMPLE_MACHINE2.MACHINE_TYPE, EnumSimpleMachineType2.ARMOR_BENCH));      
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.SIMPLE_MACHINE2,1,armor_bench_meta), " c ","oeo","sos", 'e', "gemEmerald", 'o', "plateObsidianSteel", 's', "obsidian", 'c', "workbench");
        
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.QUARTZ_ROD.getItem(), 1, TGItems.QUARTZ_ROD.getItemDamage()), "q  ", " q ", "  q", 'q', "gemQuartz");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.QUARTZ_ROD.getItem(), 1, TGItems.QUARTZ_ROD.getItemDamage()), "  q", " q ", "q  ", 'q', "gemQuartz");
        
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.LAMP_0,16,0),"iii","grg","ggg",'i',"nuggetIron",'r', "dustRedstone",'g', "paneGlass");
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.LAMP_0,1,0), new ItemStack(TGBlocks.LAMP_0,1,6));
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.LAMP_0,1,6), new ItemStack(TGBlocks.LAMP_0,1,0));
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.LAMP_0,16,12),"iii","grg","iii",'i',"nuggetIron",'r', "dustRedstone",'g', "paneGlass");
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.LAMP_0,1,13), new ItemStack(TGBlocks.LAMP_0,1,12));
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.LAMP_0,1,12), new ItemStack(TGBlocks.LAMP_0,1,13));
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.SANDBAGS,8), "itemRubber", "sand", "sand", "sand", "sand", "sand", "sand", "sand", "sand");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.CAMONET,8), "sds", "sds", 's', "stickWood", 'd', "dirt");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.CAMONET_TOP,16), "wdw","dsd", "wdw", 's', "string", 'd', "dirt", 'w', "stickWood");
 
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.BUNKER_DOOR,2), "pp","pp","pp", 'p', "plateIron");
    
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.TACTICAL_NUKE_WARHEAD.getItem(), 2, TGItems.TACTICAL_NUKE_WARHEAD.getItemDamage()), "pcp","tut","pcp", 'p', "plateLead", 't', TGItems.TGX, 'c', "circuitBasic", 'u', "ingotUraniumEnriched");
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.ROCKET_NUKE.getItem(), 1, TGItems.ROCKET_NUKE.getItemDamage()), new ItemStack(TGItems.ROCKET.getItem(), 1, TGItems.ROCKET.getItemDamage()), new ItemStack(TGItems.TACTICAL_NUKE_WARHEAD.getItem(), 1, TGItems.TACTICAL_NUKE_WARHEAD.getItemDamage()));
   
        //incendiary ammo
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(RIFLE_ROUNDS_STACK_INCENDIARY.getItem(), 1, RIFLE_ROUNDS_STACK_INCENDIARY.getItemDamage()), RIFLE_ROUNDS_INCENDIARY,RIFLE_ROUNDS_INCENDIARY,RIFLE_ROUNDS_INCENDIARY,RIFLE_ROUNDS_INCENDIARY);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(RIFLE_ROUNDS_INCENDIARY.getItem(), 4, RIFLE_ROUNDS_INCENDIARY.getItemDamage()), RIFLE_ROUNDS_STACK_INCENDIARY);
       
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(MINIGUN_DRUM_INCENDIARY.getItem(), 1, MINIGUN_DRUM_INCENDIARY.getItemDamage()),MINIGUN_DRUM_EMPTY, RIFLE_ROUNDS_STACK_INCENDIARY,RIFLE_ROUNDS_STACK_INCENDIARY,RIFLE_ROUNDS_STACK_INCENDIARY,RIFLE_ROUNDS_STACK_INCENDIARY);
	
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(LMG_MAGAZINE_INCENDIARY.getItem(), 1, LMG_MAGAZINE_INCENDIARY.getItemDamage()), LMG_MAGAZINE_EMPTY, RIFLE_ROUNDS_STACK_INCENDIARY, RIFLE_ROUNDS_STACK_INCENDIARY);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(LMG_MAGAZINE_INCENDIARY.getItem(), 1, LMG_MAGAZINE_INCENDIARY.getItemDamage()), LMG_MAGAZINE_EMPTY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY);
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(AS50_MAGAZINE_INCENDIARY.getItem(), 1, AS50_MAGAZINE_INCENDIARY.getItemDamage()),AS50_MAGAZINE_EMPTY, SNIPER_ROUNDS_INCENDIARY, SNIPER_ROUNDS_INCENDIARY);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(PISTOL_MAGAZINE_INCENDIARY.getItem(), 1, PISTOL_MAGAZINE_INCENDIARY.getItemDamage()), PISTOL_MAGAZINE_EMPTY, PISTOL_ROUNDS_INCENDIARY,PISTOL_ROUNDS_INCENDIARY,PISTOL_ROUNDS_INCENDIARY);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(SMG_MAGAZINE_INCENDIARY.getItem(), 1, SMG_MAGAZINE_INCENDIARY.getItemDamage()), SMG_MAGAZINE_EMPTY, PISTOL_ROUNDS_INCENDIARY, PISTOL_ROUNDS_INCENDIARY);
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(ASSAULTRIFLE_MAGAZINE_INCENDIARY.getItem(), 1, ASSAULTRIFLE_MAGAZINE_INCENDIARY.getItemDamage()), ASSAULTRIFLE_MAGAZINE_EMPTY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY, RIFLE_ROUNDS_INCENDIARY);
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(AS50_MAGAZINE_EXPLOSIVE.getItem(), 1, AS50_MAGAZINE_EXPLOSIVE.getItemDamage()),AS50_MAGAZINE_EMPTY, SNIPER_ROUNDS_EXPLOSIVE, SNIPER_ROUNDS_EXPLOSIVE);
        
        //tool upgrades
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.POWERHAMMERHEAD_OBSIDIAN.getItem(), 1, TGItems.POWERHAMMERHEAD_OBSIDIAN.getItemDamage()), "p  ","pii","p  ", 'p', "plateObsidianSteel", 'i', "ingotSteel");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.POWERHAMMERHEAD_CARBON.getItem(), 1, TGItems.POWERHAMMERHEAD_CARBON.getItemDamage()), "p  ","pii","p  ", 'p', "plateCarbon", 'i', "ingotObsidianSteel");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.MININGDRILLHEAD_OBSIDIAN.getItem(), 1, TGItems.MININGDRILLHEAD_OBSIDIAN.getItemDamage()), " dd","ddd"," dd", 'd', "plateObsidianSteel");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.MININGDRILLHEAD_CARBON.getItem(), 1, TGItems.MININGDRILLHEAD_CARBON.getItemDamage()), " dd","ddd"," dd", 'd', "plateCarbon");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.CHAINSAWBLADES_OBSIDIAN.getItem(), 1, TGItems.CHAINSAWBLADES_OBSIDIAN.getItemDamage()), " pm","m  "," pm", 'p', "plateSteel", 'm', TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.CHAINSAWBLADES_CARBON.getItem(), 1, TGItems.CHAINSAWBLADES_CARBON.getItemDamage()), " pm","m  "," pm", 'p', "plateObsidianSteel", 'm', TGItems.MECHANICAL_PARTS_CARBON);
      
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.REDSTONE_BATTERY.getItem(), 2, TGItems.REDSTONE_BATTERY.getItemDamage()), "nwn","nrn","nrn", 'n', "nuggetCopper", 'r',"dustRedstone", 'w', "wireCopper");
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.REDSTONE_BATTERY.getItem(), 1, TGItems.REDSTONE_BATTERY.getItemDamage()), TGItems.REDSTONE_BATTERY_EMPTY, "dustRedstone");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.NEONLIGHT_BLOCK,16), "nnn", "gsg", "nnn", 'n', "nuggetIron", 'g', "paneGlass", 's', "dustGlowstone");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.CONCRETE,16), "ccc","cbc","ccc", 'b', Blocks.IRON_BARS, 'c', new ItemStack(Blocks.CONCRETE, 1, OreDictionary.WILDCARD_VALUE));
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.NETHER_METAL,16), "nsn","sis","nsn", 'i', "ingotIron", 'n', "netherrack", 's', "stone");
        
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.METAL_STAIRS,6,7), "b  ", "bb ", "bbb", 'b', new ItemStack(TGBlocks.METAL_PANEL,1,TGMetalPanelType.PANEL_LARGE_BORDER.ordinal()));
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.METAL_PANEL,1,TGMetalPanelType.PANEL_LARGE_BORDER.ordinal()), new ItemStack(TGBlocks.METAL_STAIRS,1,7));
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.METAL_STAIRS,6,15), "b  ", "bb ", "bbb", 'b', new ItemStack(TGBlocks.METAL_PANEL,1,TGMetalPanelType.STEELFRAME_DARK.ordinal()));
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.METAL_PANEL,1,TGMetalPanelType.STEELFRAME_DARK.ordinal()), new ItemStack(TGBlocks.METAL_STAIRS,1,15));
       
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.CONCRETE_STAIRS,6,15), "b  ", "bb ", "bbb", 'b', new ItemStack(TGBlocks.CONCRETE,1,EnumConcreteType.CONCRETE_BROWN_LIGHT.ordinal()));
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.CONCRETE,1,EnumConcreteType.CONCRETE_BROWN_LIGHT.ordinal()), new ItemStack(TGBlocks.CONCRETE_STAIRS,1,15));
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.CONCRETE_STAIRS,6,7), "b  ", "bb ", "bbb", 'b', new ItemStack(TGBlocks.CONCRETE,1,EnumConcreteType.CONCRETE_GREY_DARK.ordinal()));
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGBlocks.CONCRETE,1,EnumConcreteType.CONCRETE_GREY_DARK.ordinal()), new ItemStack(TGBlocks.CONCRETE_STAIRS,1,7));
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.EXPLOSIVE_CHARGE,8,TGBlocks.EXPLOSIVE_CHARGE.getMetaFromState(TGBlocks.EXPLOSIVE_CHARGE.getDefaultState())), "rtr","tct","rtr", 'r', "itemRubber", 't', Blocks.TNT, 'c', "circuitBasic");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.EXPLOSIVE_CHARGE,4,TGBlocks.EXPLOSIVE_CHARGE.getMetaFromState(TGBlocks.EXPLOSIVE_CHARGE.getDefaultState().withProperty(TGBlocks.EXPLOSIVE_CHARGE.MACHINE_TYPE, EnumExplosiveChargeType.ADVANCED))), "rtr","tct","rtr", 'r', "sheetPlastic", 't', TGItems.TGX, 'c', "circuitBasic");
	
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.BARREL_GAUSS.getItem(), 1, TGItems.BARREL_GAUSS.getItemDamage()), "pww","bbc", "pww", 'p', "plateTitanium", 'w', "wireGold", 'b', TGItems.BARREL_CARBON, 'c', "circuitElite");
       
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.BARREL_TITANIUM.getItem(), 1, TGItems.BARREL_TITANIUM.getItemDamage()), "tct","lbl", "tct", 't', "plateTitanium", 'l', "plateLead", 'b', TGItems.BARREL_CARBON, 'c', "circuitElite");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.RECEIVER_TITANIUM.getItem(), 1, TGItems.RECEIVER_TITANIUM.getItemDamage()), "ttt"," rc","  t", 't', "plateTitanium", 'r', TGItems.RECEIVER_CARBON, 'c', "circuitElite");
       
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.PLASMA_GENERATOR.getItem(), 1, TGItems.PLASMA_GENERATOR.getItemDamage()), "lcl","eae","lcl", 'l', "plateLead", 'a', TGItems.ANTI_GRAV_CORE, 'c', TGItems.COIL, 'e', TGItems.ENRICHED_URANIUM);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.WORKING_GLOVES.getItem(), 1, TGItems.WORKING_GLOVES.getItemDamage()), " h","hh", 'h', TGItems.HEAVY_CLOTH);
        
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.PROTECTIVE_FIBER.getItem(), 3, TGItems.PROTECTIVE_FIBER.getItemDamage()), TGItems.HEAVY_CLOTH, TGItems.RUBBER_BAR, TGItems.PLATE_LEAD);
        
        String[] plateTypes = {"plateIron", "plateTin"};
        Arrays.stream(plateTypes).forEach(p -> {
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.DOOR3x3,2,0), "ppp", "sps","ppp", 's', Blocks.PISTON, 'p', p);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.DOOR3x3,2,1), "sps", "ppp","ppp", 's', Blocks.PISTON, 'p', p);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.DOOR3x3,2,2), "ppp", "ppp","sps", 's', Blocks.PISTON, 'p', p);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.LADDER_0,16,TGBlocks.LADDER_0.getMetaFromState(TGBlocks.LADDER_0.getDefaultState())),"iii"," i ","iii", 'i', p);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.WINDOW_FRAME_0,8,TGBlocks.WINDOW_FRAME_0.getMetaFromState(TGBlocks.WINDOW_FRAME_0.getDefaultState())),"iii","i i","iii", 'i', p);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(COMPRESSED_AIR_TANK_EMPTY.getItem(), 7, COMPRESSED_AIR_TANK_EMPTY.getItemDamage()),"plp","p p","ppp", 'p', p, 'l', Blocks.LEVER);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.METAL_PANEL,32), "sss","psp","sss", 's', "stone", 'p', p);
        });
        
        int metaHangarDoorUP = EnumDoorType.HANGAR_UP.ordinal();
        int metaHangarDoorDOWN = EnumDoorType.HANGAR_DOWN.ordinal();
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.DOOR3x3,1,metaHangarDoorDOWN), new ItemStack(TGItems.DOOR3x3,1,metaHangarDoorUP));
        RecipeJsonConverter.addShapelessRecipe(new ItemStack(TGItems.DOOR3x3,1,metaHangarDoorUP), new ItemStack(TGItems.DOOR3x3,1,metaHangarDoorDOWN));
        
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.ORE_DRILL_BLOCK,8,TGBlocks.ORE_DRILL_BLOCK.getMetaFromState(TGBlocks.ORE_DRILL_BLOCK.getDefaultState().withProperty(TGBlocks.ORE_DRILL_BLOCK.MACHINE_TYPE, EnumOreDrillType.FRAME))), "pbp","bpb","pbp", 'p', "plateIron", 'b', new ItemStack(Blocks.IRON_BARS,1));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.ORE_DRILL_BLOCK,8,TGBlocks.ORE_DRILL_BLOCK.getMetaFromState(TGBlocks.ORE_DRILL_BLOCK.getDefaultState().withProperty(TGBlocks.ORE_DRILL_BLOCK.MACHINE_TYPE, EnumOreDrillType.SCAFFOLD))), "bbb","bpb","bbb", 'p', "plateIron", 'b', new ItemStack(Blocks.IRON_BARS,1));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.ORE_DRILL_BLOCK,4,TGBlocks.ORE_DRILL_BLOCK.getMetaFromState(TGBlocks.ORE_DRILL_BLOCK.getDefaultState().withProperty(TGBlocks.ORE_DRILL_BLOCK.MACHINE_TYPE, EnumOreDrillType.ROD))), "ipi","ipi","ipi", 'p', "plateIron", 'i', "ingotIron");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.ORE_DRILL_BLOCK,4,TGBlocks.ORE_DRILL_BLOCK.getMetaFromState(TGBlocks.ORE_DRILL_BLOCK.getDefaultState().withProperty(TGBlocks.ORE_DRILL_BLOCK.MACHINE_TYPE, EnumOreDrillType.ENGINE))), "bbb","beb","bbb", 'b', new ItemStack(Blocks.IRON_BARS,1),'e', TGItems.ELECTRIC_ENGINE);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGBlocks.ORE_DRILL_BLOCK,1,TGBlocks.ORE_DRILL_BLOCK.getMetaFromState(TGBlocks.ORE_DRILL_BLOCK.getDefaultState().withProperty(TGBlocks.ORE_DRILL_BLOCK.MACHINE_TYPE, EnumOreDrillType.CONTROLLER))), "pcp","cec","pcp", 'p', "plateIron", 'c', "circuitBasic",'e', TGItems.ELECTRIC_ENGINE);
	  
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_STEEL.getItem(), 1, TGItems.OREDRILLHEAD_STEEL.getItemDamage()),"sps","sps"," s ", 's', "ingotSteel", 'p', "plateSteel");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_OBSIDIANSTEEL.getItem(), 1, TGItems.OREDRILLHEAD_OBSIDIANSTEEL.getItemDamage()),"sps","sps"," s ", 's', "ingotObsidianSteel", 'p', "plateObsidianSteel");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_CARBON.getItem(), 1, TGItems.OREDRILLHEAD_CARBON.getItemDamage()),"sss","sss"," s ",  's', "plateCarbon");
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_MEDIUM_STEEL.getItem(), 1, TGItems.OREDRILLHEAD_MEDIUM_STEEL.getItemDamage()),"d d"," d ", 'd', TGItems.OREDRILLHEAD_STEEL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_MEDIUM_OBSIDIANSTEEL.getItem(), 1, TGItems.OREDRILLHEAD_MEDIUM_OBSIDIANSTEEL.getItemDamage()),"d d"," d ", 'd', TGItems.OREDRILLHEAD_OBSIDIANSTEEL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_MEDIUM_CARBON.getItem(), 1, TGItems.OREDRILLHEAD_MEDIUM_CARBON.getItemDamage()),"d d"," d ", 'd', TGItems.OREDRILLHEAD_CARBON);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_LARGE_STEEL.getItem(), 1, TGItems.OREDRILLHEAD_LARGE_STEEL.getItemDamage()),"d d"," d ", 'd', TGItems.OREDRILLHEAD_MEDIUM_STEEL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_LARGE_OBSIDIANSTEEL.getItem(), 1, TGItems.OREDRILLHEAD_LARGE_OBSIDIANSTEEL.getItemDamage()),"d d"," d ", 'd', TGItems.OREDRILLHEAD_MEDIUM_OBSIDIANSTEEL);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.OREDRILLHEAD_LARGE_CARBON.getItem(), 1, TGItems.OREDRILLHEAD_LARGE_CARBON.getItemDamage()),"d d"," d ", 'd', TGItems.OREDRILLHEAD_MEDIUM_CARBON);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.INFUSION_BAG.getItem(), 4, TGItems.INFUSION_BAG.getItemDamage()),"pp","pp", 'p', "sheetPlastic");
        	
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_PROTECTION_1.getItem(), 1, TGItems.UPGRADE_PROTECTION_1.getItemDamage()), "pip","igi","pip", 'p', "plateSteel", 'g', "ingotGold", 'i', "plateIron");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_BLAST_PROTECTION_1.getItem(), 1, TGItems.UPGRADE_BLAST_PROTECTION_1.getItemDamage()), "pip","igi","pip", 'p', "plateSteel", 'g', "ingotGold", 'i', "blockWool");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_PROJECTILE_PROTECTION_1.getItem(), 1, TGItems.UPGRADE_PROJECTILE_PROTECTION_1.getItemDamage()), "pip","igi","pip", 'p', "plateSteel", 'g', "ingotGold", 'i', TGItems.STEAMARMOR_PLATE);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_PROTECTION_2.getItem(), 1, TGItems.UPGRADE_PROTECTION_2.getItemDamage()), "pip","igi","pip", 'p', "plateObsidianSteel", 'g', "ingotGold", 'i', "plateIron");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_BLAST_PROTECTION_2.getItem(), 1, TGItems.UPGRADE_BLAST_PROTECTION_2.getItemDamage()), "pip","igi","pip", 'p', "plateObsidianSteel", 'g', "ingotGold", 'i', "blockWool");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_PROJECTILE_PROTECTION_2.getItem(), 1, TGItems.UPGRADE_PROJECTILE_PROTECTION_2.getItemDamage()), "pip","igi","pip", 'p', "plateObsidianSteel", 'g', "ingotGold", 'i', TGItems.STEAMARMOR_PLATE);
        
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_PROTECTION_3.getItem(), 1, TGItems.UPGRADE_PROTECTION_3.getItemDamage()), "pip","igi","pip", 'p', "plateCarbon", 'g', "gemDiamond", 'i', "plateIron");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_BLAST_PROTECTION_3.getItem(), 1, TGItems.UPGRADE_BLAST_PROTECTION_3.getItemDamage()), "pip","igi","pip", 'p', "plateCarbon", 'g', "gemDiamond", 'i', "blockWool");
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.UPGRADE_PROJECTILE_PROTECTION_3.getItem(), 1, TGItems.UPGRADE_PROJECTILE_PROTECTION_3.getItemDamage()), "pip","igi","pip", 'p', "plateCarbon", 'g', "gemDiamond", 'i', TGItems.STEAMARMOR_PLATE);

        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.REINFORCED_ELECTRIC_ENGINE.getItem(), 1, TGItems.REINFORCED_ELECTRIC_ENGINE.getItemDamage()), " is", " f ", "si ", 's', TGItems.ELECTRIC_ENGINE, 'f', TGItems.INGOT_HELLISH, 'i', TGItems.QUARTZ_ROD);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGItems.RC_UV_EMITTER.getItem(), 1, TGItems.RC_UV_EMITTER.getItemDamage()), "sss", "fif", "ttt", 's', "plateSteel", 'f', TGItems.CIRCUIT_BOARD_ELITE, 'i', TGItems.UV_ELEMENT, 't', hardenedGlassOrGlass);
        
        addGunRecipes();
        addArmorRecipes();
        addUpgradeRecipes();
        addAmmoChangeRecipes();
	}

	
	public static void addUpgradeRecipes() {
		RecipeJsonConverter.addShapelessMiningHeadUpgradeRecipe(TGuns.miningdrill, TGItems.MININGDRILLHEAD_CARBON, "miningHead",0);
		RecipeJsonConverter.addShapelessMiningHeadUpgradeRecipe(TGuns.powerhammer, TGItems.POWERHAMMERHEAD_OBSIDIAN, "miningHead",0);
		RecipeJsonConverter.addShapelessMiningHeadUpgradeRecipe(TGuns.powerhammer, TGItems.POWERHAMMERHEAD_CARBON, "miningHead",1);
		RecipeJsonConverter.addShapelessMiningHeadUpgradeRecipe(TGuns.chainsaw, TGItems.CHAINSAWBLADES_OBSIDIAN, "miningHead",0);
		RecipeJsonConverter.addShapelessMiningHeadUpgradeRecipe(TGuns.chainsaw, TGItems.CHAINSAWBLADES_CARBON, "miningHead",1);
	}
	
	public static void addAmmoChangeRecipes() {
		GenericGun.guns.forEach(g -> {
			AmmoType a = g.getAmmoType();
			if(a.hasMultipleVariants()) {
				a.getVariants().forEach(v -> RecipeJsonConverter.addShapelessAmmoSwapRecipe(g, a, v.getKey()));
			}
		});
	}
	
	public static void addGunRecipes() {
		    RecipeJsonConverter.addShapedRecipe(new ItemStack(handcannon,1, handcannon.getMaxDamage()), "bfs", 'b', TGItems.BARREL_STONE, 'f', Items.FLINT_AND_STEEL, 's', TGItems.STOCK_WOOD);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(pistol,1), "bsp"," fp","  m", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL, 'p', TGItems.PLASTIC_SHEET, 's', "plateSteel", 'm', TGItems.PISTOL_MAGAZINE);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(pistol,1,pistol.getMaxDamage()), "bsp"," fp","  m", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.MECHANICAL_PARTS_OBSIDIAN_STEEL, 'p', TGItems.PLASTIC_SHEET,'s',"plateSteel", 'm', TGItems.PISTOL_MAGAZINE_EMPTY);
	               
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(revolver,1, revolver.getMaxDamage()), "bmf"," s ", 'f', Items.FLINT_AND_STEEL, 'b', TGItems.BARREL_IRON, 's', TGItems.STOCK_WOOD, 'm', TGItems.MECHANICAL_PARTS_IRON);
	             
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(sawedoff,1, sawedoff.getMaxDamage()), "bfw","bf ", 'b', TGItems.BARREL_IRON, 'f', Items.FLINT_AND_STEEL, 'w', TGItems.STOCK_WOOD);
	       
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(thompson,1), "bfs"," m ", 'f', TGItems.RECEIVER_IRON, 'b', TGItems.BARREL_IRON, 's', TGItems.STOCK_WOOD, 'm', TGItems.SMG_MAGAZINE);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(thompson,1,thompson.getMaxDamage()), "bfs"," m ", 'f', TGItems.RECEIVER_IRON, 'b', TGItems.BARREL_IRON, 's', TGItems.STOCK_WOOD, 'm', TGItems.SMG_MAGAZINE_EMPTY);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(m4,1)," gr","bfs","nmi", 'g', "paneGlass", 'r', "dustRedstone", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_STEEL, 's', TGItems.STOCK_PLASTIC, 'n', "nuggetIron",'m', TGItems.ASSAULTRIFLE_MAGAZINE, 'i', "ingotIron");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(m4,1,m4.getMaxDamage())," gr","bfs","nmi", 'g', "paneGlass", 'r', "dustRedstone", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_STEEL, 's', TGItems.STOCK_PLASTIC, 'n', "nuggetIron",'m', TGItems.ASSAULTRIFLE_MAGAZINE_EMPTY, 'i', "ingotIron");
	                
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(boltaction,1, boltaction.getMaxDamage()), "gi ", "bfs", 'g', "blockGlass", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_IRON, 's', TGItems.STOCK_WOOD,'i', "plateIron");
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(combatshotgun,1, combatshotgun.getMaxDamage()), "bfs","i  ",'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_STEEL, 's', TGItems.STOCK_PLASTIC, 'i', "ingotSteel");
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(flamethrower,1), "prs","fm ",'r', TGItems.RECEIVER_IRON, 'p', TGItems.PUMP_MECHANISM, 's', TGItems.STOCK_PLASTIC, 'f', Items.FLINT_AND_STEEL, 'm', TGItems.FUEL_TANK);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(flamethrower,1,flamethrower.getMaxDamage()), "prs","fm ",'r', TGItems.RECEIVER_IRON, 'p', TGItems.PUMP_MECHANISM, 's', TGItems.STOCK_PLASTIC, 'f', Items.FLINT_AND_STEEL, 'm', TGItems.FUEL_TANK_EMPTY);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(biogun,1), "frs"," m ",'r', TGItems.RECEIVER_STEEL, 'f', TGItems.PUMP_MECHANISM, 's', TGItems.STOCK_PLASTIC, 'm', TGItems.BIO_TANK);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(biogun,1,biogun.getMaxDamage()), "frs"," m ",'r', TGItems.RECEIVER_STEEL, 'f', TGItems.PUMP_MECHANISM, 's', TGItems.STOCK_PLASTIC, 'm', TGItems.BIO_TANK_EMPTY);
	            
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(lasergun,1)," gd","brs"," em", 'g', "paneGlass", 'd', "dustRedstone", 'b', TGItems.BARREL_LASER, 'm', TGItems.MECHANICAL_PARTS_CARBON, 's', TGItems.STOCK_PLASTIC, 'e', TGItems.ENERGY_CELL, 'r', TGItems.RECEIVER_OBSIDIAN_STEEL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(lasergun,1,lasergun.getMaxDamage())," gd","brs"," em", 'g', "paneGlass", 'd', "dustRedstone", 'b', TGItems.BARREL_LASER, 'm', TGItems.MECHANICAL_PARTS_CARBON, 's', TGItems.STOCK_PLASTIC, 'e', TGItems.ENERGY_CELL_EMPTY, 'r', TGItems.RECEIVER_OBSIDIAN_STEEL);      
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(blasterrifle,1),"pcg","bms"," e ", 'g', hardenedGlassOrGlass,'p', "plateCarbon", 'c', "circuitElite", 'b', TGItems.BARREL_LASER, 'm', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'e', TGItems.ENERGY_CELL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(blasterrifle,1,blasterrifle.getMaxDamage()),"pcg","bms"," e ", 'g', hardenedGlassOrGlass,'p', "plateCarbon", 'c', "circuitElite", 'b', TGItems.BARREL_LASER, 'm', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'e', TGItems.ENERGY_CELL_EMPTY);
	        
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(ak47,1),"bfs","wm ", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_IRON, 's', TGItems.STOCK_WOOD, 'w', "logWood",'m', TGItems.ASSAULTRIFLE_MAGAZINE);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(ak47,1,ak47.getMaxDamage()),"bfs","wm ", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_IRON, 's', TGItems.STOCK_WOOD, 'w', "logWood",'m', TGItems.ASSAULTRIFLE_MAGAZINE_EMPTY);
	          
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(as50,1),"dpd","bbf", " mt", 'f', TGItems.RECEIVER_OBSIDIAN_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 't', TGItems.STOCK_PLASTIC,'m', TGItems.AS50_MAGAZINE, 'd', "gemDiamond", 'p', "plateObsidianSteel");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(as50,1,as50.getMaxDamage()),"dpd","bbf", " mt", 'f', TGItems.RECEIVER_OBSIDIAN_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 't', TGItems.STOCK_PLASTIC,'m', TGItems.AS50_MAGAZINE_EMPTY, 'd', "gemDiamond", 'p', "plateObsidianSteel");
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(rocketlauncher,1),"rbb"," f ", 'r', TGItems.ROCKET, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_STEEL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(rocketlauncher,1,rocketlauncher.getMaxDamage())," bb"," f ", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_STEEL);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(guidedmissilelauncher,1)," gc","rbb"," f ", 'r', TGItems.ROCKET, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_OBSIDIAN_STEEL, 'g', hardenedGlassOrGlass, 'c', "circuitElite");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(guidedmissilelauncher,1,guidedmissilelauncher.getMaxDamage())," gc"," bb"," f ", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_OBSIDIAN_STEEL, 'g', hardenedGlassOrGlass, 'c', "circuitElite");
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(lmg,1), " gr","bfs"," m ", 'g', "paneGlass", 'r', "dustRedstone", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_OBSIDIAN_STEEL, 's', TGItems.STOCK_PLASTIC,'m', TGItems.LMG_MAGAZINE);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(lmg,1,lmg.getMaxDamage()), " gr","bfs"," m ", 'g', "paneGlass", 'r', "dustRedstone", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_OBSIDIAN_STEEL, 's', TGItems.STOCK_PLASTIC,'m', TGItems.LMG_MAGAZINE_EMPTY);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(stielgranate,16), " it"," wi","i  ",'i', "ingotIron",'w', "plankWood", 't', Blocks.TNT);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(minigun,1),"bbe","bbr","bbm", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'e', TGItems.ELECTRIC_ENGINE, 'r', TGItems.RECEIVER_OBSIDIAN_STEEL, 'm', TGItems.MINIGUN_DRUM);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(minigun,1,minigun.getMaxDamage()),"bbe","bbr","bbm", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'e', TGItems.ELECTRIC_ENGINE, 'r', TGItems.RECEIVER_OBSIDIAN_STEEL, 'm', TGItems.MINIGUN_DRUM_EMPTY);
	                
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(pdw,1),"brs"," m ", 'b', TGItems.BARREL_CARBON, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'm', TGItems.ADVANCED_MAGAZINE);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(pdw,1,pdw.getMaxDamage()),"brs"," m ", 'b', TGItems.BARREL_CARBON, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'm', TGItems.ADVANCED_MAGAZINE_EMPTY);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(pulserifle,1),"dpc","brs"," m ", 'b', TGItems.BARREL_CARBON, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'm', TGItems.ADVANCED_MAGAZINE, 'd', "gemDiamond", 'p', "plateTitanium", 'c', "circuitElite");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(pulserifle,1,pulserifle.getMaxDamage()),"dpc","brs"," m ", 'b', TGItems.BARREL_CARBON, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'm', TGItems.ADVANCED_MAGAZINE_EMPTY, 'd', "gemDiamond", 'p', "plateTitanium", 'c', "circuitElite");
	         
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(powerhammer,1), " m ", "spr", " mc", 'p', new ItemStack(Blocks.PISTON,1), 'r', TGItems.RECEIVER_IRON, 'm', TGItems.MECHANICAL_PARTS_IRON, 's', "plateIron", 'c', TGItems.COMPRESSED_AIR_TANK);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(powerhammer,1,powerhammer.getMaxDamage()), " m ", "spr", " mc", 'p', new ItemStack(Blocks.PISTON,1), 'r', TGItems.RECEIVER_IRON, 'm', TGItems.MECHANICAL_PARTS_IRON, 's', "plateIron", 'c', TGItems.COMPRESSED_AIR_TANK_EMPTY);

	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(teslagun,1), " gd", "crs", " e ", 'g', "paneGlass", 'd', "dustRedstone", 'c', TGItems.COIL, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'e', TGItems.ENERGY_CELL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(teslagun,1,teslagun.getMaxDamage()), " gd", "crs", " e ", 'g', "paneGlass", 'd', "dustRedstone", 'c', TGItems.COIL, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'e', TGItems.ENERGY_CELL_EMPTY);
	        
	        
	        RecipeJsonConverter.addShapedCopyNBTRecipe(new ItemStack(goldenrevolver,1), "ggg","grg","ggg", 'g', "ingotGold", 'r', new ItemStack(revolver,1));
	        
	        RecipeJsonConverter.addShapedAmmoSumRecipe(new ItemStack(grimreaper,1), "rr ", "rrc", 'r', new ItemStack(guidedmissilelauncher,1,0), 'c', TGItems.RECEIVER_CARBON);

	        RecipeJsonConverter.addShapedRecipe(new ItemStack(m4_infiltrator,1), "gpg","wwm","rri", 'm', new ItemStack(m4,1), 'g', hardenedGlassOrGlass, 'p', "plateSteel", 'w', "blockWool",'i',"ingotSteel", 'r', "dustRedstone");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(m4_infiltrator,1,m4_infiltrator.getMaxDamage()), "gpg","wwm","rri", 'm', new ItemStack(m4,1,m4.getMaxDamage()), 'g', hardenedGlassOrGlass, 'p', "plateSteel", 'w', "blockWool",'i',"ingotSteel", 'r', "dustRedstone");
	                	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(netherblaster,1), "cpc","bns","cpc", 'c', TGItems.CYBERNETIC_PARTS, 'p', "plateObsidianSteel", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'n', TGItems.NETHER_CHARGE, 's', TGItems.PUMP_MECHANISM);
	                
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(aug,1),"gpg","bfs","i m", 'g', "paneGlass", 'p', "plateSteel", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_STEEL, 's', TGItems.STOCK_PLASTIC, 'm', TGItems.ASSAULTRIFLE_MAGAZINE, 'i', "sheetPlastic");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(aug,1,aug.getMaxDamage()),"gpg","bfs","i m", 'g', "paneGlass", 'p', "plateSteel", 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'f', TGItems.RECEIVER_STEEL, 's', TGItems.STOCK_PLASTIC, 'm', TGItems.ASSAULTRIFLE_MAGAZINE_EMPTY, 'i', "sheetPlastic");
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(grenadelauncher,1,grenadelauncher.getMaxDamage()), "mrs"," p ", 'm', TGItems.BARREL_OBSIDIAN_STEEL, 'r', TGItems.RECEIVER_STEEL, 's', TGItems.STOCK_PLASTIC, 'p', "plateSteel");
	    
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(chainsaw,1), "ccp","mmr","ccf", 'c', TGItems.MECHANICAL_PARTS_IRON, 'm', "plateIron", 'r', TGItems.RECEIVER_IRON, 'p', TGItems.PLASTIC_SHEET, 'f', TGItems.FUEL_TANK);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(chainsaw,1,chainsaw.getMaxDamage()), "ccp","mmr","ccf", 'c', TGItems.MECHANICAL_PARTS_IRON, 'm', "plateIron", 'r', TGItems.RECEIVER_IRON, 'p', TGItems.PLASTIC_SHEET, 'f', TGItems.FUEL_TANK_EMPTY);
		
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(sonicshotgun,1), " p ", "ers", " c ", 'p', "plateTitanium", 'e', TGItems.SONIC_EMITTER, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'c', TGItems.ENERGY_CELL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(sonicshotgun,1,sonicshotgun.getMaxDamage()), " p ", "ers", " c ", 'p', "plateTitanium", 'e', TGItems.SONIC_EMITTER, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'c', TGItems.ENERGY_CELL_EMPTY);

	        RecipeJsonConverter.addShapedRecipe(new ItemStack(nucleardeathray,1),"ltl","ers"," m ", 'l', "plateLead", 't', "plateTitanium", 'e', TGItems.RAD_EMITTER, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'm', TGItems.NUCLEAR_POWERCELL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(nucleardeathray,1,nucleardeathray.getMaxDamage()),"ltl","ers"," m ", 'l', "plateLead", 't', "plateTitanium", 'e', TGItems.RAD_EMITTER, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'm', TGItems.NUCLEAR_POWERCELL_EMPTY);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(mac10,1)," pi","bri"," m ", 'p', "sheetPlastic", 'i', "nuggetIron", 'r', TGItems.RECEIVER_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'm', TGItems.SMG_MAGAZINE);	    
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(mac10,1,mac10.getMaxDamage())," pi","bri"," m ", 'p', "sheetPlastic", 'i', "nuggetIron", 'r', TGItems.RECEIVER_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 'm', TGItems.SMG_MAGAZINE_EMPTY);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(scar,1),"gpd","brs", " m ", 'r', TGItems.RECEIVER_OBSIDIAN_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 's', TGItems.STOCK_PLASTIC,'m', TGItems.ASSAULTRIFLE_MAGAZINE, 'd', "gemDiamond", 'p', "sheetPlastic", 'g', hardenedGlassOrGlass);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(scar,1,scar.getMaxDamage()),"gpd","brs", " m ", 'r', TGItems.RECEIVER_OBSIDIAN_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 's', TGItems.STOCK_PLASTIC,'m', TGItems.ASSAULTRIFLE_MAGAZINE_EMPTY, 'd', "gemDiamond", 'p', "sheetPlastic", 'g',hardenedGlassOrGlass);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(vector,1)," gd","brs", " m ", 'r', TGItems.RECEIVER_OBSIDIAN_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 's', TGItems.STOCK_PLASTIC,'m', TGItems.SMG_MAGAZINE, 'd', "dustRedstone", 'g', "paneGlass");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(vector,1,vector.getMaxDamage())," gd","brs", " m ", 'r', TGItems.RECEIVER_OBSIDIAN_STEEL, 'b', TGItems.BARREL_OBSIDIAN_STEEL, 's', TGItems.STOCK_PLASTIC,'m', TGItems.SMG_MAGAZINE_EMPTY, 'd', "dustRedstone", 'g', "paneGlass");
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(mibgun,1), "bsp"," fp","  m", 'b', TGItems.BARREL_LASER, 'f', TGItems.MECHANICAL_PARTS_CARBON, 'p', "plateTitanium", 's', TGItems.SONIC_EMITTER, 'm', TGItems.ENERGY_CELL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(mibgun,1,mibgun.getMaxDamage()), "bsp"," fp","  m", 'b', TGItems.BARREL_LASER, 'f', TGItems.MECHANICAL_PARTS_CARBON, 'p', "plateTitanium", 's', TGItems.SONIC_EMITTER, 'm', TGItems.ENERGY_CELL_EMPTY);
	       
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(fraggrenade,16), " if","iti"," i ",'i', "ingotSteel",'f', Items.FLINT_AND_STEEL, 't', Blocks.TNT);
	 
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(miningdrill,1), " m ","drs"," f ", 'm', TGItems.MECHANICAL_PARTS_CARBON, 'f', TGItems.FUEL_TANK, 'd', TGItems.MININGDRILLHEAD_OBSIDIAN, 's', TGItems.STOCK_PLASTIC, 'r', TGItems.RECEIVER_OBSIDIAN_STEEL);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(miningdrill,1,miningdrill.getMaxDamage()), " m ","drs"," f ", 'm', TGItems.MECHANICAL_PARTS_CARBON, 'f', TGItems.FUEL_TANK_EMPTY, 'd', TGItems.MININGDRILLHEAD_OBSIDIAN, 's', TGItems.STOCK_PLASTIC, 'r', TGItems.RECEIVER_OBSIDIAN_STEEL);
		    
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(gaussrifle,1),"dpc","brs"," ae", 'b', TGItems.BARREL_GAUSS, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'e', TGItems.ENERGY_CELL, 'a', TGItems.GAUSSRIFLE_SLUGS, 'd', "gemDiamond", 'p', "plateTitanium", 'c', "circuitElite");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(gaussrifle,1, gaussrifle.getMaxDamage()),"dpc","brs","  e", 'b', TGItems.BARREL_GAUSS, 'r', TGItems.RECEIVER_CARBON, 's', TGItems.STOCK_CARBON, 'e', TGItems.ENERGY_CELL_EMPTY, 'd', "gemDiamond", 'p', "plateTitanium", 'c', "circuitElite");
		    
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(tfg,1),"t  ","bpr","tn ", 'b', TGItems.BARREL_TITANIUM, 'r', TGItems.RECEIVER_TITANIUM, 't', "plateTitanium", 'n', TGItems.NUCLEAR_POWERCELL, 'p', TGItems.PLASMA_GENERATOR);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(tfg,1,tfg.getMaxDamage()),"t  ","bpr","tn ", 'b', TGItems.BARREL_TITANIUM, 'r', TGItems.RECEIVER_TITANIUM, 't', "plateTitanium", 'n', TGItems.NUCLEAR_POWERCELL_EMPTY, 'p', TGItems.PLASMA_GENERATOR);
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(laserpistol,1),"obo","scr","ssp", 'b', TGItems.BARREL_LASER, 'r', TGItems.REDSTONE_BATTERY, 'o', "plateObsidianSteel", 's', "nuggetSteel", 'p', "sheetPlastic", 'c', "circuitElite");
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(laserpistol,1,laserpistol.getMaxDamage()),"obo","scr","ssp", 'b', TGItems.BARREL_LASER, 'r', TGItems.REDSTONE_BATTERY_EMPTY, 'o', "plateObsidianSteel", 's', "nuggetSteel", 'p', "sheetPlastic", 'c', "circuitElite");
	        
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(shishkebap,1),"p ","pm","sf", 'p', "plateObsidianSteel", 'm', TGItems.PUMP_MECHANISM, 's', "sheetPlastic", 'f', TGItems.FUEL_TANK);
	        RecipeJsonConverter.addShapedRecipe(new ItemStack(shishkebap,1,shishkebap.getMaxDamage()),"p ","pm","sf", 'p', "plateObsidianSteel", 'm', TGItems.PUMP_MECHANISM, 's', "sheetPlastic", 'f', TGItems.FUEL_TANK_EMPTY);
		       
	}
	
	public static void addArmorRecipes() {
		//Recipes:
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_combat_Helmet,1), "iii","c c", 'i', "ingotIron", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_combat_Chestplate,1), "c c","iii","ccc", 'i', "ingotIron", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_combat_Leggings,1), "iii","c c","c c", 'i', "ingotIron", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_combat_Boots,1), "c c","i i", 'i', "ingotIron", 'c', TGItems.HEAVY_CLOTH);
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_miner_Helmet,1), "iyi","c c",'i', "ingotIron", 'c', TGItems.HEAVY_CLOTH, 'y', "dyeYellow");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_miner_Chestplate,1), "c c","ccc","ici", 'i', "ingotIron", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_miner_Leggings,1), "ici","c c","c c", 'i', "ingotIron", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_miner_Boots,1), "c c","i i", 'i', "nuggetIron", 'c', TGItems.HEAVY_CLOTH);
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_scout_Helmet,1), "ccc","c c", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_scout_Chestplate,1), "c c","ccc","ccc", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_scout_Leggings,1), "ccc","c c","c c", 'c', TGItems.HEAVY_CLOTH);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t1_scout_Boots,1), "c c","c c", 'c', TGItems.HEAVY_CLOTH);
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_combat_Helmet,1), "iii","h h", 'h', TGItems.HEAVY_CLOTH,'i', "ingotObsidianSteel");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_combat_Chestplate,1), "h h","iii","iii", 'h', TGItems.HEAVY_CLOTH,'i', "ingotObsidianSteel");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_combat_Leggings,1), "ihi","i i","h h", 'h', TGItems.HEAVY_CLOTH,'i', "ingotObsidianSteel");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_combat_Boots,1), "h h","i i", 'h',TGItems.HEAVY_CLOTH,'i', "ingotObsidianSteel");

		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_commando_Helmet,1), "ihi","hgh", 'h', "itemRubber",'i', "ingotObsidianSteel",'g',"paneGlass");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_commando_Chestplate,1), "h h","hih","iii", 'h', "itemRubber",'i', "ingotObsidianSteel");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_commando_Leggings,1), "hih","i i","h h", 'h', "itemRubber",'i', "ingotObsidianSteel");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_commando_Boots,1), "i i","h h", 'h',"itemRubber",'i', "ingotObsidianSteel");
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_combat_Helmet,1), "ppp","f f", 'f', "fiberCarbon",'p', "plateCarbon");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_combat_Chestplate,1), "p p","ppp","fpf", 'f', "fiberCarbon",'p', "plateCarbon");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_combat_Leggings,1), "pfp","p p","f f", 'f', "fiberCarbon",'p', "plateCarbon");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_combat_Boots,1), "f f","p p", 'f', "fiberCarbon",'p', "plateCarbon");
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(steam_Helmet,1), "sss","s s", 's', TGItems.STEAMARMOR_PLATE);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(steam_Chestplate,1), "s s","sss","sss", 's', TGItems.STEAMARMOR_PLATE);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(steam_Leggings,1), "sss","s s","s s", 's', TGItems.STEAMARMOR_PLATE);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(steam_Boots,1), "s s","s s", 's', TGItems.STEAMARMOR_PLATE);
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_power_Helmet,1), "cpc","pgp", 'p', TGItems.POWER_ARMOR_PLATING, 'g', "paneGlass", 'c', "circuitElite");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_power_Chestplate,1), "p p","ece","ppp", 'p', TGItems.POWER_ARMOR_PLATING, 'c', "circuitElite", 'e', TGItems.ELECTRIC_ENGINE);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_power_Leggings,1), "pcp","e e","p p", 'p', TGItems.POWER_ARMOR_PLATING, 'c', "circuitElite", 'e', TGItems.ELECTRIC_ENGINE);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_power_Boots,1), "e e","p p", 'p', TGItems.POWER_ARMOR_PLATING, 'e',TGItems.ELECTRIC_ENGINE);
	
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_miner_Helmet,1), "pcp","lgl", 'p', "plateCarbon", 'g', "paneGlass", 'c', "circuitElite", 'l', "plateTitanium");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_miner_Chestplate,1), "p p","lcl","lpl", 'p',  "plateCarbon", 'c', "circuitElite", 'l', "plateTitanium");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_miner_Leggings,1), "ppp","c c","l l", 'p',  "plateCarbon", 'c', "circuitElite", 'l', "plateTitanium");
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t3_miner_Boots,1), "l l","p p", 'p',  "plateCarbon", 'l',"plateTitanium");
	
		RecipeJsonConverter.addShapelessRecipe(new ItemStack(t3_exo_Helmet,1), new ItemStack(t3_combat_Helmet,1,OreDictionary.WILDCARD_VALUE),TGItems.MECHANICAL_PARTS_CARBON, "circuitElite", TGItems.ELECTRIC_ENGINE, "ingotTitanium");
		RecipeJsonConverter.addShapelessRecipe(new ItemStack(t3_exo_Chestplate,1), new ItemStack(t3_combat_Chestplate,1,OreDictionary.WILDCARD_VALUE),TGItems.MECHANICAL_PARTS_CARBON, "circuitElite", TGItems.ELECTRIC_ENGINE, "ingotTitanium","ingotTitanium");
		RecipeJsonConverter.addShapelessRecipe(new ItemStack(t3_exo_Leggings,1), new ItemStack(t3_combat_Leggings,1,OreDictionary.WILDCARD_VALUE),TGItems.MECHANICAL_PARTS_CARBON, "circuitElite", TGItems.ELECTRIC_ENGINE, "ingotTitanium","ingotTitanium");
		RecipeJsonConverter.addShapelessRecipe(new ItemStack(t3_exo_Boots,1), new ItemStack(t3_combat_Boots,1,OreDictionary.WILDCARD_VALUE),TGItems.MECHANICAL_PARTS_CARBON, "circuitElite", TGItems.ELECTRIC_ENGINE, "ingotTitanium");
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_riot_Helmet,1), "php","l l",'p', "plateLead", 'l', new ItemStack(TGItems.TREATED_LEATHER.getItem(), 1, TGItems.TREATED_LEATHER.getItemDamage()), 'h', new ItemStack(t2_combat_Helmet,1,OreDictionary.WILDCARD_VALUE));
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_riot_Chestplate,1), "p p","lcl","lpl",'p', "plateLead", 'l', new ItemStack(TGItems.TREATED_LEATHER.getItem(), 1, TGItems.TREATED_LEATHER.getItemDamage()), 'c', new ItemStack(t2_combat_Chestplate,1,OreDictionary.WILDCARD_VALUE));
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_riot_Leggings,1), "lhl","p p","l l",'p', "plateLead", 'l', new ItemStack(TGItems.TREATED_LEATHER.getItem(), 1, TGItems.TREATED_LEATHER.getItemDamage()), 'h', new ItemStack(t2_combat_Leggings,1,OreDictionary.WILDCARD_VALUE));
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_riot_Boots,1), "lhl","p p",'p', "plateLead", 'l', new ItemStack(TGItems.TREATED_LEATHER.getItem(), 1, TGItems.TREATED_LEATHER.getItemDamage()), 'h', new ItemStack(t2_combat_Boots,1,OreDictionary.WILDCARD_VALUE));
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(t2_beret,1), " cc","c c", 'c', TGItems.HEAVY_CLOTH);
		
		RecipeJsonConverter.addShapedRecipe(new ItemStack(TGArmors.hazmat_Helmet,1), "fff","f f", 'f', TGItems.PROTECTIVE_FIBER);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(TGArmors.hazmat_Chestplate,1), "f f","fff","fff", 'f', TGItems.PROTECTIVE_FIBER);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(TGArmors.hazmat_Leggings,1), "fff","f f","f f", 'f', TGItems.PROTECTIVE_FIBER);
		RecipeJsonConverter.addShapedRecipe(new ItemStack(TGArmors.hazmat_Boots,1), "f f","f f", 'f', TGItems.PROTECTIVE_FIBER);

        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_power_Helmet, 1), "isi","sfs", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.MK2_POWER_PLATE, 'f', new ItemStack(t3_power_Helmet,1,OreDictionary.WILDCARD_VALUE));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_power_Chestplate, 1), "s s","ifi","sss", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.MK2_POWER_PLATE, 'f', new ItemStack(t3_power_Chestplate,1,OreDictionary.WILDCARD_VALUE));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_power_Leggings, 1), "sss","ifi","s s", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.MK2_POWER_PLATE, 'f', new ItemStack(t3_power_Leggings,1,OreDictionary.WILDCARD_VALUE));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_power_Boots, 1), "i i","sfs", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.MK2_POWER_PLATE, 'f', new ItemStack(t3_power_Boots,1,OreDictionary.WILDCARD_VALUE));

        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_praetor_Helmet, 1), "isi","sfs", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.DOOM_PLATE, 'f', new ItemStack(t3_miner_Helmet,1,OreDictionary.WILDCARD_VALUE));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_praetor_Chestplate, 1), "s s","ifi","sss", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.DOOM_PLATE, 'f', new ItemStack(t3_miner_Chestplate,1,OreDictionary.WILDCARD_VALUE));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_praetor_Leggings, 1), "sss","ifi","s s", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.DOOM_PLATE, 'f', new ItemStack(t3_miner_Leggings,1,OreDictionary.WILDCARD_VALUE));
        RecipeJsonConverter.addShapedRecipe(new ItemStack(t4_praetor_Boots, 1), "i i","sfs", 'i', TGItems.REINFORCED_ELECTRIC_ENGINE, 's', TGItems.DOOM_PLATE, 'f', new ItemStack(t3_miner_Boots,1,OreDictionary.WILDCARD_VALUE));
		
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGArmors.riot_shield,1), "igi","isi", "igi", 'i', "ingotSteel", 'g', "paneGlass", 's', Items.SHIELD);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGArmors.ballistic_shield,1), "igi","isi", "iii", 'i', "plateObsidianSteel", 'g', "paneGlass", 's', Items.SHIELD);
        RecipeJsonConverter.addShapedRecipe(new ItemStack(TGArmors.advanced_shield,1), "igi","isi", "iii", 'i', "plateCarbon", 'g', "paneGlass", 's', Items.SHIELD);
        
	}
}
