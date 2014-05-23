package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;

public abstract class AnimatedShape extends CenteredDrawable {

	protected Acceleration	acceleration;

	protected Velocity		velocity;

	protected Displacement	displacement;

	protected Long			lastElapsedTime;

	public AnimatedShape(Point2D center) {
		super(center);
		lastElapsedTime = 0L;
		acceleration = new Acceleration(0.0, 0.0);
		velocity = new Velocity(0, 0);
		displacement = new Displacement(center.getX(), center.getY());
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(center.getX(), center.getY(), center.getX() + acceleration.getX().floatValue(), center.getY() + acceleration.getY().floatValue(), paint);
		canvas.drawLine(center.getX(), center.getY(), center.getX() + velocity.getX().floatValue(), center.getY() + velocity.getY().floatValue(), paint);
	}

	protected Point2D	center;

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
		displacement.add(velocity.generatedDisplacement(timeIncrement));
		velocity.add(acceleration.generatedVelocity(timeIncrement));
		setCenter(displacement.getTip());
		lastElapsedTime = elapsedTime;
	}
}
