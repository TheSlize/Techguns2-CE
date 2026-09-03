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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jetbrains.annotations.NotNull;
import techguns.Tags;
import techguns.util.TextUtil;
import techguns.tileentities.EnergyCableTileEnt;

public class BlockEnergyCable extends BlockTGConduit {

	public BlockEnergyCable(String name) {
		super(name, Material.IRON, MapColor.GRAY, SoundType.CLOTH);
		this.setHarvestLevel("pickaxe", 0);
	}

	@Override
	public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos.offset(facing));
		return tile != null && !tile.isInvalid() && tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
	}

	@Override
	public String getTransferRateInfo() {
		return TextUtil.trans("techguns.tooltip.energy_transfer_rate", getTransferRate());
	}

	protected int getTransferRate() {
		return EnergyCableTileEnt.TRANSFER_RATE;
	}

	@Override
	public void registerBlock(Register<Block> event) {
		super.registerBlock(event);
		GameRegistry.registerTileEntity(getTileEntityClass(), new ResourceLocation(Tags.MOD_ID, this.getRegistryName().getPath()));
	}

	protected Class<? extends EnergyCableTileEnt> getTileEntityClass() {
		return EnergyCableTileEnt.class;
	}

	@Override
	public TileEntity createTileEntity(@NotNull World world, @NotNull IBlockState state) {
		return new EnergyCableTileEnt();
	}
}
