package fractal.games.circus.sorin.petre.nica.persistence;

import android.os.Environment;

import java.io.File;

/**
 * Created by sorin on 30.11.2014.
 */
public class StageLoader {

    private static final String STAGE_DEFAULT_PREFIX = "Circus" + File.separator + "Stages" + File.separator + "Stage ";

    private final JsonSerializer jsonSerializer;

    public Long stageIndex = 0L;

    public StageLoader(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public Stage loadCurrentStage() {
        if (stageExists(stageIndex)) {
            return jsonSerializer.deserialize(evalFilePath(), Stage.class);
        } else {
            return new Stage();
        }
    }

    public void saveCurrentStage(Stage Stage) {
        jsonSerializer.serialize(evalFilePath(), Stage);
    }

    private String evalFilePath(Long stageIndex) {
        return STAGE_DEFAULT_PREFIX + stageIndex;
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
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED), evalFilePath(stageIndex));
        return file.isFile();
    }
}
