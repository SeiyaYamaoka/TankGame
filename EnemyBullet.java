package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class EnemyBullet {
    public float x = 0,y =0;
    private float speedx = 1f;
    private float speedy = 1f;
    //private Bitmap bmp;

    //弾の大きさ
//    final float width = 16;
//    final float height = 16;


    //元の画像大きさ
    private float OriginalSide = 16f;
    private float OriginalVertical = 16f;

    //表示サイズ　（元の画像の  magx y 倍）
    public float DisplaySide = 24f;
    public float DisplayVertical = 24f;

    //表示サイズの半分 あたり判定にも使用
    public float DisplayharfSide = 12f;
    public float DisplayharfVertical = 12f;

    //あたり判定の大きさ
    public float HitSide = 8f;
    public float HitVertical = 8f;

    //爆発のサイズ
    public float ExplosionSide = 32f;
    public float ExplosionVertical = 32f;

    //描画するときに元の大きさの何倍にするか
    private float magx = 1.5f;
    private float magy = 1.5f;

    private Matrix matrix= new Matrix();
    private float degree=0;

    public final int Damage = 10;

    private float scaleX,scaleY;

    public void Init(float startx,float starty,float deg,int size,float spx,float spy ,float _scalex,float _scaley){
        degree = deg;
        x = startx + (float)(Math.cos((degree + 90f)/180 * Math.PI)*size);
        y = starty + (float)(Math.sin((degree + 90f)/180 * Math.PI)*size);

        scaleX = _scalex;
        scaleY = _scaley;

        speedx = spx * scaleX;
        speedy = spy * scaleY;
        //bmp = b;

//        ew = width * mag;
//        eh = height * mag;

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

        x += Math.cos((degree + 90f)/180 * Math.PI) * speedx;
        y += Math.sin((degree + 90f)/180 * Math.PI) * speedy;

        if(screenxflg) {
            x -= movex;
        }
        if(screenyflg) {
            y -= movey;
        }


    }
    public void Draw(Canvas canvas ,Bitmap bmp){
        canvas.drawBitmap(bmp,matrix,null);

    }
}
