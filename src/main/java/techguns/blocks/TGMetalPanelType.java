package techguns.blocks;

import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

public enum TGMetalPanelType implements IStringSerializable{
	CONTAINER_RED,
	CONTAINER_GREEN,
	CONTAINER_BLUE,
	CONTAINER_ORANGE,
	PANEL_LARGE_BORDER,
	STEELFRAME_BLUE,
	STEELFRAME_DARK,
	STEELFRAME_SCAFFOLD,
	CONTAINER_SCIFI,
	CONTAINER_SCIFI_RUSTY;

	@Override
	public @NotNull String getName() {
		return name().toLowerCase();
	}

	public boolean isEnabled() {
		return true;
	}
}