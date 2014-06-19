package fractal.games.swipe.sorin.petre.nica.math.objects;

import android.view.MotionEvent;
import fractal.games.swipe.math.objects.IPoint2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;

public class Point2D implements IPoint2D {

    private final Float x;
    private final Float y;

    public Point2D(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Integer x, Integer y) {
        this.x = x.floatValue();
        this.y = y.floatValue();
    }

    public Point2D(Double x, Double y) {
        this.x = x.floatValue();
        this.y = y.floatValue();
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
    public Displacement delta(IPoint2D other) {
        return new Displacement(other.getX() - x, other.getY() - y, LengthUnit.PIXEL);
    }

    public Point2D translate(Displacement displacement) {
        if (displacement.getMeasureUnit() != LengthUnit.PIXEL) {
            displacement.setMeasureUnit(LengthUnit.PIXEL);
        }
        return new Point2D(x + displacement.getX(), y + displacement.getY());
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

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public static class Factory {
        public static Point2D fromMotionEvent(MotionEvent motionEvent) {
            return new Point2D(motionEvent.getX(), motionEvent.getY());
        }
    }

    @Override
    public IPoint2D rotateClockWise(IPoint2D reference, Float rotation) {
        Float distance = distanceTo(reference);
        Float angleToXAxis = clockWiseXAxisAngleTo(reference);
        Displacement rotationDisplacement = new Displacement(distance * Math.cos(angleToXAxis + rotation), distance * Math.sin(angleToXAxis + rotation));
        return translate(reference.delta(this).delta(rotationDisplacement));
    }

    public Float clockWiseXAxisAngleTo(IPoint2D other) {
        Displacement delta = other.delta(this);
        return Double.valueOf(Math.atan2(delta.getY(), delta.getX())).floatValue();
    }

    @Override
    public Float rotateCounterClockWise(IPoint2D reference, Float radians) {
        // TODO Auto-generated method stub
        return null;
    }

}
