package fractal.games.swipe.sorin.petre.nica.math.geometry.shapes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import fractal.games.swipe.sorin.petre.nica.collections.RollingList;
import fractal.games.swipe.sorin.petre.nica.collections.Tuple2;
import fractal.games.swipe.sorin.petre.nica.views.LayoutProportions;

public class Billboard extends Painting {

	private final RollingList<Tuple2<Bitmap, Long>>	slides					= new RollingList<Tuple2<Bitmap, Long>>();

	private final Tuple2<Integer, Long>[]			originalSlides;

	private Long									currentSlideShowTime	= 0L;

	@SafeVarargs
	public Billboard(Context context, LayoutProportions layoutProportions, Tuple2<Integer, Long>... slides) {
		super(context, layoutProportions, slides[0].t1);
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
		for (Tuple2<Integer, Long> originalSlide : originalSlides) {
			Bitmap unscaled = BitmapFactory.decodeResource(context.getResources(), originalSlide.t1);
			Tuple2<Bitmap, Long> slide = new Tuple2<Bitmap, Long>(Bitmap.createScaledBitmap(unscaled, evalWidth().intValue(), evalHeight().intValue(), true), originalSlide.t2);
			slides.add(slide);
		}
	}
}
