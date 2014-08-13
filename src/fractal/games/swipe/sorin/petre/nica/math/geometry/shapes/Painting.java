package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class Painting extends Rectangle {

    protected Bitmap bitmap;

    public Painting(Context context, LayoutProportions layoutProportions, Integer bitmapResourceId) {
        this(context, layoutProportions, BitmapFactory.decodeResource(context.getResources(), bitmapResourceId));
    }

    public Painting(Context context, LayoutProportions layoutProportions, Bitmap bitmap) {
        super(context, layoutProportions);
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        Displacement drawCenter = evalDrawCenter();
        canvas.drawBitmap(bitmap, drawCenter.x.floatValue() - (evalHalfWidth().floatValue()), drawCenter.y.floatValue() - (evalHalfHeight().floatValue()), paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bitmap = Bitmap.createScaledBitmap(bitmap, evalWidth().intValue(), evalHeight().intValue(), true);
    }

}
