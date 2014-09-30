package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Circle extends AnimatedShape {

	private static final double	COLLISION_SPEED_LOSS	= 2.0;

	public Double				radius;

	private Boolean				isFilled				= false;

	public Circle(Context context, LayoutProportions layoutProportions, Double radius, Paint paint) {
		super(context, layoutProportions, paint);
		this.radius = radius;
	}

	public Circle(Context context, LayoutProportions layoutProportions, Double radius) {
		super(context, layoutProportions);
		this.radius = radius;
	}

	public Circle(Context context, LayoutProportions layoutProportions, Integer radius) {
		this(context, layoutProportions, radius.doubleValue());
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
		canvas.drawCircle(center.x.floatValue(), center.y.floatValue(), radius.floatValue(), paint);
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);

		if (getBounds() != null) {
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
		center = new Displacement(getBounds().right - radius, center.y.doubleValue());
	}

	private boolean crossedRightSideBoundry() {
		return center.x + radius > getBounds().right;
	}

	private void moveToLeftSideBoundry() {
		center = new Displacement(radius, center.y.doubleValue());
	}

	private boolean crossedLeftSideBoundry() {
		return center.x - radius < 0;
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Boolean intersects(AnimatedShape obstacle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMove(Displacement translation) {
		// TODO Auto-generated method stub

	}

}
