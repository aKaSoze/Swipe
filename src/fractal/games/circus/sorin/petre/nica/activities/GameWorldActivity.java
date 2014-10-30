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
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.AnimatedShape;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.Property;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.OscilatingBillboard;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.CollisionHandler;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RammedPainting;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor.ObstaclePassedHandler;
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

		PropulsionPlatform propulsionPlatform2 = new PropulsionPlatform(new LayoutProportions(0.25, 0.025, 0.8, 1.2));

		PropulsionPlatform propulsionPlatform3 = new PropulsionPlatform(new LayoutProportions(0.25, 0.025, 0.4, 1.7));

		PropulsionPlatform propulsionPlatform4 = new PropulsionPlatform(new LayoutProportions(0.25, 0.025, 0.5, 2.2));

		Tuple2<Integer, Long> slide1 = new Tuple2<Integer, Long>(R.drawable.evil_monkey, 700L);
		Tuple2<Integer, Long> slide2 = new Tuple2<Integer, Long>(R.drawable.monkey_banana, 700L);
		OscilatingBillboard monkey = new OscilatingBillboard(new LayoutProportions(0.1, 0.08, 0.7, 1.7), new Displacement(200, 0), new Velocity(0.3, 0.0), slide1, slide2);
		monkey.addObstacle(hippo);

		OscilatingBillboard monkey2 = new OscilatingBillboard(new LayoutProportions(0.1, 0.08, 0.1, 1.05), new Displacement(600, 0), new Velocity(0.1, 0.0), slide1, slide2);
		monkey2.addObstacle(hippo);

		Sensor circleOfFire = new Sensor(R.drawable.ring_of_fire, new Rectangle(new LayoutProportions(0.02, 0.01, 0.82, 1.87)), new Rectangle(new LayoutProportions(0.02, 0.01, 0.6, 2.1)));
		circleOfFire.addObstacle(hippo);

		final Painting box = new Painting(new LayoutProportions(0.1, 0.08, 0.5, 1.5), R.drawable.reflector_1);
		box.properties.add(Property.MOVABLE);
		box.addObstacle(hippo);

		RammedPainting boxFactory = new RammedPainting(new LayoutProportions(0.1, 0.08, 0.05, 0.5), R.drawable.reflector_1);
		boxFactory.paintingCreatedHandler = new RammedPainting.PaintingCreatedHandler() {
			@Override
			public void onPaintingCreated(Painting painting) {
				gameView.addWorldObject(painting);
			}
		};

		RammedPainting platformsFactory = new RammedPainting(new LayoutProportions(0.1, 0.08, 0.05, 0.5), R.drawable.beam);
		boxFactory.paintingCreatedHandler = new RammedPainting.PaintingCreatedHandler() {
			@Override
			public void onPaintingCreated(Painting painting) {
				gameView.addWorldObject(painting);
			}
		};

		gameView.addWorldObject(hippo);
		gameView.addWorldObject(propulsionPlatform);
		gameView.addWorldObject(propulsionPlatform2);
		gameView.addWorldObject(propulsionPlatform3);
		gameView.addWorldObject(propulsionPlatform4);
		gameView.addWorldObject(box);
		gameView.addWorldObject(monkey);
		gameView.addWorldObject(monkey2);
		gameView.addWorldObject(circleOfFire);
		gameView.getWorld().addWorldObject(hippo);

		LinearLayout menu = new LinearLayout(this);
		menu.setOrientation(LinearLayout.HORIZONTAL);
		Button saveButton = new Button(this);
		saveButton.setText("save world");
		menu.addView(saveButton);

		Button loadButton = new Button(this);
		loadButton.setText("load world");
		menu.addView(loadButton);

		Button pauseButton = new Button(this);
		pauseButton.setText("p/r");
		menu.addView(pauseButton);

		Button editButton = new Button(this);
		editButton.setText("edit/play");
		menu.addView(editButton);

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
				gameView.suspend();
				gameView.loadWorld(jsonSerializer.fromJson(worlds[0], GameWorld.class));
				gameView.resume();
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
				gameView.isOnEditMode = !gameView.isOnEditMode;
			}
		});

		layout.addView(menu);
		layout.addView(gameView);

		final Score score = new Score(new LayoutProportions(0.0, 0.04, 0.03, 0.04), getAssets());
		gameView.hud.score = score;

		final Score inGameTimer = new Score(new LayoutProportions(0.0, 0.04, 0.7, 0.04), getAssets());
		gameView.hud.inGameTimer = inGameTimer;

		gameView.hud.rammedPaintings.add(boxFactory);
		gameView.hud.rammedPaintings.add(platformsFactory);

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
