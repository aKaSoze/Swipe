package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Rect;
import fractal.games.circus.sorin.petre.nica.collections.RollingList;
import fractal.games.circus.sorin.petre.nica.collections.Tuple2;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class Billboard extends Painting {

	private final RollingList<Tuple2<Integer, Long>>	slides					= new RollingList<Tuple2<Integer, Long>>();

	private final Tuple2<Integer, Long>[]				originalSlides;

	private Long										currentSlideShowTime	= 0L;

	@SafeVarargs
	public Billboard(LayoutProportions layoutProportions, Tuple2<Integer, Long>... slides) {
		super(layoutProportions, slides[0].t1);
		originalSlides = slides;
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

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		for (Tuple2<Integer, Long> slide : originalSlides) {
			slides.add(slide);
		}
	}
}
