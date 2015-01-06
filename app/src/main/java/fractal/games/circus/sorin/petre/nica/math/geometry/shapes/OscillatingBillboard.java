package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.collections.Tuple2;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class OscillatingBillboard extends Billboard {

    @Expose
    private Double allowedTranslation;

    public OscillatingBillboard() {
        super();
    }

    @SafeVarargs
    public OscillatingBillboard(LayoutProportions layoutProportions, Double xTranslation, Velocity velocity, Tuple2<Integer, Long>... slides) {
        super(layoutProportions, slides);
        this.allowedTranslation = xTranslation;
        this.velocity = velocity;
    }

    @Override
    public void updateState(Long elapsedTime) {
        super.updateState(elapsedTime);
        if ((center.distanceTo(evalOriginalCenter()) >= allowedTranslation && velocity.x > 0) || (center.x <= evalOriginalCenter().x && velocity.x < 0)) {
            velocity.reverse();
        }
    }

    @Override
    public void onCollision(AnimatedShape obstacle) {
        velocity.reverse();
    }

}
