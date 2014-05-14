package fractal.games.swipe.sorin.petre.nica.math.objects;

public class Vector2D {

	public final Double	x;
	public final Double	y;

	public final Double	fiX;
	public final Double	fiY;

	public Vector2D(Double x, Double y) {
		this.x = x;
		this.y = y;
		fiX = Math.acos(x / magnitude());
		fiY = Math.acos(y / magnitude());
	}

	public Double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public Vector2D add(Vector2D augend) {
		return new Vector2D(x + augend.x, y + augend.y);
	}

	public Vector2D subtract(Vector2D subtrahend) {
		return new Vector2D(x - subtrahend.x, y - subtrahend.y);
	}

	public Double scalarMultiply(Vector2D multiplicand) {
		return (x * multiplicand.x) + (y * multiplicand.y);
	}

	public Vector2D normalize() {
		return divideByScalar(magnitude());
	}

	public Vector2D reverse() {
		return new Vector2D(-x, -y);
	}

	public Vector2D multiplyByScalar(Double multiplicand) {
		return new Vector2D(x / multiplicand, y / multiplicand);
	}

	public Vector2D divideByScalar(Double divisor) {
		return new Vector2D(x / divisor, y / divisor);
	}
}
