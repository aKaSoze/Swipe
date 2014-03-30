package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;

public class ValueCircle extends BitmapDrawable {

    private int       touchCount  = 0;

    public final Long value;

    public Boolean    isDestroyed = false;

    public ValueCircle(Segment2D cornerToCorner, Bitmap bitmap, Long value) {
        super(cornerToCorner, bitmap);
        this.value = value;
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touchCount++;
            if (touchCount > 0) {
                isDestroyed = true;
            }
        } else {
            super.onMotionEvent(motionEvent);
        }
    }

    private Long time;

    @Override
    public void updateState(Long elapsedTime) {
        super.updateState(elapsedTime);
        if (time == null) {
            time = elapsedTime;
        }

        Float alpha = 255 * (1 - ((float) (elapsedTime - time) / 9000));
        setAlpha(alpha.intValue());
        if (elapsedTime - time > 9000) {
            isDestroyed = true;
        }
    }
}
