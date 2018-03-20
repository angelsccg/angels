package com.example.angelsdemo.activity.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.angels.http.AsyncHttpClient;
import com.angels.http.AsyncHttpResponseHandler;
import com.angels.util.ACLog;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.activity.BaseActivity;

public class GetAndPostActivity extends BaseActivity{
	String url = "https://www.baidu.com/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button btn = new Button(this);
		btn.setText("点击获取网络 "+url+" 的数据");
		setContentView(btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getNetworkData();
			}
		});
	}
	
	 private void getNetworkData(){
	    	AsyncHttpClient syncHttpClient = new AsyncHttpClient();
	    	syncHttpClient.post(url, new AsyncHttpResponseHandler()
	         {
	             @Override
	             public void onSuccess(String content)
	             {
	                 // TODO Auto-generated method stub
	                 super.onSuccess(content);
	                 ACLog.i("getNetworkArticleData()-->content:"+content);
	                 ACToastUtils.showMessage(GetAndPostActivity.this, content);
	             }

	             @Override
	             public void onStart()
	             {
	                 super.onStart();
	             }

	             @Override
	             public void onFailure(Throwable error)
	             {
	                 super.onFailure(error);
	                 ACLog.i("getNetworkArticleData()-->error:"+error.getMessage());
	                 ACToastUtils.showMessage(GetAndPostActivity.this, error.getMessage());
	             }

	             @Override
	             public void onFinish()
	             {
	                 // TODO Auto-generated method stub
	                 super.onFinish();
	             }

	         });
	    }
}
