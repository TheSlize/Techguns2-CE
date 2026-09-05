package techguns;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import techguns.init.ITGInitializer;
import techguns.items.guns.ammo.AmmoTypes;
import techguns.plugins.bht.TGBetterHurtTimerPlugin;
import techguns.plugins.chisel.TGChiselBlocks;
import techguns.plugins.crafttweaker.TGCraftTweakerIntegration;
import techguns.plugins.hats.TGHatsPlugin;
import techguns.server.CommandSetSpawner;
import techguns.world.OreGenerator;
import techguns.world.WorldGenTGStructureSpawn;
import techguns.world.dungeon.DungeonTemplate;

@Mod(modid = Tags.MOD_ID, version = Tags.VERSION, name = Tags.MOD_NAME, acceptedMinecraftVersions = Techguns.MCVERSION, updateJSON = Techguns.UPDATEURL, dependencies = Techguns.DEPENDENCIES)
public class Techguns {
    public static final String MCVERSION = "1.12.2";

    public static final Logger logger = LogManager.getLogger(Tags.MOD_ID);
    public static final String UPDATEURL = "https://raw.githubusercontent.com/TheSlize/Techguns2-CE/master/update.json";
    public static final String FORGE_BUILD = "14.23.5.2847";
    public static final String DEPENDENCIES = "required:forge@[" + FORGE_BUILD + ",);after:ftblib;after:chisel;after:configanytime;after:hats;after:betterhurttimer";

    @Mod.Instance
    public static Techguns instance;

    @SidedProxy(clientSide = "techguns.client.ClientProxy", serverSide = "techguns.server.ServerProxy")
    public static CommonProxy proxy;

    public TGItems items = new TGItems();
    public TGBlocks blocks = new TGBlocks();
    public TGuns guns = new TGuns();
    public TGEntities entities = new TGEntities();
    public TGPackets packets = new TGPackets();
    public AmmoTypes ammoTypes = new AmmoTypes();
    public TGArmors armors = new TGArmors();
    public TGFluids fluids = new TGFluids();
    public TGRecipes recipes = new TGRecipes();
    public TGPermissions permissions = new TGPermissions();
    public static TGOreClusters orecluster = new TGOreClusters();

    public static TGRadiationSystem rad = new TGRadiationSystem();

    //Mod integration
    public boolean FTBLIB_ENABLED = false;
    public boolean CHISEL_ENABLED = false;

    protected ITGInitializer[] initializers = {
            items,
            armors,
            ammoTypes,
            guns,
            fluids,
            blocks,
            entities,
            packets,
            rad,
            orecluster,
            recipes,
            permissions
    };


    public static CreativeTabs tabTechgun = new CreativeTabs(Tags.MOD_ID) {

        @Override
        @SideOnly(Side.CLIENT)
        public @NotNull ItemStack createIcon() {
            return new ItemStack(TGItems.PISTOL_ROUNDS.getItem(), 1, TGItems.PISTOL_ROUNDS.getItemDamage());
        }

        @Override
        public @NotNull String getTranslationKey() {
            return Tags.MOD_ID + "." + super.getTranslationKey();
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    };

    static {
        tabTechgun.setBackgroundImageName("item_search.png");
    }

    public static int modEntityID = -1;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("ftblib")) {
            FTBLIB_ENABLED = true;
        }
        if (Loader.isModLoaded("chisel")) {
            CHISEL_ENABLED = true;
        }
        if (Loader.isModLoaded("hats")) {
            TGHatsPlugin.init();
        }
        if (Loader.isModLoaded("betterhurttimer")) {
            TGBetterHurtTimerPlugin.init();
        }

        for (ITGInitializer init : initializers) {
            init.init(event);
        }
        proxy.init(event);
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        for (ITGInitializer init : initializers) {
            init.preInit(event);
        }
        proxy.preInit(event);

        if (Loader.isModLoaded("crafttweaker")) {
            TGCraftTweakerIntegration.init();
        }

        if (TGConfig.worldgen.doOreGenTitanium || TGConfig.worldgen.doOreGenUranium || TGConfig.worldgen.doOreGenLead || TGConfig.worldgen.doOreGenTin || TGConfig.worldgen.doOreGenCopper) {
            GameRegistry.registerWorldGenerator(new OreGenerator(), 1);
        }

        if (TGConfig.worldgen.doWorldspawn) {
            GameRegistry.registerWorldGenerator(new WorldGenTGStructureSpawn(), 6);
        }
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        for (ITGInitializer init : initializers) {
            init.postInit(event);
        }
        proxy.postInit(event);

        if (this.CHISEL_ENABLED) {
            TGChiselBlocks.postInit();
        }
        DungeonTemplate.init();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
        evt.registerServerCommand(new CommandSetSpawner());
    }


}