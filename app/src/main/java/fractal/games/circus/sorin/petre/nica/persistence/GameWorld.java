package fractal.games.circus.sorin.petre.nica.persistence;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.AnimatedShape;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.OscillatingBillboard;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RepeatedSprite;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sprite;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.ImpactHandler;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;
import fractal.games.circus.sorin.petre.nica.views.Score;

public class GameWorld {

    public static final Double GRAVITATIONAL_ACCELERATION = -0.0005;

    public Score score;
    public Score inGameTimer;

    @Expose
    public RepeatedSprite lives = new RepeatedSprite(new LayoutProportions(0.09, 0.05, 0.5, 0.98), R.drawable.hippo_wacky);

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

    private ImpactHandler impactHandler = new ImpactHandler() {
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
            hippo.deathHandler = new Hippo.HippoDeathHandler() {
                @Override
                public void onHipposDeath() {
                    lives.decreaseRepeatFactor();
                }
            };
        } else if (sprite instanceof Sensor) {
            Sensor sensor = (Sensor) sprite;
            sensor.addObstacle(hippo);
            sensors.add(sensor);
            sensor.obstaclePassedHandler = new Sensor.ObstaclePassedHandler() {
                @Override
                public void onObstaclePassed(AnimatedShape obstacle) {
                    score.addPoints(1333L);
                }
            };
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

    public void clear() {
        hippo = null;
        platforms.clear();
        sensors.clear();
        oscillatingBillboards.clear();
        sprites.clear();
        lives = null;
    }

    @Override
    public String toString() {
        return String.valueOf(getAllObjects().size());
    }
}
