package fractal.games.circus.sorin.petre.nica.persistence;

import android.os.Environment;

import java.io.File;

/**
 * Created by sorin on 30.11.2014.
 */
public class UserLoader {

    private static final String FILE_PATH = "Game";

    private final JsonSerializer jsonSerializer;

    public UserLoader(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public Game loadUser() {
        return jsonSerializer.deserialize(FILE_PATH, Game.class);
    }

    public void saveUser(Game game) {
        jsonSerializer.serialize(FILE_PATH, game);
    }

    public Boolean userExists() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED), FILE_PATH);
        return file.isFile();
    }
}
