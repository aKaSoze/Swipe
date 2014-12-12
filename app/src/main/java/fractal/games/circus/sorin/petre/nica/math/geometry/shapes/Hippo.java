package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Rect;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Hippo extends Sprite {

    public Hippo() {
        super();
    }

    public HippoDeathHandler deathHandler;

    private Displacement firstPosition;

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
            if (crossedLeftSideBoundary()) {
                moveToLeftSideBoundary();
                reverseVelocityAlongX();
            }
            if (crossedRightSideBoundary()) {
                moveToRightSideBoundary();
                reverseVelocityAlongX();
            }
        }

        if (center.y < -20) {
            velocity.neutralize();
            center.makeEqualTo(firstPosition);
            if (deathHandler != null) {
                deathHandler.onHipposDeath();
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
}
