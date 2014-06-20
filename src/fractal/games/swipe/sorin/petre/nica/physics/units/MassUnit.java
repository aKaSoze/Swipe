package fractal.games.swipe.sorin.petre.nica.physics.units;

public class MassUnit extends Unit<MassUnit> {

	public static final MassUnit	KILOGRAM	= new MassUnit("kg", 1.0);
	public static final MassUnit	GRAM		= new MassUnit("g", 0.001);

	public MassUnit(String symbol, Double magnitudeOrder) {
		super(symbol, magnitudeOrder, null, null, null);
		massComponent = this;
	}
}
