package fractal.games.circus.sorin.petre.nica.persistence;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.Hippo;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.PropulsionPlatform;

public class GameWorld {
	@Expose
	public Hippo					hippo;

	@Expose
	public Set<PropulsionPlatform>	platforms	= new CopyOnWriteArraySet<PropulsionPlatform>();

	public Set<CenteredDrawable> getAllObjects() {
		Set<CenteredDrawable> objects = new CopyOnWriteArraySet<CenteredDrawable>(platforms);
		objects.add(hippo);
		return objects;
	}

}
