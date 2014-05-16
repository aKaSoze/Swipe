package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.math.objects.Point2D;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;

public abstract class AnimatedShape extends CenteredDrawable {

    protected Acceleration acceleration;

    protected Velocity     velocity;

    protected Displacement displacement;

    protected Long         lastElapsedTime;
    
    public AnimatedShape(Point2D center) {
        super(center);
        lastElapsedTime = 0L;
        acceleration = new Acceleration(0.0, 0.0);
        velocity = new Velocity(0, 0);
        displacement = new Displacement(center.getX(), center.getY());
    }

    @Override
    public void onMotionEvent(MotionEvent motionEvent) {
        // TODO Auto-generated method stub

    }

    public void updateState(Long elapsedTime) {
        Long timeIncrement = elapsedTime - lastElapsedTime;
        Displacement addedDisplacement = velocity.generatedDisplacement(timeIncrement);
        displacement = displacement.add(addedDisplacement);
        setCenter(new Point2D(displacement.x, displacement.y));
        Velocity addedVelocity = acceleration.generatedVelocity(timeIncrement);
        velocity = velocity.add(addedVelocity);
        lastElapsedTime = elapsedTime;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(center.getX(), center.getY(), center.getX() + acceleration.x.floatValue(), center.getY() + acceleration.y.floatValue(), paint);
        canvas.drawLine(center.getX(), center.getY(), center.getX() + velocity.x.floatValue(), center.getY() + velocity.y.floatValue(), paint);
    }

}
