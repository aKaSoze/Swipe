package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class RammedPainting extends Painting {

    public interface PaintingCreatedHandler {
        void onPaintingCreated(Painting painting);
    }

    public PaintingCreatedHandler paintingCreatedHandler;

    public RammedPainting(LayoutProportions layoutProportions, Bitmap bitmap) {
        super(layoutProportions, bitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Displacement center = evalDrawCenter();
        canvas.drawRect(center.getX().floatValue() - (evalHalfWidth().floatValue()), center.getY().floatValue() - (evalHalfHeight().floatValue()), center.getX().floatValue() + (evalHalfWidth().floatValue()), center
                .getY().floatValue() + (evalHalfHeight().floatValue()), paint);
    }

    public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
        if (touchPoint.distanceTo(center) < VECINITY_DISTANCE) {
            switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Painting painting = new Painting(layoutProportions, bitmap);
                painting.properties.add(Property.MOVABLE);
                paintingCreatedHandler.onPaintingCreated(painting);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            }
        }
    }

}
