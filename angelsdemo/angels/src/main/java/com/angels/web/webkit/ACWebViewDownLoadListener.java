package com.angels.web.webkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.DownloadListener;
import android.widget.Gallery;

import com.angels.web.widget.ACWebView;

/**
 * 项目名称：angels3
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/9/7 17:06
 */
public class ACWebViewDownLoadListener implements DownloadListener {

    private ACWebView webView;
    public ACWebViewDownLoadListener(ACWebView webView) {
        this.webView = webView;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        Log.i("tag", "url="+url);
        Log.i("tag", "userAgent="+userAgent);
        Log.i("tag", "contentDisposition="+contentDisposition);
        Log.i("tag", "mimetype="+mimetype);
        Log.i("tag", "contentLength="+contentLength);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        webView.getContext().startActivity(intent);
    }
}
