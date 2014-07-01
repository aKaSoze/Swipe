package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;

public class PropulsionPlatform extends Rectangle {

	private static final Double	MAX_SPRING_DISPLACEMENT	= 200.0;

	private enum Status {
		STANDING, STRECTHING, RELEASED;
	}

	private Displacement	strecthPoint;

	private Double			elasticityCoeficient;

	private Status			status;

	private Long			strecthingTime;

	private Long			elapsedTime;

	private Velocity		springVelocity;

	public final Rectangle	projectile;

	public MediaPlayer		boingSound;

	public PropulsionPlatform(Displacement center, Double width, Double height, Rectangle projectile) {
		super(center, width, height);
		this.projectile = projectile;
		projectile.obstacles.add(this);
		strecthPoint = center.cloneVector();

		elasticityCoeficient = 21.0;
		status = Status.STANDING;

		paint.setStrokeWidth(3);
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		switch (status) {
		case STANDING:
			handleStandingMotionEvent(motionEvent, touchPoint);
			break;
		case STRECTHING:
			handleStrecthingMotionEvent(motionEvent, touchPoint);
			break;
		default:
			break;
		}
	}

	private void handleStrecthingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_UP:
			status = Status.RELEASED;
			strecthingTime = elapsedTime;
			Displacement springDisplacement = strecthPoint.subtractionVector(getCenter());
			springVelocity = new Velocity(springDisplacement.getX() * elasticityCoeficient, springDisplacement.getY() * elasticityCoeficient, LengthUnit.PIXEL, TimeUnit.SECOND);
			break;
		case MotionEvent.ACTION_MOVE:
			strecthPoint.makeEqualTo(touchPoint);
			if (strecthPoint.distanceTo(getCenter()) > MAX_SPRING_DISPLACEMENT) {
				strecthPoint.setMagnitude(MAX_SPRING_DISPLACEMENT);
			}
			if (strecthPoint.getY() < getCenter().getY()) {
				strecthPoint.setY(getCenter().getY());
			}
			break;
		}
	}

	private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN && touchPoint.distanceTo(getCenter()) < 20 && touchPoint.getY() >= getCenter().getY()) {
			status = Status.STRECTHING;
			strecthPoint.makeEqualTo(touchPoint);
		}
	}

	@Override
	public void updateState(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
		if (status == Status.RELEASED) {
			if (strecthPoint.distanceTo(getCenter()) < 0.01) {
				projectile.velocity = new Velocity(springVelocity.getX() / 4, springVelocity.getY() / 4, LengthUnit.PIXEL, TimeUnit.SECOND);
				projectile.acceleration = new Acceleration(0.0, 9.8, LengthUnit.METER, TimeUnit.SECOND);
				springVelocity.neutralize();
				boingSound.start();
				status = Status.STANDING;
			} else {
				Long elapsedStrecthingTime = elapsedTime - strecthingTime;
				strecthingTime = elapsedTime;

				Double distanceToSegmentMid = strecthPoint.distanceTo(getCenter());
				Displacement displacement = springVelocity.generatedDisplacement(elapsedStrecthingTime);
				if (displacement.magnitude() > distanceToSegmentMid) {
					displacement = strecthPoint.subtractionVector(getCenter());
				}
				strecthPoint.add(displacement);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawCircle(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), 10, paint);
		canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), evaluateLeftTopCorner().getX().floatValue(), evaluateLeftTopCorner().getY().floatValue(), paint);
		canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), evaluateRightTopCorner().getX().floatValue(), evaluateRightTopCorner().getY().floatValue(), paint);
		canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), getCenter().getX().floatValue(), getCenter().getY().floatValue(), paint);
	}

	@Override
	public void onCollision(AnimatedShape obstacle) {
		super.onCollision(obstacle);
		obstacle.velocity.divideYByScalar(-5.0);
		obstacle.velocity.setY(getCenter().getX() - obstacle.getCenter().getX());
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		// TODO Auto-generated method stub

	}

}
