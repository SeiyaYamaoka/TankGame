package com.example.tankdedone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class MainActivity extends Activity implements OnTouchListener{

    private void log(String text){
        Log.d("**MainActivityのログ**", text);
    }

    private int mPointerID1, mPointerID2; // ポインタID記憶用
    Loop loop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        loop = new Loop(this);
        loop.setOnTouchListener(this);
        setContentView(loop);
    }
    @Override
    protected void onResume() {//表に行った
        super.onResume();
        loop.resume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    @Override
    protected void onPause() {//裏に行った
        super.onPause();
        loop.pause();

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventAction = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (eventAction) {
            //
            case MotionEvent.ACTION_DOWN:

                mPointerID1 = pointerId;
                mPointerID2 = -1;

                loop.touchoperation.downx1  = event.getX(event.findPointerIndex(mPointerID1));
                loop.touchoperation.downy1  = event.getY(event.findPointerIndex(mPointerID1));
                loop.touchoperation.down1flg = true;
                loop.touchoperation.down2flg = false;

                loop.touchoperation.touchx2 = -100;
                loop.touchoperation.touchy2 = -100;

                loop.touchoperation.up1flg = false;
                loop.touchoperation.up2flg = false;
                break;

            case MotionEvent.ACTION_POINTER_DOWN://二本目以降の指でタッチしたときに呼ばれる。
                if (mPointerID2 == -1) {
                    mPointerID2 = pointerId;
                    loop.touchoperation.downx2  = event.getX(event.findPointerIndex(mPointerID2));
                    loop.touchoperation.downy2  = event.getY(event.findPointerIndex(mPointerID2));
                    loop.touchoperation.down2flg = true;

                } else if (mPointerID1 == -1) {
                    mPointerID1 = pointerId;
                    loop.touchoperation.downx1  = event.getX(event.findPointerIndex(mPointerID1));
                    loop.touchoperation.downy1  = event.getY(event.findPointerIndex(mPointerID1));
                    loop.touchoperation.down1flg = true;

                }
                loop.touchoperation.up1flg = false;
                loop.touchoperation.up2flg = false;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                //log("action_pointer_up");
                if (mPointerID1 == pointerId) {
                    mPointerID1 = -1;
                    if(loop.touchoperation.down1flgisleft == false && loop.touchoperation.down2flgisleft){
                        loop.touchoperation.up1flg = true;
                    }
                    loop.touchoperation.down1flg = false;
                    loop.touchoperation.touchx1 = -100;
                    loop.touchoperation.touchy1 = -100;




                } else if (mPointerID2 == pointerId) {
                    mPointerID2 = -1;
                    if(loop.touchoperation.down1flgisleft &&loop.touchoperation.down2flgisleft == false){
                        loop.touchoperation.up2flg = true;
                    }
                    loop.touchoperation.down2flg = false;
                    loop.touchoperation.touchx2 = -100;
                    loop.touchoperation.touchy2 = -100;

                }
                break;

            case MotionEvent.ACTION_MOVE:

                // 指の座標の更新
                float x1 = 0.0f;
                float y1 = 0.0f;
                float x2 = 0.0f;
                float y2 = 0.0f;
                if (mPointerID1 >= 0) {
                    int ptrIndex = event.findPointerIndex(mPointerID1);
                    x1 = event.getX(ptrIndex);
                    y1 = event.getY(ptrIndex);
                }
                if (mPointerID2 >= 0) {
                    int ptrIndex = event.findPointerIndex(mPointerID2);
                    x2 = event.getX(ptrIndex);
                    y2 = event.getY(ptrIndex);
                }

                // ジェスチャー処理
                if (mPointerID1 >= 0 && mPointerID2 == -1) {
                    // 1本目の指だけが動いてる時の処理
                    //mTextView1.setText(String.format("pointer1: %3.1f, %3.1f", x1, y1));
                    loop.touchoperation.touchx1 = x1;
                    loop.touchoperation.touchy1 = y1;
                } else if (mPointerID1 == -1 && mPointerID2 >= 0) {
                    // 2本目の指だけが動いてる時の処理
                    //mTextView2.setText(String.format("pointer2: %3.1f, %3.1f", x2, y2));
                    loop.touchoperation.touchx2 = x2;
                    loop.touchoperation.touchy2 = y2;
                } else if (mPointerID1 >= 0 && mPointerID2 >= 0) {
                    // 1本目と2本目の指が動いてる時の処理
                    //mTextView1.setText(String.format("pointer1/2: %3.1f, %3.1f", x1, y1));
                    //mTextView2.setText(String.format("pointer2/2: %3.1f, %3.1f", x2, y2));
                    loop.touchoperation.touchx1 = x1;
                    loop.touchoperation.touchy1 = y1;
                    loop.touchoperation.touchx2 = x2;
                    loop.touchoperation.touchy2 = y2;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                //log("action_cancel");
                mPointerID1 = -1;
                mPointerID2 = -1;
                loop.touchoperation.down1flg = false;
                loop.touchoperation.touchx1 = -100;
                loop.touchoperation.touchy1 = -100;

                loop.touchoperation.down2flg = false;
                loop.touchoperation.touchx2 = -100;
                loop.touchoperation.touchy2 = -100;
                break;
            case MotionEvent.ACTION_UP:
                //log("action_up");
                mPointerID1 = -1;
                mPointerID2 = -1;
                if(loop.touchoperation.down1flgisleft == false ){
                    loop.touchoperation.up1flg = true;
                }
                loop.touchoperation.down1flg = false;
                loop.touchoperation.touchx1 = -100;
                loop.touchoperation.touchy1 = -100;

                loop.touchoperation.down2flg = false;
                loop.touchoperation.touchx2 = -100;
                loop.touchoperation.touchy2 = -100;

//                loop.up1flg = true;
//                loop.up2flg = true;


                break;


        }
        return true;
    }
}

