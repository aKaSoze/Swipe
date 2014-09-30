package fractal.games.circus.sorin.petre.nica.physics.kinematics;

import fractal.games.circus.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.circus.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.circus.sorin.petre.nica.physics.units.TimeUnit;
import fractal.games.circus.sorin.petre.nica.physics.units.Unit.DerivedUnitBuilder;

public class Acceleration extends Vector2D<Acceleration> {

	public static final Acceleration	EARTH_SURFACE_GRAVITY	= new Acceleration(0.0, 9.8);

	public Acceleration(Double ax, Double ay, LengthUnit lengthUnit, TimeUnit timeUnit) {
		super(ax, ay, DerivedUnitBuilder.newUnit().proportionalTo(lengthUnit).inversProportionalTo(timeUnit).inversProportionalTo(timeUnit).build());
	}

	public Acceleration(Double ax, Double ay) {
		this(ax, ay, LengthUnit.METER, TimeUnit.SECOND);
	}

	public Acceleration(Integer ax, Integer ay) {
		this(ax.doubleValue(), ay.doubleValue());
	}

	public Velocity generatedVelocity(Long elapsedTime) {
		Double timeMagnitude = Math.sqrt(1 / getMeasureUnit().timeComponent.magnitudeOrder);
		Double timeInUnit = elapsedTime.doubleValue() * (TimeUnit.MILLISECOND.magnitudeOrder / timeMagnitude);
		TimeUnit velocityTimeUnit = new TimeUnit("t'", timeMagnitude);
		return new Velocity(x * timeInUnit, y * timeInUnit, getMeasureUnit().lengthComponent, velocityTimeUnit);
	}

}
