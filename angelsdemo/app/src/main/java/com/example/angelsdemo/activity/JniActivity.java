package com.example.angelsdemo.activity;

import java.util.Calendar;

import com.angels.widget.ACDoubleDatePickerDialog;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.control.GuaguakaActivity;
import com.example.angelsdemo.activity.control.ImgScrollActivity;
import com.example.angelsdemo.activity.control.RefreshAndLoadMoreActivity;
import com.example.angelsdemo.activity.jni.UninstallActivity;
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

public class JniActivity extends BaseActivity implements OnClickListener{
	public static final String[] btnNames= {"卸载弹出网页（类似卸载原因）"};
	public static final Button[] btns = new Button[btnNames.length];
	
	private LinearLayout llContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainc);
        
        llContent = (LinearLayout) findViewById(R.id.llContent);
        
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
			Intent intent = new Intent(this, UninstallActivity.class);
			startActivity(intent);
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
    /**
     * 双日期选择
     */
	private void showDoubleDatePickerDialog() {
		Calendar c = Calendar.getInstance();
		// 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
		ACDoubleDatePickerDialog dialog = new ACDoubleDatePickerDialog(JniActivity.this, 0, new ACDoubleDatePickerDialog.OnDateSetListener() {
		     @Override
		     public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,int endDayOfMonth) {
		            String textString = String.format("开始时间：%d-%d-%d\n结束时间：%d-%d-%d\n", startYear,
		            startMonthOfYear + 1, startDayOfMonth, endYear, endMonthOfYear + 1, endDayOfMonth);
//		   			et.setText(textString);
		            Toast.makeText(JniActivity.this, textString, Toast.LENGTH_SHORT).show();
		     }
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
		//屏幕切换，当精确到日的时候，屏幕不够显示，需要横屏显示才行
		dialog.setCutScreen(this,true);
		dialog.show();
		
	}
}
