package fractal.games.swipe.sorin.petre.nica.collections;

import java.util.ArrayList;

public class RollingList<T> extends ArrayList<T> {

	private static final long	serialVersionUID	= 1L;

	private Integer				currentIndex		= 0;

	public T current() {
		return get(currentIndex);
	}

	public T next() {
		currentIndex = ++currentIndex == size() ? 0 : currentIndex;
		return get(currentIndex);
	}

	public void addAll(T[] ts) {
		for (T t : ts) {
			add(t);
		}
	}

}
