package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;

public class Displacement extends Vector2D<Displacement> {

	public enum Semiplane {
		First, Second, Intersection;
	}

	public Displacement(Double sx, Double sy, LengthUnit lengthUnit) {
		super(sx, sy, lengthUnit);
	}

	public Displacement(Float sx, Float sy, LengthUnit lengthUnit) {
		this(sx.doubleValue(), sy.doubleValue(), lengthUnit);
	}

	public Displacement(Double sx, Double sy) {
		this(sx, sy, LengthUnit.PIXEL);
	}

	public Displacement(Long sx, Long sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement(Integer sx, Integer sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement(Float sx, Float sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement() {
		this(0.0, 0.0);
	}

	public Displacement additionVector(Displacement subtrahend) {
		Double magnitudeOrderTransformation = getMeasureUnit().evaluateMagnitudeOrderTransformation(subtrahend.getMeasureUnit());
		Double x = this.x + (subtrahend.x * magnitudeOrderTransformation);
		Double y = this.y + (subtrahend.y * magnitudeOrderTransformation);
		return new Displacement(x, y, getMeasureUnit().lengthComponent);
	}

	public Displacement subtractionVector(Displacement subtrahend) {
		Double magnitudeOrderTransformation = getMeasureUnit().evaluateMagnitudeOrderTransformation(subtrahend.getMeasureUnit());
		Double x = this.x - (subtrahend.x * magnitudeOrderTransformation);
		Double y = this.y - (subtrahend.y * magnitudeOrderTransformation);
		return new Displacement(x, y, getMeasureUnit().lengthComponent);
	}

	public Displacement perpendicularVector() {
		return new Displacement(y, -x, getMeasureUnit().lengthComponent);
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
