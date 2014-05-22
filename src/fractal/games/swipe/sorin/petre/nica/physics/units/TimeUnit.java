package fractal.games.swipe.sorin.petre.nica.physics.units;

public class TimeUnit extends Unit<TimeUnit> {

	public static final TimeUnit	SECOND	= new TimeUnit("s", 1.0);

	public TimeUnit(String symbol, Double magnitudeOrder) {
		super(symbol, magnitudeOrder);
	}

}
