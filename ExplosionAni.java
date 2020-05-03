package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ExplosionAni extends Animation {

    Rect src = new Rect();
    Rect dst = new Rect();

    //Matrix matrix = new Matrix();
    //Bitmap newbmp;
    //int randomValue;

    //アニメーション速度
    int frame;
    int Maxframe;

    @Override
    public void Init(float nx,float ny,float sw,float sh){
        Typeani = 1;
        nowCnt = -1;
        MaxCntani = 10;
        sizex = sw;
        sizey = sh;
        x = nx;
        y = ny;
        Endflg = false;

        frame = 0;
        Maxframe = 2;
//        Random random = new Random();
//        randomValue = random.nextInt(361);
       // matrix.reset();
        //matrix.postScale(1.5f,1.5f);
        //matrix.postTranslate(-12,-12);
        //matrix.postRotate(randomValue);
        //matrix.postTranslate((int)x,(int)y);
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
        if(screenxflg) {
            x -= movex;
        }
        if(screenyflg) {
            y -= movey;
        }
        src.set(64*nowCnt,0,64*(nowCnt + 1),64);
        dst.set((int)(x-sizex),(int)(y-sizey),(int)((sizex*2)+x-sizex),(int)((sizey*2)+y-sizey));


    }
    @Override
    public void Draw(Canvas canvas, Bitmap bmp){

        //newbmp = Bitmap.createBitmap(bmp,0,0,576,64,matrix,false);
//        canvas.drawBitmap(newbmp,src,dst,null);
        canvas.drawBitmap(bmp,src,dst,null);
    }
}
