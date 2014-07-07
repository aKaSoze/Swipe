package fractal.games.swipe.sorin.petre.nica.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class Score extends Drawable {

	private static final Paint	paint;

	public static Score			instance;

	private final Long			x;

	private final Long			y;

	static {
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setTextSize(3);
	}

	private Long				points	= 0L;

	public Score(Long x, Long y, Long textSize) {
		instance = this;
		this.x = x;
		this.y = y;
		paint.setTextSize(textSize);
	}

	public void addPoints(Long points) {
		this.points += points;
	}

	public void reset() {
		points = 0L;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawText(points.toString(), x, y, paint);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	@Override
	public void setAlpha(int alpha) {
		paint.setAlpha(alpha % 256);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
	}

}
