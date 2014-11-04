package fractal.games.circus.sorin.petre.nica.physics.kinematics;

import fractal.games.circus.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.circus.sorin.petre.nica.physics.units.FundamentalMeasure;

public class Acceleration extends Vector2D<Acceleration> {

	public static final Acceleration	EARTH_SURFACE_GRAVITY	= new Acceleration(0.0, 9.8);

	public Acceleration(Double ax, Double ay) {
		super(ax, ay, FundamentalMeasure.Length.unitSymbol + "/" + FundamentalMeasure.Time.unitSymbol + "^2");
	}

	public Acceleration(Integer ax, Integer ay) {
		this(ax.doubleValue(), ay.doubleValue());
	}

	public Velocity generatedVelocity(Long elapsedTime) {
		return new Velocity(x * elapsedTime, y * elapsedTime);
	}

}
