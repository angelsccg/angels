package com.angels.web.js;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.angels.model.ACRecordMap;

/**
 * Created by Administrator on 2015/8/12.
 */
public class ACJsBase{
    public static final String JS_ANGELS = "Angels";

    protected Context mContext;

    public interface ACJsListener
    {
        void getJsMap(ACRecordMap map);//获取传来的ACRecordMap数据
        void closeWindow(ACRecordMap map);//关闭窗口
        void getJsString(String str);//获取传来的数据
        void swtichTitle(boolean b);//标题栏开关
    }

    protected ACJsListener jsListener;

    public void setJsListener(ACJsListener jsListener) {
        this.jsListener = jsListener;
    }

}
