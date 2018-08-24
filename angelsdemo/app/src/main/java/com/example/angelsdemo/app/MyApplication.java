package com.example.angelsdemo.app;

import android.content.Intent;
import android.view.WindowManager;

import com.angels.app.ACApplication;
import com.angels.cache.ACImageLoader;

public class MyApplication extends ACApplication {

	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	@Override
	public void onCreate() {
		super.onCreate();
		
		ACImageLoader.getInstance().init();

	}

	public WindowManager.LayoutParams getMywmParams() {

		return wmParams;



	}
}
