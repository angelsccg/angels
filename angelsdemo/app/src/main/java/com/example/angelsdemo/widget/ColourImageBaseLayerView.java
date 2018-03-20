package com.example.angelsdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * 判断点击的是哪一个层（通过点击，从最上层开始判断点击的点是不是透明的，如果是透明的判断下一层，知道判断到不是透明的为止）
 */
public class ColourImageBaseLayerView extends View
{

    private LayerDrawable mDrawables;

    public ColourImageBaseLayerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mDrawables = (LayerDrawable) getBackground();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(mDrawables.getIntrinsicWidth(), mDrawables.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        final float x = event.getX();
        final float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Drawable drawable = findDrawable(x, y);
            if (drawable != null)
                drawable.setColorFilter(randomColor(), PorterDuff.Mode.SRC_IN);
        }

        return super.onTouchEvent(event);
    }

    private int randomColor()
    {
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

    private Drawable findDrawable(float x, float y)
    {
        final int numberOfLayers = mDrawables.getNumberOfLayers();
        Drawable drawable = null;
        Bitmap bitmap = null;
        for (int i = numberOfLayers - 1; i >= 0; i--)
        {
            drawable = mDrawables.getDrawable(i);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            try
            {
                int pixel = bitmap.getPixel((int) x, (int) y);
                if (pixel == Color.TRANSPARENT)//判断是否透明
                {
                    continue;
                }
            } catch (Exception e)
            {
                continue;
            }
            return drawable;
        }
        return null;
    }
}