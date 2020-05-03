package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.tankdedone.Scene.GameScene.GameScene;

public class TouchOperation {

    public float HARD_WIDTH,HARD_HEIGHT;

    private static final float DEFAULT_VALUE = -100f;

    public static final int TouchPoint = 75;
    public float TouchPointleft = 200f;
    public float TouchPointTop = 570f;
    public float TouchPointright = 1080f;


    public float touchx1 = DEFAULT_VALUE,touchy1 = DEFAULT_VALUE;
    public float touchx2 = DEFAULT_VALUE,touchy2 = DEFAULT_VALUE;
    public float downx1 = DEFAULT_VALUE,downy1 = DEFAULT_VALUE;
    public float downx2 = DEFAULT_VALUE ,downy2 = DEFAULT_VALUE;




    public float upx = DEFAULT_VALUE, upy = DEFAULT_VALUE;
    public float upx1 = DEFAULT_VALUE, upy1 = DEFAULT_VALUE;
    public float upx2 = DEFAULT_VALUE, upy2 = DEFAULT_VALUE;

    public boolean down1flg=false,down2flg=false;
    public boolean down1flgisleft = false,down2flgisleft;
    public boolean up1flg = false,up2flg = false;
    public boolean upflg = false;

    public float leftdx = DEFAULT_VALUE,leftdy= DEFAULT_VALUE;
    public float lefttx= DEFAULT_VALUE,leftty= DEFAULT_VALUE;
    public float rightdx= DEFAULT_VALUE,rightdy= DEFAULT_VALUE;
    public float righttx= DEFAULT_VALUE,rightty= DEFAULT_VALUE;
    public float leftupx = DEFAULT_VALUE,leftupy = DEFAULT_VALUE;
    public float rightupx = DEFAULT_VALUE,rightupy = DEFAULT_VALUE;

    public boolean menupflg = false;

    public float movex,movey;

    private float centerline;

    //MENUONの場合とかに片方タッチされたままだと誤爆が多いのでそれよけ
    public int changeupplus = 0;

    Paint touchp1,touchp2;
    Paint downp1,downp2;

    private boolean onceinitflg = false;

    //(movex >= 81 && movex < 281 && movey >= 357 && movey < 557) {
    Rect menuReturngamme = new Rect(81,357,281,557);
    //if (movex >= 313 && movex < 513 && movey >= 357 && movey < 557)
    Rect menuBegin = new Rect(313,357,513,557);
    //if (movex >= 551 && movex < 751 && movey >= 357 && movey < 557) {
    Rect menutoStage = new Rect(551,357,751,557);
    // if (movex >= 789 && movex < 989 && movey >= 357 && movey < 557) {
    Rect menutoTitle = new Rect(789,357,989,557);
    //if (movex >= 1024 && movex < 1224 && movey >= 357 && movey < 557) {
    Rect menuSetting = new Rect(1024,357,1224,557);


    //                    if (movex >= 151 && movex < 351 && movey >= 357 && movey < 557) {
    Rect gameoverBegin = new Rect(151,357,351,557);
    //                    if (movex >= 551 && movex < 751 && movey >= 357 && movey < 557) {
    Rect gameovertoStage = new Rect(551,357,751,557);
  //if (movex >= 955 && movex < 1155 && movey >= 357 && movey < 557) {  
    Rect gameovertoTitle = new Rect(955,357,1155,557);

    

    //Bitmap
    Bitmap Bbase;
    Bitmap Btrans;
    Bitmap Bfiring;
    
    TouchOperation(){

    }

