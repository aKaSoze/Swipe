package fractal.games.circus.sorin.petre.nica.persistence;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.AnimatedShape;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Background;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.GameAnimation;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.OscillatingBillboard;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RepeatedSprite;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.ScreenCoverAnimation;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sprite;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.views.Camera;
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
    private Score          score;
    @Expose
    private Score          level;
    @Expose
    private RepeatedSprite lives;
    @Expose
    public  Stage          stage;
    public  StageLoader    stageLoader;
    private Rect           knownBounds;
    private Background     backGround;

    private Set<GameAnimation> stopTheWorldAnimations = new HashSet<GameAnimation>();
    private Set<GameAnimation> inGameAnimations       = new HashSet<GameAnimation>();

    private Game() {
        init();
    }

    public Game(StageLoader stageLoader) {
        this.stageLoader = stageLoader;
        level = new Score("Level", new LayoutProportions(0.0, 0.028, 0.32, 0.025));
        score = new Score("Score", new LayoutProportions(0.0, 0.028, 0.57, 0.025));
        score.points = 100000L;
        lives = new RepeatedSprite(new LayoutProportions(0.07, 0.05, 0.17, 0.98), R.drawable.hippo_wacky);
        init();
        loadCurrentStage();
    }

    private void init() {
        Sprite sprite1 = new Sprite(new LayoutProportions(1d, 1d, 0.5, 0.5), R.drawable.background);
        Sprite sprite2 = new Sprite(new LayoutProportions(1d, 1d, 0.5, 1.5), R.drawable.calibrated_background);
        backGround = new Background(sprite1, sprite2);
    }

    private AnimatedShape.CenterChangedHandler hippoCenterChangedHandler = new AnimatedShape.CenterChangedHandler() {
        @Override
        public void onCenterChanged(AnimatedShape shape) {
            if (isWinConditionMet()) {
                stageLoader.selectNextStage();
                loadCurrentStage(knownBounds);
            }
            if (isLoseConditionMet()) {
                loseLife();
            }
        }
    };

    private Sensor.TipHitHandler           sensorHitHandler       = new Sensor.TipHitHandler() {
        @Override
        public void onTipHit() {
            loseLife();
        }
    };
    private AnimatedShape.CollisionHandler monkeyCollisionHandler = new AnimatedShape.CollisionHandler() {
        @Override
        public void onCollision(AnimatedShape obstacle) {
            if (obstacle == stage.getHippo()) {
                loseLife();
            }
        }
    };

    private Sensor.ObstaclePassedHandler goThroughSensorHandler = new Sensor.ObstaclePassedHandler() {
        @Override
        public void onObstaclePassed(AnimatedShape obstacle) {
            score.points += 500;
        }
    };

    private void loseLife() {
        ScreenCoverAnimation screenCoverAnimation = new ScreenCoverAnimation(1000L, false);
        screenCoverAnimation.setBounds(knownBounds);
        screenCoverAnimation.animationEndedHandler = new GameAnimation.AnimationEndedHandler() {
            @Override
            public void onAnimationEnded(GameAnimation animation) {
                stage.getHippo().velocity.neutralize();
                stage.getHippo().center.makeEqualTo(stage.getHippo().getFirstPosition());
                lives.decreaseRepeatFactor();
                ScreenCoverAnimation screenCoverAnimation = new ScreenCoverAnimation(1000L, true);
                screenCoverAnimation.setBounds(knownBounds);
                stopTheWorldAnimations.add(screenCoverAnimation);
            }
        };
        stopTheWorldAnimations.add(screenCoverAnimation);
    }

    private Boolean isLoseConditionMet() {
        return stage.getHippo().center.y < -knownBounds.height() / 2.5;
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

    private void loadCurrentStage() {
        loadCurrentStage(knownBounds);
    }

    public void loadCurrentStage(Rect bounds) {
        knownBounds = bounds;
        stage = stageLoader.loadCurrentStage();
        level.points = stageLoader.stageIndex;
        onLoad();
    }

    public void onLoad() {
        stage.onLoad(knownBounds);
        stage.getHippo().centerChangedHandler = hippoCenterChangedHandler;

        for (Sensor sensor : stage.getSensors()) {
            sensor.tipHitHandler = sensorHitHandler;
            sensor.obstaclePassedHandler = goThroughSensorHandler;
        }

        for (OscillatingBillboard oscillatingBillboard : stage.getOscillatingBillboards()) {
            oscillatingBillboard.collisionHandler = monkeyCollisionHandler;
        }

        stageLoader.stageIndex = level.getPoints().longValue();
    }

    public void loadNextStage(Rect bounds) {
        stageLoader.selectNextStage();
        loadCurrentStage(bounds);
    }

    public void setKnownBounds(Rect knownBounds) {
        this.knownBounds = knownBounds;

        MediaStore.getScaledBitmap(R.drawable.background, knownBounds.width(), knownBounds.height());
        for (CenteredDrawable drawable : stage.getAllObjects()) {
            drawable.setBounds(knownBounds);
        }

        backGround.setBounds(knownBounds);
        score.setBounds(knownBounds);
        level.setBounds(knownBounds);
        lives.setBounds(knownBounds);
        inGameTimer.setBounds(knownBounds);
    }

    public void loadPreviousStage(Rect bounds) {
        stageLoader.selectPreviousStage();
        loadCurrentStage(bounds);
    }

    public void addWorldObject(Sprite sprite) {
        stage.addWorldObject(sprite);
        onLoad();
    }

    public void update(Long elapsedTime, Long timeIncrement) {
        if (stopTheWorldAnimations.isEmpty()) {
            stage.update(elapsedTime, timeIncrement);
            for (GameAnimation gameAnimation : inGameAnimations) {
                gameAnimation.updateState(elapsedTime, timeIncrement);
            }
        } else {
            for (GameAnimation gameAnimation : stopTheWorldAnimations) {
                gameAnimation.updateState(elapsedTime, timeIncrement);
            }
        }
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

    public void draw(Canvas canvas, Camera camera) {
        backGround.draw(canvas, camera);
        for (CenteredDrawable centeredDrawable : stage.getAllObjects()) {
            centeredDrawable.drawTranslation.setComponents(camera.coordinateTranslation.x, camera.coordinateTranslation.y);
            centeredDrawable.draw(canvas);
        }

        lives.center = lives.evalOriginalCenter();
        lives.draw(canvas);

        level.draw(canvas);
        score.draw(canvas);
        inGameTimer.points = (long) CenteredDrawable.instances.size();

        Set<GameAnimation> removes = new HashSet<GameAnimation>();
        for (GameAnimation gameAnimation : stopTheWorldAnimations) {
            if (!gameAnimation.getIsComplete()) {
                gameAnimation.drawTranslation.setComponents(camera.coordinateTranslation.x, camera.coordinateTranslation.y);
                gameAnimation.draw(canvas);
            } else {
                removes.add(gameAnimation);
            }
        }
        stopTheWorldAnimations.removeAll(removes);

//        inGameTimer.draw(canvas);
    }

}
