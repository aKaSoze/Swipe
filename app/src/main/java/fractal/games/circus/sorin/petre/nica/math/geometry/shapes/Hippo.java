package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Hippo extends Sprite {

    public Hippo() {
        super();
    }

    public HippoDeathHandler deathHandler;

    public interface HippoDeathHandler {
        void onHipposDeath();
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

        if (center.y < -20) {
            if (deathHandler != null) {
                deathHandler.onHipposDeath();
            }
        }
    }

}
