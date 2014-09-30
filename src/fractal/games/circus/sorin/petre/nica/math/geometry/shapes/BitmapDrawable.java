package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class BitmapDrawable extends CenteredDrawable {

	private static final Long	EXPLOSION_DURATION_IN_MILLIS	= 1000L;

	private enum DrawState {
		Stable, Exploding, Exploded;
	}

	private class Tile {

		public Displacement		drawPos;
		public Bitmap			bitmap;
		public Canvas			ownCanvas;
		private final Camera	camera;
		private final Bitmap	clip;

		public Tile(Bitmap original, Displacement bounds) {
			bitmap = Bitmap.createBitmap(bounds.x.intValue(), bounds.y.intValue(), Bitmap.Config.ARGB_8888);
			clip = null;
			ownCanvas = new Canvas(bitmap);
			camera = new Camera();
		}

		public void spin(Long explosionProgress) {
			// int drawX = part.sourceX;
			// int drawY = part.sourceY;
			// int rotateX = 0;
			// int rotateY = 0;
			// int rotateZ = 0;
			// if (mStartTime == -1)
			// mStartTime = SystemClock.uptimeMillis();
			// final long delayedTime = SystemClock.uptimeMillis() - mStartTime;
			// int diffX = part.destX - part.sourceX;
			// int diffY = part.destY - part.sourceY;
			// float interpolation = anim.getInterpolation((float) delayedTime /
			// (float) mAnimDuration);
			// drawX = part.sourceX + (Math.round(diffX * interpolation));
			// drawY = part.sourceY + (Math.round(diffY * interpolation));
			// rotateX = Math.round(part.rotX * interpolation);
			// rotateY = Math.round(part.rotY * interpolation);
			// rotateZ = Math.round(part.rotZ * interpolation);
			// ownCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
			// ownCanvas.save();
			//
			// camera.save();
			// camera.rotateX(rotateX);
			// camera.rotateY(rotateY);
			// camera.rotateZ(rotateZ);
			// camera.applyToCanvas(ownCanvas);
			// camera.restore();
			//
			// ownCanvas.clipPath(part.triangle, part.op);
			// ownCanvas.drawBitmap(clip, 0, 0, null);
			// ownCanvas.restore();
		}

	}

	private final Bitmap	bitmap;

	private Displacement	cornerToCorner;

	private DrawState		drawState;

	private Long			explosionStartTime;

	private Long			explosionProgress;

	private Set<Tile>		tiles;

	public BitmapDrawable(Context context, LayoutProportions layoutProportions, Displacement cornerToCorner, Bitmap bitmap) {
		super(context, layoutProportions);
		this.cornerToCorner = cornerToCorner;
		this.bitmap = Bitmap.createScaledBitmap(bitmap, cornerToCorner.x.intValue(), cornerToCorner.y.intValue(), true);
		drawState = DrawState.Stable;
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_MOVE:
			center.makeEqualTo(touchPoint);
		}
	}

	@Override
	public void updateState(Long elapsedTime) {
		if (drawState == DrawState.Exploding) {
			if (explosionStartTime == null) {
				explosionStartTime = elapsedTime;
			}
			explosionProgress = elapsedTime - explosionStartTime;
			if (explosionProgress < EXPLOSION_DURATION_IN_MILLIS) {
				for (Tile tile : tiles) {
					tile.spin(explosionProgress);
				}
			} else {
				drawState = DrawState.Exploded;
			}
		}

	}

	@Override
	public void draw(Canvas canvas) {
		switch (drawState) {
		case Stable:
			canvas.drawBitmap(bitmap, cornerToCorner.applyPoint.x.floatValue(), cornerToCorner.applyPoint.y.floatValue(), paint);
		case Exploding:
			for (Tile tile : tiles) {
				canvas.drawBitmap(tile.bitmap, tile.drawPos.x.floatValue(), tile.drawPos.y.floatValue(), paint);
			}
		case Exploded:
			canvas.drawBitmap(bitmap, cornerToCorner.applyPoint.x.floatValue(), cornerToCorner.applyPoint.y.floatValue(), paint);
		}
	}

	@Override
	public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMove(Displacement translation) {
		// TODO Auto-generated method stub

	}

}