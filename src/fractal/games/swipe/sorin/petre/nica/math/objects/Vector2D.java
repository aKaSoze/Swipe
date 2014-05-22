package fractal.games.swipe.sorin.petre.nica.math.objects;

public class Vector2D<V extends Vector2D<V>> {

	public Double	x;
	public Double	y;

	public Vector2D(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Integer x, Integer y) {
		this(x.doubleValue(), y.doubleValue());
	}

	public Double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public void add(V augend) {
		x += augend.x;
		y += augend.y;
	}

	public void subtract(V subtrahend) {
		x -= subtrahend.x;
		y -= subtrahend.y;
	}

	public Double scalarMultiply(V multiplicand) {
		return (x * multiplicand.x) + (y * multiplicand.y);
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
}
