package techguns.tileentities;

public class UraniumEnergyCableTileEnt extends EnergyCableTileEnt {

	public static final int URANIUM_TRANSFER_RATE = 20000;

	@Override
	public int getTransferRate() {
		return URANIUM_TRANSFER_RATE;
	}
}
