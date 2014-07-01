package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import android.view.MotionEvent;
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
		this(sx, sy, LengthUnit.PIXEL);
	}

	public Displacement(Integer sx, Integer sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Displacement(Float sx, Float sy) {
		this(sx.doubleValue(), sy.doubleValue());
	}

	public Boolean isZero() {
		return getX() == 0 && getY() == 0;
	}

	public static class Factory {
		public static Displacement fromMotionEvent(MotionEvent motionEvent) {
			return new Displacement(motionEvent.getX(), motionEvent.getY());
		}
	}
}
