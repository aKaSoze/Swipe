package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public abstract class CenteredDrawable extends Drawable {

    protected static final Paint DEFAULT_PAINT;
    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    private Point2D              center;

    protected Paint              paint;

    public Integer               boundingBoxRight;

    public CenteredDrawable(Point2D center, Paint paint) {
        this.center = center;
        this.paint = new Paint();
        this.paint.setColor(paint.getColor());
        this.paint.setStyle(paint.getStyle());
        this.paint.setStrokeWidth(paint.getStrokeWidth());
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
    }

    public abstract void onMotionEvent(MotionEvent motionEvent);

    public abstract void updateState(Long elapsedTime);

    public void setCenter(Point2D newCenter) {
        center = newCenter;
    }

    public Point2D getCenter() {
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
