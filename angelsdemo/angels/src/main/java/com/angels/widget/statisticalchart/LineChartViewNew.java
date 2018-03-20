package com.angels.widget.statisticalchart;

/**
 * 项目名称：angels
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/6/5 14:50
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class LineChartViewNew extends View {

    private Paint rectPaint;// 设置左侧为白色，显示数表
    private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
    private Paint titlePaint;// 绘制文本的画笔
    private Paint linePaint;
    private Paint paint;// 矩形画笔 柱状图的样式信息
    private int[] text = new int[] { 90, 75, 75, 60, 100, 95, 110 };// 折线的转折点
    int x, y, preX, preY;
    // 坐标轴左侧的数标
    private Bitmap mBitmap;
    // 坐标Y轴
    private int[] strY = {160,140,120,80,40,0};
    // 坐标X轴
    private String[] strX = { "9/6","9/7","9/8","9/9","9/10","9/11","9/12"};
    /*最大值*/
    private int maxValue = 160;
    /***线的颜色*/
    private String color = "#bb2222";
    /**设置单位*/
    private String unit = "h";

    private HistogramAnimation ani;
    private int flag;

    public LineChartViewNew(Context context) {
        super(context);
        init(context, null);
    }

    public LineChartViewNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

//        text = new int[] { 6, 5, 5, 4, 5, 3, 2, 3, 1, 1 };

        ani = new HistogramAnimation();
        ani.setDuration(4000);

        rectPaint = new Paint();
        titlePaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePaint = new Paint();

        //
        titlePaint.setAntiAlias(true);
        Rect bundle1 = new Rect();
        Rect bundle2 = new Rect();
        hLinePaint = new Paint();

        /*左边留白宽度*/
        int leftX = dp2px(30);
          /*底部留白宽度*/
        int bottomY = dp2px(30);
        /*图表的高度*/
        int heightChart = getHeight() - bottomY;
        /*图表的宽度*/
        int widthChart = getWidth() - leftX;

        int perWidth = widthChart / (strX.length-1) - dp2px(5);// 将宽度分为strX.length部分
        int hPerHeight = heightChart / (strY.length);// 将高度分为strY.length + 1部分
        rectPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, leftX, getHeight(), rectPaint);// 画一块左边白色区域
        canvas.drawRect(0, heightChart,getWidth(), getHeight(), rectPaint);// 画一块底部白色区域

        Path path = new Path();// 折线图的路径
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);

        /*画横线*/
        for (int i =0; i < strY.length; i++) {
            hLinePaint.setTextAlign(Align.CENTER);
            hLinePaint.setColor(Color.parseColor("#FFE2E2E2"));
            hLinePaint.setStrokeWidth(1);
            y = (i + 1) * hPerHeight;
            canvas.drawLine(leftX, y, getWidth(), y, hLinePaint);
        }
         /*画Y轴上的值*/
        for (int i = 0; i < strY.length ; i++) {
            y = (i + 1) * hPerHeight;
            titlePaint.setTextSize(sp2px(15));
            titlePaint.setColor(Color.parseColor("#FFE2E2E2"));
            titlePaint.getTextBounds(strY[i]+"", 0,
                    (strY[i]+"").length(), bundle2);
            canvas.drawText(strY[i] + "", dp2px(25) - bundle2.width(), y - dp2px(2), titlePaint);//(bundle2.height()/2
            if(unit != null && i == 0){
                canvas.drawText(unit, dp2px(25) - bundle2.width(), y + bundle2.height(), titlePaint);//(bundle2.height()/2
            }
        }
        /*画X轴上的值*/
        for (int i =0; i < text.length; i++) {
            x = i * perWidth + leftX;
            canvas.drawText(strX[i], x - dp2px(8),heightChart + dp2px(17), titlePaint);
        }

        /*画图表上的值*/
        for (int i =0; i < text.length; i++) {
            x = i * perWidth + leftX;
            y = heightChart-text[i] * heightChart/maxValue;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            linePaint.setColor(Color.parseColor(color));
            linePaint.setAntiAlias(true);
            paint = new Paint();
            paint.setColor(Color.parseColor(color));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dp2px(1));
            mCanvas.drawCircle(x, y, dp2px(3), linePaint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            mCanvas.drawPath(path, paint);
        }

//        for (int i =0; i < strY.length; i++) {// 画x线，并在左侧显示相应的数值
//            if (i == 2) {
//                hLinePaint.setStrokeWidth(4);
//                for (int j = 1; j < strY.length + 1; j++) {
//                    canvas.drawLine(dp2px(30) + j * perWidth, y, dp2px(28)
//                            + (j + 1) * perWidth, y, hLinePaint);
//                }
//                titlePaint.setTextSize(sp2px(20));
//                titlePaint.getTextBounds(strY[i - 1], 0, strY[i - 1].length(),
//                        bundle1);
//                canvas.drawText(strY[i - 1], dp2px(25) - bundle1.width(), i
//                        * hPerHeight + (bundle1.height() / 2), titlePaint);
//            } else {
//                if (i != 0) {
//                    titlePaint.setTextSize(sp2px(15));
//                    titlePaint.getTextBounds(strY[i - 1], 0,
//                            strY[i - 1].length(), bundle2);
//                    canvas.drawText(strY[i - 1], dp2px(25) - bundle2.width(), i
//                            * hPerHeight + (bundle2.height() / 2), titlePaint);
//                }
//            }
//        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setStyle(Paint.Style.FILL);

        mCanvas.drawRect(preX + dp2px(30), 0, getWidth(), getHeight(), paint);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        // Log.i("tag", "onDraw()1111");
    }

    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }

    public void start(int flag) {
        startAnimation(ani);
        this.flag = flag;
    }

    /**
     * 集成animation的一个动画类
     *
     * @author
     */
    private class HistogramAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f && flag == 2) {
                preX = (int) ((getWidth() - dp2px(30)) * interpolatedTime);
            } else {
                preX = getWidth();
            }
            invalidate();
        }
    }

    public void setY(int[] strY) {
        this.strY = strY;
    }

    public void setX(String[] strX) {
        this.strX = strX;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
