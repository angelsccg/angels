package com.example.angelsdemo.activity.tool;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.EditText;
import android.widget.TextView;

import com.angels.util.ACViewTextCondition;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class EditJustfyActivity extends BaseActivity{
	EditText t1,t2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editjustfy);
		t1 = (EditText) findViewById(R.id.editText1);
		t2 = (EditText) findViewById(R.id.editText2);
		
		ACViewTextCondition.digit(t1, 2, 3);
		ACViewTextCondition.digit(t2, 3, 2);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		
		
	}
}
