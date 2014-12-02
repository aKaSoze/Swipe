package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import com.google.gson.annotations.Expose;

import fractal.games.circus.sorin.petre.nica.collections.RollingList;
import fractal.games.circus.sorin.petre.nica.collections.Tuple2;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Billboard extends Sprite {

	@Expose
	private RollingList<Tuple2<Integer, Long>>	slides					= new RollingList<Tuple2<Integer, Long>>();

	private Long								currentSlideShowTime	= 0L;

	public Billboard() {
		super();
	}

	@SafeVarargs
	public Billboard(LayoutProportions layoutProportions, Tuple2<Integer, Long>... slides) {
		super(layoutProportions, slides[0].t1);
		this.slides.addAll(slides);
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);
		currentSlideShowTime += timeIncrement;
		if (currentSlideShowTime >= slides.current().t2) {
			bitmapResourceId = (slides.next().t1);
			currentSlideShowTime = 0L;
		}
	}
}
