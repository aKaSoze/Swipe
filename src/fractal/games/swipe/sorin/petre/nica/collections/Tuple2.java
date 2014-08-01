package fractal.games.swipe.sorin.petre.nica.collections;

public class Tuple2<T1, T2> {

	public final T1	t1;
	public final T2	t2;

	public Tuple2(T1 t1, T2 t2) {
		super();
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t1 == null) ? 0 : t1.hashCode());
		result = prime * result + ((t2 == null) ? 0 : t2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

}
