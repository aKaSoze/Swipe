package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;

public class Painting extends Rectangle {

	public Bitmap	bitmap;

	public Painting(LayoutProportions layoutProportions, Bitmap bitmap) {
		super(layoutProportions);
		this.bitmap = bitmap;
	}

	@Override
	public void draw(Canvas canvas) {
		Displacement drawCenter = evalDrawCenter();
		canvas.drawBitmap(bitmap, drawCenter.getX().floatValue() - (evalHalfWidth().floatValue()), drawCenter.getY().floatValue() - (evalHalfHeight().floatValue()), paint);
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		bitmap = Bitmap.createScaledBitmap(bitmap, evalWidth().intValue(), evalHeight().intValue(), true);
	}

}
