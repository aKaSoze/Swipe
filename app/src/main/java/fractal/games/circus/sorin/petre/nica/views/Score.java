package fractal.games.circus.sorin.petre.nica.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.media.MediaStore;

public class Score extends Drawable {

    private static final String FONT = "fonts/Blazed.ttf";

    private final Paint paint;

    @Expose
    private LayoutProportions layoutProportions;
    @Expose
    private Float             x;
    @Expose
    private Float             y;
    @Expose
    private String            title;

    @Expose
    public Long points = 0L;

    public Score(String title, LayoutProportions layoutProportions) {
        paint = new Paint();
        paint.setColor(Color.rgb(102, 255, 255));
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTypeface(MediaStore.getTypeFace(FONT));
        this.layoutProportions = layoutProportions;
        this.title = title;
    }

    private Score() {
        this(null, null);
    }

    public Score(LayoutProportions layoutProportions) {
        this(null, layoutProportions);
    }

    public void reset() {
        points = 0L;
    }

    public Long getPoints() {
        return points;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText((title == null ? "" : title + " ") + points.toString(), x, y, paint);
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
        paint.setTextSize((float) (bounds.height() * layoutProportions.heightRatio));
    }
}
