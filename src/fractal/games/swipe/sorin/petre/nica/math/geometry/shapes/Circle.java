package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public class Circle extends AnimatedShape {

    private static final Paint DEFAULT_PAINT;
    static {
        DEFAULT_PAINT = new Paint();
        DEFAULT_PAINT.setColor(Color.WHITE);
        DEFAULT_PAINT.setStyle(Style.STROKE);
        DEFAULT_PAINT.setStrokeWidth(4);
    }

    public Float               radius;

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
        super.draw(canvas);
        canvas.drawCircle(center.getX(), center.getY(), radius, paint);
    }

    @Override
    public void updateState(Long elapsedTime) {
        super.updateState(elapsedTime);
        if (center.getX() - radius <= 0 || center.getX() + radius >= boundingBoxRight) {
            velocity.reverseX();
            velocity.divideXByScalar(2.0);
        }
    }

}
