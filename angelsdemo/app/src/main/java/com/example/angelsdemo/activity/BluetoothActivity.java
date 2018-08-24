package com.example.angelsdemo.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.bluetooth.BluetoothDeviceActivity;
import com.example.angelsdemo.activity.bluetooth.BluetoothGattActivity;
import com.example.angelsdemo.activity.control.VoiceActivity;

public class BluetoothActivity extends BaseActivity implements OnClickListener{
	public static final int REQUEST_CODE = 1001;
	public static final String[] btnNames= {"手机连接的蓝牙设备(开发中)","蓝牙Gatt(开发中)"};
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
			Intent intent = new Intent(this, BluetoothDeviceActivity.class);
			startActivity(intent);
		}
			break;
		case 1:
		{	
			Intent intent = new Intent(this, BluetoothGattActivity.class);
			startActivity(intent);
		}
			break;
			case 2:
			{

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

}
