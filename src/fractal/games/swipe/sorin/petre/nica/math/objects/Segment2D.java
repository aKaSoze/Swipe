package fractal.games.swipe.sorin.petre.nica.math.objects;

import android.graphics.Rect;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class Segment2D {

	public final Displacement	firstPoint;

	public final Displacement	secondPoint;

	public final Displacement	middle;

	public final Double			xComponent;

	public final Double			yComponent;

	public final Double			length;

	public Segment2D(Displacement firstPoint, Displacement secondPoint) {
		super();
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		middle = new Displacement(arithmeticMean(firstPoint.getX(), secondPoint.getX()), arithmeticMean(firstPoint.getY(), secondPoint.getY()));
		xComponent = Math.abs(firstPoint.getX() - secondPoint.getX());
		yComponent = Math.abs(firstPoint.getY() - secondPoint.getY());
		length = firstPoint.subtractionVector(secondPoint).magnitude();
	}

	private Double arithmeticMean(Double p1, Double p2) {
		return (p1 + p2) / 2;
	}

	public Rect toRect() {
		return new Rect(firstPoint.getX().intValue(), firstPoint.getY().intValue(), secondPoint.getX().intValue(), secondPoint.getY().intValue());
	}

	public Double distanceToAPoint(Displacement point) {
		return point.subtractionVector(firstPoint).crossProduct(point.subtractionVector(secondPoint)) / secondPoint.subtractionVector(firstPoint).magnitude();
	}

	@Override
	public String toString() {
		return firstPoint + "|---|" + secondPoint;
	}

}
