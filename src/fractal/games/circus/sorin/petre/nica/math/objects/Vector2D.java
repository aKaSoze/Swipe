package fractal.games.circus.sorin.petre.nica.math.objects;

import java.lang.reflect.InvocationTargetException;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.units.Unit;

public class Vector2D<V extends Vector2D<V>> {

    @Expose
    public Double       x;
    @Expose
    public Double       y;

    public Displacement applyPoint;

    private Unit<?>     measureUnit;

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
        Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(augend.getMeasureUnit());
        x += augend.x * magnitudeOrderTransformation;
        y += augend.y * magnitudeOrderTransformation;
    }

    public void subtract(V subtrahend) {
        Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(subtrahend.getMeasureUnit());
        x -= subtrahend.x * magnitudeOrderTransformation;
        y -= subtrahend.y * magnitudeOrderTransformation;
    }

    public Double scalarMultiply(V multiplicand) {
        Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(multiplicand.getMeasureUnit());
        return (x * multiplicand.x * magnitudeOrderTransformation) + (y * multiplicand.y * magnitudeOrderTransformation);
    }

    public Double crossMultiply(V otherVector) {
        Double magnitudeOrderTransformation = measureUnit.evaluateMagnitudeOrderTransformation(otherVector.getMeasureUnit());
        return (x * (otherVector.y * magnitudeOrderTransformation)) - (y * (otherVector.x * magnitudeOrderTransformation));
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
            V clone = (V) getClass().getConstructor(Double.class, Double.class, measureUnit.getClass()).newInstance(x, y, measureUnit);
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
        return getClass().getSimpleName() + " [" + x + "i + " + y + "j], " + magnitude() + " " + measureUnit.symbol;
    }

}
