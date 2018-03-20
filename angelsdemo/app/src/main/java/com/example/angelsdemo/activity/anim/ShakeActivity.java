package com.example.angelsdemo.activity.anim;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class ShakeActivity extends BaseActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_shake);
		
		final RelativeLayout layout = (RelativeLayout) findViewById(R.id.rlContainer);
		Button btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Animation anim = AnimationUtils.loadAnimation(ShakeActivity.this, R.anim.ac_shake);
				layout.startAnimation(anim);
			}
		});
		Button btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Animation anim = AnimationUtils.loadAnimation(ShakeActivity.this, R.anim.ac_shake);
				v.startAnimation(anim);
			}
		});
	}
}
