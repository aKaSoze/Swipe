package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.math.objects.Segment2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;

public class Net extends CenteredDrawable {

    private enum Status {
        STANDING, STRECTHING, RELEASED;
    }

    private final Segment2D segment2d;

    private Point2D         strecthPoint;

    private Double          elasticityCoeficient;

    private Status          status;

    private Long            strecthingTime;

    private Long            elapsedTime;

    private Velocity        springVelocity;

    public Net(Segment2D segment2d) {
        super(segment2d.middle);
        this.segment2d = segment2d;
        strecthPoint = segment2d.middle;

        elasticityCoeficient = 6.0;
        status = Status.STANDING;

        paint.setColor(Color.WHITE);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
        case MotionEvent.ACTION_MOVE:
            if (motionEvent.getY() > segment2d.middle.getY()) {
                strecthingTime = elapsedTime;
                strecthPoint = new Point2D(motionEvent.getX(), motionEvent.getY());
                if (status != Status.STRECTHING) {
                    circle.acceleration.neutralize();
                    circle.velocity.neutralize();
                    circle.setCenter(segment2d.middle.translate(new Displacement(0, -30)));
                    status = Status.STRECTHING;
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            Displacement displacement = strecthPoint.delta(segment2d.middle);
            springVelocity = new Velocity(displacement.getX() * elasticityCoeficient, displacement.getY() * elasticityCoeficient, LengthUnit.PIXEL, TimeUnit.SECOND);
            status = Status.RELEASED;
            break;
        }
    }

    public Circle      circle;

    public MediaPlayer boingSound;

    @Override
    public void updateState(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
        if (status == Status.RELEASED) {
            if (strecthPoint.distanceTo(segment2d.middle) < 1) {
                circle.velocity = new Velocity(springVelocity.getX() / 4, springVelocity.getY() / 4, LengthUnit.PIXEL, TimeUnit.SECOND);
                circle.acceleration = new Acceleration(0.0, 9.8, LengthUnit.METER, TimeUnit.SECOND);
                springVelocity.neutralize();
                boingSound.start();
                status = Status.STANDING;
            } else {
                Long elapsedStrecthingTime = elapsedTime - strecthingTime;
                strecthingTime = elapsedTime;

                Float distanceToSegmentMid = strecthPoint.distanceTo(segment2d.middle);
                Displacement displacement = springVelocity.generatedDisplacement(elapsedStrecthingTime);
                if (displacement.magnitude() > distanceToSegmentMid) {
                    displacement = strecthPoint.delta(segment2d.middle);
                }
                strecthPoint = strecthPoint.translate(displacement);
            }
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
