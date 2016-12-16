package com.itderrickh.cardgame.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itderrickh.cardgame.R;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        boolean isWinner = getIntent().getBooleanExtra("isWinner", false);
        Button exit = (Button) findViewById(R.id.exit);
        TextView displayWinner = (TextView) findViewById(R.id.displayWinner);

        if(isWinner) {
            displayWinner.setText("You win, you will be automatically logged out in 30 seconds.");
        } else {
            displayWinner.setText("You lose, you will be automatically logged out in 30 seconds.");
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoutExit();
            }
        }, 30000);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutExit();
            }
        });
    }

    private void logoutExit() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
