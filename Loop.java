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

//既知のバグ等
//ブロックに突っ込みながらアニメーションを実行させると、アニメーション位置が移動する。移動しない時もある
//bscreenxflg yflg　がおかしいと思われ。それかアニメーションクラスでmovex　yを使わないようにするか
//ブロックにぶつかると抜けることもあるため、ぶつかると体力HPを減らす仕様で考えてる。


public class Loop extends SurfaceView implements  Runnable, SurfaceHolder.Callback {

    //ログ取得用
    private void log(String text){
        Log.d("**Loopのログ**", text);
    }

    Thread renderThread = null;
    SurfaceHolder holder = null;
    boolean running = false;

    //画面サイズ
    private final float VIEW_WIDTH = 1280;
    private final float VIEW_HEIGHT = 720;

    //------------画像---------ビットマップ---------------------------
    //自機
    Bitmap tankbasebmp,tanktopbmp;
    //弾
    Bitmap tamabmp;
    //マップチップ
    Bitmap blockbmp;
    //爆発
    Bitmap explosionbmp;
    //煙
    Bitmap smokebmp;
    //---------------------------------------------------------

    //自機の座標
    float tankx = 650f,tanky = 370f;
    //前の自機座標
    float pretankx =650f,pretanky = 370f;

    //アニメーションのリスト
    //自機の前に描画
    ArrayList<Animation> pretankanimations = new ArrayList<>();
    //自機の後に描画
    ArrayList<Animation> reartankanimations = new ArrayList<>();

    //弾のリスト
    ArrayList<Bullet> bulletlists = new ArrayList<Bullet>();


    //弾がスクリーン移動に影響するかのフラグ アニメーションにも使用
    boolean bscreenxflg,bscreenyflg;

    //自機が移動する量
    double movex,movey;

    //自機の下部分の回転度数（移動する方向へ向きを変える）
    float tankbasedegree=0;
    //自機の上部分の回転度数　(弾を打つ方向へ向きを変える)
    float tanktopdegree=0;

    //マップ表示用クラス
    Map map = new Map();

    //操作用クラス
    TouchOperation touchoperation = new TouchOperation();
    //FPS制御用クラス
    FPS fps;

    //デバック表示用ペイント
    Paint back;
    Paint p;

    //コンストラクタ
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

            //タップ操作クラスの変数セット
            touchoperation.SetSide();

            //自機の処理
            //スクロールさせるかフラグ初期化
            bscreenxflg = false;
            bscreenyflg = false;
            //左のタップ操作
            touchoperation.leftoperation(this);



            //自機とブロックの当たり判定
            atari9();

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
                                    reartankanimations.add(a);

                                    bulletlists.remove(i);
                                    break BLoop;
                            }

                        }
                    }
                }

                //マップ外に出た場合に弾を消すにしたいけどこれ画面の外に出たら消える。
                if(bulletlists.get(i).x < -20 || bulletlists.get(i).x >= VIEW_WIDTH+20 || bulletlists.get(i).y < -20 || bulletlists.get(i).y >= VIEW_HEIGHT+20) {
                    bulletlists.remove(i);
                    break;
                }
            }
            //弾のアップデート
            for(int i = 0; i < bulletlists.size(); i++){

                bulletlists.get(i).Update(bscreenxflg,bscreenyflg,movex,movey);

            }

            if(Math.abs(movex) >= 0.1f && Math.abs(movey) >= 0.1f) {
                SmokeAni a = new SmokeAni();
                a.Init(tankx, tanky);
                pretankanimations.add(a);
            }
            //アニメーション更新 前
            for(int i = 0;i<pretankanimations.size();i++) {
                pretankanimations.get(i).Update(bscreenxflg,bscreenyflg,movex,movey);
                if(pretankanimations.get(i).Endflg == true){
                    pretankanimations.remove(i);
                }
            }
            //アニメーション更新　あと
            for(int i = 0;i<reartankanimations.size();i++) {
                reartankanimations.get(i).Update(bscreenxflg,bscreenyflg,movex,movey);
                if(reartankanimations.get(i).Endflg == true){
                    reartankanimations.remove(i);
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

            //弾
            for(int i = 0; i < bulletlists.size(); i++){
                bulletlists.get(i).Draw(canvas,tamabmp);
            }
            //アニメーション
            for(int i = 0;i<pretankanimations.size();i++) {
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
            //戦車描画
            canvas.drawBitmap(tankbasebmp,tankbasematrix,null);
            canvas.drawBitmap(tanktopbmp,tanktopmatrix,null);

            //あとアニメーション
            for(int i = 0;i<reartankanimations.size();i++) {
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

            //左が四角で右が〇
            touchoperation.drawpoint(canvas,p);

            //canvas.drawText("WIDTH:"+getWidth()+"HEIGHT:" + getHeight() ,0,40,p);
            fps.DrawFPS(canvas,p);
            debugtextdrow(canvas,p);
            touchoperation.debugdraw(canvas,p);


            Paint colpaint = new Paint();
            colpaint.setColor(Color.RED);
            colpaint.setAlpha(50);


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
//        canvas.drawText("hitflg[0]   =" + hitflg[0]     ,0,280,p);
//        canvas.drawText("hitflg[1]   =" + hitflg[1]     ,200,280,p);
//        canvas.drawText("hitflg[2]   =" + hitflg[2]     ,0,300,p);
//        canvas.drawText("hitflg[3]   =" + hitflg[3]     ,200,300,p);
        canvas.drawText("bscreenx   =" + bscreenxflg     ,0,280,p);
        canvas.drawText("bscreeny   =" + bscreenyflg    ,200,280,p);
        canvas.drawText("movex   =" + movex     ,0,320,p);
        canvas.drawText("movey   =" + movey     ,400,320,p);
        canvas.drawText("pretankx   =" + (tankx-pretankx)     ,0,340,p);
        canvas.drawText("pretanky   =" + (tanky-pretanky)     ,200,340,p);
        canvas.drawText("animetionsize   =" + pretankanimations.size()     ,0,360,p);
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
            //煙画像
            inputStream = assetManager.open("smoke 3.png");
            smokebmp = BitmapFactory.decodeStream(inputStream);
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

    
}
