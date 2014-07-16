package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public abstract class CenteredDrawable extends Drawable {

	private static final Paint	DEFAULT_PAINT;
	static {
		DEFAULT_PAINT = new Paint();
		DEFAULT_PAINT.setColor(Color.WHITE);
		DEFAULT_PAINT.setStyle(Style.STROKE);
		DEFAULT_PAINT.setStrokeWidth(4);
	}

	private static final Long	DOUBLE_TAP_TIME		= 300L;
	private static final Double	VECINITY_DISTANCE	= 50.0;

	public Displacement			center;

	public LayoutProportions	layoutProportions;

	protected final Paint		paint;

	private Long				lastTapTime			= 0L;

	public static class LayoutProportions {
		public final Long	widthRatio;
		public final Long	heightRatio;
		public final Long	xRatio;
		public final Long	yRatio;

		public LayoutProportions(Long widthRatio, Long heightRatio, Long xRatio, Long yRatio) {
			this.widthRatio = widthRatio;
			this.heightRatio = heightRatio;
			this.xRatio = xRatio;
			this.yRatio = yRatio;
		}
	}

	public CenteredDrawable(LayoutProportions layoutProportions, Paint paint) {
		this.paint = new Paint();
		this.paint.setColor(paint.getColor());
		this.paint.setStyle(paint.getStyle());
		this.paint.setStrokeWidth(paint.getStrokeWidth());
		this.paint.setAntiAlias(true);
		this.paint.setDither(true);
	}

	public CenteredDrawable(LayoutProportions layoutProportions) {
		this(layoutProportions, DEFAULT_PAINT);
	}

	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		if (touchPoint.distanceTo(center) < VECINITY_DISTANCE) {
			switch (motionEvent.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				Long now = System.currentTimeMillis();
				Long tapTime = now - lastTapTime;
				lastTapTime = now;
				if (tapTime < DOUBLE_TAP_TIME) {
					onDoubleTap(motionEvent, touchPoint);
				}
			}
		}
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		Long x = layoutProportions.xRatio * (bounds.right - bounds.left);
		Long y = layoutProportions.yRatio * (bounds.top - bounds.bottom);
		center = new Displacement(x, y);
	}

	protected Long getWidth() {
		return layoutProportions.widthRatio * (getBounds().right - getBounds().left);
	}

	protected Long getHeight() {
		return layoutProportions.heightRatio * (getBounds().top - getBounds().bottom);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	@Override
	public void setAlpha(int alpha) {
		paint.setAlpha(alpha % 256);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
	}

	public abstract void updateState(Long elapsedTime);

	public abstract void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint);
}
