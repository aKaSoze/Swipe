package fractal.games.swipe.sorin.petre.nica.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.LayoutProportions;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle.DoubleTapHandler;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.Rectangle.Property;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.views.GameView;
import fractal.games.swipe.sorin.petre.nica.views.TimerView;

public class GameWorldActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(new TimerView(this, 50));

		final GameView gameView = new GameView(this);

		Bitmap originalHippo_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hippo_wacky);
		final Rectangle hippo = new Rectangle(new LayoutProportions(0.1, 0.1, 0.3, 0.6));
		hippo.setBitmap(originalHippo_bmp);

		PropulsionPlatform propulsionPlatform = new PropulsionPlatform(new LayoutProportions(0.3, 0.01, 0.3, 0.6), hippo, MediaPlayer.create(this, R.raw.boing));
		propulsionPlatform.properties.add(Property.MOVABLE);
		propulsionPlatform.addObstacle(hippo);

		final Rectangle box = new Rectangle(new LayoutProportions(0.1, 0.1, 0.3, 0.4));
		box.properties.add(Property.MOVABLE);
		box.properties.add(Property.CLONEABLE);
		box.addObstacle(hippo);

		gameView.centeredDrawables.add(hippo);
		gameView.centeredDrawables.add(propulsionPlatform);
		gameView.centeredDrawables.add(box);

		box.doubleTapHandlers.add(new DoubleTapHandler() {
			@Override
			public void onDoubleTap(MotionEvent motionEvent, Displacement touchPoint) {
				if (box.properties.contains(Property.CLONEABLE)) {
					Rectangle newRectangle = new Rectangle(new LayoutProportions(0.1, 0.1, 0.3, 0.2));
					newRectangle.properties.addAll(box.properties);
					newRectangle.setBounds(box.getBounds());
					gameView.centeredDrawables.add(newRectangle);
					hippo.addObstacle(newRectangle);
				}
			}
		});

		layout.addView(gameView);
		setContentView(layout);
	}
}
