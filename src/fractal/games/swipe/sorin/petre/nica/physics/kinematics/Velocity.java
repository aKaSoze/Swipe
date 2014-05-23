package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.Unit.DerivedUnitBuilder;

public class Velocity extends Vector2D<Velocity> {

	public Velocity(Double vx, Double vy, LengthUnit lengthUnit, TimeUnit timeUnit) {
		super(vx, vy);
		measureUnit = DerivedUnitBuilder.newUnit().proportionalTo(lengthUnit).inversProportionalTo(timeUnit).build();
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
		return new Displacement(getX() * elapsedTime, getY() * elapsedTime);
	}
}
