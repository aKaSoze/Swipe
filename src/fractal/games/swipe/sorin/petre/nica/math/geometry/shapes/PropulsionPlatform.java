package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Acceleration;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Displacement;
import fractal.games.swipe.sorin.petre.nica.physics.kinematics.Velocity;
import fractal.games.swipe.sorin.petre.nica.physics.units.LengthUnit;
import fractal.games.swipe.sorin.petre.nica.physics.units.TimeUnit;

public class PropulsionPlatform extends CenteredDrawable {

	private static final int	MAX_SPRING_DISPLACEMENT	= 200;

	private enum Status {
		STANDING, STRECTHING, RELEASED;
	}

	private final Rectangle	platform;

	private Displacement	strecthPoint;

	private Double			elasticityCoeficient;

	private Status			status;

	private Long			strecthingTime;

	private Long			elapsedTime;

	private Velocity		springVelocity;

	public final Rectangle	projectile;

	public MediaPlayer		boingSound;

	public PropulsionPlatform(Rectangle platform, Rectangle projectile) {
		super(platform.getCenter(), DEFAULT_PAINT);
		this.platform = platform;
		this.projectile = projectile;
		projectile.obstacles.add(platform);
		strecthPoint = platform.getCenter().cloneVector();

		elasticityCoeficient = 22.0;
		status = Status.STANDING;

		paint.setStrokeWidth(3);
	}

	@Override
	public void onMotionEvent(MotionEvent motionEvent, Displacement touchPoint) {
		switch (motionEvent.getActionMasked()) {
		case MotionEvent.ACTION_MOVE:
			Displacement springDisplacement = touchPoint.subtractionVector(platform.getCenter());
			if (motionEvent.getY() > platform.getCenter().getY() && springDisplacement.magnitude() < MAX_SPRING_DISPLACEMENT) {
				strecthingTime = elapsedTime;
				strecthPoint.setComponents(motionEvent.getX(), motionEvent.getY());
				if (status != Status.STRECTHING) {
					projectile.acceleration.neutralize();
					projectile.velocity.neutralize();
					projectile.setCenter(platform.getCenter().additionVector(new Displacement(0.0, -projectile.height / 2)));
					status = Status.STRECTHING;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			Displacement displacement = strecthPoint.delta(platform.getCenter());
			springVelocity = new Velocity(displacement.getX() * elasticityCoeficient, displacement.getY() * elasticityCoeficient, LengthUnit.PIXEL, TimeUnit.SECOND);
			status = Status.RELEASED;
			break;
		}
	}

	@Override
	public void updateState(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
		if (status == Status.RELEASED) {
			if (strecthPoint.distanceTo(platform.getCenter()) < 0.01) {
				projectile.velocity = new Velocity(springVelocity.getX() / 4, springVelocity.getY() / 4, LengthUnit.PIXEL, TimeUnit.SECOND);
				projectile.acceleration = new Acceleration(0.0, 9.8, LengthUnit.METER, TimeUnit.SECOND);
				springVelocity.neutralize();
				boingSound.start();
				status = Status.STANDING;
			} else {
				Long elapsedStrecthingTime = elapsedTime - strecthingTime;
				strecthingTime = elapsedTime;

				Double distanceToSegmentMid = strecthPoint.distanceTo(platform.getCenter());
				Displacement displacement = springVelocity.generatedDisplacement(elapsedStrecthingTime);
				if (displacement.magnitude() > distanceToSegmentMid) {
					displacement = strecthPoint.delta(platform.getCenter());
				}
				strecthPoint.add(displacement);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		platform.draw(canvas);
		canvas.drawCircle(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), 10, paint);
		canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), platform.evaluateLeftTopCorner().getX().floatValue(), platform.evaluateLeftTopCorner().getY().floatValue(), paint);
		canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), platform.evaluateRightTopCorner().getX().floatValue(), platform.evaluateRightTopCorner().getY().floatValue(), paint);
		canvas.drawLine(strecthPoint.getX().floatValue(), strecthPoint.getY().floatValue(), platform.getCenter().getX().floatValue(), platform.getCenter().getY().floatValue(), paint);
	}

}
