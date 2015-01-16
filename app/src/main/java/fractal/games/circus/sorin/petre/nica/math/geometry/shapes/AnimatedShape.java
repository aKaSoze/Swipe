package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Paint;

import com.google.gson.annotations.Expose;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public abstract class AnimatedShape extends CenteredDrawable {

    public interface CenterChangedHandler {
        void onCenterChanged(AnimatedShape shape);
    }

    public interface CollisionHandler {
        void onCollision(AnimatedShape obstacle);
    }

    @Expose
    public Acceleration acceleration;

    @Expose
    public Velocity velocity;

    public CenterChangedHandler centerChangedHandler;

    public CollisionHandler collisionHandler;

    protected final Set<AnimatedShape> obstacles = new CopyOnWriteArraySet<AnimatedShape>();

    public AnimatedShape(LayoutProportions layoutProportions, Paint paint) {
        super(layoutProportions, paint);
        initState();
    }

    public AnimatedShape(LayoutProportions layoutProportions) {
        super(layoutProportions);
        initState();
    }

    private void initState() {
        acceleration = new Acceleration(0.0, 0.0);
        velocity = new Velocity(0, 0);
    }

    @Override
    public void updateState(Long elapsedTime, Long timeIncrement) {
        super.updateState(elapsedTime, timeIncrement);
        Displacement generatedDisplacement = velocity.generatedDisplacement(timeIncrement);
        center.add(generatedDisplacement);
        velocity.add(acceleration.generatedVelocity(timeIncrement));
        if (!generatedDisplacement.isZero() && centerChangedHandler != null) {
            centerChangedHandler.onCenterChanged(this);
        }
    }

    public void onCollision(AnimatedShape obstacle) {
        if (collisionHandler != null) {
            collisionHandler.onCollision(obstacle);
        }
    }

    public void addObstacle(AnimatedShape obstacle) {
        if (this != obstacle) {
            obstacles.add(obstacle);
            obstacle.obstacles.add(this);
        }
    }

    protected AnimatedShape checkPossibleOverlap() {
        for (AnimatedShape obstacle : obstacles) {
            if (obstacle instanceof Rectangle) {
                if (intersects(obstacle)) {
                    return obstacle;
                }
            }
        }
        return null;
    }

    protected abstract Boolean intersects(AnimatedShape obstacle);

    protected abstract Boolean contains(Displacement point);
}
