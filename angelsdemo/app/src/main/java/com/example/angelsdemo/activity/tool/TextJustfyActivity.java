package com.example.angelsdemo.activity.tool;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

import com.angels.util.ACLog;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class TextJustfyActivity extends BaseActivity{
	TextView t1,t2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_textjustfy);
		t1 = (TextView) findViewById(R.id.textView1);
		t2 = (TextView) findViewById(R.id.textView2);
		Display display = getWindowManager().getDefaultDisplay();           
		DisplayMetrics dm = new DisplayMetrics();           
		display.getMetrics(dm);           
		int width = dm.widthPixels; 
		t1.setLineSpacing(0f, 1.5f);
		t1.setTextSize(8*(float)width/320f);    
		t1.setText("啊实打实的三啊是方式发送方\n式的方式的方式的方阿达啊实打实式发送\n的方式的发生松岛枫大师大师大师大打撒大声地松岛枫松岛枫"); 
		t2.setText("asdassdfsdfsdf\nsdgsdfgsdasdasdsadfdsfgsdf\nsdfsdfsdfsdfsdfdsfsdasdasdsadasdfsd");
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ACLog.i("onWindowFocusChanged");
		ACToastUtils.showMessage(this, "onWindowFocusChanged");
	}
}
