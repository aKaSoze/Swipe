package fractal.games.circus.sorin.petre.nica.persistence;

import fractal.games.circus.R;
import fractal.games.circus.sorin.petre.nica.math.geometry.shapes.RepeatedSprite;
import fractal.games.circus.sorin.petre.nica.views.LayoutProportions;
import fractal.games.circus.sorin.petre.nica.views.Score;

public class GameWorld {

    private final UserLoader  userLoader;
    private final StageLoader stageLoader;

    public Game game;

    public GameWorld(UserLoader userLoader, StageLoader stageLoader) {
        this.userLoader = userLoader;
        this.stageLoader = stageLoader;

        if (userLoader.userExists()) {
            game = userLoader.loadUser();
        } else {
            game = new Game();
            game.score = new Score(new LayoutProportions(0.0, 0.04, 0.03, 0.04));
            game.lives = new RepeatedSprite(new LayoutProportions(0.09, 0.05, 0.5, 0.98), R.drawable.hippo_wacky);
            game.stage = stageLoader.loadCurrentStage();
        }
    }



}
