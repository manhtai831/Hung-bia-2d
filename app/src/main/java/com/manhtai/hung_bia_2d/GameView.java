package com.manhtai.hung_bia_2d;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.manhtai.hung_bia_2d.common.StringConstant;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    Thread thread;
    GamePlayActivity mActivity;
    int screenX;
    int screenY;


    Beer beer;
    Basket basket;
    Background background;
    Paint paint;
    Random random;

    boolean isRun = true;

    int score = 0;
    int bonus = 1;
    Beer[] beers;

    int beerIsNot = 0;


    GameView(GamePlayActivity activity, int screenX, int screenY) {
        super(activity);
        this.mActivity = activity;
        this.screenX = screenX;
        this.screenY = screenY;

        beer = new Beer(getResources());
        basket = new Basket(getResources(), screenY, screenX);
        background = new Background(getResources(), screenX, screenY);

        beers = new Beer[6];
        for (int i = 0; i < 6; i++) {
            beers[i] = new Beer(getResources());
        }

        paint = new Paint();
        paint.setTextSize(75);
        paint.setColor(Color.WHITE);

        random = new Random();
    }

    @Override
    public void run() {

        while (isRun) {
            update();
            draw();
            sleep();
        }

    }

    private void update() {
        if (basket.y < screenY - screenY / 3f) {
            basket.y = (int) ((int) screenY - screenY / 3f);
        }
        background.x = 0;
        background.y = 0;
        for (Beer b : beers) {
            if (score < 20) {
                b.y += b.speed;
            } else if (score < 50) {
                bonus = 2;
                b.y += b.speed + 10;

            } else if (score < 100) {
                bonus = 2;
                b.y += b.speed + 15;

            } else {
                bonus = 2;
                b.y += b.speed + 25;
            }

            if (b.y + b.height + 10 < 0) {
                b.x = random.nextInt(screenX - b.width);
                b.y = -random.nextInt(screenY)- b.width - 100;
            }
            if (b.y > screenY) {
                b.y = -2 * b.height;
                beerIsNot++;
            }


            if (Rect.intersects(basket.getCollisionShape(), b.getCollisionShape())) {
                score += bonus;
                b.y = -2 * b.height;
            }


        }

        if (beerIsNot >= 3) {

                isRun = false;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle(StringConstant.title);
                        builder.setMessage(StringConstant.statusMessage);
                        builder.setPositiveButton(StringConstant.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mActivity, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
                                isRun = true;
                                beerIsNot = 0;
                                score = 0;
//                                thread.start();
                            }
                        });
                        builder.setNegativeButton(StringConstant.cancel, null);
                        builder.show();
                    }
                });


        }

    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, background.x, background.y, paint);

            for (Beer beer : beers)
                canvas.drawBitmap(beer.getBeer(), beer.x, beer.y, paint);

            canvas.drawBitmap(basket.getBasket(), basket.x, basket.y, paint);

            canvas.drawText(String.valueOf(score), screenX / 2f, screenY / 5f, paint);

            for (int i = 0; i < 3; i++) {
                canvas.drawBitmap(beer.getBeerIcon(), screenX - i * 40 - 40, 0, paint);
            }



            Paint paint1 = new Paint();
            paint1.setColor(Color.RED);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setTextSize(40);
            paint1.setStrokeWidth(10);
            Path path = new Path();
            path.moveTo(100, 100);
            path.lineTo(50, 150);
            canvas.drawPath(path, paint1);
            for(int i = 0; i < beerIsNot; i++){
                canvas.drawText("X", screenX - i * 40 -30, 60, paint1);
            }
            Path path1 = new Path();
            path1.moveTo(46, 146);
            path1.lineTo(100, 200);
            canvas.drawPath(path1, paint1);
            paint1.setColor(Color.TRANSPARENT);
            canvas.drawRect(0, 50, 150, 250, paint1);


            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    private void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                basket.x = (int) event.getX() - basket.width / 2;
                basket.y = (int) event.getY() - basket.width;
                if (basket.y < screenY - screenY / 3f) {
                    basket.y = (int) ((int) screenY - screenY / 3f);
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                basket.moveMethod = -1;
                break;
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < 150 && event.getY() > 50 && event.getY() < 250) {
                    isRun = false;
                    mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                    mActivity.finish();
                }
                break;
        }
        return true;
    }

    public void resume() {
        thread = new Thread(this);
        thread.start();
        isRun = true;

    }

    public void pause() {
        try {
            isRun = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
