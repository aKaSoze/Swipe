package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public abstract class CenteredDrawable extends Drawable {

    protected static final Paint DEFAULT_PAINT;
    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    private static final Long    DOUBLE_TAP_TIME   = 300L;
    private static final Double  VECINITY_DISTANCE = 50.0;

    private Displacement         center;

    protected Paint              paint;

    public Integer               boundingBoxRight;

    private Long                 lastTapTime       = 0L;

    public CenteredDrawable(Displacement center, Paint paint) {
        this.center = center;
        this.paint = new Paint();
        this.paint.setColor(paint.getColor());
        this.paint.setStyle(paint.getStyle());
        this.paint.setStrokeWidth(paint.getStrokeWidth());
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
    }

    public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        if (touchPoint.distanceTo(getCenter()) < VECINITY_DISTANCE) {
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

    public abstract void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint);

    public abstract void updateState(Long elapsedTime);

    public void setCenter(Displacement newCenter) {
        center = newCenter;
    }

    public Displacement getCenter() {
        return center;
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

}
