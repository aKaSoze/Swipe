package fractal.games.circus.sorin.petre.nica.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RammedSprite;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sprite;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.persistence.Game;
import fractal.games.circus.sorin.petre.nica.persistence.GameLoader;
import fractal.games.circus.sorin.petre.nica.views.GameView;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class GameWorldActivity extends Activity {

    private GameLoader gameLoader;
    private Game       game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MediaStore.context = this;
        MediaStore.assetManager = getAssets();

        gameLoader = new GameLoader();

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        game = gameLoader.loadGame();
        final GameView gameView = new GameView(this, game);

//        Tuple2<Integer, Long> slide1 = new Tuple2<Integer, Long>(R.drawable.evil_monkey, 700L);
//        Tuple2<Integer, Long> slide2 = new Tuple2<Integer, Long>(R.drawable.monkey_banana, 700L);
//        OscillatingBillboard monkey = new OscillatingBillboard(new LayoutProportions(0.1, 0.08, 0.7, 1.7), new Displacement(200, 0), new Velocity(0.3, 0.0), slide1, slide2);

        RammedSprite boxFactory = new RammedSprite(new LayoutProportions(0.1, 0.08, 0.2, 0.22), R.drawable.reflector_1);
        boxFactory.paintingCreatedHandler = new RammedSprite.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Sprite sprite) {
                gameView.game.addWorldObject(sprite);
            }
        };

        RammedSprite platformsFactory = new RammedSprite(new LayoutProportions(0.25, 0.09, 0.5, 0.22), R.drawable.beam);
        platformsFactory.paintingCreatedHandler = new RammedSprite.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Sprite sprite) {
                gameView.game.addWorldObject(sprite);
            }
        };
        platformsFactory.paintingConstructor = new RammedSprite.PaintingConstructor() {
            @Override
            public Sprite construct() {
                return new PropulsionPlatform();
            }
        };

        RammedSprite circlesFactory = new RammedSprite(new LayoutProportions(0.3, 0.26, 0.8, 0.22), R.drawable.ring_of_fire);
        circlesFactory.paintingCreatedHandler = new RammedSprite.PaintingCreatedHandler() {
            @Override
            public void onPaintingCreated(Sprite sprite) {
                gameView.game.addWorldObject(sprite);
            }
        };
        circlesFactory.paintingConstructor = new RammedSprite.PaintingConstructor() {
            @Override
            public Sprite construct() {
                return new Sensor(new LayoutProportions(0.3, 0.26, 0.8, 0.22), R.drawable.ring_of_fire);
            }
        };

        LinearLayout menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout navigationMenu = new LinearLayout(this);
        navigationMenu.setOrientation(LinearLayout.HORIZONTAL);

        Button saveButton = new Button(this);
        saveButton.setText("save");
        menu.addView(saveButton);

        final Button loadButton = new Button(this);
        loadButton.setText("load");
        menu.addView(loadButton);

        Button pauseButton = new Button(this);
        pauseButton.setText("p/r");
        menu.addView(pauseButton);

        Button editButton = new Button(this);
        editButton.setText("edit/play");
        menu.addView(editButton);

        Button upButton = new Button(this);
        upButton.setText("u");
        navigationMenu.addView(upButton);

        Button downButton = new Button(this);
        downButton.setText("d");
        navigationMenu.addView(downButton);

        final Button nextStageButton = new Button(this);
        nextStageButton.setText(">");
        navigationMenu.addView(nextStageButton);

        Button previousStageButton = new Button(this);
        previousStageButton.setText("<");
        navigationMenu.addView(previousStageButton);

        saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gameView.game.stageLoader.saveCurrentStage(gameView.game.stage);
            }
        });

        loadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.loadCurrentStage();
            }
        });

        nextStageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gameView.game.stageLoader.selectNextStage();
                loadButton.callOnClick();
            }
        });

        previousStageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.game.stageLoader.selectPreviousStage();
                loadButton.callOnClick();
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

        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gameView.isSlidingUp = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.isSlidingUp = false;
                        break;
                }
                return true;
            }
        });

        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gameView.isSlidingDown = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.isSlidingDown = false;
                        break;
                }
                return true;
            }
        });

        layout.addView(menu);
        layout.addView(navigationMenu);
        layout.addView(gameView);

        gameView.hud.rammedPaintings.add(boxFactory);
        gameView.hud.rammedPaintings.add(platformsFactory);
        gameView.hud.rammedPaintings.add(circlesFactory);

        setContentView(layout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameLoader.saveGame(game);
    }
}
