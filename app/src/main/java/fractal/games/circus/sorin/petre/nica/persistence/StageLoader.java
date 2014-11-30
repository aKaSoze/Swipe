package fractal.games.circus.sorin.petre.nica.persistence;

/**
 * Created by sorin on 30.11.2014.
 */
public class StageLoader {

    private static final String STAGE_DEFAULT_PREFIX = "Stage ";

    private final JsonSerializer jsonSerializer;

    private Integer stageIndex = 0;


    public StageLoader(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
    }

    public GameWorld loadCurrentStage() {
        return jsonSerializer.deserialize(STAGE_DEFAULT_PREFIX + stageIndex, GameWorld.class);
    }

    public void selectNextStage() {
        stageIndex++;
    }


    public void selectPreviousStage() {
        stageIndex--;
    }


    public void saveCurrentStage(GameWorld gameWorld) {
        jsonSerializer.serialize(STAGE_DEFAULT_PREFIX + stageIndex, gameWorld);
    }

}
