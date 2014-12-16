package fractal.games.circus.sorin.petre.nica.math.objects;

import java.lang.reflect.InvocationTargetException;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;

public class Vector2D<V extends Vector2D<V>> {

	@Expose
	public Double		x;
	@Expose
	public Double		y;

	public Displacement	applyPoint;

	@Expose
	public String		measureUnit;

	public Vector2D(Double x, Double y, String measureUnit) {
		this.x = x;
		this.y = y;
		this.measureUnit = measureUnit;
	}

	public Vector2D(Double x, Double y) {
		this(x, y, "");
	}

	public Vector2D(Integer x, Integer y) {
		this(x.doubleValue(), y.doubleValue());
	}

	public Vector2D() {
		this(0.0, 0.0);
	}

	public void setComponents(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public void setComponents(Float x, Float y) {
		setComponents(x.doubleValue(), y.doubleValue());
	}

	public void setComponents(Integer x, Integer y) {
		setComponents(x.doubleValue(), y.doubleValue());
	}

	public Double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public void setMagnitude(Double newMagnitude) {
		normalize();
		multiplyByScalar(newMagnitude);
	}

	public Boolean isZero() {
		return x == 0 && y == 0;
	}

	public void reverseX() {
		x = -x;
	}

	public void reverseY() {
		y = -y;
	}

	public void normalize() {
		divideByScalar(magnitude());
	}

	public void reverse() {
		x = -x;
		y = -y;
	}

	public void neutralize() {
		x = 0.0;
		y = 0.0;
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

	public Double crossMultiply(V otherVector) {
		return (x * otherVector.y) - (y * otherVector.x);
	}

	public void makeEqualTo(V otherVector) {
		x = otherVector.x;
		y = otherVector.y;
	}

	public void multiplyByScalar(Double multiplicand) {
		x *= multiplicand;
		y *= multiplicand;
	}

	public void divideByScalar(Double divisor) {
		x /= divisor;
		y /= divisor;
	}

	public void divideXByScalar(Double divisor) {
		x /= divisor;
	}

	public void divideYByScalar(Double divisor) {
		y /= divisor;
	}

	public Displacement evaluateMiddle() {
		return new Displacement(applyPoint.x + x / 2, applyPoint.y + y / 2);
	}

	public Displacement evaluateTip() {
		return new Displacement(applyPoint.x + x, applyPoint.y + y);
	}

	@SuppressWarnings("unchecked")
	public V cloneVector() {
		try {
			V clone = (V) getClass().getConstructor(Double.class, Double.class, String.class).newInstance(x, y, measureUnit);
			if (applyPoint != null) {
				clone.applyPoint = applyPoint.cloneVector();
			}
			return clone;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + x + "i + " + y + "j], " + magnitude() + " " + measureUnit;
	}

}
