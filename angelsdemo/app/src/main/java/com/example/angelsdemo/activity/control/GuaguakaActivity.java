package com.example.angelsdemo.activity.control;

import java.util.Calendar;

import com.angels.widget.ACDoubleDatePickerDialog;
import com.angels.widget.ACDoubleDatePickerDialog.OnDateSetListener;
import com.angels.widget.ACGuaGuaKa;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

public class GuaguakaActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guaguaka);
		ACGuaGuaKa ggk = (ACGuaGuaKa)findViewById(R.id.ggk);
		ggk.setText("世界");
		ggk.setUpBitmap2(BitmapFactory.decodeResource(getResources(),R.drawable.test_title));
	}
}
