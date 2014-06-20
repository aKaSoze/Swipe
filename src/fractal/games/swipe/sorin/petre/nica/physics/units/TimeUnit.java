package fractal.games.swipe.sorin.petre.nica.physics.units;

public class TimeUnit extends Unit<TimeUnit> {

	public static final TimeUnit	MILLISECOND	= new TimeUnit("ms", 0.001);
	public static final TimeUnit	SECOND		= new TimeUnit("s", 1.0);
	public static final TimeUnit	MINUTE		= new TimeUnit("min", 60.0);
	public static final TimeUnit	HOUR		= new TimeUnit("h", 3600.0);

	public TimeUnit(String symbol, Double magnitudeOrder) {
		super(symbol, magnitudeOrder, null, null, null);
		timeComponent = this;
	}

}
