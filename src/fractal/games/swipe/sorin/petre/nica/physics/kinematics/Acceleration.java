package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.Unit.DerivedUnitBuilder;

public class Acceleration extends Vector2D<Acceleration> {

	public static final Acceleration	EARTH_SURFACE_GRAVITY	= new Acceleration(0.0, 9.8);

	public Acceleration(Double ax, Double ay, LengthUnit lengthUnit, TimeUnit timeUnit) {
		super(ax, ay);
		setMeasureUnit(DerivedUnitBuilder.newUnit().proportionalTo(lengthUnit).inversProportionalTo(timeUnit).inversProportionalTo(timeUnit).build());
	}

	public Acceleration(Double ax, Double ay) {
		this(ax, ay, LengthUnit.METER, TimeUnit.SECOND);
	}

	public Acceleration(Integer ax, Integer ay) {
		this(ax.doubleValue(), ay.doubleValue());
	}

	public Velocity generatedVelocity(Long elapsedTime) {
		return new Velocity(getX() * elapsedTime, getY() * elapsedTime);
	}

}
