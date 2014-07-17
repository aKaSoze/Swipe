package fractal.games.swipe.sorin.petre.nica.views;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

	private Integer						left;
	private Integer						top;
	private Integer						right;
	private Integer						bottom;

	private final ColorDrawable			backGround_drwbl	= new ColorDrawable(Color.BLACK);

	private CenteredDrawable			selectedShape;

	private Boolean						isOkToRunGameLoop	= false;

	public final Set<CenteredDrawable>	centeredDrawables	= new CopyOnWriteArraySet<CenteredDrawable>();

	private MediaPlayer					soundTrackPlayer;

	public Score						score;

	public GameView(Context context) {
		super(context);
		soundTrackPlayer = MediaPlayer.create(context, R.raw.crowded);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			for (CenteredDrawable drawable : centeredDrawables) {
				drawable.setBounds(left, top, right, bottom);
			}
			soundTrackPlayer.start();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		for (CenteredDrawable centeredDrawable : centeredDrawables) {
			centeredDrawable.onMotionEvent(event, Displacement.Factory.fromMotionEvent(event));
		}
		return true;
	}

	private CenteredDrawable evaluateTargetShape(Displacement touchPoint) {
		CenteredDrawable closestShape = null;
		double smallestDistance = 60;
		for (CenteredDrawable centeredDrawable : centeredDrawables) {
			double distanceToTouchPoint = centeredDrawable.center.distanceTo(touchPoint);
			if (distanceToTouchPoint < smallestDistance) {
				smallestDistance = distanceToTouchPoint;
				closestShape = centeredDrawable;
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
		for (CenteredDrawable movableShape : centeredDrawables) {
			movableShape.updateState(elapsedTime);
		}
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
		for (CenteredDrawable movableShape : centeredDrawables) {
			movableShape.draw(canvas);
		}
	}

}
