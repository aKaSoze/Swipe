package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class PropulsionPlatform extends Painting {

	private enum Status {
		STANDING, STRECTHING, RELEASED;
	}

	public interface CollisionHandler {
		void onCollison();
	}

	public interface ImpactHandler {
		void onImpact(PropulsionPlatform propulsionPlatform);
	}

	private static final Double		ELASTICITY_COEFICIENT	= 0.02;
	private static final Double		PROJECTILE_MIN_SPEED	= 0.035;

	private Double					maxSpringDisplacement;

	private Displacement			strecthPoint			= new Displacement();

	private Status					status					= Status.STANDING;

	private Long					strecthingTime;

	private Long					elapsedTime;

	private Velocity				springVelocity			= new Velocity(0.0, 0.0);

	public MediaPlayer				boingSoundPlayer;

	public Set<CollisionHandler>	collisionHandlers		= new HashSet<PropulsionPlatform.CollisionHandler>();

	public Set<ImpactHandler>		impactHandlers			= new HashSet<PropulsionPlatform.ImpactHandler>();

	public PropulsionPlatform() {
		super();
	}

	public PropulsionPlatform(LayoutProportions layoutProportions) {
		super(layoutProportions, R.drawable.beam);
		init();
	}

	@Override
	public void init() {
		super.init();
		paint.setStrokeWidth(1);
	}

	@Override
	public void updateState(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
		if (status == Status.RELEASED) {
			if (strecthPoint.isZero()) {
				for (ImpactHandler impactHandler : impactHandlers) {
					impactHandler.onImpact(this);
				}
				springVelocity.neutralize();
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
	public void onCollision(AnimatedShape obstacle) {
		if (obstacle.velocity.magnitude() < PROJECTILE_MIN_SPEED) {
			obstacle.acceleration.neutralize();
			obstacle.velocity.neutralize();
			obstacle.center.x = center.x;
			obstacle.center.y = center.y + evalHalfHeight() + obstacle.evalHalfHeight();
		}
		for (CollisionHandler collisonHandler : collisionHandlers) {
			collisonHandler.onCollison();
		}
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		if (properties.contains(Property.CLONEABLE)) {
			PropulsionPlatform newPropulsionPlatform = clonePropulsionPlatform();
			newPropulsionPlatform.properties.addAll(properties);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		float strecthPointOriginX = strecthPoint.applyPoint.x.floatValue();
		float strecthPointOriginY = drawTranslation.y.floatValue() - strecthPoint.applyPoint.y.floatValue();

		float strecthPointX = strecthPoint.applyPoint.x.floatValue() + strecthPoint.x.floatValue();
		float strecthPointY = drawTranslation.y.floatValue() - strecthPoint.applyPoint.y.floatValue() - strecthPoint.y.floatValue();

		canvas.drawLine(strecthPointOriginX, strecthPointOriginY, strecthPointX, strecthPointY, paint);
		canvas.drawCircle(strecthPointX, strecthPointY, 10, paint);
		canvas.drawLine(strecthPointX, strecthPointY, evalLeftTopCorner().x.floatValue(), drawTranslation.y.floatValue() - evalLeftTopCorner().y.floatValue(), paint);
		canvas.drawLine(strecthPointX, strecthPointY, evalRightTopCorner().x.floatValue(), drawTranslation.y.floatValue() - evalRightTopCorner().y.floatValue(), paint);
	}

	public PropulsionPlatform clonePropulsionPlatform() {
		PropulsionPlatform newPropulsionPlatform = new PropulsionPlatform(layoutProportions);
		newPropulsionPlatform.properties.addAll(properties);
		return newPropulsionPlatform;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		strecthPoint.applyPoint = center;
		maxSpringDisplacement = evalWidth();
	}

	private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if (touchPoint.distanceTo(center) < VECINITY_DISTANCE) {
				status = Status.STRECTHING;
				strecthPoint.makeEqualTo(touchPoint.subtractionVector(center));
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (properties.contains(Property.MOVABLE)) {
				if (touchPoint.distanceTo(evalRightTopCorner()) < VECINITY_DISTANCE) {
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
			springVelocity.setComponents(-strecthPoint.x * ELASTICITY_COEFICIENT, -strecthPoint.y * ELASTICITY_COEFICIENT);
			break;
		case MotionEvent.ACTION_MOVE:
			Displacement rawStrecthPoint = touchPoint.subtractionVector(center);
			if (rawStrecthPoint.magnitude() > maxSpringDisplacement) {
				rawStrecthPoint.setMagnitude(maxSpringDisplacement);
			}
			if (rawStrecthPoint.y > 0) {
				rawStrecthPoint.y = 0.0;
			}
			strecthPoint.makeEqualTo(rawStrecthPoint);
			break;
		}
	}

	public Velocity getSpringVelocity() {
		return new Velocity(springVelocity.x, springVelocity.y);
	}

}
