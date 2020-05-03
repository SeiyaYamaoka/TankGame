package com.example.tankdedone.Scene.StageScene;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.example.tankdedone.Fade;
import com.example.tankdedone.Scene.SceneBase;

import java.io.IOException;
import java.io.InputStream;

public class StageScene extends SceneBase {

    //Bitmap
    Bitmap stage1bmp;

    //MediaPlayer
    private MediaPlayer stagebgm = null;

    //SE
    private SoundPool.Builder builder = null;
    private SoundPool soundPool = null;
    private int decideseId;


    private Fade fade =new Fade();

    //ハード対応　画面調整用
    Matrix matrix= new Matrix();
    private float scaleX , scaleY;
    RectF decidebutton;

    //
    boolean upflg = false;

    boolean isfadeend = false;

    public float touchx,touchy;

    private void log(String text){
        Log.d("**StageSceneのログ**", text);
    }


    @Override
    public boolean Init(){

        LoadImage();

        LoadMusic();

        Loadse();

        touchx = touchy = -100;
        //fade.Setscale(scaleX,scaleY);
        fade.SetHard(HARD_VIEW);
        fade.Start(fade.IN);
        upflg = false;
        touchoperation.menupflg = false;
        touchoperation.upx =-100;
        touchoperation.upy = -100;
        isfadeend = false;

        //決定ボタンの位置
        decidebutton = new RectF(953 * scaleX,560 * scaleX,1234 * scaleY,691 * scaleY);



        stagebgm.start();
        return true;
    }
    @Override
    public boolean Update(){
        if(isfadeend){
            //フェードアウト抜けるときの
            fade.Update();
            if (fade.isend) {
                stagebgm.pause();
                End();
            }
        }else {
            if (fade.isend) {


                if (touchoperation.menupflg) {
//                    if(touchoperation.touchx1>0 && touchoperation.touchy1 >0) {
//                        touchx = touchoperation.touchx1;
//                        touchy = touchoperation.touchy1;
//                    }else{
//                        if(touchoperation.down1flg == true) {
//                            touchx = touchoperation.downx1;
//                            touchy = touchoperation.downy1;
//                        }
//                    }
                    touchx = touchoperation.upx;
                    touchy = touchoperation.upy;

                    //log("x" + touchx + "::y" + touchy);
                    if(touchx >= decidebutton.left && touchy >= decidebutton.top && touchx < decidebutton.right && touchy < decidebutton.bottom) {//281 131
                        if (upflg == false) {
                            soundPool.play(decideseId, 1.0f, 1.0f, 0, 0, 1.0f);
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

        return true;
    }
    @Override
    public boolean Draw(Canvas canvas){
        canvas.drawBitmap(stage1bmp,matrix,null);
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
            inputStream = assetManager.open("stage1-1.png");
            stage1bmp = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        //setFocusable(true);
        return true;
    }
    private boolean LoadMusic(){
        stagebgm = new MediaPlayer();
        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor descripter = assetManager.openFd("Stageselect.wav");
            stagebgm.setDataSource(descripter.getFileDescriptor(), descripter.getStartOffset(), descripter.getLength());
            stagebgm.prepare();
            stagebgm.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean Loadse(){
        AssetManager assetManager = context.getAssets();

		/*/最低SDK指定が5.0以上の場合のSoundPoolインスタンス作成例
		AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
		attrBuilder.setUsage(AudioAttributes.USAGE_GAME);
		attrBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
		AudioAttributes attr = attrBuilder.build();
		builder = new SoundPool.Builder();
		builder.setMaxStreams(20);
		builder.setAudioAttributes(attr);
		soundPool = builder.build();
		/*/

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

//    public void playSe() {
//        soundPool.play(decideseId, 1.0f, 1.0f, 0, 0, 1.0f);
//    }

    @Override
    public void onPause() {
        stagebgm.pause();
    }

    @Override
    public void onResume() {
        stagebgm.start();
    }



    public void Set(float _scaleX,float _scaleY){
        scaleX = _scaleX;
        scaleY = _scaleY;
        matrix.setScale(_scaleX,_scaleY);
    }

}

