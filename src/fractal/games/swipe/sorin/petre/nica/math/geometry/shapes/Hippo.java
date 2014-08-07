package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import android.graphics.BitmapFactory;
import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class Hippo extends Painting {

	public Hippo(Context context, LayoutProportions layoutProportions) {
		super(layoutProportions, BitmapFactory.decodeResource(context.getResources(), R.drawable.hippo_wacky));
	}
}
