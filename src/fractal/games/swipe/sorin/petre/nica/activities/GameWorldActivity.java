package fractal.games.swipe.sorin.petre.nica.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.collections.Tuple2;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.AnimatedShape;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.Property;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.OscilatingBillboard;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.CollisionHandler;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.RammedPainting;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Sensor.ObstaclePassedHandler;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;
import fractal.games.swipe.sorin.petre.nica.views.GameView;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;
import fractal.games.swipe.sorin.petre.nica.views.Score;

public class GameWorldActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final GameView gameView = new GameView(this);

        final Hippo hippo = new Hippo(this, new LayoutProportions(0.19, 0.14, 0.3, 1.0));
        hippo.acceleration = new Acceleration(0.0, -9.8, LengthUnit.METER, TimeUnit.SECOND);
        hippo.velocity = new Velocity(0.0, 0.0, LengthUnit.PIXEL, TimeUnit.SECOND);

        PropulsionPlatform propulsionPlatform = new PropulsionPlatform(this, new LayoutProportions(0.25, 0.025, 0.3, 0.7), hippo);
        propulsionPlatform.addObstacle(hippo);

        PropulsionPlatform propulsionPlatform2 = new PropulsionPlatform(this, new LayoutProportions(0.25, 0.025, 0.8, 1.2), hippo);
        propulsionPlatform.addObstacle(hippo);

        PropulsionPlatform propulsionPlatform3 = new PropulsionPlatform(this, new LayoutProportions(0.25, 0.025, 0.4, 1.7), hippo);
        propulsionPlatform.addObstacle(hippo);

        PropulsionPlatform propulsionPlatform4 = new PropulsionPlatform(this, new LayoutProportions(0.25, 0.025, 0.5, 2.2), hippo);
        propulsionPlatform.addObstacle(hippo);

        Tuple2<Integer, Long> slide1 = new Tuple2<Integer, Long>(R.drawable.evil_monkey, 700L);
        Tuple2<Integer, Long> slide2 = new Tuple2<Integer, Long>(R.drawable.monkey_banana, 700L);
        OscilatingBillboard monkey = new OscilatingBillboard(this, new LayoutProportions(0.1, 0.08, 0.7, 1.7), new Displacement(200, 0), new Velocity(7, 0), slide1, slide2);
        monkey.addObstacle(hippo);

        OscilatingBillboard monkey2 = new OscilatingBillboard(this, new LayoutProportions(0.1, 0.08, 0.1, 1.05), new Displacement(600, 0), new Velocity(4, 0), slide1, slide2);
        monkey2.addObstacle(hippo);

        Sensor circleOfFire = new Sensor(this, R.drawable.ring_of_fire, new Rectangle(this, new LayoutProportions(0.02, 0.01, 0.82, 1.87)), new Rectangle(this, new LayoutProportions(0.02, 0.01, 0.6, 2.1)));
        circleOfFire.addObstacle(hippo);

        final Painting box = new Painting(this, new LayoutProportions(0.1, 0.08, 0.5, 1.5), R.drawable.reflector_1);
        box.properties.add(Property.MOVABLE);
        box.addObstacle(hippo);

        RammedPainting boxFactory = new RammedPainting(this, new LayoutProportions(0.1, 0.08, 0.05, 0.9), R.drawable.reflector_1);
        boxFactory.paintingCreatedHandler = new RammedPainting.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Painting painting) {
                painting.addObstacle(hippo);
                gameView.addWorldObject(painting);
            }
        };

        gameView.addWorldObject(hippo);
        gameView.addWorldObject(propulsionPlatform);
        gameView.addWorldObject(propulsionPlatform2);
        gameView.addWorldObject(propulsionPlatform3);
        gameView.addWorldObject(propulsionPlatform4);
        gameView.addWorldObject(box);
        gameView.addWorldObject(boxFactory);
        gameView.addWorldObject(monkey);
        gameView.addWorldObject(monkey2);
        gameView.addWorldObject(circleOfFire);
        gameView.followedObject = hippo;

        layout.addView(gameView);

        final Score score = new Score(new LayoutProportions(0.0, 0.04, 0.03, 0.04), getAssets());
        gameView.score = score;

        propulsionPlatform.collisionHandlers.add(new CollisionHandler() {
            @Override
            public void onCollison() {
                score.addPoints(100L);
            }
        });

        circleOfFire.obstaclePassedHandler = new ObstaclePassedHandler() {
            @Override
            public void onObstaclePassed(AnimatedShape obstacle) {
                score.addPoints(1133L);
            }
        };

        setContentView(layout);
    }
}
