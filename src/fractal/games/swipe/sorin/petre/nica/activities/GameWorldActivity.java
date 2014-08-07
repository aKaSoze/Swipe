package fractal.games.swipe.sorin.petre.nica.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.collections.Tuple2;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.AnimatedShape;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.Property;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.OscilatingBillboard;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.CollisionHandler;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.RammedPainting;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Sensor.ObstaclePassedHandler;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.swipe.sorin.petre.nica.views.GameView;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;
import fractal.games.swipe.sorin.petre.nica.views.Score;

public class GameWorldActivity extends Activity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final GameView gameView = new GameView(this);

        Bitmap originalHippo_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hippo_wacky);
        final Painting hippo = new Painting(new LayoutProportions(0.19, 0.14, 0.3, 0.8), originalHippo_bmp);

        PropulsionPlatform propulsionPlatform = new PropulsionPlatform(new LayoutProportions(0.25, 0.01, 0.3, 0.7), hippo, MediaPlayer.create(this, R.raw.boing));
        propulsionPlatform.properties.add(Property.MOVABLE);
        propulsionPlatform.addObstacle(hippo);

        PropulsionPlatform propulsionPlatform2 = new PropulsionPlatform(new LayoutProportions(0.25, 0.01, 0.3, 1.5), hippo, MediaPlayer.create(this, R.raw.boing));
        propulsionPlatform.properties.add(Property.MOVABLE);
        propulsionPlatform.addObstacle(hippo);

        Tuple2<Bitmap, Long> slide1 = new Tuple2<Bitmap, Long>(BitmapFactory.decodeResource(getResources(), R.drawable.evil_monkey), 700L);
        Tuple2<Bitmap, Long> slide2 = new Tuple2<Bitmap, Long>(BitmapFactory.decodeResource(getResources(), R.drawable.monkey_banana), 700L);
        OscilatingBillboard monkey = new OscilatingBillboard(new LayoutProportions(0.1, 0.08, 0.3, 0.3), new Displacement(300, 0), new Velocity(7, 0), slide1, slide2);
        monkey.addObstacle(hippo);

        Sensor circleOfFire = new Sensor(BitmapFactory.decodeResource(getResources(), R.drawable.ring_of_fire), new Rectangle(new LayoutProportions(0.02, 0.01, 0.1, 1.1)), new Rectangle(new LayoutProportions(0.02,
                0.01, 0.9, 1.4)));
        circleOfFire.addObstacle(hippo);

        final Painting box = new Painting(new LayoutProportions(0.1, 0.08, 0.5, 0.9), BitmapFactory.decodeResource(getResources(), R.drawable.reflector_1));
        box.properties.add(Property.MOVABLE);
        box.addObstacle(hippo);

        RammedPainting boxFactory = new RammedPainting(new LayoutProportions(0.1, 0.08, 0.0, 0.9), BitmapFactory.decodeResource(getResources(), R.drawable.reflector_1));
        boxFactory.paintingCreatedHandler = new RammedPainting.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Painting painting) {
                painting.addObstacle(hippo);
                gameView.addWorldObject(painting);
            }
        };

        gameView.addWorldObject(hippo);
        gameView.addWorldObject(propulsionPlatform);
        // gameView.addWorldObject(propulsionPlatform2);
        gameView.addWorldObject(box);
        gameView.addWorldObject(boxFactory);
        gameView.addWorldObject(monkey);
        gameView.addWorldObject(circleOfFire);
        gameView.followedObject = hippo;

        layout.addView(gameView);

        final Score score = new Score(new LayoutProportions(0.0, 0.04, 0.04, 0.04), getAssets());
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
