package com.example.angelsdemo;

import android.content.Intent;

import com.angels.app.ACApplication;
import com.angels.cache.ACImageLoader;

public class MyApplication extends ACApplication {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		ACImageLoader.getInstance().init();

	}
}
