package com.example.angelsdemo.activity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import com.angels.model.ACRecordMap;
import com.angels.web.ui.ACWebActivity;
import com.angels.web.util.ACWeb;
import com.angels.widget.ACDoubleDatePickerDialog;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.control.DialogActivity;
import com.example.angelsdemo.activity.control.GuaguakaActivity;
import com.example.angelsdemo.activity.control.ImageSelectorMainActivity;
import com.example.angelsdemo.activity.control.ImgScroll2Activity;
import com.example.angelsdemo.activity.control.ImgScrollActivity;
import com.example.angelsdemo.activity.control.MenuActivity;
import com.example.angelsdemo.activity.control.RefreshAndLoadMoreActivity;
import com.example.angelsdemo.activity.control.RoundProgressActivity;
import com.example.angelsdemo.activity.control.SeldingFinishActivity;
import com.example.angelsdemo.activity.control.StatisticalchartActivity;
import com.example.angelsdemo.activity.control.StickyNavActivity;
import com.example.angelsdemo.activity.control.StickyNavActivity3;
import com.example.angelsdemo.activity.control.StickyNavActivity4;
import com.example.angelsdemo.activity.control.SwipeMenuListviewActivity;
import com.example.angelsdemo.activity.control.SwipeRefreshLayoutRefresh;
import com.example.angelsdemo.activity.control.VoiceActivity;
import com.example.angelsdemo.service.DialogService;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ControlActivity extends BaseActivity implements OnClickListener{
	public static final int REQUEST_CODE = 1001;
	public static final String[] btnNames= {"双日期选择","刮刮卡","下拉刷新和上拉加载","滚动图片展示","左滑关闭界面","网页","半圆形菜单","对话框","下拉刷新和上拉加载2","多个图片水平滚动","listview 右滑动删除","图片选择器","滑动置顶1","滑动置顶3","统计图","滑动置顶4","圆形进度条","声波","声波2"};
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
			showDoubleDatePickerDialog();
			break;
		case 1:
		{	
			Intent intent = new Intent(this, GuaguakaActivity.class);
			startActivity(intent);
		}
			break;
		case 2:
		{
			Intent intent = new Intent(this, RefreshAndLoadMoreActivity.class);
			startActivity(intent);
		}
			break;
		case 3:
		{
			Intent intent = new Intent(this, ImgScrollActivity.class);
			startActivity(intent);
		}
			break;
		case 4:
		{
			Intent intent = new Intent(this, SeldingFinishActivity.class);
			startActivity(intent);
		}
			break;
		case 5:
		{
//			Intent intent = new Intent(this, com.angels.web.ui.ACWebActivity.class);
//			startActivity(intent);
//			ACWeb.showWeb(this, "http://news.163.com/",null);
//			ACWeb.showWeb(this, "http://www.baidu.com", null, REQUEST_CODE);
			ACWeb.showWeb(this, "file:///android_asset/html/index.html", null, REQUEST_CODE);
		}
			break;
		case 6:
		{
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
		}
			break;
		case 7:
		{
			Intent intent = new Intent(this, DialogActivity.class);
			startActivity(intent);
		}
			break;
		case 8:
		{
			Intent intent = new Intent(this, SwipeRefreshLayoutRefresh.class);
			startActivity(intent);
		}
			break;
		case 9:
		{
			Intent intent = new Intent(this, ImgScroll2Activity.class);
			startActivity(intent);
		}
			break;
		case 10:
		{
			Intent intent = new Intent(this, SwipeMenuListviewActivity.class);
			startActivity(intent);
		}
			break;
		case 11:
		{
			Intent intent = new Intent(this, ImageSelectorMainActivity.class);
			startActivity(intent);
		}
			break;
			case 12:
			{
				Intent intent = new Intent(this, StickyNavActivity.class);
				startActivity(intent);
			}
			break;
			case 13:
			{
				Intent intent = new Intent(this, StickyNavActivity3.class);
				startActivity(intent);
			}
			break;
			case 14:
			{
				Intent intent = new Intent(this, StatisticalchartActivity.class);
				startActivity(intent);
			}
			break;
			case 15:
			{
				Intent intent = new Intent(this, StickyNavActivity4.class);
				startActivity(intent);
			}
			break;
			case 16:
			{
				Intent intent = new Intent(this, RoundProgressActivity.class);
				startActivity(intent);
			}
			break;
			case 17:
			{
				Intent intent = new Intent(this, VoiceActivity.class);
				startActivity(intent);
			}
			break;
			default:
			break;
		}
	}
	private Handler handler;

	public void toActivity(){
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent startIntent = new Intent(ControlActivity.this, DialogService.class);
				startService(startIntent);
			}
		},5*1000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(handler != null){
			handler.removeMessages(0);
		}
	}

	private static final int MY_PERMISSIONS_REQUEST_VOICE = 1001;
	private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1002;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_VOICE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, VoiceActivity.class);
                    startActivity(intent);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
//                    Intent intent = new Intent(this, VoiceActivity.class);
//                    startActivity(intent);
                    ACToastUtils.showMessage(this,"没权限1");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
			case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					ACToastUtils.showMessage(this,"月权限1");
//					Intent intent = new Intent(this, VoiceActivity.class);
//					startActivity(intent);
					// permission was granted, yay! Do the
					// contacts-related task you need to do.
				} else {
//                    Intent intent = new Intent(this, VoiceActivity.class);
//                    startActivity(intent);
					ACToastUtils.showMessage(this,"没权限2");
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		ACToastUtils.showMessage(this, "requestCode,resultCode,data" + requestCode + "--" + resultCode +"--"+data );
		Log.i("AC","onActivityResult --> " + "requestCode,resultCode,data" + requestCode + "--" + resultCode + "--" + data);
		if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
			if(data!=null){
				ACRecordMap map = (ACRecordMap) data.getSerializableExtra(ACWebActivity.WEBVIEW_CLOSE_DATA);
				Log.i("AC","onActivityResult --> " + map);
				if(map!=null){
					String str = "";
					Iterator iter = map.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						Object key = entry.getKey();
						Object val = entry.getValue();
						str = str + "----key:" + key + ",val:" + val + ";";
						Log.i("AC","onActivityResult --> " + key + "--" + val);
					}
					ACToastUtils.showMessage(this, str);
				}
			}
		}

	}

    /**
     * 双日期选择
     */
	private void showDoubleDatePickerDialog() {
		Calendar c = Calendar.getInstance();
		// 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
		ACDoubleDatePickerDialog dialog = new ACDoubleDatePickerDialog(ControlActivity.this, 0, new ACDoubleDatePickerDialog.OnDateSetListener() {
		     @Override
		     public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,int endDayOfMonth) {
		            String textString = String.format("开始时间：%d-%d-%d\n结束时间：%d-%d-%d\n", startYear,
		            startMonthOfYear + 1, startDayOfMonth, endYear, endMonthOfYear + 1, endDayOfMonth);
//		   			et.setText(textString);
		            Toast.makeText(ControlActivity.this, textString, Toast.LENGTH_SHORT).show();
		     }
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
		//屏幕切换，当精确到日的时候，屏幕不够显示，需要横屏显示才行
		dialog.setCutScreen(this,true);
		dialog.show();
	}

}