    public void onceInit(float scaleX,float scaleY){
        if(onceinitflg == false) {
            onceinitflg = true;

            TouchPointleft *= scaleX;
            TouchPointright *= scaleX;
            TouchPointTop *= scaleY;

            menuReturngamme.left *= scaleX;
            menuReturngamme.right *= scaleX;
            menuReturngamme.top *= scaleY;
            menuReturngamme.bottom *= scaleY;

            menuBegin.left *= scaleX;
            menuBegin.right *= scaleX;
            menuBegin.top *= scaleY;
            menuBegin.bottom *= scaleY;

            menutoStage.left *= scaleX;
            menutoStage.right *= scaleX;
            menutoStage.top *= scaleY;
            menutoStage.bottom *= scaleY;

            menutoTitle.left *= scaleX;
            menutoTitle.right *= scaleX;
            menutoTitle.top *= scaleY;
            menutoTitle.bottom *= scaleY;

            menuSetting.left *= scaleX;
            menuSetting.right *= scaleX;
            menuSetting.top *= scaleY;
            menuSetting.bottom *= scaleY;



            gameoverBegin.left *= scaleX;
            gameoverBegin.right *= scaleX;
            gameoverBegin.top *= scaleY;
            gameoverBegin.bottom *= scaleY;

            gameovertoStage.left *= scaleX;
            gameovertoStage.right *= scaleX;
            gameovertoStage.top *= scaleY;
            gameovertoStage.bottom *= scaleY;

            gameovertoTitle.left *= scaleX;
            gameovertoTitle.right *= scaleX;
            gameovertoTitle.top *= scaleY;
            gameovertoTitle.bottom *= scaleY;


        }
    }


    public void Init(float hardw,float hardh,Bitmap base,Bitmap trans,Bitmap firing){
        drawinit();

        HARD_WIDTH = hardw;
        HARD_HEIGHT = hardh;

        touchx1 = DEFAULT_VALUE;
        touchy1 = DEFAULT_VALUE;
        touchx2 = DEFAULT_VALUE;
        touchy2 = DEFAULT_VALUE;
        downx1 = DEFAULT_VALUE;
        downy1 = DEFAULT_VALUE;
        downx2 = DEFAULT_VALUE;
        downy2 = DEFAULT_VALUE;

        upx = DEFAULT_VALUE;
        upy = DEFAULT_VALUE;

        upx1 = DEFAULT_VALUE;
        upy1 = DEFAULT_VALUE;

        upx2 = DEFAULT_VALUE;
        upy2 = DEFAULT_VALUE;

        changeupplus = 0;

        down1flg=false;
        down2flg=false;
        down1flgisleft = false;
        down2flgisleft = false;
        upflg = false;
        up1flg = false;
        up2flg = false;

        leftdx = DEFAULT_VALUE;
        leftdy= DEFAULT_VALUE;
        lefttx= DEFAULT_VALUE;
        leftty= DEFAULT_VALUE;
        rightdx= DEFAULT_VALUE;
        rightdy= DEFAULT_VALUE;
        righttx= DEFAULT_VALUE;
        rightty= DEFAULT_VALUE;

        leftupx = DEFAULT_VALUE;
        leftupy = DEFAULT_VALUE;
        rightupx = DEFAULT_VALUE;
        rightupy = DEFAULT_VALUE;

        menupflg = false;

        Bbase = base;
        Btrans = trans;
        Bfiring = firing;



        centerline = HARD_WIDTH / 2;



    }

    


    public void SetSide(){


        //タップしたのが左か右かセット
        if(down1flg && down2flg == false) {
            if (downx1 < centerline) {
                down1flgisleft = true;
                down2flgisleft = false;
            } else {
                down1flgisleft = false;
                down2flgisleft = true;
            }
        }else if(down1flg == false && down2flg){
            if (downx2 < centerline) {
                down1flgisleft = false;
                down2flgisleft = true;
            } else {
                down1flgisleft = true;
                down2flgisleft = false;
            }
        }else if(down1flg && down2flg){
            if(downx1 < downx2) {
                down1flgisleft = true;
                down2flgisleft = false;
            }else if(downx1 >= downx2){
                down1flgisleft = false;
                down2flgisleft = true;
            }
        }else{
            down1flgisleft = false;
            down2flgisleft = false;
        }
        if(down1flgisleft) {
            leftdx = downx1;
            rightdx = downx2;
            leftdy =  downy1;
            rightdy = downy2;

            lefttx = touchx1;
            righttx = touchx2;
            leftty = touchy1;
            rightty = touchy2;

        }else if(down2flgisleft){
            leftdx = downx2;
            rightdx = downx1;
            leftdy =  downy2;
            rightdy = downy1;

            lefttx = touchx2;
            righttx = touchx1;
            leftty = touchy2;
            rightty = touchy1;
        }

        if(up1flg || up2flg || upflg){
            if(down1flgisleft){
                leftupx = upx1;
                leftupy = upy1;

                rightupx = upx2;
                rightupy = upy2;
            }else if(down2flgisleft) {
                leftupx = upx2;
                leftupy = upy2;

                rightupx = upx1;
                rightupy = upy1;
            }else{
                if(leftdx < centerline) {
                    leftupx = upx;
                    leftupy = upy;
                }else{
                    rightupx = upx;
                    rightupy = upy;
                }
            }
        }
    }

