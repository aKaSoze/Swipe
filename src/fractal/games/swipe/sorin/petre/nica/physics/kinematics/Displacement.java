package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.Unit.DerivedUnitBuilder;

public class Displacement extends Vector2D<Displacement> {

	public Displacement(Double sx, Double sy, LengthUnit lengthUnit) {
		super(sx, sy);
		measureUnit = DerivedUnitBuilder.newUnit().proportionalTo(lengthUnit).build();
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
}
