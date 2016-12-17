package com.itderrickh.cardgame.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.itderrickh.cardgame.R;
import com.itderrickh.cardgame.helpers.VolleyCallback;
import com.itderrickh.cardgame.services.GameService;

import org.json.JSONObject;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        SharedPreferences preferences = getSharedPreferences("CARDGAME_SETTINGS", Context.MODE_PRIVATE);
        final String token = preferences.getString("Auth_Token", "");

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
                GameService.getInstance().endGame(getApplicationContext(), token, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        logoutExit();
                    }

                    @Override
                    public void onError(VolleyError string) {
                        logoutExit();
                    }
                });
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
