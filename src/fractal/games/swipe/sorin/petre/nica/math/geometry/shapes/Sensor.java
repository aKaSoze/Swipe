package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement.Semiplane;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class Sensor extends Painting {

    public interface ObstaclePassedHandler {
        void onObstaclePassed(AnimatedShape obstacle);
    }

    private final Rectangle                     firstSupport;

    private final Rectangle                     secondSupport;

    private Displacement                        diagonal;

    public ObstaclePassedHandler                obstaclePassedHandler;

    private final Map<AnimatedShape, Semiplane> closeByObstacles = new HashMap<AnimatedShape, Semiplane>();

    public Sensor(Context context, Integer resourceId, Rectangle firstSupport, Rectangle secondSupport) {
        super(context, new LayoutProportions(firstSupport.layoutProportions, secondSupport.layoutProportions), resourceId);
        this.firstSupport = firstSupport;
        this.secondSupport = secondSupport;
    }

    @Override
    public void updateState(Long elapsedTime) {
        super.updateState(elapsedTime);

        for (AnimatedShape obstacle : firstSupport.obstacles) {
            if (!closeByObstacles.containsKey(obstacle) && obstacle.intersects(this)) {
                closeByObstacles.put(obstacle, diagonal.sideOfPoint(obstacle.center));
            } else {
                if (closeByObstacles.containsKey(obstacle) && !obstacle.intersects(this)) {
                    if (diagonal.sideOfPoint(obstacle.center) != closeByObstacles.get(obstacle) && obstaclePassedHandler != null) {
                        obstaclePassedHandler.onObstaclePassed(obstacle);
                    }
                    closeByObstacles.remove(obstacle);
                }
            }
        }
    }

    @Override
    public void addObstacle(AnimatedShape obstacle) {
        firstSupport.addObstacle(obstacle);
        secondSupport.addObstacle(obstacle);
    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw(Canvas canvas) {
        Displacement drawCenter = evalDrawCenter();
        canvas.drawBitmap(getBitmap(), drawCenter.x.floatValue() - (evalHalfWidth().floatValue()), drawCenter.y.floatValue() - (evalHalfHeight().floatValue()), paint);
        firstSupport.draw(canvas);
        secondSupport.draw(canvas);
        drawVector(diagonal, canvas);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        firstSupport.setBounds(left, top, right, bottom);
        secondSupport.setBounds(left, top, right, bottom);
        super.setBounds(left, top, right, bottom);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        diagonal = secondSupport.center.subtractionVector(firstSupport.center);
        diagonal.applyPoint = firstSupport.center;

        firstSupport.drawTranslation = drawTranslation;
        secondSupport.drawTranslation = drawTranslation;
    }

    @Override
    public void onMove(Displacement translation) {
        firstSupport.center.add(translation);
        secondSupport.center.add(translation);
    }

}
