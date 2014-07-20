package fractal.games.swipe.sorin.petre.nica.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.LayoutProportions;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.Property;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.CollisionHandler;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle.DoubleTapHandler;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.GameView;
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
        final Rectangle hippo = new Rectangle(new LayoutProportions(0.1, 0.1, 0.3, 0.8));
        hippo.setBitmap(originalHippo_bmp);

        PropulsionPlatform propulsionPlatform = new PropulsionPlatform(new LayoutProportions(0.25, 0.01, 0.3, 0.7), hippo, MediaPlayer.create(this, R.raw.boing));
        propulsionPlatform.properties.add(Property.MOVABLE);
        propulsionPlatform.addObstacle(hippo);

        final Rectangle box = new Rectangle(new LayoutProportions(0.1, 0.08, 0.5, 0.9));
        box.properties.add(Property.MOVABLE);
        box.properties.add(Property.CLONEABLE);
        box.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.reflector_1));
        box.addObstacle(hippo);

        gameView.centeredDrawables.add(hippo);
        gameView.centeredDrawables.add(propulsionPlatform);
        gameView.centeredDrawables.add(box);
        gameView.followedObject = hippo;

        box.doubleTapHandlers.add(new DoubleTapHandler() {
            @Override
            public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
                if (box.properties.contains(Property.CLONEABLE)) {
                    Log.i("cloning", "cloning box");
                    Rectangle newRectangle = new Rectangle(box.layoutProportions);
                    newRectangle.properties.addAll(box.properties);
                    newRectangle.drawTranslation.makeEqualTo(box.drawTranslation);
                    newRectangle.setBitmap(box.getBitmap());
                    newRectangle.setBounds(box.getBounds());
                    gameView.centeredDrawables.add(newRectangle);
                    hippo.addObstacle(newRectangle);
                }
            }
        });

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
