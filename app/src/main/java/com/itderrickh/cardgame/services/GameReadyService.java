package com.itderrickh.cardgame.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.android.volley.VolleyError;
import com.itderrickh.cardgame.helpers.VolleyCallback;

import org.json.JSONObject;

public class GameReadyService extends Service {

    GameReadyThread updater;
    BroadcastReceiver broadcaster;
    Intent intent;
    public static final String BROADCAST_ACTION = "com.itderrickh.broadcast";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = getSharedPreferences("CARDGAME_SETTINGS", Context.MODE_PRIVATE);
        String token = preferences.getString("Auth_Token", "");

        updater = new GameReadyThread(token);

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {

        if (!updater.isRunning()) {
            updater.token = intent.getStringExtra("token");
            updater.start();
            updater.isRunning = true;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();

        if (updater.isRunning) {
            updater.interrupt();
            updater.isRunning = false;
            updater = null;
        }
    }

    public void sendResult() {
        intent.putExtra("data", true);
        sendBroadcast(intent);
    }

    public class GameReadyThread extends Thread {
        public boolean isRunning = false;
        public static final String DATA_URL = "http://webdev.cs.uwosh.edu/students/heined50/CardsBackend/initializeGame.php";
        public long DELAY = 4000;
        public String token;

        public GameReadyThread(String token) {
            super();

            this.token = token;
        }

        @Override
        public void run() {
            super.run();

            isRunning = true;
            while (isRunning) {
                //Do work to get data here
                GameService.getInstance().initializeGame(getApplicationContext(), token, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            boolean success = result.getBoolean("success");
                            if(success) {
                                sendResult();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError exception) {
                        exception.printStackTrace();
                    }
                });

                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        }

        public boolean isRunning() {
            return this.isRunning;
        }
    }
} // outer class end