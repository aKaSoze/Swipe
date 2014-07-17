package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Paint;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;

public abstract class AnimatedShape extends CenteredDrawable {

	public Acceleration					acceleration;

	public Velocity						velocity;

	protected Long						lastElapsedTime;

	private final Set<AnimatedShape>	obstacles	= new HashSet<AnimatedShape>();

	public AnimatedShape(LayoutProportions layoutProportions, Paint paint) {
		super(layoutProportions, paint);
		initState();
	}

	public AnimatedShape(LayoutProportions layoutProportions) {
		super(layoutProportions);
		initState();
	}

	private void initState() {
		lastElapsedTime = 0L;
		acceleration = new Acceleration(0.0, 0.0);
		velocity = new Velocity(0, 0);
	}

	public void updateState(Long elapsedTime) {
		Long timeIncrement = elapsedTime - lastElapsedTime;
		center.add(velocity.generatedDisplacement(timeIncrement));
		velocity.add(acceleration.generatedVelocity(timeIncrement));
		lastElapsedTime = elapsedTime;
	}

	public void onCollision(AnimatedShape obstacle) {
		velocity.neutralize();
	}

	public void addObstacle(AnimatedShape obstacle) {
		obstacles.add(obstacle);
		obstacle.obstacles.add(this);
	}

	protected AnimatedShape checkPossibleOverlap() {
		for (AnimatedShape obstacle : obstacles) {
			if (obstacle instanceof Rectangle) {
				if (intersects(obstacle)) {
					return obstacle;
				}
			}
		}
		return null;
	}

	protected abstract Boolean intersects(AnimatedShape obstacle);
}
