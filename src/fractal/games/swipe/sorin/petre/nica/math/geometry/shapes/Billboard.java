package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.graphics.Bitmap;
import android.graphics.Rect;
import fractal.games.swipe.sorin.petre.nica.collections.RollingList;
import fractal.games.swipe.sorin.petre.nica.collections.Tuple2;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class Billboard extends Painting {

	private final RollingList<Tuple2<Bitmap, Long>>	slides					= new RollingList<Tuple2<Bitmap, Long>>();

	private final Tuple2<Bitmap, Long>[]			originalSlides;

	private Long									currentSlideShowTime	= 0L;

	@SafeVarargs
	public Billboard(LayoutProportions layoutProportions, Tuple2<Bitmap, Long>... slides) {
		super(layoutProportions, slides[0].t1);
		originalSlides = slides;
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

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		for (Tuple2<Bitmap, Long> originalSlide : originalSlides) {
			Tuple2<Bitmap, Long> slide = new Tuple2<Bitmap, Long>(Bitmap.createScaledBitmap(originalSlide.t1, evalWidth().intValue(), evalHeight().intValue(), true), originalSlide.t2);
			slides.add(slide);
		}
	}
}
