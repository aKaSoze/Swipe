package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public class Circle extends AnimatedShape {

    private static final double COLLISION_SPEED_LOSS = 2.0;

    public Double               radius;

    private Boolean             isFilled;

    public Circle(Point2D center, Double radius, Paint paint) {
        super(center, paint);
        this.radius = radius;
        isFilled = false;
    }

    public Circle(Point2D center, Double radius) {
        this(center, radius, DEFAULT_PAINT);
    }

    public Circle(Point2D center, Integer radius) {
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
        canvas.drawCircle(getCenter().getX(), getCenter().getY(), radius.floatValue(), paint);
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
        setCenter(new Point2D(boundingBoxRight - radius, getCenter().getY().doubleValue()));
    }

    private boolean crossedRightSideBoundry() {
        return getCenter().getX() + radius > boundingBoxRight;
    }

    private void moveToLeftSideBoundry() {
        setCenter(new Point2D(radius, getCenter().getY().doubleValue()));
    }

    private boolean crossedLeftSideBoundry() {
        return getCenter().getX() - radius < 0;
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        // TODO Auto-generated method stub

    }

}
