package fractal.games.swipe.sorin.petre.nica.physics.units;

public class LengthUnit extends Unit<LengthUnit> {

	public static final LengthUnit	CENTIETER	= new LengthUnit("cm", 0.01);
	public static final LengthUnit	METER		= new LengthUnit("m", 1.0);
	public static final LengthUnit	KILOMETER	= new LengthUnit("km", 1000.0);
	public static final LengthUnit	PIXEL		= new LengthUnit("pix", 2.0);

	public LengthUnit(String symbol, Double magnitudeOrder) {
		super(symbol, magnitudeOrder, null, null, null);
		lengthComponent = this;
	}

}
