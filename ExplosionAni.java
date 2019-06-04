package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.Random;

public class ExplosionAni extends Animation {

    Rect src = new Rect();
    Rect dst = new Rect();

    Matrix matrix = new Matrix();
    Bitmap newbmp;
    int randomValue;

    @Override
    public void Init(float nx,float ny){
        nowCnt = -1;
        MaxCntani = 10;
        sizex = 64;
        sizey = 64;
        x = nx;
        y = ny;
        Endflg = false;
        Random random = new Random();
        randomValue = random.nextInt(361);
        matrix.reset();
        //matrix.postScale(1.5f,1.5f);
        //matrix.postTranslate(-12,-12);
        matrix.postRotate(randomValue);
        //matrix.postTranslate((int)x,(int)y);
    }
    @Override
    public void Update(boolean screenxflg,boolean screenyflg,double movex,double movey){
        nowCnt++;
        if(nowCnt > MaxCntani){
            Endflg = true;
        }
        if(screenxflg) {
            x -= movex;
        }
        if(screenyflg) {
            y -= movey;
        }
        src.set(64*nowCnt,0,64*(nowCnt + 1),64);
        dst.set((int)x-sizex,(int)y-sizey,(sizex*2)+(int)x-sizex,(sizey*2)+(int)y-sizey);


    }
    @Override
    public void Draw(Canvas canvas, Bitmap bmp){

        newbmp = Bitmap.createBitmap(bmp,0,0,576,64,matrix,false);
//        canvas.drawBitmap(newbmp,src,dst,null);
        canvas.drawBitmap(bmp,src,dst,null);
    }
}
