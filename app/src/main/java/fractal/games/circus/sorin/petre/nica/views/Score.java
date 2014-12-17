package fractal.games.circus.sorin.petre.nica.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.media.MediaStore;

public class Score extends Drawable {

    private static final String FONT         = "fonts/Blazed.ttf";
    private static final double SCALE_FACTOR = 1.159;

    private static final Paint paint;

    static {
        paint = new Paint();
        paint.setColor(Color.rgb(21, 185, 181));
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Expose
    private LayoutProportions layoutProportions;
    @Expose
    private float             x;
    @Expose
    private float             y;

    @Expose
    public Long points = 0L;

    private Score() {
        paint.setTypeface(MediaStore.getTypeFace(FONT));
    }

    public Score(LayoutProportions layoutProportions) {
        paint.setTypeface(MediaStore.getTypeFace(FONT));
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
        paint.setTextSize((float) (bounds.height() * layoutProportions.heightRatio * SCALE_FACTOR));
    }
}
