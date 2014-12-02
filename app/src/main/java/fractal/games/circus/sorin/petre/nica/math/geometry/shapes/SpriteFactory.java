package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class SpriteFactory extends Sprite {

	public interface NewPaintingHandler {
		void onNewPainting(Sprite newSprite);
	}

	public SpriteFactory(LayoutProportions layoutProportions, Integer resourceId) {
		super(layoutProportions, resourceId);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Displacement drawCenter = evalDrawCenter();
		canvas.drawRect(drawCenter.x.floatValue() - evalHalfWidth().floatValue(), drawCenter.y.floatValue() - evalHalfHeight().floatValue(), drawCenter.x.floatValue() + evalHalfWidth().floatValue(),
				drawCenter.y.floatValue() + evalHalfHeight().floatValue(), paint);
	}
}