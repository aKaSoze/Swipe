package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.gson.annotations.Expose;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Sprite extends Rectangle {

	@Expose
	protected Integer	bitmapResourceId;

	public Sprite() {
		super();
	}

	public Sprite(LayoutProportions layoutProportions, Integer bitmapResourceId) {
		super(layoutProportions);
		this.bitmapResourceId = bitmapResourceId;
	}

	@Override
	public void draw(Canvas canvas) {
		Displacement drawCenter = evalDrawCenter();
		canvas.drawBitmap(getBitmap(), drawCenter.x.floatValue() - (evalHalfWidth().floatValue()), drawCenter.y.floatValue() - (evalHalfHeight().floatValue()), paint);
	}

	protected Bitmap getBitmap() {
		Integer bitmapResourceId = this.bitmapResourceId == null ? R.drawable.evil_monkey : this.bitmapResourceId;
		if (layoutProportions == null) {
			return MediaStore.getBitmap(bitmapResourceId);
		} else {
			return MediaStore.getScaledBitmap(bitmapResourceId, evalWidth().intValue(), evalHeight().intValue());
		}
	}
}
