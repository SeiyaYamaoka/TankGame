package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;

public class EnemyBase {

    //ハード対応　画面調整用
    float scaleX,scaleY;
    //HP
    public int hp;
    final int MAXHP = 50;

    //敵の大きさ
    final float width = 64;
    final float height = 64;

    //描画するときの敵の大きさ
    float ew;
    float eh;

    //描画するときに元の大きさの何倍にするか
    final float mag = 1.5f;

    //敵座標
    public float ex = 0,ey =0;
    //敵ビットマップ
    Bitmap bmp;
    //敵タイプ
    public int EneType;

    //敵のマトリックス
    Matrix matrix= new Matrix();
    //角度
    double degree;

    //旋回速度
    final double turnspeed = 3d;

    //距離
    double distance;

    Paint testp = new Paint();
    //HPbar
    Paint hpp = new Paint();

    //敵の射程
    private int Range = 600;

    //時間計測
    Time time = new Time();

    //弾打つ間隔 ms
    final int msbdistance = 1000;

    private SoundPool soundPool = null;
    private int firingId;

    //自機の半分の大きさ　Vertical　縦　side 横
    public float harfSide = 48f;
    public float harfVertical = 48f;

    //自機の当たり判定の大きさ
    public float HitSide = 38f;
    public float HitVertical = 38f;

    // 自機の縦横大きさ
    public float Side = 96f;
    public float Vertical = 96f;

    //HPバー調整　自機との距離　上との間  tankharfSide + 10f;
    public float yHPAdjust = 58f;

    //HPばーの縦大きさ  yHPAdjust + 4f;
    public float yHPsize = 62f;

    //爆発の大きさ
    public float ExproSide = 96f;
    public float ExproVertical = 96f;

    //敵の獲得スコア
    public final int EarnedScore = 100;

    //敵との接触時ダメージ
    public final int ContactDamage = 1;

    //弾が当たったときのダメージ
    public final int BulletDamage = 10;


    private void log(String text){
        Log.d("**EnemyBaseのログ**", text);
    }

    public void Init(int enemytype , Bitmap b , float x , float y , float deg,SoundPool sound ,int firsoundid,float _scalex,float _scaley){

        hp = MAXHP;

        EneType = enemytype;
        bmp = b;

        degree = deg;

//        ew = width * mag * _scalex;
//        eh = height * mag * _scaley;

        scaleX = _scalex;
        scaleY = _scaley;

        ex = x ;
        ey = y ;

        Side *= scaleX;
        Vertical *= scaleY;

        harfSide *= scaleX;
        harfVertical *= scaleY;

        HitSide *= scaleX;
        HitVertical *= scaleY;

        yHPAdjust *= scaleY;
        yHPsize *= scaleY;


        Range *= ((scaleX + scaleY) / 2);

        ExproSide *= scaleX;
        ExproVertical *= scaleY;

        testp.setColor(Color.BLUE);
        testp.setAlpha(20);

        matrix.postScale(mag * scaleX,mag * scaleY);
        matrix.postTranslate(-harfSide,-harfVertical);

        matrix.postRotate((float)degree + 180);
        matrix.postTranslate((int)ex,(int)ey);

        soundPool = sound;
        firingId = firsoundid;


    }
    public void Update(boolean screenxflg, boolean screenyflg,MainCharacter mc, ArrayList eneble){
        matrix.reset();
        matrix.postScale(mag * scaleX,mag * scaleY);
        matrix.postTranslate(-harfSide,-harfVertical);

        //tankとの距離測定
        distance = Math.sqrt((mc.tankx - ex) * (mc.tankx - ex) + (mc.tanky - ey) * (mc.tanky - ey));
        //log("distance"+distance);


        //敵の射程に入ったとき
        if(distance < Range){

            //ラジアン角
            double rad = -Math.atan2(mc.tankx - ex,mc.tanky-ey);
            //double rad = Math.atan2(ex-tankx,ey-tanky);
            //目標、タンク座標
            double deg = rad * 180d / Math.PI;
            //少しずつ足していく　実際の座標　-　目標座標がturnspeed以上なら入る
            //旋回する。
            if(Math.abs(degree-deg) > turnspeed) {

                float cix = ex +(float)(Math.cos((degree+90)/180 * Math.PI));
                float ciy = ey +(float)(Math.sin((degree+90)/180 * Math.PI));

                if(Cross(ex,ey,cix,ciy,mc.tankx,mc.tanky) > 0){
                    degree += turnspeed;
                }else{
                    degree -= turnspeed;
                }
            }

            //1秒ごとに弾を撃つ
            if(time.Use()){
                if(time.Getms() > msbdistance){
                    EnemyBullet eb = new EnemyBullet();
                    eb.Init(ex,ey,(float)degree,48,8f,8f,scaleX,scaleY);
                    eneble.add(eb);
                    //発射音
                    soundPool.play(firingId, 1.0f, 1.0f, 0, 0, 1.0f);

                    time.End();
                }

            }else{
                time.Start();
            }

        }else{

        }
        matrix.postRotate((float)degree + 180);
        matrix.postTranslate((int)ex,(int)ey);


        //スクリーンずらしてるの対応する。
        if(screenxflg) {
            ex -= mc.movex;
        }
        if(screenyflg) {
            ey -= mc.movey;
        }

    }
    public void Draw(Canvas canvas){

        canvas.drawBitmap(bmp,matrix,null);
        //canvas.drawCircle(ex,ey,Range, testp);
        //HPバー
        hpp.setColor(Color.RED);
        canvas.drawRect(ex - harfSide,ey - yHPAdjust,ex + harfSide,ey - yHPsize ,hpp);

        if(hp > 0) {
            hpp.setColor(Color.GREEN);
            canvas.drawRect(ex - harfSide, ey - yHPAdjust, ex - harfSide + (Side * ((float)hp / (float)MAXHP)), ey - yHPsize, hpp);
        }
//        testp.setColor(Color.RED);
//        testp.setAlpha(255);
//        testp.setTextSize(20);
//        //canvas.drawText("time =" + time.Getms() + "/ms",0,80,testp);
//        testp.setColor(Color.BLUE);
//        testp.setAlpha(20);

    }

    //始点1  終点②　　点p
    private float Cross(float x1,float y1,float x2,float y2 ,float px,float py){

        float cro = (x2 - x1) * (py - y1) - (px - x1) * (y2 - y1);

        return cro;
    }

}
