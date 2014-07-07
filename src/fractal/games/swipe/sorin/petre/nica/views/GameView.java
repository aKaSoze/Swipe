package fractal.games.swipe.sorin.petre.nica.views;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle.Property;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

	private final ColorDrawable			backGround_drwbl	= new ColorDrawable(Color.BLACK);

	private CenteredDrawable			selectedShape		= null;

	private Boolean						isOkToRunGameLoop;

	public final Set<CenteredDrawable>	centeredDrawables	= new CopyOnWriteArraySet<CenteredDrawable>();

	public final Set<Drawable>			drawables			= new CopyOnWriteArraySet<Drawable>();

	public Rectangle					hippo;

	private Rectangle					firstObstacle;

	private Rectangle					secondObstacle;

	private MediaPlayer					crowded;

	public Score						score;

	public GameView(Context context) {
		super(context);
		Displacement platformCenter = new Displacement(200, 700);

		Bitmap originalHippo_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hippo_wacky);
		hippo = new Rectangle(platformCenter.additionVector(new Displacement(0, -96)), 85, 101);
		hippo.setBitmap(originalHippo_bmp);
		PropulsionPlatform propulsionPlatform = new PropulsionPlatform(platformCenter, 200.0, 20.0, hippo);
		propulsionPlatform.properties.add(Property.MOVABLE);
		propulsionPlatform.properties.add(Property.CLONEABLE);
		propulsionPlatform.scene = this;

		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.boing);
		crowded = MediaPlayer.create(context, R.raw.crowded);
		propulsionPlatform.boingSound = mediaPlayer;

		firstObstacle = new Rectangle(new Displacement(350, 455), 80, 80);
		firstObstacle.setFilled(true);
		firstObstacle.properties.add(Property.MOVABLE);
		firstObstacle.properties.add(Property.CLONEABLE);
		firstObstacle.scene = this;

		secondObstacle = new Rectangle(new Displacement(375, 235), 60, 60);
		secondObstacle.properties.add(Property.MOVABLE);
		secondObstacle.properties.add(Property.CLONEABLE);
		secondObstacle.scene = this;
		secondObstacle.setFilled(false);

		hippo.obstacles.add(firstObstacle);
		hippo.obstacles.add(secondObstacle);

		Score score = new Score(200L, 200L, 5L);

		centeredDrawables.add(propulsionPlatform);
		centeredDrawables.add(hippo);
		centeredDrawables.add(firstObstacle);
		centeredDrawables.add(secondObstacle);
		drawables.add(score);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		hippo.boundingBoxRight = right;
		crowded.start();
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
