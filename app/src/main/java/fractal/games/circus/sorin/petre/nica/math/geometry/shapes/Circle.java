package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

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

	public Circle(LayoutProportions layoutProportions, Double radius, Paint paint) {
		super(layoutProportions, paint);
		this.radius = radius;
	}

	public Circle(LayoutProportions layoutProportions, Double radius) {
		super(layoutProportions);
		this.radius = radius;
	}

	public Circle(LayoutProportions layoutProportions, Integer radius) {
		this(layoutProportions, radius.doubleValue());
	}

	@Override
	public void init() {
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
			if (crossedLeftSideBoundary()) {
				moveToLeftSideBoundary();
				reverseVelocityAlongX();
			}
			if (crossedRightSideBoundary()) {
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

	private boolean crossedRightSideBoundary() {
		return center.x + radius > getBounds().right;
	}

	private void moveToLeftSideBoundary() {
		center = new Displacement(radius, center.y.doubleValue());
	}

	private boolean crossedLeftSideBoundary() {
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
    protected Boolean contains(Displacement point) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public void onMove(Displacement translation) {
		// TODO Auto-generated method stub

	}

}
