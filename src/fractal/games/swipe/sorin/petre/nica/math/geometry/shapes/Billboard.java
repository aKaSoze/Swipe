package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import fractal.games.swipe.sorin.petre.nica.collections.RollingList;
import fractal.games.swipe.sorin.petre.nica.collections.Tuple2;

public class Billboard extends Painting {

	private final RollingList<Tuple2<Bitmap, Long>>	slides					= new RollingList<Tuple2<Bitmap, Long>>();

	private Long									currentSlideShowTime	= 0L;

	@SafeVarargs
	public Billboard(LayoutProportions layoutProportions, Tuple2<Bitmap, Long>... slides) {
		super(layoutProportions, slides[0].t1);
		this.slides.addAll(slides);
	}

	@Override
	public void updateState(Long elapsedTime) {
		super.updateState(elapsedTime);
		currentSlideShowTime += timeIncrement;
		if (currentSlideShowTime >= slides.current().t2) {
			bitmap = slides.next().t1;
			currentSlideShowTime = 0L;
		}
	}
}
