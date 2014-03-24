package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import java.util.Random;

import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;

public class CircleDrawable extends CenteredDrawable {

    public final Float   radius;
    private final Random random = new Random();

    public CircleDrawable(Point2D center, Float radius) {
        super(center);
        setShape(new OvalShape());
        this.radius = radius;
        setBounds((int) Math.floor(center.getX() - radius), (int) Math.floor(center.getY() - radius), Math.round(center.getX() + radius), Math.round(center.getY() + radius));
        getPaint().setColor(Color.WHITE);
        getPaint().setStyle(Style.STROKE);
        getPaint().setStrokeWidth(4);
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
        case MotionEvent.ACTION_MOVE:
            setCenter(Point2D.Factory.fromMotionEvent(motionEvent));
        }
    }

    @Override
    public void updateState(Long elapsedTime) {
        getPaint().setStrokeWidth(getPaint().getStrokeWidth() + (random.nextInt(2) == 0 && getPaint().getStrokeWidth() < 15 ? 1 : -1));
    }

    @Override
    public void setCenter(Point2D newCenter) {
        super.setCenter(newCenter);
        setBounds((int) Math.floor(center.getX() - radius), (int) Math.floor(center.getY() - radius), Math.round(center.getX() + radius), Math.round(center.getY() + radius));
    }

}
