package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public abstract class CenteredDrawable extends Drawable {

    public static final WeakHashMap<CenteredDrawable, Object> instances = new WeakHashMap<CenteredDrawable, Object>();

    public static final Displacement ORIGIN_CENTER = new Displacement(0, 0);

    private static final Paint DEFAULT_PAINT;

    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.CYAN);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    public enum Property {
        MOVABLE, CLONEABLE
    }

    private static final   Long   DOUBLE_TAP_TIME   = 300L;
    protected static final Double VECINITY_DISTANCE = 50.0;

    @Expose
    public Set<Property> properties      = new HashSet<Property>();
    @Expose
    public Displacement  center          = ORIGIN_CENTER;
    @Expose
    public Displacement  drawTranslation = new Displacement(0, 0);

    @Expose
    public LayoutProportions layoutProportions;

    protected transient final Paint paint;

    private transient   Long lastTapTime    = 0L;
    protected transient Long ownElapsedTime = 0L;

    public CenteredDrawable(LayoutProportions layoutProportions, Paint paint) {
        super();
        instances.put(this, null);
        this.layoutProportions = layoutProportions;
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

    protected abstract void init();

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

    public void updateState(Long elapsedTime, Long timeIncrement) {
        ownElapsedTime += timeIncrement;
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
        if (center == ORIGIN_CENTER) {
            center = evalOriginalCenter(bounds);
        }
    }

    public Displacement evalOriginalCenter() {
        return evalOriginalCenter(getBounds());
    }

    private Displacement evalOriginalCenter(Rect bounds) {
        Double x = layoutProportions.xRatio * bounds.width();
        Double y = layoutProportions.yRatio * bounds.height();
        return new Displacement(x, y);
    }

    protected Displacement evalDrawCenter() {
        return evalDrawLocation(center);
    }

    protected Double evalWidth() {
        return layoutProportions.widthRatio * getBounds().width();
    }

    protected Double evalHalfWidth() {
        return evalWidth() / 2.0;
    }

    protected Double evalHeight() {
        return layoutProportions.heightRatio * getBounds().height();
    }

    protected Double evalHalfHeight() {
        return evalHeight() / 2.0;
    }

    public Displacement evalDrawLocation(Displacement realLocation) {
        if (realLocation == null) {
            return null;
        } else {
            Displacement drawLocation = new Displacement(evalDrawX(realLocation.x), evalDrawY(realLocation.y));
            drawLocation.applyPoint = evalDrawLocation(realLocation.applyPoint);
            return drawLocation;
        }
    }

    protected Double evalRealX(Double drawX) {
        return drawX - drawTranslation.x;
    }

    protected Double evalRealY(Double drawY) {
        return getBounds().height() - (drawY + drawTranslation.y);
    }

    protected Double evalDrawX(Double realX) {
        return realX + drawTranslation.x;
    }

    protected Double evalDrawY(Double realY) {
        return getBounds().height() - (realY + drawTranslation.y);
    }

    protected void drawVector(Displacement vector, Canvas canvas) {
        Bitmap line  = MediaStore.getScaledBitmap(R.drawable.line, vector.magnitude()+1, 50d);
        Double angle = vector.clockWiseXAxisAngleTo(new Displacement(1, 0));

        Log.i("angle", angle.toString());

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(line , 0, 0, line.getWidth(), line.getHeight(), matrix, true);

        Displacement origin = evalDrawLocation(vector.applyPoint);
        Displacement tip = evalDrawLocation(vector.additionVector(vector.applyPoint));

        canvas.drawBitmap(rotatedBitmap, origin.x.floatValue(), origin.y.floatValue(), paint);

        canvas.drawLine(origin.x.floatValue(), origin.y.floatValue(),
                tip.x.floatValue(), tip.y.floatValue(), paint);
    }

    protected void drawPoint(Displacement displacement, Canvas canvas) {
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        Displacement drawLocation = evalDrawLocation(displacement);
        canvas.drawCircle(drawLocation.x.floatValue(), drawLocation.y.floatValue(), 20, paint);
        canvas.restore();
    }
}
