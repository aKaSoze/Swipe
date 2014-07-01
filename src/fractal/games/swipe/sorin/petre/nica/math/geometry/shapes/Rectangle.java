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
import fractal.games.swipe.sorin.petre.nica.views.GameView;

public class Rectangle extends AnimatedShape {

    private static final double COLLISION_SPEED_LOSS = 2.0;

    public enum Property {
        MOVABLE, CLONEABLE;
    }

    public GameView                 scene;

    private Boolean                 isFilled;

    private Double                  width;

    public Double                   height;

    private Bitmap                  bitmap;

    public final Set<AnimatedShape> obstacles;

    private final Set<Displacement> displacementsToObstacles = new HashSet<Displacement>();

    public final Set<Property>      properties;

    public Rectangle(Displacement center, Double width, Double height, Paint paint) {
        super(center, paint);
        this.width = width;
        this.height = height;
        isFilled = false;
        obstacles = new HashSet<AnimatedShape>();
        properties = new HashSet<Property>();
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
        Set<Segment2D> segments = new LinkedHashSet<Segment2D>();
        segments.add(new Segment2D(evaluateLeftTopCorner(), evaluateRightTopCorner()));
        segments.add(new Segment2D(evaluateLeftBottomCorner(), evaluateRightBottomCorner()));
        return segments;
    }

    public Set<Segment2D> evaluateVerticalSegments() {
        Set<Segment2D> segments = new LinkedHashSet<Segment2D>();
        segments.add(new Segment2D(evaluateLeftTopCorner(), evaluateLeftBottomCorner()));
        segments.add(new Segment2D(evaluateRightBottomCorner(), evaluateRightTopCorner()));
        return segments;
    }

    public Displacement evaluateLeftTopCorner() {
        return new Displacement(getCenter().getX() - width / 2, getCenter().getY() - height / 2);
    }

    public Displacement evaluateRightTopCorner() {
        return new Displacement(getCenter().getX() + width / 2, getCenter().getY() - height / 2);
    }

    public Displacement evaluateLeftBottomCorner() {
        return new Displacement(getCenter().getX() - width / 2, getCenter().getY() + height / 2);
    }

    public Displacement evaluateRightBottomCorner() {
        return new Displacement(getCenter().getX() + width / 2, getCenter().getY() + height / 2);
    }

    public Set<Segment2D> evaluateSegments() {
        Set<Segment2D> segments = new LinkedHashSet<Segment2D>();
        segments.add(new Segment2D(evaluateLeftTopCorner(), evaluateRightTopCorner()));
        segments.add(new Segment2D(evaluateLeftTopCorner(), evaluateLeftBottomCorner()));
        segments.add(new Segment2D(evaluateLeftBottomCorner(), evaluateRightBottomCorner()));
        segments.add(new Segment2D(evaluateRightBottomCorner(), evaluateRightTopCorner()));
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
        for (Displacement displacement : displacementsToObstacles) {
            displacement.draw(canvas);
        }
    }

    private AnimatedShape checkPossibleOverlap() {
        // displacementsToObstacles.clear();
        for (AnimatedShape obstacle : obstacles) {
            if (obstacle instanceof Rectangle) {
                if (intersects((Rectangle) obstacle)) {
                    return obstacle;
                }
            }
        }
        return null;
    }

    public Boolean intersects(Rectangle other) {
        Double dx = Math.abs(getCenter().getX() - other.getCenter().getX()) - (width / 2 + other.width / 2);
        Double dy = Math.abs(getCenter().getY() - other.getCenter().getY()) - (height / 2 + other.height / 2);
        return dx < 0 && dy < 0;
    }

    private Displacement evaluateSmallestTouchTransaltion(Rectangle other) {
        Double centerDx = getCenter().getX() - other.getCenter().getX();
        Double centerDy = getCenter().getY() - other.getCenter().getY();
        Double dx = Math.abs(Math.abs(centerDx) - (width / 2 + other.width / 2));
        Double dy = Math.abs(Math.abs(centerDy) - (height / 2 + other.height / 2));

        Displacement displacement;
        if (dx < dy) {
            displacement = new Displacement(dx * Math.signum(centerDx), 0.0);
        } else {
            displacement = new Displacement(0.0, dy * Math.signum(centerDy));
        }
        displacement.applyPoint = getCenter();
        return displacement;
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

    private void moveOutsideBoundriesOfObstacle(Rectangle colidedObstacle) {
        Displacement escapeDisplacement = evaluateSmallestTouchTransaltion(colidedObstacle);
        getCenter().add(escapeDisplacement);
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
    public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        super.onMotionEvent(motionEvent, touchPoint);

        if (properties.contains(Property.MOVABLE)) {
            switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (touchPoint.distanceTo(getCenter()) < 50) {
                    setCenter(touchPoint);
                }
                break;
            }
        }
    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
        if (properties.contains(Property.CLONEABLE)) {
            Rectangle newRectangle = new Rectangle(new Displacement(width / 2, height / 2), width, height);
            newRectangle.properties.addAll(properties);
            newRectangle.scene = scene;
            scene.drawables.add(newRectangle);
            scene.hippo.obstacles.add(newRectangle);
        }
    }
}
