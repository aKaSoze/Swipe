package fractal.games.circus.sorin.petre.nica.persistence;

import android.os.Environment;

import java.io.File;

/**
 * Created by sorin on 30.11.2014.
 */
public class GameLoader {

    private static final String FILE_PATH = "Circus" + File.separator + "Game";

    private final JsonSerializer jsonSerializer;
    private final StageLoader    stageLoader;

    public GameLoader(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
        stageLoader = new StageLoader(jsonSerializer);
    }

    public Game loadGame() {
        try {
            Game game = jsonSerializer.deserialize(FILE_PATH, Game.class);
            game.stageLoader = stageLoader;
            return game;
        } catch (Exception e) {
            Game game = new Game(stageLoader);
            saveGame(game);
            return game;
        }
    }

    public void saveGame(Game game) {
        jsonSerializer.serialize(FILE_PATH, game);
    }

    public Boolean gameExists() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED), FILE_PATH);
        return file.isFile();
    }
}