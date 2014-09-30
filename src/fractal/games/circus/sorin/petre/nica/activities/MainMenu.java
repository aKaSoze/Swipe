package fractal.games.circus.sorin.petre.nica.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import fractal.games.swipe.R;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
    	Intent intent = new Intent(this, GameWorldActivity.class);
    	startActivity(intent);
    }
    
}
                                                                                                                                                                                       