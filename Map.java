package com.example.tankdedone;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Map {
    int map[][] = new int[100][100];
    int amap[][] = new int[100][100];
    int mapsizex = 0,mapsizey = 0;
    float screenx = 403f,screeny = 985f;
    float prescreenx,prescreeny;
    int radius = 96;
    Rect src = new Rect();
    Rect dst = new Rect();

    Matrix matrix= new Matrix();

    public void Draw(Canvas canvas, Bitmap mapchip){

        //dst.set((int)x-radius,(int)y-radius,(radius*2)+(int)x-radius,(radius*2)+(int)y-radius);
        //canvas.drawBitmap(mapchip,matrix,null);
        for(int i=0;i<mapsizex;i++) {
            for(int j =0;j<mapsizey;j++) {
                switch(map[i][j]){
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        src.set(64*map[i][j],0,64+(64*map[i][j]),64);
                        break;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        src.set(64*(map[i][j]-9),64,64+(64*(map[i][j]-9)),128);
                        break;
                    case 21:
                        src.set(0,128,64,192);
                        break;
                    case 22:
                        src.set(128,128,192,192);
                        break;

                }
                //if(map[i][j]==0) {
                    //src.set(64*map[i][j],0,64+(64*map[i][j]),64);
                    dst.set(i * (radius) - (int)screenx,j * (radius)- (int)screeny,
                            (radius)+i * (radius) - (int)screenx,(radius)+j * (radius)- (int)screeny);
                    canvas.drawBitmap(mapchip, src, dst, null);
                    //canvas.drawCircle(i * (radius)+radius/2, j * (radius)+radius/2,radius/2,paint);

                //}
            }
        }
    }
}
