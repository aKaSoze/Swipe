package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Paint;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;

public abstract class AnimatedShape extends CenteredDrawable {

	protected Acceleration	acceleration;

	protected Velocity		velocity;

	protected Long			lastElapsedTime;

	public AnimatedShape(Displacement center, Paint paint) {
		super(center, paint);
		lastElapsedTime = 0L;
		acceleration = new Acceleration(0.0, 0.0);
		velocity = new Velocity(0, 0);
	}

	public void updateState(Long elapsedTime) {
		Long timeIncrement = elapsedTime - lastElapsedTime;
		getCenter().add(velocity.generatedDisplacement(timeIncrement));
		velocity.add(acceleration.generatedVelocity(timeIncrement));
		lastElapsedTime = elapsedTime;
	}
}
