package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.google.gson.annotations.Expose;

import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class Painting extends Rectangle {

    private transient Bitmap bitmap;

    @Expose
    private Integer          bitmapResourceId;

    public Painting() {

    }

    public Painting(Context context, LayoutProportions layoutProportions, Integer bitmapResourceId) {
        this(context, layoutProportions, BitmapFactory.decodeResource(context.getResources(), bitmapResourceId));
        this.bitmapResourceId = bitmapResourceId;
    }

    public Painting(Context context, LayoutProportions layoutProportions, Bitmap bitmap) {
        super(context, layoutProportions);
        this.bitmap = bitmap;
    }

    @Override
    public void draw(Canvas canvas) {
        Displacement drawCenter = evalDrawCenter();
        canvas.drawBitmap(getBitmap(), drawCenter.x.floatValue() - (evalHalfWidth().floatValue()), drawCenter.y.floatValue() - (evalHalfHeight().floatValue()), paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        Log.i("painting", "bounds changed mf");
        super.onBoundsChange(bounds);
        bitmap = Bitmap.createScaledBitmap(getBitmap(), evalWidth().intValue(), evalHeight().intValue(), true);
        Log.i("width - height", evalWidth().intValue() + " - " + evalHeight().intValue());
    }

    protected Bitmap getBitmap() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResourceId == null ? R.drawable.evil_monkey : bitmapResourceId);
        }
        return bitmap;
    }

    protected void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
