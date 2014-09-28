package com.indyvision.fractal_circus.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {
	private static Typeface mainTf;
//	"From Cartoon Blocks.ttf"
//	"SnackerComic_PerosnalUseOnly.ttf"
	public static void init(Context context)
	{
		mainTf = Typeface.createFromAsset(context.getAssets(), "fonts/From Cartoon Blocks.ttf");
	}
	
	public static Typeface getMainFont(){
		return mainTf;
	}
	
}
