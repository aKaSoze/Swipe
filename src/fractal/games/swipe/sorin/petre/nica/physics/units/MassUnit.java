package fractal.games.swipe.sorin.petre.nica.physics.units;

public class MassUnit extends Unit<MassUnit> {

	public static final MassUnit	KILOGRAM	= new MassUnit("kg", 1.0);

	public MassUnit(String symbol, Double magnitudeOrder) {
		super(symbol, magnitudeOrder);
	}
}
