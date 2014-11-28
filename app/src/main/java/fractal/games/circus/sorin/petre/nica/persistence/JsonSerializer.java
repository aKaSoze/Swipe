package fractal.games.circus.sorin.petre.nica.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonSerializer {

    private final Context context;

    private final Gson gsonService;

    public JsonSerializer(Context context) {
        this.context = context;
        gsonService = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public void serialize(String fileName, Object object) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.MEDIA_MOUNTED), "Circus Stages");
            file.mkdirs();
            Log.i("json", file.getAbsolutePath() + File.separator + fileName);
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath() + File.separator + fileName);
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
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.MEDIA_MOUNTED), "Circus Stages");
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file.getAbsolutePath() + File.separator + fileName));
            T t = gsonService.fromJson(inputStreamReader, clazz);
            inputStreamReader.close();
            return t;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String jsonForm(Object object) {
        return gsonService.toJson(object);
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        return gsonService.fromJson(json, clazz);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
