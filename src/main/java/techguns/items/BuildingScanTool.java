package techguns.items;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import techguns.TGBlocks;
import techguns.blocks.EnumDebugBlockType;
import techguns.util.BlockPosInd;
import techguns.util.EntityPosInd;
import techguns.util.MBlock;
import techguns.util.MEntity;
import techguns.world.dungeon.presets.specialblocks.MBlockBanner;
import techguns.world.dungeon.presets.specialblocks.MBlockItemFrame;
import techguns.world.dungeon.presets.specialblocks.MBlockSkull;
import techguns.world.dungeon.presets.specialentities.MEntityEndCrystal;
import techguns.world.dungeon.presets.specialentities.MEntityShulker;

public class BuildingScanTool extends GenericItem {

    public BuildingScanTool(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, @NotNull World world, BlockPos pos, @NotNull EnumHand hand,
                                               @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack item = player.getHeldItem(hand);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (!item.hasTagCompound()) {
            item.setTagCompound(new NBTTagCompound());
        }
        if (item.getTagCompound().hasKey("x1") && item.getTagCompound().hasKey("y1") && item.getTagCompound().hasKey("z1")) {

            if (world.isRemote)
                player.sendMessage(new TextComponentString("Position2 set (" + x + "/" + y + "/" + z + ")."));
            int x1 = item.getTagCompound().getInteger("x1");
            int y1 = item.getTagCompound().getInteger("y1");
            int z1 = item.getTagCompound().getInteger("z1");
            int sizeX = Math.abs(x1 - x) + 1;
            int sizeY = Math.abs(y1 - y) + 1;
            int sizeZ = Math.abs(z1 - z) + 1;

            if (world.isRemote)
                player.sendMessage(new TextComponentString("Size: (" + sizeX + "/" + sizeY + "/" + sizeZ + ")."));

            if (!world.isRemote) {
                scanStructure(world, Math.min(x, x1), Math.min(y, y1), Math.min(z, z1), sizeX, sizeY, sizeZ);
            }

            item.getTagCompound().removeTag("x1");
            item.getTagCompound().removeTag("y1");
            item.getTagCompound().removeTag("z1");

        } else {
            item.getTagCompound().setInteger("x1", x);
            item.getTagCompound().setInteger("y1", y);
            item.getTagCompound().setInteger("z1", z);
            if (world.isRemote)
                player.sendMessage(new TextComponentString("Position1 set (" + x + "/" + y + "/" + z + ")."));
        }

        return EnumActionResult.SUCCESS;
    }

