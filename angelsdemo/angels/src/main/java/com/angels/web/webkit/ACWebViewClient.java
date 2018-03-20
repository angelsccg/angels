package com.angels.web.webkit;

import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.angels.util.ACLog;
import com.angels.web.widget.ACWebView;

/**
 * Created by Administrator on 2015/8/11.
 */
public class ACWebViewClient extends WebViewClient {
	
    private ACWebView mWebView;
    
    public ACWebViewClient(ACWebView mWebView) {
        this.mWebView = mWebView;
    }

    /**
     * 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("tel:")) {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_DIAL,Uri.parse(url)); 
            view.getContext().startActivity(intent);
        } else if (url.startsWith("sms:")) {
            url = url.replace("sms:", "smsto:");
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SENDTO,
                    Uri.parse(url));
            view.getContext().startActivity(intent);
        }
//        else if(url.endsWith(".apk")){//如果是apk链接地址 就跳转到系统浏览器下载  2015-7-29
//        	android.content.Intent intent= new android.content.Intent();
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse(url);
//            intent.setData(content_url);
//            view.getContext().startActivity(intent);
//            if(mWebView.getWebViewListener()!=null){
//                mWebView.getWebViewListener().closeWindow(null);
//            }
//        }
        else {
        	if(!ACWebView.DEFAULT_404.equals(url)){
        		mWebView.setUrl(url);
        	}
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            view.loadUrl(url);
        }
//        return true;
        return super.shouldOverrideUrlLoading(view, url);
    }
    /**
     * 网页加载错误
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if(mWebView.getWebViewListener()!=null){
            mWebView.getWebViewListener().OnReceivedError(view, request, error);
        }
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
    	super.onLoadResource(view, url);
    }

    /**
     *   页面加载完成的时候调用
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        mWebView.loadUrl("javascript:startPlay()");
        mWebView.loadUrl("javascript:(function() { " +
                "var videos = document.getElementsByTagName('audio');" +
                " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
    }
}
