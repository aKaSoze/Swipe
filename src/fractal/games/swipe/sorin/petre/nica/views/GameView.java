package fractal.games.swipe.sorin.petre.nica.views;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Net;
import fractal.games.swipe.sorin.petre.nica.math.objects.Displacement2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;

public class GameView extends AutoUpdatableView {

	private final ColorDrawable			backGround_drwbl	= new ColorDrawable(Color.BLACK);

	private CenteredDrawable			selectedShape		= null;

	private Boolean						isOkToRunGameLoop;

	private final Set<CenteredDrawable>	drawables			= new HashSet<CenteredDrawable>();

	public GameView(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		Point2D x1 = new Point2D((right - left) / 2, (bottom - top) / 2);
		Point2D x2 = x1.translate(new Displacement2D(300, 0));
		Net segment2d = new Net(new Segment2D(x1, x2));
		drawables.add(segment2d);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (CenteredDrawable centeredDrawable : drawables) {
			centeredDrawable.onMotionEvent(event);
		}
		return true;
	}

	private CenteredDrawable evaluateTargetShape(Point2D touchPoint) {
		CenteredDrawable closestShape = null;
		float smallestDistance = 60;
		for (CenteredDrawable movableShape : drawables) {
			float distanceToTouchPoint = movableShape.distanceTo(touchPoint);
			if (distanceToTouchPoint < smallestDistance) {
				smallestDistance = distanceToTouchPoint;
				closestShape = movableShape;
			}
		}
		return closestShape;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawSurface(canvas);
	}

	public void updateWorld(Long elapsedTime) {
		Set<CenteredDrawable> deadObjects = new HashSet<CenteredDrawable>();

		for (CenteredDrawable movableShape : drawables) {
			movableShape.updateState(elapsedTime);
		}

		drawables.removeAll(deadObjects);
	}

	@Override
	protected Runnable getBehavior() {
		return new Runnable() {
			@Override
			public void run() {
				isOkToRunGameLoop = true;
				Log.d(logTag, "Game loop started.");
				Long startTime = System.currentTimeMillis();
				while (isOkToRunGameLoop) {
					Long elapsedTime = System.currentTimeMillis() - startTime;
					updateWorld(elapsedTime);
					drawSurface();
				}
			}
		};
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isOkToRunGameLoop = false;
		super.surfaceDestroyed(holder);
	}

	@Override
	protected void drawSurface(Canvas canvas) {
		backGround_drwbl.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		backGround_drwbl.draw(canvas);
		for (CenteredDrawable movableShape : drawables) {
			movableShape.draw(canvas);
		}
	}

}
