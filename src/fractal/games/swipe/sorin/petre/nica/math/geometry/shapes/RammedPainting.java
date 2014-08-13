package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class RammedPainting extends Painting {

	public interface PaintingCreatedHandler {
		void onPaintingCreated(Painting painting);
	}

	public PaintingCreatedHandler	paintingCreatedHandler;

	private final Integer			resourceId;

	public RammedPainting(Context context, LayoutProportions layoutProportions, Integer resourceId) {
		super(context, layoutProportions, resourceId);
		this.resourceId = resourceId;
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
				Painting painting = new Painting(context, layoutProportions, resourceId);
				painting.properties.add(Property.MOVABLE);
				paintingCreatedHandler.onPaintingCreated(painting);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			}
		}
	}

}
