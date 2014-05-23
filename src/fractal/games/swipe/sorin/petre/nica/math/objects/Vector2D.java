package fractal.games.swipe.sorin.petre.nica.math.objects;

import fractal.games.swipe.sorin.petre.nica.physics.units.Unit;

public class Vector2D<V extends Vector2D<V>> {

	private Double	x;
	private Double	y;

	public Unit<?>	measureUnit;

	public Vector2D(Double x, Double y, Unit<?> measureUnit) {
		this.x = x;
		this.y = y;
		this.measureUnit = measureUnit;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public Vector2D(Double x, Double y) {
		this(x, y, Unit.ADIMENSIONAL);
	}

	public Vector2D(Integer x, Integer y) {
		this(x.doubleValue(), y.doubleValue());
	}

	public Double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public void add(V augend) {
		Double magnitudeOrderTransformation = evaluateMagnitudeOrderTransformation(augend);
		x += augend.getX() * magnitudeOrderTransformation;
		y += augend.getY() * magnitudeOrderTransformation;
	}

	public void subtract(V subtrahend) {
		Double magnitudeOrderTransformation = evaluateMagnitudeOrderTransformation(subtrahend);
		x -= subtrahend.getX() * magnitudeOrderTransformation;
		y -= subtrahend.getY() * magnitudeOrderTransformation;
	}

	public Double scalarMultiply(V multiplicand) {
		Double magnitudeOrderTransformation = evaluateMagnitudeOrderTransformation(multiplicand);
		return (x * multiplicand.getX() * magnitudeOrderTransformation) + (y * multiplicand.getY() * magnitudeOrderTransformation);
	}

	public Double evaluateMagnitudeOrderTransformation(V otherVector) {
		return otherVector.measureUnit.magnitudeOrder / measureUnit.magnitudeOrder;
	}

	public void normalize() {
		divideByScalar(magnitude());
	}

	public void reverse() {
		x = -x;
		y = -y;
	}

	public void multiplyByScalar(Double multiplicand) {
		x *= multiplicand;
		y *= multiplicand;
	}

	public void divideByScalar(Double divisor) {
		x /= divisor;
		y /= divisor;
	}

	public Point2D getTip() {
		return new Point2D(x, y);
	}

	@Override
	public String toString() {
		return "Vector2D [" + x + "i + " + y + "j], " + magnitude() + " " + measureUnit.symbol;
	}

}
