package com.angels.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 项目名称：
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/11/7 11:02
 *
 *  1,根据activity获取view
 */

public class ACUIUtil {
    public static View getContentView(Activity ac){
        ViewGroup view = (ViewGroup)ac.getWindow().getDecorView();
        FrameLayout content = (FrameLayout)view.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }
}
