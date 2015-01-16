package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

/**
 * Created by sorin on 16.01.2015.
 */
public class ScreenCoverAnimation extends GameAnimation {

    public ScreenCoverAnimation(Long totalAnimationTimeInMillis) {
        super(new LayoutProportions(0d, 0d, 0.5, 0.5), totalAnimationTimeInMillis);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float xCenter = getBounds().width() / 2;
        float yCenter = getBounds().height() / 2;

        float left = xCenter - (xCenter * progress.floatValue());
        float top = yCenter - (yCenter * progress.floatValue());
        float right = xCenter + (xCenter * progress.floatValue());
        float bottom = yCenter + (yCenter * progress.floatValue());

        paint.setColor(Color.BLACK);
        paint.setAlpha((int) (255 * progress));
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(left, top, right, bottom, paint);
    }
}
