package fractal.games.circus.sorin.petre.nica.collections;

import com.google.gson.annotations.Expose;

public class Tuple2<T1, T2> {

	@Expose
	public T1	t1;

	@Expose
	public T2	t2;

	public Tuple2(T1 t1, T2 t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	public Tuple2() {
		this(null, null);
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
