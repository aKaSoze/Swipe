package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public abstract class CenteredDrawable extends Drawable {

	private static final Paint	DEFAULT_PAINT;
	static {
		DEFAULT_PAINT = new Paint();
		DEFAULT_PAINT.setColor(Color.WHITE);
		DEFAULT_PAINT.setStyle(Style.STROKE);
		DEFAULT_PAINT.setStrokeWidth(4);
	}

	public enum Property {
		MOVABLE, CLONEABLE;
	}

	private static final Long		DOUBLE_TAP_TIME		= 300L;
	protected static final Double	VECINITY_DISTANCE	= 50.0;

	protected final Context			context;

	public final Set<Property>		properties			= new HashSet<Property>();

	public Displacement				center				= new Displacement(0, 0);

	private Displacement			drawCenter			= new Displacement(0, 0);

	public Displacement				drawTranslation		= new Displacement(0, 0);

	public LayoutProportions		layoutProportions;

	protected final Paint			paint;

	private Long					lastTapTime			= 0L;

	protected Long					lastUpdateTime;

	protected Long					timeIncrement		= 0L;

	public CenteredDrawable(Context context, LayoutProportions layoutProportions, Paint paint) {
		this.context = context;
		this.paint = new Paint();
		this.layoutProportions = layoutProportions;
		this.paint.setColor(paint.getColor());
		this.paint.setStyle(paint.getStyle());
		this.paint.setStrokeWidth(paint.getStrokeWidth());
		this.paint.setAntiAlias(true);
		this.paint.setDither(true);
	}

	public CenteredDrawable(Context context, LayoutProportions layoutProportions) {
		this(context, layoutProportions, DEFAULT_PAINT);
	}

	public abstract void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint);

	public abstract void onMove(Displacement translation);

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
				break;
			case MotionEvent.ACTION_MOVE:
				if (properties.contains(Property.MOVABLE)) {
					Displacement translation = touchPoint.subtractionVector(center);
					center.makeEqualTo(touchPoint);
					onMove(translation);
				}
				break;
			}
		}
	}

	public void updateState(Long elapsedTime) {
		if (lastUpdateTime != null) {
			timeIncrement = elapsedTime - lastUpdateTime;
		}
		lastUpdateTime = elapsedTime;
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

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		Double x = layoutProportions.xRatio * (bounds.right - bounds.left);
		Double y = layoutProportions.yRatio * (bounds.bottom - bounds.top);
		center.setComponents(x, y);
	}

	protected Displacement evalDrawCenter() {
		drawCenter.setComponents(center.x + drawTranslation.x, drawTranslation.y - center.y);
		return drawCenter;
	}

	protected Double evalWidth() {
		return layoutProportions.widthRatio * (getBounds().right - getBounds().left);
	}

	protected Double evalHalfWidth() {
		return evalWidth() / 2.0;
	}

	protected Double evalHeight() {
		return layoutProportions.heightRatio * (getBounds().bottom - getBounds().top);
	}

	protected Double evalHalfHeight() {
		return evalHeight() / 2.0;
	}

	protected void drawVector(Vector2D<?> vector, Canvas canvas) {
		float strecthPointOriginX = vector.applyPoint.x.floatValue();
		float strecthPointOriginY = drawTranslation.y.floatValue() - vector.applyPoint.y.floatValue();

		float strecthPointX = vector.applyPoint.x.floatValue() + vector.x.floatValue();
		float strecthPointY = drawTranslation.y.floatValue() - vector.applyPoint.y.floatValue() - vector.y.floatValue();

		canvas.drawLine(strecthPointOriginX, strecthPointOriginY, strecthPointX, strecthPointY, paint);
	}

	public Map<String, Object> toSerialForm() {
		Map<String, Object> serialization = new HashMap<String, Object>();
		serialization.put("layoutProportions", layoutProportions);
		serialization.put("class", getClass());

		return serialization;
	}

}
