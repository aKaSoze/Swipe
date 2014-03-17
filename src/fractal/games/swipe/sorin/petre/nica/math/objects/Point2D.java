package fractal.games.swipe.sorin.petre.nica.math.objects;

import android.view.MotionEvent;
import fractal.games.swipe.math.objects.IPoint2D;

public class Point2D implements IPoint2D {

    private final Float x;
    private final Float y;

    public Point2D(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Float distanceTo(IPoint2D point2d) {
        double dx = x - point2d.getX();
        double dy = y - point2d.getY();
        Double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        return dist.floatValue();
    }

    @Override
    public Float getX() {
        return x;
    }

    @Override
    public Float getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point2D) {
            Point2D otherPoint = (Point2D) o;
            return x.floatValue() == otherPoint.x.floatValue() && y.floatValue() == otherPoint.y.floatValue();
        } else {
            return false;
        }
    }

    public static class Factory {

        public static Point2D fromMotionEvent(MotionEvent motionEvent) {
            return new Point2D(motionEvent.getX(), motionEvent.getY());
        }

    }

}
