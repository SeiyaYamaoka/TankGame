package com.example.tankdedone.Scene.Title;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.example.tankdedone.Fade;
import com.example.tankdedone.Scene.SceneBase;

import java.io.IOException;
import java.io.InputStream;

public class TitleScene extends SceneBase {


    private Fade fade =new Fade();

    //ハード対応　画面調整用
    Matrix matrix= new Matrix();

    float scaleX,scaleY;
    //------------画像---------ビットマップ---------------------------
    //タイトル画面
    Bitmap titlescreenbmp;

    //---------------------------------------------------------

    //MediaPlayer
    private MediaPlayer bgm = null;

    //SE
    private SoundPool.Builder builder = null;
    private SoundPool soundPool = null;
    private int decideseId;

    float scale;

    boolean upflg = false;

    boolean isfadeend = false;


    private void log(String text){
        Log.d("**TitleSceneのログ**", text);
    }

    @Override
    public boolean Init(){
        LoadImage();
        LoadMusic();
        Loadse();



        log(""+HARD_VIEW.x + " " +HARD_VIEW.y);
        fade.SetHard(HARD_VIEW);
        fade.Start(fade.IN);
        upflg = false;
        touchoperation.menupflg = false;
        isfadeend = false;

        bgm.start();

        return true;
    }
    @Override
    public boolean Update(){

        if(isfadeend){
            //フェードアウト抜けるときの
            fade.Update();
            if (fade.isend) {

                End();
            }
        }else {
            if (fade.isend) {
                //タイトルシーンタッチするとフェードアウト開始

                if (touchoperation.menupflg) {
                    if (upflg == false) {
                        upflg = true;
                        isfadeend = true;
                        fade.Start(fade.OUT);
                        soundPool.play(decideseId, 1.0f, 1.0f, 0, 0, 1.0f);
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

        return true;
    }
    @Override
    public boolean Draw(Canvas canvas){

        canvas.drawBitmap(titlescreenbmp,matrix,null);
        fade.Draw(canvas);
        return true;
    }
    @Override
    public boolean End(){
        bgm.pause();
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
            inputStream = assetManager.open("title.png");
            titlescreenbmp = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        //setFocusable(true);
        return true;
    }

    private boolean LoadMusic(){
        bgm = new MediaPlayer();
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor descripter = assetManager.openFd("TitleBGM.wav");
            bgm.setDataSource(descripter.getFileDescriptor(), descripter.getStartOffset(), descripter.getLength());
            bgm.prepare();
            bgm.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean Loadse(){
        AssetManager assetManager = context.getAssets();

        //最低SDK指定が5.0未満の場合のSoundPoolインスタンス作成例
        //現在は非推奨だが低いバージョン端末を網羅する場合は使用する
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        try{
            AssetFileDescriptor descripter = assetManager.openFd("se_maoudamashii_system49.ogg");
            decideseId = soundPool.load(descripter, 1);
        }catch(IOException e){
            e.printStackTrace();
        }
        //setFocusable(true);
        return true;
    }

    @Override
    public void onPause() {
        bgm.pause();
    }

    @Override
    public void onResume() {
        bgm.start();
    }

    public void Set(float _scaleX,float _scaleY){
        scaleX = _scaleX;
        scaleY = _scaleY;
        matrix.setScale(_scaleX,_scaleY);
    }

}
