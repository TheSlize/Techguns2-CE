package techguns.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import techguns.tileentities.EnergyCableTileEnt;
import techguns.tileentities.UraniumEnergyCableTileEnt;

public class BlockUraniumEnergyCable extends BlockEnergyCable {

	public BlockUraniumEnergyCable(String name) {
		super(name);
	}

	@Override
	protected int getTransferRate() {
		return UraniumEnergyCableTileEnt.URANIUM_TRANSFER_RATE;
	}

	@Override
	protected Class<? extends EnergyCableTileEnt> getTileEntityClass() {
		return UraniumEnergyCableTileEnt.class;
	}

	@Override
	public TileEntity createTileEntity(@NotNull World world, @NotNull IBlockState state) {
		return new UraniumEnergyCableTileEnt();
	}
}
