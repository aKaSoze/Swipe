package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;

public class Displacement extends Vector2D<Displacement> {

	public Displacement(Double sx, Double sy, LengthUnit lengthUnit) {
		super(sx, sy, lengthUnit);
	}

	public Displacement(Float sx, Float sy, LengthUnit lengthUnit) {
		this(sx.doubleValue(), sy.doubleValue(), lengthUnit);
	}

	public Displacement(Double sx, Double sy) {
		this(sx, sy, LengthUnit.METER);
	}

	public Displacement(Integer sx, Integer sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement(Float sx, Float sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement delta(Displacement other) {
		Double magnitudeTrans = getMeasureUnit().evaluateMagnitudeOrderTransformation(other.getMeasureUnit());
		return new Displacement((other.getX() * magnitudeTrans) - getX(), (other.getY() * magnitudeTrans) - getY(), getMeasureUnit().lengthComponent);
	}

	public Boolean isZero() {
		return getX() == 0 && getY() == 0;
	}

}
