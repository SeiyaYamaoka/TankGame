package com.example.tankdedone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.tankdedone.Scene.GameScene.GameScene;
import com.example.tankdedone.Scene.ResultScene.ResultScene;
import com.example.tankdedone.Scene.SceneBase;
import com.example.tankdedone.Scene.StageScene.StageScene;
import com.example.tankdedone.Scene.Title.TitleScene;

//既知のバグ等



public class Loop extends SurfaceView implements  Runnable, SurfaceHolder.Callback {

    public Point HARD_VIEW = new Point(1280,720);

    public float scaleX , scaleY;

    //ログ取得用
    private void log(String text){
        Log.d("**Loopのログ**", text);
    }

    Thread renderThread = null;
    SurfaceHolder holder = null;
    boolean running = false;



    public enum SceneNum{
        TITLESCENE,
        STAGESCENE,
        GAMESCENE,
        RESULTSCENE,

    }

    public SceneNum scenenum;

    //FPS制御用クラス
    FPS fps;

    //操作用クラス
    public TouchOperation touchoperation = new TouchOperation();

    //デバック表示用ペイント
    Paint back;
    Paint p;

    //シーン
    SceneBase scene;


    TitleScene titlescene;

    StageScene stagescene;

    GameScene gamescene;

    ResultScene resultscene;

    Fade fade;


    //コンストラクタ
    public Loop(Context context) {
        super(context);
        holder = getHolder();//画面画びょうが可能か調べられる
        holder.addCallback(this);

        titlescene = new TitleScene();
        stagescene = new StageScene();
        gamescene = new GameScene();
        resultscene = new ResultScene();

//        fade.SetHard(HARD_VIEW.x,HARD_VIEW.y);





        scene = titlescene;
        scene.setContext(context);



//        scene = resultscene;
//        scene.setContext(context);
//
//        scenenum = scenenum.RESULTSCENE;

        //resultscene.setScore(24,78,755);

        //ReadFile(context);


    }

    public void Init(){
        scenenum = scenenum.TITLESCENE;

        scene.setTouchope(touchoperation);
        scene.Init();
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

        fps = new FPS();


        //scene.Init();

        while(running) {
            fps.fpsStart();

            scene.Update();

            if(scene.nextflg){
                scene.nextflg = false;
                switch(scenenum){

                    case TITLESCENE:
                        Context c = scene.getContext();
                        scene = stagescene;
                        scene.setContext(c);
                        scene.setTouchope(touchoperation);
                        //scene.HARD_VIEW = HARD_VIEW;
                        scenenum = scenenum.STAGESCENE;

                        scene.Init();
                        break;

                    case STAGESCENE:
                        c = scene.getContext();
                        //gamescene.Set(scaleX,scaleY);
                        scene = gamescene;
                        scene.setContext(c);
                        scene.setTouchope(touchoperation);
                        scenenum = scenenum.GAMESCENE;

                        scene.Init();

                        break;
                    case GAMESCENE:
                        c = scene.getContext();
                        if(scene.nextscene == scene.TITLE) {

                            scene = titlescene;
                            scene.setContext(c);
                            scene.setTouchope(touchoperation);
                            scenenum = scenenum.TITLESCENE;

                        }else if(scene.nextscene == scene.STAGE){
                            scene = stagescene;
                            scene.setContext(c);
                            scene.setTouchope(touchoperation);
                            scenenum = scenenum.STAGESCENE;

                        }else if(scene.nextscene == scene.RESULT){

                            scene = resultscene;
                            scene.setContext(c);
                            scene.setTouchope(touchoperation);
                            scenenum = scenenum.RESULTSCENE;
                            resultscene.setScore(gamescene.nclearhp,gamescene.ncleartime,gamescene.nclearscore);
                        }

                        scene.Init();

                        break;

                    case RESULTSCENE:

                        c = scene.getContext();
                        if(scene.nextscene == scene.TITLE) {

                            scene = titlescene;
                            scene.setContext(c);
                            scene.setTouchope(touchoperation);
                            scenenum = scenenum.TITLESCENE;

                        }
                        scene.Init();


                        break;

                }
            }

            if (!holder.getSurface().isValid())//描画------------------------------------------------------------------------------------ーーーーーーーー
                continue;

            Canvas canvas = holder.lockCanvas();//①②とセット　間に描画命令を書く　ロックしたらアンロックする
            //背景
            //canvas.drawRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT, back);

            scene.Draw(canvas);

            fps.DrawFPS(canvas,p);



            holder.unlockCanvasAndPost(canvas);//描画ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー

            fps.fpsSleep();
        }

    }
    public void resume() {
        scene.onResume();

        running = true;
        renderThread = new Thread(this);
        renderThread.start();

    }

    //裏に回るときにサーフェースがすべて失われる。
    public void pause() {
        scene.onPause();

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


    
}
