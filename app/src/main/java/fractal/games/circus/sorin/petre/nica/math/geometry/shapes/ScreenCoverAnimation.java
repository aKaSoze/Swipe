package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

/**
 * Created by sorin on 16.01.2015.
 */
public class ScreenCoverAnimation extends GameAnimation {

    private final Boolean isRetracting;

    public ScreenCoverAnimation(Long totalAnimationTimeInMillis, Boolean isRetracting) {
        super(new LayoutProportions(0d, 0d, 0.5, 0.5), totalAnimationTimeInMillis);
        this.isRetracting = isRetracting;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float xCenter = getBounds().width() / 2;
        float yCenter = getBounds().height() / 2;

        this.paint.setAntiAlias(false);
        this.paint.setDither(false);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getBounds().height() * 2 * strokeMultiplier());
        canvas.drawCircle(xCenter, yCenter, getBounds().height(), paint);
    }

    private float strokeMultiplier() {
        if (isRetracting) {
            return 1f - progress.floatValue();
        } else {
            return progress.floatValue();
        }
    }
}
