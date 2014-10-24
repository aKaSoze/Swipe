package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Hippo extends Painting {

	public Hippo() {
		super();
	}

	public Hippo(LayoutProportions layoutProportions) {
		super(layoutProportions, R.drawable.hippo_wacky);
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
