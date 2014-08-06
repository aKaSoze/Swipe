package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement.Semiplane;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class Sensor extends Rectangle {

	public interface ObstaclePassedHandler {
		void onObstaclePassed(AnimatedShape obstacle);
	}

	private final Rectangle						firstSupport;

	private final Rectangle						secondSupport;

	private Displacement						diagonal;

	public ObstaclePassedHandler				obstaclePassedHandler;

	private Bitmap								bitmap;

	private final Map<AnimatedShape, Semiplane>	closeByObstacles	= new HashMap<AnimatedShape, Semiplane>();

	public Sensor(Bitmap bitmap, Rectangle firstSupport, Rectangle secondSupport) {
		super(new LayoutProportions(firstSupport.layoutProportions, secondSupport.layoutProportions));
		this.bitmap = bitmap;
		this.firstSupport = firstSupport;
		this.secondSupport = secondSupport;
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);

		for (AnimatedShape obstacle : obstacles) {
			if (obstacle.intersects(this)) {
				closeByObstacles.put(obstacle, diagonal.sideOfPoint(obstacle.center));
			} else {
				if (closeByObstacles.containsKey(obstacle)) {
					if (diagonal.sideOfPoint(obstacle.center) != closeByObstacles.get(obstacle) && obstaclePassedHandler != null) {
						obstaclePassedHandler.onObstaclePassed(obstacle);
					}
					closeByObstacles.remove(obstacle);
				}
			}
		}
	}

	@Override
	protected Boolean intersects(AnimatedShape obstacle) {
		return firstSupport.intersects(obstacle) || secondSupport.intersects(obstacle);
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Canvas canvas) {
		Displacement drawCenter = evalDrawCenter();
		canvas.drawBitmap(bitmap, drawCenter.x.floatValue() - (evalHalfWidth().floatValue()), drawCenter.y.floatValue() - (evalHalfHeight().floatValue()), paint);
		firstSupport.draw(canvas);
		secondSupport.draw(canvas);
		drawVector(diagonal, canvas);
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		bitmap = Bitmap.createScaledBitmap(bitmap, evalWidth().intValue(), evalHeight().intValue(), true);
		diagonal = firstSupport.center.subtractionVector(secondSupport.center);
		diagonal.applyPoint = firstSupport.center;
	}

	@Override
	public void onMove(Displacement translation) {
		firstSupport.center.add(translation);
		secondSupport.center.add(translation);
	}

}
