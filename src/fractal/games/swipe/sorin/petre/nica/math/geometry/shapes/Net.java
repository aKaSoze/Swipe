package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
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

    private Displacement    strecthPoint;

    private Double          elasticityCoeficient;

    private Status          status;

    private Long            strecthingTime;

    private Long            elapsedTime;

    private Velocity        springVelocity;

    public Rectangle        rectangle;

    public MediaPlayer      boingSound;

    public Net(Segment2D segment2d) {
        super(segment2d.middle, DEFAULT_PAINT);
        this.segment2d = segment2d;
        strecthPoint = segment2d.middle.cloneVector();

        elasticityCoeficient = 6.0;
        status = Status.STANDING;

        paint.setStrokeWidth(3);
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
        case MotionEvent.ACTION_MOVE:
            if (motionEvent.getY() > segment2d.middle.getY()) {
                strecthingTime = elapsedTime;
                strecthPoint.setComponents(motionEvent.getX(), motionEvent.getY());
                if (status != Status.STRECTHING) {
                    rectangle.acceleration.neutralize();
                    rectangle.velocity.neutralize();
                    rectangle.setCenter(segment2d.middle.additionVector(new Displacement(0.0, -rectangle.height / 2)));
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

    @Override
    public void updateState(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
        if (status == Status.RELEASED) {
            if (strecthPoint.distanceTo(segment2d.middle) < 0.1) {
                rectangle.velocity = new Velocity(springVelocity.getX() / 4, springVelocity.getY() / 4, LengthUnit.PIXEL, TimeUnit.SECOND);
                rectangle.acceleration = new Acceleration(0.0, 9.8, LengthUnit.METER, TimeUnit.SECOND);
                springVelocity.neutralize();
                boingSound.start();
                status = Status.STANDING;
            } else {
                Long elapsedStrecthingTime = elapsedTime - strecthingTime;
                strecthingTime = elapsedTime;

                Double distanceToSegmentMid = strecthPoint.distanceTo(segment2d.middle);
                Displacement displacement = springVelocity.generatedDisplacement(elapsedStrecthingTime);
                if (displacement.magnitude() > distanceToSegmentMid) {
                    displacement = strecthPoint.delta(segment2d.middle);
                }
                strecthPoint.add(displacement);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(segment2d.firstPoint.getX().floatValue(), segment2d.firstPoint.getY().floatValue(), segment2d.secondPoint.getX().floatValue(), segment2d.secondPoint.getY().floatValue(), paint);
        canvas.drawCircle(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), 10, paint);
        canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), segment2d.firstPoint.getX().floatValue(), segment2d.firstPoint.getY().floatValue(), paint);
        canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), segment2d.secondPoint.getX().floatValue(), segment2d.secondPoint.getY().floatValue(), paint);
        canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), segment2d.middle.getX().floatValue(), segment2d.middle.getY().floatValue(), paint);
    }

}
