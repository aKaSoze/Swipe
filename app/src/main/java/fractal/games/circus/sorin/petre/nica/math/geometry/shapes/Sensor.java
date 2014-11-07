package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement.Semiplane;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Sensor extends Painting {

    public interface ObstaclePassedHandler {
        void onObstaclePassed(AnimatedShape obstacle);
    }

    public ObstaclePassedHandler obstaclePassedHandler;

    private final Map<AnimatedShape, Semiplane> closeByObstacles = new HashMap<AnimatedShape, Semiplane>();

    public Sensor() {
        super();
    }

    public Sensor(LayoutProportions layoutProportions, Integer resourceId) {
        super(layoutProportions, resourceId);
    }

    @Override
    public void updateState(Long elapsedTime) {
        super.updateState(elapsedTime);

        for (AnimatedShape obstacle : obstacles) {
            if (!closeByObstacles.containsKey(obstacle) && obstacle.intersects(this)) {
                closeByObstacles.put(obstacle, evalDiagonal().sideOfPoint(obstacle.center));
            } else {
                if (closeByObstacles.containsKey(obstacle) && !obstacle.intersects(this)) {
                    if (obstaclePassedHandler != null && evalDiagonal().sideOfPoint(obstacle.center) != closeByObstacles.get(obstacle)) {
                        obstaclePassedHandler.onObstaclePassed(obstacle);
                    }
                    closeByObstacles.remove(obstacle);
                }
            }
        }
    }

    @Override
    public void addObstacle(AnimatedShape obstacle) {
    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw(Canvas canvas) {
        Displacement drawCenter = evalDrawCenter();
        canvas.drawBitmap(getBitmap(), drawCenter.x.floatValue() - (evalHalfWidth().floatValue()), drawCenter.y.floatValue() - (evalHalfHeight().floatValue()), paint);
        drawPoint(evalFirstCorner(), canvas);
        drawPoint(evalSecondCorner(), canvas);
        drawVector(evalDiagonal(), canvas);
    }

    private Displacement evalFirstCorner() {
        return new Displacement(center.x - evalHalfWidth(), center.y + evalHalfHeight());
    }

    private Displacement evalSecondCorner() {
        return new Displacement(center.x + evalHalfWidth(), center.y - evalHalfHeight());
    }

    private Displacement evalDiagonal() {
        Displacement firstCorner = evalFirstCorner();
        Displacement diagonal = evalSecondCorner().subtractionVector(firstCorner);
        diagonal.applyPoint = firstCorner;
        return diagonal;
    }
}
