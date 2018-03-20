package com.example.angelsdemo.activity.control;

import android.os.Bundle;
import android.widget.TextView;

import com.angels.widget.ACSildingFinishLayout;
import com.angels.widget.ACSildingFinishLayout.OnSildingFinishListener;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class SeldingFinishActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seldingfinsh);
		
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		ACSildingFinishLayout sildingFinishLayout = ((ACSildingFinishLayout)findViewById(R.id.finish));
		sildingFinishLayout.setOnSildingFinishListener(new OnSildingFinishListener() {  
			  
            @Override  
            public void onSildingFinish() {  
            	SeldingFinishActivity.this.finish();  
            }  
        });
		sildingFinishLayout.setTouchView(textView1);
	}
}
