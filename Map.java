package com.example.tankdedone;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Map {
    public int map[][] = new int[100][100];
    public int amap[][] = new int[100][100];
    public int mapsizex = 0,mapsizey = 0;
    public float screenx,screeny;
    public float screenMaxx,screenMaxy;
    public float prescreenx,prescreeny;
    public int radius = 96;
    Rect src = new Rect();
    Rect dst = new Rect();
    private float scaleX , scaleY;
    Matrix matrix= new Matrix();
    public RectF clearrect;
    String mapname;
    public float initankx,initanky;

    //１ブロックの大きさVertical　縦　side 横
    public float blockSide = 96f;
    public float blockVertical = 96f;

    private boolean onceinitflg = false;

    //
    private float HardSizeW ;
    private float HardSizeH ;

    //
    private float ViewWID ;
    private float ViewHEI ;

    public void Draw(Canvas canvas, Bitmap mapchip){

        //dst.set((int)x-radius,(int)y-radius,(radius*2)+(int)x-radius,(radius*2)+(int)y-radius);
        //canvas.drawBitmap(mapchip,matrix,null);
        for(int i=0;i<mapsizex;i++) {
            for(int j =0;j<mapsizey;j++) {
                if(i * (blockSide) - screenx > -blockSide && j * (blockVertical) - screeny > -blockVertical && i * (blockSide) - screenx < HardSizeW &&
                        j * (blockVertical) - screeny < HardSizeH) {

                    switch (map[i][j]) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            src.set(64 * map[i][j], 0, 64 + (64 * map[i][j]), 64);
                            break;
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            src.set(64 * (map[i][j] - 9), 64, 64 + (64 * (map[i][j] - 9)), 128);
                            break;
                        case 21:
                            src.set(0, 128, 64, 192);
                            break;
                        case 22:
                            src.set(128, 128, 192, 192);
                            break;

                    }
                    //if(map[i][j]==0) {
//                    //src.set(64*map[i][j],0,64+(64*map[i][j]),64);
//                    dst.set(i * (radius) - (int) screenx, j * (radius) - (int) screeny,
//                            (radius) + i * (radius) - (int) screenx, (radius) + j * (radius) - (int) screeny);
                    dst.set(i * ((int)blockSide) - (int) screenx, j * ((int)blockVertical) - (int) screeny,
                            (i + 1) * ((int)blockSide) - (int) screenx, (j + 1) * ((int)blockVertical) - (int) screeny);
//                    dst.left *= scaleX;
//                    dst.right *= scaleX;
//                    dst.top *= scaleY;
//                    dst.bottom *= scaleY;

                    canvas.drawBitmap(mapchip, src, dst, null);
                    //canvas.drawCircle(i * (radius)+radius/2, j * (radius)+radius/2,radius/2,paint);

                    //}
                }

            }
        }
    }

    public void Init(Context context,float _scaleX,float _scaleY,String map,float HardW,float HardH,float ViewW,float ViewH){
        mapname = map;
        scaleX = _scaleX;
        scaleY = _scaleY;
        ReadFile(context);
        HardSizeW = HardW;
        HardSizeH = HardH;
        ViewWID = ViewW;
        ViewHEI = ViewH;

        onceInit();

    }
    private void onceInit(){
        if(onceinitflg == false){
            onceinitflg = true;
            screenMaxx = (mapsizex - 1) * blockSide - (ViewWID - blockSide);
            screenMaxy = (mapsizey - 1) * blockVertical - (ViewHEI - blockVertical);
            screenMaxx *= scaleX;
            screenMaxy *= scaleY;

            blockSide *= scaleX;
            blockVertical *= scaleY;






//            HardSizeW *= scaleX;
//            HardSizeH *= scaleY;
        }
    }

    private void ReadFile(Context context){
        try{


            // AssetManagerクラスのインスタンスはcontext.getAssets()で取れます。
            // Contextが取れない場合getResources().getAssets()または
            // getContext().getAssets()でも取得できます。
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("m"+mapname+".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String str;
            StringBuilder strBuilder = new StringBuilder();
            int j =0;

            while ((str = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(str, ",");
                if(j == 0){
                    mapsizex = Integer.parseInt(st.nextToken());
                    mapsizey = Integer.parseInt(st.nextToken());
                    screenx = Integer.parseInt(st.nextToken());
                    screeny = Integer.parseInt(st.nextToken());
                    int cl = Integer.parseInt(st.nextToken());
                    int ct = Integer.parseInt(st.nextToken());
                    int cr = Integer.parseInt(st.nextToken());
                    int cb = Integer.parseInt(st.nextToken());
                    initankx = Integer.parseInt(st.nextToken());
                    initanky = Integer.parseInt(st.nextToken());



//                    mapsizex *= scaleX;
//                    mapsizey *= scaleY;
                    screenx *= scaleX;
                    screeny *= scaleY;
                    initankx *= scaleX;
                    initanky *= scaleY;



                    clearrect = new RectF((cl * 96) * scaleX,(ct * 96) * scaleX,(cr * 96) * scaleX,(cb * 96) * scaleX);

                }else {
                    for (int i = 0; i < mapsizex; i++) {
                        map[i][j-1] = Integer.parseInt(st.nextToken());
                    }
                }
                j++;
            }


            in = assetManager.open("a"+mapname+".txt");
            br = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            j =0;

            while ((str = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(str, ",");
                if(j == 0){
//                    map.mapsizex = Integer.parseInt(st.nextToken());
//                    map.mapsizey = Integer.parseInt(st.nextToken());
                }else {
                    for (int i = 0; i < mapsizex; i++) {
                        amap[i][j-1] = Integer.parseInt(st.nextToken());
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
//    public MainCharacter SetInitTankt(MainCharacter m,float x,float y){
//        m.tankx = x;
//        m.tanky = y;
//        return m;
//    }
}
