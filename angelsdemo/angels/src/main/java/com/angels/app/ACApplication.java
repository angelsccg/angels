package com.angels.app;

import android.app.Application;

import com.angels.cache.ACGlobalInfo;
import com.angels.exception.ACCrashHandler;

public class ACApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ACGlobalInfo.getInstance().initGlobal(this);

		// 异常处理，不需要处理时注释掉这两句即可！
		ACCrashHandler crashHandler = ACCrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(getApplicationContext());
	}
}