    public void operation(GameScene l){

        switch (l.gamesceneflg) {
            case 0://ゲーム中
                //左手の操作
                if (lefttx > -1 && leftty > -1) {
                    //左側が押されているとき
                    if (down1flgisleft || down2flgisleft) {
                        //log(""+lefttx + "   " + leftty + "   " + leftdy + "   " + leftdx);

                        if (GetDistance(leftdx, lefttx, leftdy, leftty) > 20) { //タッチして向かせるときの調整
                            //0でいろいろするとNaNになるからそれよけ。
                            if (lefttx != leftdx && leftty != leftdy) {

                                //戦車動かす
                                double pow, sqr, speedx = l.maincharacter.speedx;
                                double speedy = l.maincharacter.speedy;
                                pow = Math.pow((lefttx - leftdx), 2) + Math.pow((leftty - leftdy), 2);
                                sqr = Math.sqrt(pow);

                                l.maincharacter.movex = ((lefttx - leftdx) / sqr) * speedx;
                                l.maincharacter.movey = ((leftty - leftdy) / sqr) * speedy;

                                double radian = Math.atan2(leftty - leftdy, lefttx - leftdx);
                                double degree = radian * 180d / Math.PI;
                                l.maincharacter.tankbasedegree = (float) degree + 90f;


                            }
                        }
                    }
                }
                if (righttx > -1 && rightty > -1) {
                    //右側　打つ処理　右側が押されているとき
                    if (down1flg && down1flgisleft == false || down2flg && down2flgisleft == false) {

                        if (GetDistance(righttx, rightdx, rightty, rightdy) > 20) { //タッチして向かせるときの調整
                            //砲台の方向を変更させる
                            double radian = Math.atan2(rightty - rightdy, righttx - rightdx);
                            double degree = radian * 180d / Math.PI;
                            l.maincharacter.tanktopdegree = (float) degree + 90f;

                        }
                    }
                }

                //メニューアイコンが押されたとき // 100 ー＞メニューアイコンのサイズ
                //                   if (upx >= l.HARD_VIEW.x - (100 * l.scaleX) && upx < l.HARD_VIEW.x && upy >= 0 && upy < (100 * l.scaleX)){
                if ((upx >= l.HARD_VIEW.x - (100 * l.scaleX) && upx < l.HARD_VIEW.x && upy >= 0 && upy < (100 * l.scaleX)) ||
                        (upx1 >= l.HARD_VIEW.x - (100 * l.scaleX) && upx1 < l.HARD_VIEW.x && upy1 >= 0 && upy1 < (100 * l.scaleX)) ||
                        (upx2 >= l.HARD_VIEW.x - (100 * l.scaleX) && upx2 < l.HARD_VIEW.x && upy2 >= 0 && upy2 < (100 * l.scaleX))) {
//                    if(rightupx >= l.HARD_VIEW.x - (100 * l.scaleX) && rightupx < l.HARD_VIEW.x && rightupy >= 0 && rightupy < (100 * l.scaleX)){

                    l.gamesceneflg = l.MENUON;
                    l.time.Stop();
//                        downx1 = DEFAULT_VALUE;
//                        downy1 = DEFAULT_VALUE;
                }


                break;
            case 1://MENUON
                if(touchx1>0 && touchy1 >0) {
                    movex = touchx1;
                    movey = touchy1;
                }else{
                    if(down1flg == true) {
                        movex = downx1;
                        movey = downy1;
                    }
                }
                //メニュー時のゲームに戻る処理
                if (movex >= menuReturngamme.left && movex < menuReturngamme.right && movey >= menuReturngamme.top && movey < menuReturngamme.bottom) {
                    if(menupflg) {
                        //l.time.Start();
                        l.gamesceneflg = l.GAMEON;
                        l.starttime.End();
                        l.starttime.Start();
                        l.startflg = false;
                        l.startx = (l.HARD_VIEW.x / 2) - (190 * l.scaleX);
                        l.starty = -160 * l.scaleY;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //313 357  最初から
                if (movex >= menuBegin.left && movex < menuBegin.right && movey >= menuBegin.top && movey < menuBegin.bottom) {
                    if(menupflg) {
                        l.Init();
                        l.gamesceneflg = l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                if (movex >= menutoStage.left && movex < menutoStage.right && movey >= menutoStage.top && movey < menutoStage.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.STAGE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //789 356　タイトルへ
                if  (movex >= menutoTitle.left && movex < menutoTitle.right && movey >= menutoTitle.top && movey < menutoTitle.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.TITLE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //1024 356　設定
                if (movex >= menuSetting.left && movex < menuSetting.right && movey >= menuSetting.top && movey < menuSetting.bottom) {
                    if(menupflg) {
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }

                break;
            case 2://GAMEOVER


                if(touchx1>0 && touchy1 >0) {
                    movex = touchx1;
                    movey = touchy1;
                }else{
                    if(down1flg == true) {
                        movex = downx1;
                        movey = downy1;
                    }
                }
                //最初から処理
                if (movex >= gameoverBegin.left && movex < gameoverBegin.right && movey >= gameoverBegin.top && movey < gameoverBegin.bottom) {
                    if(menupflg) {
                        l.Init();
                        l.gamesceneflg = l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //551 357　ステージ選択
                if (movex >= gameovertoStage.left && movex < gameovertoStage.right && movey >= gameovertoStage.top && movey < gameovertoStage.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.STAGE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //789 356　タイトルへ
                if (movex >= gameovertoTitle.left && movex < gameovertoTitle.right && movey >= gameovertoTitle.top && movey < gameovertoTitle.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.TITLE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }

                break;
        }


    }

    
    public void rightoperation(GameScene l){

        if (down1flg && down1flgisleft == false || down2flg && down2flgisleft == false) {


            switch (l.gamesceneflg) {
                case 0://ゲーム中
                    //メニューアイコンが押されたとき // 100 ー＞メニューアイコンのサイズ
                    if ((upx >= l.HARD_VIEW.x - (100 * l.scaleX) && upx < l.HARD_VIEW.x && upy >= 0 && upy < (100 * l.scaleX)) ||
                            (upx1 >= l.HARD_VIEW.x - (100 * l.scaleX) && upx1 < l.HARD_VIEW.x && upy1 >= 0 && upy1 < (100 * l.scaleX)) ||
                                (upx2 >= l.HARD_VIEW.x - (100 * l.scaleX) && upx2 < l.HARD_VIEW.x && upy2 >= 0 && upy2 < (100 * l.scaleX))) {
                    //if(rightupx >= l.HARD_VIEW.x - (100 * l.scaleX) && rightupx < l.HARD_VIEW.x && rightupy >= 0 && rightupy < (100 * l.scaleX)){

                        l.gamesceneflg = l.MENUON;
                        l.time.Stop();
//                        downx1 = DEFAULT_VALUE;
//                        downy1 = DEFAULT_VALUE;
                    }

                        if (GetDistance(righttx, rightdx, rightty, rightdy) > 20) { //タッチして向かせるときの調整
                            //砲台の方向を変更させる
                            double radian = Math.atan2(rightty - rightdy, righttx - rightdx);
                            double degree = radian * 180d / Math.PI;
                            l.maincharacter.tanktopdegree = (float) degree + 90f;

                            //弾の発射
                            //log(""+ bulletlists.size());
//                        if (up1flg || up2flg) {
//                            up1flg = false;
//                            up2flg = false;
//
//                            Bullet b = new Bullet();
//                            b.Init(l.maincharacter, l.scaleX, l.scaleY);
//                            l.bulletlists.add(b);
//                            l.soundPool.play(l.firingId, 0.7f, 0.7f, 0, 0, 1.0f);
//
//                        }

                    }
                    break;
                case 1://MENUON
                    //
                    if(touchx1 > 0 && touchy1 > 0) {
                        movex = touchx1;
                        movey = touchy1;
                    }else{
                        if(down1flg == true) {
                            movex = downx1;
                            movey = downy1;
                        }
                    }
                    if(changeupplus >= 1){
                        menupflg = false;
                        changeupplus = 0;
                    }

                    //メニュー時のゲームに戻る処理
                    if (movex >= menuReturngamme.left && movex < menuReturngamme.right && movey >= menuReturngamme.top && movey < menuReturngamme.bottom) {
                        if(menupflg) {
                            //l.time.Start();
                            l.gamesceneflg = l.GAMEON;
                            l.starttime.End();
                            l.starttime.Start();
                            l.startflg = false;
                            l.startx = (l.HARD_VIEW.x / 2) - (190 * l.scaleX);
                            l.starty = -160 * l.scaleY;
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
                    //313 357  最初から
                    if (movex >= menuBegin.left && movex < menuBegin.right && movey >= menuBegin.top && movey < menuBegin.bottom) {
                        if(menupflg) {
                            l.Init();
                            l.gamesceneflg = l.GAMEON;
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
                    //551 357　ステージ選択
                    if (movex >= menutoStage.left && movex < menutoStage.right && movey >= menutoStage.top && movey < menutoStage.bottom) {
                        if(menupflg) {
                            l.End();
                            l.nextscene = l.STAGE;
                            l.gamesceneflg =l.GAMEON;
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
                    //789 356　タイトルへ
                    if (movex >= menutoTitle.left && movex < menutoTitle.right && movey >= menutoTitle.top && movey < menutoTitle.bottom) {
                        if(menupflg) {
                            l.End();
                            l.nextscene = l.TITLE;
                            l.gamesceneflg =l.GAMEON;
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
                    //1024 356　設定
                    if (movex >= menuSetting.left && movex < menuSetting.right && movey >= menuSetting.top && movey < menuSetting.bottom) {
                        if(menupflg) {
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
//                if (downx2 >= 81 && downx2 < 281 && downy2 >= 357 && downy2 < 557) {
//                    if(up1flg || up2flg) {
//                        l.gamesceneflg = false;
//                    }
//                }
                    break;
                case 2://GAMEOVER
                    if(touchx1>0 && touchy1 >0) {
                        movex = touchx1;
                        movey = touchy1;
                    }else{
                        if(down1flg == true) {
                            movex = downx1;
                            movey = downy1;
                        }
                    }

                    //最初から処理
                    if (movex >= gameoverBegin.left && movex < gameoverBegin.right && movey >= gameoverBegin.top && movey < gameoverBegin.bottom) {
                        if(menupflg) {
                            l.Init();
                            l.gamesceneflg = l.GAMEON;
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
                    //551 357　ステージ選択
                    if (movex >= gameovertoStage.left && movex < gameovertoStage.right && movey >= gameovertoStage.top && movey < gameovertoStage.bottom) {
                        if(menupflg) {
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
                    //789 356　タイトルへ
                    if (movex >= gameovertoTitle.left && movex < gameovertoTitle.right && movey >= gameovertoTitle.top && movey < gameovertoTitle.bottom) {
                        if(menupflg) {
                            l.End();
                            l.gamesceneflg =l.GAMEON;
                            movex = DEFAULT_VALUE;
                            movey = DEFAULT_VALUE;
                        }
                    }
                    break;

            }

            //double degree = 90f;
            //tanktopmatrix.postRotate((float)degree);
            //log("degree:" + degree);
        }

    }
    
    public void leftoperation(GameScene l){
        switch (l.gamesceneflg) {
            case 0://ゲーム中
                if (lefttx > -1 && leftty > -1) {
                    if (down1flgisleft || down2flgisleft) {
                        //log(""+lefttx + "   " + leftty + "   " + leftdy + "   " + leftdx);

                        if(GetDistance(leftdx,lefttx,leftdy,leftty) > 20) { //タッチして向かせるときの調整
                            //0でいろいろするとNaNになるからそれよけ。
                            if (lefttx != leftdx && leftty != leftdy) {

                                double pow, sqr, speedx = l.maincharacter.speedx;
                                double speedy = l.maincharacter.speedy;
                                pow = Math.pow((lefttx - leftdx), 2) + Math.pow((leftty - leftdy), 2);
                                sqr = Math.sqrt(pow);
                                //double movex,movey;
                                l.maincharacter.movex = ((lefttx - leftdx) / sqr) * speedx;
                                l.maincharacter.movey = ((leftty - leftdy) / sqr) * speedy;
//                        tankx += movex;
//                        tanky += movey;

                                double radian = Math.atan2(leftty - leftdy, lefttx - leftdx);
                                double degree = radian * 180d / Math.PI;
                                l.maincharacter.tankbasedegree = (float) degree + 90f;


                            }
                        }
                    }
                }
                break;
            case 1://MENUON
                if(touchx1>0 && touchy1 >0) {
                    movex = touchx1;
                    movey = touchy1;
                }else{
                    if(down1flg == true) {
                        movex = downx1;
                        movey = downy1;
                    }
                }
                //メニュー時のゲームに戻る処理
                if (movex >= menuReturngamme.left && movex < menuReturngamme.right && movey >= menuReturngamme.top && movey < menuReturngamme.bottom) {
                    if(menupflg) {
                        //l.time.Start();
                        l.gamesceneflg = l.GAMEON;
                        l.starttime.End();
                        l.starttime.Start();
                        l.startflg = false;
                        l.startx = (l.HARD_VIEW.x / 2) - (190 * l.scaleX);
                        l.starty = -160 * l.scaleY;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //313 357  最初から
                if (movex >= menuBegin.left && movex < menuBegin.right && movey >= menuBegin.top && movey < menuBegin.bottom) {
                    if(menupflg) {
                        l.Init();
                        l.gamesceneflg = l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                if (movex >= menutoStage.left && movex < menutoStage.right && movey >= menutoStage.top && movey < menutoStage.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.STAGE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //789 356　タイトルへ
                if  (movex >= menutoTitle.left && movex < menutoTitle.right && movey >= menutoTitle.top && movey < menutoTitle.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.TITLE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //1024 356　設定
                if (movex >= menuSetting.left && movex < menuSetting.right && movey >= menuSetting.top && movey < menuSetting.bottom) {
                    if(menupflg) {
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
//            if (downx2 >= 81 && downx2 < 281 && downy2 >= 357 && downy2 < 557) {
//                if(up1flg || up2flg) {
//                    l.gamesceneflg = false;
//                }
//            }
                break;
            case 2://GAMEOVER


                if(touchx1>0 && touchy1 >0) {
                    movex = touchx1;
                    movey = touchy1;
                }else{
                    if(down1flg == true) {
                        movex = downx1;
                        movey = downy1;
                    }
                }
                //最初から処理
                if (movex >= gameoverBegin.left && movex < gameoverBegin.right && movey >= gameoverBegin.top && movey < gameoverBegin.bottom) {
                    if(menupflg) {
                        l.Init();
                        l.gamesceneflg = l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //551 357　ステージ選択
                if (movex >= gameovertoStage.left && movex < gameovertoStage.right && movey >= gameovertoStage.top && movey < gameovertoStage.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.STAGE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }
                //789 356　タイトルへ
                if (movex >= gameovertoTitle.left && movex < gameovertoTitle.right && movey >= gameovertoTitle.top && movey < gameovertoTitle.bottom) {
                    if(menupflg) {
                        l.End();
                        l.nextscene = l.TITLE;
                        l.gamesceneflg =l.GAMEON;
                        movex = DEFAULT_VALUE;
                        movey = DEFAULT_VALUE;
                    }
                }

                break;
        }


    }
    

    private void drawinit(){
        touchp1 = new Paint();
        touchp1.setColor(Color.RED);
        touchp2 = new Paint();
        touchp2.setColor(Color.GREEN);
        downp1 = new Paint();
        downp1.setColor(Color.RED);
        downp1.setAlpha(50);
        downp2 = new Paint();
        downp2.setColor(Color.GREEN);
        downp2.setAlpha(50);
    }


    public void drawpoint(Canvas canvas){

        

        if(down1flg && down2flg == false) {
            if (down1flgisleft) {
                //canvas.drawCircle(downx1,downy1,30,downp1);
                //canvas.drawCircle(touchx1,touchy1,30,touchp1);
                canvas.drawBitmap(Bbase,downx1 - TouchPoint,downy1 - TouchPoint,null);
                canvas.drawBitmap(Btrans,touchx1 - TouchPoint,touchy1 - TouchPoint,null);
                canvas.drawBitmap(Bbase,TouchPointright - TouchPoint,TouchPointTop - TouchPoint,null);
                canvas.drawBitmap(Bfiring,TouchPointright - TouchPoint, TouchPointTop - TouchPoint,null);
//                canvas.drawRect(downx1 - 32, downy1 - 32, downx1 + 32, downy1 + 32, downp1);
//                canvas.drawRect(touchx1 - 32, touchy1 - 32, touchx1 + 32, touchy1 + 32, touchp1);
            } else if (down2flgisleft) {
                canvas.drawBitmap(Bbase,TouchPointleft - TouchPoint,TouchPointTop - TouchPoint,null);
                canvas.drawBitmap(Btrans,TouchPointleft- TouchPoint, TouchPointTop- TouchPoint,null);
                canvas.drawBitmap(Bbase,downx1 - TouchPoint,downy1 - TouchPoint,null);
                canvas.drawBitmap(Bfiring,touchx1 - TouchPoint,touchy1 - TouchPoint,null);

//                canvas.drawCircle(downx1, downy1, 30, downp2);
//                canvas.drawCircle(touchx1, touchy1, 30, touchp2);
            }
        }else if(down1flg == false && down2flg){
            if (down1flgisleft) {
//                canvas.drawCircle(downx2, downy2, 30, downp2);
//                canvas.drawCircle(touchx2, touchy2, 30, touchp2);
                canvas.drawBitmap(Bbase,TouchPointleft - TouchPoint,TouchPointTop - TouchPoint,null);
                canvas.drawBitmap(Btrans,TouchPointleft- TouchPoint, TouchPointTop- TouchPoint,null);
                canvas.drawBitmap(Bbase,downx2 - TouchPoint,downy2 - TouchPoint,null);
                canvas.drawBitmap(Bfiring,touchx2 - TouchPoint,touchy2 - TouchPoint,null);


            } else if (down2flgisleft) {
                canvas.drawBitmap(Bbase,downx2 - TouchPoint,downy2 - TouchPoint,null);
                canvas.drawBitmap(Btrans,touchx2 - TouchPoint,touchy2 - TouchPoint,null);
                canvas.drawBitmap(Bbase,TouchPointright - TouchPoint,TouchPointTop - TouchPoint,null);
                canvas.drawBitmap(Bfiring,TouchPointright - TouchPoint, TouchPointTop - TouchPoint,null);
//                canvas.drawRect(downx2 - 32, downy2 - 32, downx2 + 32, downy2 + 32, downp1);
//                canvas.drawRect(touchx2 - 32, touchy2 - 32, touchx2 + 32, touchy2 + 32, touchp1);
            }
        } else if(down1flg && down2flg){

//            canvas.drawRect(leftdx - 32, leftdy-32, leftdx+32,leftdy+32, downp1);
//            canvas.drawRect(lefttx-32,leftty-32, lefttx+32,leftty+32, touchp1);
            canvas.drawBitmap(Bbase,leftdx - TouchPoint,leftdy - TouchPoint,null);
            canvas.drawBitmap(Btrans,lefttx - TouchPoint,leftty - TouchPoint,null);

//            canvas.drawCircle(rightdx,rightdy,30,downp2);
//            canvas.drawCircle(righttx,rightty,30,touchp2);
            canvas.drawBitmap(Bbase,rightdx - TouchPoint,rightdy - TouchPoint,null);
            canvas.drawBitmap(Bfiring,righttx - TouchPoint,rightty - TouchPoint,null);
        }else{
            //なにも押されてない時140 570
            canvas.drawBitmap(Bbase,TouchPointleft - TouchPoint,TouchPointTop - TouchPoint,null);
            canvas.drawBitmap(Btrans,TouchPointleft- TouchPoint, TouchPointTop- TouchPoint,null);
            canvas.drawBitmap(Bbase,TouchPointright - TouchPoint,TouchPointTop - TouchPoint,null);
            canvas.drawBitmap(Bfiring,TouchPointright - TouchPoint, TouchPointTop - TouchPoint,null);
        }
 
    }

    public void AcDown(){

    }



    public void debugdraw(Canvas canvas , Paint p){

//        p.setColor(Color.RED);
////        p.setAlpha(100);
////
////        canvas.drawCircle(HARD_WIDTH/2,HARD_HEIGHT/2,GetDistance(leftdx,lefttx,leftdy,leftty),p);
////
////        p.setColor(Color.BLUE);
////        p.setAlpha(100);
////        canvas.drawCircle(HARD_WIDTH/2,HARD_HEIGHT/2,GetDistance(righttx,rightdx,rightty,rightdy),p);
////
////        p.setColor(Color.BLACK);
////        p.setAlpha(255);

//        canvas.drawText("distance  =" + GetDistance(leftdx,lefttx,leftdy,leftty)     ,0,160,p);
//        canvas.drawText("rightdistance  =" + GetDistance(righttx,rightdx,rightty,rightdy)     ,0,180,p);

        canvas.drawText("upx   =" + upx     ,0,160,p);
        canvas.drawText("upy   =" + upy     ,0,180,p);

        canvas.drawText("upx1   =" + upx1     ,0,220,p);
        canvas.drawText("upy1   =" + upy1     ,0,240,p);

        canvas.drawText("upx2   =" + upx2     ,0,280,p);
        canvas.drawText("upy2   =" + upy2     ,0,300,p);

        canvas.drawText("leftupx   =" + leftupx     ,0,340,p);
        canvas.drawText("leftupy   =" + leftupy     ,0,360,p);

        canvas.drawText("rightupx   =" + rightupx     ,0,400,p);
        canvas.drawText("rightupy   =" + rightupy     ,0,420,p);

        canvas.drawText("upflg    =" + upflg      ,0,500,p);
        canvas.drawText("up1flg   =" + up1flg     ,0,460,p);
        canvas.drawText("up2flg   =" + up2flg     ,0,480,p);

        canvas.drawText("rightdx   =" + rightdx  ,0,540,p);
        canvas.drawText("rightdy   =" + rightdy     ,0,560,p);


//        canvas.drawText("leftdx   =" + leftdx     ,0,160,p);
//        canvas.drawText("leftdy   =" + leftdy     ,200,160,p);
//        canvas.drawText("lefttx   =" + lefttx     ,0,180,p);
//        canvas.drawText("leftty   =" + leftty    ,200,180,p);
//        canvas.drawText("touchx1 =" + touchx1  ,0,40,p);
//        canvas.drawText("touchy1 =" + touchy1  ,200,40,p);
//        canvas.drawText("touchx2 =" + touchx2  ,0,60,p);
//        canvas.drawText("touchy2 =" + touchy2  ,200,60,p);
//        canvas.drawText("downx1  =" + downx1   ,0,80,p);
//        canvas.drawText("downy1  =" + downy1   ,200,80,p);
//        canvas.drawText("downx2  =" + downx2   ,0,100,p);
//        canvas.drawText("downy2  =" + downy2   ,200,100,p);
//        canvas.drawText("down1flg=" + down1flg ,0,120,p);
//        canvas.drawText("down2flg=" + down2flg ,200,120,p);
//
//        canvas.drawText("down1flgisleft   =" + down1flgisleft     ,0,200,p);
//        canvas.drawText("down2flgisleft   =" + down2flgisleft    ,200,200,p);
//
//        canvas.drawText("up1flg   =" + up1flg     ,0,220,p);
//        canvas.drawText("up2flg   =" + up2flg    ,200,220,p);
    }

    public float GetDistance(float x1,float x2,float y1,float y2){

        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        distance =Math.abs(distance);

        return (float)distance;
    }

}
