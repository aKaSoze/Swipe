package fractal.games.circus.sorin.petre.nica.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.collections.Tuple2;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.OscillatingBillboard;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.CollisionHandler;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RammedPainting;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.persistence.GameWorld;
import fractal.games.circus.sorin.petre.nica.persistence.JsonSerializer;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.circus.sorin.petre.nica.views.GameView;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;
import fractal.games.circus.sorin.petre.nica.views.Score;

public class GameWorldActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MediaStore.context = this;

        final JsonSerializer jsonSerializer = new JsonSerializer(this);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final GameView gameView = new GameView(this);

        final Hippo hippo = new Hippo(new LayoutProportions(0.19, 0.14, 0.3, 1.0));
        hippo.acceleration = new Acceleration(0.0, GameWorld.GRAVITATIONAL_ACCELERATION);
        hippo.velocity = new Velocity(0.0, 0.0);

        PropulsionPlatform propulsionPlatform = new PropulsionPlatform(new LayoutProportions(0.25, 0.025, 0.3, 0.7));

        Tuple2<Integer, Long> slide1 = new Tuple2<Integer, Long>(R.drawable.evil_monkey, 700L);
        Tuple2<Integer, Long> slide2 = new Tuple2<Integer, Long>(R.drawable.monkey_banana, 700L);
        OscillatingBillboard monkey = new OscillatingBillboard(new LayoutProportions(0.1, 0.08, 0.7, 1.7), new Displacement(200, 0), new Velocity(0.3, 0.0), slide1, slide2);
        monkey.addObstacle(hippo);

        RammedPainting boxFactory = new RammedPainting(new LayoutProportions(0.1, 0.08, 0.2, 0.22), R.drawable.reflector_1);
        boxFactory.paintingCreatedHandler = new RammedPainting.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Painting painting) {
                gameView.addWorldObject(painting);
            }
        };

        RammedPainting platformsFactory = new RammedPainting(new LayoutProportions(0.25, 0.025, 0.5, 0.22), R.drawable.beam);
        platformsFactory.paintingCreatedHandler = new RammedPainting.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Painting painting) {
                gameView.addWorldObject(painting);
            }
        };
        platformsFactory.paintingConstructor = new RammedPainting.PaintingConstructor() {
            @Override
            public Painting construct() {
                return new PropulsionPlatform();
            }
        };

        RammedPainting circlesFactory = new RammedPainting(new LayoutProportions(0.3, 0.26, 0.8, 0.22), R.drawable.ring_of_fire);
        circlesFactory.paintingCreatedHandler = new RammedPainting.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Painting painting) {
                gameView.addWorldObject(painting);
            }
        };
        circlesFactory.paintingConstructor = new RammedPainting.PaintingConstructor() {
            @Override
            public Painting construct() {
                return new Sensor(new LayoutProportions(0.3, 0.26, 0.8, 0.22), R.drawable.ring_of_fire);
            }
        };

        gameView.addWorldObject(hippo);
        gameView.addWorldObject(propulsionPlatform);
        gameView.addWorldObject(monkey);
        gameView.getWorld().addWorldObject(hippo);

        LinearLayout menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout navigationMenu = new LinearLayout(this);
        navigationMenu.setOrientation(LinearLayout.HORIZONTAL);

        Button saveButton = new Button(this);
        saveButton.setText("save");
        menu.addView(saveButton);

        Button loadButton = new Button(this);
        loadButton.setText("load");
        menu.addView(loadButton);

        Button pauseButton = new Button(this);
        pauseButton.setText("p/r");
        menu.addView(pauseButton);

        Button editButton = new Button(this);
        editButton.setText("edit/play");
        menu.addView(editButton);

        Button upButton = new Button(this);
        upButton.setText("up");
        navigationMenu.addView(upButton);

        final String[] worlds = new String[3];

        saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                worlds[0] = jsonSerializer.jsonForm(gameView.getWorld());
                Log.i("world", worlds[0]);
            }
        });

        loadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (worlds[0] != null) {
                    gameView.suspend();
                    gameView.loadWorld(jsonSerializer.fromJson(worlds[0], GameWorld.class));
                    gameView.resume();
                }
            }
        });

        pauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.switchPauseState();
            }
        });

        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setIsOnEditMode(!gameView.getIsOnEditMode());
            }
        });

        upButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.coordinateTranslation.x += 1;
            }
        });

        layout.addView(menu);
        layout.addView(navigationMenu);
        layout.addView(gameView);

        final Score score = new Score(new LayoutProportions(0.0, 0.04, 0.03, 0.04), getAssets());
        gameView.getWorld().score = score;

        final Score inGameTimer = new Score(new LayoutProportions(0.0, 0.04, 0.7, 0.04), getAssets());
        gameView.getWorld().inGameTimer = inGameTimer;

        gameView.hud.rammedPaintings.add(boxFactory);
        gameView.hud.rammedPaintings.add(platformsFactory);
        gameView.hud.rammedPaintings.add(circlesFactory);

        propulsionPlatform.collisionHandlers.add(new CollisionHandler() {
            @Override
            public void onCollison() {
                score.addPoints(100L);
            }
        });

        setContentView(layout);
    }
}
