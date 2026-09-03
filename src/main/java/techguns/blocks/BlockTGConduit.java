package techguns.blocks;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockTGConduit extends GenericBlock {

	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public static final PropertyBool UP = PropertyBool.create("up");
	public static final PropertyBool DOWN = PropertyBool.create("down");

	protected static final double MIN = 5 / 16d;
	protected static final double MAX = 11 / 16d;

	protected static final AxisAlignedBB CENTER_AABB = new AxisAlignedBB(MIN, MIN, MIN, MAX, MAX, MAX);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(MIN, MIN, 0d, MAX, MAX, MIN);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(MIN, MIN, MAX, MAX, MAX, 1d);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(MAX, MIN, MIN, 1d, MAX, MAX);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0d, MIN, MIN, MIN, MAX, MAX);
	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(MIN, MAX, MIN, MAX, 1d, MAX);
	protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(MIN, 0d, MIN, MAX, MIN, MAX);

	protected GenericItemBlock itemblock;

	public BlockTGConduit(String name, Material mat, MapColor mc, SoundType soundType) {
		super(name, mat, mc);
		this.setSoundType(soundType);
		this.setHardness(3.0f);
		this.setResistance(10.0f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false)
				.withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UP, false).withProperty(DOWN, false));
	}

	public abstract boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing);

	public abstract String getTransferRateInfo();

	@Override
	protected @NotNull BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

	@Override
	public @NotNull IBlockState getActualState(@NotNull IBlockState state, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
		return state.withProperty(NORTH, canConnectTo(worldIn, pos, EnumFacing.NORTH))
				.withProperty(EAST, canConnectTo(worldIn, pos, EnumFacing.EAST))
				.withProperty(SOUTH, canConnectTo(worldIn, pos, EnumFacing.SOUTH))
				.withProperty(WEST, canConnectTo(worldIn, pos, EnumFacing.WEST))
				.withProperty(UP, canConnectTo(worldIn, pos, EnumFacing.UP))
				.withProperty(DOWN, canConnectTo(worldIn, pos, EnumFacing.DOWN));
	}

	@Override
	public int getMetaFromState(@NotNull IBlockState state) {
		return 0;
	}

	@Override
	public @NotNull IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
		state = state.getActualState(source, pos);

		double minX = MIN;
		double minY = MIN;
		double minZ = MIN;
		double maxX = MAX;
		double maxY = MAX;
		double maxZ = MAX;

		if (state.getValue(NORTH)) {
			minZ = 0d;
		}
		if (state.getValue(SOUTH)) {
			maxZ = 1d;
		}
		if (state.getValue(WEST)) {
			minX = 0d;
		}
		if (state.getValue(EAST)) {
			maxX = 1d;
		}
		if (state.getValue(DOWN)) {
			minY = 0d;
		}
		if (state.getValue(UP)) {
			maxY = 1d;
		}

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public void addCollisionBoxToList(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos,
									  @NotNull AxisAlignedBB entityBox, @NotNull List<AxisAlignedBB> collidingBoxes,
									  @Nullable Entity entityIn, boolean isActualState) {
		if (!isActualState) {
			state = state.getActualState(worldIn, pos);
		}

		addCollisionBoxToList(pos, entityBox, collidingBoxes, CENTER_AABB);

		if (state.getValue(NORTH)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
		}
		if (state.getValue(SOUTH)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
		}
		if (state.getValue(EAST)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
		}
		if (state.getValue(WEST)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);
		}
		if (state.getValue(UP)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, UP_AABB);
		}
		if (state.getValue(DOWN)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
		}
	}

	@Override
	public boolean isOpaqueCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public @NotNull BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state,
													 @NotNull BlockPos pos, @NotNull EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean hasTileEntity(@NotNull IBlockState state) {
		return true;
	}

	@Override
	public ItemBlock createItemBlock() {
		this.itemblock = new GenericItemBlock(this) {
			@Override
			public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
				super.addInformation(stack, worldIn, tooltip, flagIn);
				tooltip.add(ChatFormatting.GRAY + BlockTGConduit.this.getTransferRateInfo());
			}
		};
		return this.itemblock;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerItemBlockModels() {
		ModelLoader.setCustomModelResourceLocation(this.itemblock, 0, new ModelResourceLocation(getRegistryName() + "_inventory", "inventory"));
	}
}
