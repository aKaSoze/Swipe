package fractal.games.swipe.sorin.petre.nica.persistence;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

import com.google.gson.Gson;

public class JsonSerializer {

	private final Context	context;

	private final Gson		gsonService;

	public JsonSerializer(Context context) {
		this.context = context;
		gsonService = new Gson();
	}

	public void serialize(String fileName, Object object) {
		try {
			FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream.write(gsonService.toJson(object).getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T deserialize(String fileName, Class<T> clazz) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(fileName));
			T t = gsonService.fromJson(inputStreamReader, clazz);
			inputStreamReader.close();
			return t;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
