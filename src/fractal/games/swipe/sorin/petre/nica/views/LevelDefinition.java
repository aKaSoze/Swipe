package fractal.games.swipe.sorin.petre.nica.views;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable;
import fractal.games.swipe.sorin.petre.nica.math.geometry.shapes.CenteredDrawable.LayoutProportions;

public class LevelDefinition {

	public final Set<CenteredDrawableSerialForm>	centeredDrawables	= new HashSet<CenteredDrawableSerialForm>();

	public final CenteredDrawableSerialForm			followedObject;

	public void loadLevel(LevelDefinition levelDefinition) {
		selectedShape = null;
		centeredDrawables.clear();
		followedObject = fromSerialForm(levelDefinition.followedObject);
		for (CenteredDrawableSerialForm serialForm : levelDefinition.centeredDrawables) {
			centeredDrawables.add(fromSerialForm(serialForm));
		}
	}

	public LevelDefinition(CenteredDrawableSerialForm followedObject) {
		this.followedObject = followedObject;
	}

	public class CenteredDrawableSerialForm {
		public final LayoutProportions					layoutProportions;
		public final Class<? extends CenteredDrawable>	actualClass;

		public CenteredDrawableSerialForm(LayoutProportions layoutProportions, Class<? extends CenteredDrawable> actualClass) {
			this.layoutProportions = layoutProportions;
			this.actualClass = actualClass;
		}
	}

	public static CenteredDrawable fromSerialForm(CenteredDrawableSerialForm serialForm) {
		try {
			return serialForm.actualClass.getConstructor(LayoutProportions.class).newInstance(serialForm.layoutProportions);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
