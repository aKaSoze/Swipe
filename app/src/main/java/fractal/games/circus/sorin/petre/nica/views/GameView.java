package fractal.games.circus.sorin.petre.nica.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RammedSprite;
import fractal.games.circus.sorin.petre.nica.persistence.Game;
import fractal.games.circus.sorin.petre.nica.persistence.Stage;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

    public Game game;

    private CenteredDrawable selectedShape;
    private MediaPlayer      soundTrackPlayer;

    private Boolean      isOkToRunGameLoop     = false;
    public  Displacement coordinateTranslation = new Displacement();
    private Displacement realTouchPoint        = new Displacement();
    public  Hud          hud                   = new Hud();
    private Long         elapsedTime           = 0L;
    private Long         lastUpdateTime        = null;
    private Boolean      isOnEditMode          = false;

    public Boolean isSlidingUp   = false;
    public Boolean isSlidingDown = false;

    public static class Hud {
        public Set<RammedSprite> rammedPaintings = new HashSet<RammedSprite>();
    }

    public GameView(Context context, Game game) {
        super(context);
        this.game = game;
    }

    public void setIsOnEditMode(Boolean isOnEditMode) {
        this.isOnEditMode = isOnEditMode;
        game.stage.setIsOnEditMode(isOnEditMode);
    }

    public Boolean getIsOnEditMode() {
        return isOnEditMode;
    }

    public void loadNextStage() {
        suspend();
        game.loadNextStage(new Rect(getLeft(), getTop(), getRight(), getBottom()));
        resume();
    }

    public void loadPreviousStage() {
        suspend();
        game.loadPreviousStage(new Rect(getLeft(), getTop(), getRight(), getBottom()));
        resume();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            Rect bounds = new Rect(left, top, right, bottom);
            for (RammedSprite rammedPainting : hud.rammedPaintings) {
                rammedPainting.setBounds(bounds);
            }
            game.setKnownBounds(bounds);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        realTouchPoint.setComponents(event.getX() - getLeft() - coordinateTranslation.x, (getBottom() - getTop()) - (event.getY() + coordinateTranslation.y));
        for (CenteredDrawable centeredDrawable : game.stage.getAllObjects()) {
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
        for (CenteredDrawable centeredDrawable : game.stage.getAllObjects()) {
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
                    game.stage.update(elapsedTime);
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
            coordinateTranslation.setComponents(0.0, (getHeight() / 2) - game.stage.getHippo().center.y);
        } else {
            if (isSlidingUp) {
                coordinateTranslation.y -= 6;
            }
            if (isSlidingDown) {
                coordinateTranslation.y += 6;
            }
        }

        game.draw(canvas, coordinateTranslation);
        drawHud(canvas);
//        drawHelperLines(canvas);
    }

    private void drawHelperLines(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);

        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);

        paint.setColor(Color.WHITE);

        Displacement deathLine = game.stage.getHippo().evalDrawLocation(new Displacement(0, -20));
        canvas.drawLine(0, deathLine.y.floatValue(), getWidth(), deathLine.y.floatValue(), paint);
    }

    private void drawHud(Canvas canvas) {
        if (isOnEditMode) {
            for (RammedSprite rammedPainting : hud.rammedPaintings) {
                rammedPainting.center = rammedPainting.evalOriginalCenter();
                rammedPainting.center.y -= coordinateTranslation.y;
                rammedPainting.drawTranslation.setComponents(coordinateTranslation.x, coordinateTranslation.y);
                rammedPainting.draw(canvas);
            }
        }
    }
}
