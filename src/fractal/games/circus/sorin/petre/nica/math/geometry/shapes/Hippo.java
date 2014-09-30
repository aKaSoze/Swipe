package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;
import fractal.games.swipe.R;

public class Hippo extends Painting {

	public Hippo(Context context, LayoutProportions layoutProportions) {
		super(context, layoutProportions, R.drawable.hippo_wacky);
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);
		if (getBounds() != null) {
			if (crossedLeftSideBoundry()) {
				moveToLeftSideBoundry();
				reverseVelocityAlongX();
			}
			if (crossedRightSideBoundry()) {
				moveToRightSideBoundry();
				reverseVelocityAlongX();
			}
		}
	}

}
