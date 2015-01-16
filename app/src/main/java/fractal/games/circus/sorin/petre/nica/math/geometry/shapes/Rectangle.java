package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.view.MotionEvent;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Rectangle extends AnimatedShape {

    public interface DoubleTapHandler {
        void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint);
    }

    private static final double ROUNDING_ERROR = 0.0001;

    private static final double COLLISION_SPEED_LOSS = 2.0;

    @Expose
    private Boolean isFilled = false;

    public transient final Set<DoubleTapHandler> doubleTapHandlers = new HashSet<DoubleTapHandler>();

    public Rectangle(LayoutProportions layoutProportions) {
        super(layoutProportions);
    }

    public Rectangle() {
        this(null);
    }

    @Override
    public void init() {
    }

    public void setFilled(Boolean isFilled) {
        paint.setStyle(isFilled ? Style.FILL : Style.STROKE);
        this.isFilled = isFilled;
    }

    public Boolean isFilled() {
        return isFilled;
    }

    public void setRightTopCorner(Displacement rightTopCorner) {
        center.makeEqualTo(rightTopCorner.subtractionVector(new Displacement(evalHalfWidth(), evalHalfHeight())));
    }

    public Displacement evalLeftTopCorner() {
        return new Displacement(center.x - evalHalfWidth(), center.y + evalHalfHeight());
    }

    public Displacement evalRightTopCorner() {
        return new Displacement(center.x + evalHalfWidth(), center.y + evalHalfHeight());
    }

    @Override
    public void updateState(Long elapsedTime, Long timeIncrement) {
        super.updateState(elapsedTime, timeIncrement);
        if (!velocity.isZero()) {
            AnimatedShape collidedObstacle = checkPossibleOverlap();
            if (collidedObstacle != null) {
                moveOutsideBoundariesOfObstacle((Rectangle) collidedObstacle);
                onCollision(collidedObstacle);
                collidedObstacle.onCollision(this);
            }
        }
    }

    @Override
    public void onCollision(AnimatedShape obstacle) {
        if (collisionHandler == null) {
            if (touchesOnVerticalSide((Rectangle) obstacle)) {
                velocity.reverseX();
            } else {
                velocity.reverseY();
            }
            velocity.multiplyByScalar(0.5);
        } else {
            super.onCollision(obstacle);
        }
    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
        for (DoubleTapHandler doubleTapHandler : doubleTapHandlers) {
            doubleTapHandler.onDoubleTap(motionEvent, touchPoint);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Displacement center = evalDrawCenter();
        canvas.drawRect(center.x.floatValue() - (evalHalfWidth().floatValue()), center.y.floatValue() - (evalHalfHeight().floatValue()), center.x.floatValue() + (evalHalfWidth().floatValue()), center.y.floatValue()
                + (evalHalfHeight().floatValue()), paint);
    }

    @Override
    protected Boolean intersects(AnimatedShape obstacle) {
        if (obstacle instanceof Rectangle) {
            return intersects((Rectangle) obstacle);
        }
        return false;
    }

    private Boolean intersects(Rectangle other) {
        Double dx = Math.abs(center.x - other.center.x) - (evalHalfWidth() + other.evalHalfWidth());
        Double dy = Math.abs(center.y - other.center.y) - (evalHalfHeight() + other.evalHalfHeight());
        return dx < 0 && dy < 0;
    }

    @Override
    protected Boolean contains(Displacement point) {
        Double dx = Math.abs(center.x - point.x) - evalHalfWidth();
        Double dy = Math.abs(center.y - point.y) - evalHalfHeight();
        return dx < 0 && dy < 0;
    }

    protected Boolean touchesOnVerticalSide(Rectangle other) {
        return Math.abs(Math.abs(center.x - other.center.x) - (evalHalfWidth() + other.evalHalfWidth())) < ROUNDING_ERROR;
    }

    public Boolean touchesOnHorizontalSide(Rectangle other) {
        return Math.abs(Math.abs(center.y - other.center.y) - (evalHalfHeight() + other.evalHalfHeight())) < ROUNDING_ERROR;
    }

    private Displacement evaluateSmallestTouchTranslation(Displacement otherCenter, Double otherHalfWidth, Double otherHalfHeight) {
        Double centerDx = center.x - otherCenter.x;
        Double centerDy = center.y - otherCenter.y;
        Double dx = Math.abs(Math.abs(centerDx) - (evalHalfWidth() + otherHalfWidth));
        Double dy = Math.abs(Math.abs(centerDy) - (evalHalfHeight() + otherHalfHeight));

        Displacement displacement;
        if (dx < dy) {
            displacement = new Displacement(dx * Math.signum(centerDx), 0.0);
        } else {
            displacement = new Displacement(0.0, dy * Math.signum(centerDy));
        }
        displacement.applyPoint = center;
        return displacement;
    }

    private Displacement evaluateSmallestTouchTranslation(Rectangle other) {
        return evaluateSmallestTouchTranslation(other.center, other.evalHalfWidth(), other.evalHalfHeight());
    }

    private Displacement evaluateSmallestTouchTranslation(Displacement point) {
        return evaluateSmallestTouchTranslation(point, 0.0, 0.0);
    }

    private void moveOutsideBoundariesOfObstacle(Rectangle collidedObstacle) {
        center.add(evaluateSmallestTouchTranslation(collidedObstacle));
    }

    protected void moveOutsideBoundariesOfObstacle(Displacement point) {
        center.add(evaluateSmallestTouchTranslation(point));
    }

    protected void moveToLeftSideBoundary() {
        center.setComponents(evalHalfWidth(), center.y);
    }

    protected Boolean crossedLeftSideBoundary() {
        return center.x - evalHalfWidth() < 0;
    }

    protected void moveToRightSideBoundary() {
        center.setComponents(getBounds().right - evalHalfWidth(), center.y);
    }

    protected Boolean crossedRightSideBoundary() {
        return center.x + evalHalfWidth() > getBounds().right;
    }

    protected void reverseVelocityAlongX() {
        velocity.reverseX();
        velocity.divideXByScalar(COLLISION_SPEED_LOSS);
    }

    @Override
    public void onMove(Displacement translation) {
        // TODO Auto-generated method stub

    }
}
