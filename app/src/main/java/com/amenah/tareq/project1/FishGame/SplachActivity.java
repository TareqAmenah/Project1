package com.amenah.tareq.project1.FishGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amenah.tareq.project1.MainActivity;
import com.amenah.tareq.project1.R;


public class SplachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fish_game_activity_splach);


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                    Intent mainIntent = new Intent(SplachActivity.this, MainActivity.class);
                    startActivity(mainIntent);

                }
            }
        };
        thread.start();
    }


    protected void onPuse() {
        super.onPause();
        finish();
    }
}
