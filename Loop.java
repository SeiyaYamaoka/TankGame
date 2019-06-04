package com.example.tankdedone;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Loop extends SurfaceView implements  Runnable, SurfaceHolder.Callback {

    private void log(String text){
        Log.d("**Loopのログ**", text);
    }

    Thread renderThread = null;
    SurfaceHolder holder = null;
    boolean running = false;

    private final float VIEW_WIDTH = 1280;
    private final float VIEW_HEIGHT = 720;



    Bitmap tankbasebmp,tanktopbmp;
    Bitmap tamabmp;
    Bitmap blockbmp;
    Bitmap explosionbmp;

    float tankx = 650f,tanky = 370f;
    float pretankx =650f,pretanky = 270f;//前の自機座標

    ArrayList<Animation> animations = new ArrayList<>();

    ArrayList<Bullet> bulletlists = new ArrayList<Bullet>();
    //弾がスクリーン移動に影響するかのフラグ
    boolean bscreenxflg,bscreenyflg;
    //自機が移動する量
    double movex,movey;

    float tankbasedegree=0;
    float tanktopdegree=0;

    boolean hitflg[] = new boolean[4]; //0左上　1右上　2右下　3左下

    int prebackb = 0;

    Map map = new Map();
    TouchOperation touchoperation = new TouchOperation();

    public Loop(Context context) {
        super(context);
        holder = getHolder();//画面画びょうが可能か調べられる
        holder.addCallback(this);
        LoadImage(context);
        ReadFile(context);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    FPS fps;
    Paint back;
    Paint p;

    //Bullet bull;
    @Override
    public void run() {
        back = new Paint();
        back.setColor(Color.BLACK);

        p = new Paint();
        p.setTextSize(20);
        p.setColor(Color.BLACK);

        Matrix tankbasematrix= new Matrix();
        tankbasedegree=0;
        Matrix tanktopmatrix= new Matrix();



        fps = new FPS();


        while(running) {
            fps.fpsStart();
            tankbasematrix.reset();


            tanktopmatrix.reset();

            movex=0f;
            movey=0f;

            //
            touchoperation.SetSide();


            //自機の処理
            //スクロールさせるかフラグ初期化
            bscreenxflg = false;
            bscreenyflg = false;
            //左のタップ操作
            touchoperation.leftoperation(this);



            //自機とブロックの当たり判定
            atari9();
            //画面スクロール
            //screenscroll();



            //右のタップ操作 照準を合わせる。上の部分が回転する。
            touchoperation.rightoperation(this);

            //弾の発射
            //log(""+ bulletlists.size());
            if(touchoperation.up1flg ||touchoperation.up2flg){
                touchoperation.up1flg = false;
                touchoperation.up2flg = false;
                Bullet b = new Bullet();
                b.Init(tankx,tanky,tanktopdegree);
                bulletlists.add(b);

            }
            //弾を消す処理
            BLoop:for(int i = 0; i < bulletlists.size(); i++){
                for(int mj=0;mj<map.mapsizex;mj++) {
                    for (int mk = 0; mk < map.mapsizey; mk++) {
                        if (map.amap[mj][mk] == 1) {
                            //マップに接触したとき爆発させる。
                            if(bulletlists.get(i).x - 8 < (mj * (map.radius) + 96 - map.screenx) && bulletlists.get(i).x + 8 > (mj * map.radius) -  map.screenx
                                && bulletlists.get(i).y - 8 < (mk * (map.radius) + 96 - map.screeny) && bulletlists.get(i).y + 8 > (mk * map.radius) -  map.screeny){

                                    ExplosionAni a = new ExplosionAni();
                                    a.Init(bulletlists.get(i).x,bulletlists.get(i).y );
                                    animations.add(a);

                                    bulletlists.remove(i);
                                    break BLoop;
                            }

                        }
                    }
                }

                //マップ外に出た場合に弾を消す
                if(bulletlists.get(i).x < -20 || bulletlists.get(i).x >= VIEW_WIDTH+20 || bulletlists.get(i).y < -20 || bulletlists.get(i).y >= VIEW_HEIGHT+20) {
                    bulletlists.remove(i);
                    break;
                }
            }
            //弾のアップデート
            for(int i = 0; i < bulletlists.size(); i++){

                bulletlists.get(i).Update(bscreenxflg,bscreenyflg,movex,movey);

            }

            //アニメーション更新
            for(int i = 0;i<animations.size();i++) {
                animations.get(i).Update(bscreenxflg,bscreenyflg,movex,movey);
                if(animations.get(i).Endflg == true){
                    animations.remove(i);
                }
            }

            tankbasematrix.postScale(1.5f,1.5f);
            tankbasematrix.postTranslate(-48,-48);
            tankbasematrix.postRotate(tankbasedegree);
            tankbasematrix.postTranslate((int)tankx,(int)tanky);

            tanktopmatrix.postScale(1.5f,1.5f);
            tanktopmatrix.postTranslate(-48,-48);
            tanktopmatrix.postRotate(tanktopdegree);
            tanktopmatrix.postTranslate((int)tankx,(int)tanky);



            if (!holder.getSurface().isValid())//描画------------------------------------------------------------------------------------ーーーーーーーー
                continue;

            Canvas canvas = holder.lockCanvas();//①②とセット　間に描画命令を書く　ロックしたらアンロックする
            //背景
            canvas.drawRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT, back);
            map.Draw(canvas,blockbmp);
            //戦車描画
            canvas.drawBitmap(tankbasebmp,tankbasematrix,null);
            canvas.drawBitmap(tanktopbmp,tanktopmatrix,null);
            //弾
            for(int i = 0; i < bulletlists.size(); i++){
                bulletlists.get(i).Draw(canvas,tamabmp);
            }
            //爆発アニメーション
            for(int i = 0;i<animations.size();i++) {
                animations.get(i).Draw(canvas,explosionbmp);
            }

            //左が四角で右が〇
            touchoperation.drawpoint(canvas,p);

            //canvas.drawText("WIDTH:"+getWidth()+"HEIGHT:" + getHeight() ,0,40,p);
            fps.DrawFPS(canvas,p);
//            debugtextdrow(canvas,p);
//            touchoperation.debugdraw(canvas,p);


            Paint colpaint = new Paint();
            colpaint.setColor(Color.RED);
            colpaint.setAlpha(50);
//            canvas.drawRect((tankx-48), (tanky-48), (tankx+48), (tanky+48), colpaint);
//            canvas.drawRect((tankx+36), (tanky-36), (tankx+48), (tanky+36), p);
//            colpaint.setColor(Color.GREEN);
//            colpaint.setAlpha(50);
//
//            canvas.drawRect((tankx-(float)movex-48), (tanky-(float)movey-48), (tankx-(float)movex+48), (tanky - (float)movey+48), colpaint);
//            colpaint.setColor(Color.YELLOW);
//            colpaint.setAlpha(50);
//
//            canvas.drawRect((tankx-38), (tanky-38), (tankx+38), (tanky+38), colpaint);
//            canvas.drawRect((tankx-36), (tanky-36), (tankx-48), (tanky+36), p);

//            canvas.drawRect((tankx-48), (tanky-48), (tankx-36), (tanky-36), downp2);
//            canvas.drawRect((tankx+36), (tanky-48), (tankx+48), (tanky-36), downp2);
//            canvas.drawRect((tankx+36), (tanky+36), (tankx+48), (tanky+48), downp2);
//            canvas.drawRect((tankx-48), (tanky+36), (tankx-36), (tanky+48), downp2);
//
//            canvas.drawRect((tankx-36), (tanky-48), (tankx+36), (tanky-36), downp1);
//            canvas.drawRect((tankx+36), (tanky-36), (tankx+48), (tanky+36), downp1);
//            canvas.drawRect((tankx-36), (tanky+36), (tankx+36), (tanky+48), downp1);
//            canvas.drawRect((tankx-48), (tanky-36), (tankx-36), (tanky+36), downp1);

//            canvas.drawCircle((tankx+48),(tanky+48),3,p);
//            canvas.drawCircle((tankx-48),(tanky-48),3,p);
//            canvas.drawCircle((tankx+48),(tanky-48),3,p);
//            canvas.drawCircle((tankx-48),(tanky+48),3,p);

//            canvas.drawCircle(tankx + (float)(Math.cos((tankbasedegree - 90f)/180 * Math.PI)*48),tanky + (float)(Math.sin((tankbasedegree - 90f)/180 * Math.PI)*48),3,p);
//            canvas.drawCircle(tankx + (float)(Math.cos((tankbasedegree - 0f)/180 * Math.PI)*48),tanky + (float)(Math.sin((tankbasedegree - 0f)/180 * Math.PI)*48),3,p);
//            canvas.drawCircle(tankx + (float)(Math.cos((tankbasedegree - 180f)/180 * Math.PI)*48),tanky + (float)(Math.sin((tankbasedegree - 180f)/180 * Math.PI)*48),3,p);
//            canvas.drawCircle(tankx + (float)(Math.cos((tankbasedegree - 270f)/180 * Math.PI)*48),tanky + (float)(Math.sin((tankbasedegree - 270f)/180 * Math.PI)*48),3,p);


//            canvas.drawCircle((tankx),(tanky+48),3,p);
//            canvas.drawCircle((tankx),(tanky-48),3,p);
//            canvas.drawCircle((tankx+48),(tanky),3,p);
//            canvas.drawCircle((tankx-48),(tanky),3,p);

            //canvas.drawLine(pretankx, pretanky, tankx, tanky, p);

            //canvas.drawCircle((tankx),(tanky),48,downp1);
//
//            for(float aa = tankx - 48; aa< tankx+47; aa+=0.9f) {
//                canvas.drawRect((aa), (tanky-48), (aa+1), (tanky-36), downp1);
//            }

//            for(int i=0;i<map.mapsizex;i++) {
//                for (int j = 0; j < map.mapsizey; j++) {
//                    if(map.amap[i][j] == 1) {
//                        canvas.drawCircle((i * map.radius) - (int) map.screenx, (j * map.radius) - (int) map.screeny, 5, downp2);
//                        canvas.drawCircle((i * (map.radius) +48 - (int)map.screenx), (j * (map.radius) +48 - (int)map.screeny)  , 5, downp1);
//                    }
//                }
//            }


            holder.unlockCanvasAndPost(canvas);//描画ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
            pretankx = tankx;
            pretanky = tanky;
            map.prescreenx = map.screenx;
            map.prescreeny = map.screeny;
            fps.fpsSleep();
        }

    }
    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();

    }

    //裏に回るときにサーフェースがすべて失われる。
    public void pause() {

        running = false;//ゲームループ終了
        while(true) {//無限ループ
            try{
                renderThread.join();//スレッドが終了していたらおｋで次に行くが途中だった場合下のエラーが出る
                break;
            } catch (InterruptedException e) {
                //例外が発生した場合はもう一度 renderThread.join(); をリトライ
            }
        }
    }//ここで裏に回る。このまま表示処理が働くと落ちるので無限ループでrunが終わるまで回す

    private void debugtextdrow(Canvas canvas, Paint p){
        //canvas.drawText("fps=" + fps ,0,20,p);

        canvas.drawText("tankx   =" + tankx     ,0,140,p);
        canvas.drawText("tanky   =" + tanky     ,200,140,p);



        canvas.drawText("map.mapsizex   =" + map.mapsizex     ,0,240,p);
        canvas.drawText("map.mapsizey   =" + map.mapsizey    ,200,240,p);
        canvas.drawText("map.screenx   =" + map.screenx     ,0,260,p);
        canvas.drawText("map.screeny   =" + map.screeny     ,300,260,p);
        canvas.drawText("hitflg[0]   =" + hitflg[0]     ,0,280,p);
        canvas.drawText("hitflg[1]   =" + hitflg[1]     ,200,280,p);
        canvas.drawText("hitflg[2]   =" + hitflg[2]     ,0,300,p);
        canvas.drawText("hitflg[3]   =" + hitflg[3]     ,200,300,p);

        canvas.drawText("movex   =" + movex     ,0,320,p);
        canvas.drawText("movey   =" + movey     ,400,320,p);
        canvas.drawText("pretankx   =" + (tankx-pretankx)     ,0,340,p);
        canvas.drawText("pretanky   =" + (tanky-pretanky)     ,200,340,p);
        canvas.drawText("animetionsize   =" + animations.size()     ,0,360,p);
//        loop.up1flg = false;
//        loop.up2flg = false;
       // canvas.drawText("ONE_MILLI_TO_NANO=" + ONE_MILLI_TO_NANO,0,140,p);

    }
    private void LoadImage(Context context){

        try{
            //画像読み込み
            AssetManager assetManager = null;
            InputStream inputStream = null;
            assetManager = context.getAssets();
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
            inputStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }finally{
        }
        setFocusable(true);
    }

    private void ReadFile(Context context){
        try{
            // AssetManagerクラスのインスタンスはcontext.getAssets()で取れます。
            // Contextが取れない場合getResources().getAssets()または
            // getContext().getAssets()でも取得できます。
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("m01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String str;
            StringBuilder strBuilder = new StringBuilder();
            int j =0;

            while ((str = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(str, ",");
                if(j == 0){
                    map.mapsizex = Integer.parseInt(st.nextToken());
                    map.mapsizey = Integer.parseInt(st.nextToken());
                }else {
                    for (int i = 0; i < map.mapsizex; i++) {
                        map.map[i][j-1] = Integer.parseInt(st.nextToken());
                    }
                }
                j++;
            }
//
//            in.close();
//            br.close();

            in = assetManager.open("a01.txt");
            br = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            j =0;

            while ((str = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(str, ",");
                if(j == 0){
//                    map.mapsizex = Integer.parseInt(st.nextToken());
//                    map.mapsizey = Integer.parseInt(st.nextToken());
                }else {
                    for (int i = 0; i < map.mapsizex; i++) {
                        map.amap[i][j-1] = Integer.parseInt(st.nextToken());
                        //log("" + map.amap[i][j-1]);
                    }
                }
                j++;
            }

            in.close();
            br.close();


        }catch (Exception e){
            e.printStackTrace();
        }

    }




    public void atari9(){
        boolean hitxflg = false;
        boolean hityflg = false;
        float ntankx = tankx + (float)movex;
        float ntanky = tanky + (float)movey;


        for(int i=0;i<map.mapsizex;i++) {
            for (int j = 0; j < map.mapsizey; j++) {
                if(map.amap[i][j] == 1) {

                    if(Math.abs(movex) >= 0) {

                        if ((ntankx - 38) < (i * (map.radius) + 96 - map.screenx) && (ntankx + 38) > ((i * map.radius) -  map.screenx)
                                && (tanky - 38) < (j * (map.radius) + 96 - map.screeny) && (tanky + 38) > ((j * map.radius) -  map.screeny)) {
                            if(ntankx > i*map.radius + 48 - (int) map.screenx){
                                //右側に自機がいるから左に押し出す

                                tankx = ((i * map.radius) + 96 - (int) map.screenx) + 38;
                                //map.screenx = (i) * map.radius ;
                            }else{

                                tankx = ((i * map.radius) - (int) map.screenx) - 39;
                            }


                            //j * map.radius + 48;
                            hitxflg = true;
                        }

                    }
                    if(Math.abs(movey) >= 0) {
                        if ((tankx - 38) < (i * (map.radius) + 96 - map.screenx) && (tankx + 38) > ((i * map.radius) -  map.screenx)
                                && (ntanky - 38) < (j * (map.radius) + 96 - map.screeny) && (ntanky + 38) > ((j * map.radius) -  map.screeny)) {

                            if(ntanky > j * map.radius + 48 - (int) map.screeny){

                                tanky = (j * (map.radius) + 96 - (int) map.screeny) + 38;

                            }else{
                                //left
                                tanky = ((j * map.radius) - (int) map.screeny) - 39;
                            }
                            //map.screeny = (j - 1) * map.radius ;
                            hityflg = true;
                        }
                    }
                }
            }
        }
        if(hitxflg== false){
            tankx = ntankx;
        }else{

        }
        if(hityflg == false){
            tanky = ntanky;
        }else{

        }
//        log("x:"+(tankx-pretankx) +":movex:" + movex);
//        log("y:"+(tanky-pretanky) +":movey:" + movey);
        //画面スクロール
        //
        screenscroll(hitxflg,hityflg);



    }


    //画面のスクロールの処理
    public void screenscroll(boolean hitmovex,boolean hitmovey){

        if (map.screenx <= 0) {
            map.screenx = 0;
            if (tankx <= 48) {
                tankx = 48;
            }
        } else {
            if (tankx <= 600) {
                if(hitmovex == false) {
                    map.screenx += movex;
                }else{
                    //map.screenx += (tankx-pretankx);
                }
                bscreenxflg = true;
                tankx = 600;
            }
        }
        if (map.screeny <= 0) {
            map.screeny = 0;
            if (tanky <= 48) {
                tanky = 48;
            }
        } else {
            if (tanky <= 340) {
                if(hitmovey == false) {
                    map.screeny += movey;

                }else{
                    //map.screeny += (tanky-pretanky);
                }
                bscreenyflg = true;
                tanky = 340;
            }
        }

        if (map.screenx >= (map.mapsizex - 1) * map.radius - (VIEW_WIDTH - map.radius)) {
            map.screenx = (map.mapsizex - 1) * map.radius - (VIEW_WIDTH - map.radius);
            if (tankx >= VIEW_WIDTH - 48) {
                tankx = VIEW_WIDTH - 48;
            }
        } else {
            if (tankx >= 680) {
                if(hitmovex == false) {
                    map.screenx += movex;

                }else{
                   // map.screenx += (tankx-pretankx);
                }
                bscreenxflg = true;
                tankx = 680;
            }
        }
        if (map.screeny >= (map.mapsizey - 1) * map.radius - (VIEW_HEIGHT - map.radius)) {
            map.screeny = (map.mapsizey - 1) * map.radius - (VIEW_HEIGHT - map.radius);
            if (tanky >= VIEW_HEIGHT - 48) {
                tanky = VIEW_HEIGHT - 48;
            }
        } else {
            if (tanky >= 380) {
                if(hitmovey == false) {
                    map.screeny += movey;

                }else{
                   // map.screeny += (tanky-pretanky);
                }
                bscreenyflg = true;
                tanky = 380;
            }
        }

    }

    public int min(float a,float b,float c,float d){
        float min = a;
        int ret = 0;
        if(min > b){
            min = b;
            //log("b");
            ret = 1;
        }
        if(min > c){
            min = c;
            //log("c");
            ret =2;
        }
        if(min > d){
            min = d;
            //log("d");
            ret = 3;
        }

        return ret;
    }
    public int max(int a,int b,int c,int d){

//        int[] hoge = new int[4];
//        hoge[0] = a;
//        hoge[1] = b;
//        hoge[2] = c;
//        hoge[3] = d;
//
//        for(int i = 0;i<hoge.length-1;i++){
//            for(int j = hoge.length-1; j>i; j--){
//                if(hoge[j]<hoge[j-1]){
//                    int t=hoge[j];
//                    hoge[j]=hoge[j-1];
//                    hoge[j-1]=t;
//                }
//            }
//        }
//        log("hoge"+hoge.length);
////
//        log("h"+hoge[0]+hoge[1]+hoge[2]+hoge[3]);
//
//        return hoge[0];
        int max = a;
        int ret = 0;
        boolean iqflg = false;

        if(max <= b){
            if(max == b && b != 0){
                iqflg = true;
            }
            max = b;
            //log("b");
            ret = 1;

        }
        if(max < c){
            if(max == c && c != 0){
                iqflg = true;
            }
            max = c;
            //log("c");
            ret =2;
        }
        if(max < d){
            if(max == d && d != 0){
                iqflg = true;
            }
            max = d;
            //log("d");
            ret = 3;
        }
        if(iqflg) {
            //log("hi");
            return prebackb;
        }else{
            return ret;
        }

    }
    public int max(float a,float b,float c,float d){
        float max = a;
        int ret = 0;

        if(max < b){
            max = b;
            //log("b");
            ret = 1;

        }
        if(max < c){
            max = c;
            //log("c");
            ret =2;
        }
        if(max < d){
            max = d;
            //log("d");
            ret = 3;
        }
        return ret;
    }
    
}
