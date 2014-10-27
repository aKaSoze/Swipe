package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Rect;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.collections.Tuple2;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class OscilatingBillboard extends Billboard {

	@Expose
	private Displacement		displacement;

	private final Displacement	originalCenter	= new Displacement();

	public OscilatingBillboard() {
		super();
	}

	@SafeVarargs
	public OscilatingBillboard(LayoutProportions layoutProportions, Displacement displacement, Velocity velocity, Tuple2<Integer, Long>... slides) {
		super(layoutProportions, slides);
		this.displacement = displacement;
		this.velocity = velocity;
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);
		if ((center.distanceTo(originalCenter) >= displacement.magnitude() && velocity.x > 0) || (center.x <= originalCenter.x && velocity.x < 0)) {
			velocity.reverse();
		}
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		originalCenter.makeEqualTo(center);
	}

	@Override
	public void onCollision(AnimatedShape obstacle) {
		velocity.reverse();
	}

}
