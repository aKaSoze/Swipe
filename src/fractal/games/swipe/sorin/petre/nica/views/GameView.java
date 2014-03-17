package fractal.games.swipe.sorin.petre.nica.views;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Circle;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.MovableShape;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public class GameView extends AutoUpdatableView {

    private final Rect              backGround_rect  = new Rect();
    private final Paint             backGround_paint = new Paint();

    private final Paint             blueCircle_paint;
    private final Paint             greenCircle_paint;
    private final Paint             redCircle_paint;
    private final Paint             cyanCircle_paint;

    private final Set<MovableShape> movableShapes    = new HashSet<MovableShape>();
    private MovableShape            selectedShape    = null;

    private Boolean                 isOkToRunGameLoop;

    public GameView(Context context) {
        super(context);

        backGround_paint.setColor(Color.BLACK);
        backGround_paint.setStyle(Style.FILL);

        blueCircle_paint = new Paint();
        blueCircle_paint.setColor(Color.BLUE);
        blueCircle_paint.setStyle(Style.STROKE);
        blueCircle_paint.setStrokeWidth(4);

        greenCircle_paint = new Paint(blueCircle_paint);
        greenCircle_paint.setColor(Color.GREEN);

        redCircle_paint = new Paint(blueCircle_paint);
        redCircle_paint.setColor(Color.RED);

        cyanCircle_paint = new Paint(blueCircle_paint);
        cyanCircle_paint.setColor(Color.CYAN);

        movableShapes.add(new Circle(new Point2D(100f, 50f), 40f));
        movableShapes.add(new Circle(new Point2D(100f, 150f), 40f, blueCircle_paint));
        movableShapes.add(new Circle(new Point2D(100f, 250f), 40f, greenCircle_paint));
        movableShapes.add(new Circle(new Point2D(100f, 350f), 40f, redCircle_paint));
        movableShapes.add(new Circle(new Point2D(100f, 450f), 40f, cyanCircle_paint));
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
        case MotionEvent.ACTION_MOVE:
            if (selectedShape != null) {
                selectedShape.react(event);
            }
            break;
        default:
            break;
        }

        return true;
    }

    private MovableShape evaluateTargetShape(Point2D touchPoint) {
        MovableShape closestShape = null;
        float smallestDistance = 60;
        for (MovableShape movableShape : movableShapes) {
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
        backGround_rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(backGround_rect, backGround_paint);
        for (MovableShape movableShape : movableShapes) {
            movableShape.draw(canvas);
        }
    }

    public void updateWorld(Long elapsedTime) {
        for (MovableShape movableShape : movableShapes) {
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
        backGround_rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(backGround_rect, backGround_paint);
        for (MovableShape movableShape : movableShapes) {
            movableShape.draw(canvas);
        }
    }

}
