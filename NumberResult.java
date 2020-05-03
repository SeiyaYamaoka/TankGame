package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class NumberResult extends NumberBase {
    private int score = 0;
    public int goalscore = 0;



    int x,y;

    private Bitmap bmp;//380  64
    private void log(String text){
        Log.d("**NumberResultのログ**", text);
    }



    @Override
    public void Init(Bitmap numb,float _scalex,float _scaley){
        scaleX = _scalex;
        scaleY = _scaley;
        bmp = numb;
        score = 0;
        goalscore = 0;
        x = 0;
        y = 0;
    }
    @Override
    public void Update(){
        if(score < goalscore){
            score +=1;
        }

    }

    @Override
    public void Draw(Canvas canvas){
        int len = String.valueOf(goalscore).length();  //対象数値の桁数を求める
        //log("len :: "+len);
        int s = goalscore;
        for(int i = 0;i<len;i++){
            Rect src = new Rect();
            Rect dst = new Rect();

            int cnt = s % 10;

            //元画像の場所切り取る
            src.set(38 * cnt,0,38 * (cnt + 1),64);
            //実際の場所指定。
            dst.set((int)((x - (i * 38)) * scaleX),(int)(y * scaleY),(int) ((x + 38 - (i * 38))*scaleX ),(int)((y + 64) * scaleY));

            canvas.drawBitmap(bmp,src,dst,null);

            s /= 10;
        }

    }


    public void setGoalscore(int sgoal){
        goalscore = sgoal;
    }
    public void setxy(int sx,int sy){
        x = sx;
        y = sy;

    }




}
