package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;

public class BitmapDrawable extends CenteredDrawable {

    private final Bitmap bitmap;

    private Segment2D    cornerToCorner;

    public BitmapDrawable(Segment2D cornerToCorner, Bitmap bitmap) {
        super(cornerToCorner.middle);
        this.cornerToCorner = cornerToCorner;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(cornerToCorner.xComponent), Math.round(cornerToCorner.yComponent), true);
        setBounds(cornerToCorner.toRect());
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
        case MotionEvent.ACTION_MOVE:
            setCenter(Point2D.Factory.fromMotionEvent(motionEvent));
        }
    }

    @Override
    public void updateState(Long elapsedTime) {
    }

    @Override
    public void setCenter(Point2D newCenter) {
        cornerToCorner = cornerToCorner.translate(center.delta(newCenter));
        setBounds(cornerToCorner.toRect());
        super.setCenter(newCenter);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, cornerToCorner.firstPoint.getX(), cornerToCorner.firstPoint.getY(), paint);
    }

}
