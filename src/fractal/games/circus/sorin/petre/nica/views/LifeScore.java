package fractal.games.circus.sorin.petre.nica.views;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class LifeScore extends Drawable {

	private LayoutProportions	layoutProportions;

	private Long				lives	= 0L;

	public LifeScore(LayoutProportions layoutProportions, AssetManager assets) {
		this.layoutProportions = layoutProportions;
	}

	@Override
	public void draw(Canvas canvas) {
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
	}
}
