package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.GameView;

public class Rectangle extends AnimatedShape {

	private static final double	COLLISION_SPEED_LOSS	= 2.0;

	public enum Property {
		MOVABLE, CLONEABLE;
	}

	public GameView					scene;

	private Double					width;
	private Double					height;

	private Boolean					isFilled					= false;

	private Bitmap					bitmap;

	public final Set<AnimatedShape>	obstacles					= new HashSet<AnimatedShape>();

	private final Set<Displacement>	displacementsToObstacles	= new HashSet<Displacement>();

	public final Set<Property>		properties					= new HashSet<Property>();

	public Rectangle(Displacement center, Double width, Double height, Paint paint) {
		super(center, paint);
		initState(width, height);
	}

	public Rectangle(Displacement center, Double width, Double height) {
		super(center);
		initState(width, height);
	}

	public Rectangle(Displacement center, Integer width, Integer height) {
		this(center, width.doubleValue(), height.doubleValue());
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = Bitmap.createScaledBitmap(bitmap, width.intValue(), height.intValue(), true);
	}

	public Boolean isFilled() {
		return isFilled;
	}

	public void setFilled(Boolean isFilled) {
		paint.setStyle(isFilled ? Style.FILL : Style.STROKE);
		this.isFilled = isFilled;
	}

	public Displacement evalLeftTopCorner() {
		return new Displacement(center.getX() - evalHalfWidth(), center.getY() - evalHalfHeight());
	}

	public Displacement evalRightTopCorner() {
		return new Displacement(center.getX() + evalHalfWidth(), center.getY() - evalHalfHeight());
	}

	public Double evalHalfWidth() {
		return width / 2;
	}

	public Double evalHalfHeight() {
		return height / 2;
	}

	public Boolean intersects(Rectangle other) {
		Double dx = Math.abs(center.getX() - other.center.getX()) - (evalHalfWidth() + other.evalHalfWidth());
		Double dy = Math.abs(center.getY() - other.center.getY()) - (evalHalfHeight() + other.evalHalfHeight());
		return dx < 0 && dy < 0;
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);

		if (boundingBoxRight != null) {
			if (crossedLeftSideBoundry()) {
				moveToLeftSideBoundry();
				reverseVelocityAlongX();
			}
			if (crossedRightSideBoundry()) {
				moveToRightSideBoundry();
				reverseVelocityAlongX();
			}
		}

		AnimatedShape colidedObstacle = checkPossibleOverlap();
		if (colidedObstacle != null) {
			moveOutsideBoundriesOfObstacle((Rectangle) colidedObstacle);
			onCollision(colidedObstacle);
			colidedObstacle.onCollision(this);
		}
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		super.onMotionEvent(motionEvent, touchPoint);

		if (properties.contains(Property.MOVABLE)) {
			switch (motionEvent.getActionMasked()) {
			case MotionEvent.ACTION_MOVE:
				if (touchPoint.distanceTo(center) < 50) {
					center.makeEqualTo(touchPoint);
				}
				break;
			}
		}
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		if (properties.contains(Property.CLONEABLE)) {
			Rectangle newRectangle = new Rectangle(new Displacement(evalHalfWidth(), evalHalfHeight()), width, height);
			newRectangle.properties.addAll(properties);
			newRectangle.scene = scene;
			scene.drawables.add(newRectangle);
			scene.hippo.obstacles.add(newRectangle);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		paint.setColor(Color.WHITE);
		if (bitmap == null) {
			canvas.drawRect(center.getX().floatValue() - (width.floatValue() / 2), center.getY().floatValue() - (height.floatValue() / 2), center.getX().floatValue() + (width.floatValue() / 2), center.getY()
					.floatValue() + (height.floatValue() / 2), paint);
		} else {
			canvas.drawBitmap(bitmap, center.getX().floatValue() - (width.floatValue() / 2), center.getY().floatValue() - (height.floatValue() / 2), paint);
		}

		paint.setColor(Color.RED);
		for (Displacement displacement : displacementsToObstacles) {
			displacement.draw(canvas);
		}
	}

	private void initState(Double width, Double height) {
		this.width = width;
		this.height = height;
	}

	private AnimatedShape checkPossibleOverlap() {
		for (AnimatedShape obstacle : obstacles) {
			if (obstacle instanceof Rectangle) {
				if (intersects((Rectangle) obstacle)) {
					return obstacle;
				}
			}
		}
		return null;
	}

	private Displacement evaluateSmallestTouchTransaltion(Rectangle other) {
		Double centerDx = center.getX() - other.center.getX();
		Double centerDy = center.getY() - other.center.getY();
		Double dx = Math.abs(Math.abs(centerDx) - (evalHalfWidth() + other.evalHalfWidth()));
		Double dy = Math.abs(Math.abs(centerDy) - (evalHalfHeight() + other.evalHalfHeight()));

		Displacement displacement;
		if (dx < dy) {
			displacement = new Displacement(dx * Math.signum(centerDx), 0.0);
		} else {
			displacement = new Displacement(0.0, dy * Math.signum(centerDy));
		}
		displacement.applyPoint = center;
		return displacement;
	}

	private void moveOutsideBoundriesOfObstacle(Rectangle colidedObstacle) {
		Displacement escapeDisplacement = evaluateSmallestTouchTransaltion(colidedObstacle);
		center.add(escapeDisplacement);
	}

	private void moveToLeftSideBoundry() {
		center = new Displacement(evalHalfWidth(), center.getY());
	}

	private Boolean crossedLeftSideBoundry() {
		return center.getX() - evalHalfWidth() < 0;
	}

	private void moveToRightSideBoundry() {
		center = new Displacement(boundingBoxRight - evalHalfWidth(), center.getY());
	}

	private Boolean crossedRightSideBoundry() {
		return center.getX() + evalHalfWidth() > boundingBoxRight;
	}

	private void reverseVelocityAlongX() {
		velocity.reverseX();
		velocity.divideXByScalar(COLLISION_SPEED_LOSS);
	}
}
