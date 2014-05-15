package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;

public class Acceleration extends Vector2D {

	public Acceleration(Double ax, Double ay) {
		super(ax, ay);
	}

	public Acceleration(Integer ax, Integer ay) {
		super(ax, ay);
	}

	public Velocity generatedVelocity(Long elapsedTime) {
		return new Velocity(x * elapsedTime, y * elapsedTime);
	}

}
