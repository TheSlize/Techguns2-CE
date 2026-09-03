package techguns;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import techguns.blocks.*;
import techguns.blocks.machines.*;
import techguns.init.ITGInitializer;
import techguns.plugins.chisel.ChiselIMCHelper;
import techguns.tools.BlockJsonCreator;

import java.util.ArrayList;

public class TGBlocks implements ITGInitializer{
	public static final ArrayList<IGenericBlock> BLOCKLIST = new ArrayList<>();
	
	//Machines
	public static BasicMachine<EnumMachineType> BASIC_MACHINE;
	
	public static SimpleMachine<EnumSimpleMachineType> SIMPLE_MACHINE;
	
	public static SimpleMachine<EnumSimpleMachineType2> SIMPLE_MACHINE2;
	
	public static MultiBlockMachine<EnumMultiBlockMachineType> MULTIBLOCK_MACHINE;
	
	public static BlockTGOre TG_ORE;
	
	public static BlockBioblob BIOBLOB;
	
	public static BlockSandbags SANDBAGS;
	
	public static BlockTGLamp<EnumLampType> LAMP_0;
	
	public static GenericBlockMetaEnum<TGMetalPanelType> METAL_PANEL;
	
	public static GenericBlockMetaEnum<EnumNetherMetalType> NETHER_METAL;
		
	public static BlockTGLadder<EnumLadderType> LADDER_0;

	public static BlockTGWindowFrame<EnumLadderType> WINDOW_FRAME_0;

	public static GenericBlockMetaEnum<EnumConcreteType> CONCRETE;
	
	public static BlockTGDoor3x3<EnumDoorType> DOOR3x3;
	
	public static BlockTGCamoNet CAMONET;
	public static BlockTGCamoNetTop CAMONET_TOP;
	
	public static BlockTGDoor2x1 BUNKER_DOOR;
	
	public static BlockTGStairs METAL_STAIRS;
	public static BlockTGStairs METAL_STAIRS_ALT;
	public static BlockTGStairs METAL_STAIRS_ALT1;
	public static BlockTGStairs METAL_STAIRS_ALT2;
	public static BlockTGStairs CONCRETE_STAIRS;
	public static BlockTGStairs CONCRETE_STAIRS_ALT;
	public static BlockTGStairs CONCRETE_STAIRS_ALT1;
	public static BlockTGStairs NETHER_METAL_STAIRS;
	public static BlockTGStairs NETHER_METAL_STAIRS_ALT;
	public static BlockTGStairs NETHER_METAL_STAIRS_ALT1;
	public static BlockTGStairs NETHER_METAL_STAIRS_ALT2;

	public static BlockTGSlab METAL_SLAB;
	public static BlockTGDoubleSlab METAL_DOUBLE_SLAB;

	public static BlockTGSlab CONCRETE_SLAB;
	public static BlockTGDoubleSlab CONCRETE_DOUBLE_SLAB;

	public static BlockTGSlab CONCRETE_SLAB_ALT;
	public static BlockTGDoubleSlab CONCRETE_DOUBLE_SLAB_ALT;

	public static BlockTGSlab NETHER_METAL_SLAB;
	public static BlockTGDoubleSlab NETHER_METAL_DOUBLE_SLAB;

	public static BlockTGSlab NETHER_METAL_SLAB_ALT;
	public static BlockTGDoubleSlab NETHER_METAL_DOUBLE_SLAB_ALT;

	public static BlockTGFence METAL_FENCE;
	public static BlockTGFence CONCRETE_FENCE;
	public static BlockTGFence NETHER_METAL_FENCE;

	public static BlockTGSpawner MONSTER_SPAWNER;
	
	public static GenericBlockMetaEnumCamoChangeable<EnumLightblockType> NEONLIGHT_BLOCK;
	
	public static BlockMilitaryCrate MILITARY_CRATE;
	
	public static BlockExplosiveCharge<EnumExplosiveChargeType> EXPLOSIVE_CHARGE;
	
	public static BlockTGSandHard SAND_HARD;
	
	public static BlockTGSlimy SLIMY_BLOCK;
	
	public static GenericBlockMetaEnum<EnumDebugBlockType> DEBUG_BLOCK;
	
	public static BlockTGSlimyLadder SLIMY_LADDER;
	
	public static BlockOreCluster<EnumOreClusterType> ORE_CLUSTER;
	
	public static BlockOreDrill ORE_DRILL_BLOCK;
	
	public static BlockEnergyCable ENERGY_CABLE;
	
	public static BlockUraniumEnergyCable URANIUM_ENERGY_CABLE;
	
