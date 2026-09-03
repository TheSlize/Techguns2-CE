package techguns.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import org.jetbrains.annotations.NotNull;

public class WindowFrameTileEnt extends BasicTGTileEntity {

	protected ItemStack pane = ItemStack.EMPTY;

	public WindowFrameTileEnt() {
		super(false);
	}

	public ItemStack getPane() {
		return this.pane;
	}

	public void setPane(ItemStack pane) {
		this.pane = pane == null ? ItemStack.EMPTY : pane;
		if (this.world != null && !this.world.isRemote) {
			this.needUpdate();
			IBlockState state = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(this.pos, state, state, 3);
		}
	}

	@Override
	public void writeClientDataToNBT(NBTTagCompound tags) {
		super.writeClientDataToNBT(tags);
		if (!this.pane.isEmpty()) {
			tags.setTag("pane", this.pane.writeToNBT(new NBTTagCompound()));
		} else {
			tags.removeTag("pane");
		}
	}

	@Override
	public void readClientDataFromNBT(NBTTagCompound tags) {
		super.readClientDataFromNBT(tags);
		this.pane = tags.hasKey("pane") ? new ItemStack(tags.getCompoundTag("pane")) : ItemStack.EMPTY;
	}

	@Override
	public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.markForRenderUpdate();
	}

	@Override
	public void handleUpdateTag(@NotNull NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.markForRenderUpdate();
	}

	protected void markForRenderUpdate() {
		if (this.world != null && this.world.isRemote) {
			this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		}
	}
}
