package com.example.angelsdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.angels.model.ACRecordMap;
import com.angels.util.ACToastUtils;
import com.angels.web.ui.ACWebActivity;
import com.angels.widget.ACDoubleDatePickerDialog;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.bluetooth.BluetoothGattActivity;
import com.example.angelsdemo.activity.control.VoiceActivity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class BluetoothActivity extends BaseActivity implements OnClickListener{
	public static final int REQUEST_CODE = 1001;
	public static final String[] btnNames= {"蓝牙Gatt"};
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
			Intent intent = new Intent(this, BluetoothGattActivity.class);
			startActivity(intent);
		}
			break;
			default:
				break;
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
		ACDoubleDatePickerDialog dialog = new ACDoubleDatePickerDialog(BluetoothActivity.this, 0, new ACDoubleDatePickerDialog.OnDateSetListener() {
		     @Override
		     public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,int endDayOfMonth) {
		            String textString = String.format("开始时间：%d-%d-%d\n结束时间：%d-%d-%d\n", startYear,
		            startMonthOfYear + 1, startDayOfMonth, endYear, endMonthOfYear + 1, endDayOfMonth);
//		   			et.setText(textString);
		            Toast.makeText(BluetoothActivity.this, textString, Toast.LENGTH_SHORT).show();
		     }
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true);
		//屏幕切换，当精确到日的时候，屏幕不够显示，需要横屏显示才行
		dialog.setCutScreen(this,true);
		dialog.show();
		
	}

}
