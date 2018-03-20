package com.example.angelsdemo;

import android.content.Intent;

import com.angels.app.ACApplication;
import com.angels.cache.ACImageLoader;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

public class MyApplication extends ACApplication {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ACImageLoader.getInstance().init();

		PushAgent mPushAgent = PushAgent.getInstance(this);
		System.out.println("友盟-->注册");
		//注册推送服务 每次调用register都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				System.out.println("友盟-->deviceToken："+deviceToken);
			}

			@Override
			public void onFailure(String s, String s1) {
				System.out.println("友盟-->s："+s + ",s1:"+s1);
			}
		});
	}
}
