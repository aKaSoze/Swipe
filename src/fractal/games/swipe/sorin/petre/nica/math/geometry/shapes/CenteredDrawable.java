package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public abstract class CenteredDrawable extends Drawable {

    protected Point2D center;

    protected Paint   paint;

    public CenteredDrawable(Point2D center) {
        this.center = center;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    public abstract void onMotionEvent(MotionEvent motionEvent);

    public void setCenter(Point2D newCenter) {
        center = newCenter;
    }

    public Float distanceTo(Point2D point2d) {
        return center.distanceTo(point2d);
    }

    public abstract void updateState(Long elapsedTime);

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
