package fractal.games.circus.sorin.petre.nica.physics.kinematics;

import fractal.games.circus.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.circus.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.circus.sorin.petre.nica.physics.units.TimeUnit;
import fractal.games.circus.sorin.petre.nica.physics.units.Unit.DerivedUnitBuilder;

public class Velocity extends Vector2D<Velocity> {

	public Velocity(Double vx, Double vy, LengthUnit lengthUnit, TimeUnit timeUnit) {
		super(vx, vy, DerivedUnitBuilder.newUnit().proportionalTo(lengthUnit).inversProportionalTo(timeUnit).build());
	}

	public Velocity(Double vx, Double vy) {
		this(vx, vy, LengthUnit.METER, TimeUnit.SECOND);
	}

	public Velocity(Integer vx, Integer vy) {
		this(vx.doubleValue(), vy.doubleValue());
	}

	public Velocity(Float vx, Float vy) {
		this(vx.doubleValue(), vy.doubleValue());
	}

	public Displacement generatedDisplacement(Long elapsedTime) {
		Double timeMagnitude = 1 / getMeasureUnit().timeComponent.magnitudeOrder;
		Double timeInUnit = elapsedTime.doubleValue() * (TimeUnit.MILLISECOND.magnitudeOrder / timeMagnitude);
		return new Displacement(x * timeInUnit, y * timeInUnit, getMeasureUnit().lengthComponent);
	}

}
