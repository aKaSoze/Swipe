package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class Rectangle extends AnimatedShape {

    private static final double        COLLISION_SPEED_LOSS     = 2.0;

    private Boolean                    isFilled;

    private Double                     width;

    public Double                      height;

    private Bitmap                     bitmap;

    public final Set<CenteredDrawable> obstacles;

    private final Set<Displacement>    displacementsToObstacles = new HashSet<Displacement>();

    public Rectangle(Displacement center, Double width, Double height, Paint paint) {
        super(center, paint);
        this.width = width;
        this.height = height;
        isFilled = false;
        obstacles = new HashSet<CenteredDrawable>();
    }

    public Rectangle(Displacement center, Double width, Double height) {
        this(center, width, height, DEFAULT_PAINT);
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

    public Set<Segment2D> evaluateHorizontalSegments() {
        return null;
    }

    public Set<Segment2D> evaluateVerticalSegments() {
        return null;
    }

    public Displacement evaluateLeftTopCorner() {
        return new Displacement(getCenter().getX() - width / 2, getCenter().getY() - height / 2);
    }

    public Displacement evaluateRightTopCorner() {
        return new Displacement(getCenter().getX() + width / 2, getCenter().getY() - height / 2);
    }

    public Set<Segment2D> evaluateSegments() {
        Set<Segment2D> segments = new LinkedHashSet<Segment2D>();
        segments.add(new Segment2D(evaluateLeftTopCorner(), evaluateRightTopCorner()));
        return segments;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        if (bitmap == null) {
            canvas.drawRect(getCenter().getX().floatValue() - (width.floatValue() / 2), getCenter().getY().floatValue() - (height.floatValue() / 2), getCenter().getX().floatValue() + (width.floatValue() / 2),
                    getCenter().getY().floatValue() + (height.floatValue() / 2), paint);
        } else {
            canvas.drawBitmap(bitmap, getCenter().getX().floatValue() - (width.floatValue() / 2), getCenter().getY().floatValue() - (height.floatValue() / 2), paint);
        }

        paint.setColor(Color.RED);
        evaluateSegments().iterator().next().draw(canvas);
        for (Displacement displacement : displacementsToObstacles) {
            displacement.draw(canvas);
        }
    }

    private CenteredDrawable checkPossibleOverlap() {
        displacementsToObstacles.clear();
        for (CenteredDrawable obstacle : obstacles) {
            if (obstacle instanceof Rectangle) {
                Rectangle rectangleObstacle = (Rectangle) obstacle;
                for (Segment2D segment : evaluateSegments()) {
                    displacementsToObstacles.add(segment.distanceToAPoint(obstacle.getCenter()));
                }
            }
        }
        return null;
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

        CenteredDrawable colidedObstacle = checkPossibleOverlap();
        if (colidedObstacle != null) {
            moveOutsideBoundriesOfObstacle(colidedObstacle);
            onCollision(colidedObstacle);
            colidedObstacle.onCollision(this);
        }
    }

    private void moveOutsideBoundriesOfObstacle(CenteredDrawable colidedObstacle) {
        // TODO Auto-generated method stub

    }

    private void reverseVelocityAlongX() {
        velocity.reverseX();
        velocity.divideXByScalar(COLLISION_SPEED_LOSS);
    }

    private void moveToRightSideBoundry() {
        setCenter(new Displacement(boundingBoxRight - (width / 2), getCenter().getY()));
    }

    private boolean crossedRightSideBoundry() {
        return getCenter().getX() + (width / 2) > boundingBoxRight;
    }

    private void moveToLeftSideBoundry() {
        setCenter(new Displacement((width / 2), getCenter().getY()));
    }

    private boolean crossedLeftSideBoundry() {
        return getCenter().getX() - (width / 2) < 0;
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        // TODO Auto-generated method stub

    }

}
