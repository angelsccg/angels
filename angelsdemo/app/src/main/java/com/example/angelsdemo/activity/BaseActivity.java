package com.example.angelsdemo.activity;

import android.os.Bundle;

import com.angels.app.ACBaseActivity;
import com.umeng.message.PushAgent;


public class BaseActivity extends ACBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//友盟推送
		PushAgent.getInstance(this).onAppStart();
		System.out.println("友盟-->BaseActivity-->id:"+PushAgent.getInstance(this).getRegistrationId());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
