package com.angels.web.js;

import com.angels.model.ACRecordMap;
import com.angels.util.ACToastUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by Administrator on 2015/8/17.
 */
public class ACJsObject extends ACJsBase{

    public ACJsObject(Context context) {
        mContext = context;
    }
    @JavascriptInterface
    public void getString(String str) {
        Log.i("aw", "jsonToAndroid --> " + str);
        if(jsListener!=null){
            jsListener.getJsString(str);
        }
    }
    @JavascriptInterface
    public void getMap(String param){
        Log.i("aw", "getString --> " + param);
//        ACToastUtils.showMessage(mContext, param);
        ACRecordMap map = new ACRecordMap(param, "&", "=");
        if(jsListener!=null){
            jsListener.getJsMap(map);
        }
    }
    /**
     * 关闭窗口
     * */
    @JavascriptInterface
    public void closeWindow(String param){
        Log.i("aw", "getString --> " + param);
//        ACToastUtils.showMessage(mContext, param);
        ACRecordMap map = null;
        if(param!=null && !TextUtils.isEmpty(param) && !"null".equals(param)){
            map = new ACRecordMap(param, "&", "=");
        }
        if(jsListener!=null){
            jsListener.closeWindow(map);
        }
    }
    /**
     * 吐司 提示
     * */
    @JavascriptInterface
    public void showToast(String msg){
        ACToastUtils.showMessage(mContext, msg);
//       Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

/**
 * 标题栏开关
 * */
    @JavascriptInterface
    public void swtichTitle(boolean b){
//       Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        if(jsListener!=null){
            jsListener.swtichTitle(b);
        }
    }

}
