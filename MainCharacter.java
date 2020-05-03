package com.example.tankdedone;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class MainCharacter {

    //ハード対応　画面調整用
    float scaleX,scaleY;

    //何倍表示か
    float scale = 1.5f;

    //自機の座標
    public float tankx = 650f,tanky = 370f;

    //自機
    Bitmap tankbasebmp, tanktopbmp;

    //マトリックス
    //自機下部分
    Matrix tankbasematrix;
    //自機上部分
    Matrix tanktopmatrix;

    //自機の体力
    public int hp = 100;
    public final int MAXHP = 100;

    //自機が移動する量
    public double movex,movey;

    //自機の下部分の回転度数（移動する方向へ向きを変える）
    public float tankbasedegree=0;
    //自機の上部分の回転度数　(弾を打つ方向へ向きを変える)
    public float tanktopdegree=0;

    //キャラのスピード
    public float speedx = 6.0f;
    public float speedy = 6.0f;

    //HPbarのペイント
    Paint hpp = new Paint();

    //自機の半分の大きさ　Vertical　縦　side 横
    public float tankharfSide = 48f;
    public float tankharfVertical = 48f;

    //自機の当たり判定の大きさ
    public float tankHitSide = 38f;
    public float tankHitVertical = 38f;

    // 自機の縦横大きさ
    public float tankSide = 96f;
    public float tankVertical = 96f;

    //HPバー調整　自機との距離　上との間  tankharfSide + 10f;
    public float yHPAdjust = 58f;

    //HPばーの縦大きさ  yHPAdjust + 4f;
    public float yHPsize = 62f;

    //煙の大きさ　
    public float SmokeSide = 64f;
    public float SmokeVertical = 64f;

    //接触時ダメージ
    public final int ContactDamage = 1;

    private boolean onceinitflg = false;


    public void Init(Bitmap topb,Bitmap baseb ,float inix,float iniy){
        //自機初期座標
        tankx = inix;
        tanky = iniy;

        //角度
        tankbasedegree =0f;
        tanktopdegree = 0f;

        //Bitmap
        tanktopbmp = topb;
        tankbasebmp = baseb;

        hp = MAXHP;

        //マトリクス
        tankbasematrix= new Matrix();
        tankbasedegree=0;
        tanktopmatrix= new Matrix();

        MatrixUpdate();

        onceInit();

    }
    private void onceInit(){
        if(onceinitflg == false){
            onceinitflg = true;

            tankSide *= scaleX;
            tankVertical *= scaleY;

            tankharfSide *= scaleX;
            tankharfVertical *= scaleY;

            tankHitSide *= scaleX;
            tankHitVertical *= scaleY;

            yHPAdjust *= scaleY;
            yHPsize *= scaleY;

            SmokeSide *= scaleX;
            SmokeVertical *= scaleY;

            speedx *= scaleX;
            speedy *= scaleY;
        }
    }

    public void InitUpdate(){
        tankbasematrix.reset();

        tanktopmatrix.reset();

        movex = 0f;
        movey = 0f;

        if (hp < 0) {
            hp = 0;
        }
    }

    public void Update(){

    }

    public void MatrixUpdate(){
        tankbasematrix.postScale(scale * scaleX, scale * scaleY);
        tankbasematrix.postTranslate(-tankharfSide , -tankharfVertical);
        tankbasematrix.postRotate(tankbasedegree);
        tankbasematrix.postTranslate((int) tankx, (int) tanky);

        tanktopmatrix.postScale(scale * scaleX, scale * scaleY);
        tanktopmatrix.postTranslate(-tankharfSide , -tankharfVertical);
        tanktopmatrix.postRotate(tanktopdegree);
        tanktopmatrix.postTranslate((int) tankx, (int) tanky);
    }

    public void DrawChara(Canvas canvas){
        canvas.drawBitmap(tankbasebmp,tankbasematrix, null);
        canvas.drawBitmap(tanktopbmp, tanktopmatrix, null);
    }

    public void DrawHPbar(Canvas canvas){
        //HPバー　赤色
        hpp.setColor(Color.RED);
        canvas.drawRect(tankx - tankharfSide,tanky - yHPAdjust,tankx + tankharfSide,tanky - yHPsize,hpp);

        //HP　緑色
        if(hp > 0) {
            hpp.setColor(Color.GREEN);
            canvas.drawRect(tankx - tankharfSide, tanky - yHPAdjust, tankx - tankharfSide + (tankSide * ((float)hp / (float)MAXHP)), tanky - yHPsize, hpp);
        }
    }


    public void Set(float _scaleX,float _scaleY){
        scaleX = _scaleX;
        scaleY = _scaleY;

    }
}
