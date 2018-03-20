package com.example.angelsdemo.activity;

import java.util.Calendar;

import com.angels.widget.ACDoubleDatePickerDialog;
import com.example.angelsdemo.R;
import com.example.angelsdemo.R.id;
import com.example.angelsdemo.R.layout;
import com.example.angelsdemo.activity.another.HtmlParseActivity;
import com.example.angelsdemo.activity.control.GuaguakaActivity;
import com.example.angelsdemo.activity.control.ImgScrollActivity;
import com.example.angelsdemo.activity.control.RefreshAndLoadMoreActivity;
import com.example.angelsdemo.activity.tool.EditJustfyActivity;
import com.example.angelsdemo.activity.tool.TextJustfyActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AnotherActivity extends BaseActivity implements OnClickListener{
	public static final String[] btnNames= {"HTML解析过滤（jsoup）"};
	public static final Button[] btns = new Button[btnNames.length];
	
	private LinearLayout llContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_mainc);
        
        llContent = (LinearLayout) findViewById(id.llContent);
        
        for (int i = 0; i < btnNames.length; i++) {
			Button button = new Button(this);
			button.setText(btnNames[i]);
			button.setOnClickListener(this);
			llContent.addView(button);
			btns[i] = button;
			btns[i].setId(i);
		}
    }
    
    @Override
	public void onClick(View v) {
    	switch (v.getId()) {
		case 0:
		{
			Intent intent = new Intent(this, HtmlParseActivity.class);
			startActivity(intent);
		}
			break;
		case 1:
		{	
			
		}
			break;
		case 2:
		{
		}
			break;
		case 3:
		{

		}
			break;
		default:
			break;
		}
	}
}
