package fractal.games.circus.cristian.baita.views.custom;

import fractal.games.circus.cristian.baita.utils.FontUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView{

	public CustomTextView(Context context) {
		super(context);
		init();
	}
	
	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * Instantiates a new woow text view bold.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		setTypeface(FontUtils.getMainFont());
	}

}
