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
		strecthPoint = new Displacement(0.0, 0.0);
		strecthPoint.applyPoint = center;

		elasticityCoeficient = 18.0;
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
			springVelocity = new Velocity(-strecthPoint.getX() * elasticityCoeficient, -strecthPoint.getY() * elasticityCoeficient, LengthUnit.PIXEL, TimeUnit.SECOND);
			break;
		case MotionEvent.ACTION_MOVE:
			Displacement rawStrecthPoint = touchPoint.subtractionVector(center);
			if (rawStrecthPoint.magnitude() > MAX_SPRING_DISPLACEMENT) {
				rawStrecthPoint.setMagnitude(MAX_SPRING_DISPLACEMENT);
			}
			if (rawStrecthPoint.getY() < 0) {
				rawStrecthPoint.setY(0.0);
			}
			strecthPoint.makeEqualTo(rawStrecthPoint);
			break;
		}
	}

	private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN && touchPoint.distanceTo(center) < 50 && touchPoint.getY() >= center.getY()) {
			status = Status.STRECTHING;
			projectile.velocity.neutralize();
			projectile.acceleration.neutralize();
			projectile.center = center.subtractionVector(new Displacement(0.0, projectile.evalHalfHeight() + evalHalfHeight()));
			strecthPoint.makeEqualTo(touchPoint.subtractionVector(center));
		}
	}

	@Override
	public void updateState(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
		if (status == Status.RELEASED) {
			if (strecthPoint.isZero()) {
				projectile.velocity = new Velocity(springVelocity.getX() / 4, springVelocity.getY() / 4, LengthUnit.PIXEL, TimeUnit.SECOND);
				projectile.acceleration = new Acceleration(0.0, 9.8, LengthUnit.METER, TimeUnit.SECOND);
				springVelocity.neutralize();
				boingSound.start();
				status = Status.STANDING;
			} else {
				Long elapsedStrecthingTime = elapsedTime - strecthingTime;
				strecthingTime = elapsedTime;

				Displacement displacement = springVelocity.generatedDisplacement(elapsedStrecthingTime);
				if (displacement.magnitude() > strecthPoint.magnitude()) {
					strecthPoint.neutralize();
				} else {
					strecthPoint.add(displacement);
				}
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Displacement springTip = strecthPoint.evaluateTip();
		strecthPoint.draw(canvas);
		canvas.drawCircle(springTip.getX().floatValue(), springTip.evaluateTip().getY().floatValue(), 10, paint);
		canvas.drawLine(springTip.getX().floatValue(), springTip.getY().floatValue(), evalLeftTopCorner().getX().floatValue(), evalLeftTopCorner().getY().floatValue(), paint);
		canvas.drawLine(springTip.getX().floatValue(), springTip.getY().floatValue(), evalRightTopCorner().getX().floatValue(), evalRightTopCorner().getY().floatValue(), paint);
	}

	@Override
	public void onCollision(AnimatedShape obstacle) {
		obstacle.velocity.divideYByScalar(-5.0);
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		// TODO Auto-generated method stub

	}

}
