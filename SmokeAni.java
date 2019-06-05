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

    @Override
    public void Init(float nx,float ny){
        Typeani =2;
        nowCnt = 0;
        MaxCntani = 5;

        frame = 0;
        Maxframe = 3;
        sizex = 64;
        sizey = 64;
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
        if(nowCnt > 2) {
            al.setAlpha(100 - nowCnt * 12);
            matrix.postScale(0.1f + 4 * 0.08f, 0.1f + 4 * 0.08f);
        }else {
            al.setAlpha(50 + nowCnt * 10);
            matrix.postScale(0.1f + nowCnt * 0.08f, 0.1f + nowCnt * 0.08f);
        }
        matrix.postTranslate((int)x,(int)y);


//        frame++;

    }
    @Override
    public void Draw(Canvas canvas, Bitmap bmp){

        //canvas.drawBitmap(bmp,src,dst,null);
        canvas.drawBitmap(bmp,matrix,al);
    }


}
