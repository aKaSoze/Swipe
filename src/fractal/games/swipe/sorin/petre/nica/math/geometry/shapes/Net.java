package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Displacement2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;

public class Net extends CenteredDrawable {

	private enum Status {
		STANDING, STRECTHING, RELEASED;
	}

	private final Segment2D	segment2d;

	private Point2D			strecthPoint;

	private Float			elasticityCoeficient;

	private Status			status;

	private Long			startStrecthingTime;

	private Long			elapsedTime;

	private Float			vx;

	private Float			vy;

	public Net(Segment2D segment2d) {
		super(segment2d.middle);
		this.segment2d = segment2d;
		strecthPoint = segment2d.middle;

		elasticityCoeficient = 0.00005f;
		status = Status.STANDING;
		vx = 0f;
		vy = 0f;

		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_MOVE:
			status = Status.STRECTHING;
			startStrecthingTime = elapsedTime;
			strecthPoint = new Point2D(motionEvent.getX(), motionEvent.getY());
			break;
		case MotionEvent.ACTION_UP:
			status = Status.RELEASED;
		}
	}

	@Override
	public void updateState(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
		if (status == Status.RELEASED) {
			Float distance = strecthPoint.distanceTo(segment2d.middle);
			if (distance < 1) {
				vx = 0f;
				vy = 0f;
				status = Status.STANDING;
			} else {
				Displacement2D d = strecthPoint.delta(segment2d.middle);
				vx += d.dx * elasticityCoeficient;
				vy += d.dy * elasticityCoeficient;

				Long timeFromLastStretch = elapsedTime - startStrecthingTime;
				Float dx = smallestAbs(vx * timeFromLastStretch, d.dx);
				Float dy = smallestAbs(vy * timeFromLastStretch, d.dy);

				strecthPoint = strecthPoint.translate(new Displacement2D(dx, dy));
			}
		}
	}

	private Float smallestAbs(Float a, Float b) {
		if (a < 0) {
			return Math.max(a, b);
		} else {
			return Math.min(a, b);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine(segment2d.firstPoint.getX(), segment2d.firstPoint.getY(), segment2d.secondPoint.getX(), segment2d.secondPoint.getY(), paint);
		canvas.drawCircle(strecthPoint.getX(), strecthPoint.getY(), 10, paint);
		canvas.drawLine(strecthPoint.getX(), strecthPoint.getY(), segment2d.firstPoint.getX(), segment2d.firstPoint.getY(), paint);
		canvas.drawLine(strecthPoint.getX(), strecthPoint.getY(), segment2d.secondPoint.getX(), segment2d.secondPoint.getY(), paint);
		canvas.drawLine(strecthPoint.getX(), strecthPoint.getY(), segment2d.middle.getX(), segment2d.middle.getY(), paint);
	}

}
