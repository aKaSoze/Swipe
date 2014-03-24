package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.ShapeDrawable;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public abstract class CenteredDrawable extends ShapeDrawable {

    protected Point2D center;

    public CenteredDrawable(Point2D center) {
        this.center = center;
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
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub

    }

}
