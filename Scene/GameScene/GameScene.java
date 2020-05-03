package com.example.tankdedone.Scene.GameScene;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.example.tankdedone.Animation;
import com.example.tankdedone.Bullet;
import com.example.tankdedone.EnemyBase;
import com.example.tankdedone.EnemyBullet;
import com.example.tankdedone.ExplosionAni;
import com.example.tankdedone.Fade;
import com.example.tankdedone.MainCharacter;
import com.example.tankdedone.Map;
import com.example.tankdedone.NumberScore;
import com.example.tankdedone.NumberTime;
import com.example.tankdedone.Scene.SceneBase;
import com.example.tankdedone.SmokeAni;
import com.example.tankdedone.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GameScene extends SceneBase {

    //ハード対応　画面調整用
    Matrix matrix = new Matrix();
    public float scaleX,scaleY;

    //画面スクロール範囲
    private RectF ScreenScrollRange = new RectF(600,340,680,380);

    //自機
    public MainCharacter maincharacter = new MainCharacter();

    private int smokenum = 0;

    //アニメーションのリスト
    //自機の前に描画
    ArrayList<Animation> pretankanimations = new ArrayList<>();
    //自機の後に描画
    ArrayList<Animation> reartankanimations = new ArrayList<>();

    //弾のリスト
    ArrayList<Bullet> bulletlists = new ArrayList<Bullet>();


    //弾がスクリーン移動に影響するかのフラグ アニメーションにも使用
    boolean bscreenxflg,bscreenyflg;

    public String mapnum = "01";

    //マップ表示用クラス
    Map map = new Map();

    //デバック用ペイント
    Paint p;

    //メニューフラグ
    public static final int GAMEON = 0;
    public static final int MENUON = 1;
    public static final int GAMEOVER = 2;

    //ゲームオーバとか遷移するときに使用するフラグ
    public int gamesceneflg = GAMEON;

    //敵のリスト
    private ArrayList<EnemyBase> enemylist = new ArrayList<>();

    //敵の玉リスト
    private ArrayList<EnemyBullet> eneblelist = new ArrayList<>();

    //時間
    public Time time = new Time();

    //
    NumberScore numberscore = new NumberScore();
    //
    //int score = 0;

    NumberTime numbertime = new NumberTime();

    private boolean onceinitflg = false;

    private Fade fade =new Fade();
    boolean isfadeinend = false;

    public Time starttime = new Time();
    public boolean startflg = false;
    public float startx,starty;
    private float startstopy ;
    private float startdownyspeed = 15f;
    private int startstoptime = 1500;
    private Matrix startmatrix = new Matrix();


    //クリア　
    public boolean clearflg = false;

    public float clearx,cleary;
    public Time cleartime = new Time();
    private float clearstopy;
    private int clearstoptime = 3000;
    private float cleardownyspeed = 15f;
    private Matrix clearmatrix = new Matrix();

    public long ncleartime = 0;
    public int nclearscore = 0;
    public int nclearhp = 0;

    //マトリックス　スコア　タイム　メニュー　
    private Matrix scorematrix = new Matrix();
    private Matrix timematrix  = new Matrix();
    private Matrix menicomatrix = new Matrix();


    //フェードフラグ
    boolean isfadeoutend = false;
    boolean isfadeoutstart =false;

    private boolean lastendex = false;

    //------------画像---------ビットマップ---------------------------
    //クラスにビットマップ入れてInitで入れたほうがいいのかわからない
    //自機
    Bitmap tankbasebmp, tanktopbmp;
    //弾
    Bitmap tamabmp;
    //マップチップ
    Bitmap blockbmp;
    //爆発
    Bitmap explosionbmp;
    //煙
    Bitmap smokebmp;
    //敵　固定砲台
    Bitmap enemy01bmp;
    //敵　弾
    Bitmap etamabmp;
    //メニューアイコン
    Bitmap menuiconbmp;
    //メニュー画像
    Bitmap menubmp;
    //ゲームオーバー画面
    Bitmap gameoverbmp;
    //スコア
    Bitmap scorebmp;
    //ボタンベース
    Bitmap buttonbasebmp;
    //発射ボタン
    Bitmap buttonfilbmp;
    //移動ボタン
    Bitmap buttontransbmp;
    //白数字
    Bitmap numberwhitebmp;
    //タイム表示
    Bitmap timebmp;
    //スタート文字
    Bitmap startbmp;
    //クリア文字
    Bitmap clearbmp;


    //MediaPlayer
    private MediaPlayer bgm = null;

    //SE
    //private SoundPool.Builder builder = null;
    private SoundPool soundPool = null;
    //決定音
    private int decideseId;
    //発射音
    private int firingId;
    //爆発音
    private int expId;
    //敵の発射音
    private int enefirId;
    //スタート音
    private int startId;
    //クリア音
    //ゲームオーバー音
    //敵の爆発音
    //
    //
    //
    //


    private void log(String text){
        Log.d("**GAMESCENEのログ**", text);
    }

    public GameScene() {


    }

    @Override
    public boolean Init() {

        onceInit();

        map.Init(context,scaleX,scaleY,mapnum,HARD_VIEW.x,HARD_VIEW.y,VIEW_WIDTH,VIEW_HEIGHT);
        //ReadFile(context);

        p = new Paint();
        p.setTextSize(20);
        p.setColor(Color.BLACK);

        gamesceneflg = GAMEON;

        maincharacter.Set(scaleX,scaleY);
        maincharacter.Init(tanktopbmp,tankbasebmp,map.initankx,map.initanky);

        smokenum = 0;


//        map.screenx = f;
//        map.screeny = f;

        bulletlists.clear();
        pretankanimations.clear();
        reartankanimations.clear();

        touchoperation.Init(HARD_VIEW.x,HARD_VIEW.y,buttonbasebmp,buttontransbmp,buttonfilbmp);

        enemylist.clear();
        eneblelist.clear();

        ReadEnemyFile(context);


        numberscore.Init(numberwhitebmp,scaleX,scaleY);
        numbertime.Init(numberwhitebmp,scaleX,scaleY);

//        scorematrix.reset();
//        scorematrix.setScale(scaleX,scaleY);
//        scorematrix.setTranslate();

        timematrix.reset();
        timematrix.postScale(scaleX,scaleY);

        timematrix.postTranslate(HARD_VIEW.x / 2 - (scaleX * 75),0);
        //log("scalex " + scaleX + "  scaley "+ scaleY);
        menicomatrix.reset();
        menicomatrix.postScale(scaleX,scaleY);
        menicomatrix.postTranslate(HARD_VIEW.x - (scaleX * 100),0);




//        fade.Setscale(scaleX,scaleY);
        fade.SetHard(HARD_VIEW);
        fade.Start(fade.IN);
        isfadeinend = false;

        starttime.End();
        startflg = false; //380
        startx = (HARD_VIEW.x / 2) - (190 * scaleX);
        starty = (-160) * scaleY;

        startmatrix.reset();
        startmatrix.postScale(scaleX,scaleY);
        startmatrix.postTranslate(startx,starty);



        clearflg = false;//27 1,27 2, 28 1 ,28 2

        clearx = (HARD_VIEW.x / 2) - (190 * scaleX);
        cleary = -160 * scaleY;


        clearmatrix.reset();
        clearmatrix.postScale(scaleX,scaleY);
        clearmatrix.postTranslate(clearx,cleary);

        cleartime.End();
        isfadeoutend = false;
        isfadeoutstart =false;

        ncleartime = 0;
        nclearscore = 0;
        nclearhp = 0;

        time.End();
        //time.Start();

        lastendex = false;

        bgm.seekTo(0); //リセット（先頭から再生する）

        bgm.start();
        return true;
    }

    @Override
    public boolean Update() {

        if(lastendex == true) {
            //log("fade" + fade.alp );
            End();
            nextscene = RESULT;
        }
        //
        maincharacter.InitUpdate();


        //タップ操作クラスの変数セット
        touchoperation.SetSide();

        //自機の処理
        //スクロールさせるかフラグ初期化
        bscreenxflg = false;
        bscreenyflg = false;

//        //左のタップ操作
//        touchoperation.leftoperation(this);
//
//        //右のタップ操作 照準を合わせる。上の部分が回転する。
//        touchoperation.rightoperation(this);

        touchoperation.operation(this);

        maincharacter.MatrixUpdate();

        if(isfadeinend) {
            if(startflg) {

                if(clearflg) {


                    if(cleartime.Getms() >= clearstoptime) {
                        if(isfadeoutend){
                            lastendex = true;
                        }else {
                            if(isfadeoutstart == false) {
                                fade.Start(fade.OUT);
                                isfadeoutstart = true;

                            }

                            fade.Update();

                            if (fade.isend) {
                                isfadeoutend = true;
                            }

                        }

                    }


                    if (cleary >= clearstopy) {
                        cleary = clearstopy;

                    }else{
                        cleary += cleardownyspeed;
                    }
                    clearmatrix.reset();
                    clearmatrix.postScale(scaleX,scaleY);
                    clearmatrix.postTranslate(clearx,cleary);

                }

                if(maincharacter.tankx - maincharacter.tankharfSide < map.clearrect.right - map.screenx
                        && maincharacter.tankx + maincharacter.tankharfSide >= map.clearrect.left - map.screenx
                        && maincharacter.tanky - maincharacter.tankharfVertical < map.clearrect.bottom
                        && maincharacter.tanky +  maincharacter.tankharfVertical >= map.clearrect.top - map.screeny){
                    if(clearflg == false) {
                        nclearscore = numberscore.goalscore;
                        ncleartime = numbertime.nows;
                        nclearhp = maincharacter.hp;
                        clearflg = true;
                        //fade.Start(fade.OUT);
                        cleartime.Start();
                        time.Stop();
                    }
                }

                //ゲームの状態　GAMEON：ゲーム中　MENUON：メニュー開き　GAMEOVER：ゲームオーバー画面
                switch (gamesceneflg) {
                    case GAMEON:

                        numbertime.time = time.Getms();


                        if (maincharacter.hp <= 0) {
                            if(clearflg == false) {
                                bgm.pause();
                                ExplosionAni m = new ExplosionAni();
                                m.Init(maincharacter.tankx, maincharacter.tanky, maincharacter.tankSide, maincharacter.tankVertical);
                                reartankanimations.add(m);
                                gamesceneflg = GAMEOVER;
                            }

                        }

                        //自機と敵玉の当たり判定
                        Ebatari(maincharacter);

                        //敵と自機玉の当たり判定
                        Beatari(bulletlists, enemylist);

                        //敵と自機の当たり判定
                        //Ematari(maincharacter,enemylist);

                        //自機とブロックの当たり判定
                        atari9(maincharacter);

                        //弾の発射
                        //log(""+ bulletlists.size());
                        if (touchoperation.up1flg || touchoperation.up2flg || touchoperation.upflg) {
                            touchoperation.upflg = false;
                            touchoperation.up1flg = false;
                            touchoperation.up2flg = false;
                            if(touchoperation.GetDistance(touchoperation.rightupx,touchoperation.rightdx,touchoperation.rightupy,touchoperation.rightdy) > 20)
                            {
                                Bullet b = new Bullet();
                                b.Init(maincharacter, scaleX, scaleY);
                                bulletlists.add(b);
                                soundPool.play(firingId, 0.7f, 0.7f, 0, 0, 1.0f);
                            }
                        }
                        //弾を消す処理
                        BLoop:
                        for (int i = 0; i < bulletlists.size(); i++) {
                            for (int mj = 0; mj < map.mapsizex; mj++) {
                                for (int mk = 0; mk < map.mapsizey; mk++) {
                                    if (map.amap[mj][mk] == 1) {
                                        //マップに接触したとき爆発させる。８　が12　かも　あたり判定だからどうだろう。
                                        if (bulletlists.get(i).x - bulletlists.get(i).HitSide  < (((mj + 1) * map.blockSide) - map.screenx)
                                                && bulletlists.get(i).x + bulletlists.get(i).HitSide > (mj * map.blockSide) - map.screenx
                                                && bulletlists.get(i).y - bulletlists.get(i).HitVertical < (((mk + 1) * map.blockVertical) - map.screeny)
                                                && bulletlists.get(i).y + bulletlists.get(i).HitVertical > (mk * map.blockVertical) - map.screeny) {

                                            ExplosionAni a = new ExplosionAni();
                                            a.Init(bulletlists.get(i).x, bulletlists.get(i).y, bulletlists.get(i).ExplosionSide, bulletlists.get(i).ExplosionVertical);
                                            reartankanimations.add(a);
                                            //爆発音鳴らす
                                            soundPool.play(expId, 0.5f, 0.5f, 0, 0, 1.0f);

                                            bulletlists.remove(i);
                                            break BLoop;
                                        }

                                    }
                                }
                            }

                            //マップ外に出た場合に弾を消すにしたいけどこれ画面の外に出たら消える。
//                    if (bulletlists.get(i).x < -20 || bulletlists.get(i).x >= VIEW_WIDTH + 20 || bulletlists.get(i).y < -20 || bulletlists.get(i).y >= VIEW_HEIGHT + 20) {
//                        bulletlists.remove(i);
//                        break;
//                    }
                        }

                        //弾を消す処理
                        EBLoop:
                        for (int i = 0; i < eneblelist.size(); i++) {
                            for (int mj = 0; mj < map.mapsizex; mj++) {
                                for (int mk = 0; mk < map.mapsizey; mk++) {
                                    if (map.amap[mj][mk] == 1) {
                                        //マップに接触したとき爆発させる。
                                        if (eneblelist.get(i).x - eneblelist.get(i).DisplayharfSide < (((mj + 1) * map.blockSide) - map.screenx)
                                                && eneblelist.get(i).x + eneblelist.get(i).DisplayharfSide > (mj * map.blockSide) - map.screenx
                                                && eneblelist.get(i).y - eneblelist.get(i).DisplayharfVertical < (((mk + 1) * map.blockVertical) - map.screeny)
                                                && eneblelist.get(i).y + eneblelist.get(i).DisplayharfVertical > (mk * map.blockVertical) - map.screeny) {

                                            ExplosionAni a = new ExplosionAni();
                                            a.Init(eneblelist.get(i).x, eneblelist.get(i).y, eneblelist.get(i).ExplosionSide, eneblelist.get(i).ExplosionVertical);
                                            reartankanimations.add(a);

                                            //爆発音鳴らす
                                            //soundPool.play(expId, 0.2f, 0.2f, 0, 0, 1.0f);

                                            eneblelist.remove(i);
                                            break EBLoop;
                                        }

                                    }
                                }
                            }

                        }


                        //敵のアップデート
                        for (int i = 0; i < enemylist.size(); i++) {
                            enemylist.get(i).Update(bscreenxflg, bscreenyflg, maincharacter, eneblelist);
                            //hp0以下なら爆発　消す。
                            if (enemylist.get(i).hp <= 0) {
                                ExplosionAni e = new ExplosionAni();
                                e.Init(enemylist.get(i).ex, enemylist.get(i).ey, enemylist.get(i).ExproSide, enemylist.get(i).ExproVertical);
                                reartankanimations.add(e);

//                                switch (enemylist.get(i).EneType) {
//                                    case 0:
                                numberscore.goalscore += enemylist.get(i).EarnedScore;

                                enemylist.remove(i);
                            }
                        }
                        //敵　弾のアップデート
                        for (int i = 0; i < eneblelist.size(); i++) {

                            eneblelist.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);

                        }

                        //弾のアップデート
                        for (int i = 0; i < bulletlists.size(); i++) {

                            bulletlists.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);

                        }

                        //自機が動いているとき　煙を発生させる。
                        if (Math.abs(maincharacter.movex) >= 0.1f && Math.abs(maincharacter.movey) >= 0.1f) {
                            if(smokenum < 5) {
                                SmokeAni a = new SmokeAni();
                                a.Setscale(scaleX, scaleY);
                                a.Init(maincharacter.tankx, maincharacter.tanky, maincharacter.SmokeSide, maincharacter.SmokeVertical);
                                pretankanimations.add(a);
                            }
                        }

                        smokenum = 0;

                        //アニメーション更新 前
                        for (int i = 0; i < pretankanimations.size(); i++) {
                            pretankanimations.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);

                            if(pretankanimations.get(i).Typeani == 2) {
                                smokenum++;
                            }
                            if (pretankanimations.get(i).Endflg == true) {
                                pretankanimations.remove(i);
                            }

                        }
                        //アニメーション更新　あと
                        for (int i = 0; i < reartankanimations.size(); i++) {
                            reartankanimations.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);
                            if (reartankanimations.get(i).Endflg == true) {
                                reartankanimations.remove(i);
                            }
                        }


//                pretankx = tankx;
//                pretanky = tanky;
                        map.prescreenx = map.screenx;
                        map.prescreeny = map.screeny;

                        //数字アップデート　スコア
                        numberscore.Update();

                        numbertime.Update();

                        break;
                    case MENUON:
                        break;
                    case GAMEOVER:



                        //弾を消す処理
                        BLoop:
                        for (int i = 0; i < bulletlists.size(); i++) {
                            for (int mj = 0; mj < map.mapsizex; mj++) {
                                for (int mk = 0; mk < map.mapsizey; mk++) {
                                    if (map.amap[mj][mk] == 1) {
                                        //マップに接触したとき爆発させる。８　が12　かも　あたり判定だからどうだろう。
                                        if (bulletlists.get(i).x - bulletlists.get(i).HitSide  < (((mj + 1) * map.blockSide) - map.screenx)
                                                && bulletlists.get(i).x + bulletlists.get(i).HitSide > (mj * map.blockSide) - map.screenx
                                                && bulletlists.get(i).y - bulletlists.get(i).HitVertical < (((mk + 1) * map.blockVertical) - map.screeny)
                                                && bulletlists.get(i).y + bulletlists.get(i).HitVertical > (mk * map.blockVertical) - map.screeny) {

                                            ExplosionAni a = new ExplosionAni();
                                            a.Init(bulletlists.get(i).x, bulletlists.get(i).y, bulletlists.get(i).ExplosionSide, bulletlists.get(i).ExplosionVertical);
                                            reartankanimations.add(a);
                                            //爆発音鳴らす
                                            //soundPool.play(expId, 0.5f, 0.5f, 0, 0, 1.0f);

                                            bulletlists.remove(i);
                                            break BLoop;
                                        }

                                    }
                                }
                            }

                            //マップ外に出た場合に弾を消すにしたいけどこれ画面の外に出たら消える。
//                    if (bulletlists.get(i).x < -20 || bulletlists.get(i).x >= VIEW_WIDTH + 20 || bulletlists.get(i).y < -20 || bulletlists.get(i).y >= VIEW_HEIGHT + 20) {
//                        bulletlists.remove(i);
//                        break;
//                    }
                        }

                        //弾を消す処理
                        EBLoop:
                        for (int i = 0; i < eneblelist.size(); i++) {
                            for (int mj = 0; mj < map.mapsizex; mj++) {
                                for (int mk = 0; mk < map.mapsizey; mk++) {
                                    if (map.amap[mj][mk] == 1) {
                                        //マップに接触したとき爆発させる。
                                        if (eneblelist.get(i).x - eneblelist.get(i).DisplayharfSide < (((mj + 1) * map.blockSide) - map.screenx)
                                                && eneblelist.get(i).x + eneblelist.get(i).DisplayharfSide > (mj * map.blockSide) - map.screenx
                                                && eneblelist.get(i).y - eneblelist.get(i).DisplayharfVertical < (((mk + 1) * map.blockVertical) - map.screeny)
                                                && eneblelist.get(i).y + eneblelist.get(i).DisplayharfVertical > (mk * map.blockVertical) - map.screeny) {

                                            ExplosionAni a = new ExplosionAni();
                                            a.Init(eneblelist.get(i).x, eneblelist.get(i).y, eneblelist.get(i).ExplosionSide, eneblelist.get(i).ExplosionVertical);
                                            reartankanimations.add(a);

                                            //爆発音鳴らす
                                            //soundPool.play(expId, 0.2f, 0.2f, 0, 0, 1.0f);

                                            eneblelist.remove(i);
                                            break EBLoop;
                                        }

                                    }
                                }
                            }

                        }


                        //敵　弾のアップデート
                        for (int i = 0; i < eneblelist.size(); i++) {

                            eneblelist.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);

                        }

                        //弾のアップデート
                        for (int i = 0; i < bulletlists.size(); i++) {

                            bulletlists.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);

                        }

                        //ゲームオーバーで煙ださんでよいでしょ
