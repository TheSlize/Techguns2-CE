package techguns;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID, name = "techguns")
@Config.LangKey("config.techguns.title")
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class TGConfig {

	@Config.Name("General")
	@Config.LangKey("config.techguns.general")
	public static General general = new General();

	public static class General {
		@Config.Comment("Limits using unsafe guns mode to opped players; the config option OVERRIDES the permission setting 'techguns.allowunsafemode'")
		@Config.LangKey("config.techguns.general.limitUnsafeModeToOP")
		@Config.RequiresMcRestart
		public boolean limitUnsafeModeToOP = false;

		@Config.Comment("Disable automatic feeding of Food in the Techguns inventory tab.")
		@Config.LangKey("config.techguns.general.disableAutofeeder")
		public boolean disableAutofeeder = false;

		@Config.Comment("Disables power usage for all machines; activate this if you don't want to install additional tech mods.")
		@Config.LangKey("config.techguns.general.machinesNeedNoPower")
		public boolean machinesNeedNoPower = false;

		@Config.Comment("Keep recipes with lava instead of fuel even when fuel from any other mods is present.")
		@Config.LangKey("config.techguns.general.keepLavaRecipesWhenFuelIsPresent")
		@Config.RequiresMcRestart
		public boolean keepLavaRecipesWhenFuelIsPresent = false;

		@Config.Comment("Disable Radiation for players. (Radiation system is still WIP)")
		@Config.LangKey("config.techguns.general.WIP_disableRadiationSystem")
		@Config.RequiresMcRestart
		public boolean WIP_disableRadiationSystem = false;
	}

	@Config.Name("Misc")
	@Config.LangKey("config.techguns.misc")
	public static Misc misc = new Misc();

	public static class Misc {
		@Config.Comment("Enable debug items and unfinished stuff; disable this for regular survival.")
		@Config.LangKey("config.techguns.misc.debug")
		@Config.RequiresMcRestart
		public boolean debug = false;

		@Config.Comment("Highest blockHardness normal explosive charges can break; obsidian is 50.0")
		@Config.LangKey("config.techguns.misc.explosiveChargeMaxBlockHardness")
		public float explosiveChargeMaxBlockHardness = 30.0f;

		@Config.Comment("Highest blockHardness advanced explosive charges can break; obsidian is 50.0")
		@Config.LangKey("config.techguns.misc.explosiveChargeAdvancedMaxBlockHardness")
		public float explosiveChargeAdvancedMaxBlockHardness = 100.0f;

		@Config.Comment("Disable gun drops from mobs / NPCs")
		@Config.LangKey("config.techguns.misc.disableGunDrops")
		public boolean disableGunDrops = false;

		@Config.Comment("Disable armor drops from mobs / NPCs")
		@Config.LangKey("config.techguns.misc.disableArmorDrops")
		public boolean disableArmorDrops = false;

		@Config.Comment("Base XP value for Upgrade Bench recipes (enchants)")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.misc.upgrade_xp_cost")
		public int upgrade_xp_cost = 20;

		@Config.Comment("Enables steel recipe in a TG blast furnace.")
		@Config.LangKey("config.techguns.misc.addSteelRecipe")
		@Config.RequiresMcRestart
		public boolean addSteelRecipe = true;

		@Config.Comment("Registers oreDicts entries for carbon, titanium, circuits and other items that might be used from other mods.")
		@Config.LangKey("config.techguns.misc.addOreDicts")
		@Config.RequiresMcRestart
		public boolean addOreDicts = true;

		@Config.Comment("false = default TG mode (spawns only ITGSpawnerNPC mobs from presets). true = custom mode (spawns any EntityLiving from this spawner's mobtypes NBT id; use this when editing spawner NBT to point at entities from other mods).")
		@Config.LangKey("config.techguns.misc.spawnerBlockUseCustomEntitySpawn")
		public boolean spawnerBlockUseCustomEntitySpawn = false;
	}

	@Config.Name("SRParasites Integration")
	@Config.Comment("Options for those using Techguns with the Scape & Run: Parasites mod (or other mods with hard monsters)")
	@Config.LangKey("config.techguns.srpIntegration")
	public static SRPIntegration srpIntegration = new SRPIntegration();

	public static class SRPIntegration {
		@Config.Comment("Increase the Nuclear Death Ray's damage to make it the only weapon against the parasites. false = default NDR damage")
		@Config.LangKey("config.techguns.srpIntegration.rebalance_nucleardeathray")
		@Config.RequiresMcRestart
		public boolean rebalance_nucleardeathray = false;

		@Config.Comment("Increase the BFG-10K's damage to make it more deadly for parasites. false = default BFG damage")
		@Config.LangKey("config.techguns.srpIntegration.rebalance_tfg")
		@Config.RequiresMcRestart
		public boolean rebalance_tfg = false;
	}

	@Config.Name("Disable Items")
	@Config.LangKey("config.techguns.disable_items")
	public static DisableItems disableItems = new DisableItems();

	public static class DisableItems {
		@Config.Comment("Add copper ingots.")
		@Config.LangKey("config.techguns.disable_items.addCopperIngots")
		@Config.RequiresMcRestart
		public boolean addCopperIngots = true;

		@Config.Comment("Add copper nuggets.")
		@Config.LangKey("config.techguns.disable_items.addCopperNuggets")
		@Config.RequiresMcRestart
		public boolean addCopperNuggets = true;

		@Config.Comment("Add tin ingots.")
		@Config.LangKey("config.techguns.disable_items.addTinIngots")
		@Config.RequiresMcRestart
		public boolean addTinIngots = true;

		@Config.Comment("Add bronze ingots.")
		@Config.LangKey("config.techguns.disable_items.addBronzeIngots")
		@Config.RequiresMcRestart
		public boolean addBronzeIngots = true;

		@Config.Comment("Add lead ingots.")
		@Config.LangKey("config.techguns.disable_items.addLeadIngots")
		@Config.RequiresMcRestart
		public boolean addLeadIngots = true;

		@Config.Comment("Add Lead nuggets.")
		@Config.LangKey("config.techguns.disable_items.addLeadNuggets")
		@Config.RequiresMcRestart
		public boolean addLeadNuggets = true;

		@Config.Comment("Add steel ingots.")
		@Config.LangKey("config.techguns.disable_items.addSteelIngots")
		@Config.RequiresMcRestart
		public boolean addSteelIngots = true;

		@Config.Comment("Add steel nuggets.")
		@Config.LangKey("config.techguns.disable_items.addSteelNuggets")
		@Config.RequiresMcRestart
		public boolean addSteelNuggets = true;
	}

	@Config.Name("NPC Spawn")
	@Config.LangKey("config.techguns.npc_spawn")
	public static NpcSpawn npcSpawn = new NpcSpawn();

	public static class NpcSpawn {
		@Config.Comment("Up to which distance to worldspawn only mobs with danger level up to 0 will spawn")
		@Config.LangKey("config.techguns.npc_spawn.distanceSpawnLevel0")
		@Config.RequiresMcRestart
		public int distanceSpawnLevel0 = 500;

		@Config.Comment("Up to which distance to worldspawn only mobs with danger level up to 1 will spawn")
		@Config.LangKey("config.techguns.npc_spawn.distanceSpawnLevel1")
		@Config.RequiresMcRestart
		public int distanceSpawnLevel1 = 1000;

		@Config.Comment("Up to which distance to worldspawn only mobs with danger level up to 2 will spawn")
		@Config.LangKey("config.techguns.npc_spawn.distanceSpawnLevel2")
		@Config.RequiresMcRestart
		public int distanceSpawnLevel2 = 2500;

		@Config.Comment("Spawn weight of Techguns NPCs in the Overworld; determines how many TG NPCs spawn")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightTGOverworld")
		@Config.RequiresMcRestart
		public int spawnWeightTGOverworld = 600;

		@Config.Comment("Spawn weight of Techguns NPCs in the Nether; determines how many TG NPCs spawn")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightTGNether")
		@Config.RequiresMcRestart
		public int spawnWeightTGNether = 300;

		@Config.Comment("Spawn weight of Techguns NPCs in the End; determines how many TG NPCs spawn")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightTGEnd")
		@Config.RequiresMcRestart
		public int spawnWeightTGEnd = 5;

		@Config.Comment("Spawn weight for spawning Zombie Soldiers; at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightZombieSoldier")
		@Config.RequiresMcRestart
		public int spawnWeightZombieSoldier = 100;

		@Config.Comment("Spawn weight for spawning Zombie Farmers; at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightZombieFarmer")
		@Config.RequiresMcRestart
		public int spawnWeightZombieFarmer = 200;

		@Config.Comment("Spawn weight for spawning Zombie Miners; at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightZombieMiner")
		@Config.RequiresMcRestart
		public int spawnWeightZombieMiner = 200;

		@Config.Comment("Spawn weight for spawning Zombie Pigman Soldiers (Nether only); at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightZombiePigmanSoldier")
		@Config.RequiresMcRestart
		public int spawnWeightZombiePigmanSoldier = 80;

		@Config.Comment("Spawn weight for spawning Cyber Demons (Nether only); at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightCyberDemon")
		@Config.RequiresMcRestart
		public int spawnWeightCyberDemon = 30;

		@Config.Comment("Spawn weight for spawning Ghastlings (Nether only); at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightGhastling")
		@Config.RequiresMcRestart
		public int spawnWeightGhastling = 10;

		@Config.Comment("Spawn weight for spawning Basic Super Mutants (End only); at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightSuperMutantBasic")
		@Config.RequiresMcRestart
		public int spawnWeightSuperMutantBasic = 100;

		@Config.Comment("Spawn weight for spawning Elite Super Mutants (End only); at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightSuperMutantElite")
		@Config.RequiresMcRestart
		public int spawnWeightSuperMutantElite = 40;

		@Config.Comment("Spawn weight for spawning Heavy Super Mutants (End only); at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightSuperMutantHeavy")
		@Config.RequiresMcRestart
		public int spawnWeightSuperMutantHeavy = 10;

		@Config.Comment("Spawn weight for spawning Bandit groups; at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightBandit")
		@Config.RequiresMcRestart
		public int spawnWeightBandit = 50;

		@Config.Comment("Spawn weight for spawning Skeleton Soldiers; at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightSkeletonSoldier")
		@Config.RequiresMcRestart
		public int spawnWeightSkeletonSoldier = 100;

		@Config.Comment("Spawn weight for spawning Psycho Steve, an early-game boss; at 0 spawn will not be registered")
		@Config.RangeInt(min = 0, max = 10000)
		@Config.LangKey("config.techguns.npc_spawn.spawnWeightPsychoSteve")
		@Config.RequiresMcRestart
		public int spawnWeightPsychoSteve = 3;

		@Config.Comment("Biome Registry names (e.g: minecraft:mushroom_island) that are excluded from Techguns monster spawning")
		@Config.LangKey("config.techguns.npc_spawn.biomeBlacklist")
		@Config.RequiresMcRestart
		public String[] biomeBlacklist = new String[]{""};

		@Config.Comment("Enables auto-adding dimensions for Techguns spawn lists (e.g. if the dimension has any of vanilla types, it will have corresponding spawnlist automatically)")
		@Config.LangKey("config.techguns.npc_spawn.dimensionAutoadd")
		@Config.RequiresMcRestart
		public boolean dimensionAutoadd = false;

		@Config.Comment("Dimensions (e.g: 12, 13, 14 etc.) that are included in Techguns monster spawning to use Overworld spawnlist. NOTE: ONLY WORKS IF DimensionsAutoAdd TURNED OFF!")
		@Config.LangKey("config.techguns.npc_spawn.dimensionWhitelistOverworld")
		@Config.RequiresMcRestart
		public String[] dimensionWhitelistOverworld = new String[]{""};

		@Config.Comment("Dimensions (e.g: 12, 13, 14 etc.) that are included in Techguns monster spawning in the Nether spawnlist. NOTE: ONLY WORKS IF DimensionsAutoAdd TURNED OFF!")
		@Config.LangKey("config.techguns.npc_spawn.dimensionWhitelistNether")
		@Config.RequiresMcRestart
		public String[] dimensionWhitelistNether = new String[]{""};

		@Config.Comment("Dimensions (e.g: 12, 13, 14 etc.) that are included in Techguns monster spawning in the End spawnlist. NOTE: ONLY WORKS IF DimensionsAutoAdd TURNED OFF!")
		@Config.LangKey("config.techguns.npc_spawn.dimensionWhitelistEnd")
		@Config.RequiresMcRestart
		public String[] dimensionWhitelistEnd = new String[]{""};
	}

	@Config.Name("Damage Factors")
	@Config.LangKey("config.techguns.damage_factors")
	public static DamageFactors damageFactors = new DamageFactors();

	public static class DamageFactors {
		@Config.Comment("Damage factor Techguns weapons deal when fired from players against other players; it automatically sets to zero when PvP is disabled")
		@Config.RangeDouble(min = 0.0, max = 100.0)
		@Config.LangKey("config.techguns.damage_factors.damagePvP")
		public float damagePvP = 0.5f;

		@Config.Comment("Damage factor Techguns Turrets deal when hitting players")
		@Config.RangeDouble(min = 0.0, max = 100.0)
		@Config.LangKey("config.techguns.damage_factors.damageTurretToPlayer")
		public float damageTurretToPlayer = 0.5f;

		@Config.Comment("Damage factor for all NPCs other than turrets, multiplied by default difficulty penalty (easy - 0.6, normal - 0.8, hard - 1.0)")
		@Config.RangeDouble(min = 0.0, max = 100.0)
		@Config.LangKey("config.techguns.damage_factors.damageFactorNPC")
		public float damageFactorNPC = 1.0f;
	}

	@Config.Name("World Generation")
	@Config.LangKey("config.techguns.worldgen")
	public static WorldGeneration worldgen = new WorldGeneration();

	public static class WorldGeneration {
		@Config.Comment("Generate Copper Ore (advised to disable if other mod does)")
		@Config.LangKey("config.techguns.worldgen.doOreGenCopper")
		@Config.RequiresMcRestart
		public boolean doOreGenCopper = true;

		@Config.Comment("Generate Tin Ore (advised to disable if other mod does)")
		@Config.LangKey("config.techguns.worldgen.doOreGenTin")
		@Config.RequiresMcRestart
		public boolean doOreGenTin = true;

		@Config.Comment("Generate Lead Ore (advised to disable if other mod does)")
		@Config.LangKey("config.techguns.worldgen.doOreGenLead")
		@Config.RequiresMcRestart
		public boolean doOreGenLead = true;

		@Config.Comment("Generate Titanium (advised to disable if other mod does)")
		@Config.LangKey("config.techguns.worldgen.doOreGenTitanium")
		@Config.RequiresMcRestart
		public boolean doOreGenTitanium = true;

		@Config.Comment("Generate Uranium (advised to disable if other mod does)")
		@Config.LangKey("config.techguns.worldgen.doOreGenUranium")
		@Config.RequiresMcRestart
		public boolean doOreGenUranium = true;

		@Config.Comment("Should any Techguns structures spawn in the world?")
		@Config.LangKey("config.techguns.worldgen.doWorldspawn")
		@Config.RequiresMcRestart
		public boolean doWorldspawn = true;

		@Config.Comment("Every X chunks Techguns tries to spawn a Big building. This is only in overworld; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 16, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureBigOverworld")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureBigOverworld = 64;

		@Config.Comment("Every X chunks Techguns tries to spawn a Small building. This is only in overworld; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 4, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureSmallOverworld")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureSmallOverworld = 16;

		@Config.Comment("Every X chunks Techguns tries to spawn a Medium building. This is only in overworld; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 8, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureMediumOverworld")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureMediumOverworld = 32;

		@Config.Comment("Every X chunks Techguns tries to spawn a Big building. This is only in the Nether; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 16, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureBigNether")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureBigNether = 24;

		@Config.Comment("Every X chunks Techguns tries to spawn a Small building. This is only in the Nether; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 4, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureSmallNether")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureSmallNether = 8;

		@Config.Comment("Every X chunks Techguns tries to spawn a Medium building. This is only in the Nether; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 8, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureMediumNether")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureMediumNether = 10;

		@Config.Comment("Every X chunks Techguns tries to spawn a Big building. This is only in the End; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 8, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureBigEnd")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureBigEnd = 64;

		@Config.Comment("Every X chunks Techguns tries to spawn a Small building. This is only in the End; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 8, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureSmallEnd")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureSmallEnd = 16;

		@Config.Comment("Every X chunks Techguns tries to spawn a Medium building. This is only in the End; ChunkX, and ChunkY modulo <this Value> must be 0")
		@Config.RangeInt(min = 8, max = 100000)
		@Config.LangKey("config.techguns.worldgen.spawnWeightTGStructureMediumEnd")
		@Config.RequiresWorldRestart
		public int spawnWeightTGStructureMediumEnd = 42;

		@Config.Comment("When worldgen is enabled, include structure spawns that contain ore clusters.")
		@Config.LangKey("config.techguns.worldgen.spawnOreClusterStructures")
		@Config.RequiresWorldRestart
		public boolean spawnOreClusterStructures = true;
	}

	@Config.Name("Clientside")
	@Config.Comment("Clientside options, can be changed when playing on a server")
	@Config.LangKey("config.techguns.clientside")
	public static Clientside clientside = new Clientside();

	public static class Clientside {
		@Config.Name("Ammo HUD")
		@Config.LangKey("config.techguns.ammo_hud")
		public AmmoHud ammoHud = new AmmoHud();

		@Config.Comment("Enables Techguns death effects in general (biogun, lasergun and, if enabled, gore).")
		@Config.LangKey("config.techguns.clientside.cl_enableDeathFX")
		public boolean cl_enableDeathFX = true;

		@Config.Comment("Enable the gore Death Effect; requires 'Enable Death Effects' to be enabled.")
		@Config.LangKey("config.techguns.clientside.cl_enableDeathFX_Gore")
		public boolean cl_enableDeathFX_Gore = true;

		@Config.Comment("Counters the speed dependant FOV change. This also stops FOV changes while sprinting. Don't activate if another mod does this too.")
		@Config.LangKey("config.techguns.clientside.cl_lockSpeedFov")
		public boolean cl_lockSpeedFov = true;

		@Config.Comment("Multiply the FOV while sprinting by this value independent from the actual speed, has no effect when LockSpeedDependantFov is false.")
		@Config.RangeDouble(min = 1.0, max = 10.0)
		@Config.LangKey("config.techguns.clientside.cl_fixedSprintFov")
		public float cl_fixedSprintFov = 1.15f;

		@Config.Comment("Each tick, Techguns particles are sorted via bubble sorting to determine in which order they should render. This parameter defines how many sort passes should be done in a single tick. 0 disables sorting entirely.")
		@Config.RangeInt(min = 0, max = 20)
		@Config.LangKey("config.techguns.clientside.cl_sortPassesPerTick")
		public int cl_sortPassesPerTick = 10;
	}

	public static class AmmoHud {
		@Config.Comment("Enables legacy ammo HUD while holding a gun (legacy means before 2.1 version)")
		@Config.LangKey("config.techguns.ammo_hud.cl_enableLegacyHud")
		public boolean cl_enableLegacyHud = false;

		@Config.Comment("Changes the scale of the ammo text (e.g. 30/30 on AKM)")
		@Config.RangeDouble(min = 0.01, max = 10.0)
		@Config.LangKey("config.techguns.ammo_hud.cl_ammoTextScale")
		public float cl_ammoTextScale = 1.0f;

		@Config.Comment("Changes the scale of the ammo icon (e.g. rifle ammo on AKM)")
		@Config.RangeDouble(min = 0.01, max = 10.0)
		@Config.LangKey("config.techguns.ammo_hud.cl_ammoIconScale")
		public float cl_ammoIconScale = 1.0f;

		@Config.Comment("Changes the scale of the magazine count text (e.g. x19)")
		@Config.RangeDouble(min = 0.01, max = 10.0)
		@Config.LangKey("config.techguns.ammo_hud.cl_ammoMagTextScale")
		public float cl_ammoMagTextScale = 0.65f;

		@Config.Comment("How many pixels should the ammo counter be away from the right side of the screen?")
		@Config.RangeInt(min = 0, max = 9999)
		@Config.LangKey("config.techguns.ammo_hud.cl_hudMarginRight")
		public int cl_hudMarginRight = 40;

		@Config.Comment("How many pixels should the ammo counter be away from the bottom side of the screen?")
		@Config.RangeInt(min = 0, max = 9999)
		@Config.LangKey("config.techguns.ammo_hud.cl_hudMarginBottom")
		public int cl_hudMarginBottom = 40;

		@Config.Comment("How many pixels should the ammo icon be away from the ammo count text? (note: it's always moved LEFT from the text)")
		@Config.RangeInt(min = 0, max = 999)
		@Config.LangKey("config.techguns.ammo_hud.cl_iconTextGap")
		public int cl_iconTextGap = 6;

		@Config.Comment("How many pixels should the mag count text be away from the ammo count text? (note: it's always moved LOWER from the a.c. text)")
		@Config.RangeInt(min = 0, max = 999)
		@Config.LangKey("config.techguns.ammo_hud.cl_textMagGap")
		public int cl_textMagGap = 3;
	}

	@Config.Name("Fluid Settings")
	@Config.LangKey("config.techguns.fluid_recipes")
	public static FluidSettings fluidSettings = new FluidSettings();

	public static class FluidSettings {
		@Config.Comment("Fluids that can be used to fill up fuel tanks")
		@Config.LangKey("config.techguns.fluid_recipes.fluidListFuel")
		@Config.RequiresMcRestart
		public String[] fluidListFuel = new String[]{"fuel", "refined_fuel", "biofuel", "biodiesel", "diesel", "gasoline", "fluiddiesel",
				"fluidnitrodiesel", "fliudnitrofuel", "refined_biofuel", "fire_water", "rocket_fuel"};

		@Config.Comment("Fluids that are treated as oil.")
		@Config.LangKey("config.techguns.fluid_recipes.fluidListOil")
		@Config.RequiresMcRestart
		public String[] fluidListOil = new String[]{"oil", "tree_oil", "crude_oil", "fluidoil", "seed_oil"};

		@Config.Comment("Fluids that are treated as oil for oil vein and oil ore clusters world spawn.")
		@Config.LangKey("config.techguns.fluid_recipes.fluidListOilWorldspawn")
		@Config.RequiresMcRestart
		public String[] fluidListOilWorldspawn = new String[]{"oil", "crude_oil"};
	}

	@Config.Name("Ore Drills")
	@Config.LangKey("config.techguns.ore_drills")
	public static OreDrills oreDrills = new OreDrills();

	public static class OreDrills {
		@Config.Comment("Multiplier to default rate on how many ores an ore drill produces")
		@Config.RangeDouble(min = 0.001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oreDrillMultiplierOres")
		public float oreDrillMultiplierOres = 1.0f;

		@Config.Comment("Multiplier to default rate on how much power an ore drill requires")
		@Config.RangeDouble(min = 0.0, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oreDrillMultiplierPower")
		public float oreDrillMultiplierPower = 1.0f;

		@Config.Comment("Multiplier to calculate value of furnace fuel burn time = RF for ore Drill. burnTime * <THIS_VALUE> = RF. Only for internal use of the ore Drill, no real RF generation.")
		@Config.RangeDouble(min = 1.0, max = 100000.0)
		@Config.LangKey("config.techguns.ore_drills.oreDrillMultiplierFuel")
		public float oreDrillMultiplierFuel = 1000.0f;

		@Config.Comment("Fuel value for Liquid Fuel for use in ore Drills per mB")
		@Config.RangeDouble(min = 1.0, max = 100000.0)
		@Config.LangKey("config.techguns.ore_drills.oreDrillFuelValueFuel")
		public float oreDrillFuelValueFuel = 100.0f;

		@Config.Comment("Enables original Techguns' list of ores to ore clusters")
		@Config.LangKey("config.techguns.ore_drills.addDefaultClusterOres")
		public boolean addDefaultClusterOres = true;

		@Config.Comment("A list where you can add your own oredicts to ore clusters. Scheme is: 'oredictName;enumOreClusterType;weight;amount'. Example is: 'oreIron;URANIUM;30;2'. Available enumOreClusterTypes: COAL, COMMON_METAL, RARE_METAL, SHINY_METAL, URANIUM, COMMON_GEM, SHINY_GEM, NETHER_CRYSTAL, OIL")
		@Config.LangKey("config.techguns.ore_drills.additionalClusterOres")
		public String[] additionalClusterOres = new String[]{""};

		@Config.Comment("Mining Level for coal ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_coal")
		public int mininglevel_coal = 0;

		@Config.Comment("Mining Level for common metal ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_common_metal")
		public int mininglevel_common_metal = 0;

		@Config.Comment("Mining Level for rare metal ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_rare_metal")
		public int mininglevel_rare_metal = 1;

		@Config.Comment("Mining Level for shiny metal ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_shiny_metal")
		public int mininglevel_shiny_metal = 2;

		@Config.Comment("Mining Level for uranium ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_uranium")
		public int mininglevel_uranium = 3;

		@Config.Comment("Mining Level for common gem ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_common_gem")
		public int mininglevel_common_gem = 1;

		@Config.Comment("Mining Level for shiny gem ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_shiny_gem")
		public int mininglevel_shiny_gem = 3;

		@Config.Comment("Mining Level for nether crystal ore clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_nether_crystal")
		public int mininglevel_nether_crystal = 2;

		@Config.Comment("Mining Level for oil clusters")
		@Config.RangeInt(min = 0, max = 10)
		@Config.LangKey("config.techguns.ore_drills.mininglevel_oil")
		public int mininglevel_oil = 2;

		@Config.Comment("Ore Multiplier for coal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_coal")
		public float oremult_coal = 20f;

		@Config.Comment("Ore Multiplier for common metal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_common_metal")
		public float oremult_common_metal = 10f;

		@Config.Comment("Ore Multiplier for rare metal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_rare_metal")
		public float oremult_rare_metal = 5f;

		@Config.Comment("Ore Multiplier for shiny metal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_shiny_metal")
		public float oremult_shiny_metal = 2f;

		@Config.Comment("Ore Multiplier for uranium ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_uranium")
		public float oremult_uranium = 1f;

		@Config.Comment("Ore Multiplier for common gem ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_common_gem")
		public float oremult_common_gem = 10f;

		@Config.Comment("Ore Multiplier for shiny gem ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_shiny_gem")
		public float oremult_shiny_gem = 0.4f;

		@Config.Comment("Ore Multiplier for nether crystal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_nether_crystal")
		public float oremult_nether_crystal = 8f;

		@Config.Comment("Ore Multiplier for oil clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.oremult_oil")
		public float oremult_oil = 8f;

		@Config.Comment("Power Multiplier for coal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_coal")
		public float powermult_coal = 0.08f;

		@Config.Comment("Power Multiplier for common metal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_common_metal")
		public float powermult_common_metal = 0.16f;

		@Config.Comment("Power Multiplier for rare metal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_rare_metal")
		public float powermult_rare_metal = 0.32f;

		@Config.Comment("Power Multiplier for shiny metal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_shiny_metal")
		public float powermult_shiny_metal = 0.8f;

		@Config.Comment("Power Multiplier for uranium ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_uranium")
		public float powermult_uranium = 0.8f;

		@Config.Comment("Power Multiplier for common gem ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_common_gem")
		public float powermult_common_gem = 0.16f;

		@Config.Comment("Power Multiplier for shiny gem ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_shiny_gem")
		public float powermult_shiny_gem = 0.8f;

		@Config.Comment("Power Multiplier for nether crystal ore clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_nether_crystal")
		public float powermult_nether_crystal = 0.4f;

		@Config.Comment("Power Multiplier for oil clusters")
		@Config.RangeDouble(min = 0.0001, max = 1000.0)
		@Config.LangKey("config.techguns.ore_drills.powermult_oil")
		public float powermult_oil = 0.8f;
	}

	static {
		try {
			Class.forName("com.cleanroommc.configanytime.ConfigAnytime")
					.getMethod("register", Class.class)
					.invoke(null, TGConfig.class);
		} catch (Exception ignored) {
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equalsIgnoreCase(Tags.MOD_ID)) {
			ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
		}
	}
}