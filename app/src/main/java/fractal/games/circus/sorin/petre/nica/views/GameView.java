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
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RammedPainting;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.persistence.GameWorld;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

    private static final Paint DEFAULT_PAINT;

    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    private Bitmap backGround_drwbl;

    private CenteredDrawable selectedShape;

    private Boolean isOkToRunGameLoop = false;

    private MediaPlayer soundTrackPlayer;

    private Displacement coordinateTransaltion = new Displacement();

    private Displacement realTouchPoint = new Displacement();

    private GameWorld world = new GameWorld();

    public Hud hud = new Hud();

    private Long elapsedTime = 0L;

    private Long lastUpdateTime = null;

    private Boolean isOnEditMode = false;

    public static class Hud {
        public Score score;

        public Score inGameTimer;

        public Set<RammedPainting> rammedPaintings = new HashSet<RammedPainting>();
    }

    public GameView(Context context) {
        super(context);
    }

    public void setIsOnEditMode(Boolean isOnEditMode) {
        this.isOnEditMode = isOnEditMode;
        if (isOnEditMode) {
            for (Painting painting : world.getAllObjects()) {
                painting.properties.add(CenteredDrawable.Property.MOVABLE);
            }
        } else {
            for (Painting painting : world.getAllObjects()) {
                painting.properties.remove(CenteredDrawable.Property.MOVABLE);
            }
        }
    }

    public Boolean getIsOnEditMode() {
        return isOnEditMode;
    }

    public void loadWorld(GameWorld world) {
        suspend();
        this.world.clear();
        for (Painting painting : world.getAllObjects()) {
            painting.init();
            painting.setBounds(getLeft(), getTop(), getRight(), getBottom());
            painting.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
            this.world.addWorldObject(painting);
        }
        resume();
    }

    public GameWorld getWorld() {
        return world;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            backGround_drwbl = MediaStore.getScaledBitmap(R.drawable.background, getWidth(), getHeight());

            for (CenteredDrawable drawable : world.getAllObjects()) {
                drawable.setBounds(left, top, right, bottom);
                drawable.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
            }
            for (RammedPainting rammedPainting : hud.rammedPaintings) {
                rammedPainting.setBounds(left, top, right, bottom);
            }
            if (hud.score != null) {
                hud.score.setBounds(left, top, right, bottom);
            }
            if (hud.inGameTimer != null) {
                hud.inGameTimer.setBounds(left, top, right, bottom);
            }
        }
    }

    public void addWorldObject(Painting painting) {
        painting.setBounds(getLeft(), getTop(), getRight(), getBottom());
        painting.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
        world.addWorldObject(painting);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        realTouchPoint.setComponents(event.getX() - getLeft() - coordinateTransaltion.x, getBottom() - (event.getY() + coordinateTransaltion.y));
        for (CenteredDrawable centeredDrawable : world.getAllObjects()) {
            centeredDrawable.onMotionEvent(event, realTouchPoint);
        }
        for (RammedPainting rammedPainting : hud.rammedPaintings) {
            rammedPainting.onMotionEvent(event, realTouchPoint);
        }
        return true;
    }

    private CenteredDrawable evaluateTargetShape(Displacement touchPoint) {
        CenteredDrawable closestShape = null;
        double smallestDistance = 60;
        for (CenteredDrawable centeredDrawable : world.getAllObjects()) {
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

    public void updateWorld(Long elapsedTime) {
        for (CenteredDrawable movableShape : world.getAllObjects()) {
            movableShape.updateState(elapsedTime);
        }
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
                    updateWorld(elapsedTime);
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
        coordinateTransaltion.setComponents(0.0, (getHeight() / 2) - world.getHippo().center.y);
        canvas.drawBitmap(backGround_drwbl, 0, 0, DEFAULT_PAINT);
        for (CenteredDrawable centeredDrawable : world.getAllObjects()) {
            centeredDrawable.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
            centeredDrawable.draw(canvas);
        }

        if (isOnEditMode) {
            for (RammedPainting rammedPainting : hud.rammedPaintings) {
                rammedPainting.center = rammedPainting.evalOriginalCenter();
                rammedPainting.center.y -= coordinateTransaltion.y;
                rammedPainting.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
                rammedPainting.draw(canvas);
            }
        }

        if (hud.score != null) {
            hud.score.draw(canvas);
        }
        if (hud.inGameTimer != null) {
            hud.inGameTimer.points = (long) CenteredDrawable.instances.size();
            hud.inGameTimer.draw(canvas);
        }
    }
}
