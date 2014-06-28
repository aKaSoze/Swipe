package fractal.games.swipe.sorin.petre.nica.views;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Net;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class GameView extends AutoUpdatableView {

    private final ColorDrawable         backGround_drwbl = new ColorDrawable(Color.BLACK);

    private CenteredDrawable            selectedShape    = null;

    private Boolean                     isOkToRunGameLoop;

    private final Set<CenteredDrawable> drawables        = new HashSet<CenteredDrawable>();

    private Rectangle                   rectangle;

    private Rectangle                   firstObstacle;

    private Rectangle                   secondObstacle;

    private MediaPlayer                 crowded;

    public GameView(Context context) {
        super(context);
        Displacement x1 = new Displacement(200, 700);
        Displacement x2 = x1.additionVector(new Displacement(300, 0));
        Segment2D segment2d = new Segment2D(x1, x2);
        Net net = new Net(segment2d);

        Bitmap originalHippo_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hippo_wacky);
        rectangle = new Rectangle(segment2d.middle.additionVector(new Displacement(0, -96)), 108, 192);
//        rectangle.setBitmap(originalHippo_bmp);

        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.boing);
        crowded = MediaPlayer.create(context, R.raw.crowded);
        net.rectangle = rectangle;
        net.boingSound = mediaPlayer;

        firstObstacle = new Rectangle(new Displacement(250, 455), 20, 30);
        firstObstacle.setFilled(true);

        secondObstacle = new Rectangle(new Displacement(375, 235), 20, 15);
        secondObstacle.setFilled(false);

        rectangle.obstacles.add(firstObstacle);
        rectangle.obstacles.add(secondObstacle);

        drawables.add(net);
        drawables.add(rectangle);
        drawables.add(firstObstacle);
        drawables.add(secondObstacle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rectangle.boundingBoxRight = right;
        crowded.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (CenteredDrawable centeredDrawable : drawables) {
            centeredDrawable.onMotionEvent(event);
        }
        return true;
    }

    private CenteredDrawable evaluateTargetShape(Displacement touchPoint) {
        CenteredDrawable closestShape = null;
        double smallestDistance = 60;
        for (CenteredDrawable centeredDrawable : drawables) {
            double distanceToTouchPoint = centeredDrawable.getCenter().distanceTo(touchPoint);
            if (distanceToTouchPoint < smallestDistance) {
                smallestDistance = distanceToTouchPoint;
                closestShape = centeredDrawable;
            }
        }
        return closestShape;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSurface(canvas);
    }

    public void updateWorld(Long elapsedTime) {
        for (CenteredDrawable movableShape : drawables) {
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
        backGround_drwbl.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        backGround_drwbl.draw(canvas);
        for (CenteredDrawable movableShape : drawables) {
            movableShape.draw(canvas);
        }
    }

}
