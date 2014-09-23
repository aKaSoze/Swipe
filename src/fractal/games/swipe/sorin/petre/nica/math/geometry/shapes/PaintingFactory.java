package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import android.graphics.Canvas;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class PaintingFactory extends Painting {

	public interface NewPaintingHandler {
		void onNewPainting(Painting newPainting);
	}

	public PaintingFactory(Context context, LayoutProportions layoutProportions, Integer resourceId) {
		super(context, layoutProportions, resourceId);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Displacement drawCenter = evalDrawCenter();
		canvas.drawRect(drawCenter.x.floatValue() - evalHalfWidth().floatValue(), drawCenter.y.floatValue() - evalHalfHeight().floatValue(), drawCenter.x.floatValue() + evalHalfWidth().floatValue(),
				drawCenter.y.floatValue() + evalHalfHeight().floatValue(), paint);
	}
}
