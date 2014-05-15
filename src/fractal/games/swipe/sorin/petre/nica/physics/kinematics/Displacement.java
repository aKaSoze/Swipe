package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;

public class Displacement extends Vector2D {

	public Displacement(Double sx, Double sy) {
		super(sx, sy);
	}

	public Displacement(Vector2D vector) {
		super(vector.x, vector.y);
	}

	public Displacement add(Displacement augend) {
		return new Displacement(super.add(augend));
	}

}
