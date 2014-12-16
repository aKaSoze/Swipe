package fractal.games.circus.sorin.petre.nica.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RammedSprite;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sprite;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.persistence.Game;
import fractal.games.circus.sorin.petre.nica.persistence.GameWorld;
import fractal.games.circus.sorin.petre.nica.persistence.Stage;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

    private static final Paint DEFAULT_PAINT;

    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    private Bitmap           backGround_drwbl;
    private CenteredDrawable selectedShape;
    private MediaPlayer      soundTrackPlayer;

    private Boolean      isOkToRunGameLoop     = false;
    public  Displacement coordinateTranslation = new Displacement();
    private Displacement realTouchPoint        = new Displacement();
    private Game         game                  = new Game();
    public  Hud          hud                   = new Hud();
    private Long         elapsedTime           = 0L;
    private Long         lastUpdateTime        = null;
    private Boolean      isOnEditMode          = false;

    public Boolean isSlidingUp   = false;
    public Boolean isSlidingDown = false;

    public static class Hud {
        public Set<RammedSprite> rammedPaintings = new HashSet<RammedSprite>();
    }

    public GameView(Context context) {
        super(context);
    }

    public void setIsOnEditMode(Boolean isOnEditMode) {
        this.isOnEditMode = isOnEditMode;
        game.setIsOnEditMode(isOnEditMode);
    }

    public Boolean getIsOnEditMode() {
        return isOnEditMode;
    }

    public void loadStage(Stage stage) {
        suspend();
        this.game.clear();

        world.lives.setBounds(getLeft(), getTop(), getRight(), getBottom());
        this.game.lives = world.lives;

        for (Sprite sprite : world.getAllObjects()) {
            sprite.init();
            sprite.setBounds(getLeft(), getTop(), getRight(), getBottom());
            this.game.addWorldObject(sprite);
            if (isOnEditMode) {
                sprite.properties.add(CenteredDrawable.Property.MOVABLE);
            } else {
                sprite.properties.remove(CenteredDrawable.Property.MOVABLE);
            }
        }
        resume();
    }

    public GameWorld getGame() {
        return game;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            backGround_drwbl = MediaStore.getScaledBitmap(R.drawable.background, getWidth(), getHeight());

            for (CenteredDrawable drawable : game.getAllObjects()) {
                drawable.setBounds(left, top, right, bottom);
            }
            for (RammedSprite rammedPainting : hud.rammedPaintings) {
                rammedPainting.setBounds(left, top, right, bottom);
            }
            game.lives.setBounds(left, top, right, bottom);

            if (game.score != null) {
                game.score.setBounds(left, top, right, bottom);
            }
            if (game.inGameTimer != null) {
                game.inGameTimer.setBounds(left, top, right, bottom);
            }
        }
    }

    public void addWorldObject(Sprite sprite) {
        sprite.setBounds(getLeft(), getTop(), getRight(), getBottom());
        game.addWorldObject(sprite);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        realTouchPoint.setComponents(event.getX() - getLeft() - coordinateTranslation.x, (getBottom() - getTop()) - (event.getY() + coordinateTranslation.y));
        for (CenteredDrawable centeredDrawable : game.getAllObjects()) {
            centeredDrawable.onMotionEvent(event, realTouchPoint);
        }
        for (RammedSprite rammedPainting : hud.rammedPaintings) {
            rammedPainting.onMotionEvent(event, realTouchPoint);
        }
        return true;
    }

    private CenteredDrawable evaluateTargetShape(Displacement touchPoint) {
        CenteredDrawable closestShape = null;
        double smallestDistance = 60;
        for (CenteredDrawable centeredDrawable : game.getAllObjects()) {
            double distanceToTouchPoint = centeredDrawable.center.distanceTo(touchPoint);
            if (distanceToTouchPoint < smallestDistance) {
                smallestDistance = distanceToTouchPoint;
                closestShape = centeredDrawable;
            }
        }
        return closestShape;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSurface(canvas);
    }

    @Override
    protected Runnable behavior() {
        return new Runnable() {
            @Override
            public void run() {
                isOkToRunGameLoop = true;
                Log.i(logTag, "Game loop started.");
                while (isOkToRunGameLoop) {
                    long now = System.currentTimeMillis();
                    elapsedTime += now - lastUpdateTime;
                    lastUpdateTime = now;
                    game.update(elapsedTime);
                    drawSurface();
                }
            }
        };
    }

    @Override
    public void suspend() {
        isOkToRunGameLoop = false;
        super.suspend();
    }

    @Override
    public void resume() {
        lastUpdateTime = System.currentTimeMillis();
        super.resume();
    }

    public void switchPauseState() {
        if (running) {
            suspend();
        } else {
            resume();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isOkToRunGameLoop = false;
        super.surfaceDestroyed(holder);
    }

    @Override
    protected void drawSurface(Canvas canvas) {
        if (!isOnEditMode) {
            coordinateTranslation.setComponents(0.0, (getHeight() / 2) - game.getHippo().center.y);
        } else {
            if (isSlidingUp) {
                coordinateTranslation.y -= 6;
            }
            if (isSlidingDown) {
                coordinateTranslation.y += 6;
            }
        }

        canvas.drawBitmap(backGround_drwbl, 0, 0, DEFAULT_PAINT);
        for (CenteredDrawable centeredDrawable : game.getAllObjects()) {
            centeredDrawable.drawTranslation.setComponents(coordinateTranslation.x, coordinateTranslation.y);
            centeredDrawable.draw(canvas);
        }

        if (isOnEditMode) {
            for (RammedSprite rammedPainting : hud.rammedPaintings) {
                rammedPainting.center = rammedPainting.evalOriginalCenter();
                rammedPainting.center.y -= coordinateTranslation.y;
                rammedPainting.drawTranslation.setComponents(coordinateTranslation.x, coordinateTranslation.y);
                rammedPainting.draw(canvas);
            }
        }

        game.lives.center = game.lives.evalOriginalCenter();
        game.lives.center.y -= coordinateTranslation.y;
        game.lives.drawTranslation.setComponents(coordinateTranslation.x, coordinateTranslation.y);
        game.lives.draw(canvas);

        if (game.score != null) {
            game.score.draw(canvas);
        }
        if (game.inGameTimer != null) {
            game.inGameTimer.points = (long) CenteredDrawable.instances.size();
            game.inGameTimer.draw(canvas);
        }

        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);

        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);

        paint.setColor(Color.WHITE);
        Displacement deathLine = game.getHippo().evalDrawLocation(new Displacement(0, -20));
        canvas.drawLine(0, deathLine.y.floatValue(), getWidth(), deathLine.y.floatValue(), paint);
    }
}
