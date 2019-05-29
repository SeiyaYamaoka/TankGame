package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Bullet {
    public float x = 0,y =0;
    private float speed = 15f;

    Matrix matrix= new Matrix();
    float degree=0;
    public void Init(float tankx,float tanky,float tankdeg){
        degree = tankdeg;
        x = tankx + (float)(Math.cos((degree - 90f)/180 * Math.PI)*48);
        y = tanky + (float)(Math.sin((degree - 90f)/180 * Math.PI)*48);

    }
    public void Update(boolean screenxflg,boolean screenyflg,double movex,double movey){
        matrix.reset();
        matrix.postScale(1.5f,1.5f);
        matrix.postTranslate(-12,-12);
        matrix.postRotate(degree);
        matrix.postTranslate((int)x,(int)y);

        x += Math.cos((degree - 90f)/180 * Math.PI) * speed;
        y += Math.sin((degree - 90f)/180 * Math.PI) * speed;

        if(screenxflg) {
            x -= movex;
        }
        if(screenyflg) {
            y -= movey;
        }


    }
    public void Draw(Canvas canvas, Bitmap bmp){
        canvas.drawBitmap(bmp,matrix,null);

    }
}
