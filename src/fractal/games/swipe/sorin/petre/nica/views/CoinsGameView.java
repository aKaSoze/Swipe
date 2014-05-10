package fractal.games.swipe.sorin.petre.nica.views;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.ValueCircle;
import fractal.games.swipe.sorin.petre.nica.math.objects.Displacement2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;

public class CoinsGameView extends AutoUpdatableView {

    private final ColorDrawable         backGround_drwbl = new ColorDrawable(Color.BLACK);

    private CenteredDrawable            selectedShape    = null;

    private Boolean                     isOkToRunGameLoop;

    private final Set<CenteredDrawable> drawables        = new HashSet<CenteredDrawable>();

    private final Bitmap                goldCoin_bmp;

    private Integer                     coinSize;

    public CoinsGameView(Context context) {
        super(context);
        goldCoin_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gold_coin);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            coinSize = (right - left) / 15;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            selectedShape = evaluateTargetShape(Point2D.Factory.fromMotionEvent(event));
            break;
        case MotionEvent.ACTION_UP:
            selectedShape = null;
            break;
        default:
            break;
        }
        if (selectedShape != null) {
            selectedShape.onMotionEvent(event);
        }
        return true;
    }

    private CenteredDrawable evaluateTargetShape(Point2D touchPoint) {
        CenteredDrawable closestShape = null;
        float smallestDistance = 60;
        for (CenteredDrawable movableShape : drawables) {
            float distanceToTouchPoint = movableShape.distanceTo(touchPoint);
            if (distanceToTouchPoint < smallestDistance) {
                smallestDistance = distanceToTouchPoint;
                closestShape = movableShape;
            }
        }
        return closestShape;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSurface(canvas);
    }

    private Long lastCoinAddTime = 0L;

    public void updateWorld(Long elapsedTime) {
        if (elapsedTime - lastCoinAddTime > 999) {
            Random rng = new Random();
            Point2D topLeft = new Point2D(rng.nextInt(400), rng.nextInt(400));
            Point2D bottomRight = topLeft.translate(new Displacement2D(coinSize, coinSize));
            Segment2D diagonal = new Segment2D(topLeft, bottomRight);
            drawables.add(new ValueCircle(diagonal, goldCoin_bmp, elapsedTime));
            lastCoinAddTime = elapsedTime;
        }

        Set<CenteredDrawable> deadObjects = new HashSet<CenteredDrawable>();

        for (CenteredDrawable movableShape : drawables) {
            if (movableShape instanceof ValueCircle) {
                ValueCircle valueCircle = (ValueCircle) movableShape;
                if (valueCircle.isDestroyed) {
                    deadObjects.add(valueCircle);
                    continue;
                }
            }
            movableShape.updateState(elapsedTime);
        }

        drawables.removeAll(deadObjects);
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
