package fractal.games.swipe.sorin.petre.nica.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Score extends View {

	private static final Paint	paint;

	public static Score			instance;

	static {
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setTextSize(3);
	}

	private Long				points	= 0L;

	public Score(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Score(Context context) {
		super(context);
		instance = this;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(200, 60);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.DKGRAY);
		canvas.drawText(points.toString(), 0, 0, paint);
	}

	public void addPoints(Long points) {
		this.points += points;
	}

	public void reset() {
		points = 0L;
	}

}
