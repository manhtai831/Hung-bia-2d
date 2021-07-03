package com.manhtai.hung_bia_2d;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Basket {
    int x = 0, y, width, height;
    int moveMethod = -1;

    Bitmap basket;

    public Basket(Resources res, int screenY, int screenX) {
        basket = BitmapFactory.decodeResource(res, R.drawable.basket);

        width = basket.getWidth();
        height = basket.getHeight();

        width /= 4;
        height /= 4;

        basket = Bitmap.createScaledBitmap(basket, width, height, false);
        y = screenY - width - 150;
        x = screenX / 2 - width / 2;
    }

    public Bitmap getBasket() {
        return basket;
    }

    Rect getCollisionShape() {
        return new Rect(x, y +height/2, x + width, y + height);
    }
}
