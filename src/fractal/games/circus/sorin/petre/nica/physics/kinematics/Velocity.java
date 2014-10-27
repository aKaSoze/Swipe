package fractal.games.circus.sorin.petre.nica.physics.kinematics;

import fractal.games.circus.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.circus.sorin.petre.nica.physics.units.FundamentalMeasure;

public class Velocity extends Vector2D<Velocity> {

	public Velocity(Double vx, Double vy) {
		super(vx, vy, FundamentalMeasure.Length.unitSymbol + "/" + FundamentalMeasure.Time.unitSymbol);
	}

	public Velocity(Integer vx, Integer vy) {
		this(vx.doubleValue(), vy.doubleValue());
	}

	public Velocity(Float vx, Float vy) {
		this(vx.doubleValue(), vy.doubleValue());
	}

	public Displacement generatedDisplacement(Long elapsedTime) {
		return new Displacement(x * elapsedTime, y * elapsedTime);
	}

}
