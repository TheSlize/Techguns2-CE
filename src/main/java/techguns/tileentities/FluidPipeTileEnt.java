package techguns.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FluidPipeTileEnt extends TileEntity implements IFluidHandler, ITickable {

	public static final int TRANSFER_RATE = 250;
	public static final int MAX_NETWORK_SIZE = 2048;

	protected final IFluidTankProperties[] tankProperties = new IFluidTankProperties[]{new FluidTankProperties(null, TRANSFER_RATE, true, false)};

	protected long lastTransferTick = -1L;
	protected int transferredThisTick = 0;
	protected boolean busy = false;

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote || this.busy) {
			return;
		}

		for (EnumFacing facing : EnumFacing.VALUES) {
			int already = getTransferredThisTick();
			int budget = TRANSFER_RATE - already;
			if (budget <= 0) {
				return;
			}

			IFluidHandler source = getNeighbourHandler(facing);
			if (source == null) {
				continue;
			}

			FluidStack available = source.drain(budget, false);
			if (available == null || available.amount <= 0) {
				continue;
			}

			int sent;
			this.busy = true;
			try {
				sent = distribute(available, true, source);
			} finally {
				this.busy = false;
			}

			if (sent > 0) {
				source.drain(new FluidStack(available, sent), true);
				this.lastTransferTick = this.world.getTotalWorldTime();
				this.transferredThisTick = already + sent;
			}
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (this.world == null || this.busy || resource == null || resource.amount <= 0) {
			return 0;
		}

		int already = getTransferredThisTick();
		int budget = Math.min(resource.amount, TRANSFER_RATE - already);
		if (budget <= 0) {
			return 0;
		}

		int sent;
		this.busy = true;
		try {
			sent = distribute(new FluidStack(resource, budget), doFill, null);
		} finally {
			this.busy = false;
		}

		if (doFill && sent > 0) {
			this.lastTransferTick = this.world.getTotalWorldTime();
			this.transferredThisTick = already + sent;
		}
		return sent;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return this.tankProperties;
	}

	protected int getTransferredThisTick() {
		return this.world.getTotalWorldTime() == this.lastTransferTick ? this.transferredThisTick : 0;
	}

	@Nullable
	protected IFluidHandler getNeighbourHandler(EnumFacing facing) {
		BlockPos target = this.pos.offset(facing);
		if (!this.world.isBlockLoaded(target)) {
			return null;
		}
		TileEntity tile = this.world.getTileEntity(target);
		if (tile == null || tile.isInvalid() || tile instanceof FluidPipeTileEnt) {
			return null;
		}
		if (!tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
			return null;
		}
		return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
	}

	protected int distribute(FluidStack resource, boolean doFill, @Nullable IFluidHandler excluded) {
		List<IFluidHandler> targets = collectTargets(excluded);
		if (targets.isEmpty()) {
			return 0;
		}

		int remaining = resource.amount;
		while (!targets.isEmpty() && remaining > 0) {
			int share = Math.max(1, remaining / targets.size());
			int before = remaining;

			Iterator<IFluidHandler> it = targets.iterator();
			while (it.hasNext() && remaining > 0) {
				IFluidHandler target = it.next();
				int filled = target.fill(new FluidStack(resource, Math.min(share, remaining)), doFill);
				if (filled <= 0) {
					it.remove();
					continue;
				}
				remaining -= filled;
			}

			if (before == remaining) {
				break;
			}
		}
		return resource.amount - remaining;
	}

	protected List<IFluidHandler> collectTargets(@Nullable IFluidHandler excluded) {
		List<IFluidHandler> targets = new ArrayList<>();
		Set<IFluidHandler> found = Collections.newSetFromMap(new IdentityHashMap<>());
		Set<BlockPos> visited = new HashSet<>();
		Deque<BlockPos> queue = new ArrayDeque<>();

		if (excluded != null) {
			found.add(excluded);
		}
		visited.add(this.pos);
		queue.add(this.pos);

		while (!queue.isEmpty() && visited.size() < MAX_NETWORK_SIZE) {
			BlockPos current = queue.poll();

			for (EnumFacing facing : EnumFacing.VALUES) {
				BlockPos next = current.offset(facing);
				if (!this.world.isBlockLoaded(next)) {
					continue;
				}
				TileEntity tile = this.world.getTileEntity(next);
				if (tile == null || tile.isInvalid()) {
					continue;
				}
				if (tile instanceof FluidPipeTileEnt) {
					if (visited.add(next)) {
						queue.add(next);
					}
					continue;
				}
				if (!tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
					continue;
				}
				IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
				if (handler != null && found.add(handler)) {
					targets.add(handler);
				}
			}
		}
		return targets;
	}

	@Override
	public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				? CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this) : super.getCapability(capability, facing);
	}
}
