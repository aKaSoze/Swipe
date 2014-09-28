package fractal.games.swipe.sorin.petre.nica.views;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.google.gson.annotations.Expose;

import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

    private static final Paint DEFAULT_PAINT;
    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    private Bitmap             backGround_drwbl      = BitmapFactory.decodeResource(getResources(), R.drawable.background);

    private CenteredDrawable   selectedShape;

    private Boolean            isOkToRunGameLoop     = false;

    private MediaPlayer        soundTrackPlayer;

    public Score               score;

    private Displacement       coordinateTransaltion = new Displacement();

    private Displacement       realTouchPoint        = new Displacement();

    private World              world                 = new World();

    public static class World {
        @Expose
        public Painting            followedObject;

        @Expose
        public final Set<Painting> centeredDrawables = new CopyOnWriteArraySet<Painting>();
    }

    public GameView(Context context) {
        super(context);
    }

    public void loadWorld(World world) {
        this.world = world;
        for (Rectangle rectangle : world.centeredDrawables) {
            rectangle.context = getContext();
            rectangle.setBounds(getLeft(), getTop(), getRight(), getBottom());
            rectangle.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
        }
    }

    public World getWorld() {
        return world;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            world.followedObject.setBounds(left, top, right, bottom);
            for (CenteredDrawable drawable : world.centeredDrawables) {
                drawable.setBounds(left, top, right, bottom);
                drawable.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
            }
            backGround_drwbl = Bitmap.createScaledBitmap(backGround_drwbl, getWidth(), getHeight(), true);
            if (score != null) {
                score.setBounds(left, top, right, bottom);
            }
            Log.i("GameView", "layout changed");
        }
    }

    public void addWorldObject(Painting drawable) {
        drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
        drawable.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
        world.centeredDrawables.add(drawable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        realTouchPoint.setComponents(Double.valueOf(event.getX()), coordinateTransaltion.y - event.getY());
        for (CenteredDrawable centeredDrawable : world.centeredDrawables) {
            centeredDrawable.onMotionEvent(event, realTouchPoint);
        }
        return true;
    }

    private CenteredDrawable evaluateTargetShape(Displacement touchPoint) {
        CenteredDrawable closestShape = null;
        double smallestDistance = 60;
        for (CenteredDrawable centeredDrawable : world.centeredDrawables) {
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
        for (CenteredDrawable movableShape : world.centeredDrawables) {
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
                Long startTime = System.currentTimeMillis();
                while (isOkToRunGameLoop) {
                    Long elapsedTime = System.currentTimeMillis() - startTime;
                    updateWorld(elapsedTime);
                    drawSurface();
                }
            }
        };
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isOkToRunGameLoop = false;
        super.surfaceDestroyed(holder);
    }

    @Override
    protected void drawSurface(Canvas canvas) {
        coordinateTransaltion.setComponents(Double.valueOf(getLeft()), (getHeight() / 2) + world.followedObject.center.y);
        canvas.drawBitmap(backGround_drwbl, 0, 0, DEFAULT_PAINT);
        for (CenteredDrawable centeredDrawable : world.centeredDrawables) {
            centeredDrawable.drawTranslation.setComponents(coordinateTransaltion.x, coordinateTransaltion.y);
            centeredDrawable.draw(canvas);
        }
        if (score != null) {
            score.draw(canvas);
        }
    }
}
