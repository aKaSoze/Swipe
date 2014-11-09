package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import android.graphics.Paint;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public abstract class AnimatedShape extends CenteredDrawable {

	@Expose
	public Acceleration					acceleration;

	@Expose
	public Velocity						velocity;

	protected final Set<AnimatedShape>	obstacles	= new CopyOnWriteArraySet<AnimatedShape>();

	public AnimatedShape(LayoutProportions layoutProportions, Paint paint) {
		super(layoutProportions, paint);
		initState();
	}

	public AnimatedShape(LayoutProportions layoutProportions) {
		super(layoutProportions);
		initState();
	}

	private void initState() {
		acceleration = new Acceleration(0.0, 0.0);
		velocity = new Velocity(0, 0);
	}

	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);
		center.add(velocity.generatedDisplacement(timeIncrement));
		velocity.add(acceleration.generatedVelocity(timeIncrement));
	}

	public void onCollision(AnimatedShape obstacle) {
		velocity.neutralize();
	}

	public void addObstacle(AnimatedShape obstacle) {
		if (this != obstacle) {
			obstacles.add(obstacle);
			obstacle.obstacles.add(this);
		}
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

    protected abstract Boolean contains(Displacement point);
}
