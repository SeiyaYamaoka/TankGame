package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Bullet {
    //弾の座標
    public float x = 0,y =0;

    //弾のスピード
    private float speedx = 15f;
    private float speedy = 15f;

    Matrix matrix= new Matrix();

    //弾の角度
    private float degree=0;

    //元の画像大きさ
    private float OriginalSide = 16f;
    private float OriginalVertical = 16f;

    //表示サイズ　（元の画像の  magx y 倍）
    public float DisplaySide = 24f;
    public float DisplayVertical = 24f;

    //表示サイズの半分
    private float DisplayharfSide = 12f;
    private float DisplayharfVertical = 12f;

    //あたり判定の大きさ
    public float HitSide = 8f;
    public float HitVertical = 8f;

    //爆発のサイズ
    public float ExplosionSide = 64f;
    public float ExplosionVertical = 64f;

    //弾の大きさ倍率　
    private float magx = 1.5f,magy = 1.5f;

    private float scaleX,scaleY;



    public void Init(MainCharacter mc ,float _scalex ,float _scaley){
        degree = mc.tanktopdegree;
        x = mc.tankx + (float)(Math.cos((degree - 90f)/180 * Math.PI) * mc.tankharfSide);
        y = mc.tanky + (float)(Math.sin((degree - 90f)/180 * Math.PI) * mc.tankharfVertical);

        scaleX = _scalex;
        scaleY = _scaley;

        speedx *= scaleX;
        speedy *= scaleY;

        magx *= scaleX;
        magy *= scaleY;

        DisplaySide *= scaleX;
        DisplayVertical *= scaleY;

        DisplayharfSide *= scaleX;
        DisplayharfVertical *= scaleY;

        HitSide *= scaleX;
        HitVertical *= scaleY;

        ExplosionSide *= scaleX;
        ExplosionVertical *= scaleY;

    }
    public void Update(boolean screenxflg,boolean screenyflg,double movex,double movey){
        matrix.reset();
        matrix.postScale(magx,magy);
        matrix.postTranslate(-DisplayharfSide,-DisplayharfVertical);
        matrix.postRotate(degree);
        matrix.postTranslate((int)x,(int)y);

        x += Math.cos((degree - 90f)/180 * Math.PI) * speedx;
        y += Math.sin((degree - 90f)/180 * Math.PI) * speedy;

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
