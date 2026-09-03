package techguns.world.structures;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import techguns.Tags;
import techguns.blocks.EnumMonsterSpawnerType;
import techguns.entities.npcs.CyberDemon;
import techguns.entities.npcs.StormTrooper;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.world.dungeon.presets.specialblocks.MBlockBanner;
import techguns.world.dungeon.presets.specialblocks.MBlockChestLoottable;
import techguns.world.dungeon.presets.specialblocks.MBlockSkull;
import techguns.world.dungeon.presets.specialblocks.MBlockTGSpawner;

import java.util.ArrayList;
import java.util.Random;

public class NetherSubmarine extends WorldgenStructure {

    private static final ResourceLocation CHEST_LOOT_COMMON = new ResourceLocation(Tags.MOD_ID, "chests/nether_submarine_common");
    private static final ResourceLocation CHEST_LOOT_RARE = new ResourceLocation(Tags.MOD_ID, "chests/nether_submarine_rare");

    public static final int LAVA_SUBMERGE_DEPTH = 4;

    static ArrayList<MBlock> blockList = new ArrayList<>();
    static short[][] blocks;

    static {
        blockList.add(new MBlock("minecraft:air", 0));
        blockList.add(new MBlock("techguns:metalpanel", 7));
        blockList.add(new MBlock("techguns:stairs_metal", 11));
        blockList.add(new MBlock("minecraft:stained_hardened_clay", 14));
        blockList.add(new MBlock("techguns:stairs_metal", 15));
        blockList.add(new MBlock("techguns:metalpanel", 6));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 5, CHEST_LOOT_RARE));
        blockList.add(new MBlock("techguns:concrete", 3));
        blockList.add(new MBlock("minecraft:jukebox", 0));
        blockList.add(new MBlockSkull(3, 3, "3d80d659-36cd-4aee-8540-8cdb548ede75", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FmNTk3NzZmMmYwMzQxMmM3YjU5NDdhNjNhMGNmMjgzZDUxZmU2NWFjNmRmN2YyZjg4MmUwODM0NDU2NWU5In19fQ=="));
        blockList.add(new MBlockSkull(3, 5, "12c8e58d-6b45-49b6-9b63-77b57b290243", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM0NGQ4M2U3YzZlY2U5MGQzNGM2ODVhOWEzMzg0ZjhlOGQwMTUxNjMzZmMyZWVhZTRkNGI2MzY4NjJkMzMifX19"));
        blockList.add(new MBlock("minecraft:furnace", 5));
        blockList.add(new MBlock("techguns:lamp0", 3));
        blockList.add(new MBlock("techguns:lamp0", 2));
        blockList.add(new MBlock("techguns:concrete", 2));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 8));
        blockList.add(new MBlock("techguns:bunkerdoor", 7));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 12));
        blockList.add(new MBlock("minecraft:tnt", 0));
        blockList.add(new MBlock("techguns:military_crate", 4));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 13));
        blockList.add(new MBlock("techguns:bunkerdoor", 9));
        blockList.add(new MBlock("techguns:bunkerdoor", 8));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 4));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 5));
        blockList.add(new MBlock("minecraft:double_stone_slab", 0));
        blockList.add(new MBlock("techguns:stairs_concrete", 7));
        blockList.add(new MBlockSkull(3, 3, "89eddb62-24bd-48a3-b1d8-3f5fea05e754", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdkOTc3ZTI4ODlhMWY1MGJiYjIzZDQyMjI3NWFiOWRhYTNjMTUwYzg5ODMxZTc5ODRiNDliNDA5ZGIwNjcifX19"));
        blockList.add(new MBlock("minecraft:oak_stairs", 1));
        blockList.add(new MBlock("techguns:bunkerdoor", 5));
        blockList.add(new MBlock("minecraft:spruce_stairs", 1));
        blockList.add(new MBlock("minecraft:wooden_slab", 9));
        blockList.add(new MBlock("minecraft:flower_pot", 0));
        blockList.add(new MBlock("minecraft:stone_slab", 8));
        blockList.add(new MBlock("techguns:stairs_metal", 6));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 15));
        blockList.add(new MBlock("minecraft:nether_brick_stairs", 0));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 7));
        blockList.add(new MBlock("minecraft:fence", 0));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 2, CHEST_LOOT_COMMON));
        blockList.add(new MBlock("techguns:ladder0", 8));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 4, CHEST_LOOT_COMMON));
        blockList.add(new MBlock("minecraft:spruce_stairs", 0));
        blockList.add(new MBlock("techguns:ladder0", 0));
        blockList.add(new MBlock("minecraft:wooden_pressure_plate", 0));
        blockList.add(new MBlockSkull(3, 1, "8e39ec3f-2af4-48d8-ae39-25e9b8561527", "uioz", "ewogICJ0aW1lc3RhbXAiIDogMTc2OTQyNTM2OTE1NCwKICAicHJvZmlsZUlkIiA6ICI4ZTM5ZWMzZjJhZjQ0OGQ4YWUzOTI1ZTliODU2MTUyNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ1aW96IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2RiOWYwY2VjYjcxOTRkOTE0NzcwN2Q3YTU5NDM3YWM1ZWM4Nzc5MjFkNjVlOTFkZWYzZGVmYTlhNDQ2YWMwYjQiCiAgICB9CiAgfQp9"));
        blockList.add(new MBlock("minecraft:stone_slab", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 10));
        blockList.add(new MBlockTGSpawner(EnumMonsterSpawnerType.SOLDIER_SPAWN, 5, 2, 120, 0).addMobType(StormTrooper.class, 15).addMobType(CyberDemon.class, 5));
        blockList.add(new MBlock("minecraft:lever", 2));
        blockList.add(new MBlock("minecraft:stone_button", 2));
        blockList.add(new MBlock("techguns:ladder0", 4));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 2, CHEST_LOOT_RARE));
        blockList.add(new MBlock("minecraft:tripwire_hook", 1));
        blockList.add(new MBlock("techguns:metalpanel", 5));
        blockList.add(new MBlock("minecraft:oak_stairs", 0));
        blockList.add(new MBlock("minecraft:anvil", 1));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 5, CHEST_LOOT_COMMON));
        blockList.add(new MBlock("techguns:ladder0", 10));
        blockList.add(new MBlock("techguns:metalpanel", 4));
        blockList.add(new MBlockChestLoottable(Blocks.CHEST, 3, CHEST_LOOT_COMMON));
        blockList.add(new MBlock("minecraft:bed", 3));
        blockList.add(new MBlock("minecraft:bookshelf", 0));
        blockList.add(new MBlock("techguns:stairs_metal", 14));
        blockList.add(new MBlock("techguns:lamp0", 1));
        blockList.add(new MBlockSkull(3, 1, "18a2bb50-334a-4084-9184-2c380251a24b", "MHF_PigZombie", "ewogICJ0aW1lc3RhbXAiIDogMTc2OTQyNTEzMDk5NSwKICAicHJvZmlsZUlkIiA6ICIxOGEyYmI1MDMzNGE0MDg0OTE4NDJjMzgwMjUxYTI0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfUGlnWm9tYmllIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzkxNmQxNjdjNTc0NGVkMTRlYmMwMmY0NDdmMzI2MTQwNTkzNjJiN2QyZWNiODA4ZmYwNjE2NWQyYzM0M2JlZjIiCiAgICB9CiAgfQp9"));
        blockList.add(new MBlock("techguns:bunkerdoor", 1));
        blockList.add(new MBlockBanner(true, 6, 15, MBlockBanner.createPatterns("cr", 1, "sc", 1, "ss", 0, "cbo", 0, "bo", 0, "flo", 0)));
        blockList.add(new MBlock("techguns:stairs_metal", 7));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 14));
        blockList.add(new MBlock("minecraft:nether_brick_stairs", 1));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 6));
        blockList.add(new MBlock("techguns:stairs_concrete", 0));
        blockList.add(new MBlock("minecraft:bed", 11));
        blockList.add(new MBlock("minecraft:lever", 10));
        blockList.add(new MBlock("techguns:stairs_metal", 13));
        blockList.add(new MBlock("techguns:stairs_metal", 9));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 11));
        blockList.add(new MBlock("techguns:stairs_metal", 2));
        blockList.add(new MBlock("techguns:stairs_metal", 4));
        blockList.add(new MBlockSkull(3, 10, "c020761e-41d2-4e18-b3e8-8e28c5643612", null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2U0ZWNjYjE3YThlOTI2MDFhNTE5MTdjYjVhNmZjMTdlYWYyYWRjMjdlODM3MzIzNmIyMzIzZjQ3NGVmNDhmMSJ9fX0="));
        blockList.add(new MBlock("techguns:stairs_concrete", 5));
        blockList.add(new MBlock("techguns:bunkerdoor", 3));
        blockList.add(new MBlockChestLoottable(Blocks.TRAPPED_CHEST, 4, CHEST_LOOT_RARE));
        blockList.add(new MBlock("minecraft:bed", 0));
        blockList.add(new MBlock("minecraft:bed", 8));
        blockList.add(new MBlock("minecraft:iron_trapdoor", 4));
        blockList.add(new MBlock("minecraft:dropper", 10));

        blocks = BlockUtils.loadStructureFromFile("nether_submarine");
    }

    public NetherSubmarine() {
        super(0, 0, 0, 0, 0, 0);
        this.setXYZSize(17, 14, 33);
    }

    @Override
    public void setBlocks(World world, int posX, int posY, int posZ, int sizeX,
                          int sizeY, int sizeZ, int direction, BiomeColorType colorType, Random rnd) {
        int centerX, centerZ;

        if (((sizeX < this.minX) && (sizeZ > this.minX) && (sizeX >= this.minZ))
                || ((sizeZ < this.minZ) && (sizeX > this.minZ) && (sizeZ >= this.minX))) {
            direction = (direction + 1) % 4;
            centerZ = (int) (sizeX / 2.0f);
            centerX = (int) (sizeZ / 2.0f);
        } else {
            centerX = (int) (sizeX / 2.0f);
            centerZ = (int) (sizeZ / 2.0f);
        }

        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 0, this.lootTier, colorType);
        BlockUtils.placeScannedStructure(world, blocks, blockList, posX, posY, posZ, centerX, centerZ, direction, 1, this.lootTier, colorType);
    }

    public void spawnStructureLavaWorldgen(World world, int chunkX, int chunkZ, int sizeX, int sizeY, int sizeZ, Random rnd, Biome biome) {
        int direction = rnd.nextInt(4);

        int sizeXr = direction == 0 || direction == 2 ? sizeX : sizeZ;
        int sizeZr = direction == 0 || direction == 2 ? sizeZ : sizeX;

        int centerX = (int) (sizeX / 2.0f);
        int centerZ = (int) (sizeZ / 2.0f);

        int[] p0 = rotatePoint(0, 0, direction, centerX, centerZ);
        int[] p1 = rotatePoint(sizeX, 0, direction, centerX, centerZ);
        int[] p2 = rotatePoint(0, sizeZ, direction, centerX, centerZ);
        int[] p3 = rotatePoint(sizeX, sizeZ, direction, centerX, centerZ);

        int minX = Math.min(Math.min(p0[0], p1[0]), Math.min(p2[0], p3[0]));
        int minZ = Math.min(Math.min(p0[1], p1[1]), Math.min(p2[1], p3[1]));

        int x = minX + chunkX * 16;
        int z = minZ + chunkZ * 16;

        int lavaY = getValidLavaSpawnY(world, x, z, sizeXr, sizeZr, 3);

        if (lavaY < 0) {
            return;
        }

        int posY = lavaY - LAVA_SUBMERGE_DEPTH + 1;

        this.setBlocks(world, x, posY, z, sizeX, sizeY, sizeZ, direction, getBiomeColorTypeFromBiome(biome), rnd);
    }

    public static int getValidLavaSpawnY(World world, int x, int z, int sizeX, int sizeZ, int heightDiffLimit) {
        int h0 = getLavaSurfaceHeight(world, x, z);
        int h1 = getLavaSurfaceHeight(world, x + sizeX, z);
        int h2 = getLavaSurfaceHeight(world, x, z + sizeZ);
        int h3 = getLavaSurfaceHeight(world, x + sizeX, z + sizeZ);

        if (h0 < 0 || h1 < 0 || h2 < 0 || h3 < 0) {
            return -1;
        }

        int min = Math.min(Math.min(h0, h1), Math.min(h2, h3));
        int max = Math.max(Math.max(h0, h1), Math.max(h2, h3));

        if ((max - min) > heightDiffLimit) {
            return -1;
        }

        return (h0 + h1 + h2 + h3) / 4;
    }

    private static int getLavaSurfaceHeight(World world, int x, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 0, z);

        for (int y = 31; y < 100; y++) {
            pos.setY(y);
            Block block = world.getBlockState(pos).getBlock();
            pos.setY(y + 1);
            Block blockAbove = world.getBlockState(pos).getBlock();

            if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
                if (blockAbove == Blocks.AIR) {
                    return y;
                }
            }
        }
        return -1;
    }
}