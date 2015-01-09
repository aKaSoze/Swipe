package fractal.games.circus.sorin.petre.nica.persistence;

import android.os.Environment;

import java.io.File;

/**
 * Created by sorin on 30.11.2014.
 */
public class StageLoader {

    private static final String STAGE_FILE_BASE_NAME = "Stage ";

    public enum Mode {
        DEV("Circus" + File.separator + "Stages" + File.separator + STAGE_FILE_BASE_NAME), USER("stages" + File.separator + STAGE_FILE_BASE_NAME);

        public final String stageBasePath;

        private Mode(String stageBasePath) {
            this.stageBasePath = stageBasePath;
        }

    }

    private final JsonSerializer jsonSerializer;

    public Long stageIndex = 0L;
    public Mode mode       = Mode.USER;

    public StageLoader(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public Stage loadCurrentStage() {
        if (stageExists(stageIndex)) {
            return mode == Mode.USER ? jsonSerializer.deserializeAsset(evalFilePath(), Stage.class) : jsonSerializer.deserialize(evalFilePath(), Stage.class);
        } else {
            return new Stage();
        }
    }

    public void saveCurrentStage(Stage Stage) {
        jsonSerializer.serialize(evalFilePath(), Stage);
    }

    private String evalFilePath(Long stageIndex) {
        return mode.stageBasePath + stageIndex;
    }

    private String evalFilePath() {
        return evalFilePath(stageIndex);
    }

    public void selectNextStage() {
        stageIndex++;
    }

    public void selectPreviousStage() {
        stageIndex--;
    }

    private Boolean stageExists(Long stageIndex) {
        if (mode == Mode.DEV) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED), evalFilePath(stageIndex));
            return file.isFile();
        } else {
            return true;
        }
    }
}
