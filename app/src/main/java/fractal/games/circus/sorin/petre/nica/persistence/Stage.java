package fractal.games.circus.sorin.petre.nica.persistence;

import android.graphics.Rect;

import com.google.gson.annotations.Expose;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.OscillatingBillboard;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sprite;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

/**
 * Created by sorin on 16.12.2014.
 */
public class Stage {

    public static final Double GRAVITATIONAL_ACCELERATION = -0.0005;

    @Expose
    private Hippo hippo;
    @Expose
    private Set<PropulsionPlatform>   platforms             = new CopyOnWriteArraySet<PropulsionPlatform>();
    @Expose
    private Set<Sensor>               sensors               = new CopyOnWriteArraySet<Sensor>();
    @Expose
    private Set<OscillatingBillboard> oscillatingBillboards = new CopyOnWriteArraySet<OscillatingBillboard>();
    @Expose
    private Set<Sprite>               sprites               = new CopyOnWriteArraySet<Sprite>();

    private Boolean isOnEditMode = false;

    public Stage() {
        hippo = new Hippo(new LayoutProportions(0.16, 0.14, 0.3, 1.0));
        hippo.acceleration = new Acceleration(0.0, Stage.GRAVITATIONAL_ACCELERATION);
        hippo.velocity = new Velocity(0.0, 0.0);
        platforms.add(new PropulsionPlatform(new LayoutProportions(0.25, 0.09, 0.3, 0.7)));
        onLoad(null);
    }

    private PropulsionPlatform.ImpactHandler impactHandler = new PropulsionPlatform.ImpactHandler() {
        @Override
        public void onImpact(PropulsionPlatform propulsionPlatform) {
            if (hippo.velocity.isZero() && propulsionPlatform.touchesOnHorizontalSide(hippo)) {
                Velocity springVelocity = propulsionPlatform.getSpringVelocity();
                hippo.velocity.setComponents(springVelocity.x / 4, springVelocity.y / 4);
                hippo.acceleration.y = GRAVITATIONAL_ACCELERATION;
                MediaStore.getSound(R.raw.boing).start();
            }
        }
    };

    public void addWorldObject(Sprite sprite) {
        if (sprite instanceof PropulsionPlatform) {
            PropulsionPlatform platform = (PropulsionPlatform) sprite;
            platforms.add(platform);
            if (hippo != null) {
                platform.addObstacle(hippo);
                platform.impactHandlers.add(impactHandler);
            }
        } else if (sprite instanceof Hippo) {
            hippo = (Hippo) sprite;
            for (PropulsionPlatform platform : platforms) {
                platform.addObstacle(hippo);
                platform.impactHandlers.add(impactHandler);
            }
        } else if (sprite instanceof Sensor) {
            Sensor sensor = (Sensor) sprite;
            sensor.addObstacle(hippo);
            sensors.add(sensor);
        } else if (sprite instanceof OscillatingBillboard) {
            OscillatingBillboard oscillatingBillboard = (OscillatingBillboard) sprite;
            oscillatingBillboard.addObstacle(hippo);
            oscillatingBillboards.add(oscillatingBillboard);
        } else {
            sprites.add(sprite);
            if (hippo != null) {
                sprite.addObstacle(hippo);
            }
        }
    }

    public void update(Long elapsedTime, Long timeIncrement) {
        for (CenteredDrawable movableShape : getAllObjects()) {
            movableShape.updateState(elapsedTime, timeIncrement);
        }
    }

    public Set<Sprite> getAllObjects() {
        Set<Sprite> objects = new CopyOnWriteArraySet<Sprite>();
        objects.add(hippo);
        objects.addAll(platforms);
        objects.addAll(sensors);
        objects.addAll(oscillatingBillboards);
        objects.addAll(sprites);
        return objects;
    }

    public Hippo getHippo() {
        return hippo;
    }

    public Set<PropulsionPlatform> getPlatforms() {
        return platforms;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public Set<OscillatingBillboard> getOscillatingBillboards() {
        return oscillatingBillboards;
    }

    @Override
    public String toString() {
        return String.valueOf(getAllObjects().size());
    }

    public void onLoad(Rect bounds) {
        for (Sprite sprite : getAllObjects()) {
            sprite.init();
            if (bounds != null) {
                sprite.setBounds(bounds);
            }
            addWorldObject(sprite);
            if (isOnEditMode) {
                sprite.properties.add(CenteredDrawable.Property.MOVABLE);
            } else {
                sprite.properties.remove(CenteredDrawable.Property.MOVABLE);
            }
        }
    }

    public void setIsOnEditMode(Boolean isOnEditMode) {
        this.isOnEditMode = isOnEditMode;
        if (isOnEditMode) {
            for (Sprite sprite : getAllObjects()) {
                sprite.properties.add(CenteredDrawable.Property.MOVABLE);
            }
        } else {
            for (Sprite sprite : getAllObjects()) {
                sprite.properties.remove(CenteredDrawable.Property.MOVABLE);
            }
        }
    }
}
