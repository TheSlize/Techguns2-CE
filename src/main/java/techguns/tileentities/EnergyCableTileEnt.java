package techguns.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
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

public class EnergyCableTileEnt extends TileEntity implements IEnergyStorage, ITickable {

	public static final int TRANSFER_RATE = 2000;
	public static final int MAX_NETWORK_SIZE = 2048;

	protected long lastTransferTick = -1L;
	protected int transferredThisTick = 0;
	protected boolean busy = false;

	public int getTransferRate() {
		return TRANSFER_RATE;
	}

	@Override
	public void update() {
		if (this.world == null || this.world.isRemote || this.busy) {
			return;
		}

		for (EnumFacing facing : EnumFacing.VALUES) {
			int already = getTransferredThisTick();
			int budget = getTransferRate() - already;
			if (budget <= 0) {
				return;
			}

			IEnergyStorage source = getNeighbourStorage(facing);
			if (source == null || !source.canExtract()) {
				continue;
			}

			int available = source.extractEnergy(budget, true);
			if (available <= 0) {
				continue;
			}

			int sent;
			this.busy = true;
			try {
				sent = distribute(available, false, source);
			} finally {
				this.busy = false;
			}

			if (sent > 0) {
				source.extractEnergy(sent, false);
				this.lastTransferTick = this.world.getTotalWorldTime();
				this.transferredThisTick = already + sent;
			}
		}
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (this.world == null || this.busy || maxReceive <= 0) {
			return 0;
		}

		int already = getTransferredThisTick();
		int budget = Math.min(maxReceive, getTransferRate() - already);
		if (budget <= 0) {
			return 0;
		}

		int sent;
		this.busy = true;
		try {
			sent = distribute(budget, simulate, null);
		} finally {
			this.busy = false;
		}

		if (!simulate && sent > 0) {
			this.lastTransferTick = this.world.getTotalWorldTime();
			this.transferredThisTick = already + sent;
		}
		return sent;
	}

	protected int getTransferredThisTick() {
		return this.world.getTotalWorldTime() == this.lastTransferTick ? this.transferredThisTick : 0;
	}

	@Nullable
	protected IEnergyStorage getNeighbourStorage(EnumFacing facing) {
		BlockPos target = this.pos.offset(facing);
		if (!this.world.isBlockLoaded(target)) {
			return null;
		}
		TileEntity tile = this.world.getTileEntity(target);
		if (tile == null || tile.isInvalid() || tile instanceof EnergyCableTileEnt) {
			return null;
		}
		if (!tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
			return null;
		}
		return tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
	}

	protected int distribute(int amount, boolean simulate, @Nullable IEnergyStorage excluded) {
		List<IEnergyStorage> targets = collectTargets(excluded);
		if (targets.isEmpty()) {
			return 0;
		}

		int remaining = amount;
		while (!targets.isEmpty() && remaining > 0) {
			int share = Math.max(1, remaining / targets.size());
			int before = remaining;

			Iterator<IEnergyStorage> it = targets.iterator();
			while (it.hasNext() && remaining > 0) {
				IEnergyStorage target = it.next();
				int accepted = target.receiveEnergy(Math.min(share, remaining), simulate);
				if (accepted <= 0) {
					it.remove();
					continue;
				}
				remaining -= accepted;
			}

			if (before == remaining) {
				break;
			}
		}
		return amount - remaining;
	}

	protected List<IEnergyStorage> collectTargets(@Nullable IEnergyStorage excluded) {
		List<IEnergyStorage> targets = new ArrayList<>();
		Set<IEnergyStorage> found = Collections.newSetFromMap(new IdentityHashMap<>());
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
				if (tile instanceof EnergyCableTileEnt) {
					if (visited.add(next)) {
						queue.add(next);
					}
					continue;
				}
				if (!tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
					continue;
				}
				IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				if (storage != null && storage.canReceive() && found.add(storage)) {
					targets.add(storage);
				}
			}
		}
		return targets;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return 0;
	}

	@Override
	public int getMaxEnergyStored() {
		return getTransferRate();
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	@Override
	public boolean hasCapability(@NotNull Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@NotNull Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this) : super.getCapability(capability, facing);
	}
}
