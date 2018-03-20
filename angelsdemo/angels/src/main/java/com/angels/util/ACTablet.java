package com.angels.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * 
* @ClassName: ACTablet 
* @Description: 平板的一些相关属性
* @author angelsC
* @date 2015-11-16 上午11:26:06 
*
 */
public class ACTablet {

    /***
     * 是否是平板
     * @param context
     * @return
     */
	public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
