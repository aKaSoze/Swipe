package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.sorin.petre.nica.views.Camera;

/**
 * Created by sorin on 06.01.2015.
 */
public class Background {

    public Set<Sprite> sprites = new HashSet<Sprite>();

    public Background(Sprite... sprites) {
        for (Sprite sprite : sprites) {
            this.sprites.add(sprite);
        }
    }

    public void draw(Canvas canvas, Camera camera) {
        for (Sprite sprite : sprites) {
            sprite.drawTranslation.makeEqualTo(camera.coordinateTranslation);
            sprite.drawTranslation.divideByScalar(3d);
            sprite.draw(canvas);
        }
    }

    public void setBounds(Rect bounds) {
        for (Sprite sprite : sprites) {
            sprite.setBounds(bounds);
        }
    }
}
