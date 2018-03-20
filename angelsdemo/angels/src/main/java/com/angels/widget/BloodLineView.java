package com.angels.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.angels.R;
import com.angels.util.ACLogUtil;
import com.angels.util.ACUntilUtil;

/**
 * Created by chencg on 2018/1/25.
 */

public class BloodLineView extends View {

    private RectF oval;//画布
    private int viewWidth; //宽度--控件所占区域
    private int viewHeight; //宽度--控件所占区域

    Paint paintLow,paintHigh,paintNormal;
    RectF rectLow,rectHigh,rectNormal;
    public BloodLineView(Context context) {
        super(context);
        init();
    }

    public BloodLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BloodLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BloodLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        paintLow = new Paint();
        paintLow.setColor(Color.parseColor("#f6b92d"));
        rectLow = new RectF (oval.left,oval.top,oval.right/3,oval.top);

        paintHigh = new Paint();
        paintLow.setColor(Color.parseColor("#fc894a"));
        rectHigh = new RectF (oval.left,oval.top,oval.right/3,oval.top);

        paintNormal = new Paint();
        paintLow.setColor(Color.parseColor("#a6d129"));
        rectNormal = new RectF (oval.left,oval.top,oval.right/3,oval.top);
    }

//    private void initAttrs(AttributeSet attr, Context context) {
//        TypedArray array = context.obtainStyledAttributes(attr, R.styleable.ACAnnulusView);
//
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

//        if (widthSpecMode == MeasureSpec.AT_MOST) {//可获得最大空间
//            setMeasuredDimension(widthMeasureSpec, (widthSpecSize / 2) + (int) (Math.cos(20) * (widthSpecSize / 2)));
//        } else if (widthMeasureSpec == MeasureSpec.EXACTLY) {//一般指精确值
//            setMeasuredDimension(widthMeasureSpec, (widthSpecSize / 2) + (int) (Math.cos(20) * (widthSpecSize / 2)));
//        } else {
//            setMeasuredDimension(widthMeasureSpec, (viewWidth / 2) + (int) (Math.cos(20) * (viewWidth / 2)));
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;//得到宽度以此来计算控件所占实际大小
        viewHeight = h;//
        //计算画布所占区域
        oval = new RectF();
        oval.left = getPaddingLeft();
        oval.top = getPaddingTop();
        oval.right = viewWidth - getPaddingRight();
        oval.bottom = viewHeight - getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(rectLow,paintLow);
        canvas.drawRect(rectNormal,paintLow);
        canvas.drawRect(rectHigh,paintLow);
    }
}
