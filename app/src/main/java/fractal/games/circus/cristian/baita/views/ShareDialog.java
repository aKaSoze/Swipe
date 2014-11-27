package fractal.games.circus.cristian.baita.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import fractal.games.circus.R;

public class ShareDialog extends Dialog {
	private Context	context;

	public ShareDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);

		setContentView(R.layout.dialog_share);
		setCanceledOnTouchOutside(true);

		// LayoutParams params = getWindow().getAttributes();
		// params.height = LayoutParams.FILL_PARENT;
		// getWindow().setAttributes((android.view.WindowManager.LayoutParams)
		// params);
	}

}