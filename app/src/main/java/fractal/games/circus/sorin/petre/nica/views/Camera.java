package fractal.games.circus.sorin.petre.nica.views;

import android.graphics.Rect;

import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;

/**
 * Created by sorin on 19.12.2014.
 */
public class Camera {

    public Displacement coordinateTranslation = new Displacement();

    public Rect knownBounds;

    public void centerVerticallyOnObject(CenteredDrawable centeredDrawable) {
        coordinateTranslation.setComponents(0.0, (knownBounds.height() / 2) - centeredDrawable.center.y);
    }

    public void centerHorizontallyOnObject(CenteredDrawable centeredDrawable) {
        coordinateTranslation.setComponents((knownBounds.width() / 2) - centeredDrawable.center.x, 0.0);
    }

    public void centerOnObject(CenteredDrawable centeredDrawable) {
        coordinateTranslation.setComponents((knownBounds.width() / 2) - centeredDrawable.center.x, (knownBounds.height() / 2) - centeredDrawable.center.y);
    }

    public Displacement evalDrawLocation(Displacement realLocation) {
        if (realLocation == null) {
            return null;
        } else {
            Displacement drawLocation = new Displacement(evalDrawX(realLocation.x), evalDrawY(realLocation.y));
            drawLocation.applyPoint = evalDrawLocation(realLocation.applyPoint);
            return drawLocation;
        }
    }

    public Displacement evalRealLocation(Displacement drawLocation) {
        if (drawLocation == null) {
            return null;
        } else {
            Displacement realLocation = new Displacement(evalRealX(drawLocation.x), evalRealY(drawLocation.y));
            realLocation.applyPoint = evalRealLocation(drawLocation.applyPoint);
            return realLocation;
        }
    }

    protected Double evalRealX(Double drawX) {
        return drawX - coordinateTranslation.x;
    }

    protected Double evalRealY(Double drawY) {
        return knownBounds.height() - (drawY + coordinateTranslation.y);
    }

    protected Double evalDrawX(Double realX) {
        return realX + coordinateTranslation.x;
    }

    protected Double evalDrawY(Double realY) {
        return knownBounds.height() - (realY + coordinateTranslation.y);
    }

    public void slideUp() {
        coordinateTranslation.y -= 6;
    }

    public void slideDown() {
        coordinateTranslation.y += 6;
    }
}
