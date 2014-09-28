package com.indyvision.fractal_circus.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.indyvision.fractal_circus.utils.FontUtils;
import com.indyvision.fractal_circus.views.FeedbackDialog;
import com.indyvision.fractal_circus.views.ScoresDialog;
import com.indyvision.fractal_circus.views.SettingsDialog;
import com.indyvision.fractal_circus.views.ShareDialog;

import fractal.games.swipe.R;
import fractal.games.swipe.sorin.petre.nica.activities.GameWorldActivity;

public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontUtils.init(this);
        setContentView(R.layout.activity_main_menu);

        Button playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GameWorldActivity.class);
                startActivity(intent);
            }
        });

        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new SettingsDialog(MenuActivity.this, android.R.style.Theme_Translucent_NoTitleBar).show();
            }
        });
        Button feedbackButton = (Button) findViewById(R.id.feedback_button);
        feedbackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new FeedbackDialog(MenuActivity.this, android.R.style.Theme_Translucent_NoTitleBar).show();
            }
        });
        Button scoresButton = (Button) findViewById(R.id.achievements_button);
        scoresButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScoresDialog(MenuActivity.this, android.R.style.Theme_Translucent_NoTitleBar).show();
            }
        });
        Button shareButton = (Button) findViewById(R.id.share_button);
        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog(MenuActivity.this, android.R.style.Theme_Translucent_NoTitleBar).show();

            }
        });

    }

}
