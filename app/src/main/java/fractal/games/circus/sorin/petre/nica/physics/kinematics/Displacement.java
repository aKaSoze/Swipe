package fractal.games.circus.sorin.petre.nica.physics.kinematics;

import android.view.MotionEvent;
import fractal.games.circus.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.circus.sorin.petre.nica.physics.units.FundamentalMeasure;

public class Displacement extends Vector2D<Displacement> {

	public enum Semiplane {
		First, Second, Intersection;
	}

	public Displacement(Double sx, Double sy) {
		super(sx, sy, FundamentalMeasure.Length.unitSymbol);
	}

	public Displacement(Float sx, Float sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement(Long sx, Long sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement(Integer sx, Integer sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement() {
		this(0.0, 0.0);
	}

	public Displacement additionVector(Displacement augend) {
		return new Displacement(x + augend.x, y + augend.y);
	}

	public Displacement subtractionVector(Displacement subtrahend) {
		return new Displacement(x - subtrahend.x, y - subtrahend.y);
	}

	public Displacement perpendicularVector() {
		return new Displacement(y, -x);
	}

	public Double distanceTo(Displacement otherVector) {
		return subtractionVector(otherVector).magnitude();
	}

	public Displacement perpendicularFromAPoint(Displacement point) {
		Double length = point.subtractionVector(applyPoint).crossMultiply(point.subtractionVector(evaluateTip())) / evaluateTip().subtractionVector(applyPoint).magnitude();
		Displacement perpendicularVector = perpendicularVector();
		perpendicularVector.normalize();
		perpendicularVector.multiplyByScalar(length);
		perpendicularVector.applyPoint = point;
		return perpendicularVector;
	}

	public Displacement rotateClockWise(Displacement reference, Double rotation) {
		Double distance = distanceTo(reference);
		Double angleToXAxis = clockWiseXAxisAngleTo(reference);
		Displacement rotationDisplacement = new Displacement(distance * Math.cos(angleToXAxis + rotation), distance * Math.sin(angleToXAxis + rotation));
		return additionVector(reference.subtractionVector(this).subtractionVector(rotationDisplacement));
	}

	public Double clockWiseXAxisAngleTo(Displacement other) {
		Displacement delta = other.subtractionVector(this);
		return Double.valueOf(Math.atan2(delta.y, delta.x));
	}

	public Double rotateCounterClockWise(Displacement reference, Float radians) {
		// TODO Auto-generated method stub
		return null;
	}

	public Semiplane sideOfPoint(Displacement point) {
		Double cross = crossMultiply(point.subtractionVector(applyPoint));
		if (cross == 0.0) {
			return Semiplane.Intersection;
		} else if (cross < 0.0) {
			return Semiplane.First;
		} else {
			return Semiplane.Second;
		}
	}

	public static class Factory {
		public static Displacement fromMotionEvent(MotionEvent motionEvent) {
			return new Displacement(motionEvent.getX(), motionEvent.getY());
		}
	}
}
