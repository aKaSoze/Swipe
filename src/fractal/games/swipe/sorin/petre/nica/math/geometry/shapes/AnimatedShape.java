package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public abstract class AnimatedShape {

    protected Point2D center;

    public AnimatedShape(Point2D center) {
        this.center = center;
    }

    public void react(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
        case MotionEvent.ACTION_MOVE:
            setCenter(Point2D.Factory.fromMotionEvent(motionEvent));
        }
    }

    public void setCenter(Point2D newCenter) {
        center = newCenter;
    }

    public Float distanceTo(Point2D point2d) {
        return center.distanceTo(point2d);
    }

    public abstract void draw(Canvas canvas);

    public abstract void updateState(Long elapsedTime);
}
