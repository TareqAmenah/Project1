package com.amenah.tareq.project1;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.amenah.tareq.project1.FishGame.FlayingFishView;

import java.util.Timer;
import java.util.TimerTask;

public class FishGameActivity extends AppCompatActivity {

    private final static long Interval = 30;
    private FlayingFishView gameview;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_game);
        gameview = new FlayingFishView(this);
        setContentView(gameview);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameview.invalidate();
                    }
                });
            }
        }, 0, Interval);
    }
}
