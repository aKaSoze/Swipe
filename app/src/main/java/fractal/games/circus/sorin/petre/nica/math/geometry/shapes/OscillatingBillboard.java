package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Rect;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.collections.Tuple2;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class OscillatingBillboard extends Billboard {

    public static final Long DISTANCE_FACTOR = 8L;

    private Double allowedTranslation;

    @Expose
    private Displacement startPosition = new Displacement();

    public OscillatingBillboard() {
        super();
    }

    @SafeVarargs
    public OscillatingBillboard(LayoutProportions layoutProportions, Velocity velocity, Tuple2<Integer, Long>... slides) {
        super(layoutProportions, slides);
        this.velocity = velocity;
    }

    @Override
    public void updateState(Long elapsedTime) {
        super.updateState(elapsedTime);
        if ((center.distanceTo(startPosition) >= allowedTranslation && velocity.x > 0) || (center.x <= startPosition.x && velocity.x < 0)) {
            velocity.reverse();
        }
    }

    @Override
    public void onMove(Displacement translation) {
        super.onMove(translation);
        startPosition.makeEqualTo(center);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        allowedTranslation = evalWidth() * DISTANCE_FACTOR;
    }
}
