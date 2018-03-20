package com.angels.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

public class AnnulusView2 extends View {


//    private Paint paint;
    private RectF oval;
    private RectF oval2;

    //外环和内圆的距离
    private int distance = 40;
    //圆弧颜色
    private int roundColor;
    //进度颜色
    private int progressColor;
    //文字内容
    private boolean textIsShow;
    //字体大小
    private float textSize = 14;
    //文字颜色
    private int textColor;
    //最大进度
    private int max = 100;
    //当前进度
    private int progress = 100;
    //圆弧宽度
    private int roundWidth = 30;
    //圆环弧度(360 表示圆)
    private int sweepAngle = 360;
    //开始的弧度
    private int startAngle = 270;

    private int viewWidth; //宽度--控件所占区域
    private int viewHeight; //宽度--控件所占区域

    private float nowPro = 0;//用于动画

    private ValueAnimator animator;

    private Path path;
    //圆环画笔
    Paint paintRound;
    //进度条画笔
    Paint paintProgress;
    //中间圆 上部分画笔============正常：176,217,59  偏低：255,157,102  偏高：246,185,45
    Paint paintCircleUp;
    //中间圆 下部分画笔============正常：166,209,41   偏低：252,137,74  偏高：240,174,28
    Paint paintCircleDown;
    //文字
    Paint paintTxt;
    //角度
    private int angle = 20;
    //高血压
    private int bloodHigh;
    //低血压
    private int bloodLow;

    public AnnulusView2(Context context) {
        super(context);
    }

    public AnnulusView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, context);
    }

    public AnnulusView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, context);
    }

    public AnnulusView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs, context);
    }

    private void initAttrs(AttributeSet attr, Context context) {
        TypedArray array = context.obtainStyledAttributes(attr, R.styleable.ACAnnulusView);

        roundColor = array.getColor(R.styleable.ACAnnulusView_annulusRoundColor, Color.BLACK);//环形颜色
        progressColor = array.getColor(R.styleable.ACAnnulusView_annulusProgressColor, Color.RED);//进度颜色
        textIsShow = array.getBoolean(R.styleable.ACAnnulusView_annulusTextIsShow, false);//文字
        textSize = array.getDimension(R.styleable.ACAnnulusView_annulusTextSize, 14);//文字大小
        textColor = array.getColor(R.styleable.ACAnnulusView_annulusTextColor, Color.BLACK);//文字颜色
        roundWidth = array.getInt(R.styleable.ACAnnulusView_annulusRoundWidth, 30);//圆环宽度

        array.recycle();

        //动画
        animator = ValueAnimator.ofFloat(0, progress);
        animator.setDuration(10000);

        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                nowPro = (float) animation.getAnimatedValue();
                ACLogUtil.i("环形动画-->"+animation.getDuration() + "----" + nowPro);
                postInvalidate();
            }
        });
        animator.start();


        path = new Path();
        //外环
        paintRound = new Paint();
        paintRound.setColor(roundColor);
        paintRound.setAntiAlias(true); //设置画笔为无锯齿
        paintRound.setStrokeWidth(roundWidth); //线宽
        paintRound.setStyle(Paint.Style.STROKE); //空心
        //进度条
        paintProgress = new Paint();
        paintProgress.setColor(progressColor);
        paintProgress.setAntiAlias(true); //设置画笔为无锯齿
        paintProgress.setStrokeWidth(roundWidth+1); //线宽
        paintProgress.setStyle(Paint.Style.STROKE); //空心

        //内圆 上
        paintCircleUp = new Paint();
        paintCircleUp.setARGB(255,176,217,59);
        paintCircleUp.setAntiAlias(true); //设置画笔为无锯齿
        paintCircleUp.setStyle(Paint.Style.FILL); //实心
        //内圆 下
        paintCircleDown = new Paint();
        paintCircleDown.setARGB(255,166,209,41);
        paintCircleDown.setAntiAlias(true); //设置画笔为无锯齿
        paintCircleDown.setStyle(Paint.Style.FILL); //实心

        paintTxt = new Paint();
        paintTxt.setColor(Color.WHITE);
        paintTxt.setTextSize(ACUntilUtil.sp2px(context,textSize));
    }

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
        if(widthMeasureSpec > heightMeasureSpec){
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
        }else{
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;//得到宽度以此来计算控件所占实际大小
        viewHeight = h;//
        //计算画布所占区域
        oval = new RectF();
        oval.left = roundWidth + getPaddingLeft();
        oval.top = roundWidth + getPaddingTop();
        oval.right = viewWidth - roundWidth - getPaddingRight();
        oval.bottom = viewWidth - roundWidth - getPaddingBottom();

        //计算画布所占区域
        oval2 = new RectF();
        oval2.left = roundWidth + getPaddingLeft() + distance;
        oval2.top = roundWidth + getPaddingTop() + distance;
        oval2.right = viewWidth - roundWidth - getPaddingRight() - distance;
        oval2.bottom = viewWidth - roundWidth - getPaddingBottom() - distance;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //===========================外环===========================
        canvas.drawArc(oval, startAngle, sweepAngle, false, paintRound); //绘制圆弧
        //===========================画进度层===========================
        canvas.drawArc(oval, startAngle, sweepAngle * nowPro / max, false, paintProgress); //绘制圆弧

        /*============================中间==============================*/
        canvas.drawOval(oval2,paintCircleDown);
        //扇形图
        canvas.drawArc(oval2, -angle, -(180-angle*2), true, paintCircleUp);
        //三角形图
        path.moveTo(viewWidth/2, viewHeight/2);// 此点为多边形的起点
        float a = (float) (oval2.height()/2 * Math.sin(angle * Math.PI / 180));
        float b = (float) (a / Math.tan(angle * Math.PI / 180));
        path.lineTo(viewWidth/2 - b ,viewHeight/2 - a );
        path.lineTo(viewWidth/2 + b, viewHeight/2 - a);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paintCircleDown);

        //======================文字========================

        String high = "-",low = "-";
        if(bloodHigh <= 0){
            high = " - ";
        }else{
            high = bloodHigh + "";
        }
        if(bloodLow <= 0){
            low = " - ";
        }else{
            low = bloodLow + "";
        }
        String blood = high + "/" + low;
        float textWidth = paintTxt.measureText(blood);
        Paint.FontMetrics fm = paintTxt.getFontMetrics();
        float textHeight = (float) Math.ceil(fm.descent - fm.top) + 2;
        canvas.drawText(blood, viewWidth / 2 - textWidth / 2, viewHeight / 2 + textHeight / 3, paintTxt);

//        if (textIsShow) {
//            paint.setColor(textColor);
//            paint.setStrokeWidth(0);
//            paint.setTypeface(Typeface.DEFAULT);
//            paint.setTextSize(textSize * 2);
//            float textWidth = paint.measureText((int) ((nowPro / (float) max) * 100) + "%");
//            canvas.drawText((int) ((nowPro / (float) max) * 100) + "%", viewWidth / 2 - textWidth / 2, viewWidth / 2, paint);
//        }


    }

    private int getDefaultHeight() {
        return 0;
    }

    private int getDefaultWidth() {
        return 0;
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public boolean getText() {
        return textIsShow;
    }

    public void setText(boolean text) {
        this.textIsShow = text;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setBloodHigh(int bloodHigh) {
        this.bloodHigh = bloodHigh;
    }

    public void setBloodLow(int bloodLow) {
        this.bloodLow = bloodLow;
    }
}
