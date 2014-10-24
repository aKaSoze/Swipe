package fractal.games.circus.sorin.petre.nica.persistence;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.gson.annotations.Expose;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.OscilatingBillboard;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform.ImpactHandler;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Sensor;
import fractal.games.circus.sorin.petre.nica.media.MediaStore;
import fractal.games.circus.sorin.petre.nica.physics.kinematics.Velocity;

public class GameWorld {

	@Expose
	private Hippo						hippo;

	@Expose
	private Set<PropulsionPlatform>		platforms				= new CopyOnWriteArraySet<PropulsionPlatform>();

	@Expose
	private Set<Sensor>					sensors					= new CopyOnWriteArraySet<Sensor>();

	@Expose
	private Set<OscilatingBillboard>	oscilatingBillboards	= new CopyOnWriteArraySet<OscilatingBillboard>();

	@Expose
	private Set<Painting>				paintings				= new CopyOnWriteArraySet<Painting>();

	private ImpactHandler				impactHandler			= new ImpactHandler() {
																	@Override
																	public void onImpact(PropulsionPlatform propulsionPlatform) {
																		if (hippo.velocity.isZero() && propulsionPlatform.touchesOnHorizontalSide(hippo)) {
																			Velocity springVelocity = propulsionPlatform.getSpringVelocity();
																			hippo.velocity.setComponents(springVelocity.x / 4, springVelocity.y / 4);
																			hippo.acceleration.y = -9.8;
																			MediaStore.getSound(R.raw.boing).start();
																		}
																	}
																};

	public void addWorldObject(Painting painting) {
		if (painting instanceof PropulsionPlatform) {
			PropulsionPlatform platform = (PropulsionPlatform) painting;
			platforms.add(platform);
			if (hippo != null) {
				platform.addObstacle(hippo);
				platform.impactHandlers.add(impactHandler);
			}
		} else if (painting instanceof Hippo) {
			hippo = (Hippo) painting;
			for (PropulsionPlatform platform : platforms) {
				platform.addObstacle(hippo);
				platform.impactHandlers.add(impactHandler);
			}
		} else if (painting instanceof Sensor) {
			Sensor sensor = (Sensor) painting;
			sensor.addObstacle(hippo);
			sensors.add(sensor);
		} else if (painting instanceof OscilatingBillboard) {
			OscilatingBillboard oscilatingBillboard = (OscilatingBillboard) painting;
			oscilatingBillboard.addObstacle(hippo);
			oscilatingBillboards.add(oscilatingBillboard);
		} else {
			paintings.add(painting);
			if (hippo != null) {
				painting.addObstacle(hippo);
			}
		}
	}

	public Set<CenteredDrawable> getAllObjects() {
		Set<CenteredDrawable> objects = new CopyOnWriteArraySet<CenteredDrawable>(platforms);
		objects.addAll(sensors);
		objects.addAll(oscilatingBillboards);
		objects.addAll(paintings);
		objects.add(hippo);
		return objects;
	}

	public Hippo getHippo() {
		return hippo;
	}

}
