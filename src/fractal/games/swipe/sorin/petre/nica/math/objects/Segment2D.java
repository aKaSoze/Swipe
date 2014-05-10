package fractal.games.swipe.sorin.petre.nica.math.objects;

import android.graphics.Rect;

public class Segment2D {

	public final Point2D	firstPoint;

	public final Point2D	secondPoint;

	public final Point2D	middle;

	public final Float		xComponent;

	public final Float		yComponent;

	public final Float		length;

	public Segment2D(Point2D firstPoint, Point2D secondPoint) {
		super();
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		middle = new Point2D(arithmeticMean(firstPoint.getX(), secondPoint.getX()), arithmeticMean(firstPoint.getY(), secondPoint.getY()));
		xComponent = Math.abs(firstPoint.getX() - secondPoint.getX());
		yComponent = Math.abs(firstPoint.getY() - secondPoint.getY());
		length = firstPoint.distanceTo(secondPoint);
	}

	private Float arithmeticMean(Float p1, Float p2) {
		return (p1 + p2) / 2;
	}

	public Rect toRect() {
		return new Rect(Math.round(firstPoint.getX()), Math.round(firstPoint.getY()), Math.round(secondPoint.getX()), Math.round(secondPoint.getY()));
	}

	public Segment2D translate(Displacement2D displacement) {
		return new Segment2D(firstPoint.translate(displacement), secondPoint.translate(displacement));
	}

	@Override
	public String toString() {
		return firstPoint + "|---|" + secondPoint;
	}

}
