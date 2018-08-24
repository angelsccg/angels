package com.example.angelsdemo.widget;

/**
 * Created by chencg on 2018/8/24.
 */

import android.content.Context;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.view.View;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.angels.util.ACLogUtil;
import com.angels.util.ACToast;
import com.example.angelsdemo.R;

public class MyFloatView extends LinearLayout {

    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private WindowManager wm;
    // 此wmParams变量为获取的全局变量，用以保存悬浮窗口的属性
    private WindowManager.LayoutParams wmParams;

    public MyFloatView(Context context,WindowManager wm,WindowManager.LayoutParams wmParams) {
        super(context);
        this.wm = wm;
        this.wmParams = wmParams;
        init();
    }

//    public MyFloatView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public MyFloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    public MyFloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }

    private void init(){
//        wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//        wmParams = new WindowManager.LayoutParams();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.test, null);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACToast.showShort(view.getContext(),"点击");
            }
        });
        this.addView(view);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - 35;
        Log.i("currP", "currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE://捕获手指触摸移动动作
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP://捕获手指触摸离开动作
                updateViewPosition();
                mTouchStartX=mTouchStartY=0;
                break;
            default:
                break;
        }
        return true;
    }

    private void updateViewPosition() {
        //更新浮动窗口位置参数
        wmParams.x = (int)(x - mTouchStartX);
        wmParams.y = (int)(y - mTouchStartY);
        ACLogUtil.i("悬浮窗 位置：" + wmParams.x + "--" + wmParams.y);
        wm.updateViewLayout(this, wmParams);//刷新显示
    }

}
