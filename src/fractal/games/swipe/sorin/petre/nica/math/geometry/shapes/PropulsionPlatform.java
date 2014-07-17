package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Rect;
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

	public MediaPlayer		boingSoundPlayer;

	public PropulsionPlatform(LayoutProportions layoutProportions, Rectangle projectile, MediaPlayer boingSoundPlayer) {
		super(layoutProportions);
		this.projectile = projectile;
		this.boingSoundPlayer = boingSoundPlayer;

		projectile.addObstacle(this);
		strecthPoint = new Displacement(0.0, 0.0);
		elasticityCoeficient = 18.0;
		status = Status.STANDING;

		paint.setStrokeWidth(3);
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		strecthPoint.applyPoint = center;
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

	@Override
	public void updateState(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
		if (status == Status.RELEASED) {
			if (strecthPoint.isZero()) {
				projectile.velocity = new Velocity(springVelocity.getX() / 4, springVelocity.getY() / 4, LengthUnit.PIXEL, TimeUnit.SECOND);
				projectile.acceleration = new Acceleration(0.0, 9.8, LengthUnit.METER, TimeUnit.SECOND);
				springVelocity.neutralize();
				boingSoundPlayer.start();
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
		canvas.drawCircle(springTip.getX().floatValue(), springTip.getY().floatValue(), 10, paint);
		canvas.drawLine(springTip.getX().floatValue(), springTip.getY().floatValue(), evalLeftTopCorner().getX().floatValue(), evalLeftTopCorner().getY().floatValue(), paint);
		canvas.drawLine(springTip.getX().floatValue(), springTip.getY().floatValue(), evalRightTopCorner().getX().floatValue(), evalRightTopCorner().getY().floatValue(), paint);
	}

	@Override
	public void onCollision(AnimatedShape obstacle) {
		// Score.instance.addPoints(3L);
	}

	public PropulsionPlatform clonePropulsionPlatform() {
		PropulsionPlatform newPropulsionPlatform = new PropulsionPlatform(layoutProportions, projectile, boingSoundPlayer);
		newPropulsionPlatform.properties.addAll(properties);
		return newPropulsionPlatform;
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		if (properties.contains(Property.CLONEABLE)) {
			PropulsionPlatform newPropulsionPlatform = clonePropulsionPlatform();
			newPropulsionPlatform.properties.addAll(properties);
			projectile.addObstacle(newPropulsionPlatform);
		}
	}

	private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if (touchPoint.distanceTo(center) < 35) {
				status = Status.STRECTHING;
				projectile.velocity.neutralize();
				projectile.acceleration.neutralize();
				projectile.center = center.subtractionVector(new Displacement(0.0, projectile.evalHalfHeight() + evalHalfHeight()));
				strecthPoint.makeEqualTo(touchPoint.subtractionVector(center));
			}
			if (properties.contains(Property.CLONEABLE) && touchPoint.distanceTo(evalLeftTopCorner()) < 20) {
				PropulsionPlatform newPropulsionPlatform = clonePropulsionPlatform();
				projectile.addObstacle(newPropulsionPlatform);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (properties.contains(Property.MOVABLE)) {
				if (touchPoint.distanceTo(evalRightTopCorner()) < 50) {
					setRightTopCorner(touchPoint);
				}
			}
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

}
