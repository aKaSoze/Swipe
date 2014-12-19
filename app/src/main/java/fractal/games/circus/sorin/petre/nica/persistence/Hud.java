package fractal.games.circus.sorin.petre.nica.persistence;

import android.graphics.Canvas;

import java.util.HashSet;
import java.util.Set;

import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RammedSprite;
import fractal.games.circus.sorin.petre.nica.views.Camera;

public class Hud {

    public Set<RammedSprite> rammedPaintings = new HashSet<RammedSprite>();

    public void draw(Canvas canvas, Camera camera) {
        for (RammedSprite rammedPainting : rammedPaintings) {
            rammedPainting.center = rammedPainting.evalOriginalCenter();
            rammedPainting.center.y -= camera.coordinateTranslation.y;
            rammedPainting.drawTranslation.setComponents(camera.coordinateTranslation.x, camera.coordinateTranslation.y);
            rammedPainting.draw(canvas);
        }
    }
}
