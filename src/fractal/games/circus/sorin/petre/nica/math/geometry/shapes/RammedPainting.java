package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class RammedPainting extends Painting {

	public interface PaintingCreatedHandler {
		void onPaintingCreated(Painting painting);
	}

	public PaintingCreatedHandler	paintingCreatedHandler;

	public RammedPainting() {
		super();
	}

	public RammedPainting(LayoutProportions layoutProportions, Integer resourceId) {
		super(layoutProportions, resourceId);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Displacement center = evalDrawCenter();
		canvas.drawRect(center.x.floatValue() - (evalHalfWidth().floatValue()), center.y.floatValue() - (evalHalfHeight().floatValue()), center.x.floatValue() + (evalHalfWidth().floatValue()), center.y.floatValue()
				+ (evalHalfHeight().floatValue()), paint);
	}

	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		if (touchPoint.distanceTo(center) < VECINITY_DISTANCE) {
			switch (motionEvent.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				Painting painting = new Painting(layoutProportions, bitmapResourceId);
				painting.center = center.cloneVector();
				painting.properties.add(Property.MOVABLE);
				paintingCreatedHandler.onPaintingCreated(painting);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			}
		}
	}

}
