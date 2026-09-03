package techguns.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import techguns.Tags;
import techguns.items.armors.ICamoChangeable;
import techguns.tileentities.WindowFrameTileEnt;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTGWindowFrame<T extends Enum<T> & IStringSerializable> extends GenericBlock implements ICamoChangeable {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final IUnlistedProperty<Integer> PANE = new IUnlistedProperty<>() {
		@Override
		public String getName() {
			return "pane";
		}

		@Override
		public boolean isValid(Integer value) {
			return value != null && value >= 0;
		}

		@Override
		public Class<Integer> getType() {
			return Integer.class;
		}

		@Override
		public String valueToString(Integer value) {
			return value.toString();
		}
	};

	protected static final double D = 1.0D / 16.0D;

	protected static final AxisAlignedBB PLANE_X_AABB = new AxisAlignedBB(0.0D, 0.0D, 6 * D, 1.0D, 1.0D, 10 * D);
	protected static final AxisAlignedBB PLANE_Z_AABB = new AxisAlignedBB(6 * D, 0.0D, 0.0D, 10 * D, 1.0D, 1.0D);

	protected static final AxisAlignedBB[] PARTS_X_AABB = new AxisAlignedBB[]{
			new AxisAlignedBB(0.0D, 0.0D, 6 * D, 2 * D, 1.0D, 10 * D),
			new AxisAlignedBB(14 * D, 0.0D, 6 * D, 1.0D, 1.0D, 10 * D),
			new AxisAlignedBB(2 * D, 0.0D, 6 * D, 14 * D, 2 * D, 10 * D),
			new AxisAlignedBB(2 * D, 14 * D, 6 * D, 14 * D, 1.0D, 10 * D),
			new AxisAlignedBB(2 * D, 5 * D, 6 * D, 14 * D, 7 * D, 10 * D),
			new AxisAlignedBB(2 * D, 9 * D, 6 * D, 14 * D, 11 * D, 10 * D)};

	protected static final AxisAlignedBB[] PARTS_Z_AABB = new AxisAlignedBB[]{
			new AxisAlignedBB(6 * D, 0.0D, 0.0D, 10 * D, 1.0D, 2 * D),
			new AxisAlignedBB(6 * D, 0.0D, 14 * D, 10 * D, 1.0D, 1.0D),
			new AxisAlignedBB(6 * D, 0.0D, 2 * D, 10 * D, 2 * D, 14 * D),
			new AxisAlignedBB(6 * D, 14 * D, 2 * D, 10 * D, 1.0D, 14 * D),
			new AxisAlignedBB(6 * D, 5 * D, 2 * D, 10 * D, 7 * D, 14 * D),
			new AxisAlignedBB(6 * D, 9 * D, 2 * D, 10 * D, 11 * D, 14 * D)};

	protected static final AxisAlignedBB PANE_X_AABB = new AxisAlignedBB(0.0D, 0.0D, 7 * D, 1.0D, 1.0D, 9 * D);
	protected static final AxisAlignedBB PANE_Z_AABB = new AxisAlignedBB(7 * D, 0.0D, 0.0D, 9 * D, 1.0D, 1.0D);

	protected BlockStateContainer blockStateOverride;

	public PropertyEnum<T> TYPE;
	protected Class<T> clazz;
	protected GenericItemBlockMeta itemblock;
	protected String[] textures;

	public BlockTGWindowFrame(String name, Class<T> clazz) {
		super(name, Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setLightOpacity(0);
		this.clazz = clazz;
		this.TYPE = PropertyEnum.create("type", clazz);
		this.textures = new String[clazz.getEnumConstants().length];
		this.blockStateOverride = new ExtendedBlockState(this, new IProperty[]{TYPE, FACING}, new IUnlistedProperty[]{PANE});
		this.setDefaultState(this.getBlockState().getBaseState());
	}

	public BlockTGWindowFrame<T> setTextures(String... tex) {
		System.arraycopy(tex, 0, this.textures, 0, Math.min(tex.length, this.textures.length));
		return this;
	}

	public String[] getTextures() {
		return this.textures;
	}

	public Class<T> getTypeClass() {
		return this.clazz;
	}

	@Override
	public @NotNull BlockStateContainer getBlockState() {
		return this.blockStateOverride;
	}

	@Override
	public boolean hasTileEntity(@NotNull IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(@NotNull World world, @NotNull IBlockState state) {
		return new WindowFrameTileEnt();
	}

	@Override
	public void registerBlock(RegistryEvent.Register<Block> event) {
		super.registerBlock(event);
		GameRegistry.registerTileEntity(WindowFrameTileEnt.class, new ResourceLocation(Tags.MOD_ID, this.getRegistryName().getPath()));
	}

	protected boolean isXPlane(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		return facing.getAxis() == EnumFacing.Axis.Z;
	}

	@Override
	public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
		return isXPlane(state) ? PLANE_X_AABB : PLANE_Z_AABB;
	}

	@Override
	public void addCollisionBoxToList(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull AxisAlignedBB entityBox, @NotNull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		boolean xPlane = isXPlane(state);
		for (AxisAlignedBB box : (xPlane ? PARTS_X_AABB : PARTS_Z_AABB)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
		}
		if (getPaneIndex(worldIn, pos) > 0) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, xPlane ? PANE_X_AABB : PANE_Z_AABB);
		}
	}

	protected int getPaneIndex(IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return tile instanceof WindowFrameTileEnt ? TGWindowPanes.getIndex(((WindowFrameTileEnt) tile).getPane()) : 0;
	}

	@Override
	public @NotNull IBlockState getExtendedState(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
		if (state instanceof IExtendedBlockState) {
			return ((IExtendedBlockState) state).withProperty(PANE, getPaneIndex(world, pos));
		}
		return state;
	}

	@Override
	public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (!(tile instanceof WindowFrameTileEnt window)) return false;
        ItemStack held = playerIn.getHeldItem(hand);

		if (window.getPane().isEmpty()) {
			if (!TGWindowPanes.isPaneStack(held)) return false;
			if (!worldIn.isRemote) {
				ItemStack pane = held.copy();
				pane.setCount(1);
				window.setPane(pane);
				if (!playerIn.isCreative()) held.shrink(1);
			}
			worldIn.playSound(playerIn, pos, SoundType.GLASS.getPlaceSound(), SoundCategory.BLOCKS, 1.0f, 1.0f);
			return true;
		} else if (held.isEmpty()) {
			if (!worldIn.isRemote) {
				ItemStack pane = window.getPane();
				window.setPane(ItemStack.EMPTY);
				if (!playerIn.inventory.addItemStackToInventory(pane)) {
					playerIn.dropItem(pane, false);
				}
			}
			worldIn.playSound(playerIn, pos, SoundType.GLASS.getBreakSound(), SoundCategory.BLOCKS, 1.0f, 1.0f);
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof WindowFrameTileEnt) {
			ItemStack pane = ((WindowFrameTileEnt) tile).getPane();
			if (!pane.isEmpty()) spawnAsEntity(worldIn, pos, pane);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean isOpaqueCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(@NotNull IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRenderInLayer(@NotNull IBlockState state, @NotNull BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public @NotNull IBlockState getStateForPlacement(@NotNull World worldIn, @NotNull BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @NotNull EntityLivingBase placer) {
		IBlockState basetype = this.getStateFromMeta(meta);
		if (facing.getAxis().isHorizontal()) {
			return basetype.withProperty(FACING, facing);
		}
		return basetype.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(this.getDefaultState().withProperty(TYPE, state.getValue(TYPE)));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() << 2 | state.getValue(TYPE).ordinal();
	}

	@Override
	public @NotNull IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
				.withProperty(FACING, EnumFacing.byHorizontalIndex(meta >> 2))
				.withProperty(TYPE, clazz.getEnumConstants()[meta & 0b11]);
	}

	@Override
	public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess world, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing facing) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void getSubBlocks(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
		for (T t : clazz.getEnumConstants()) {
			items.add(new ItemStack(this, 1, this.getMetaFromState(getDefaultState().withProperty(TYPE, t))));
		}
	}

	@Override
	public @NotNull IBlockState withRotation(IBlockState state, Rotation rot) {
		EnumFacing facing = state.getValue(FACING);
		switch (rot) {
			case CLOCKWISE_180:
				return state.withProperty(FACING, facing.getOpposite());
			case CLOCKWISE_90:
				return state.withProperty(FACING, facing.rotateY());
			case COUNTERCLOCKWISE_90:
				return state.withProperty(FACING, facing.rotateYCCW());
			case NONE:
			default:
				return state;
		}
	}

	@Override
	public @NotNull IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}

	@Override
	public ItemBlock createItemBlock() {
		this.itemblock = new GenericItemBlockMeta(this);
		return this.itemblock;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerItemBlockModels() {
		for (int i = 0; i < clazz.getEnumConstants().length; i++) {
			IBlockState state = getDefaultState().withProperty(TYPE, clazz.getEnumConstants()[i]);
			ModelLoader.setCustomModelResourceLocation(this.itemblock, this.getMetaFromState(state), new ModelResourceLocation(getRegistryName(), "inventory_" + i));
		}
	}

	@Override
	public int getCamoCount() {
		return clazz.getEnumConstants().length - 1;
	}

	@Override
	public int getCurrentCamoIndex(ItemStack item) {
		return this.getStateFromMeta(item.getMetadata()).getValue(TYPE).ordinal();
	}

	@Override
	public String getCurrentCamoName(ItemStack item) {
		return Tags.MOD_ID + "." + this.getRegistryName().getPath() + ".camoname." + getCurrentCamoIndex(item);
	}

	@Override
	public void switchCamo(ItemStack item, boolean back) {
		IBlockState state = this.getStateFromMeta(item.getMetadata());

		int type = state.getValue(TYPE).ordinal();

		if (back) {
			type--;
			if (type < 0) {
				type = clazz.getEnumConstants().length - 1;
			}
		} else {
			type++;
			if (type >= clazz.getEnumConstants().length) {
				type = 0;
			}
		}
		item.setItemDamage(this.getMetaFromState(state.withProperty(TYPE, clazz.getEnumConstants()[type])));
	}

	@Override
	public int getFirstItemCamoDamageValue() {
		return this.getMetaFromState(getDefaultState().withProperty(TYPE, clazz.getEnumConstants()[0]));
	}
}
