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
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

    private static final Paint          DEFAULT_PAINT;
    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    private Bitmap                      backGround_drwbl      = BitmapFactory.decodeResource(getResources(), R.drawable.normal);

    private CenteredDrawable            selectedShape;

    private Boolean                     isOkToRunGameLoop     = false;

    private final Set<CenteredDrawable> centeredDrawables     = new CopyOnWriteArraySet<CenteredDrawable>();

    private MediaPlayer                 soundTrackPlayer;

    public Score                        score;

    private Displacement                coordinateTransaltion = new Displacement();

    private Displacement                realTouchPoint        = new Displacement();

    public CenteredDrawable             followedObject;

    public GameView(Context context) {
        super(context);
    }

    public void addWorldObject(CenteredDrawable drawable) {
        drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
        drawable.drawTranslation.setComponents(coordinateTransaltion.getX(), coordinateTransaltion.getY());
        centeredDrawables.add(drawable);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            followedObject.setBounds(left, top, right, bottom);
            coordinateTransaltion.setComponents(Double.valueOf(left), bottom + followedObject.center.getY() + bottom / 2);
            for (CenteredDrawable drawable : centeredDrawables) {
                drawable.setBounds(left, top, right, bottom);
                drawable.drawTranslation.setComponents(coordinateTransaltion.getX(), coordinateTransaltion.getY());
            }
            backGround_drwbl = Bitmap.createScaledBitmap(backGround_drwbl, right - left, bottom - top, true);
            if (score != null) {
                score.setBounds(left, top, right, bottom);
            }
            Log.i("layout", "layout changed");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        realTouchPoint.setComponents(Double.valueOf(event.getX()), coordinateTransaltion.getY() - event.getY());
        for (CenteredDrawable centeredDrawable : centeredDrawables) {
            centeredDrawable.onMotionEvent(event, realTouchPoint);
        }
        return true;
    }

    private CenteredDrawable evaluateTargetShape(Displacement touchPoint) {
        CenteredDrawable closestShape = null;
        double smallestDistance = 60;
        for (CenteredDrawable centeredDrawable : centeredDrawables) {
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
        for (CenteredDrawable movableShape : centeredDrawables) {
            movableShape.updateState(elapsedTime);
        }
    }

    @Override
    protected Runnable getBehavior() {
        return new Runnable() {
            @Override
            public void run() {
                isOkToRunGameLoop = true;
                Log.d(logTag, "Game loop started.");
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
        coordinateTransaltion.setComponents(Double.valueOf(getLeft()), getBottom() + followedObject.center.getY() - (getBottom() / 2));
        canvas.drawBitmap(backGround_drwbl, 0, 0, DEFAULT_PAINT);
        for (CenteredDrawable centeredDrawable : centeredDrawables) {
            centeredDrawable.drawTranslation.setComponents(coordinateTransaltion.getX(), coordinateTransaltion.getY());
            centeredDrawable.draw(canvas);
        }
        if (score != null) {
            score.draw(canvas);
        }
    }

}
