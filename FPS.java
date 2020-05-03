package com.example.tankdedone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by yanoka on 2017/07/14.
 */
public class FPS {
        private void log(String text){
            Log.d("**fpsのログ**", text);
        }
        private static final long ONE_SEC_TO_NANO = TimeUnit.SECONDS.toNanos(1L);
        private static final long ONE_MILLI_TO_NANO = TimeUnit.MILLISECONDS.toNanos(1L);
        private static final long MAX_FPS = 60;
        long startTime;
        long elapsedTime;
        long sleepTime;
        long oneCycle = ONE_SEC_TO_NANO / MAX_FPS;
        int fps;

        int cnt = 0;
        long etime = 0;
        int cntm = 0;


        public void fpsStart(){
            startTime = System.nanoTime();
            cnt++;

      }
        public void fpsSleep(){
            elapsedTime = System.nanoTime() - startTime;
            sleepTime = oneCycle - elapsedTime;

             //sleepTimeが1ミリ秒未満でも1ミリ秒はsleepさせる
            if (sleepTime < ONE_MILLI_TO_NANO) {
                sleepTime = ONE_MILLI_TO_NANO;
            }
            fps = (int)(ONE_SEC_TO_NANO / (elapsedTime + sleepTime));
            //log("fps=" + fps + "+ elapsedTime=" + elapsedTime + "+ sleepTime" + sleepTime);
            try {
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    public void DrawFPS(Canvas canvas, Paint p){
        canvas.drawText("fps=" + fps ,0,200,p);
//        canvas.drawText("startTime=" + startTime ,0,40,p);
        //canvas.drawText("elapsedTime=" + elapsedTime ,0,60,p);
//        if(cnt%60==0){
//            cntm++;
//            etime += elapsedTime;
//            log("et" + etime + "cntm" + cntm);
            //log("elapsedTime=" + elapsedTime);
  //      }

//        canvas.drawText("oneCycle=" + oneCycle ,0,80,p);
//        canvas.drawText("sleepTime=" + sleepTime ,0,100,p);
//        canvas.drawText("ONE_SEC_TO_NANO=" + ONE_SEC_TO_NANO ,0,120,p);
//        canvas.drawText("ONE_MILLI_TO_NANO=" + ONE_MILLI_TO_NANO,0,140,p);
//        canvas.drawText("MAX_FPS=" + MAX_FPS,0,160,p);
            //long  fpsl = elapsedTime/1000000000;//一秒の計測
         //canvas.drawText("fps=" + cntmae ,0,20,p);
//        if(fpsl>etime){
//            cntmae = cnt;
//            cnt = 0;
//            etime = fpsl + 1;
//        }
    }



}