	public static BlockFluidPipe FLUID_PIPE;
	
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		BLOCKLIST.forEach(b -> b.registerBlock(event));
	}
	
	public void registerItems(RegistryEvent.Register<Item> event) {
		BLOCKLIST.forEach(b -> event.getRegistry().register(b.createItemBlock()));
		
		OreDictionary.registerOre("oreCopper", TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_COPPER));
		OreDictionary.registerOre("oreTin", TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_TIN));
		OreDictionary.registerOre("oreLead", TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_LEAD));
		OreDictionary.registerOre("oreUranium", TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_URANIUM));
		OreDictionary.registerOre("oreTitaniumIron", TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_TITANIUM));
		OreDictionary.registerOre("oreIllmenite", TGBlocks.TG_ORE.getStackFor(EnumOreType.ORE_TITANIUM));
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		BLOCKLIST.forEach(IGenericBlock::registerItemBlockModels);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		BASIC_MACHINE= new BasicMachine<>("basicmachine", EnumMachineType.class);
		SIMPLE_MACHINE = new SimpleMachine<>("simplemachine", EnumSimpleMachineType.class);
		SIMPLE_MACHINE2 = new SimpleMachine<>("simplemachine2", EnumSimpleMachineType2.class);
		MULTIBLOCK_MACHINE = new MultiBlockMachine<>("multiblockmachine", EnumMultiBlockMachineType.class);
		TG_ORE = new BlockTGOre("basicore");
		BIOBLOB= new BlockBioblob("bioblob");
		SANDBAGS = (BlockSandbags) new BlockSandbags("sandbags").setHardness(6.0f);
		LAMP_0 = (BlockTGLamp<EnumLampType>) new BlockTGLamp<>("lamp0", EnumLampType.class).setHardness(4.0f);
		METAL_PANEL = (GenericBlockMetaEnum) new GenericBlockMetaEnumCamoChangeable<>("metalpanel", Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, TGMetalPanelType.class).setHardness(8.0f);
		NETHER_METAL = (GenericBlockMetaEnum) new GenericBlockMetaEnumCamoChangeableLightlevel<>("nethermetal", Material.IRON, Material.IRON.getMaterialMapColor(), SoundType.METAL, EnumNetherMetalType.class).setHardness(8.0f);
		
		CONCRETE = (GenericBlockMetaEnum<EnumConcreteType>) new GenericBlockMetaEnumCamoChangeable<>("concrete", Material.ROCK, EnumConcreteType.class).setHardness(8.0f);
		LADDER_0 = (BlockTGLadder<EnumLadderType>) new BlockTGLadder<>("ladder0", EnumLadderType.class).setHardness(6.0f);
		WINDOW_FRAME_0 = new BlockTGWindowFrame<>("windowframe", EnumLadderType.class)
				.setTextures("metal_ladder", "metal_ladder_shiny", "metal_ladder_rusty", "metal_ladder_carbon");
		WINDOW_FRAME_0.setHardness(6.0f);
		DOOR3x3 = (BlockTGDoor3x3<EnumDoorType>) new BlockTGDoor3x3<>("door3x3", EnumDoorType.class, TGItems.DOOR3x3).setHardness(6.0f);
		CAMONET = new BlockTGCamoNet("camonet");
		CAMONET_TOP = new BlockTGCamoNetTop("camonet_top");
		BUNKER_DOOR = (BlockTGDoor2x1) new BlockTGDoor2x1("bunkerdoor",TGItems.BUNKER_DOOR).setHardness(8.0f);
		
		METAL_STAIRS = (BlockTGStairs) new BlockTGStairs("stairs_metal", Material.IRON, SoundType.METAL).setTextures("panel_large_border", "steelframe_dark").setHardness(8.0f);
		METAL_STAIRS_ALT = (BlockTGStairs) new BlockTGStairs("stairs_metal_alt", Material.IRON, SoundType.METAL).setTextures("container_red", "container_green").setHardness(8.0f);
		METAL_STAIRS_ALT1 = (BlockTGStairs) new BlockTGStairs("stairs_metal_alt1", Material.IRON, SoundType.METAL).setTextures("container_blue", "container_orange").setHardness(8.0f);
		METAL_STAIRS_ALT2 = (BlockTGStairs) new BlockTGStairs("stairs_metal_alt2", Material.IRON, SoundType.METAL).setTextures("steelframe_blue", "steelframe_scaffold").setHardness(8.0f);
		CONCRETE_STAIRS = (BlockTGStairs) new BlockTGStairs("stairs_concrete", Material.ROCK, SoundType.STONE).setTextures("concrete_grey_dark", "concrete_brown_light").setHardness(6.0f);
		CONCRETE_STAIRS_ALT = (BlockTGStairs) new BlockTGStairs("stairs_concrete_alt", Material.ROCK, SoundType.STONE).setTextures("concrete_brown", "concrete_grey").setHardness(6.0f);
		CONCRETE_STAIRS_ALT1 = (BlockTGStairs) new BlockTGStairs("stairs_concrete_alt1", Material.ROCK, SoundType.STONE).setTextures("concrete_brown_light_scaff").setHardness(6.0f);

		NETHER_METAL_STAIRS = (BlockTGStairs) new BlockTGStairs("stairs_nethermetal", Material.IRON, SoundType.METAL).setTextures("nethermetal_panel", "nethermetal_grate1").setHardness(8.0f);
		NETHER_METAL_STAIRS_ALT = (BlockTGStairs) new BlockTGStairs("stairs_nethermetal_alt", Material.IRON, SoundType.METAL).setTextures("nethermetal_grate2", "nethermetal_grey_dark").setHardness(8.0f);
		NETHER_METAL_STAIRS_ALT1 = (BlockTGStairs) new BlockTGStairs("stairs_nethermetal_alt1", Material.IRON, SoundType.METAL).setTextures("nethermetal_grey", "nethermetal_grey_tiles").setHardness(8.0f);
		NETHER_METAL_STAIRS_ALT2 = (BlockTGStairs) new BlockTGStairs("stairs_nethermetal_alt2", Material.IRON, SoundType.METAL).setTextures("nethermetal_plate_red", "nethermetal_plate_black").setHardness(8.0f);

		METAL_SLAB = (BlockTGSlab) new BlockTGSlab("slab_metal", Material.IRON, SoundType.METAL).setTextures("container_red", "container_green",
				"container_blue", "container_orange", "panel_large_border", "steelframe_blue", "steelframe_dark", "steelframe_scaffold").setHardness(8.0f);
		METAL_DOUBLE_SLAB = (BlockTGDoubleSlab) new BlockTGDoubleSlab("double_slab_metal", Material.IRON, SoundType.METAL, METAL_SLAB).setHardness(8.0f);
		METAL_SLAB.setDoubleSlab(METAL_DOUBLE_SLAB);

		CONCRETE_SLAB = (BlockTGSlab) new BlockTGSlab("slab_concrete", Material.ROCK, SoundType.STONE).setTextures("concrete_brown", "concrete_brown_light", "concrete_grey", "concrete_grey_dark", "concrete_brown_pipes", "concrete_brown_light_scaff").setSideTexture(4, "concrete_brown").setHardness(8.0f);
		CONCRETE_DOUBLE_SLAB = (BlockTGDoubleSlab) new BlockTGDoubleSlab("double_slab_concrete", Material.ROCK, SoundType.STONE, CONCRETE_SLAB).setHardness(8.0f);
		CONCRETE_SLAB.setDoubleSlab(CONCRETE_DOUBLE_SLAB);

		CONCRETE_SLAB_ALT = (BlockTGSlab) new BlockTGSlab("slab_concrete_alt", Material.ROCK, SoundType.STONE).setTextures("concrete_brown_light_pipes", "concrete_grey_pipes", "concrete_grey_dark_pipes").setSideTexture(0, "concrete_brown_light")
				.setSideTexture(1, "concrete_grey").setSideTexture(2, "concrete_grey_dark").setHardness(8.0f);
		CONCRETE_DOUBLE_SLAB_ALT = (BlockTGDoubleSlab) new BlockTGDoubleSlab("double_slab_concrete_alt", Material.ROCK, SoundType.STONE, CONCRETE_SLAB).setHardness(8.0f);
		CONCRETE_SLAB_ALT.setDoubleSlab(CONCRETE_DOUBLE_SLAB_ALT);

		NETHER_METAL_SLAB = (BlockTGSlab) new BlockTGSlab("slab_nethermetal", Material.ROCK, SoundType.STONE).setTextures("nethermetal_panel", "nethermetal_grate1", "nethermetal_grate2", "nethermetal_grey_dark", "nethermetal_grey",
				"nethermetal_grey_tiles", "nethermetal_border_red", "nethermetal_plate_black").setSideTexture(6, "nethermetal_panel").setHardness(8.0f);
		NETHER_METAL_DOUBLE_SLAB = (BlockTGDoubleSlab) new BlockTGDoubleSlab("double_slab_nethermetal", Material.ROCK, SoundType.STONE, NETHER_METAL_SLAB).setHardness(8.0f);
		NETHER_METAL_SLAB.setDoubleSlab(NETHER_METAL_DOUBLE_SLAB);

		NETHER_METAL_SLAB_ALT = (BlockTGSlab) new BlockTGSlab("slab_nethermetal_alt", Material.ROCK, SoundType.STONE).setTextures("nethermetal_plate_red").setHardness(8.0f);
		NETHER_METAL_DOUBLE_SLAB_ALT = (BlockTGDoubleSlab) new BlockTGDoubleSlab("double_slab_nethermetal_alt", Material.ROCK, SoundType.STONE, NETHER_METAL_SLAB).setHardness(8.0f);
		NETHER_METAL_SLAB_ALT.setDoubleSlab(NETHER_METAL_DOUBLE_SLAB_ALT);

		METAL_FENCE = (BlockTGFence) new BlockTGFence("fence_metal", Material.IRON, SoundType.METAL).setTextures("container_red", "container_green",
				"container_blue", "container_orange", "panel_large_border", "steelframe_blue", "steelframe_dark", "steelframe_scaffold").setHardness(8.0f);
		CONCRETE_FENCE = (BlockTGFence) new BlockTGFence("fence_concrete", Material.IRON, SoundType.METAL).setTextures("concrete_brown", "concrete_brown_light", "concrete_grey", "concrete_grey_dark", "concrete_brown_light_scaff").setHardness(8.0f);
		NETHER_METAL_FENCE = (BlockTGFence) new BlockTGFence("fence_nethermetal", Material.IRON, SoundType.METAL).setTextures("nethermetal_panel", "nethermetal_grate1", "nethermetal_grate2", "nethermetal_grey_dark", "nethermetal_grey",
				"nethermetal_grey_tiles", "nethermetal_plate_black", "nethermetal_plate_red").setHardness(8.0f);

		MONSTER_SPAWNER = new BlockTGSpawner("tg_spawner");
		
		NEONLIGHT_BLOCK = (GenericBlockMetaEnumCamoChangeable<EnumLightblockType>) new GenericBlockMetaEnumCamoChangeable<>("neonlights", Material.GLASS, MapColor.YELLOW, SoundType.GLASS, EnumLightblockType.class).setLightLevel(1f).setHardness(4.0f);
		
		MILITARY_CRATE = (BlockMilitaryCrate) new BlockMilitaryCrate("military_crate", Material.WOOD).setHardness(4.0f);
		
		EXPLOSIVE_CHARGE = new BlockExplosiveCharge<>("explosive_charge", EnumExplosiveChargeType.class);
		
		SAND_HARD = new BlockTGSandHard("sand_hard", EnumTGSandHardTypes.class);
		
		SLIMY_BLOCK = new BlockTGSlimy("slimy", EnumTGSlimyType.class);
		SLIMY_LADDER = new BlockTGSlimyLadder("slimyladder");
		
		ORE_CLUSTER= new BlockOreCluster<>("orecluster", Material.ROCK, EnumOreClusterType.class);
		
		ORE_DRILL_BLOCK = new BlockOreDrill("oredrill");
		
		ENERGY_CABLE = new BlockEnergyCable("energy_cable");
		URANIUM_ENERGY_CABLE = new BlockUraniumEnergyCable("uranium_energy_cable");
		FLUID_PIPE = new BlockFluidPipe("fluid_pipe");
		
		if (TGConfig.misc.debug) {
			DEBUG_BLOCK = new BlockDebugMarker("debugblock", Material.GROUND);
		}
		
		if(TGItems.WRITE_ITEM_JSON && event.getSide()==Side.CLIENT){
			BLOCKLIST.stream().filter(t -> {
				if(t instanceof GenericBlockMetaEnum) {
					return ((GenericBlockMetaEnum)t).shouldAutoGenerateJsonForEnum();
				}
				return false;
			}).forEach(b -> BlockJsonCreator.writeBlockstateJsonFileForBlock((GenericBlockMetaEnum)b));
		}	
	}

	@Override
	public void init(FMLInitializationEvent event) {
		TGWindowPanes.init();

		ChiselIMCHelper.addChiselVariants("techguns:camonet", TGBlocks.CAMONET, EnumCamoNetType.class);
		ChiselIMCHelper.addChiselVariants("techguns:camonettop", TGBlocks.CAMONET_TOP, EnumCamoNetType.class);
		ChiselIMCHelper.addChiselVariants("techguns:metalpanel", TGBlocks.METAL_PANEL, TGMetalPanelType.class);
		ChiselIMCHelper.addChiselVariants("techguns:neonlights", TGBlocks.NEONLIGHT_BLOCK, EnumLightblockType.class);
		ChiselIMCHelper.addChiselVariants("reinforced_concrete", TGBlocks.CONCRETE, EnumConcreteType.class);
		
		for(EnumLadderType t: EnumLadderType.values()) {
			ChiselIMCHelper.addChiselVariation("techguns:metalladder", TGBlocks.LADDER_0.getRegistryName(), TGBlocks.LADDER_0.getMetaFromState(TGBlocks.LADDER_0.getDefaultState().withProperty(TGBlocks.LADDER_0.TYPE, t)));
			ChiselIMCHelper.addChiselVariation("techguns:metalwindowframe", TGBlocks.WINDOW_FRAME_0.getRegistryName(), TGBlocks.WINDOW_FRAME_0.getMetaFromState(TGBlocks.WINDOW_FRAME_0.getDefaultState().withProperty(TGBlocks.WINDOW_FRAME_0.TYPE, t)));
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}


}
