package com.manhtai.hung_bia_2d;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Beer {
    int speed = 15;
    int x, y, width, height;

    Bitmap beer;

    Beer(Resources res) {
        beer = BitmapFactory.decodeResource(res, R.drawable.beer);

        width = beer.getWidth();
        height = beer.getHeight();

        width /= 15;
        height /= 15;

        beer = Bitmap.createScaledBitmap(beer, width, height, false);
        y = -1000;
    }

    public Bitmap getBeer() {
        return beer;
    }

    public Bitmap getBeerIcon() {
        beer = Bitmap.createScaledBitmap(beer, 40, 80, false);
        return beer;
    }

    Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
