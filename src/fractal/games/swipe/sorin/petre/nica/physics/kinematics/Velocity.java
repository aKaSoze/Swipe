package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;

public class Velocity extends Vector2D<Velocity> {

	protected LengthUnit	lengthUnit	= LengthUnit.METER;
	protected TimeUnit		timeUnit	= TimeUnit.SECOND;

	public Velocity(Double vx, Double vy, LengthUnit lengthUnit, TimeUnit timeUnit) {
		this(vx, vy);
		this.lengthUnit = lengthUnit;
		this.timeUnit = timeUnit;
	}

	public Velocity(Double vx, Double vy) {
		super(vx, vy);
	}

	public Velocity(Integer vx, Integer vy) {
		super(vx, vy);
	}

	public Velocity(Vector2D vector) {
		super(vector.x, vector.y);
	}

	public Displacement generatedDisplacement(Long elapsedTime) {
		return new Displacement(x * elapsedTime, y * elapsedTime);
	}

	public Velocity add(Velocity augend) {
		Double lengthMultiplicand = augend.lengthUnit.magnitudeOrder / lengthUnit.magnitudeOrder;
		Double timeMultiplicand = timeUnit.magnitudeOrder / augend.timeUnit.magnitudeOrder;
		augend.multiplyByScalar(lengthMultiplicand * timeMultiplicand);
		return new Velocity(super.add(augend));
	}
}
