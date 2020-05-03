package com.example.tankdedone.Scene.ResultScene;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.tankdedone.Fade;
import com.example.tankdedone.NumberResult;
import com.example.tankdedone.Scene.SceneBase;

import java.io.IOException;
import java.io.InputStream;

public class ResultScene extends SceneBase {

    //
    Bitmap resultbmp;
    //
    Bitmap numberbmp;

    private Fade fade =new Fade();

    //ハード対応　画面調整用
    Matrix matrix= new Matrix();
    float scaleX,scaleY;

    //
    boolean upflg = false;

    boolean isfadeend = false;

    public float touchx,touchy;

    int shp = 0;
    int stime = 0;
    int sdefeet = 0;
    int stotal = 0;

    NumberResult hpresult = new NumberResult();
    NumberResult timeresult = new NumberResult();
    NumberResult defeetresult = new NumberResult();
    NumberResult totalresult = new NumberResult();

    @Override
    public boolean Init(){
//        shp = 0;
//        stime = 0;
//        sdefeet = 0;
//        stotal = 0;
        LoadImage();
        touchx = touchy = -100;
        //fade.Setscale(scaleX,scaleY);
        fade.SetHard(HARD_VIEW);
        fade.Start(fade.IN);
        upflg = false;
        touchoperation.menupflg = false;
        touchoperation.upx =-100;
        touchoperation.upy = -100;
        isfadeend = false;

        hpresult.Init(numberbmp,scaleX,scaleY);
        hpresult.setGoalscore(shp);
        hpresult.setxy(1000,225);

        timeresult.Init(numberbmp,scaleX,scaleY);
        timeresult.setGoalscore(stime);
        timeresult.setxy(1000,345);

        defeetresult.Init(numberbmp,scaleX,scaleY);
        defeetresult.setGoalscore(sdefeet);
        defeetresult.setxy(1000,465);

        totalresult.Init(numberbmp,scaleX,scaleY);
        totalresult.setGoalscore(stotal);
        totalresult.setxy(1000,585);


        return true;
    }
    @Override
    public boolean Update(){
        if(isfadeend){
            //フェードアウト抜けるときの
            fade.Update();
            if (fade.isend) {
                End();
                nextscene = TITLE;
            }
        }else {
            if (fade.isend) {





                if (touchoperation.menupflg) {

                    touchx = touchoperation.upx;
                    touchy = touchoperation.upy;

                    //log("x" + touchx + "::y" + touchy);
                    if(touchx >= 0 && touchy >= 0 && touchx < 1280 && touchy < 720) {//281 131
                        if (upflg == false) {
                            //フェードアウト開始
                            upflg = true;
                            isfadeend = true;
                            fade.Start(fade.OUT);
                        }
                    }
                } else {
                    upflg = false;
                }

            } else {

                //フェードイン　タッチされたらalpを０にしている
                fade.Update();

                if (touchoperation.menupflg) {
                    if (upflg == false) {
                        upflg = true;
                        fade.End();
                    }
                } else {
                    upflg = false;
                }
            }

        }

        hpresult.Update();
        timeresult.Update();
        defeetresult.Update();
        totalresult.Update();
        return true;
    }
    @Override
    public boolean Draw(Canvas canvas){
        canvas.drawBitmap(resultbmp,matrix,null);


        hpresult.Draw(canvas);
        timeresult.Draw(canvas);
        defeetresult.Draw(canvas);
        totalresult.Draw(canvas);

        //canvas.drawBitmap(numberbmp,0,0,null);

        fade.Draw(canvas);

        return true;
    }
    @Override
    public boolean End(){
        nextflg = true;

        return true;
    }
    private boolean LoadImage() {
        try {
            //画像読み込み
            AssetManager assetManager = null;
            InputStream inputStream = null;
            assetManager = context.getAssets();
            //assetManager = getResources().getAssets();
            inputStream = assetManager.open("result.png");
            resultbmp = BitmapFactory.decodeStream(inputStream);

            inputStream = assetManager.open("numberorenge.png");
            numberbmp = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        //setFocusable(true);
        return true;
    }
    public void setScore(int hp,long time,int defeet){
        shp = hp;
        stime = (int)time;
        sdefeet = defeet;
        stotal = shp * stime * sdefeet;
    }

    @Override
    public void onPause() {
        //stagebgm.pause();
    }

    @Override
    public void onResume() {
        //stagebgm.start();
    }

    public void Set(float _scaleX,float _scaleY){
        scaleX = _scaleX;
        scaleY = _scaleY;
        matrix.setScale(_scaleX,_scaleY);
    }


}
