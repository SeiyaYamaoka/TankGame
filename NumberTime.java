package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class NumberTime extends NumberBase {

    public long time = 0;

    public long limit = 120;

    public long nows = 120;

    private float scaleX,scaleY;
    @Override
    public void Init(Bitmap b  ,float _scalex,float _scaley){
        bmp = b;
        nows = limit;

        scaleX = _scalex;
        scaleY = _scaley;
    }
    @Override
    public void Update(){
        nows = limit - (time / 1000);
    }
    @Override
    public void Draw(Canvas canvas){

        long s = nows;

        if(s <= 0) s = 0;

        int len = String.valueOf(s).length();  //対象数値の桁数を求める
        //log("len :: "+len);

        for(int i = 0;i<len;i++){
            Rect src = new Rect();
            Rect dst = new Rect();

//            int d = (int)Math.pow(10, i);
//            //log("d :: "+d);
//            int cnt = goalscore / d;

            int cnt = (int)s % 10;

            //元画像の場所切り取る
            src.set(29 * cnt,0,29 * (cnt + 1),50);
            //実際の場所指定。
            dst.set(655 - (i * 29),35,684 - (i * 29),85);//35 85
            dst.left *= scaleX;
            dst.top *= scaleY;
            dst.right *= scaleX;
            dst.bottom *= scaleY;

            canvas.drawBitmap(bmp,src,dst,null);

            s /= 10;
        }
    }
}
