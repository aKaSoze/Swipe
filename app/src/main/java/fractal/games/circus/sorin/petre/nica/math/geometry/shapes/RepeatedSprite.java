package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

/**
 * Created by sorin on 02.12.2014.
 */
public class RepeatedSprite extends Sprite {

    public RepeatedSprite() {
        super();
    }

    @Expose
    private Integer repeatFactor = 3;

    public RepeatedSprite(LayoutProportions layoutProportions, Integer bitmapResourceId) {
        super(layoutProportions, bitmapResourceId);
    }

    @Override
    public void draw(Canvas canvas) {
        Displacement drawCenter = evalDrawCenter();

        float drawX = drawCenter.x.floatValue() - (evalHalfWidth().floatValue());
        float drawY = drawCenter.y.floatValue() - (evalHalfHeight().floatValue());

        for (int i = 0; i < repeatFactor; i++) {
            float xTranslation = evalHalfWidth().floatValue() * (i*2 - repeatFactor);
            canvas.drawBitmap(getBitmap(), drawX + xTranslation, drawY, paint);
        }
    }

    public void decreaseRepeatFactor() {
        if(repeatFactor > 0) {
            repeatFactor--;
        }
    }

}
