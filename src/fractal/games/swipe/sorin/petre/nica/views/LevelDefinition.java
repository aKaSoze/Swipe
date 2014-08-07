package fractal.games.swipe.sorin.petre.nica.views;

import java.util.Map;
import java.util.Set;

public class LevelDefinition {

	public final Set<Map<String, Object>>	centeredDrawables;

	public final Map<String, Object>		followedObject;

	public LevelDefinition(Set<Map<String, Object>> centeredDrawables, Map<String, Object> followedObject) {
		this.centeredDrawables = centeredDrawables;
		this.followedObject = followedObject;
	}

}
