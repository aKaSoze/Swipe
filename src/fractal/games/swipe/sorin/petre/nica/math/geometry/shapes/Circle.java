package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public class Circle extends MovableShape {

    private static final Paint DEFAULT_PAINT;
    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    public Float               radius;
    public Paint               paint;
    private final Random       random = new Random();

    public Circle(Point2D center, Float radius, Paint paint) {
        super(center);
        this.radius = radius;
        this.paint = paint;
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    public Circle(Point2D center, Float radius) {
        this(center, radius, DEFAULT_PAINT);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(center.getX(), center.getY(), radius, paint);
    }

    @Override
    public void updateState(Long elapsedTime) {
        paint.setStrokeWidth(paint.getStrokeWidth() + (random.nextInt(2) == 0 && paint.getStrokeWidth() < 25 ? 1 : -1));
    }

}
