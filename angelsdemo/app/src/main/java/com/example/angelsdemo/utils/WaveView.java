package com.example.angelsdemo.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


import com.angels.util.ACLogUtil;


public class WaveView extends View {
    private float XPoint = 60;
    private float YPoint = 260;
    private float XScale = 8;  //刻度长度
    private float YScale = 40;
    private float XLength = 800;
    private float YLength = 240;
    private int MaxDataSize = (int) (XLength / XScale);
    private List<Float> data = new ArrayList<Float>();

    private String[] YLabel;
    /**是否已经初始化了*/
    boolean isInit = false;

    private int maxY = 0;
    private int lastNum = 0;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == 0x1234){
                WaveView.this.invalidate();
            }
        };
    };
    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        init();
//        private int XPoint = 60;
//        private int YPoint = 260;
//        private int XScale = 8;  //刻度长度
//        private int YScale = 40;
//        private int XLength = 800;
//        private int YLength = 240;
//        private int MaxDataSize = XLength / XScale;

    }

    private void init(){
        XPoint = 60;
        YPoint = getHeight() - 10;
        XLength = getWidth() - 100;
        YLength = getHeight() - 20;
        YScale = YLength/5; //100分贝 每格20分贝
        XScale = XLength/150; //15秒的长度时间 （每100毫秒刷新一次）
        MaxDataSize = (int) (XLength / XScale);
        YLabel = new String[(int) (YLength / YScale)];

        for(int i=0; i<YLabel.length; i++){
            YLabel[i] = i * 20 + "db";
        }
        ACLogUtil.i("初始化的数据-->YPoint:"+YPoint+"--XLength:"+XLength+"--YLength:"+YLength+"--"+YScale+"--XScale:"+XScale+"--MaxDataSize："+MaxDataSize);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if(data.size() >= MaxDataSize){
//                        data.remove(data.size()-1);
//                    }
//                    data.add(0,new Random().nextInt(5));
//                    handler.sendEmptyMessage(0x1234);
//                }
//            }
//        }).start();
        isInit = true;
    }
    /**
     *    动态添加点
     * @param db 分贝
     */
    public void setVolume(float db){
        if(data.size() >= MaxDataSize){
            data.remove(data.size()-1);
        }
        System.out.println("声音--实际>"+db + "--" +YLength + "--最大容量"+MaxDataSize);
//        data.add(0,YLength*db/100);
//        6460974
//        int h = ((int)db)%220;
//        if(h < 35){
//            data.add(0,YLength*h/50);
//        }else{
//            data.add(0,0f);
//        }
        float difference = Math.abs(db - lastNum);
//        if(lastNum =){
//
//        }
        maxY = 1000;
//        lastNum =
        int h = ((int)db)%10000/100;
        if(db > 6000000 && h < maxY){
            data.add(0, (float) h);
        }else{
            data.add(0,0f);
        }

        handler.sendEmptyMessage(0x1234);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInit){
            return;
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); //去锯齿
        paint.setColor(Color.WHITE);

        //画Y轴
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);

        //Y轴箭头
        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint-YLength + 6, paint);  //箭头
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint-YLength + 6 ,paint);

        //添加刻度和文字
        for(int i=0; i * YScale < YLength; i++) {
            canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i * YScale, paint);  //刻度
            canvas.drawText(YLabel[i], XPoint - 50, YPoint - i * YScale, paint);//文字
        }

        //画X轴
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);
        System.out.println("Data.size = " + data.size());
        if(data.size() > 1){
            for(int i=1; i<data.size(); i++){
//                YLength*h/7000
                if(maxY != 0){
                    canvas.drawLine(XPoint + (i-1) * XScale, YPoint - YLength*data.get(i-1)/maxY ,
                            XPoint + i * XScale, YPoint - YLength*data.get(i)/maxY, paint);
                }else{
                    canvas.drawLine(XPoint + (i-1) * XScale, YPoint - 0 ,
                            XPoint + i * XScale, YPoint - 0, paint);
                }

//                canvas.drawLine(XPoint + (i-1) * XScale, YPoint - data.get(i-1) ,
//                        XPoint + i * XScale, YPoint - data.get(i), paint);
            }
        }
    }
}
