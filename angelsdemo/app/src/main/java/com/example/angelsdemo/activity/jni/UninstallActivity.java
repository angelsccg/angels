package com.example.angelsdemo.activity.jni;

import com.angels.jni.ACUninstall;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class UninstallActivity extends Activity{
	private String url = "https://www.baidu.com/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 TextView  tv = new TextView(this);
	     tv.setText("卸载demo,可以卸载试试了\n"+url);
	     setContentView(tv);
		 //初始化卸载反馈
//        new ACUninstall().init("/data/data/"+getPackageName(),url);
	}
}
