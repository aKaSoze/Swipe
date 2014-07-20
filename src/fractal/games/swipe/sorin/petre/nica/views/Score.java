package fractal.games.swipe.sorin.petre.nica.views;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.LayoutProportions;

public class Score extends Drawable {

    public static final double SCALE_FACTOR = 1.159;
    private LayoutProportions  layoutProportions;

    private static final Paint paint;

    private float              x;
    private float              y;

    static {
        paint = new Paint();
        paint.setColor(Color.rgb(21, 185, 181));
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    private Long               points       = 0L;

    public Score(LayoutProportions layoutProportions, AssetManager assets) {
        paint.setTypeface(Typeface.createFromAsset(assets, "fonts/Blazed.ttf"));
        this.layoutProportions = layoutProportions;
    }

    public void addPoints(Long points) {
        this.points += points;
    }

    public void reset() {
        points = 0L;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(points.toString(), x, y, paint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha % 256);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        x = (float) ((bounds.right - bounds.left) * layoutProportions.xRatio);
        y = (float) ((bounds.bottom - bounds.top) * layoutProportions.yRatio);
        Log.i("score", "x=" + x + " y=" + y);
        paint.setTextSize((float) (bounds.height() * layoutProportions.heightRatio * SCALE_FACTOR));
    }
}
