package techguns.client;

import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import techguns.*;
import techguns.api.npc.INPCTechgunsShooter;
import techguns.blocks.*;
import techguns.capabilities.TGDeathTypeCap;
import techguns.capabilities.TGDeathTypeCapStorage;
import techguns.capabilities.TGExtendedPlayerClient;
import techguns.capabilities.TGShooterValues;
import techguns.client.audio.TGSound;
import techguns.client.audio.TGSoundCategory;
import techguns.client.models.armor.*;
import techguns.client.models.guns.*;
import techguns.client.models.items.ModelARMagazine;
import techguns.client.models.items.ModelAS50Mag;
import techguns.client.models.items.ModelLmgMag;
import techguns.client.models.machines.ModelAmmoPress;
import techguns.client.models.machines.ModelChemLab;
import techguns.client.models.machines.ModelMetalPress;
import techguns.client.models.machines.ModelTurretBase;
import techguns.client.models.projectiles.ModelRocket;
import techguns.client.particle.*;
import techguns.client.render.AdditionalSlotRenderRegistry;
import techguns.client.render.ItemRenderHack;
import techguns.client.render.RenderAdditionalSlotItem;
import techguns.client.render.RenderAdditionalSlotSharedItem;
import techguns.client.render.entities.TGLayerRendererer;
import techguns.client.render.entities.npcs.*;
import techguns.client.render.entities.projectiles.*;
import techguns.client.render.fx.IScreenEffect;
import techguns.client.render.fx.ScreenEffect;
import techguns.client.render.item.*;
import techguns.client.render.tileentities.*;
import techguns.deatheffects.EntityDeathUtils.DeathType;
import techguns.debug.Keybinds;
import techguns.entities.npcs.*;
import techguns.entities.projectiles.*;
import techguns.events.TGGuiEvents;
import techguns.events.TechgunsGuiHandler.GuiHandlerRegister;
import techguns.gui.*;
import techguns.gui.containers.*;
import techguns.gui.player.tabs.TGPlayerTab;
import techguns.items.guns.GenericGun;
import techguns.keybind.TGKeybinds;
import techguns.tileentities.*;
import techguns.util.BlockUtils;
import techguns.util.EntityCondition;

