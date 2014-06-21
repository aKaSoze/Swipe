package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public class Rectangle extends AnimatedShape {

    private static final double COLLISION_SPEED_LOSS = 2.0;

    private Boolean             isFilled;

    private Double              width;

    public Double               height;

    private Bitmap              bitmap;

    public Rectangle(Point2D center, Double width, Double height, Paint paint) {
        super(center, paint);
        this.width = width;
        this.height = height;
        isFilled = false;
    }

    public Rectangle(Point2D center, Double width, Double height) {
        this(center, width, height, DEFAULT_PAINT);
    }

    public Rectangle(Point2D center, Integer width, Integer height) {
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

    @Override
    public void draw(Canvas canvas) {
        if (bitmap == null) {
            canvas.drawRect(getCenter().getX() - (width.floatValue() / 2), getCenter().getY() - (height.floatValue() / 2), getCenter().getX() + (width.floatValue() / 2), getCenter().getY() + (height.floatValue() / 2),
                    paint);
        } else {
            canvas.drawBitmap(bitmap, getCenter().getX() - (width.floatValue() / 2), getCenter().getY() - (height.floatValue() / 2), paint);
        }
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
        setCenter(new Point2D(boundingBoxRight - (width / 2), getCenter().getY().doubleValue()));
    }

    private boolean crossedRightSideBoundry() {
        return getCenter().getX() + (width / 2) > boundingBoxRight;
    }

    private void moveToLeftSideBoundry() {
        setCenter(new Point2D((width / 2), getCenter().getY().doubleValue()));
    }

    private boolean crossedLeftSideBoundry() {
        return getCenter().getX() - (width / 2) < 0;
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        // TODO Auto-generated method stub

    }

}