//                        if (Math.abs(maincharacter.movex) >= 0.1f && Math.abs(maincharacter.movey) >= 0.1f) {
//                            SmokeAni a = new SmokeAni();
//                            a.Init(maincharacter.tankx, maincharacter.tanky, maincharacter.SmokeSide, maincharacter.SmokeVertical);
//                            pretankanimations.add(a);
//                        }

                        smokenum =0;
                        //アニメーション更新 前
                        for (int i = 0; i < pretankanimations.size(); i++) {
                            pretankanimations.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);

                            if(pretankanimations.get(i).Typeani == 2) {
                                smokenum++;
                            }
                            if (pretankanimations.get(i).Endflg == true) {
                                pretankanimations.remove(i);
                            }

                        }
                        //アニメーション更新　あと
                        for (int i = 0; i < reartankanimations.size(); i++) {
                            reartankanimations.get(i).Update(bscreenxflg, bscreenyflg, maincharacter.movex, maincharacter.movey);
                            if (reartankanimations.get(i).Endflg == true) {
                                reartankanimations.remove(i);
                            }
                        }


                        break;
                }
            }else{//startflgelse

                starty += startdownyspeed;
                if(starttime.Getms() <= startstoptime) {
                    if (starty >= startstopy) {
                        starty = startstopy;
                    }
                }


                if(starty >= HARD_VIEW.y){
                    startflg = true;
                    time.Start();
                }
                startmatrix.reset();
                startmatrix.postScale(scaleX,scaleY);
                startmatrix.postTranslate(startx,starty);

            }
        }else{//isfadeend else
            if(fade.isend) {
                starttime.Start();
                isfadeinend = true;
                soundPool.play(startId, 1.0f, 1.0f, 0, 0, 1.0f);
                //
            }
            fade.Update();

        }

        return true;
    }

    @Override
    public boolean Draw(Canvas canvas) {
        switch (gamesceneflg){
            case GAMEON:
                map.Draw(canvas, blockbmp);

                //敵
                for (int i = 0; i < enemylist.size(); i++) {
                    if(enemylist.get(i).ex >= -enemylist.get(i).Side
                            && enemylist.get(i).ex  <= HARD_VIEW.x + enemylist.get(i).Side
                            && enemylist.get(i).ey  >= -enemylist.get(i).Vertical
                            && enemylist.get(i).ey <= HARD_VIEW.y + enemylist.get(i).Vertical) {

                        enemylist.get(i).Draw(canvas);

                    }
                }
                //敵　弾のアップデート
                for (int i = 0; i < eneblelist.size(); i++) {
                    if(eneblelist.get(i).x >= -eneblelist.get(i).DisplaySide
                            && eneblelist.get(i).x <= HARD_VIEW.x + eneblelist.get(i).DisplaySide
                            && eneblelist.get(i).y >= -eneblelist.get(i).DisplayVertical
                            && eneblelist.get(i).y <= HARD_VIEW.y + eneblelist.get(i).DisplayVertical) {

                        eneblelist.get(i).Draw(canvas, etamabmp);
                    }
                }
                //弾
                for (int i = 0; i < bulletlists.size(); i++) {
                    if(bulletlists.get(i).x >= -bulletlists.get(i).DisplaySide
                            && bulletlists.get(i).x <= HARD_VIEW.x + bulletlists.get(i).DisplaySide
                            && bulletlists.get(i).y >= -bulletlists.get(i).DisplayVertical
                            && bulletlists.get(i).y <= HARD_VIEW.y + bulletlists.get(i).DisplayVertical) {

                        bulletlists.get(i).Draw(canvas, tamabmp);
                    }
                }
                //アニメーション
                for (int i = 0; i < pretankanimations.size(); i++) {
                    if(pretankanimations.get(i).x >= -pretankanimations.get(i).sizex
                            && pretankanimations.get(i).x <= HARD_VIEW.x + pretankanimations.get(i).sizex
                            && pretankanimations.get(i).y >= -pretankanimations.get(i).sizey
                            && pretankanimations.get(i).y <= HARD_VIEW.y + pretankanimations.get(i).sizey) {


                        switch (pretankanimations.get(i).Typeani) {
                            case 1:
                                //爆発アニメーション
                                pretankanimations.get(i).Draw(canvas, explosionbmp);
                                break;
                            case 2:
                                pretankanimations.get(i).Draw(canvas, smokebmp);
                                break;
                        }
                    }
                }
                //戦車描画
                maincharacter.DrawChara(canvas);

                //あとアニメーション
                for (int i = 0; i < reartankanimations.size(); i++) {
                    if(reartankanimations.get(i).x >= -reartankanimations.get(i).sizex
                            && reartankanimations.get(i).x <= HARD_VIEW.x + reartankanimations.get(i).sizex
                            && reartankanimations.get(i).y >= -reartankanimations.get(i).sizey
                            && reartankanimations.get(i).y <= HARD_VIEW.y + reartankanimations.get(i).sizey) {


                        switch (reartankanimations.get(i).Typeani) {
                            case 1:
                                //爆発アニメーション
                                reartankanimations.get(i).Draw(canvas, explosionbmp);
                                break;
                            case 2:
                                reartankanimations.get(i).Draw(canvas, smokebmp);
                                break;
                        }
                    }
                }


                //
                maincharacter.DrawHPbar(canvas);

                //p.setColor(Color.BLACK);

                //メニューアイコン　三　
                canvas.drawBitmap(menuiconbmp,menicomatrix,null);

                //スコア
                canvas.drawBitmap(scorebmp,matrix,null);
                numberscore.Draw(canvas);

                //タイム表示
                canvas.drawBitmap(timebmp,timematrix,null);
                numbertime.Draw(canvas);



                //左が四角で右が〇
                touchoperation.drawpoint(canvas);

                if(startflg == false){

                    canvas.drawBitmap(startbmp, startmatrix, null);
                    fade.Draw(canvas);

                }
                if(clearflg == true){

                    canvas.drawBitmap(clearbmp, clearmatrix, null);
                    fade.Draw(canvas);


                }

                break;

            case MENUON:
                //menu 画像表示
                canvas.drawBitmap(menubmp,matrix,null);

                //スコア
                canvas.drawBitmap(scorebmp,matrix,null);
                numberscore.Draw(canvas);

                //タイム表示
                canvas.drawBitmap(timebmp,timematrix,null);
                numbertime.Draw(canvas);

                break;
            case GAMEOVER:
                map.Draw(canvas, blockbmp);

                //敵
                for (int i = 0; i < enemylist.size(); i++) {
                    if(enemylist.get(i).ex >= -enemylist.get(i).Side
                            && enemylist.get(i).ex <= HARD_VIEW.x + enemylist.get(i).Side
                            && enemylist.get(i).ey >= -enemylist.get(i).Vertical
                            && enemylist.get(i).ey <= HARD_VIEW.y + enemylist.get(i).Vertical) {

                        enemylist.get(i).Draw(canvas);

                    }
                }
                //敵　弾のアップデート
                for (int i = 0; i < eneblelist.size(); i++) {
                    if(eneblelist.get(i).x >= -eneblelist.get(i).DisplaySide
                            && eneblelist.get(i).x <= HARD_VIEW.x + eneblelist.get(i).DisplaySide
                            && eneblelist.get(i).y >= -eneblelist.get(i).DisplayVertical
                            && eneblelist.get(i).y <= HARD_VIEW.y + eneblelist.get(i).DisplayVertical) {

                        eneblelist.get(i).Draw(canvas, etamabmp);
                    }
                }
                //弾
                for (int i = 0; i < bulletlists.size(); i++) {
                    if(bulletlists.get(i).x >= -bulletlists.get(i).DisplaySide
                            && bulletlists.get(i).x <= HARD_VIEW.x + bulletlists.get(i).DisplaySide
                            && bulletlists.get(i).y >= -bulletlists.get(i).DisplayVertical
                            && bulletlists.get(i).y <= HARD_VIEW.y + bulletlists.get(i).DisplayVertical) {

                        bulletlists.get(i).Draw(canvas, tamabmp);
                    }
                }
                //アニメーション
                for (int i = 0; i < pretankanimations.size(); i++) {
                    if(pretankanimations.get(i).x >= -pretankanimations.get(i).sizex
                            && pretankanimations.get(i).x <= HARD_VIEW.x + pretankanimations.get(i).sizex
                            && pretankanimations.get(i).y >= -pretankanimations.get(i).sizey
                            && pretankanimations.get(i).y <= HARD_VIEW.y + pretankanimations.get(i).sizey) {


                        switch (pretankanimations.get(i).Typeani) {
                            case 1:
                                //爆発アニメーション
                                pretankanimations.get(i).Draw(canvas, explosionbmp);
                                break;
                            case 2:
                                pretankanimations.get(i).Draw(canvas, smokebmp);
                                break;
                        }
                    }
                }
                //戦車描画
                //maincharacter.DrawChara(canvas);

                //あとアニメーション
                for (int i = 0; i < reartankanimations.size(); i++) {
                    if(reartankanimations.get(i).x >= -reartankanimations.get(i).sizex
                            && reartankanimations.get(i).x <= HARD_VIEW.x + reartankanimations.get(i).sizex
                            && reartankanimations.get(i).y >= -reartankanimations.get(i).sizey
                            && reartankanimations.get(i).y <= HARD_VIEW.y + reartankanimations.get(i).sizey) {


                        switch (reartankanimations.get(i).Typeani) {
                            case 1:
                                //爆発アニメーション
                                reartankanimations.get(i).Draw(canvas, explosionbmp);
                                break;
                            case 2:
                                reartankanimations.get(i).Draw(canvas, smokebmp);
                                break;
                        }
                    }
                }
                //メニューアイコン　三　
                //canvas.drawBitmap(menuiconbmp,VIEW_WIDTH - 100,0,null);

                //p.setAlpha(100);
                //canvas.drawRect(0,0,VIEW_WIDTH,VIEW_HEIGHT,p);
                //p.setAlpha(255);
                //ゲームオーバ画面
                canvas.drawBitmap(gameoverbmp,matrix,null);

                //スコア
                canvas.drawBitmap(scorebmp,matrix,null);
                numberscore.Draw(canvas);

                //タイム表示
                canvas.drawBitmap(timebmp,timematrix,null);
                numbertime.Draw(canvas);

                break;
        }

        touchoperation.debugdraw(canvas,p);
        //debugtextdrow(canvas,p);





        return true;
    }

    @Override
    public boolean End() {
        enemylist.clear();
        pretankanimations.clear();
        reartankanimations.clear();
        bulletlists.clear();
        eneblelist.clear();

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
            inputStream = assetManager.open("tankbase.png");
            tankbasebmp = BitmapFactory.decodeStream(inputStream);
            inputStream = assetManager.open("tanktop.png");
            tanktopbmp = BitmapFactory.decodeStream(inputStream);
            inputStream = assetManager.open("tama.png");
            tamabmp = BitmapFactory.decodeStream(inputStream);
            inputStream = assetManager.open("tankmapthip1.png");
            blockbmp = BitmapFactory.decodeStream(inputStream);
            //爆発画像
            inputStream = assetManager.open("bomb.png");
            explosionbmp = BitmapFactory.decodeStream(inputStream);
            //煙画像
            inputStream = assetManager.open("smoke 3.png");
            smokebmp = BitmapFactory.decodeStream(inputStream);
            //敵１固定砲台
            inputStream = assetManager.open("E01.png");
            enemy01bmp = BitmapFactory.decodeStream(inputStream);
            //
            inputStream = assetManager.open("EB01.png");
            etamabmp = BitmapFactory.decodeStream(inputStream);
            //
            inputStream = assetManager.open("Pause.png");
            menubmp = BitmapFactory.decodeStream(inputStream);
            //
            inputStream = assetManager.open("menyu.png");
            menuiconbmp = BitmapFactory.decodeStream(inputStream);
            //GAMEOVERがめん
            inputStream = assetManager.open("gameover.png");
            gameoverbmp = BitmapFactory.decodeStream(inputStream);

            //スコア
            inputStream = assetManager.open("score.png");
            scorebmp = BitmapFactory.decodeStream(inputStream);

            //ボタンベース
            inputStream = assetManager.open("buttonbase.png");
            buttonbasebmp = BitmapFactory.decodeStream(inputStream);

            //発射ボタン
            inputStream = assetManager.open("buttonfil.png");
            buttonfilbmp = BitmapFactory.decodeStream(inputStream);

            //移動ボタン
            inputStream = assetManager.open("buttontrans.png");
            buttontransbmp = BitmapFactory.decodeStream(inputStream);

            //白数字
            inputStream = assetManager.open("numberwhite.png");
            numberwhitebmp = BitmapFactory.decodeStream(inputStream);

            //タイム表示
            inputStream = assetManager.open("time.png");
            timebmp = BitmapFactory.decodeStream(inputStream);

            //タイム表示
            inputStream = assetManager.open("start.png");
            startbmp = BitmapFactory.decodeStream(inputStream);
            //タイム表示
            inputStream = assetManager.open("clear.png");
            clearbmp = BitmapFactory.decodeStream(inputStream);

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
            AssetFileDescriptor descripter = assetManager.openFd("Stage1bgm.wav");
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

            descripter = assetManager.openFd("se_maoudamashii_battle07.wav");
            firingId = soundPool.load(descripter, 1);

            descripter = assetManager.openFd("se_maoudamashii_battle18.wav");
            enefirId = soundPool.load(descripter, 1);

            descripter = assetManager.openFd("game_explosion5.mp3");
            expId = soundPool.load(descripter, 1);

            descripter = assetManager.openFd("police-whistle2 (1).mp3");
            startId = soundPool.load(descripter, 1);
        }catch(IOException e){
            e.printStackTrace();
        }
        //setFocusable(true);
        return true;
    }

    public void atari9(MainCharacter mc){
        boolean hitxflg = false;
        boolean hityflg = false;
        float ntankx = mc.tankx + (float)mc.movex;
        float ntanky = mc.tanky + (float)mc.movey;


        for(int i=0;i<map.mapsizex;i++) {
            for (int j = 0; j < map.mapsizey; j++) {
                if(map.amap[i][j] == 1) {

                    if(Math.abs(mc.movex) >= 0) {

                        if ((ntankx - mc.tankHitSide) < (i * (map.blockSide) + map.blockSide - map.screenx)
                                && (ntankx + mc.tankHitSide) > ((i * map.blockSide) -  map.screenx)
                                && (mc.tanky - mc.tankHitVertical) < (j * (map.blockVertical) + map.blockVertical - map.screeny)
                                && (mc.tanky + mc.tankHitVertical) > ((j * map.blockVertical) -  map.screeny)) {
                            if(ntankx > i*map.blockSide + mc.tankharfSide - (int) map.screenx){
                                //右側に自機がいるから左に押し出す

                                mc.tankx = ((i * map.blockSide) + map.blockSide - (int) map.screenx) + mc.tankHitSide;
                                //map.screenx = (i) * map.radius ;
                            }else{

                                mc.tankx = ((i * map.blockSide) - (int) map.screenx) - (mc.tankHitSide + 1f);
                            }


                            //j * map.radius + 48;
                            hitxflg = true;
                        }

                    }
                    if(Math.abs(mc.movey) >= 0) {
                        if ((mc.tankx - mc.tankHitSide) < (i * (map.blockSide) + map.blockSide - map.screenx)
                                && (mc.tankx + mc.tankHitSide) > ((i * map.blockSide) -  map.screenx)
                                && (ntanky - mc.tankHitVertical) < (j * (map.blockVertical) + map.blockVertical - map.screeny)
                                && (ntanky + mc.tankHitVertical) > ((j * map.blockVertical) -  map.screeny)) {

                            if(ntanky > j * map.blockVertical + mc.tankharfVertical - (int) map.screeny){

                                mc.tanky = (j * (map.blockVertical) + map.blockVertical - (int) map.screeny) + mc.tankHitVertical;

                            }else{
                                //left
                                mc.tanky = ((j * map.blockVertical) - (int) map.screeny) - (mc.tankHitVertical + 1f);
                            }
                            //map.screeny = (j - 1) * map.radius ;
                            hityflg = true;
                        }
                    }
                }
            }
        }

        //敵と磁気の当たり判定
        for(int ei = 0;ei<enemylist.size();ei++){

            if(Math.abs(mc.movex) >= 0) {
                if ((ntankx - mc.tankHitSide) < (enemylist.get(ei).ex + enemylist.get(ei).harfSide)
                        && (ntankx + mc.tankHitSide) > (enemylist.get(ei).ex - enemylist.get(ei).harfSide )
                        && (mc.tanky - mc.tankHitVertical) < (enemylist.get(ei).ey + enemylist.get(ei).harfVertical)
                        && (mc.tanky + mc.tankHitVertical) > (enemylist.get(ei).ey - enemylist.get(ei).harfVertical)) {

                    if(ntankx > enemylist.get(ei).ex + enemylist.get(ei).harfSide){
                        //右側に自機がいるから左に押し出す

                        mc.tankx = (enemylist.get(ei).ex + enemylist.get(ei).harfSide) + mc.tankHitSide;
                        //map.screenx = (i) * map.radius ;
                    }else{

                        mc.tankx = (enemylist.get(ei).ex - enemylist.get(ei).harfSide) - (mc.tankHitSide + 1f);
                    }

                    enemylist.get(ei).hp -= enemylist.get(ei).ContactDamage;

                    hitxflg = true;
                }

            }
            if(Math.abs(mc.movey) >= 0) {
                if ((mc.tankx - mc.tankHitSide) < (enemylist.get(ei).ex + enemylist.get(ei).harfSide)
                        && (mc.tankx + mc.tankHitSide) > (enemylist.get(ei).ex - enemylist.get(ei).harfSide )
                        && (ntanky - mc.tankHitVertical) < (enemylist.get(ei).ey + enemylist.get(ei).harfVertical )
                        && (ntanky + mc.tankHitVertical) > (enemylist.get(ei).ey - enemylist.get(ei).harfVertical )) {

                    if(ntanky > enemylist.get(ei).ey + enemylist.get(ei).harfVertical ){

                        mc.tanky = (enemylist.get(ei).ey + enemylist.get(ei).harfVertical) + mc.tankHitVertical;
                    }else{

                        mc.tanky = (enemylist.get(ei).ey - enemylist.get(ei).harfVertical) - (mc.tankHitVertical + 1f);
                    }
                    enemylist.get(ei).hp -= enemylist.get(ei).ContactDamage;
                    hityflg = true;
                }
            }
        }

        if(hitxflg== false){
            mc.tankx = ntankx;
        }else{

        }
        if(hityflg == false){
            mc.tanky = ntanky;
        }else{

        }

        //衝突したときHP減らす
        if(hitxflg || hityflg) {
            mc.hp -= mc.ContactDamage;

        }

        screenscroll(hitxflg,hityflg,maincharacter);

    }

    //敵弾と自機の当たり判定
    public void Ebatari(MainCharacter mc){

        for (int i = 0; i < eneblelist.size(); i++) {

            //円と円の当たり
            if((mc.tankx - eneblelist.get(i).x) * (mc.tankx - eneblelist.get(i).x) + (mc.tanky - eneblelist.get(i).y) * (mc.tanky-eneblelist.get(i).y)
                    <= (eneblelist.get(i).DisplayharfSide + mc.tankHitSide) * (eneblelist.get(i).DisplayharfVertical + mc.tankHitVertical)){
                ExplosionAni a = new ExplosionAni();
                a.Init(eneblelist.get(i).x, eneblelist.get(i).y,eneblelist.get(i).ExplosionSide,eneblelist.get(i).ExplosionVertical);
                reartankanimations.add(a);
                soundPool.play(expId, 0.2f, 0.2f, 0, 0, 1.0f);
                mc.hp -= eneblelist.get(i).Damage;
                eneblelist.remove(i);

            }

        }
    }


    public void Beatari(ArrayList<Bullet> bl, ArrayList<EnemyBase> eb){


        beLoop:
        for (int bi = 0; bi < bl.size(); bi++) {
            for(int ej = 0; ej < eb.size(); ej++){
                //円と円の当たり    bl.get(bi).hit === 12 => 8f
                if((bl.get(bi).x - eb.get(ej).ex) * (bl.get(bi).x - eb.get(ej).ex) +
                        (bl.get(bi).y - eb.get(ej).ey) * (bl.get(bi).y - eb.get(ej).ey)
                        <= (bl.get(bi).HitSide +  eb.get(ej).HitSide) * (bl.get(bi).HitVertical + eb.get(ej).HitVertical)){

                    //弾の爆発add
                    ExplosionAni b = new ExplosionAni();
                    b.Init(bl.get(bi).x, bl.get(bi).y,bl.get(bi).ExplosionSide,bl.get(bi).ExplosionVertical);
                    reartankanimations.add(b);
                    //弾消し
                    bl.remove(bi);

                    //敵　hp減らし
                    eb.get(ej).hp -= eb.get(ej).BulletDamage;
                    soundPool.play(expId, 0.5f, 0.5f, 0, 0, 1.0f);
                    //hp0以下なら爆発　消す。
//                    if(eb.get(ej).hp <= 0){
//                        ExplosionAni e = new ExplosionAni();
//                        e.Init(eb.get(ej).ex, eb.get(ej).ey,96,96);
//                        reartankanimations.add(e);
//                        eb.remove(ej);
//                    }
                    //ないとIndex　のエラー　消したときにないよ言われる。
                    break beLoop;
                }
            }
        }
    }

