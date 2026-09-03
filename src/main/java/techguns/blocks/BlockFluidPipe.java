package techguns.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jetbrains.annotations.NotNull;
import techguns.Tags;
import techguns.util.TextUtil;
import techguns.tileentities.FluidPipeTileEnt;

public class BlockFluidPipe extends BlockTGConduit {

	public BlockFluidPipe(String name) {
		super(name, Material.IRON, MapColor.SILVER, SoundType.METAL);
		this.setHarvestLevel("pickaxe", 0);
	}

	@Override
	public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos.offset(facing));
		return tile != null && !tile.isInvalid()
				&& tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
	}

	@Override
	public String getTransferRateInfo() {
		return TextUtil.trans("techguns.tooltip.fluid_transfer_rate", FluidPipeTileEnt.TRANSFER_RATE);
	}

	@Override
	public void registerBlock(Register<Block> event) {
		super.registerBlock(event);
		GameRegistry.registerTileEntity(FluidPipeTileEnt.class, new ResourceLocation(Tags.MOD_ID, "fluid_pipe"));
	}

	@Override
	public TileEntity createTileEntity(@NotNull World world, @NotNull IBlockState state) {
		return new FluidPipeTileEnt();
	}
}
