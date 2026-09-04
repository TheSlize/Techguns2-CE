package techguns;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import techguns.blocks.EnumConcreteType;
import techguns.blocks.EnumNetherMetalType;
import techguns.blocks.TGMetalPanelType;
import techguns.init.ITGInitializer;

// Th3_Sl1ze: from now on, workbench recipes will be slowly converted to here. I mean, if you delete crafts there's 100% you use either CT or Groovyscript.
// For now I left generic blocks recipes here.
public class TGRecipes implements ITGInitializer {

    @Override
    public void init(FMLInitializationEvent event) {
        for (TGMetalPanelType type : TGMetalPanelType.values()) {
            int meta = type.ordinal();
            String typeName = type.getName();
            if (meta < 8) {
                addShaped(typeName + "_slab", new ItemStack(TGBlocks.METAL_SLAB, 6, meta),
                        "XXX",
                        'X', new ItemStack(TGBlocks.METAL_PANEL, 1, meta)
                );

                addShaped(typeName + "_fence", new ItemStack(TGBlocks.METAL_FENCE, 6, meta),
                        "XXX",
                        "XXX",
                        'X', new ItemStack(TGBlocks.METAL_PANEL, 1, meta)
                );
            } else {
                addShaped(typeName + "_slab", new ItemStack(TGBlocks.METAL_SLAB_ALT, 6, meta - 8),
                        "XXX",
                        'X', new ItemStack(TGBlocks.METAL_PANEL, 1, meta)
                );

                addShaped(typeName + "_fence", new ItemStack(TGBlocks.METAL_FENCE_ALT, 6, meta - 8),
                        "XXX",
                        "XXX",
                        'X', new ItemStack(TGBlocks.METAL_PANEL, 1, meta)
                );
            }
        }

        for (EnumConcreteType type : EnumConcreteType.values()) {
            int meta = type.ordinal();
            String typeName = type.getName();
            if(meta > 5) {
                addShaped(typeName + "_slab", new ItemStack(TGBlocks.CONCRETE_SLAB_ALT, 6, meta - 6),
                        "XXX",
                        'X', new ItemStack(TGBlocks.CONCRETE, 1, meta)
                );
            } else {
                addShaped(typeName + "_slab", new ItemStack(TGBlocks.CONCRETE_SLAB, 6, meta),
                        "XXX",
                        'X', new ItemStack(TGBlocks.CONCRETE, 1, meta)
                );
            }
            if(meta == 5) {
                addShaped(typeName + "_fence", new ItemStack(TGBlocks.CONCRETE_FENCE, 6, 4),
                        "XXX",
                        "XXX",
                        'X', new ItemStack(TGBlocks.CONCRETE, 1, meta)
                );
            } else if (meta < 4){
                addShaped(typeName + "_fence", new ItemStack(TGBlocks.CONCRETE_FENCE, 6, meta),
                        "XXX",
                        "XXX",
                        'X', new ItemStack(TGBlocks.CONCRETE, 1, meta)
                );
            }
        }

        for (EnumNetherMetalType type : EnumNetherMetalType.values()) {
            int meta = type.ordinal();
            String typeName = type.getName();

            if(meta != EnumNetherMetalType.BORDER_LAVA.ordinal()) {
                if(meta != EnumNetherMetalType.PLATE_RED.ordinal()) {
                    addShaped(typeName + "_slab", new ItemStack(TGBlocks.NETHER_METAL_SLAB, 6, meta),
                            "XXX",
                            'X', new ItemStack(TGBlocks.NETHER_METAL, 1, meta)
                    );
                } else {
                    addShaped(typeName + "_slab", new ItemStack(TGBlocks.NETHER_METAL_SLAB_ALT, 6, 0),
                            "XXX",
                            'X', new ItemStack(TGBlocks.NETHER_METAL, 1, meta)
                    );
                }
                if(meta >= EnumNetherMetalType.BORDER_RED.ordinal()) {
                    addShaped(typeName + "_fence", new ItemStack(TGBlocks.NETHER_METAL_FENCE, 6, meta),
                            "XXX",
                            "XXX",
                            'X', new ItemStack(TGBlocks.NETHER_METAL, 1, meta + 1)
                    );
                } else {
                    addShaped(typeName + "_fence", new ItemStack(TGBlocks.NETHER_METAL_FENCE, 6, meta),
                            "XXX",
                            "XXX",
                            'X', new ItemStack(TGBlocks.NETHER_METAL, 1, meta)
                    );
                }
            }
        }

        addStair(TGBlocks.METAL_STAIRS, 7, TGBlocks.METAL_PANEL, TGMetalPanelType.PANEL_LARGE_BORDER.ordinal(), "metal_stairs_0");
        addStair(TGBlocks.METAL_STAIRS, 15, TGBlocks.METAL_PANEL, TGMetalPanelType.STEELFRAME_DARK.ordinal(), "metal_stairs_1");
        addStair(TGBlocks.METAL_STAIRS_ALT, 7, TGBlocks.METAL_PANEL, TGMetalPanelType.CONTAINER_RED.ordinal(), "metal_stairs_alt_0");
        addStair(TGBlocks.METAL_STAIRS_ALT, 15, TGBlocks.METAL_PANEL, TGMetalPanelType.CONTAINER_GREEN.ordinal(), "metal_stairs_alt_1");
        addStair(TGBlocks.METAL_STAIRS_ALT1, 7, TGBlocks.METAL_PANEL, TGMetalPanelType.CONTAINER_BLUE.ordinal(), "metal_stairs_alt1_0");
        addStair(TGBlocks.METAL_STAIRS_ALT1, 15, TGBlocks.METAL_PANEL, TGMetalPanelType.CONTAINER_ORANGE.ordinal(), "metal_stairs_alt1_1");
        addStair(TGBlocks.METAL_STAIRS_ALT2, 7, TGBlocks.METAL_PANEL, TGMetalPanelType.STEELFRAME_BLUE.ordinal(), "metal_stairs_alt2_0");
        addStair(TGBlocks.METAL_STAIRS_ALT2, 15, TGBlocks.METAL_PANEL, TGMetalPanelType.STEELFRAME_SCAFFOLD.ordinal(), "metal_stairs_alt2_1");
        addStair(TGBlocks.METAL_STAIRS_ALT3, 7, TGBlocks.METAL_PANEL, TGMetalPanelType.CONTAINER_SCIFI.ordinal(), "metal_stairs_alt3_0");
        addStair(TGBlocks.METAL_STAIRS_ALT3, 15, TGBlocks.METAL_PANEL, TGMetalPanelType.CONTAINER_SCIFI_RUSTY.ordinal(), "metal_stairs_alt3_1");
        addStair(TGBlocks.CONCRETE_STAIRS, 7, TGBlocks.CONCRETE, EnumConcreteType.CONCRETE_GREY_DARK.ordinal(), "concrete_stairs_0");
        addStair(TGBlocks.CONCRETE_STAIRS, 15, TGBlocks.CONCRETE, EnumConcreteType.CONCRETE_BROWN_LIGHT.ordinal(), "concrete_stairs_1");
        addStair(TGBlocks.CONCRETE_STAIRS_ALT, 7, TGBlocks.CONCRETE, EnumConcreteType.CONCRETE_BROWN.ordinal(), "concrete_stairs_alt_0");
        addStair(TGBlocks.CONCRETE_STAIRS_ALT, 15, TGBlocks.CONCRETE, EnumConcreteType.CONCRETE_GREY.ordinal(), "concrete_stairs_alt_1");
        addStair(TGBlocks.CONCRETE_STAIRS_ALT1, 7, TGBlocks.CONCRETE, EnumConcreteType.CONCRETE_BROWN_LIGHT_SCAFF.ordinal(), "concrete_stairs_alt1_0");
        addStair(TGBlocks.NETHER_METAL_STAIRS, 7, TGBlocks.NETHER_METAL, EnumNetherMetalType.PANEL.ordinal(), "nethermetal_stairs_0");
        addStair(TGBlocks.NETHER_METAL_STAIRS, 15, TGBlocks.NETHER_METAL, EnumNetherMetalType.GRATE1.ordinal(), "nethermetal_stairs_1");
        addStair(TGBlocks.NETHER_METAL_STAIRS_ALT, 7, TGBlocks.NETHER_METAL, EnumNetherMetalType.GRATE2.ordinal(), "nethermetal_stairs_alt_0");
        addStair(TGBlocks.NETHER_METAL_STAIRS_ALT, 15, TGBlocks.NETHER_METAL, EnumNetherMetalType.GREY_DARK.ordinal(), "nethermetal_stairs_alt_1");
        addStair(TGBlocks.NETHER_METAL_STAIRS_ALT1, 7, TGBlocks.NETHER_METAL, EnumNetherMetalType.GREY.ordinal(), "nethermetal_stairs_alt1_0");
        addStair(TGBlocks.NETHER_METAL_STAIRS_ALT1, 15, TGBlocks.NETHER_METAL, EnumNetherMetalType.GREY_TILES.ordinal(), "nethermetal_stairs_alt1_1");
        addStair(TGBlocks.NETHER_METAL_STAIRS_ALT2, 7, TGBlocks.NETHER_METAL, EnumNetherMetalType.PLATE_RED.ordinal(), "nethermetal_stairs_alt2_0");
        addStair(TGBlocks.NETHER_METAL_STAIRS_ALT2, 15, TGBlocks.NETHER_METAL, EnumNetherMetalType.PLATE_BLACK.ordinal(), "nethermetal_stairs_alt2_1");
    }

    private static void addStair(Block output, int metaOut, Block input, int metaIn, String name) {
        addShaped(name, new ItemStack(output, 4, metaOut),
                "X  ",
                "XX ",
                "XXX",
                'X', new ItemStack(input, 1, metaIn)
        );
    }

    private static void addShaped(String name, ItemStack output, Object... params) {
        GameRegistry.addShapedRecipe(new ResourceLocation(Tags.MOD_ID, name), null, output, params);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }
}