package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class PaintingFactory extends Painting {

	public interface NewPaintingHandler {
		void onNewPainting(Painting newPainting);
	}

	public PaintingFactory(LayoutProportions layoutProportions, Bitmap bitmap) {
		super(layoutProportions, bitmap);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Displacement drawCenter = evalDrawCenter();
		canvas.drawRect(drawCenter.x.floatValue() - (evalHalfWidth().floatValue()), drawCenter.y.floatValue() - (evalHalfHeight().floatValue()), drawCenter.x.floatValue() + (evalHalfWidth().floatValue()),
				drawCenter.y.floatValue() + (evalHalfHeight().floatValue()), paint);
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		super.onMotionEvent(motionEvent, touchPoint);
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		}
	}

}
