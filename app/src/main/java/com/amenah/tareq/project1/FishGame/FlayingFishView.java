package com.amenah.tareq.project1.FishGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.amenah.tareq.project1.R;


public class FlayingFishView extends View {


    private Bitmap fish[] = new Bitmap[3];
    private int fishx = 10;
    private int fishy;
    private int fishSpeed;
    private int score = 0, lifecounter = 3;
    private boolean touch = false;


    private int canavesWidth, canavasHight;
    private int yallowX, yallowY, yallowSpeed = 10;
    private Paint yallowPaint = new Paint();
    private int greenX, greenY, greenSpeed = 15;
    private Paint greenPaint = new Paint();

    private Bitmap backGroundImage;
    private Paint ScorePaint = new Paint();
    private Bitmap life[] = new Bitmap[3];


    public FlayingFishView(Context context) {
        super(context);

        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.playfish);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.playfishtwo);
        fish[2] = BitmapFactory.decodeResource(getResources(), R.drawable.small);


        backGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.backgroungiewmage);
        yallowPaint.setColor(Color.YELLOW);
        yallowPaint.setAntiAlias(false);
        greenPaint.setColor(Color.RED);
        greenPaint.setAntiAlias(false);


        ScorePaint.setColor(Color.WHITE);
        ScorePaint.setTextSize(70);
        ScorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        ScorePaint.setAntiAlias(true);

        fishy = 550;
        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.deadheart);

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canavesWidth = canvas.getWidth();
        canavasHight = canvas.getHeight();


        canvas.drawBitmap(backGroundImage, 0, 0, null);

        int minFishY = 120;
        int maxFishY = 1700;

        fishy = fishy + fishSpeed;

        if (fishy < minFishY) {
            fishy = minFishY;
        }

        if (fishy > maxFishY) {
            fishy = maxFishY;
        }

        fishSpeed += 1.5;
        if (touch) {
            canvas.drawBitmap(fish[1], fishx, fishy, null);
            touch = false;
        } else {
            canvas.drawBitmap(fish[0], fishx, fishy, null);
        }

        yallowX = yallowX - yallowSpeed;
        greenX = greenX - greenSpeed;


        if (yallowX < 0) {
            yallowX = canavesWidth + 21;
            yallowY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }


        if (greenX < 0) {
            greenX = canavesWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }


        canvas.drawCircle(yallowX, yallowY, 20, yallowPaint);
        //  canvas.drawBitmap(fish[2],fishx,fishy,null);

        canvas.drawCircle(greenX, greenY, 25, greenPaint);


        if (hitBallCheker(yallowX, yallowY)) {
            score += 10;
            yallowX = -150;
        }
        if (hitBallCheker(greenX, greenY)) {

            greenX = -150;
            lifecounter--;
            if (lifecounter == 0) {
                Toast.makeText(getContext(), "GAME OVER!", Toast.LENGTH_SHORT).show();

                return;
            }
        }

        for (int i = 0; i < 3; i++) {
            int x = (int) (580 + life[0].getWidth() * 1.3 * i);
            int y = 30;
            if (i < lifecounter) {
                canvas.drawBitmap(life[0], x, y, null);
            } else {

                canvas.drawBitmap(life[1], x, y, null);

            }
        }


        canvas.drawText("score : " + score, 30, 90, ScorePaint);


    }

    public boolean hitBallCheker(int x, int y) {

        if (fishx < x && x < (fishx + fish[0].getWidth()) && fishy < y && y < (fishy + fish[0].getHeight())) {
            return true;
        }
        return false;


    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;
            fishSpeed = -25;
        }
        return true;
    }
}
