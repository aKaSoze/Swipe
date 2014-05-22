package fractal.games.swipe.sorin.petre.nica.physics.kinematics;

import fractal.games.swipe.sorin.petre.nica.math.objects.Vector2D;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;

public class Acceleration extends Vector2D {

	protected LengthUnit	lengthUnit	= LengthUnit.METER;
	protected TimeUnit		timeUnit	= TimeUnit.SECOND;

	public Acceleration(Double ax, Double ay, LengthUnit lengthUnit, TimeUnit timeUnit) {
		this(ax, ay);
		this.lengthUnit = lengthUnit;
		this.timeUnit = timeUnit;
	}

	public Acceleration(Double ax, Double ay) {
		super(ax, ay);
	}

	public Acceleration(Integer ax, Integer ay) {
		super(ax, ay);
	}

	public Velocity generatedVelocity(Long elapsedTime) {
		return new Velocity(x * elapsedTime, y * elapsedTime);
	}

	@Override
	public String toString() {
		return x + "i + " + y + "j (" + lengthUnit.symbol + "/" + timeUnit.symbol + "^2)";
	}

}
