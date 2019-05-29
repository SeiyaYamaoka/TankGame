package com.example.tankdedone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TouchOperation {
    float touchx1 = -100f,touchy1 = -100f;
    float touchx2 = -100f,touchy2 = -100f;
    float downx1 = -100f,downy1 = -100f;
    float downx2 = -100f ,downy2 = -100f;
    boolean down1flg=false,down2flg=false;
    boolean down1flgisleft = false,down2flgisleft;
    boolean up1flg = false,up2flg = false;

    float leftdx = -100f,leftdy= -100f;
    float lefttx= -100f,leftty= -100f;
    float rightdx= -100f,rightdy= -100f;
    float righttx= -100f,rightty= -100f;

    Paint touchp1,touchp2;
    Paint downp1,downp2;
    
    TouchOperation(){
        drawinit();
    }
    


    public void SetSide(){
        //タップしたのが左か右かセット
        if(down1flg && down2flg == false) {
            if (downx1 < 640) {
                down1flgisleft = true;
                down2flgisleft = false;
            } else {
                down1flgisleft = false;
                down2flgisleft = true;
            }
        }else if(down1flg == false && down2flg){
            if (downx2 < 640) {
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
    }
    
    public void rightoperation(Loop l){
        if (down1flg && down1flgisleft == false || down2flg && down2flgisleft == false) {
            double radian = Math.atan2(rightty - rightdy,righttx - rightdx);
            double degree = radian * 180d / Math.PI;
            l.tanktopdegree = (float)degree + 90f;
            //double degree = 90f;
            //tanktopmatrix.postRotate((float)degree);
            //log("degree:" + degree);
        }

    }
    
    public void leftoperation(Loop l){
        if(lefttx > -1 && leftty > -1) {
            if (down1flgisleft || down2flgisleft) {
                //log(""+lefttx + "   " + leftty + "   " + leftdy + "   " + leftdx);

                //0でいろいろするとNaNになるからそれよけ。
                if (lefttx != leftdx && leftty != leftdy) {

                    double pow, sqr, speed = 6.0f;
                    pow = Math.pow((lefttx - leftdx), 2) + Math.pow((leftty - leftdy), 2);
                    sqr = Math.sqrt(pow);
                    //double movex,movey;
                    l.movex = ((lefttx - leftdx) / sqr) * speed;
                    l.movey = ((leftty - leftdy) / sqr) * speed;
//                        tankx += movex;
//                        tanky += movey;

                    double radian = Math.atan2(leftty - leftdy, lefttx - leftdx);
                    double degree = radian * 180d / Math.PI;
                    l.tankbasedegree = (float) degree + 90f;


                }
            }
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


    public void drawpoint(Canvas canvas ,Paint p){
        if(down1flg && down2flg == false) {
            if (down1flgisleft) {
                //canvas.drawCircle(downx1,downy1,30,downp1);
                //canvas.drawCircle(touchx1,touchy1,30,touchp1);
                canvas.drawRect(downx1 - 32, downy1 - 32, downx1 + 32, downy1 + 32, downp1);
                canvas.drawRect(touchx1 - 32, touchy1 - 32, touchx1 + 32, touchy1 + 32, touchp1);
            } else if (down2flgisleft) {
                canvas.drawCircle(downx1, downy1, 30, downp2);
                canvas.drawCircle(touchx1, touchy1, 30, touchp2);
            }
        }else if(down1flg == false && down2flg){
            if (down1flgisleft) {
                canvas.drawCircle(downx2, downy2, 30, downp2);
                canvas.drawCircle(touchx2, touchy2, 30, touchp2);

            } else if (down2flgisleft) {
                canvas.drawRect(downx2 - 32, downy2 - 32, downx2 + 32, downy2 + 32, downp1);
                canvas.drawRect(touchx2 - 32, touchy2 - 32, touchx2 + 32, touchy2 + 32, touchp1);
            }
        } else if(down1flg && down2flg){

            canvas.drawRect(leftdx - 32, leftdy-32, leftdx+32,leftdy+32, downp1);
            canvas.drawRect(lefttx-32,leftty-32, lefttx+32,leftty+32, touchp1);

            canvas.drawCircle(rightdx,rightdy,30,downp2);
            canvas.drawCircle(righttx,rightty,30,touchp2);
        }
 
    }


    public void debugdraw(Canvas canvas , Paint p){
        canvas.drawText("leftdx   =" + leftdx     ,0,160,p);
        canvas.drawText("leftdy   =" + leftdy     ,200,160,p);
        canvas.drawText("lefttx   =" + lefttx     ,0,180,p);
        canvas.drawText("leftty   =" + leftty    ,200,180,p);
        canvas.drawText("touchx1 =" + touchx1  ,0,40,p);
        canvas.drawText("touchy1 =" + touchy1  ,200,40,p);
        canvas.drawText("touchx2 =" + touchx2  ,0,60,p);
        canvas.drawText("touchy2 =" + touchy2  ,200,60,p);
        canvas.drawText("downx1  =" + downx1   ,0,80,p);
        canvas.drawText("downy1  =" + downy1   ,200,80,p);
        canvas.drawText("downx2  =" + downx2   ,0,100,p);
        canvas.drawText("downy2  =" + downy2   ,200,100,p);
        canvas.drawText("down1flg=" + down1flg ,0,120,p);
        canvas.drawText("down2flg=" + down2flg ,200,120,p);

        canvas.drawText("down1flgisleft   =" + down1flgisleft     ,0,200,p);
        canvas.drawText("down2flgisleft   =" + down2flgisleft    ,200,200,p);

        canvas.drawText("up1flg   =" + up1flg     ,0,220,p);
        canvas.drawText("up2flg   =" + up2flg    ,200,220,p);
    }
}
