package techguns.blocks;

import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

public enum EnumDebugBlockType implements IStringSerializable {
	AIRMARKER,
	ANTIAIRMARKER;

	@Override
	public @NotNull String getName() {
		return name().toLowerCase();
	}
}
