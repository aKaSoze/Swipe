package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class Circle extends AnimatedShape {

	private static final double	COLLISION_SPEED_LOSS	= 2.0;

	public Double				radius;

	private Boolean				isFilled				= false;

	public Circle(Displacement center, Double radius, Paint paint) {
		super(center, paint);
		this.radius = radius;
	}

	public Circle(Displacement center, Double radius) {
		super(center);
		this.radius = radius;
	}

	public Circle(Displacement center, Integer radius) {
		this(center, radius.doubleValue());
	}

	public Boolean isFilled() {
		return isFilled;
	}

	public void setFilled(Boolean isFilled) {
		paint.setStyle(isFilled ? Style.FILL : Style.STROKE);
		this.isFilled = isFilled;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(center.getX().floatValue(), center.getY().floatValue(), radius.floatValue(), paint);
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
	}

	private void reverseVelocityAlongX() {
		velocity.reverseX();
		velocity.divideXByScalar(COLLISION_SPEED_LOSS);
	}

	private void moveToRightSideBoundry() {
		center = new Displacement(boundingBoxRight - radius, center.getY().doubleValue());
	}

	private boolean crossedRightSideBoundry() {
		return center.getX() + radius > boundingBoxRight;
	}

	private void moveToLeftSideBoundry() {
		center = new Displacement(radius, center.getY().doubleValue());
	}

	private boolean crossedLeftSideBoundry() {
		return center.getX() - radius < 0;
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		// TODO Auto-generated method stub

	}

}
