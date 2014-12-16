package fractal.games.circus.sorin.petre.nica.persistence;

import android.graphics.Canvas;

import com.google.gson.annotations.Expose;

import java.util.Set;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.AnimatedShape;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RepeatedSprite;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sprite;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;
import fractal.games.circus.sorin.petre.nica.views.Score;

/**
 * Created by sorin on 16.12.2014.
 */
public class Game {

    @Expose
    private Score score;

    @Expose
    private RepeatedSprite lives;

    @Expose
    private Stage stage;

    private StageLoader stageLoader;

    private Game() {
    }

    public Game(StageLoader stageLoader) {
        this.stageLoader = stageLoader;
        score = new Score(new LayoutProportions(0.0, 0.04, 0.03, 0.04));
        lives = new RepeatedSprite(new LayoutProportions(0.09, 0.05, 0.5, 0.98), R.drawable.hippo_wacky);
        loadCurrentStage();
    }

    private AnimatedShape.CenterChangedHandler hippoCenterChangedHandler = new AnimatedShape.CenterChangedHandler() {
        @Override
        public void onCenterChanged(AnimatedShape shape) {
            if (isWinConditionMet()) {
                stageLoader.selectNextStage();
                loadCurrentStage();
            }
            if (isLoseConditionMet()) {
                shape.velocity.neutralize();
                shape.center.makeEqualTo(stage.getHippo().getFirstPosition());
                lives.decreaseRepeatFactor();
            }
        }
    };

    private Boolean isLoseConditionMet() {
        return stage.getHippo().center.y < -20;
    }

    private Boolean isWinConditionMet() {
        Double maxY = max(stage.getPlatforms(), new NumericRepresentant<PropulsionPlatform>() {
            @Override
            public Double getNumericValue(PropulsionPlatform propulsionPlatform) {
                return propulsionPlatform.center.y;
            }
        });
        return stage.getHippo().center.y > maxY + 20;
    }

    private void loadCurrentStage() {
        stage = stageLoader.loadCurrentStage();
        stage.getHippo().centerChangedHandler = hippoCenterChangedHandler;
    }

    public void setIsOnEditMode(Boolean isOnEditMode) {
        if (isOnEditMode) {
            for (Sprite sprite : stage.getAllObjects()) {
                sprite.properties.add(CenteredDrawable.Property.MOVABLE);
            }
        } else {
            for (Sprite sprite : stage.getAllObjects()) {
                sprite.properties.remove(CenteredDrawable.Property.MOVABLE);
            }
        }
    }

    public void setStageLoader(StageLoader stageLoader) {
        this.stageLoader = stageLoader;
    }

    private interface NumericRepresentant<T> {
        Double getNumericValue(T t);
    }

    private <T> Double max(Set<T> ts, NumericRepresentant<T> numericRepresentant) {
        Double max = Double.MIN_VALUE;
        for(T t: ts) {
            if(numericRepresentant.getNumericValue(t) > max) {
                max = numericRepresentant.getNumericValue(t);
            }
        }
        return max;
    }

    public void draw(Canvas canvas) {

    }

}
