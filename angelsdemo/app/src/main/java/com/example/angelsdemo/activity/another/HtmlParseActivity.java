package com.example.angelsdemo.activity.another;

import java.io.InputStream;
import java.net.URL;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.angels.http.AsyncHttpClient;
import com.angels.http.AsyncHttpResponseHandler;
import com.angels.util.ACLog;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class HtmlParseActivity extends BaseActivity {

	private TextView tvHtml;
//	private String url = "http://libgdxapp.vipsinaapp.com/test/test%20.html";
	private String url = "http://news.163.com/15/1116/07/B8HCFIBH00014AED.html";
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tvHtml.setText((CharSequence)(msg.obj));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_parse);

		tvHtml = (TextView) findViewById(R.id.tv_html);
		getNetworkData();
	}

	private void getNetworkData() {
		AsyncHttpClient syncHttpClient = new AsyncHttpClient();
		syncHttpClient.post(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				ACLog.i("getNetworkArticleData()-->content:" + content);
				
				doData(content);
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(Throwable error) {
				super.onFailure(error);
				ACLog.i("getNetworkArticleData()-->error:" + error.getMessage());
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

		});
	}
	
	private void doData(final String html){
		 tvHtml.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动 
		 tvHtml.setText(Html.fromHtml(html));
		 Thread thread = new Thread(){
			 @Override
			 public void run() {
				 CharSequence  text = Html.fromHtml(html, new ImageGetter() {
					    @Override  
					    public Drawable getDrawable(String source) {  
					        InputStream is = null;  
					        try {  
					            is = (InputStream) new URL(source).getContent();  
					            Drawable d = Drawable.createFromStream(is, null);  
//					            d.setBounds(0, 0, d.getIntrinsicWidth(),  d.getIntrinsicHeight());  
					            d.setBounds(0, 0, tvHtml.getWidth(),  d.getIntrinsicWidth()/d.getIntrinsicHeight()*tvHtml.getWidth()); 
					            is.close();  
					            return d;  
					        } catch (Exception e) {  
					            return null;  
					        }  
					    }  
					}, null);
				 Message msg = new Message();
				 msg.obj = text;
				 handler.sendMessage(msg);
			 };
			 
		 };
		 thread.start();
	}


}
