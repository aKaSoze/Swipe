package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class PropulsionPlatform extends Painting {

	private enum Status {
		STANDING, STRECTHING, RELEASED;
	}

	public interface CollisionHandler {
		void onCollison();
	}

	private static final Double		ELASTICITY_COEFICIENT	= 18.0;

	private Double					maxSpringDisplacement;

	private Displacement			strecthPoint			= new Displacement(0.0, 0.0);

	private Status					status					= Status.STANDING;

	private Long					strecthingTime;

	private Long					elapsedTime;

	private Velocity				springVelocity			= new Velocity(0.0, 0.0, LengthUnit.PIXEL, TimeUnit.SECOND);

	public final AnimatedShape		projectile;

	public MediaPlayer				boingSoundPlayer;

	public Set<CollisionHandler>	collisionHandlers		= new HashSet<PropulsionPlatform.CollisionHandler>();

	private final Context			context;

	public PropulsionPlatform(Context context, LayoutProportions layoutProportions, AnimatedShape projectile) {
		super(layoutProportions, BitmapFactory.decodeResource(context.getResources(), R.drawable.beam));
		this.context = context;
		this.boingSoundPlayer = MediaPlayer.create(context, R.raw.boing);
		this.projectile = projectile;
		projectile.addObstacle(this);
		projectile.velocity = new Velocity(0.0, 0.0, LengthUnit.PIXEL, TimeUnit.SECOND);
		projectile.acceleration = new Acceleration(0.0, 0.0, LengthUnit.METER, TimeUnit.SECOND);
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

	@Override
	public void updateState(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
		if (status == Status.RELEASED) {
			if (strecthPoint.isZero()) {
				projectile.velocity.setComponents(springVelocity.x / 4, springVelocity.y / 4);
				projectile.acceleration.y = -9.8;
				springVelocity.neutralize();
				status = Status.STANDING;
				boingSoundPlayer.start();
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
	public void onCollision(AnimatedShape obstacle) {
		if (obstacle.velocity.magnitude() < 20) {
			obstacle.acceleration.neutralize();
			obstacle.velocity.neutralize();
		}
		for (CollisionHandler collisonHandler : collisionHandlers) {
			collisonHandler.onCollison();
		}
	}

	public PropulsionPlatform clonePropulsionPlatform() {
		PropulsionPlatform newPropulsionPlatform = new PropulsionPlatform(context, layoutProportions, projectile);
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

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		strecthPoint.applyPoint = center;
		maxSpringDisplacement = evalWidth();
		projectile.center.setComponents(center.x, center.y + projectile.evalHalfHeight() + evalHalfHeight());
	}

	private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if (touchPoint.distanceTo(center) < VECINITY_DISTANCE) {
				status = Status.STRECTHING;
				projectile.velocity.neutralize();
				projectile.acceleration.neutralize();
				projectile.center.setComponents(center.x, center.y + projectile.evalHalfHeight() + evalHalfHeight());
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

}
