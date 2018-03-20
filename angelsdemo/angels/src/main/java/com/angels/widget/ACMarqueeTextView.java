package com.angels.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 项目名称：bills
 * 类描述：实现多条一起 跑马灯
 * 创建人：Administrator
 * 创建时间：2016/9/30 15:35
 */

class ACMarqueeTextView extends TextView {
    public ACMarqueeTextView(Context con) {
        super(con);
    }

    public ACMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ACMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
