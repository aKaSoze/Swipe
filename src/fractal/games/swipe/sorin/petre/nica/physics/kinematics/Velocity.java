package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;

public class Velocity extends Vector2D {

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
		return new Velocity(super.add(augend));
	}

}
