package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashSet;
import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;

public class PropulsionPlatform extends Rectangle {

    private enum Status {
        STANDING, STRECTHING, RELEASED;
    }

    public interface CollisionHandler {
        void onCollison();
    }

    private static final Double  ELASTICITY_COEFICIENT = 18.0;                                                      ;

    private Double               maxSpringDisplacement;

    private Displacement         strecthPoint          = new Displacement(0.0, 0.0);

    private Status               status                = Status.STANDING;

    private Long                 strecthingTime;

    private Long                 elapsedTime;

    private Velocity             springVelocity        = new Velocity(0.0, 0.0, LengthUnit.PIXEL, TimeUnit.SECOND);

    public final AnimatedShape   projectile;

    public MediaPlayer           boingSoundPlayer;

    public Set<CollisionHandler> collisionHandlers     = new HashSet<PropulsionPlatform.CollisionHandler>();

    public PropulsionPlatform(LayoutProportions layoutProportions, AnimatedShape projectile, MediaPlayer boingSoundPlayer) {
        super(layoutProportions);
        this.projectile = projectile;
        this.boingSoundPlayer = boingSoundPlayer;
        projectile.addObstacle(this);
        projectile.velocity = new Velocity(0.0, 0.0, LengthUnit.PIXEL, TimeUnit.SECOND);
        projectile.acceleration = new Acceleration(0.0, 0.0, LengthUnit.METER, TimeUnit.SECOND);
        paint.setStrokeWidth(3);
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        switch (status) {
        case STANDING:
            handleStandingMotionEvent(motionEvent, touchPoint);
            break;
        case STRECTHING:
            handleStrecthingMotionEvent(motionEvent, touchPoint);
            break;
        default:
            break;
        }
    }

    @Override
    public void updateState(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
        if (status == Status.RELEASED) {
            if (strecthPoint.isZero()) {
                projectile.velocity.setComponents(springVelocity.getX() / 4, springVelocity.getY() / 4);
                projectile.acceleration.setY(-9.8);
                springVelocity.neutralize();
                status = Status.STANDING;
                boingSoundPlayer.start();
            } else {
                Long elapsedStrecthingTime = elapsedTime - strecthingTime;
                strecthingTime = elapsedTime;

                Displacement displacement = springVelocity.generatedDisplacement(elapsedStrecthingTime);
                if (displacement.magnitude() > strecthPoint.magnitude()) {
                    strecthPoint.neutralize();
                } else {
                    strecthPoint.add(displacement);
                }
            }
        }
    }

    @Override
    public void onCollision(AnimatedShape obstacle) {
        Log.i("velocity", obstacle.velocity.magnitude().toString());
        if (obstacle.velocity.magnitude() < 20) {
            obstacle.acceleration.neutralize();
            obstacle.velocity.neutralize();
        }
        for (CollisionHandler collisonHandler : collisionHandlers) {
            collisonHandler.onCollison();
        }
    }

    public PropulsionPlatform clonePropulsionPlatform() {
        PropulsionPlatform newPropulsionPlatform = new PropulsionPlatform(layoutProportions, projectile, boingSoundPlayer);
        newPropulsionPlatform.properties.addAll(properties);
        return newPropulsionPlatform;
    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
        if (properties.contains(Property.CLONEABLE)) {
            PropulsionPlatform newPropulsionPlatform = clonePropulsionPlatform();
            newPropulsionPlatform.properties.addAll(properties);
            projectile.addObstacle(newPropulsionPlatform);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float strecthPointOriginX = strecthPoint.applyPoint.getX().floatValue();
        float strecthPointOriginY = drawTranslation.getY().floatValue() - strecthPoint.applyPoint.getY().floatValue();

        float strecthPointX = strecthPoint.applyPoint.getX().floatValue() + strecthPoint.getX().floatValue();
        float strecthPointY = drawTranslation.getY().floatValue() - strecthPoint.applyPoint.getY().floatValue() - strecthPoint.getY().floatValue();

        canvas.drawLine(strecthPointOriginX, strecthPointOriginY, strecthPointX, strecthPointY, paint);
        canvas.drawCircle(strecthPointX, strecthPointY, 10, paint);
        canvas.drawLine(strecthPointX, strecthPointY, evalLeftTopCorner().getX().floatValue(), drawTranslation.getY().floatValue() - evalLeftTopCorner().getY().floatValue(), paint);
        canvas.drawLine(strecthPointX, strecthPointY, evalRightTopCorner().getX().floatValue(), drawTranslation.getY().floatValue() - evalRightTopCorner().getY().floatValue(), paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        strecthPoint.applyPoint = center;
        maxSpringDisplacement = evalWidth() * 0.9;
        projectile.center.setComponents(center.getX(), center.getY() + projectile.evalHalfHeight() + evalHalfHeight());
    }

    private void handleStandingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        switch (motionEvent.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            if (touchPoint.distanceTo(center) < 35) {
                status = Status.STRECTHING;
                projectile.velocity.neutralize();
                projectile.acceleration.neutralize();
                projectile.center.setComponents(center.getX(), center.getY() + projectile.evalHalfHeight() + evalHalfHeight());
                strecthPoint.makeEqualTo(touchPoint.subtractionVector(center));
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (properties.contains(Property.MOVABLE)) {
                if (touchPoint.distanceTo(evalRightTopCorner()) < 35) {
                    setRightTopCorner(touchPoint);
                }
            }
            break;
        }
    }

    private void handleStrecthingMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        switch (motionEvent.getActionMasked()) {
        case MotionEvent.ACTION_UP:
            status = Status.RELEASED;
            strecthingTime = elapsedTime;
            springVelocity.setComponents(-strecthPoint.getX() * ELASTICITY_COEFICIENT, -strecthPoint.getY() * ELASTICITY_COEFICIENT);
            break;
        case MotionEvent.ACTION_MOVE:
            Displacement rawStrecthPoint = touchPoint.subtractionVector(center);
            if (rawStrecthPoint.magnitude() > maxSpringDisplacement) {
                rawStrecthPoint.setMagnitude(maxSpringDisplacement);
            }
            if (rawStrecthPoint.getY() > 0) {
                rawStrecthPoint.setY(0.0);
            }
            strecthPoint.makeEqualTo(rawStrecthPoint);
            break;
        }
    }

}
