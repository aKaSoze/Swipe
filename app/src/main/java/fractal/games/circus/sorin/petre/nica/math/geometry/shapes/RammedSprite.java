package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class RammedSprite extends Sprite {

    public interface PaintingCreatedHandler {
        void onPaintingCreated(Sprite sprite);
    }

    public interface PaintingConstructor {
        Sprite construct();
    }

    public PaintingCreatedHandler paintingCreatedHandler;

    public PaintingConstructor paintingConstructor;

    public RammedSprite() {
        super();
    }

    public RammedSprite(LayoutProportions layoutProportions, Integer resourceId) {
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
                    Sprite sprite;
                    if (paintingConstructor == null) {
                        sprite = new Sprite(layoutProportions, bitmapResourceId);
                    } else {
                        sprite = paintingConstructor.construct();
                    }
                    sprite.center = center.cloneVector();
                    sprite.layoutProportions = layoutProportions;
                    sprite.properties.add(Property.MOVABLE);
                    paintingCreatedHandler.onPaintingCreated(sprite);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
        }
    }
}
