package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement.Semiplane;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Sensor extends Sprite {

    public interface ObstaclePassedHandler {
        void onObstaclePassed(AnimatedShape obstacle);
    }

    public interface TipHitHandler {
        void onTipHit();
    }

    @Expose
    private Long orientation = 1L;

    public ObstaclePassedHandler obstaclePassedHandler;
    public TipHitHandler         tipHitHandler;

    private final Map<AnimatedShape, Semiplane> closeByObstacles = new HashMap<AnimatedShape, Semiplane>();

    private Sensor() {
        this(null, null);
    }

    public Sensor(LayoutProportions layoutProportions, Integer resourceId) {
        super(layoutProportions, resourceId);
    }

    @Override
    public void updateState(Long elapsedTime) {
        super.updateState(elapsedTime);

        Displacement firstCorner = evalFirstCorner();
        Displacement secondCorner = evalSecondCorner();
        Displacement diagonal = evalDiagonal();

        for (AnimatedShape obstacle : obstacles) {
            if (!closeByObstacles.containsKey(obstacle) && obstacle.intersects(this)) {
                closeByObstacles.put(obstacle, evalDiagonal().sideOfPoint(obstacle.center));
            } else {
                if (closeByObstacles.containsKey(obstacle) && !obstacle.intersects(this)) {
                    if (obstaclePassedHandler != null && diagonal.sideOfPoint(obstacle.center) != closeByObstacles.get(obstacle)) {
                        obstaclePassedHandler.onObstaclePassed(obstacle);
                    }
                    closeByObstacles.remove(obstacle);
                }
            }

            if (obstacle.contains(firstCorner)) {
                moveOutsideBoundariesOfObstacle(firstCorner);
                if (tipHitHandler != null) {
                    tipHitHandler.onTipHit();
                }
            }

            if (obstacle.contains(secondCorner)) {
                moveOutsideBoundariesOfObstacle(secondCorner);
                if (tipHitHandler != null) {
                    tipHitHandler.onTipHit();
                }
            }
        }
    }

    @Override
    public void addObstacle(AnimatedShape obstacle) {
        if (this != obstacle) {
            obstacles.add(obstacle);
        }
    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
        if (properties.contains(Property.MOVABLE)) {
            switchSensor();
        }
    }

    public void switchSensor() {
        orientation *= -1;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        drawPoint(evalFirstCorner(), canvas);
        drawPoint(evalSecondCorner(), canvas);
        drawVector(evalDiagonal(), canvas);
    }

    private Displacement evalFirstCorner() {
        return new Displacement(center.x - evalHalfWidth(), center.y + orientation * evalHalfHeight());
    }

    private Displacement evalSecondCorner() {
        return new Displacement(center.x + evalHalfWidth(), center.y - orientation * evalHalfHeight());
    }

    private Displacement evalDiagonal() {
        Displacement firstCorner = evalFirstCorner();
        Displacement diagonal = evalSecondCorner().subtractionVector(firstCorner);
        diagonal.applyPoint = firstCorner;
        return diagonal;
    }
}
