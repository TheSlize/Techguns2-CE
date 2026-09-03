package techguns.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import techguns.TGBlocks;
import techguns.blocks.EnumDebugBlockType;
import techguns.util.BlockUtils;
import techguns.util.MBlock;
import techguns.world.dungeon.presets.specialblocks.SpecialBlockHandler;

public class Structure implements Serializable {

    private static final long serialVersionUID = 1L + 1;

    int sizeX;
    int sizeY;
    int sizeZ;
    List<MBlock> blocks = new ArrayList<>();
    HashMap<Integer, List<BlockEntry>> blockEntries = new HashMap<>();

    public void applySpecialMBlocks(ResourceLocation loottable) {
        SpecialBlockHandler.applySpecialMBlocks(blocks, loottable);
    }

    public Structure(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public static Structure scanBlocks(World world, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        Structure structure = new Structure(sizeX, sizeY, sizeZ);

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (int k = 0; k < sizeZ; k++) {
                    BlockPos bpos = new BlockPos(x + i, y + j, z + k);
                    IBlockState scanned = world.getBlockState(bpos);
                    EnumDebugBlockType marker = (scanned.getBlock() == TGBlocks.DEBUG_BLOCK) ? scanned.getValue(TGBlocks.DEBUG_BLOCK.TYPE) : null;

                    if (marker == EnumDebugBlockType.ANTIAIRMARKER) {
                        continue;
                    }

                    if (marker == EnumDebugBlockType.AIRMARKER || scanned.getBlock() == Blocks.STRUCTURE_VOID) {
                        scanned = Blocks.AIR.getDefaultState();
                    }

                    int layer = BlockData.getBlockLayer(scanned);
                    MBlock mblock = new MBlock(scanned);
                    int index = structure.blocks.indexOf(mblock);
                    if (index < 0) {
                        structure.blocks.add(mblock);
                        index = structure.blocks.indexOf(mblock);
                    }
                    if (!structure.blockEntries.containsKey(layer)) {
                        structure.blockEntries.put(layer, new ArrayList<>());
                    }
                    structure.blockEntries.get(layer).add(new BlockEntry(i, j, k, index));
                }
            }
        }

        return structure;
    }

    public void placeBlocks(World world, int x, int y, int z, int rotation) {
        //BlockPos axis = new BlockPos(x+sizeX*0.5, y, z+sizeZ*0.5); //TODO
        Vec3d axis = new Vec3d(x + sizeX * 0.5, y, z + sizeZ * 0.5);

        MutableBlockPos mpos = new MutableBlockPos();
        for (int i = BlockData.MIN_LAYER; i <= BlockData.MAX_LAYER; i++) {

            List<BlockEntry> blockList = blockEntries.get(i);
            if (blockList != null) {
                for (BlockEntry b : blockList) {
                    MBlock mb = blocks.get(b.blockIndex);
                    //BlockPos pos = new BlockPos(x+b.x, y+b.y, z+b.z);
                    mpos.setPos(x + b.x, y + b.y, z + b.z);

                    if (mpos.getY() >= 1) {

                        BlockUtils.rotateAroundY(mpos, axis, rotation);

                        IBlockState state = mb.getState(); //mb.block.getStateFromMeta(mb.meta);


                        state = BlockRotator.getRotatedHorizontal(state, rotation);

                        world.setBlockState(mpos, state, 2);

                        if (mb.hasTileEntity()) {
                            mb.tileEntityPostPlacementAction(world, state, mpos, rotation);
                        }
                        //AxisAlignedBB aabb = new AxisAlignedBB(pos1, axis);
                    }
                }
            }
        }
    }


    static class BlockEntry implements Serializable {

        private static final long serialVersionUID = 1L;

        int x;
        int y;
        int z;
        int blockIndex;

        public BlockEntry(int x, int y, int z, int blockIndex) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.blockIndex = blockIndex;
        }

    }
}
