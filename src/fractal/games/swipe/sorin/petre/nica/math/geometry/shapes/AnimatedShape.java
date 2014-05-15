package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;

public abstract class AnimatedShape {

	protected Point2D		center;

	protected Acceleration	acceleration;

	protected Velocity		velocity;

	protected Displacement	displacement;

	protected Long			lastElapsedTime;

	public AnimatedShape(Point2D center) {
		this.center = center;
		acceleration = new Acceleration(0, 0);
		velocity = new Velocity(0, 0);
	}

	public void react(MotionEvent motionEvent) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_MOVE:
			setCenter(Point2D.Factory.fromMotionEvent(motionEvent));
		}
	}

	public void setCenter(Point2D newCenter) {
		center = newCenter;
	}

	public Float distanceTo(Point2D point2d) {
		return center.distanceTo(point2d);
	}

	public void updateState(Long elapsedTime) {
		Long timeIncrement = elapsedTime - lastElapsedTime;
		Displacement addedDisplacement = velocity.generatedDisplacement(timeIncrement);
		displacement = displacement.add(addedDisplacement);
		setCenter(new Point2D(displacement.x, displacement.y));
		Velocity addedVelocity = acceleration.generatedVelocity(timeIncrement);
		velocity = velocity.add(addedVelocity);
		lastElapsedTime = elapsedTime;
	}

	public abstract void draw(Canvas canvas);
}
