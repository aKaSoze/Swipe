package fractal.games.circus.sorin.petre.nica.persistence;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.gson.annotations.Expose;

import java.util.Set;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.AnimatedShape;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RepeatedSprite;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sprite;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;
import fractal.games.circus.sorin.petre.nica.views.Score;

/**
 * Created by sorin on 16.12.2014.
 */
public class Game {

    private static final Paint DEFAULT_PAINT;

    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Paint.Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    public Score inGameTimer = new Score(new LayoutProportions(0.0, 0.04, 0.7, 0.04));

    @Expose
    private Score score;

    @Expose
    private RepeatedSprite lives;

    @Expose
    public Stage stage;

    public StageLoader stageLoader;

    private Rect knownBounds;

    private Bitmap backGround_drwbl;

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
                loadCurrentStage(knownBounds);
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
        return stage.getHippo().center.y > maxY + 1000;
    }

    public void loadCurrentStage() {
        loadCurrentStage(knownBounds);
    }

    private void loadCurrentStage(Rect bounds) {
        knownBounds = bounds;
        stage = stageLoader.loadCurrentStage();
        stage.initObjects(bounds);
        stage.getHippo().centerChangedHandler = hippoCenterChangedHandler;
    }

    public void setStageLoader(StageLoader stageLoader) {
        this.stageLoader = stageLoader;
    }

    public void loadNextStage(Rect bounds) {
        stageLoader.selectNextStage();
        loadCurrentStage(bounds);
    }

    public void setKnownBounds(Rect knownBounds) {
        this.knownBounds = knownBounds;

        backGround_drwbl = MediaStore.getScaledBitmap(R.drawable.background, knownBounds.width(), knownBounds.height());
        for (CenteredDrawable drawable : stage.getAllObjects()) {
            drawable.setBounds(knownBounds);
        }

        score.setBounds(knownBounds);
        lives.setBounds(knownBounds);
        inGameTimer.setBounds(knownBounds);
    }

    public void loadPreviousStage(Rect bounds) {
        stageLoader.selectPreviousStage();
        loadCurrentStage(bounds);
    }

    public void addWorldObject(Sprite sprite) {
        if(knownBounds != null) {
            sprite.setBounds(knownBounds);
        }
        stage.addWorldObject(sprite);
    }

    private interface NumericRepresentant<T> {
        Double getNumericValue(T t);
    }

    private <T> Double max(Set<T> ts, NumericRepresentant<T> numericRepresentant) {
        Double max = Double.MIN_VALUE;
        for (T t : ts) {
            if (numericRepresentant.getNumericValue(t) > max) {
                max = numericRepresentant.getNumericValue(t);
            }
        }
        return max;
    }

    public void draw(Canvas canvas, Displacement coordinateTranslation) {
        canvas.drawBitmap(backGround_drwbl, 0, 0, DEFAULT_PAINT);
        for (CenteredDrawable centeredDrawable : stage.getAllObjects()) {
            centeredDrawable.drawTranslation.setComponents(coordinateTranslation.x, coordinateTranslation.y);
            centeredDrawable.draw(canvas);
        }

        lives.center = lives.evalOriginalCenter();
        lives.center.y -= coordinateTranslation.y;
        lives.drawTranslation.setComponents(coordinateTranslation.x, coordinateTranslation.y);
        lives.draw(canvas);

        score.draw(canvas);
        inGameTimer.points = (long) CenteredDrawable.instances.size();
        inGameTimer.draw(canvas);
    }

}
