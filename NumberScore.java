package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class NumberScore extends NumberBase{

    private int score = 0;
    public int goalscore = 0;
    private Bitmap bmp;//290  50

    private float scaleX,scaleY;


    private void log(String text){
        Log.d("**Numberscoreのログ**", text);
    }

    @Override
    public void Init(Bitmap numb ,float _scalex,float _scaley){
        bmp = numb;
        score = 0;
        goalscore = 0;

        scaleX = _scalex;
        scaleY = _scaley;
    }
    @Override
    public void Update(){
        if(score < goalscore){
            score +=1;
        }

    }

    @Override
    public void Draw(Canvas canvas){
        int len = String.valueOf(score).length();  //対象数値の桁数を求める
        //log("len :: "+len);
        int s = score;
        for(int i = 0;i<len;i++){
            Rect src = new Rect();
            Rect dst = new Rect();

//            int d = (int)Math.pow(10, i);
//            //log("d :: "+d);
//            int cnt = goalscore / d;

            int cnt = s % 10;

            //元画像の場所切り取る
            src.set(29 * cnt,0,29 * (cnt + 1),50);
            //実際の場所指定。
            dst.set(330 - (i * 29),0,359 - (i * 29),50);

            dst.left *= scaleX;
            dst.top *= scaleY;
            dst.right *= scaleX;
            dst.bottom *= scaleY;

            canvas.drawBitmap(bmp,src,dst,null);

            s /= 10;
        }

    }

}
