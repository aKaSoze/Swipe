package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Rect;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Hippo extends Sprite {

    public Hippo() {
        super();
    }

    private Displacement firstPosition;

    public Hippo(LayoutProportions layoutProportions) {
        super(layoutProportions, R.drawable.hippo_wacky);
    }

    @Override
    public void updateState(Long elapsedTime, Long timeIncrement) {
        super.updateState(elapsedTime, timeIncrement);
        if (getBounds() != null) {
            if (crossedLeftSideBoundary()) {
                moveToLeftSideBoundary();
                reverseVelocityAlongX();
            }
            if (crossedRightSideBoundary()) {
                moveToRightSideBoundary();
                reverseVelocityAlongX();
            }
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (firstPosition == null) {
            firstPosition = center.cloneVector();
        }
    }

    public Displacement getFirstPosition() {
        return firstPosition.cloneVector();
    }
}
