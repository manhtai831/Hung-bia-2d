package com.manhtai.hung_bia_2d;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0,y = 0;

    Bitmap background;
    Background(Resources res,int screenX,int screenY){
        background = BitmapFactory.decodeResource(res,R.drawable.background);
        background = Bitmap.createScaledBitmap(background,screenX,screenY,false);
    }
}
