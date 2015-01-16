package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

/**
 * Created by sorin on 16.01.2015.
 */
public class GameAnimation extends CenteredDrawable {

    private final Long totalAnimationTimeInMillis;

    protected Double progress = 0d;

    private Boolean isComplete = false;

    public AnimationEndedHandler animationEndedHandler;

    public interface AnimationEndedHandler {
        void onAnimationEnded(GameAnimation animation);
    }

    public GameAnimation(LayoutProportions layoutProportions, Long totalAnimationTimeInMillis) {
        super(layoutProportions);
        this.totalAnimationTimeInMillis = totalAnimationTimeInMillis;
    }

    @Override
    protected void init() {

    }

    @Override
    public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {

    }

    @Override
    public void onMove(Displacement translation) {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void updateState(Long elapsedTime, Long timeIncrement) {
        super.updateState(elapsedTime, timeIncrement);
        if (ownElapsedTime > totalAnimationTimeInMillis) {
            isComplete = true;
            progress = 1d;
            if (animationEndedHandler != null) {
                animationEndedHandler.onAnimationEnded(this);
            }
        } else {
            progress = ownElapsedTime.doubleValue() / totalAnimationTimeInMillis.doubleValue();
        }
    }

    public Boolean getIsComplete() {
        return isComplete;
    }
}