    private MBlock createMBlockFromWorld(World world, BlockPos pos, IBlockState bs) {
        Block block = bs.getBlock();

        if (block == Blocks.SKULL) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntitySkull skull) {
                int skullType = skull.getSkullType();
                int rotation = skull.getSkullRotation();
                String playerUUID = null;
                String playerName = null;
                String textureValue = null;

                GameProfile profile = skull.getPlayerProfile();
                if (profile != null) {
                    if (profile.getId() != null) {
                        playerUUID = profile.getId().toString();
                    }
                    playerName = profile.getName();

                    Collection<Property> textures = profile.getProperties().get("textures");
                    if (textures != null && !textures.isEmpty()) {
                        Property textureProperty = textures.iterator().next();
                        textureValue = textureProperty.getValue();
                    }
                }

                return new MBlockSkull(block, block.getMetaFromState(bs), skullType, rotation, playerUUID, playerName, textureValue);
            }
        }

        if (block == Blocks.STANDING_BANNER || block == Blocks.WALL_BANNER) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBanner banner) {
                int baseColor = banner.baseColor.getDyeDamage();

                NBTTagCompound nbt = banner.writeToNBT(new NBTTagCompound());
                NBTTagList patterns = nbt.getTagList("Patterns", 10);

                return new MBlockBanner(block, block.getMetaFromState(bs), baseColor, patterns);
            }
        }

        return new MBlock(bs);
    }

    private MBlock createMBlockFromEntity(Entity entity) {
        if (entity instanceof EntityItemFrame frame) {
            ItemStack item = frame.getDisplayedItem();
            String itemName = null;
            int itemMeta = 0;
            int itemCount = 0;
            NBTTagCompound itemNBT = null;

            if (!item.isEmpty()) {
                itemName = item.getItem().getRegistryName().toString();
                itemMeta = item.getMetadata();
                itemCount = item.getCount();
                if (item.hasTagCompound()) {
                    itemNBT = item.getTagCompound().copy();
                }
            }

            return new MBlockItemFrame(
                    frame.facingDirection.getIndex(),
                    itemName,
                    itemMeta,
                    itemCount,
                    itemNBT.toString(),
                    frame.getRotation()
            );
        }

        return null;
    }

    private String getMBlockDefinition(MBlock mblock) {
        if (mblock instanceof MBlockSkull skull) {
            String uuidStr = skull.getPlayerUUID() != null ? "\"" + skull.getPlayerUUID() + "\"" : "null";
            String playerNameStr = skull.getPlayerName() != null ? "\"" + skull.getPlayerName() + "\"" : "null";
            String textureStr = skull.getTextureValue() != null ? "\"" + skull.getTextureValue() + "\"" : "null";
            return "blockList.add(new MBlockSkull(" + skull.getSkullType() + ", " + skull.getSkullRotation() + ", " + uuidStr + ", " + playerNameStr + ", " + textureStr + "));";
        }

        if (mblock instanceof MBlockBanner banner) {
            boolean standing = banner.block == Blocks.STANDING_BANNER;
            String patternsStr = nbtListToCode(banner.getPatterns());
            return "blockList.add(new MBlockBanner(" + standing + ", " + banner.meta + ", " + banner.getBaseColor() + ", " + patternsStr + "));";
        }

        if (mblock instanceof MBlockItemFrame frame) {
            String itemStr = frame.getItemRegistryName() != null ? "\"" + frame.getItemRegistryName() + "\"" : "null";
            String nbtStr = (frame.getItemNBT() != null && !frame.getItemNBT().isEmpty()) ? frame.getItemNBT().toString() : "null";
            if (frame.getItemNBT() != null) {
                return "blockList.add(new MBlockItemFrame(" + frame.getFacingIndex() + ", " + itemStr + ", "
                        + frame.getItemMeta() + ", " + frame.getItemCount() + ", " + nbtStr + ", " + frame.getItemRotation() + "));";
            }
            return "blockList.add(new MBlockItemFrame(" + frame.getFacingIndex() + ", " + itemStr + ", "
                    + frame.getItemMeta() + ", " + frame.getItemCount() + ", " + nbtStr + ", " + frame.getItemRotation() + "));";
        }

        return "blockList.add(new MBlock(\"" + mblock.block.getRegistryName() + "\", " + mblock.meta + "));";
    }

    private String nbtListToCode(NBTTagList patterns) {
        if (patterns == null || patterns.isEmpty()) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("MBlockBanner.createPatterns(");
        for (int i = 0; i < patterns.tagCount(); i++) {
            NBTTagCompound pattern = patterns.getCompoundTagAt(i);
            String patternId = pattern.getString("Pattern");
            int color = pattern.getInteger("Color");
            if (i > 0) sb.append(", ");
            sb.append("\"").append(patternId).append("\", ").append(color);
        }
        sb.append(")");
        return sb.toString();
    }

    private MEntity createMEntityFromEntity(Entity entity, int baseX, int baseY, int baseZ) {
        if (entity instanceof EntityEnderCrystal crystal) {
            double offX = entity.posX - Math.floor(entity.posX);
            double offY = entity.posY - Math.floor(entity.posY);
            double offZ = entity.posZ - Math.floor(entity.posZ);
            return new MEntityEndCrystal(offX, offY, offZ, crystal.shouldShowBottom());
        }

        if (entity instanceof EntityShulker shulker) {
            double offX = entity.posX - Math.floor(entity.posX);
            double offY = entity.posY - Math.floor(entity.posY);
            double offZ = entity.posZ - Math.floor(entity.posZ);

            NBTTagCompound nbt = new NBTTagCompound();
            shulker.writeEntityToNBT(nbt);
            EnumFacing attachFace = EnumFacing.byIndex(nbt.getByte("AttachFace"));

            return new MEntityShulker(offX, offY, offZ, attachFace);
        }

        return null;
    }

    private String getMEntityDefinition(MEntity mentity) {
        if (mentity instanceof MEntityEndCrystal crystal) {
            return "entityList.add(new MEntityEndCrystal(" + crystal.getOffsetX() + ", " + crystal.getOffsetY() + ", " + crystal.getOffsetZ() + ", " + crystal.isShowBottom() + "));";
        }

        if (mentity instanceof MEntityShulker shulker) {
            return "entityList.add(new MEntityShulker(" + shulker.getOffsetX() + ", " + shulker.getOffsetY() + ", " + shulker.getOffsetZ() + ", EnumFacing." + shulker.getAttachFace().name() + "));";
        }

        return "// Unknown entity type";
    }

    private void scanStructure(World world, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        StringBuilder sbDefBlocks = new StringBuilder();
        StringBuilder sbDefEntities = new StringBuilder();
        ArrayList<MBlock> blockList = new ArrayList<>();
        ArrayList<MEntity> entityList = new ArrayList<>();
        ArrayList<BlockPosInd> blockPosList = new ArrayList<>();
        ArrayList<EntityPosInd> entityPosList = new ArrayList<>();

        MutableBlockPos p = new MutableBlockPos();

        for (int ix = 0; ix < sizeX; ix++) {
            for (int iy = 0; iy < sizeY; iy++) {
                for (int iz = 0; iz < sizeZ; iz++) {

                    int coordX = x + ix;
                    int coordY = y + iy;
                    int coordZ = z + iz;

                    IBlockState bs = world.getBlockState(p.setPos(coordX, coordY, coordZ));
                    Block scanned = bs.getBlock();
                    EnumDebugBlockType marker = (scanned == TGBlocks.DEBUG_BLOCK) ? bs.getValue(TGBlocks.DEBUG_BLOCK.TYPE) : null;

                    if (marker == EnumDebugBlockType.ANTIAIRMARKER) {
                        continue;
                    }

                    if (marker == EnumDebugBlockType.AIRMARKER || scanned == Blocks.STRUCTURE_VOID) {
                        bs = Blocks.AIR.getDefaultState();
                    } else if ((bs == Blocks.AIR.getDefaultState()) || (bs == Blocks.DIRT.getDefaultState()) || (bs == Blocks.GRASS.getDefaultState())) {
                        continue;
                    }

                    MBlock mblock = createMBlockFromWorld(world, p, bs);
                    if (!blockList.contains(mblock)) {
                        blockList.add(mblock);
                        sbDefBlocks.append(getMBlockDefinition(mblock)).append("\n");
                    }
                    blockPosList.add(new BlockPosInd(ix, iy, iz, blockList.indexOf(mblock)));
                }
            }
        }

        AxisAlignedBB scanBox = new AxisAlignedBB(x, y, z, x + sizeX, y + sizeY, z + sizeZ);
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, scanBox);

        for (Entity entity : entities) {
            int ix = MathHelper.floor(entity.posX) - x;
            int iy = MathHelper.floor(entity.posY) - y;
            int iz = MathHelper.floor(entity.posZ) - z;

            if (ix >= 0 && ix < sizeX && iy >= 0 && iy < sizeY && iz >= 0 && iz < sizeZ) {
                MBlock mblock = createMBlockFromEntity(entity);
                if (mblock != null) {
                    if (!blockList.contains(mblock)) {
                        blockList.add(mblock);
                        sbDefBlocks.append(getMBlockDefinition(mblock)).append("\n");
                    }
                    blockPosList.add(new BlockPosInd(ix, iy, iz, blockList.indexOf(mblock)));
                } else {
                    MEntity mentity = createMEntityFromEntity(entity, x, y, z);
                    if (mentity != null) {
                        if (!entityList.contains(mentity)) {
                            entityList.add(mentity);
                            sbDefEntities.append(getMEntityDefinition(mentity)).append("\n");
                        }
                        entityPosList.add(new EntityPosInd(ix, iy, iz, entityList.indexOf(mentity)));
                    }
                }
            }
        }

        try {
            PrintWriter pw = new PrintWriter(new FileWriter("structure_scan.txt"));
            pw.println("// Block definitions:");
            pw.println(sbDefBlocks);
            pw.println("// Entity definitions:");
            pw.println(sbDefEntities);
            pw.println("---");
            pw.println();
            pw.println("// Block data:");
            pw.println(blockPosList.size());
            for (BlockPosInd bp : blockPosList) {
                pw.println(bp.toString());
            }
            pw.flush();
            pw.close();

            if (!entityPosList.isEmpty()) {
                PrintWriter pwEnt = new PrintWriter(new FileWriter("structure_scan_entities"));
                pwEnt.println(entityPosList.size());
                for (EntityPosInd ep : entityPosList) {
                    pwEnt.println(ep.toString());
                }
                pwEnt.flush();
                pwEnt.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}