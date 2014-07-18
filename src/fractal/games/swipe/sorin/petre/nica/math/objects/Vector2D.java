package fractal.games.swipe.sorin.petre.nica.math.objects;

import java.lang.reflect.InvocationTargetException;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.units.Unit;

public class Vector2D<V extends Vector2D<V>> {

	protected static final Paint	DEFAULT_PAINT;
	static {
		DEFAULT_PAINT = new Paint();
		DEFAULT_PAINT.setColor(Color.RED);
		DEFAULT_PAINT.setStyle(Style.STROKE);
		DEFAULT_PAINT.setStrokeWidth(4);
	}

	private Double					x;
	private Double					y;

	private Unit<?>					measureUnit;

	public Displacement				applyPoint;

	public Vector2D(Double x, Double y, Unit<?> measureUnit) {
		this.x = x;
		this.y = y;
		this.measureUnit = measureUnit;
	}

	public Vector2D(Double x, Double y) {
		this(x, y, Unit.ADIMENSIONAL);
	}

	public Vector2D(Integer x, Integer y) {
		this(x.doubleValue(), y.doubleValue());
	}

	public Vector2D() {
		this(0.0, 0.0);
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public void setComponents(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public void setMagnitude(Double newMagnitude) {
		normalize();
		multiplyByScalar(newMagnitude);
	}

	public void setComponents(Float x, Float y) {
		setComponents(x.doubleValue(), y.doubleValue());
	}

	public Unit<?> getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(Unit<?> measureUnit) {
		if (measureUnit.equals(this.measureUnit)) {
			return;
		}

		if (this.measureUnit != null) {
			Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(this.measureUnit);
			x *= magnitudeOrderTransformation;
			y *= magnitudeOrderTransformation;
		}
		this.measureUnit = measureUnit;
	}

	public Double magnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public void add(V augend) {
		Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(augend.getMeasureUnit());
		x += augend.getX() * magnitudeOrderTransformation;
		y += augend.getY() * magnitudeOrderTransformation;
	}

	public void subtract(V subtrahend) {
		Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(subtrahend.getMeasureUnit());
		x -= subtrahend.getX() * magnitudeOrderTransformation;
		y -= subtrahend.getY() * magnitudeOrderTransformation;
	}

	public Displacement additionVector(V subtrahend) {
		Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(subtrahend.getMeasureUnit());
		Double x = this.x + (subtrahend.getX() * magnitudeOrderTransformation);
		Double y = this.y + (subtrahend.getY() * magnitudeOrderTransformation);
		return new Displacement(x, y, measureUnit.lengthComponent);
	}

	public Displacement subtractionVector(V subtrahend) {
		Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(subtrahend.getMeasureUnit());
		Double x = this.x - (subtrahend.getX() * magnitudeOrderTransformation);
		Double y = this.y - (subtrahend.getY() * magnitudeOrderTransformation);
		return new Displacement(x, y, measureUnit.lengthComponent);
	}

	public Displacement perpendicularVector() {
		return new Displacement(y, -x, measureUnit.lengthComponent);
	}

	public Double distanceTo(V otherVector) {
		return subtractionVector(otherVector).magnitude();
	}

	public Double crossProduct(V otherVector) {
		Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(otherVector.getMeasureUnit());
		return (x * (otherVector.getY() * magnitudeOrderTransformation)) - (y * (otherVector.getX() * magnitudeOrderTransformation));
	}

	public Double scalarMultiply(V multiplicand) {
		Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(multiplicand.getMeasureUnit());
		return (x * multiplicand.getX() * magnitudeOrderTransformation) + (y * multiplicand.getY() * magnitudeOrderTransformation);
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

	public Displacement evaluateMiddle() {
		return new Displacement(applyPoint.getX() + x / 2, applyPoint.getY() + y / 2);
	}

	public Displacement evaluateTip() {
		return new Displacement(applyPoint.getX() + x, applyPoint.getY() + y);
	}

	public void neutralize() {
		x = 0.0;
		y = 0.0;
	}

	public void reverseX() {
		x = -x;
	}

	public void reverseY() {
		y = -y;
	}

	public void divideXByScalar(Double divisor) {
		x /= divisor;
	}

	public void divideYByScalar(Double divisor) {
		y /= divisor;
	}

	public void makeEqualTo(V otherVector) {
		x = otherVector.getX();
		y = otherVector.getY();
	}

	public Boolean isZero() {
		return getX() == 0 && getY() == 0;
	}

	public Displacement distanceToAPoint(Displacement point) {
		Double length = point.subtractionVector(applyPoint).crossProduct(point.subtractionVector(evaluateTip())) / evaluateTip().subtractionVector(applyPoint).magnitude();
		Displacement perpendicularVector = perpendicularVector();
		perpendicularVector.normalize();
		perpendicularVector.multiplyByScalar(length);
		perpendicularVector.applyPoint = point;
		return perpendicularVector;
	}

	public void draw(Canvas canvas) {
		canvas.drawLine(applyPoint.getX().floatValue(), applyPoint.getY().floatValue(), applyPoint.getX().floatValue() + x.floatValue(), applyPoint.getY().floatValue() + y.floatValue(), DEFAULT_PAINT);
	}

	@SuppressWarnings("unchecked")
	public V cloneVector() {
		try {
			return (V) getClass().getConstructor(Double.class, Double.class, measureUnit.getClass()).newInstance(x, y, measureUnit);
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
		return getClass().getSimpleName() + " [" + x + "i + " + y + "j], " + magnitude() + " " + measureUnit.symbol;
	}

}
