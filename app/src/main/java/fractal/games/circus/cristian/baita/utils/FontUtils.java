package fractal.games.circus.cristian.baita.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {
	private static Typeface mainTf;
//	"From Cartoon Blocks.ttf"
//	"SnackerComic_PerosnalUseOnly.ttf"
//	Tequilla Sunrise.ttf
	public static void init(Context context)
	{
		mainTf = Typeface.createFromAsset(context.getAssets(), "fonts/Tequilla Sunrise.ttf");
	}
	
	public static Typeface getMainFont(){
		return mainTf;
	}
	
}
