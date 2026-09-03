package techguns.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import techguns.TGConfig;

public class BlockDebugMarker extends GenericBlockMetaEnum<EnumDebugBlockType> {

	public BlockDebugMarker(String name, Material mat) {
		super(name, mat, EnumDebugBlockType.class);
	}

	@Override
	public @NotNull BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isFullBlock(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(@NotNull IBlockState state) {
		return false;
	}

	@Override
	public void getSubBlocks(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
		if(TGConfig.misc.debug) {
			items.add(new ItemStack(this,1,this.getMetaFromState(getDefaultState().withProperty(TYPE, EnumDebugBlockType.AIRMARKER))));
			items.add(new ItemStack(this,1,this.getMetaFromState(getDefaultState().withProperty(TYPE, EnumDebugBlockType.ANTIAIRMARKER))));
		}
	}
	
}
