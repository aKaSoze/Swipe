package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

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

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);
		Double alpha = 255 * (1 - (timeIncrement / 9000.0));
		setAlpha(alpha.intValue());
		if (timeIncrement > 9000) {
			isDestroyed = true;
		}
	}
}
