package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Rect;
import fractal.games.swipe.sorin.petre.nica.collections.Tuple2;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;

public class OscilatingBillboard extends Billboard {

	private final Displacement	displacement;

	private final Displacement	originalCenter	= new Displacement();

	public OscilatingBillboard(LayoutProportions layoutProportions, Tuple2<Bitmap, Long>[] slides, Displacement displacement, Velocity velocity) {
		super(layoutProportions, slides);
		this.displacement = displacement;
		this.velocity = velocity;
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);
		if (center.distanceTo(originalCenter) >= displacement.magnitude() || center.distanceTo(originalCenter) < 0.00001) {
			velocity.reverse();
		}
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		originalCenter.makeEqualTo(center);
	}

}
