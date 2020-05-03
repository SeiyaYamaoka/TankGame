package com.example.tankdedone.Scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

import com.example.tankdedone.TouchOperation;

public class SceneBase {

    //public int scenenumber;

    public boolean nextflg = false;
    public int nextscene;
    public final int TITLE = 0;
    public final int STAGE = 1;
    public final int GAME = 2;
    public final int RESULT = 3;

    //画面サイズ
    public final float VIEW_WIDTH = 1280;
    public final float VIEW_HEIGHT = 720;

    public Point HARD_VIEW;

    protected Context context;
    public void setContext(Context c){
        context = c;
    }
    public Context getContext(){return context;}

    protected TouchOperation touchoperation;

    public void setTouchope(TouchOperation t){
        touchoperation = t;
    }


    public boolean Init(){
        return true;
    }
    public boolean Update(){
        return true;
    }
    public boolean Draw(Canvas canvas){
        return true;
    }
    public boolean End(){
        return true;
    }

    public void onPause(){ }
    public void onResume(){}

}
