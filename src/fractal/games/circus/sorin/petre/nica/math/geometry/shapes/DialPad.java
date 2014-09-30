package fractal.games.circus.sorin.petre.nica.math.geometry.shapes;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;

public class DialPad extends ViewGroup {

	public final LayoutProportions	layoutProportions;

	private final Set<Button>		numberButtons	= new HashSet<Button>();

	public DialPad(Context context, AttributeSet attributeSet, int defStyle, LayoutProportions layoutProportions) {
		super(context, attributeSet, defStyle);
		this.layoutProportions = layoutProportions;

		for (int i = 0; i < 10; i++) {
			Button button = new Button(getContext());
			button.setText(String.valueOf(i));
			numberButtons.add(button);
			addView(button);
		}
	}

	public DialPad(Context context, AttributeSet attributeSet, int defStyle) {
		this(context, attributeSet, defStyle, new LayoutProportions(0.20, 0.20, 0.0, 0.0));
	}

	public DialPad(Context context, LayoutProportions layoutProportions) {
		this(context, null, layoutProportions);
	}

	public DialPad(Context context, AttributeSet attributeSet, LayoutProportions layoutProportions) {
		this(context, attributeSet, 0, layoutProportions);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int width = r - l;
			int height = b - t;

			for (Button numButton : numberButtons) {
			}
		}
	}
}
