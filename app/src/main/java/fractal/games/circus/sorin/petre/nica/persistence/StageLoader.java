package fractal.games.circus.sorin.petre.nica.persistence;

import android.os.Environment;

import java.io.File;

/**
 * Created by sorin on 30.11.2014.
 */
public class StageLoader {

    private static final String STAGE_DEFAULT_PREFIX = "Circus Stages" + File.separator + "Stage ";

    private final JsonSerializer jsonSerializer;

    private Integer stageIndex = 0;

    public StageLoader(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public Stage loadCurrentStage() {
        if (stageExists(stageIndex)) {
            return jsonSerializer.deserialize(evalFilePath(), Stage.class);
        } else {
            selectPreviousStage();
            Stage Stage = loadCurrentStage();
            selectNextStage();
            saveCurrentStage(Stage);
            return loadCurrentStage();
        }
    }

    public void saveCurrentStage(Stage Stage) {
        jsonSerializer.serialize(evalFilePath(), Stage);
    }

    private String evalFilePath(Integer stageIndex) {
        return STAGE_DEFAULT_PREFIX + stageIndex;
    }

    private String evalFilePath() {
        return evalFilePath(stageIndex);
    }

    public void selectNextStage() {
        stageIndex++;
    }

    public void selectPreviousStage() {
        if (stageExists(stageIndex - 1)) {
            stageIndex--;
        }
    }

    private Boolean stageExists(Integer stageIndex) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED), evalFilePath(stageIndex));
        return file.isFile();
    }

    public Integer getStageIndex() {
        return stageIndex;
    }
}
