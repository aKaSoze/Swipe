package fractal.games.swipe.math.objects;

import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public interface IPoint2D {

	Float getX();

	Float getY();

	Displacement delta(IPoint2D other);

	Float distanceTo(IPoint2D point2d);

	IPoint2D rotateClockWise(IPoint2D reference, Float radians);

	Float rotateCounterClockWise(IPoint2D reference, Float radians);
}