package com.angels.web.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.angels.R;
import com.angels.app.ACBaseActivity;
import com.angels.model.ACRecordMap;
import com.angels.util.ACLog;
import com.angels.web.widget.ACWebView;
import com.angels.util.ACToastUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/11.
 */
public class ACWebActivity extends ACBaseActivity implements View.OnClickListener{
    /**关闭此activity后 传给下一个activity的数据*/
    public static final String WEBVIEW_CLOSE_DATA = "webview_close_data";
    /**类型 */
    public static final String WEBVIEW_TYPE = "webview_type";
    /**webView*/
    private ACWebView webView;
    /**返回键*/
    private TextView tvBack;
    /**标题文本*/
    private TextView tvTitle;
    /**标题栏*/
    private RelativeLayout rlWebTitle;
    /**底部*/
    private LinearLayout llButtom;

    /**上一页*/
    private ImageView ivBefore;
    /**下一页*/
    private ImageView ivAfter;
    /**刷新*/
    private ImageView ivRefresh;
    /**分享*/
    private ImageView ivShare;

    /**类型：1,default 默认  2，horizontal 横轴显示 3，horizontal_no_all 横轴 不显示头部和底部*/
    private String type = "default";

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.ac_iv_web_back) {
                if(webView.canGoBack()){
                    webView.goBack(); // goBack()表示返回WebView的上一页面
                }
            }else if(i == R.id.ac_iv_web_forward){
                if(webView.canGoForward()){
                    webView.goForward();
                }
            }else if(i == R.id.ac_iv_web_refresh){
                webView.reload();  //刷新
            }else if(i == R.id.ac_iv_web_share){
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
//                String imagePath = "file:///sdcard/car_273/logo_273.png";
//                String imagePath = ACSDCardUtils.getSDCardPath()+"/car_273/logo_273.png";
//                Uri imageUri = Uri.fromFile(new File(imagePath));
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("text/plain");
//                shareIntent.setType("image/*");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_activity_web);
        initView();
        initData();
        /**
         * 设置为横屏 全屏
         */
        if("horizontal_no_all".equals(type) && getRequestedOrientation() !=  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            rlWebTitle.setVisibility(View.GONE);
            llButtom.setVisibility(View.GONE);
        }
    }
    private void initView() {
        webView = (ACWebView) findViewById(R.id.ac_webview);
        tvBack = (TextView) findViewById(R.id.ac_tv_back);
        tvTitle = (TextView) findViewById(R.id.ac_tv_title);
        rlWebTitle = (RelativeLayout) findViewById(R.id.ac_rl_web_title);
        llButtom = (LinearLayout) findViewById(R.id.ac_ll_buttom);
        ACLog.i("webView:"+webView);
        tvBack.setOnClickListener(this);

        webView.setTvBack(tvBack);
        webView.setTvTitle(tvTitle);
        webView.setWebViewListener(webViewListener);

        ivBefore = (ImageView) findViewById(R.id.ac_iv_web_back);
        ivAfter = (ImageView) findViewById(R.id.ac_iv_web_forward);
        ivRefresh = (ImageView) findViewById(R.id.ac_iv_web_refresh);
        ivShare = (ImageView) findViewById(R.id.ac_iv_web_share);
        ivBefore.setOnClickListener(listener);
        ivAfter.setOnClickListener(listener);
        ivRefresh.setOnClickListener(listener);
        ivShare.setOnClickListener(listener);
    }

    private void initData() {
    	Intent intent = getIntent();
        type = intent.getStringExtra(WEBVIEW_TYPE);

    	String scheme = intent.getScheme();  
    	Uri uri = intent.getData();
    	System.out.println("scheme:" + scheme);
    	String host,dataString = null,id,path,path1,queryString;
    	if(uri!=null){
    		 host = uri.getHost();  
             dataString = intent.getDataString();  
             id = uri.getQueryParameter("d");  
             path = uri.getPath();  
             path1 = uri.getEncodedPath();  
             queryString = uri.getQuery();  
             System.out.println("ACWebActivity-->host:"+host);
             System.out.println("ACWebActivity-->dataString:"+dataString);
             System.out.println("ACWebActivity-->id:"+id);
             System.out.println("ACWebActivity-->path:"+path);
             System.out.println("ACWebActivity-->path1:"+path1);
             System.out.println("ACWebActivity-->queryString:" + queryString);
    	}
    	if(dataString!=null||!TextUtils.isEmpty(dataString)){
    		 webView.loadUrl(dataString);
    	}else{
    		 webView.loadUrl(ACWebView.DEFAULT_URL);
    	}
    }

    @Override
    public void onClick(View v) {
        if(v.equals(tvBack)){
//            Intent intent = new Intent();
            this.finish();
//          webView.loadUrl("javascript:updateHtml()");
//            rlWebTitle.setVisibility(View.GONE);
//            webView.loadUrl("javascript:wave()");
        }
//    	webView.reload();  //刷新
    }

    /**
     * 隐藏标题
     */
    public void hideTitle(){
        if(rlWebTitle.getVisibility()!=View.GONE){
            rlWebTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 显示标题
     */
    public void showTitle(){
        if(rlWebTitle.getVisibility()!=View.VISIBLE){
            rlWebTitle.setVisibility(View.VISIBLE);
        }
    }

    private ACWebView.WebViewListener webViewListener = new ACWebView.WebViewListener(){
        @Override
        public void closeWindow(ACRecordMap map) {
            if(map!=null){
                String str = "";
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    str = str + "key:" + key + ",val:" + val + ";";
                }
                ACToastUtils.showMessage(ACWebActivity.this, str);
                Intent intent = new Intent();
                intent.putExtra(WEBVIEW_CLOSE_DATA,map);
                setResult(RESULT_OK, intent);
            }
            finish();
        }

        @Override
        public void OnReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    	/*
         * 主要是在WebViewClient里，重载OnReceivedError的方法，然后通过javascript操作dom去改变内容。
         * 而网上大多数的处理方式是view.loadUrl(指向一个assets目录下的html文件或者"about:blank")，
         * 这样处理的话，在重新刷新时就会刷新当前这个错误的页面，或者在处理goBack()时候会出现一些问题。而通过以下方式则可以避免相应的问题。
         */
            //用javascript隐藏系统定义的404页面信息
//        String data = "Page NO FOUND！";
//        view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");

            webView.loadUrl(ACWebView.DEFAULT_404);
        }

        @Override
        public void getJsMap(ACRecordMap map) {
            if(map!=null){
                String str = "";
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    str = str + "key:" + key + ",val:" + val + ";";
                }
                ACToastUtils.showMessage(ACWebActivity.this, str);
            }
        }

        @Override
        public void getJsString(String str) {
            ACToastUtils.showMessage(ACWebActivity.this, str);
        }

        @Override
        public void swtichTitle(boolean b) {
            if(b){
                showTitle();
            }else{
                hideTitle();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }
}
