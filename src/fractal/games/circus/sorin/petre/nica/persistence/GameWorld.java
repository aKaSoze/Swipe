package fractal.games.circus.sorin.petre.nica.persistence;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Painting;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;

public class GameWorld {

	@Expose
	public Hippo					hippo;

	@Expose
	private Set<PropulsionPlatform>	platforms	= new CopyOnWriteArraySet<PropulsionPlatform>();

	public void addWorldObject(Painting painting) {
		if (painting instanceof PropulsionPlatform) {
			PropulsionPlatform platform = (PropulsionPlatform) painting;
			platforms.add(platform);
			if (hippo != null) {
				platform.addObstacle(hippo);
			}
		} else if (painting instanceof Hippo) {
			hippo = (Hippo) painting;
			for (PropulsionPlatform platform : platforms) {
				platform.addObstacle(hippo);
			}
		}
	}

	public Set<CenteredDrawable> getAllObjects() {
		Set<CenteredDrawable> objects = new CopyOnWriteArraySet<CenteredDrawable>(platforms);
		objects.add(hippo);
		return objects;
	}

	public Hippo getHippo() {
		return hippo;
	}

}
