package com.itderrickh.cardgame.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;

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
        updater = new GameReadyThread();

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

    public class GameReadyThread extends Thread {
        public boolean isRunning = false;
        public static final String DATA_URL = "http://webdev.cs.uwosh.edu/students/heined50/CardsBackend/initializeGame.php";
        public long DELAY = 4000;
        public String token;

        @Override
        public void run() {
            super.run();

            isRunning = true;
            while (isRunning) {
                //Do work to get data here
                //TODO STUFF HERE
                GameService.getInstance().initializeGame();

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