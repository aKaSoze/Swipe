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
	public Displacement2D delta(IPoint2D other) {
		return new Displacement2D(other.getX() - x, other.getY() - y);
	}

	public Point2D translate(Displacement2D displacement) {
		return new Point2D(x + displacement.dx, y + displacement.dy);
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
		Displacement2D rotationDisplacement = new Displacement2D(distance * Math.cos(angleToXAxis + rotation), distance * Math.sin(angleToXAxis + rotation));
		return translate(reference.delta(this).delta(rotationDisplacement));
	}

	public Float clockWiseXAxisAngleTo(IPoint2D other) {
		Displacement2D delta = other.delta(this);
		return Double.valueOf(Math.atan2(delta.dy, delta.dx)).floatValue();
	}

	@Override
	public Float rotateCounterClockWise(IPoint2D reference, Float radians) {
		// TODO Auto-generated method stub
		return null;
	}

}
