package com.example.tankdedone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class Fade {
    Paint blackp = new Paint();
    public boolean isend = true;
    public boolean isnow = false;

    private void log(String text){
        Log.d("**Fadeのログ**", text);
    }

    float scaleX,scaleY;

//    private float VIEW_WIDTH =1880;
//    private float VIEW_HEIGHT = 1020;

    private Point Hard_VIEW = new Point();
    private float HARD_WIDTH ;
    private float HARD_HEIGHT;

    //フェードタイプ　
    //何もしてない状態
    public final int NOT = 0;
    //フェードイン　画面が現れる、アルファ値を下げる
    public final int IN = 1;
    //フェードアウト　画面が消えていく　アルファ値を上げる
    public final int OUT = 2;

    int type = NOT;   //

    public int alp =0;

    int alspeed = 10;

    public void Start(int fadet){
        type = fadet;
        isend = false;
        isnow = true;
        switch(type){
            case NOT:
                break;
            case IN:
                alp = 255;
                break;
            case OUT:
                alp = 0;
                break;
        }

        blackp.setColor(Color.BLACK);
//        blackp.setAlpha((int) alp);
        //blackp.setTextSize(50);
    }
    public void Update(){
        switch(type){
            case NOT:
                break;
            case IN:
                alp -= alspeed;
                //log("fade"+alp);
                if(alp <= 0){
                    //alp = 0;
                    //type = NOT;

                    End();
                }
                break;
            case OUT:
                alp +=  alspeed;

                if(alp >= 255){
                    //alp = 255;
                    //type = NOT;
                    End();
                }
                break;
        }
        //alp++;
        //blackp.setAlpha(alp);

    }
    public void End(){
        switch (type){
            case IN:
                alp = 0;
                break;
            case OUT:
                alp = 255;
                break;
            case NOT:
                break;

        }
        type = NOT;
        blackp.setAlpha(alp);
        isnow = false;
        isend = true;
    }
    public void Draw(Canvas canvas){
        //if(type != NOT) {

//        blackp.setColor(Color.BLACK);
//        blackp.setAlpha(alp);
        blackp.setAlpha(alp);
        //blackp.setTextSize(50);

        //canvas.drawRect(0, 0, 1280, 720, blackp);
        //log(Hard_VIEW.x +" " + Hard_VIEW.y);
        if(!isend) {
            canvas.drawRect(0, 0, Hard_VIEW.x,Hard_VIEW.y, blackp);

        }
    }

    public void SetHard(Point hard/*float w , float h*/){
//        scaleX = _scaleX;
//        scaleY = _scaleY;
        //
         Hard_VIEW = hard;
//        HARD_WIDTH = w;
//        HARD_HEIGHT = h;
    }

}