//
//    public void Ematari(MainCharacter mc,ArrayList<EnemyBase> eb) {
//
//        boolean hitxflg = false;
//        boolean hityflg = false;
//        float ntankx = mc.tankx + (float)mc.movex;
//        float ntanky = mc.tanky + (float)mc.movey;
//
//        float newtankx = 0f;
//        float newtanky = 0f;
//
//        for(int ei = 0;ei<enemylist.size();ei++){
//
//            if(Math.abs(mc.movex) >= 0) {
//                if ((ntankx - 38) < (enemylist.get(ei).ex + 48) && (ntankx + 38) > (enemylist.get(ei).ex - 48 )
//                        && (mc.tanky - 38) < (enemylist.get(ei).ey + 48) && (mc.tanky + 38) > (enemylist.get(ei).ey - 48)) {
//
//                    if(ntankx > enemylist.get(ei).ex + 48){
//                        //右側に自機がいるから左に押し出す
//
//                        mc.tankx = (enemylist.get(ei).ex + 48) + 38;
//                        //map.screenx = (i) * map.radius ;
//                    }else{
//
//                        mc.tankx = (enemylist.get(ei).ex - 48) - 39;
//                    }
//
//
//                    hitxflg = true;
//                }
//
//            }
//            if(Math.abs(mc.movey) >= 0) {
//                if ((mc.tankx - 38) < (enemylist.get(ei).ex + 48) && (mc.tankx + 38) > (enemylist.get(ei).ex - 48 )
//                        && (ntanky - 38) < (enemylist.get(ei).ey + 48 ) && (ntanky + 38) > (enemylist.get(ei).ey - 48 )) {
//
//                    if(ntanky > enemylist.get(ei).ey + 48 ){
//
//                        mc.tanky = (enemylist.get(ei).ey + 48) + 38;
//                    }else{
//
//                        mc.tanky = (enemylist.get(ei).ey - 48) - 39;
//                    }
//
//                    hityflg = true;
//                }
//            }
//        }
//
//        if(hitxflg == false){
//            mc.tankx = ntankx;
//        }
//        if(hityflg == false){
//            mc.tanky = ntanky;
//        }
//
//
//    }
    private void ReadEnemyFile(Context context){
        try{
            // AssetManagerクラスのインスタンスはcontext.getAssets()で取れます。
            // Contextが取れない場合getResources().getAssets()または
            // getContext().getAssets()でも取得できます。
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("e" + mapnum + ".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String str;
            StringBuilder strBuilder = new StringBuilder();

            while ((str = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(str, ",");

                int type = Integer.parseInt(st.nextToken());
                int xt =  Integer.parseInt(st.nextToken());
                int yt = Integer.parseInt(st.nextToken());
                int deg = Integer.parseInt(st.nextToken());

                //log(type +" "+ xt + " "+ yt +" "+ deg + "");

                Bitmap eb = enemy01bmp;
                xt *= map.blockSide;
                yt *= map.blockVertical;//96f

//                xt *= scaleX;
//                yt *= scaleY;

                switch(type){
                        case 0:
                            eb = enemy01bmp;
                            break;
                        case 1:

                            break;

                }

                EnemyBase e = new EnemyBase();
                e.Init(type,eb,xt - map.screenx,yt - map.screeny,deg,soundPool,enefirId,scaleX,scaleY);
                enemylist.add(e);
            }


            in.close();
            br.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //画面のスクロールの処理
    public void screenscroll(boolean hitmovex,boolean hitmovey ,MainCharacter mc){

        if (map.screenx <= 0) {
            map.screenx = 0;
            if (mc.tankx <= mc.tankharfSide) {
                mc.tankx = mc.tankharfSide;
            }
        } else {
            if (mc.tankx <= ScreenScrollRange.left) {
                if(hitmovex == false) {
                    map.screenx += mc.movex;
                    bscreenxflg = true;
                }else{
                    //map.screenx += (tankx-pretankx);
                }

                mc.tankx = ScreenScrollRange.left;
            }
        }
        if (map.screeny <= 0) {
            map.screeny = 0;
            if (mc.tanky <= mc.tankharfVertical) {
                mc.tanky = mc.tankharfVertical;
            }
        } else {
            if (mc.tanky <= ScreenScrollRange.top) {
                if(hitmovey == false) {
                    map.screeny += mc.movey;
                    bscreenyflg = true;
                }else{
                    //map.screeny += (tanky-pretanky);
                }

                mc.tanky = ScreenScrollRange.top;
            }
        }

        if (map.screenx >= map.screenMaxx) {
            map.screenx = map.screenMaxx;
            if (mc.tankx >= HARD_VIEW.x - mc.tankharfSide) {
                mc.tankx = HARD_VIEW.x - mc.tankharfSide;
            }
        } else {
            if (mc.tankx >= ScreenScrollRange.right) {
                if(hitmovex == false) {
                    map.screenx += mc.movex;
                    bscreenxflg = true;
                }else{
                    // map.screenx += (tankx-pretankx);
                }

                mc.tankx = ScreenScrollRange.right;
            }
        }

        if (map.screeny >= map.screenMaxy) {
            map.screeny = map.screenMaxy;
            if (mc.tanky >= HARD_VIEW.y - mc.tankharfVertical) {
                mc.tanky = HARD_VIEW.y - mc.tankharfVertical;
            }
        } else {
            if (mc.tanky >= ScreenScrollRange.bottom) {
                if(hitmovey == false) {
                    map.screeny += mc.movey;
                    bscreenyflg = true;
                }else{
                    // map.screeny += (tanky-pretanky);
                }

                mc.tanky = ScreenScrollRange.bottom;
            }
        }

    }
    private void debugtextdrow(Canvas canvas, Paint p){


//        p.setColor(Color.RED);
//        p.setAlpha(100);
//        canvas.drawRect(HARD_VIEW.x - 80,0,HARD_VIEW.x,100,p);
//
//        p.setColor(Color.BLACK);
//        p.setAlpha(255);

//        p.setColor(Color.BLUE);
//        p.setAlpha(255);
//        for (int i = 0; i < eneblelist.size(); i++) {
//
//            canvas.drawCircle(eneblelist.get(i).x, eneblelist.get(i).y, 12, p);
//        }
//
//        p.setColor(Color.BLACK);
//        p.setAlpha(255);
        //canvas.drawText("fps=" + fps ,0,20,p);de
        //canvas.drawText("hp   =" + hp     ,0,140,p);
        //canvas.drawText("(96 * (hp / MAXHP)   =" + (96 * ((float)hp / (float)MAXHP)),200,140,p);
        canvas.drawText("tankx   =" + maincharacter.tankx     ,0,140,p);
        canvas.drawText("tanky   =" + maincharacter.tanky     ,200,140,p);
//
//
//
        canvas.drawText("smoke num =" + smokenum    ,0,80,p);
        canvas.drawText("VIEW_HEIGHT   =" + VIEW_HEIGHT    ,0,100,p);
        //numberscore.goalscore = (int)time.Getms();
//        canvas.drawText("map.mapsizex   =" + map.mapsizex     ,0,240,p);
//        canvas.drawText("map.mapsizey   =" + map.mapsizey    ,200,240,p);
        canvas.drawText("map.screenx   =" + map.screenx     ,0,260,p);
        canvas.drawText("map.screeny   =" + map.screeny     ,300,260,p);
        canvas.drawText("tankvertical   =" + maincharacter.tankVertical     ,0,280,p);
        canvas.drawText("tankside   =" + maincharacter.tankSide     ,300,280,p);
        canvas.drawText("tankharfVertical   =" + maincharacter.tankharfVertical     ,0,300,p);
        canvas.drawText("ankharfSide   =" + maincharacter.tankharfSide   ,300,300,p);
        canvas.drawText("bscreenx   =" + bscreenxflg     ,0,380,p);
        canvas.drawText("bscreeny   =" + bscreenyflg    ,200,380,p);
        canvas.drawText("map.screenMaxx,  =" + map.screenMaxx,0,320,p);
        canvas.drawText("map.screenMaxy,   =" + map.screenMaxy     ,0,340,p);
//        canvas.drawText("pretankx   =" + (tankx-pretankx)     ,0,340,p);
//        canvas.drawText("pretanky   =" + (tanky-pretanky)     ,200,340,p);
//        canvas.drawText("animetionsize   =" + pretankanimations.size()     ,0,360,p)//        loop.up1flg = false;
//        loop.up2flg = false;
        // canvas.drawText("ONE_MILLI_TO_NANO=" + ONE_MILLI_TO_NANO,0,140,p);

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

    private void onceInit(){
        if(onceinitflg == false) {
            onceinitflg = true;
            LoadImage();
            LoadMusic();
            Loadse();


            startstopy = (HARD_VIEW.y / 2 - (80 * scaleY));
            startdownyspeed *= scaleY;

            clearstopy = (HARD_VIEW.y / 2 - (80 * scaleY));
            cleardownyspeed *= scaleY;

            ScreenScrollRange.left *= scaleX;
            ScreenScrollRange.right *= scaleX;
            ScreenScrollRange.top *= scaleY;
            ScreenScrollRange.bottom *= scaleY;

            touchoperation.onceInit(scaleX,scaleY);

            // touchoperation
        }
    }

}
