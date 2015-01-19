package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class PropulsionPlatform extends Sprite {

    private enum Status {
        STANDING, STRETCHING, RELEASED
    }

    public interface ImpactHandler {
        void onImpact(PropulsionPlatform propulsionPlatform);
    }

    private static final Double ELASTICITY_COEFFICIENT = 0.017;
    private static final Double PROJECTILE_MIN_SPEED   = 0.035;

    private Displacement       stretchPoint   = new Displacement();
    private Velocity           springVelocity = new Velocity(0.0, 0.0);
    private Status             status         = Status.STANDING;
    public  Set<ImpactHandler> impactHandlers = new HashSet<PropulsionPlatform.ImpactHandler>();

    private Double maxSpringDisplacement;
    private Bitmap silverRing;

    public PropulsionPlatform() {
        this(null);
    }

    public PropulsionPlatform(LayoutProportions layoutProportions) {
        super(layoutProportions, R.drawable.beam);
        init();
    }

    @Override
    public void init() {
        super.init();
        paint.setStrokeWidth(4);
        stretchPoint.applyPoint = new Displacement();
    }

    @Override
    public void updateState(Long elapsedTime, Long timeIncrement) {
        if (status == Status.RELEASED) {
            if (stretchPoint.isZero()) {
                for (ImpactHandler impactHandler : impactHandlers) {
                    impactHandler.onImpact(this);
                }
                springVelocity.neutralize();
                status = Status.STANDING;
            } else {
                Displacement displacement = springVelocity.generatedDisplacement(timeIncrement);
                if (displacement.magnitude() > stretchPoint.magnitude()) {
                    stretchPoint.neutralize();
                } else {
                    stretchPoint.add(displacement);
                }
            }
        }
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        if (properties.contains(Property.MOVABLE)) {
            super.onMotionEvent(motionEvent, touchPoint);
        } else {
            switch (status) {
                case STANDING:
                    handleStandingMotionEvent(motionEvent, touchPoint);
                    break;
                case STRETCHING:
                    handleStretchingMotionEvent(motionEvent, touchPoint);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onCollision(AnimatedShape obstacle) {
        obstacle.velocity.divideByScalar(2d);
        if (obstacle.velocity.magnitude() < PROJECTILE_MIN_SPEED) {
            obstacle.acceleration.neutralize();
            obstacle.velocity.neutralize();
            obstacle.center.x = center.x;
            obstacle.center.y = center.y + evalHalfHeight() + obstacle.evalHalfHeight();
        }
        super.onCollision(obstacle);
    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
        if (properties.contains(Property.CLONEABLE)) {
            PropulsionPlatform newPropulsionPlatform = clonePropulsionPlatform();
            newPropulsionPlatform.properties.addAll(properties);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Displacement drawStretchPoint = evalDrawLocation(stretchPoint.evaluateTip());
        stretchPoint.applyPoint.makeEqualTo(evalHookPoint());
        drawVector(stretchPoint, canvas);
        canvas.drawBitmap(silverRing, drawStretchPoint.x.floatValue() - silverRing.getWidth() / 2, drawStretchPoint.y.floatValue() - silverRing.getHeight() / 2, paint);
        super.draw(canvas);
    }

    public PropulsionPlatform clonePropulsionPlatform() {
        PropulsionPlatform newPropulsionPlatform = new PropulsionPlatform(layoutProportions);
        newPropulsionPlatform.properties.addAll(properties);
        return newPropulsionPlatform;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        maxSpringDisplacement = evalWidth();
        silverRing = MediaStore.getScaledBitmap(R.drawable.silver_ring, evalWidth() / 4, evalWidth() / 4);
    }

    private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Displacement hookPoint = evalHookPoint();
                if (touchPoint.distanceTo(hookPoint) < VECINITY_DISTANCE) {
                    status = Status.STRETCHING;
                    stretchPoint.makeEqualTo(touchPoint.subtractionVector(hookPoint));
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

    private void handleStretchingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                status = Status.RELEASED;
                springVelocity.setComponents(-stretchPoint.x * ELASTICITY_COEFFICIENT, -stretchPoint.y * ELASTICITY_COEFFICIENT);
                break;
            case MotionEvent.ACTION_MOVE:
                Displacement rawStretchPoint = touchPoint.subtractionVector(evalHookPoint());
                if (rawStretchPoint.magnitude() > maxSpringDisplacement) {
                    rawStretchPoint.setMagnitude(maxSpringDisplacement);
                }
                if (rawStretchPoint.y > 0) {
                    rawStretchPoint.y = 0.0;
                }
                stretchPoint.makeEqualTo(rawStretchPoint);
                break;
        }
    }

    private Displacement evalHookPoint() {
        Displacement hookPoint = new Displacement(center.x, center.y);
        hookPoint.y -= evalHeight() / 4;
        return hookPoint;
    }

    public Velocity getSpringVelocity() {
        return new Velocity(springVelocity.x, springVelocity.y);
    }

}
