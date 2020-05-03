package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class SmokeAni extends Animation {
    Rect src = new Rect();
    Rect dst = new Rect();

    //何回回れば終了かカウントする。
    int frame;
    int Maxframe;

    Matrix matrix = new Matrix();
    int randomVal;
    Paint al;

    float scaleX,scaleY;

    int plusalpa =0;
    int alpha = 0;

    @Override
    public void Init(float nx,float ny ,float sw,float sh){
        Typeani =2;
        nowCnt = 0;
        MaxCntani = 10;

        plusalpa = 255 / MaxCntani / 2;
        alpha = 0;

        frame = 0;
        Maxframe = 1;
        sizex = sw;
        sizey = sh;
        x = nx;
        y = ny;
        Endflg = false;

        //src.set(0,0,128,128);
        Random random = new Random();
        randomVal = random.nextInt(361);

        al = new Paint();
//        al.setAlpha(255);
//
//        matrix.reset();
//        matrix.postScale(0.5f,0.5f);
//        matrix.postRotate(randomVal);
//        matrix.postTranslate((int)x,(int)y);

    }
    @Override
    public void Update(boolean screenxflg,boolean screenyflg,double movex,double movey){

        if(frame>Maxframe) {
            nowCnt++;
            frame=0;
        }
        frame++;
        if(nowCnt > MaxCntani){
            Endflg = true;
        }

//        if(frame > Maxframe){
//            Endflg = true;
//        }
        if(screenxflg) {
            x -= movex;
        }
        if(screenyflg) {
            y -= movey;
        }

        //dst.set((int)x-sizex,(int)y-sizey,(sizex*2)+(int)x-sizex,(sizey*2)+(int)y-sizey);


        matrix.reset();

        matrix.postRotate(randomVal);
//        if(nowCnt > 2) {
//            al.setAlpha(100 - nowCnt * 12);
//            matrix.postScale(0.1f + 4 * 0.08f, 0.1f + 4 * 0.08f);
//        }else {
//            al.setAlpha(50 + nowCnt * 10);
//            matrix.postScale(0.1f + nowCnt * 0.08f, 0.1f + nowCnt * 0.08f);
//        }
        //255 0 ~ MaxCntani(5)
        //255/maxcntani * nowcnt

        if(nowCnt < MaxCntani / 2){
            alpha += plusalpa;
            if(alpha > 255)alpha = 255;

        }else{
            alpha -= plusalpa;
            if(alpha < 0)alpha = 0;

        }
        al.setAlpha(alpha);
        matrix.postScale(0.1f + nowCnt * 0.03f, 0.1f + nowCnt * 0.03f);

        matrix.postScale(scaleX,scaleY);
        matrix.postTranslate((int)x,(int)y);


//        frame++;

    }
    @Override
    public void Draw(Canvas canvas, Bitmap bmp){

        //canvas.drawBitmap(bmp,src,dst,null);
        canvas.drawBitmap(bmp,matrix,al);
    }


    public void Setscale(float _scaleX,float _scaleY){
        scaleX = _scaleX;
        scaleY = _scaleY;
    }

}
