package com.angels.web.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angels.R;
import com.angels.model.ACRecordMap;
import com.angels.web.js.ACJsBase;
import com.angels.web.js.ACJsObject;
import com.angels.web.webkit.ACWebChromeClient;
import com.angels.web.webkit.ACWebViewClient;
import com.angels.web.webkit.ACWebViewDownLoadListener;


/**
 * Created by Administrator on 2015/8/11.
 */
public class ACWebView extends WebView{
	/**默认主页*/
//    public static final String DEFAULT_URL = "https://www.baidu.com/";
    public static final String DEFAULT_URL = "file:///android_asset/html/index.html";
    /**默认404页*/
    public static final String DEFAULT_404 = "file:///android_asset/html/404/404.html";
    
    public interface WebViewListener{
        void closeWindow(ACRecordMap map);
        void OnReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
        void getJsMap(ACRecordMap map);
        void getJsString(String str);
        void swtichTitle(boolean b);
    }
    private WebViewListener mListener;

    private ACJsBase.ACJsListener jsListener = new ACJsBase.ACJsListener() {
        @Override
        public void getJsMap(ACRecordMap map) {
            mListener.getJsMap(map);
        }

        @Override
        public void closeWindow(ACRecordMap map) {
            Log.i("aw", "closeWindow --> " + map);
            mListener.closeWindow(map);
        }

        @Override
        public void getJsString(String str) {
            mListener.getJsString(str);
        }

        @Override
        public void swtichTitle(boolean b) {
            mListener.swtichTitle(b);
        }
    };

    private ACWebViewClient webViewClient;
    private ACWebChromeClient webChromeClient;
    private ACWebViewDownLoadListener downLoadListener;
    private ProgressBar progressbar;
    private TextView tvTitle;
    private TextView tvBack;
    private String mUrl;

    private ACJsObject jsObject;

    public ACWebView(Context context) {
        super(context);
        init();
    }
    public ACWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ACWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    @Override
    public void loadUrl(String url) {
    	super.loadUrl(url);
    }
    
    @Override
    public void reload() {
    	loadUrl(mUrl);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(progressbar!=null){
            LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
            lp.x = l;
            lp.y = t;
            progressbar.setLayoutParams(lp);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
    @Override
    public void destroy() {
    	clearCacheAndHistory();
    	super.destroy();
    }
/*
    进度条加载变化
* */
    public void onProgressChanged(int newProgress){
        if(progressbar != null){
            if (newProgress >= 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE){
                    progressbar.setVisibility(VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
        }
    }
    
    @SuppressLint("NewApi") private void init() {
//        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
//        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));
//        Drawable drawable = getResources().getDrawable(R.drawable.ac_web_progress);
//        progressbar.setProgressDrawable(drawable);
        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));

        addView(progressbar);

        /*===============WebViewClient===================*/
        webViewClient = new ACWebViewClient(this);
        this.setWebViewClient(webViewClient);

        /*===============WebChromeClient===================*/
        webChromeClient = new ACWebChromeClient(this);
        this.setWebChromeClient(webChromeClient);

         /*===============WebViewDownLoadListener===================*/
        downLoadListener = new ACWebViewDownLoadListener(this);
        this.setDownloadListener(downLoadListener);

        /*===================WebSettings======================*/
        WebSettings setting = this.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setAllowFileAccess(true); // 允许访问文件
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setSupportMultipleWindows(true);
        setting.setDefaultTextEncodingName("utf-8");//设置编码
        setting.setSupportZoom(true);
        setting.setBuiltInZoomControls(true);  // 显示放大缩小 controler
        setting.setDisplayZoomControls(false);
        /*
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //保证可滑动
        setting.setDomStorageEnabled(true);
        setting.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = this.getContext().getCacheDir().getAbsolutePath();
        setting.setAppCachePath(appCachePath);
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(true);

        jsObject = new ACJsObject(getContext());
        this.addJavascriptInterface(jsObject, ACJsBase.JS_ANGELS);
        jsObject.setJsListener(jsListener);
        //自适应屏幕
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
//        this.setVerticalScrollBarEnabled(false);
//        this.setHorizontalScrollBarEnabled(false);
    }


    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvBack() {
        return tvBack;
    }

    public void setTvBack(TextView tvBack) {
        this.tvBack = tvBack;
    }

    public void setWebViewListener(WebViewListener mListener){
        this.mListener = mListener;
    }

    public WebViewListener getWebViewListener(){
        return mListener;
    }

    public String getUrl() {
		return mUrl;
	}
	
	public void setUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	/**
	 * 清理cache 和历史记录
	 */
	private void clearCacheAndHistory(){
	    this.clearCache(true);
	    this.clearHistory();
	}
}