import java.lang.reflect.Method;
import java.util.*;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	public TGParticleManager particleManager = new TGParticleManager();
	
	protected GuiHandlerRegister guihandler = new GuiHandlerRegister();

	public LinkedList<LightPulse> activeLightPulses;
	
	private boolean lightPulsesEnabled=true;
	
	/**
	 * if local player has pressed the fire key this tick
	 */
	public boolean keyFirePressedMainhand;
	public boolean keyFirePressedOffhand;

	@SideOnly(Side.CLIENT)
	public float player_zoom=1.0f;
	
	//client side safeguard to prevent multiple reloadsounds overlapping caused by deyncs
	public long lastReloadsoundPlayed=0L;
	
	public float PARTIAL_TICK_TIME;
	//local muzzle flash jitter offsets
	public float muzzleFlashJitterX = 0; //-1.0 to 1.0
	public float muzzleFlashJitterY = 0; //-1.0 to 1.0
	public float muzzleFlashJitterAngle = 0; //-1.0 to 1.0
	public float muzzleFlashJitterScale = 0; //-1.0 to 1.0
	
	public boolean hasStepassist=false;
	
	public boolean hasNightvision=false;

	public static boolean ignoreBlockstatesForCustomModels = false;

	public static final StateMapperBase CUSTOM_STATE_MAPPER = new StateMapperBase() {
		@Override
		public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
			if (ignoreBlockstatesForCustomModels) {
				return Collections.emptyMap();
			}
			return super.putStateModelLocations(blockIn);
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return new ModelResourceLocation(state.getBlock().getRegistryName(), BlockUtils.getBlockStateVariantString(state));
		}
	};
	
	protected HashMap<String,ModelBiped> armorModelRegistry = new HashMap<>();
	
	public void registerArmorModel(ResourceLocation key, ModelBiped model) {
		this.armorModelRegistry.put(key.toString(), model);
	}
	
	public ModelBiped getArmorModel(ResourceLocation key) {
		return this.armorModelRegistry.get(key.toString());
	}


    @Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		OBJLoader.INSTANCE.addDomain(Tags.MOD_ID);
		TGFX.loadFXList();
		if (!Loader.isModLoaded("albedo")){
			this.lightPulsesEnabled=false;
		} else {
			this.activeLightPulses=new LinkedList<>();
		}
		
		//Register Armor models
		registerArmorModel(TGArmors.ARMORMODEL_STEAM_ARMOR_0, new ModelSteamArmor(0));
		registerArmorModel(TGArmors.ARMORMODEL_STEAM_ARMOR_1, new ModelSteamArmor(1));
		registerArmorModel(TGArmors.ARMORMODEL_POWER_ARMOR_0, new ModelT3PowerArmor(0));
		registerArmorModel(TGArmors.ARMORMODEL_POWER_ARMOR_1, new ModelT3PowerArmor(1));
		registerArmorModel(TGArmors.ARMORMODEL_EXO_SUIT_0, new ModelExoSuit(0,1.0f));
		registerArmorModel(TGArmors.ARMORMODEL_EXO_SUIT_1, new ModelExoSuit(1,0.5f));
		registerArmorModel(TGArmors.ARMORMODEL_EXO_SUIT_2, new ModelExoSuit(0,0.75f));
		registerArmorModel(TGArmors.ARMORMODEL_BERET_0, new ModelBeret());
		registerArmorModel(TGArmors.ARMORMODEL_COAT_0, new ModelArmorCoat(0,1.0f));
		registerArmorModel(TGArmors.ARMORMODEL_COAT_1, new ModelArmorCoat(1,0.51f));
		registerArmorModel(TGArmors.ARMORMODEL_COAT_2, new ModelArmorCoat(2,0.49f));
		registerArmorModel(TGArmors.ARMORMODEL_COAT_3, new ModelArmorCoat(3,0.75f));
		registerArmorModel(TGArmors.ARMORMODEL_STEAM_ARMOR_2, new ModelSteamArmor(0,0.01f));
		registerArmorModel(TGArmors.ARMORMODEL_POWER_ARMOR_2, new ModelT3PowerArmor(0,0.01f));
		
		registerArmorModel(TGArmors.ARMORMODEL_POWER_ARMOR_MK2_0, new ModelT4PowerArmorMk2(0));
		registerArmorModel(TGArmors.ARMORMODEL_POWER_ARMOR_MK2_1, new ModelT4PowerArmorMk2(1));
		registerArmorModel(TGArmors.ARMORMODEL_POWER_ARMOR_MK2_2, new ModelT4PowerArmorMk2(0,0.01f));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		IScreenEffect.preloadAll();

		if(TGConfig.misc.debug) {
			Keybinds.init();
		}
		TGKeybinds.init();
		
		MinecraftForge.EVENT_BUS.register(new TGGuiEvents());
		MinecraftForge.EVENT_BUS.register(new TGKeybinds());
	
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		
		RenderPlayer slim = skinMap.get("slim");
		//slim.addLayer(new TGLayerRendererer(slim,true));
		insertLayerAfterArmor(slim, new TGLayerRendererer(slim));
		
		RenderPlayer def = skinMap.get("default");
		//def.addLayer(new TGLayerRendererer(def,false));
		insertLayerAfterArmor(def, new TGLayerRendererer(def));
		
		ClientRegistry.bindTileEntitySpecialRenderer(AmmoPressTileEnt.class,
				new RenderMachine(new ModelAmmoPress(), new ResourceLocation(Tags.MOD_ID, "textures/blocks/ammopress.png")));
		
		ClientRegistry.bindTileEntitySpecialRenderer(MetalPressTileEnt.class,
				new RenderMachine(new ModelMetalPress(), new ResourceLocation(Tags.MOD_ID, "textures/blocks/metalpress.png")));
		
		ClientRegistry.bindTileEntitySpecialRenderer(ChemLabTileEnt.class,
				new RenderMachine(new ModelChemLab(), new ResourceLocation(Tags.MOD_ID, "textures/blocks/chemlab.png")));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TurretTileEnt.class,
				new RenderTurret(new ModelTurretBase()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(FabricatorTileEntMaster.class, new RenderFabricator());
		
		ClientRegistry.bindTileEntitySpecialRenderer(ChargingStationTileEnt.class, new RenderChargingStation());
		
		ClientRegistry.bindTileEntitySpecialRenderer(ReactionChamberTileEntMaster.class, new RenderReactionChamber());
		
		ClientRegistry.bindTileEntitySpecialRenderer(DungeonScannerTileEnt.class, new RenderDungeonScanner());
		ClientRegistry.bindTileEntitySpecialRenderer(DungeonGeneratorTileEnt.class, new RenderDungeonGenerator());
		
		ClientRegistry.bindTileEntitySpecialRenderer(Door3x3TileEntity.class, new RenderDoor3x3Fast());
		
		ClientRegistry.bindTileEntitySpecialRenderer(OreDrillTileEntMaster.class, new RenderOreDrill());
		
		ClientRegistry.bindTileEntitySpecialRenderer(BlastFurnaceTileEnt.class, new RenderBlastfurnace());
		ClientRegistry.bindTileEntitySpecialRenderer(GrinderTileEnt.class, new RenderGrinder());
		
		this.initGuiHandler();
		
		//Galacticraft API for TABS
		if(TabRegistry.getTabList().isEmpty()) {
			MinecraftForge.EVENT_BUS.register(new TabRegistry());
			TabRegistry.registerTab(new InventoryTabVanilla());
		}
		TabRegistry.registerTab(new TGPlayerTab());
	}
	
	protected void initGuiHandler() {
		guihandler.addEntry(CamoBenchTileEnt.class, CamoBenchGui::new, CamoBenchContainer::new);
		guihandler.addEntry(RepairBenchTileEnt.class, RepairBenchGui::new, RepairBenchContainer::new);
		guihandler.addEntry(AmmoPressTileEnt.class, AmmoPressGui::new, AmmoPressContainer::new);
		guihandler.addEntry(MetalPressTileEnt.class, MetalPressGui::new, MetalPressContainer::new);
		guihandler.addEntry(ChemLabTileEnt.class, ChemLabGui::new, ChemLabContainer::new);
		guihandler.addEntry(TurretTileEnt.class, TurretGui::new, TurretContainer::new);
		guihandler.addEntry(FabricatorTileEntMaster.class, FabricatorGui::new, FabricatorContainer::new);
		guihandler.addEntry(ChargingStationTileEnt.class, ChargingStationGui::new, ChargingStationContainer::new);
		guihandler.addEntry(ReactionChamberTileEntMaster.class, ReactionChamberGui::new, ReactionChamberContainer::new);
		guihandler.addEntry(DungeonScannerTileEnt.class, DungeonScannerGui::new, DungeonScannerContainer::new);
		guihandler.addEntry(DungeonGeneratorTileEnt.class, DungeonGeneratorGui::new, DungeonGeneratorContainer::new);
		guihandler.addEntry(Door3x3TileEntity.class, Door3x3Gui::new, Door3x3Container::new);
		guihandler.addEntry(ExplosiveChargeTileEnt.class, ExplosiveChargeGui::new, ExplosiveChargeContainer::new);
		guihandler.addEntry(ExplosiveChargeAdvTileEnt.class, ExplosiveChargeGui::new, ExplosiveChargeContainer::new);
		guihandler.addEntry(OreDrillTileEntMaster.class, OreDrillGui::new, OreDrillContainer::new);
		guihandler.addEntry(BlastFurnaceTileEnt.class, BlastFurnaceGui::new, BlastFurnaceContainer::new);
		guihandler.addEntry(GrinderTileEnt.class, GrinderGui::new, GrinderContainer::new);
		guihandler.addEntry(UpgradeBenchTileEnt.class, UpgradeBenchGui::new, UpgradeBenchContainer::new);
	}
	
	@Override
	public GuiHandlerRegister getGuihandlers() {
		return guihandler;
	}

    @SuppressWarnings("unchecked")
    private void insertLayerAfterArmor(RenderPlayer r, LayerRenderer<? super AbstractClientPlayer> tglayer) {
        List<LayerRenderer<? super AbstractClientPlayer>> layers = (List<LayerRenderer<? super AbstractClientPlayer>>) (List<?>) r.layerRenderers;

        for (int i = 0; i < layers.size(); i++) {
            LayerRenderer<? super AbstractClientPlayer> layer = layers.get(i);
            if (layer instanceof LayerBipedArmor) {
                layers.add(i + 1, tglayer);
                break;
            }
        }
    }


	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		DeathEffect.postInit();
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ignoreBlockstatesForCustomModels = true;
		TGItems.initModels();
		TGArmors.initModels();
		TGuns.initModels();
		TGEntities.initModels();
		TGBlocks.initModels();
		TGFluids.initModels();

		for (IGenericBlock b : TGBlocks.BLOCKLIST) {
			if (b instanceof BlockTGStairs || b instanceof BlockTGSlab || b instanceof BlockTGDoubleSlab || b instanceof BlockTGFence) {
				ModelLoader.setCustomStateMapper((Block) b, CUSTOM_STATE_MAPPER);
			}
		}
	}

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
		for (IGenericBlock b : TGBlocks.BLOCKLIST) {
			if (b instanceof BlockTGStairs) {
				for (String tex : ((BlockTGStairs) b).getTextures()) {
					if (tex != null) event.getMap().registerSprite(new ResourceLocation(Tags.MOD_ID, "blocks/" + tex));
				}
			} else if (b instanceof BlockTGSlab) {
				for (String tex : ((BlockTGSlab) b).getTextures()) {
					if (tex != null) event.getMap().registerSprite(new ResourceLocation(Tags.MOD_ID, "blocks/" + tex));
				}
				for (String tex : ((BlockTGSlab) b).getSideTextures()) {
					if (tex != null) event.getMap().registerSprite(new ResourceLocation(Tags.MOD_ID, "blocks/" + tex));
				}
			} else if (b instanceof BlockTGFence) {
				for (String tex : ((BlockTGFence) b).getSideTextures()) {
					if (tex != null) event.getMap().registerSprite(new ResourceLocation(Tags.MOD_ID, "blocks/" + tex));
				}
				for (String tex : ((BlockTGFence) b).getTopTextures()) {
					if (tex != null) event.getMap().registerSprite(new ResourceLocation(Tags.MOD_ID, "blocks/" + tex));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		ignoreBlockstatesForCustomModels = false;
		IModel stairs, stairsOuter, stairsInner, slabHalf, slabUpper, cubeBottomTop, fenceCenter, fenceSideN, fenceSideE, fenceSideS, fenceSideW, fenceCornerNE, fenceCornerES, fenceCornerSW, fenceCornerWN, fenceInventory;
		try {
			stairs = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/stairs"));
			stairsOuter = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/outer_stairs"));
			stairsInner = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/inner_stairs"));
			slabHalf = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/half_slab"));
			slabUpper = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/upper_slab"));
			cubeBottomTop = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/cube_bottom_top"));
			// Th3_Sl1ze: as far as I hate having different models for each side, they're necessary here. rotating a single side model will cause
			// fucked up connected textures at the top and bottom sides
			fenceCenter = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_center"));
			fenceSideN = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_side_north"));
			fenceSideE = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_side_east"));
			fenceSideS = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_side_south"));
			fenceSideW = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_side_west"));

			fenceCornerNE = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_corner_ne"));
			fenceCornerES = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_corner_es"));
			fenceCornerSW = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_corner_sw"));
			fenceCornerWN = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_corner_wn"));

			fenceInventory = ModelLoaderRegistry.getModel(new ResourceLocation(Tags.MOD_ID, "block/fence_inventory"));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		for (IGenericBlock b : TGBlocks.BLOCKLIST) {
			if (b instanceof BlockTGStairs block) {
				for (IBlockState state : block.getBlockState().getValidStates()) {
					boolean type2 = state.getValue(BlockTGStairs.TYPE2);
					String texture = block.getTextures()[type2 ? 1 : 0];
					if (texture == null) continue;

					EnumFacing facing = state.getValue(BlockTGStairs.FACING);
					BlockStairs.EnumHalf half = state.getValue(BlockTGStairs.HALF);
					BlockStairs.EnumShape shape = state.getValue(BlockTGStairs.SHAPE);

					IModel baseModel = stairs;
					if (shape == BlockStairs.EnumShape.OUTER_LEFT || shape == BlockStairs.EnumShape.OUTER_RIGHT) baseModel = stairsOuter;
					else if (shape == BlockStairs.EnumShape.INNER_LEFT || shape == BlockStairs.EnumShape.INNER_RIGHT) baseModel = stairsInner;

					int x = (half == BlockStairs.EnumHalf.TOP) ? 180 : 0;
					int y = getStairsYRotation(facing, shape, half);

					ModelRotation rot = ModelRotation.getModelRotation(x, y);
					IBakedModel baked = bakeModel(baseModel, rot, true, texture, null);
					event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), BlockUtils.getBlockStateVariantString(state)), baked);
				}
			} else if (b instanceof BlockTGSlab block) {
				for (IBlockState state : block.getBlockState().getValidStates()) {
					int variant = state.getValue(BlockTGSlab.VARIANT);
					String texture = block.getTextures()[variant];
					if (texture == null) continue;
					String sideTexture = block.getSideTextures()[variant];

					BlockSlab.EnumBlockHalf half = state.getValue(BlockTGSlab.HALF);
					IModel baseModel = (half == BlockSlab.EnumBlockHalf.TOP) ? slabUpper : slabHalf;

					IBakedModel baked = bakeModel(baseModel, ModelRotation.X0_Y0, true, texture, sideTexture);
					event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), BlockUtils.getBlockStateVariantString(state)), baked);
				}
			} else if (b instanceof BlockTGDoubleSlab block) {
				for (IBlockState state : block.getBlockState().getValidStates()) {
					int variant = state.getValue(BlockTGDoubleSlab.VARIANT);
					String texture = block.getSingleSlab().getTextures()[variant];
					if (texture == null) continue;
					String sideTexture = block.getSingleSlab().getSideTextures()[variant];

					IBakedModel baked = event.getModelRegistry().getObject(new ModelResourceLocation(Tags.MOD_ID + ":" + texture + (sideTexture != null ? "_side_" + sideTexture : ""), "normal"));
					if (baked == null) {
						baked = bakeModel(cubeBottomTop, ModelRotation.X0_Y0, true, texture, sideTexture);
					}
					event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), BlockUtils.getBlockStateVariantString(state)), baked);
				}
			} else if (b instanceof BlockTGFence block) {
				for (int variant = 0; variant < block.numVariants; variant++) {
					String sideTex = block.getSideTextures()[variant];
					String topTex = block.getTopTextures()[variant];
					if (sideTex == null || topTex == null) continue;

					IBakedModel centerBaked = bakeFencePart(fenceCenter, ModelRotation.X0_Y0, false, sideTex, topTex);
					IBakedModel northBaked = bakeFencePart(fenceSideN, ModelRotation.X0_Y0, false, sideTex, topTex);
					IBakedModel eastBaked = bakeFencePart(fenceSideE, ModelRotation.X0_Y0, false, sideTex, topTex);
					IBakedModel southBaked = bakeFencePart(fenceSideS, ModelRotation.X0_Y0, false, sideTex, topTex);
					IBakedModel westBaked = bakeFencePart(fenceSideW, ModelRotation.X0_Y0, false, sideTex, topTex);

					IBakedModel cornerNEBaked = bakeFencePart(fenceCornerNE, ModelRotation.X0_Y0, false, sideTex, topTex);
					IBakedModel cornerESBaked = bakeFencePart(fenceCornerES, ModelRotation.X0_Y0, false, sideTex, topTex);
					IBakedModel cornerSWBaked = bakeFencePart(fenceCornerSW, ModelRotation.X0_Y0, false, sideTex, topTex);
					IBakedModel cornerWNBaked = bakeFencePart(fenceCornerWN, ModelRotation.X0_Y0, false, sideTex, topTex);

					IBakedModel inventoryBaked = bakeFencePart(fenceInventory, ModelRotation.X0_Y0, false, sideTex, topTex);
					event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "inventory_" + variant), inventoryBaked);

					MultipartBakedModel.Builder builder = new MultipartBakedModel.Builder();
					builder.putModel(s -> true, centerBaked);
					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.NORTH), northBaked);
					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.EAST), eastBaked);
					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.SOUTH), southBaked);
					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.WEST), westBaked);

					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.CORNER_NE), cornerNEBaked);
					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.CORNER_ES), cornerESBaked);
					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.CORNER_SW), cornerSWBaked);
					builder.putModel(s -> s != null && s.getBlock() == block && s.getValue(BlockTGFence.CORNER_WN), cornerWNBaked);

					IBakedModel combined = builder.makeMultipartModel();

					for (IBlockState state : block.getBlockState().getValidStates()) {
						if (state.getValue(BlockTGFence.VARIANT) == variant) {
							event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), BlockUtils.getBlockStateVariantString(state)), combined);
						}
					}
				}
			}
		}
	}

	private static IBakedModel bakeFencePart(IModel model, ModelRotation rot, boolean uvlock, String sideTex, String topTex) {
		String sidePath = techguns.Tags.MOD_ID + ":blocks/" + sideTex;
		String topPath = techguns.Tags.MOD_ID + ":blocks/" + topTex;
		ImmutableMap<String, String> dict = ImmutableMap.<String, String>builder()
				.put("0", sidePath)
				.put("1", topPath)
				.put("particle", sidePath)
				.build();
		IModel retextured = model.retexture(dict).uvlock(uvlock);
		IModelState state = new Variant(new net.minecraft.util.ResourceLocation("minecraft:block/fence"), rot, uvlock, 1).getState();
		IBakedModel baked = retextured.bake(state, DefaultVertexFormats.BLOCK, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		return forceCTMWrap(retextured, baked);
	}

	private static IBakedModel bakeModel(IModel model, ModelRotation rot, boolean uvlock, String textureName, String sideTextureName) {
		String texPath = techguns.Tags.MOD_ID + ":blocks/" + textureName;
		String sideTexPath = techguns.Tags.MOD_ID + ":blocks/" + (sideTextureName != null ? sideTextureName : textureName);
		ImmutableMap<String, String> dict = ImmutableMap.<String, String>builder()
				.put("bottom", texPath)
				.put("top", texPath)
				.put("side", sideTexPath)
				.put("all", texPath)
				.put("particle", texPath)
				.build();
		IModel retextured = model.retexture(dict).uvlock(uvlock);
		IModelState state = new Variant(new ResourceLocation("minecraft:block/stairs"), rot, uvlock, 1).getState();
		IBakedModel baked = retextured.bake(state, DefaultVertexFormats.BLOCK, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
		return forceCTMWrap(retextured, baked);
	}

	// Th3_Sl1ze: for anyone wondering, this enables CTM support for models that AREN'T done in json
	// by default, CTM doesn't really like dealing with hardcoded models
	private static IBakedModel forceCTMWrap(IModel iModel, IBakedModel bakedModel) {
		if (Loader.isModLoaded("ctm")) {
			try {
				Class<?> cls = Class.forName("team.chisel.ctm.client.util.TextureMetadataHandler");
				Object instance = cls.getField("INSTANCE").get(null);
				Method wrapMethod = cls.getDeclaredMethod("wrap", IModel.class, IBakedModel.class);
				wrapMethod.setAccessible(true);
				IBakedModel wrapped = (IBakedModel) wrapMethod.invoke(instance, iModel, bakedModel);
				if (wrapped != null) return wrapped;
			} catch (Throwable ignored) {
			}
		}
		return bakedModel;
	}

	private static int getStairsYRotation(EnumFacing facing, BlockStairs.EnumShape shape, BlockStairs.EnumHalf half) {
		boolean top = (half == BlockStairs.EnumHalf.TOP);
		switch (shape) {
			case STRAIGHT:
				switch (facing) {
					case EAST: return 0;
					case WEST: return 180;
					case SOUTH: return 90;
					case NORTH: return 270;
				}
				break;
			case OUTER_RIGHT:
				if (top) {
					switch (facing) {
						case EAST: return 90;
						case WEST: return 270;
						case SOUTH: return 180;
						case NORTH: return 0;
					}
				} else {
					switch (facing) {
						case EAST: return 0;
						case WEST: return 180;
						case SOUTH: return 90;
						case NORTH: return 270;
					}
				}
				break;
			case OUTER_LEFT:
				if (top) {
					switch (facing) {
						case EAST: return 0;
						case WEST: return 180;
						case SOUTH: return 90;
						case NORTH: return 270;
					}
				} else {
					switch (facing) {
						case EAST: return 270;
						case WEST: return 90;
						case SOUTH: return 0;
						case NORTH: return 180;
					}
				}
				break;
			case INNER_RIGHT:
				if (top) {
					switch (facing) {
						case EAST: return 90;
						case WEST: return 270;
						case SOUTH: return 180;
						case NORTH: return 0;
					}
				} else {
					switch (facing) {
						case EAST: return 0;
						case WEST: return 180;
						case SOUTH: return 90;
						case NORTH: return 270;
					}
				}
				break;
			case INNER_LEFT:
				if (top) {
					switch (facing) {
						case EAST: return 0;
						case WEST: return 180;
						case SOUTH: return 90;
						case NORTH: return 270;
					}
				} else {
					switch (facing) {
						case EAST: return 270;
						case WEST: return 90;
						case SOUTH: return 0;
						case NORTH: return 180;
					}
				}
				break;
		}
		return 0;
	}

	@Override
	public void registerItemRenderers() {
		RenderGenericSharedItem3D sharedRenderer = new RenderGenericSharedItem3D();
		
		float[][] m4magTranslations = {
				{0,0.25f,0}, //First Person
				{0f,0.25f,0.05f}, //Third Person
				{0.1f,0.25f,0}, //GUI
				{0,-0.1f,0}, //Ground
				{0,0,-0.05f} //frame
		};
		
		float[][] as50magTranslations = {
				{0,0f,0}, //First Person
				{0f,0f,0f}, //Third Person
				{0f,0.05f,0}, //GUI
				{0,-0.1f,0}, //Ground
				{0,0,0f} //frame
		};
		
		float[][] lmgmagTranslations = {
				{0,0.35f,0}, //First Person
				{0f,0.15f,0.0f}, //Third Person
				{0.1f,0.25f,0}, //GUI
				{0,0f,0}, //Ground
				{0,0.25f,-0.05f} //frame
		};
		
		float[][] rocketTranslations = {
				{0,0f,0f}, //First Person
				{0f,-0.1f,0.02f}, //Third Person
				{0.0f,0.0f,0}, //GUI
				{0.0f,0.0f,0}, //Ground
				{0,0,0f} //frame
		};
		/*
		  DEFAULT
		 
		.setTransformTranslations(new float[][]{
			{0f,0f,0f}, //First Person
			{0f,0f,0f}, //Third Person
			{0f,0f,0f}, //GUI
			{0f,0f,0f}, //Ground
			{0f,0f,-0.05f} //frame
		})
		
		*/
		sharedRenderer.addRenderForType("assaultriflemagazine", new RenderItemBase(new ModelARMagazine(false), new ResourceLocation(Tags.MOD_ID,"textures/guns/ar_mag.png")).setBaseScale(1.25f).setGUIScale(0.85f).setBaseTranslation(0, -0.2f, 0).setTransformTranslations(m4magTranslations));
		sharedRenderer.addRenderForType("assaultriflemagazineempty", new RenderItemBase(new ModelARMagazine(true), new ResourceLocation(Tags.MOD_ID,"textures/guns/ar_mag.png")).setBaseScale(1.25f).setGUIScale(0.85f).setBaseTranslation(0, -0.2f, 0).setTransformTranslations(m4magTranslations));
		
		sharedRenderer.addRenderForType("assaultriflemagazine_incendiary", new RenderItemBase(new ModelARMagazine(false), new ResourceLocation(Tags.MOD_ID,"textures/guns/ar_mag_inc.png")).setBaseScale(1.25f).setGUIScale(0.85f).setBaseTranslation(0, -0.2f, 0).setTransformTranslations(m4magTranslations));
		
		
		sharedRenderer.addRenderForType("lmgmagazine", new RenderItemLMGMag(new ModelLmgMag(false), new ResourceLocation(Tags.MOD_ID,"textures/guns/lmg_mag.png")).setBaseScale(1.25f).setGUIScale(0.75f).setBaseTranslation(0, 0f, 0.2f).setTransformTranslations(lmgmagTranslations));
		sharedRenderer.addRenderForType("lmgmagazineempty", new RenderItemLMGMag(new ModelLmgMag(true), new ResourceLocation(Tags.MOD_ID,"textures/guns/lmg_mag.png")).setBaseScale(1.25f).setGUIScale(0.75f).setBaseTranslation(0, 0f, 0.2f).setTransformTranslations(lmgmagTranslations));	
		
		sharedRenderer.addRenderForType("lmgmagazine_incendiary", new RenderItemLMGMag(new ModelLmgMag(false), new ResourceLocation(Tags.MOD_ID,"textures/guns/lmg_mag_inc.png")).setBaseScale(1.25f).setGUIScale(0.75f).setBaseTranslation(0, 0f, 0.2f).setTransformTranslations(lmgmagTranslations));
		
		
		sharedRenderer.addRenderForType("as50magazine", new RenderItemBase(new ModelAS50Mag(false), new ResourceLocation(Tags.MOD_ID,"textures/guns/as50_mag.png")).setBaseScale(1.5f).setGUIScale(0.75f).setBaseTranslation(0.0325f, -0.2f, 0.33f).setTransformTranslations(as50magTranslations));
		sharedRenderer.addRenderForType("as50magazineempty", new RenderItemBase(new ModelAS50Mag(true), new ResourceLocation(Tags.MOD_ID,"textures/guns/as50_mag.png")).setBaseScale(1.5f).setGUIScale(0.75f).setBaseTranslation(0.0325f, -0.2f, 0.33f).setTransformTranslations(as50magTranslations));
		
		sharedRenderer.addRenderForType("as50magazine_incendiary", new RenderItemBase(new ModelAS50Mag(false), new ResourceLocation(Tags.MOD_ID,"textures/guns/as50_mag_inc.png")).setBaseScale(1.5f).setGUIScale(0.75f).setBaseTranslation(0.0325f, -0.2f, 0.33f).setTransformTranslations(as50magTranslations));
		sharedRenderer.addRenderForType("as50magazine_explosive", new RenderItemBase(new ModelAS50Mag(false), new ResourceLocation(Tags.MOD_ID,"textures/guns/as50_mag_exp.png")).setBaseScale(1.5f).setGUIScale(0.75f).setBaseTranslation(0.0325f, -0.2f, 0.33f).setTransformTranslations(as50magTranslations));
		
		
		sharedRenderer.addRenderForType("rocket", new RenderItemBaseRocketItem(new ModelRocket(), new ResourceLocation(Tags.MOD_ID,"textures/guns/rocket.png")).setBaseScale(1.5f).setGUIScale(0.5f).setBaseTranslation(0, 0, 0.1f).setTransformTranslations(rocketTranslations).setFirstPersonScale(0.35f));
		
		sharedRenderer.addRenderForType("rocket_nuke", new RenderItemBaseRocketItem(new ModelRocket(), new ResourceLocation(Tags.MOD_ID,"textures/guns/rocket_nuke.png")).setBaseScale(1.5f).setGUIScale(0.5f).setBaseTranslation(0, 0, 0.1f).setTransformTranslations(rocketTranslations).setFirstPersonScale(0.35f));
		
		sharedRenderer.addRenderForType("rocket_high_velocity", new RenderItemBaseRocketItem(new ModelRocket(), new ResourceLocation(Tags.MOD_ID,"textures/guns/rocket_hv.png")).setBaseScale(1.5f).setGUIScale(0.5f).setBaseTranslation(0, 0, 0.1f).setTransformTranslations(rocketTranslations).setFirstPersonScale(0.35f));
		
		ItemRenderHack.registerItemRenderer(TGItems.SHARED_ITEM, sharedRenderer);
		
		ItemRenderHack.registerItemRenderer(TGuns.m4,new RenderGunBase(new ModelM4(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0)
				.setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.18f, -1.29f, 0.75f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0f,-0.05f}, //First Person
					{0f,0.01f,-0.1f}, //Third Person
					{0f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.13f, -1f).setMuzzleFlashJitter(0.02f, 0.02f, 5.0f, 0.1f));
		
		ItemRenderHack.registerItemRenderer(TGuns.ak47,new RenderGunBase(new ModelAK(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0).setBaseScale(0.75f)
				.setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.18f, -1.36f, 0.8f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0.06f,-0.02f}, //First Person
					{0f,0f,-0.08f}, //Third Person
					{0.06f,-0.01f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.08f, -1.02f).setMuzzleFlashJitter(0.02f, 0.02f, 5.0f, 0.1f));
		
		ItemRenderHack.registerItemRenderer(TGuns.lmg,new RenderGunBase(new ModelLMG(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0)
				.setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.21f, -1.5f, 0.78f,0).setMuzzleFXPos3P(0.13f, -0.98f).setRecoilAnim(GunAnimation.genericRecoil, 0.01f, 2.0f).setTransformTranslations(new float[][]{
					{0f,0.02f,-0.09f}, //First Person
					{0f,0f,-0.06f}, //Third Person
					{0.05f,-0.03f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.14f, -1.02f).setMuzzleFlashJitter(0.03f, 0.03f, 5.0f, 0.1f));
		
		ItemRenderHack.registerItemRenderer(TGuns.handcannon,new RenderGunBase90(new ModelHandgun(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE-0.1f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.16f, -0.75f, 0.9f,0).setReloadAnim(GunAnimation.breechReload, -0.15f, 55.0f).setReloadAnim3p(GunAnimation.breechReload, 0f, 55.0f).setTransformTranslations(new float[][]{
					{0,0.03f,-0.12f}, //First Person
					{0.0f,-0.05f,-0.09f}, //Third Person
					{0.0f,0.0f,0}, //GUI
					{0.0f,0.0f,0}, //Ground
					{0,0,0f} //frame
				}).setMuzzleFXPos3P(0.03f, -0.59f).setRecoilAnim(GunAnimation.genericRecoil, 0.2f, 25.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.sawedoff,new RenderGunBase90(new ModelSawedOff(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE-0.1f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.16f, -0.75f, 1.05f,0).setReloadAnim(GunAnimation.breechReload, -0.15f, 55.0f).setReloadAnim3p(GunAnimation.breechReload, 0f, 55.0f).setTransformTranslations(new float[][]{
					{0f,0f,0f}, //First Person
					{0f,-0.04f,0f}, //Third Person
					{0f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.06f, -0.49f).setRecoilAnim(GunAnimation.genericRecoil, 0.2f, 20.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.thompson,new RenderGunBase90(new ModelThompson(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE*0.5f-0.1f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.14f, -0.75f, 0.55f,0).setMuzzleFXPos3P(0.1f, -0.59f).setMuzzleFlashJitter(0.02f, 0.02f, 5.0f, 0.1f));
		
		ItemRenderHack.registerItemRenderer(TGuns.boltaction,new RenderGunBase90(new ModelBoltaction(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE-0.1f).setBaseScale(1.35f)
				.setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.21f, -1.48f, 0.9f,0).setScope(ScreenEffect.sniperScope).setTransformTranslations(new float[][]{
					{0f,-0.02f,-0.09f}, //First Person
					{0f,-0.04f,-0.11f}, //Third Person
					{0.1f,-0.08f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.025f} //frame
				}).setMuzzleFXPos3P(0.12f, -1.13f).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.20f, 2.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.biogun,new RenderGunBase90(new ModelBiogun(),1).setBaseTranslation(0.35f, -0.2f, RenderItemBase.SCALE-0.1f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleGreenFlare, 0, 0.23f, -0.51f, 0.55f,0).setTransformTranslations(new float[][]{
					{0f,0.16f,0.05f}, //First Person
					{0f,0.07f,-0.05f}, //Third Person
					{0f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.1f, -0.51f).setChargeTranslationAmount(0.05f));
	
		ItemRenderHack.registerItemRenderer(TGuns.flamethrower,new RenderGunFlamethrower(new ModelFlamethrower(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE-0.1f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.FlamethrowerMuzzleFlash, 0, 0.16f, -0.9f, 0.45f,0).setTransformTranslations(new float[][]{
					{0f,0f,0f}, //First Person
					{0f,0f,-0.08f}, //Third Person
					{0.05f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.1f, -0.63f).setRecoilAnim(GunAnimation.swayRecoil, 0.025f, 2.5f));
	
		
		ItemRenderHack.registerItemRenderer(TGuns.pistol,new RenderGunBase(new ModelPistol(),2).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.3f, -0.4f)
				.setBaseScale(1.2f).setGUIScale(0.9f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0.03f, 0.2f, -0.5f, 0.55f,-0.03f).setTransformTranslations(new float[][]{
					{0,0.09f,-0.02f}, //First Person
					{0.0f,-0.03f,0.0f}, //Third Person
					{0.02f,-0.08f,0}, //GUI
					{0.02f,-0.08f,0}, //Ground
					{0,0,0f} //frame
				}).setRecoilAnim(GunAnimation.genericRecoil, 0.025f, 12.0f).setMuzzleFlashJitter(0.01f, 0.01f, 5.0f, 0.05f).setMuzzleFXPos3P(0.07f, -0.26f));
		
		ItemRenderHack.registerItemRenderer(TGuns.rocketlauncher,new RenderRocketLauncher(new ModelRocketLauncher(),2).setBaseTranslation(-0.4f, -0.2f, -RenderItemBase.SCALE*0.5f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.39f, -0.6f, 0.87f, 0).setTransformTranslations(new float[][]{
					{-0.13f,0.3f,0.32f}, //First Person
					{0,0.02f,0.07f}, //Third Person
					{-0.06f,0.0f,0.0f}, //GUI
					{0.0f,0.0f,0}, //Ground
					{0,0,0f} //frame
				}).setMuzzleFXPos3P(0.09f, -0.26f));
		
		ItemRenderHack.registerItemRenderer(TGuns.minigun,new RenderGunBase90(new ModelMinigun(),2).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE*0.5f).setBaseScale(1.2f)
				.setGUIScale(0.3f).setMuzzleFx(ScreenEffect.muzzleFlash_minigun, -0.04f, 0.05f, -1.25f, 0.8f,0.04f).setTransformTranslations(new float[][]{
					{0f,-0.16f,0.1f}, //First Person
					{0f,-0.53f,0.2f}, //Third Person
					{0.12f,-0.02f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(-0.38f, -0.78f).setRecoilAnim(GunAnimation.genericRecoil, 0.05f, 3.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.combatshotgun,new RenderGunBase90(new ModelCombatShotgun(),2).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE-0.1f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.21f, -0.91f, 0.75f,0).setTransformTranslations(new float[][]{
					{0f,0.03f,0f}, //First Person
					{0f,-0.01f,-0.10f}, //Third Person
					{0.05f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.12f, -0.81f).setRecoilAnim(GunAnimation.genericRecoil, 0.3f, 15.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.revolver,new RenderGunBase90(new ModelRevolver(),1).setBaseTranslation(-0.35f, -0.2f, RenderItemBase.SCALE*0.5f-0.1f)
				.setBaseScale(0.75f).setGUIScale(0.75f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.25f, -0.41f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.14f,0.01f}, //First Person
					{0f,0.0f,0.0f}, //Third Person
					{0.05f,0.01f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,0f} //frame
				}).setMuzzleFXPos3P(0.09f, -0.30f).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 10.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.grimreaper,new RenderGunBase90(new ModelGrimReaper(),1).setBaseTranslation(0.3f, -0.2f, RenderItemBase.SCALE*0.5f)
				.setGUIScale(0.3f).setChargeTranslationAmount(0.025f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.39f, -0.61f, 0.75f,0).setTransformTranslations(new float[][]{
					{0f,0.25f,0.18f}, //First Person
					{0,0.13f,0.1f}, //Third Person
					{-0.02f,0.01f,0.0f}, //GUI
					{0.0f,0.1f,0}, //Ground
					{0,0,0f} //frame
				}).setMuzzleFXPos3P(0.24f, -0.56f).setBaseScale(1.25f).setFirstPersonScale(0.4f).setGroundAndFrameScale(0.35f));
		
		ItemRenderHack.registerItemRenderer(TGuns.pdw,new RenderGunBase90(new ModelPDW(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE*1.5f-0.1f)
				.setGUIScale(0.55f).setMuzzleFx(ScreenEffect.muzzleFlash_blue, 0, 0.13f, -0.58f, 0.55f ,0).setTransformTranslations(new float[][]{
					{0.0f, 0.09f, -0.04f}, //First Person
					{0f, 0.06f, -0.02f}, //Third Person
					{0f, 0f, 0f}, //GUI
					{0f, 0f, 0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.11f, -0.41f).setMuzzleFlashJitter(0.01f, 0.01f, 5.0f, 0.1f).setRecoilAnim(GunAnimation.genericRecoil,  0.06f, 4.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.as50,new RenderGunBase(new ModelAS50(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0)
				.setBaseScale(0.85f).setGUIScale(0.30f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.29f, -1.82f, 1.15f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.25f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0.06f,-0.1f}, //First Person
					{0f,0.0f,-0.05f}, //Third Person
					{0.13f,-0.09f,-0.05f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,-0.2f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.17f, -1.29f).setScope(ScreenEffect.sniperScope).setRecoilAnim(GunAnimation.genericRecoil, 0.25f, 5.0f).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.2f, 2.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.m4_infiltrator,new RenderGunBase(new ModelM4Infiltrator(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0)
				.setGUIScale(0.35f)/*.setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.18f, -1.5f, 0.5f,0)*/.setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0f,-0.05f}, //First Person
					{0f,0.01f,-0.1f}, //Third Person
					{0f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.14f, -1.15f).setScope(ScreenEffect.sniperScope).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.05f, 1.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.goldenrevolver,new RenderGunBase90(new ModelGoldenRevolver(),1).setBaseTranslation(-0.35f, -0.2f, RenderItemBase.SCALE*0.5f-0.1f)
				.setBaseScale(0.75f).setGUIScale(0.75f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.25f, -0.41f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.14f,0.01f}, //First Person
					{0f,0.0f,0.0f}, //Third Person
					{0.05f,0.01f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,0f} //frame
				}).setMuzzleFXPos3P(0.09f, -0.30f).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 15.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.pulserifle,new RenderGunBase90(new ModelPulseRifle(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE*1.5f - 0.09f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_blue, 0, 0.22f, -0.76f, 0.6f,0).setTransformTranslations(new float[][]{
					{0f,0.16f,0.01f}, //First Person
					{0f,0.05f,0.08f}, //Third Person
					{0.05f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.1f, -0.50f).setScope(ScreenEffect.techScope,2.125f).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.10f, 1.5f).setRecoilAnim(GunAnimation.pulseRifleRecoil, 0.25f, 10.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.teslagun,new RenderGunBase90(new ModelTeslaGun(),1).setBaseTranslation(0.25f, -0.2f, RenderItemBase.SCALE-0.09f)
				.setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlashLightning, 0, 0.26f, -0.67f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.08f,0.04f}, //First Person
					{0f,0.05f,-0.08f}, //Third Person
					{0.03f,0.01f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.12f, -0.65f).setRecoilAnim(GunAnimation.genericRecoil, 0.2f, 5.0f));	
		
		ItemRenderHack.registerItemRenderer(TGuns.netherblaster,new RenderGunBase(new ModelNetherBlaster(),1).setBaseTranslation(0f, -0.2f, RenderItemBase.SCALE-0.09f)
				.setGUIScale(0.60f).setMuzzleFx(ScreenEffect.muzzleFlashFireball_alpha, 0, 0.29f, -0.33f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.15f,0.04f}, //First Person
					{0f,-0.16f,-0.24f}, //Third Person
					{-0.10f,0.01f,0f}, //GUI
					{0f,0f,-0.11f}, //Ground
					{0.16f,-0.07f,-0.16f} //frame
				}).setMuzzleFXPos3P(-0.06f, -0.45f));
		
		ItemRenderHack.registerItemRenderer(TGuns.lasergun,new RenderGunBase90(new ModelLasergun(),1).setBaseTranslation(0.25f, -0.2f, RenderItemBase.SCALE*0.5f-0.10f)
				.setBaseScale(1.1f).setGUIScale(0.40f).setMuzzleFx(ScreenEffect.muzzleFlashLaser, 0, 0.30f, -1.06f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.07f,0.04f}, //First Person
					{0f,0.02f,0.01f}, //Third Person
					{0.13f,0.01f,0f}, //GUI
					{0f,0f,0.15f}, //Ground
					{-0.18f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.11f, -0.83f).setRecoilAnim(GunAnimation.genericRecoil, 0.2f, 5.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.blasterrifle,new RenderGunBase(new ModelBlasterRifle(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0)
				.setBaseScale(0.9f).setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlashLaser, 0, 0.24f, -0.93f, 0.75f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0.11f,-0.20f}, //First Person
					{0f,0.0f,-0.04f}, //Third Person
					{0f,0f,0.03f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.09f, -0.57f).setScope(ScreenEffect.sniperScope).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.05f, 1.0f));
		
		ResourceLocation[] powerhammer_textures = {new ResourceLocation(Tags.MOD_ID, "textures/guns/powerHammer.png"),new ResourceLocation(Tags.MOD_ID, "textures/guns/powerHammer_obsidian.png"),new ResourceLocation(Tags.MOD_ID, "textures/guns/powerHammer_carbon.png")};
		ItemRenderHack.registerItemRenderer(TGuns.powerhammer,new RenderMiningToolMultiTexture(new ModelPowerHammer(),2, powerhammer_textures).setBaseTranslation(0.15f, -0.2f, RenderItemBase.SCALE-0.09f)
				.setBaseScale(1.25f).setGUIScale(0.45f).setMuzzleFx(null, 0, 0.26f, -0.67f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.18f,0.09f}, //First Person
					{0f,0.04f,0.04f}, //Third Person
					{0.03f,0.01f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{-0.07f,-0.03f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.12f, -0.65f).setChargeTranslationAmount(0.125f));	
		
		ItemRenderHack.registerItemRenderer(TGuns.grenadelauncher,new RenderGunBaseObj(new ModelBaseBakedGrenadeLauncher(
				new ResourceLocation(Tags.MOD_ID,"textures/guns/grenadelauncher.png"), new ModelResourceLocation(TGuns.grenadelauncher.getRegistryName(), "inventory"), new ModelResourceLocation(TGuns.grenadelauncher.getRegistryName()+"_1", "inventory")),1,90.0f)
				.setBaseTranslation(0f, 0f, 0f)
				.setBaseScale(0.125f).setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.22f, -0.63f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.19f,-0.09f}, //First Person
					{0f,0.06f,-0.20f}, //Third Person
					{-0.05f,0.08f,0f}, //GUI
					{0f,0.05f,-0.09f}, //Ground
					{0.11f,0.01f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.07f, -0.61f));	
		
		ItemRenderHack.registerItemRenderer(TGuns.aug,new RenderGunBase(new ModelAUG(),2).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0.1f)
				.setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.19f, -1.45f, 0.75f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0.01f,-0.15f}, //First Person
					{0f,0.0f,-0.01f}, //Third Person
					{0f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0.02f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.12f, -0.87f).setMuzzleFlashJitter(0.02f, 0.02f, 5.0f, 0.1f).setScope(ScreenEffect.sniperScope).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.075f, 1.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.sonicshotgun,new RenderGunBase90(new ModelSonicShotgun(),1).setBaseTranslation(0, -0.2f, RenderItemBase.SCALE*0.5f-0.10f)
				.setBaseScale(1.0f).setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlashSonic, 0, 0.28f, -0.98f, 0.5f,0).setTransformTranslations(new float[][]{
					{0f,0.10f,0.04f}, //First Person
					{0f,-0.01f,-0.03f}, //Third Person
					{0.08f,0.0f,0f}, //GUI
					{0f,0.05f,0.15f}, //Ground
					{-0.18f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.11f, -0.81f));
		
		ItemRenderHack.registerItemRenderer(TGuns.chainsaw,new RenderGunChainsaw(new ModelChainsaw(),2).setBaseTranslation(-0.4f, -0.2f, RenderItemBase.SCALE-0.09f)
				.setBaseScale(0.95f).setGUIScale(0.45f).setTransformTranslations(new float[][]{
					{0f,-0.08f,0.15f}, //First Person
					{0f,-0.5f,0.04f}, //Third Person
					{0.03f,0.01f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{-0.07f,-0.03f,-0.11f} //frame
				}));	
		
		ItemRenderHack.registerItemRenderer(TGuns.scatterbeamrifle,new RenderGunBase(new ModelLasergun2(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0.1f)
				.setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlashLaser, 0, 0.22f, -1.09f, 0.75f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0.04f,-0.05f}, //First Person
					{0f,0.01f,-0.1f}, //Third Person
					{0f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.14f, -0.82f));
		
		ItemRenderHack.registerItemRenderer(TGuns.nucleardeathray,new RenderGunBase90(new ModelNDR(),1).setBaseTranslation(1f, -0.2f, RenderItemBase.SCALE*1.5f-0.09f)
				.setBaseScale(1.2f).setGUIScale(0.32f).setMuzzleFx(ScreenEffect.muzzleFlashNukeBeam, 0f, 0.19f, -1.4f, 0.65f, 0f).setTransformTranslations(new float[][]{
					{0.1f,0.02f,-0.05f}, //First Person
					{-0.01f,0.04f,0.25f}, //Third Person
					{0.11f,-0.08f,0.1f}, //GUI
					{0f,0f,0.15f}, //Ground
					{-0.23f,-0.08f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.15f, -0.77f).setRecoilAnim(GunAnimation.swayRecoil, 0.025f, 0.75f));
		
		ItemRenderHack.registerItemRenderer(TGuns.scar,new RenderGunBase(new ModelScar(),2).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.1f, 0.1f)
				.setGUIScale(0.35f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.23f, -1.48f, 0.78f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.1f, 4.0f).setTransformTranslations(new float[][]{
					{0f,0.04f,-0.15f}, //First Person
					{0f,0.02f,-0.11f}, //Third Person
					{0.05f,0f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0.02f,-0.09f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.14f, -1.04f).setMuzzleFlashJitter(0.02f, 0.02f, 5.0f, 0.1f).setScope(ScreenEffect.sniperScope).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.05f, 2.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.vector,new RenderGunBase(new ModelVector(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.57f, -0.2f)
				.setBaseScale(1.1f).setGUIScale(0.45f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.10f, -0.72f, 0.60f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.05f, 2.0f).setTransformTranslations(new float[][]{
					{0f,-0.17f,0.0f}, //First Person
					{0f,-0.2f,-0.04f}, //Third Person
					{-0.08f,-0.09f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0.05f,-0.17f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.0f, -0.53f).setMuzzleFlashJitter(0.02f, 0.02f, 5.0f, 0.1f));
		
		ItemRenderHack.registerItemRenderer(TGuns.mac10,new RenderGunBase(new ModelMac10(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.45f, -0.3f)
				.setBaseScale(1.2f).setGUIScale(0.55f).setMuzzleFx(ScreenEffect.muzzleFlash_rifle, 0, 0.23f, -0.46f, 0.5f,0).setRecoilAnim(GunAnimation.genericRecoil, 0.06f, 3.0f).setTransformTranslations(new float[][]{
					{0f,0f,-0.05f}, //First Person
					{0f,-0.10f,0.01f}, //Third Person
					{-0.02f,-0.02f,0f}, //GUI
					{0f,0.03f,0f}, //Ground
					{0f,-0.05f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.07f, -0.26f).setMuzzleFlashJitter(0.02f, 0.05f, 5.0f, 0.1f));
		
		ItemRenderHack.registerItemRenderer(TGuns.mibgun,new RenderGunBase(new ModelMibGun(),1).setBaseTranslation(/*RenderItemBase.SCALE*0.5f*/0f, -0.56f, -0.02f)
				.setBaseScale(1.2f).setGUIScale(0.75f).setMuzzleFx(ScreenEffect.muzzleFlashMibGun, 0f, 0.26f, -0.42f, 0.55f,0f).setTransformTranslations(new float[][]{
					{0,0.10f,-0.02f}, //First Person
					{0.0f,-0.04f,-0.01f}, //Third Person
					{0.02f,-0.08f,0}, //GUI
					{0.02f,-0.08f,0}, //Ground
					{-0.04f,-0.09f,0f} //frame
				}).setRecoilAnim(GunAnimation.genericRecoil, 0.025f, 10.0f).setMuzzleFXPos3P(0.06f, -0.31f).setReloadAnim(GunAnimation.breechReload, -0.15f, 55.0f).setReloadAnim3p(GunAnimation.breechReload, 0f, 55.0f));
	
		
		ItemRenderHack.registerItemRenderer(TGuns.gaussrifle,new RenderGunBaseObj(new ModelBaseBaked(new ResourceLocation(Tags.MOD_ID,"textures/guns/gaussrifle.png"), new ModelResourceLocation(TGuns.gaussrifle.getRegistryName(), "inventory")),1,-90f)
				.setBaseTranslation(0.6f, 0f, RenderItemBase.SCALE*0.5f-0.09f)
				.setBaseScale(0.9f).setGUIScale(0.25f).setMuzzleFx(ScreenEffect.muzzleFlashSonic, 0, 0.21f, -1.56f, 1.0f,0).setTransformTranslations(new float[][]{
					{0f,0.12f,-0.1f}, //First Person
					{0f,0.05f,-0.17f}, //Third Person
					{0.f,0.06f,0.06f}, //GUI
					{0f,0f,0.f}, //Ground
					{0f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.09f, -1.26f).setRecoilAnim(GunAnimation.genericRecoil, 0.25f, 7.5f).setScope(ScreenEffect.techScope,2.125f).setScopeRecoilAnim(GunAnimation.scopeRecoil, 0.15f, 1.0f));
	
		ItemRenderHack.registerItemRenderer(TGuns.guidedmissilelauncher,new RenderGunBase90(new ModelGuidedMissileLauncher(),1).setBaseTranslation(-0.4f, -0.2f, RenderItemBase.SCALE*0.5f)
				.setGUIScale(0.35f).setChargeTranslationAmount(0).setMuzzleFx(ScreenEffect.muzzleFlash_gun, 0, 0.39f, -0.6f, 0.87f, 0).setTransformTranslations(new float[][]{
					{-0.13f,0.3f,0.62f}, //First Person
					{0,0.09f,0.28f}, //Third Person
					{0.0f,0.03f,0.0f}, //GUI
					{0.0f,0.0f,0}, //Ground
					{0,0,-0.04f} //frame
				}).setMuzzleFXPos3P(0.09f, -0.26f));
		
		ResourceLocation[] drill_textures = {new ResourceLocation(Tags.MOD_ID, "textures/guns/miningdrill_obsidian.png"), new ResourceLocation(Tags.MOD_ID, "textures/guns/miningdrill_carbon.png")};
		ItemRenderHack.registerItemRenderer(TGuns.miningdrill,new RenderMiningToolMultiTexture(new ModelMiningDrill(),2, drill_textures).setBaseTranslation(0, -0.2f, -RenderItemBase.SCALE*0.5f).setBaseScale(2.0f)
				.setGUIScale(0.35f).setTransformTranslations(new float[][]{
					{0f,-0.03f,0.0f}, //First Person
					{0f,-0.57f,0.08f}, //Third Person
					{0.01f,-0.01f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,-0.08f,-0.05f} //frame
				}).setRecoilAnim(GunAnimation.genericRecoil, 0.05f, 1.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.tfg,new RenderGunBase90(new ModelTFG(),1).setBaseTranslation(-0.46f, -0.38f, RenderItemBase.SCALE-0.125f)
				.setBaseScale(1.20f).setGUIScale(0.30f).setMuzzleFx(ScreenEffect.muzzleFlashTFG, 0.0f, 0.18f, -0.87f, 0.9f,0).setTransformTranslations(new float[][]{
					{0f,-0.03f,0.16f}, //First Person
					{0f,-0.09f,-0.26f}, //Third Person
					{0.04f,-0.04f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{-0.07f,0f,-0.05f} //frame
				}).setMuzzleFXPos3P(0.09f, -1.14f).setChargeTranslationAmount(0.05f).setFirstPersonScale(0.45f));
		
		ItemRenderHack.registerItemRenderer(TGuns.shishkebap, new RenderGunBase(new ModelShishkebap(),1).setBaseTranslation(0, 0,0).setBaseScale(1.0f)
				.setGUIScale(0.3f).setTransformTranslations(new float[][]{
					{0f,0.0f,0.0f}, //First Person
					{0f,0.0f,0.0f}, //Third Person
					{0.0f,-0.25f,0f}, //GUI
					{0f,0f,0f}, //Ground
					{0f,0.0f,0.0f} //frame
				}).setAmbientParticleFX("ScreenTestFX").setReloadAnim(GunAnimation.breechReload, -0.15f, 55.0f).setReloadAnim3p(GunAnimation.breechReload, 0f, 55.0f));
		
		ItemRenderHack.registerItemRenderer(TGuns.laserpistol,new RenderGunBase(new ModelLaserPistol(),1).setBaseTranslation(RenderItemBase.SCALE*0.5f, -0.3f, -0.4f)
				.setBaseScale(1.2f).setGUIScale(0.7f).setMuzzleFx(ScreenEffect.muzzleFlashLaser, 0.03f, 0.2f, -0.5f, 0.55f,-0.03f).setTransformTranslations(new float[][]{
					{0,0.09f,-0.02f}, //First Person
					{0.0f,-0.03f,0.0f}, //Third Person
					{0.02f,-0.08f,0}, //GUI
					{0.02f,-0.08f,0}, //Ground
					{0,0,0f} //frame
				}).setRecoilAnim(GunAnimation.genericRecoil, 0.0125f, 6.0f).setMuzzleFXPos3P(0.07f, -0.26f));
		
		ItemRenderHack.registerItemRenderer(TGArmors.steam_Helmet,  new RenderArmorItem(new ModelSteamArmor(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/steam_armor.png"), EntityEquipmentSlot.HEAD) );
		ItemRenderHack.registerItemRenderer(TGArmors.steam_Chestplate,  new RenderArmorItem(new ModelSteamArmor(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/steam_armor.png"), EntityEquipmentSlot.CHEST) );
		ItemRenderHack.registerItemRenderer(TGArmors.steam_Leggings,  new RenderArmorItem(new ModelSteamArmor(1), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/steam_armor.png"), EntityEquipmentSlot.LEGS) );
		ItemRenderHack.registerItemRenderer(TGArmors.steam_Boots,  new RenderArmorItem(new ModelSteamArmor(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/steam_armor.png"), EntityEquipmentSlot.FEET) );
	
		ItemRenderHack.registerItemRenderer(TGArmors.t3_power_Helmet,  new RenderArmorItem(new ModelT3PowerArmor(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor.png"), EntityEquipmentSlot.HEAD) );
		ItemRenderHack.registerItemRenderer(TGArmors.t3_power_Chestplate,  new RenderArmorItem(new ModelT3PowerArmor(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor.png"), EntityEquipmentSlot.CHEST) );
		ItemRenderHack.registerItemRenderer(TGArmors.t3_power_Leggings,  new RenderArmorItem(new ModelT3PowerArmor(1), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor.png"), EntityEquipmentSlot.LEGS) );
		ItemRenderHack.registerItemRenderer(TGArmors.t3_power_Boots,  new RenderArmorItem(new ModelT3PowerArmor(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor.png"), EntityEquipmentSlot.FEET) );
		
		ItemRenderHack.registerItemRenderer(TGArmors.t4_power_Helmet,  new RenderArmorItem(new ModelT4PowerArmorMk2(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor_mk2_darkgrey.png"), EntityEquipmentSlot.HEAD) );
		ItemRenderHack.registerItemRenderer(TGArmors.t4_power_Chestplate,  new RenderArmorItem(new ModelT4PowerArmorMk2(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor_mk2_darkgrey.png"), EntityEquipmentSlot.CHEST) );
		ItemRenderHack.registerItemRenderer(TGArmors.t4_power_Leggings,  new RenderArmorItem(new ModelT4PowerArmorMk2(1), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor_mk2_darkgrey.png"), EntityEquipmentSlot.LEGS) );
		ItemRenderHack.registerItemRenderer(TGArmors.t4_power_Boots,  new RenderArmorItem(new ModelT4PowerArmorMk2(0), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/powerarmor_mk2_darkgrey.png"), EntityEquipmentSlot.FEET) );
	
		ItemRenderHack.registerItemRenderer(TGuns.stielgranate,new RenderGrenade(new ModelStielgranate(), new ResourceLocation(Tags.MOD_ID,"textures/guns/stielgranate.png")).setBaseScale(1f).setGUIScale(0.8f).setBaseTranslation(-RenderItemBase.SCALE*0.5f, 0.48f, -RenderItemBase.SCALE).setTransformTranslations(new float[][]{
			{0f,-0.06f,0f}, //First Person
			{0f,-0.15f,0.06f}, //Third Person
			{-0.05f,-0.49f,0f}, //GUI
			{0f,-0.15f,0f}, //Ground
			{0f,0f,-0.05f} //frame
		}));	
		
		ItemRenderHack.registerItemRenderer(TGuns.fraggrenade,new RenderGrenade(new ModelFragGrenade(true), new ResourceLocation(Tags.MOD_ID,"textures/guns/frag_grenade_texture.png"),90.0f).setBaseScale(1.25f).setGUIScale(1.35f).setBaseTranslation(-0.02f,0.65f,-RenderItemBase.SCALE+0.02f).setTransformTranslations(new float[][]{
			{0f,-0.06f,0f}, //First Person
			{0f,-0.11f,-0.01f}, //Third Person
			{-0.05f,-0.49f,0f}, //GUI
			{0f,0.f,0f}, //Ground
			{0f,0f,-0.05f} //frame
		}));	
		
		AdditionalSlotRenderRegistry.register(TGItems.GAS_MASK, new RenderAdditionalSlotItem(new ModelGasMask(), new ResourceLocation(Tags.MOD_ID, "textures/armors/gasmask.png")));
		AdditionalSlotRenderRegistry.register(TGItems.GLIDER, new RenderAdditionalSlotItem(new ModelGlider(), new ResourceLocation(Tags.MOD_ID, "textures/armors/glider.png")));
		AdditionalSlotRenderRegistry.register(TGItems.JUMPPACK, new RenderAdditionalSlotItem(new ModelJetPack(1), Tags.MOD_ID, "textures/armors/jetpack",4));
		AdditionalSlotRenderRegistry.register(TGItems.JETPACK, new RenderAdditionalSlotItem(new ModelJetPack(0), Tags.MOD_ID, "textures/armors/jetpack",4));
		AdditionalSlotRenderRegistry.register(TGItems.SCUBA_TANKS, new RenderAdditionalSlotItem(new ModelOxygenTanks(), new ResourceLocation(Tags.MOD_ID,"textures/armors/oxygentanks.png")));
		AdditionalSlotRenderRegistry.register(TGItems.NIGHTVISION_GOGGLES, new RenderAdditionalSlotItem(new ModelNightVisionGoggles(), new ResourceLocation(Tags.MOD_ID,"textures/armors/nightvisiongoggles.png")));
		AdditionalSlotRenderRegistry.register(TGItems.ANTI_GRAV_PACK, new RenderAdditionalSlotItem(new ModelAntiGravPack(), Tags.MOD_ID, "textures/armors/antigravpack",5));
		AdditionalSlotRenderRegistry.register(TGItems.TACTICAL_MASK, new RenderAdditionalSlotItem(new ModelFaceMask(true), Tags.MOD_ID, "textures/armors/tacticalmask",4));
		
		RenderAdditionalSlotSharedItem sharedItemRenderer = new RenderAdditionalSlotSharedItem();
		sharedItemRenderer.addRenderForSharedItem(TGItems.OXYGEN_MASK.getItemDamage(),new RenderAdditionalSlotItem(new ModelFaceMask(true), new ResourceLocation(Tags.MOD_ID,"textures/armors/oxygenmask.png")));
		sharedItemRenderer.addRenderForSharedItem(TGItems.WORKING_GLOVES.getItemDamage(),new RenderAdditionalSlotItem(new ModelGloves(0.45f,false), new ModelGloves(0.45f, true), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/working_gloves.png"), new ResourceLocation(Tags.MOD_ID,"textures/models/armor/working_gloves_slim.png")));
		
		AdditionalSlotRenderRegistry.register(TGItems.SHARED_ITEM, sharedItemRenderer);
	
		TGArmors.riot_shield.setTileEntityItemStackRenderer(new TileEntityItemRendererTGShield(new ModelRiotShield(), new ResourceLocation(Tags.MOD_ID, "textures/armors/riot_shield.png"),
				new ResourceLocation(Tags.MOD_ID, "textures/armors/riot_shield_black.png"), new ResourceLocation(Tags.MOD_ID, "textures/armors/riot_shield_darkgreen.png"),
				new ResourceLocation(Tags.MOD_ID, "textures/armors/riot_shield_grey.png")));
		
		TGArmors.ballistic_shield.setTileEntityItemStackRenderer(new TileEntityItemRendererTGShield(new ModelBallisticShield(), new ResourceLocation(Tags.MOD_ID, "textures/armors/ballistic_shield.png"),
				new ResourceLocation(Tags.MOD_ID, "textures/armors/ballistic_shield_black.png"), new ResourceLocation(Tags.MOD_ID, "textures/armors/ballistic_shield_green.png"),
				new ResourceLocation(Tags.MOD_ID, "textures/armors/ballistic_shield_grey.png")));
		
		TGArmors.advanced_shield.setTileEntityItemStackRenderer(new TileEntityItemRendererTGShield(new ModelAdvancedShield(), getTextures("textures/armors/advanced_shield", "silver", "green" )));
				
	}
	
	@Override
	protected void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(GenericProjectile.class, RenderGenericProjectile<GenericProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(GenericProjectileIncendiary.class, RenderGenericProjectile<GenericProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(RocketProjectile.class, RenderRocketProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(StoneBulletProjectile.class, RenderStoneBulletProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(BioGunProjectile.class, RenderBioGunProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(FlamethrowerProjectile.class, RenderFlameThrowerProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(GrenadeProjectile.class, RenderGrenadeProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(FlyingGibs.class, RenderFlyingGibs::new);
		RenderingRegistry.registerEntityRenderingHandler(Grenade40mmProjectile.class, RenderGrenade40mmProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(LaserProjectile.class, RenderLaserProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(TeslaProjectile.class, RenderTeslaProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(NDRProjectile.class, RenderNDRProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(BlasterProjectile.class, RenderBlasterProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(GaussProjectile.class, RenderAdvancedBulletProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(AdvancedBulletProjectile.class, RenderAdvancedBulletProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(GuidedMissileProjectile.class, RenderRocketProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(DeatomizerProjectile.class, RenderInvisibleProjectile<DeatomizerProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(SonicShotgunProjectile.class, RenderSonicShotgunProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(CyberdemonBlasterProjectile.class, RenderInvisibleProjectile<CyberdemonBlasterProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(PowerHammerProjectile.class, RenderInvisibleProjectile<PowerHammerProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(ChainsawProjectile.class, RenderInvisibleProjectile<ChainsawProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(FragGrenadeProjectile.class, RenderFragGrenadeProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(RocketProjectileNuke.class, RenderRocketProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(TFGProjectile.class, RenderInvisibleProjectile<TFGProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(GenericProjectileExplosive.class, RenderGenericProjectile<GenericProjectile>::new);
		RenderingRegistry.registerEntityRenderingHandler(RocketProjectileHV.class, RenderRocketProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(GuidedMissileProjectileHV.class, RenderRocketProjectile::new);
		
		//NPCS
		RenderingRegistry.registerEntityRenderingHandler(NPCTurret.class, RenderNPCTurret::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ZombieSoldier.class, RenderZombieSoldier::new);
		RenderingRegistry.registerEntityRenderingHandler(ZombieFarmer.class, RenderZombieFarmer::new);
		RenderingRegistry.registerEntityRenderingHandler(ZombieMiner.class, RenderZombieMiner::new);
		RenderingRegistry.registerEntityRenderingHandler(ArmySoldier.class, RenderArmySoldier::new);
		RenderingRegistry.registerEntityRenderingHandler(Bandit.class, RenderBandit::new);
		RenderingRegistry.registerEntityRenderingHandler(Commando.class, RenderCommando::new);
		RenderingRegistry.registerEntityRenderingHandler(DictatorDave.class, RenderDictatorDave::new);
		RenderingRegistry.registerEntityRenderingHandler(CyberDemon.class, RenderCyberDemon::new);
		RenderingRegistry.registerEntityRenderingHandler(SkeletonSoldier.class, RenderSkeletonSoldier::new);
		RenderingRegistry.registerEntityRenderingHandler(PsychoSteve.class, RenderPsychoSteve::new);
		RenderingRegistry.registerEntityRenderingHandler(StormTrooper.class, RenderStormTrooper::new);
		RenderingRegistry.registerEntityRenderingHandler(Outcast.class, RenderOutcast::new);
		RenderingRegistry.registerEntityRenderingHandler(ZombiePigmanSoldier.class, RenderZombiePigmanSoldier::new);
		RenderingRegistry.registerEntityRenderingHandler(SuperMutantBasic.class, RenderSuperMutant::new);
		RenderingRegistry.registerEntityRenderingHandler(SuperMutantElite.class, RenderSuperMutant::new);
		RenderingRegistry.registerEntityRenderingHandler(SuperMutantHeavy.class, RenderSuperMutant::new);
		RenderingRegistry.registerEntityRenderingHandler(AttackHelicopter.class, RenderAttackHelicopter::new);
		RenderingRegistry.registerEntityRenderingHandler(AlienBug.class, RenderAlienBug::new);
		RenderingRegistry.registerEntityRenderingHandler(Ghastling.class, RenderGhastling::new);
		RenderingRegistry.registerEntityRenderingHandler(ZombiePoliceman.class, RenderZombiePoliceman::new);
	}
	
	
	public static ClientProxy get(){
		return (ClientProxy) Techguns.proxy;
	}

	@Override
	public EntityPlayer getPlayerClient() {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void openWorldGenTestToolGui() {
		Minecraft.getMinecraft().displayGuiScreen(new WorldGenTestToolGui());
	}

	@Override
	public void setGunTextures(GenericGun gun, String path, int variations) {
		gun.textures = new ArrayList<>();
		for(int i = 0; i < variations; i++){
			gun.textures.add(new ResourceLocation(Tags.MOD_ID,path + ( i != 0 ? ("_" + i) : "") + ".png"));
		}
	}
	
	@Override
	public void setGunTextures(GenericGun gun, ResourceLocation path, int variations) {
		gun.textures = new ArrayList<>();
		for(int i = 0; i < variations; i++){
			gun.textures.add(new ResourceLocation(path.getNamespace(),path.getPath() + (i != 0 ? ("_" + i) : "") + ".png"));
		}
	}

	@Override
	public void handleSoundEvent(EntityPlayer ply, int entityId, SoundEvent soundname, float volume, float pitch, boolean repeat, boolean moving,
			boolean gunPosition,boolean playOnOwnPlayer, TGSoundCategory soundCategory, EntityCondition condition) {
		Entity entity = null;
		if (entityId != -1) {
			entity = ply.world.getEntityByID(entityId);
		}

		if(entity != null){
			if (entity != ply || playOnOwnPlayer ) {
				Minecraft.getMinecraft().getSoundHandler().playSound(new TGSound(soundname,entity,volume,pitch, repeat, moving, gunPosition,soundCategory));
					
			}
		}
	}



	@Override
	public void playSoundOnEntity(Entity ent, SoundEvent soundname, float volume, float pitch, boolean repeat,
			boolean moving, boolean gunPosition, boolean playForOwnPlayer, TGSoundCategory category) {
		if(playForOwnPlayer || ent != this.getPlayerClient()) {
			this.playSoundOnEntity(ent, soundname, volume, pitch, repeat, moving, gunPosition, category);
		}
	}

	@Override
	public void playSoundOnEntity(Entity ent, SoundEvent soundname, float volume, float pitch, boolean repeat,
			boolean moving, boolean gunPosition, boolean playForOwnPlayer, TGSoundCategory category, EntityCondition condition) {
		if(playForOwnPlayer || ent != this.getPlayerClient()) {
			this.playSoundOnEntity(ent, soundname, volume, pitch, repeat, moving, gunPosition, category, condition);
		}
	}

    @Override
    public void playSoundOnEntity(Entity ent, SoundEvent soundname, float volume, float pitch, boolean repeat,
                                  boolean moving, boolean gunPosition, TGSoundCategory category) {
        EntityPlayerSP self = Minecraft.getMinecraft().player;
        if (self != null && ent == self) {
            gunPosition = false;
        }
        Minecraft.getMinecraft().getSoundHandler().playSound(new TGSound(soundname, ent, volume, pitch, repeat, moving, gunPosition, category));
    }

    @Override
    public void playSoundOnEntity(Entity ent, SoundEvent soundname, float volume, float pitch, boolean repeat,
                                  boolean moving, boolean gunPosition, TGSoundCategory category, EntityCondition condition) {
        EntityPlayerSP self = Minecraft.getMinecraft().player;
        if (self != null && ent == self) {
            gunPosition = false;
        }
        Minecraft.getMinecraft().getSoundHandler().playSound(new TGSound(soundname, ent, volume, pitch, repeat, moving, gunPosition, category, condition));
    }
	
	
	@Override
	public void playSoundOnPosition(SoundEvent soundname, float posx, float posy, float posz, float volume, float pitch, boolean repeat, TGSoundCategory soundCategory) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new TGSound(soundname,posx,posy,posz,volume,pitch, repeat,soundCategory));
	}
	
	@Override
	public void createFX(String name, World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ){	
		List<TGParticleSystem> systems = TGFX.createFX(world, name, posX, posY, posZ, motionX, motionY, motionZ);
		if (systems!=null) {
			systems.forEach(s -> particleManager.addEffect(s));//Minecraft.getMinecraft().effectRenderer.addEffect(s));
		}
	}
	
	@Override
	public void createFX(String name, World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float pitch, float yaw){	
		List<TGParticleSystem> systems = TGFX.createFX(world, name, posX, posY, posZ, motionX, motionY, motionZ);
		if (systems!=null) {
			for (TGParticleSystem s : systems) {
				s.rotationPitch = pitch;
				s.rotationYaw = yaw;
				particleManager.addEffect(s);
			}
		}
	}
	
	@Override
	public void createFX(String name, World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, float scale){	
		List<TGParticleSystem> systems = TGFX.createFX(world, name, posX, posY, posZ, motionX, motionY, motionZ);
		if (systems!=null) {
			for (TGParticleSystem s : systems) {
				s.scale = scale;
				particleManager.addEffect(s);
			}
		}
	}
	
	@Override
	public void createFXOnEntity(String name, Entity ent) {
		List<TGParticleSystem> systems = TGFX.createFXOnEntity(ent, name);
		if (systems!=null) {
			systems.forEach(s -> {
				s.condition=EntityCondition.ENTITY_ALIVE;
				particleManager.addEffect(s);
			}); //Minecraft.getMinecraft().effectRenderer.addEffect(s));
		}
	}
	
	@Override
	public void createFXOnEntity(String name, Entity ent, float scale) {
		List<TGParticleSystem> systems = TGFX.createFXOnEntity(ent, name);
		if (systems!=null) {
			for (TGParticleSystem s : systems) {
				s.scale = scale;
				s.condition=EntityCondition.ENTITY_ALIVE;
				particleManager.addEffect(s);
			}
		}
	}
	
	@Override
	public void createFXOnEntityWithOffset(String name, Entity ent, float offsetX, float offsetY, float offsetZ, boolean attachToHead, EntityCondition condition) {
		List<TGParticleSystem> systems = TGFX.createFXOnEntity(ent, name);
		if (systems!=null) {
			for (TGParticleSystem s : systems) {
				s.entityOffset = new Vec3d(offsetX, offsetY, offsetZ);
				s.attachToHead = attachToHead;
				s.condition = condition;
				particleManager.addEffect(s);
			}
		}
	}
	
	@Override
	public void setHasStepassist(boolean value) {
		this.hasStepassist=value;
	}

	@Override
	public void setHasNightvision(boolean value) {
		this.hasNightvision=value;
	}

	@Override
	public boolean getHasStepassist() {
		return this.hasStepassist;
	}

	@Override
	public boolean getHasNightvision() {
		return this.hasNightvision;
	}
	
	@Override
	public void setFlySpeed(float value) {
		EntityPlayer ply = getPlayerClient();
		ply.capabilities.setFlySpeed(value);
	}

	@Override
	public void registerCapabilities() {
		super.registerCapabilities();
		CapabilityManager.INSTANCE.register(TGDeathTypeCap.class, new TGDeathTypeCapStorage(), () -> new TGDeathTypeCap(null));
	}
	
	/**
	 * EntityDeathType
	 */
	public void setEntityDeathType(EntityLivingBase entity, DeathType deathtype){
		TGDeathTypeCap cap = TGDeathTypeCap.get(entity);
		cap.setDeathType(deathtype);
	}
	
	public DeathType getEntityDeathType(EntityLivingBase entity) {
		return TGDeathTypeCap.get(entity).getDeathType();
	}
	
	@Deprecated
	public boolean hasDeathType(EntityLivingBase entity) {
		return TGDeathTypeCap.get(entity).getDeathType() == DeathType.DEFAULT;
	}
	
	@Deprecated
	public void clearEntityDeathType(EntityLivingBase entity) {
		setEntityDeathType(entity, DeathType.DEFAULT);
	}
		
	@Override
	public boolean isClientPlayerAndIn1stPerson(EntityLivingBase ent) {
		return ent == this.getPlayerClient() && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0;
	}

	@Override
	public void createLightPulse(double x, double y, double z, int lifetime, float rad_start, float rad_end, float r, float g, float b) {
		if (this.lightPulsesEnabled) {
			this.activeLightPulses.add(new LightPulse(x, y, z, lifetime, rad_start, rad_end, r, g, b));
		}
	}

	@Override
	public void createLightPulse(double x, double y, double z, int fadeIn, int fadeOut, float rad_large, float rad_small, float r, float g, float b) {
		if (this.lightPulsesEnabled) {
			this.activeLightPulses.add(new LightPulse(x, y, z, fadeIn, fadeOut, rad_large, rad_small, r, g, b));
		}
	}
	
	public String resolvePlayerNameFromUUID(UUID uuid){
		String name = UsernameCache.getLastKnownUsername(uuid);
		if (name!=null){
			return name;
		} else {
			return "UNKNOW_PLAYERNAME";
		}
	}

	@Override
	public void registerFluidModelsForFluidBlock(Block b) {
		if(!(b instanceof IFluidBlock)) {
			System.out.println("Tried to register "+b+ " as Fluid, but block is no IFluidBlock");
			return;
		}
		IFluidBlock f = (IFluidBlock) b;
		
		final Item item = Item.getItemFromBlock(b);
		if(item==Items.AIR) {
			System.out.println("No item found for IFluidBlock "+b);
			return;
		}
		
		ModelBakery.registerItemVariants(item);
		
		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(Tags.MOD_ID, "fluid"), f.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
			@Override
			public @NotNull ModelResourceLocation getModelLocation(@NotNull ItemStack stack) {
				return modelResourceLocation;
			}
		});

		ModelLoader.setCustomStateMapper(b, new StateMapperBase() {
			@Override
			protected @NotNull ModelResourceLocation getModelResourceLocation(final @NotNull IBlockState bs) {
				return modelResourceLocation;
			}
		});
	}

	@Override
	public void clearItemParticleSystemsHand(EntityLivingBase elb, EnumHand hand) {
		if(elb instanceof EntityPlayer) {
			TGExtendedPlayerClient props = TGExtendedPlayerClient.get((EntityPlayer) elb);
			props.clearAttachedSystemsHand(hand);
		} else if (elb instanceof INPCTechgunsShooter) {
			TGShooterValues props = TGShooterValues.get(elb);
			props.clearAttachedSystemsHand(hand);
		}
	}
	
	@Override
	public void handlePlayerGliding(EntityPlayer player) {
		if (player.world.isRemote){
			TGExtendedPlayerClient props = TGExtendedPlayerClient.get(player);
			if(props.isGliding){
				 if(props.gliderLoop==null){
					 props.gliderLoop=new TGSound(TGSounds.GLIDER_LOOP, player, 0.75f, 1.0f, true, true, false,TGSoundCategory.PLAYER_EFFECT);
					 Minecraft.getMinecraft().getSoundHandler().playSound(props.gliderLoop);
				 }
			 } else {
				 if(props.gliderLoop!=null){
					 props.gliderLoop.setDonePlaying();
					 props.gliderLoop=null;
				 }
			 }
		}
	}

	protected static ResourceLocation[] getTextures(String name, String ...suffixes) {
		ResourceLocation[] tex = new ResourceLocation[suffixes.length+1];
		tex[0]=new ResourceLocation(Tags.MOD_ID, name+".png");
		
		for(int i=1; i<=suffixes.length; i++) {
			tex[i] = new ResourceLocation(Tags.MOD_ID, name+"_"+suffixes[i-1]+".png");
		}
		
		return tex;
		
	}
}
