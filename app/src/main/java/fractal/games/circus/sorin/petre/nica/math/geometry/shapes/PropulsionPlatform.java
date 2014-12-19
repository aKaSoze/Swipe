package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class PropulsionPlatform extends Sprite {

    private enum Status {
        STANDING, STRETCHING, RELEASED
    }

    public interface CollisionHandler {
        void onCollision();
    }

    public interface ImpactHandler {
        void onImpact(PropulsionPlatform propulsionPlatform);
    }

    private static final Double ELASTICITY_COEFFICIENT = 0.02;
    private static final Double PROJECTILE_MIN_SPEED   = 0.035;

    private Double maxSpringDisplacement;

    private Displacement stretchPoint = new Displacement();

    private Status status = Status.STANDING;

    private Long stretchingTime;

    private Long elapsedTime;

    private Velocity springVelocity = new Velocity(0.0, 0.0);

    public MediaPlayer boingSoundPlayer;

    public Set<CollisionHandler> collisionHandlers = new HashSet<PropulsionPlatform.CollisionHandler>();

    public Set<ImpactHandler> impactHandlers = new HashSet<PropulsionPlatform.ImpactHandler>();

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
    }

    @Override
    public void updateState(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
        if (status == Status.RELEASED) {
            if (stretchPoint.isZero()) {
                for (ImpactHandler impactHandler : impactHandlers) {
                    impactHandler.onImpact(this);
                }
                springVelocity.neutralize();
                status = Status.STANDING;
            } else {
                Long elapsedStretchingTime = elapsedTime - stretchingTime;
                stretchingTime = elapsedTime;

                Displacement displacement = springVelocity.generatedDisplacement(elapsedStretchingTime);
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
        if (obstacle.velocity.magnitude() < PROJECTILE_MIN_SPEED) {
            obstacle.acceleration.neutralize();
            obstacle.velocity.neutralize();
            obstacle.center.x = center.x;
            obstacle.center.y = center.y + evalHalfHeight() + obstacle.evalHalfHeight();
        }
        for (CollisionHandler collisonHandler : collisionHandlers) {
            collisonHandler.onCollision();
        }
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
        super.draw(canvas);

        Displacement drawStretchPoint = evalDrawLocation(stretchPoint.evaluateTip());
        Displacement drawLeftTopCorner = evalDrawLocation(evalLeftTopCorner());
        Displacement drawRightTopCorner = evalDrawLocation(evalRightTopCorner());

        drawVector(stretchPoint, canvas);
        canvas.drawCircle(drawStretchPoint.x.floatValue(), drawStretchPoint.y.floatValue(), 10, paint);
        canvas.drawLine(drawStretchPoint.x.floatValue(), drawStretchPoint.y.floatValue(), drawLeftTopCorner.x.floatValue(), drawLeftTopCorner.y.floatValue(), paint);
        canvas.drawLine(drawStretchPoint.x.floatValue(), drawStretchPoint.y.floatValue(), drawRightTopCorner.x.floatValue(), drawRightTopCorner.y.floatValue(), paint);
    }

    public PropulsionPlatform clonePropulsionPlatform() {
        PropulsionPlatform newPropulsionPlatform = new PropulsionPlatform(layoutProportions);
        newPropulsionPlatform.properties.addAll(properties);
        return newPropulsionPlatform;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        stretchPoint.applyPoint = center;
        maxSpringDisplacement = evalWidth();
    }

    private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (touchPoint.distanceTo(center) < VECINITY_DISTANCE) {
                    status = Status.STRETCHING;
                    stretchPoint.makeEqualTo(touchPoint.subtractionVector(center));
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
                stretchingTime = elapsedTime;
                springVelocity.setComponents(-stretchPoint.x * ELASTICITY_COEFFICIENT, -stretchPoint.y * ELASTICITY_COEFFICIENT);
                break;
            case MotionEvent.ACTION_MOVE:
                Displacement rawStretchPoint = touchPoint.subtractionVector(center);
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

    public Velocity getSpringVelocity() {
        return new Velocity(springVelocity.x, springVelocity.y);
    }

}
