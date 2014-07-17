package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class ValueCircle extends BitmapDrawable {

	private int			touchCount	= 0;

	public final Long	value;

	public Boolean		isDestroyed	= false;

	public ValueCircle(LayoutProportions layoutProportions, Displacement cornerToCorner, Bitmap bitmap, Long value) {
		super(layoutProportions, cornerToCorner, bitmap);
		this.value = value;
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
			touchCount++;
			if (touchCount > 1) {
				isDestroyed = true;
			}
		} else {
			super.onMotionEvent(motionEvent, touchPoint);
		}
	}

	private Long	time;

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
