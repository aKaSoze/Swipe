package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public class ValueCircle extends CircleDrawable {

    private int       touchCount  = 0;

    public final Long value;

    public boolean    isDestroyed = false;

    public ValueCircle(Point2D center, Float radius, Long value) {
        super(center, radius);
        this.value = value;
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touchCount++;
            if (touchCount > 2) {
                isDestroyed = true;
            }
        } else {
            super.onMotionEvent(motionEvent);
        }
    }

}
