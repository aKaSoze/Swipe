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
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.Property;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.CollisionHandler;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.RammedPainting;
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

        Bitmap originalHippo_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hippo_wacky);
        final Painting hippo = new Painting(new LayoutProportions(0.1, 0.1, 0.3, 0.8), originalHippo_bmp);

        PropulsionPlatform propulsionPlatform = new PropulsionPlatform(new LayoutProportions(0.25, 0.01, 0.3, 0.7), hippo, MediaPlayer.create(this, R.raw.boing));
        propulsionPlatform.properties.add(Property.MOVABLE);
        propulsionPlatform.addObstacle(hippo);

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
        gameView.addWorldObject(box);
        gameView.addWorldObject(boxFactory);
        gameView.followedObject = hippo;

        layout.addView(gameView);

        final Score score = new Score(new LayoutProportions(0.0, 0.02, 0.88, 0.06), getAssets());
        gameView.score = score;

        propulsionPlatform.collisionHandlers.add(new CollisionHandler() {
            @Override
            public void onCollison() {
                score.addPoints(100L);
            }
        });

        setContentView(layout);
    }
}
