package com.example.angelsdemo.activity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.angels.util.ACLogUtil;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;
import com.example.angelsdemo.activity.control.GuaguakaActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothDeviceActivity extends BaseActivity implements OnClickListener{

	private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device);


		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

		int a2dp = adapter.getProfileConnectionState(BluetoothProfile.A2DP);
		int headset = adapter.getProfileConnectionState(BluetoothProfile.HEADSET);
		int health = adapter.getProfileConnectionState(BluetoothProfile.HEALTH);
		ACLogUtil.i("蓝牙-->=========："+a2dp + "--" + headset + "--" + health);

		Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
		try {//得到连接状态的方法
			Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
			//打开权限
			method.setAccessible(true);
			int state = (int) method.invoke(adapter, (Object[]) null);
			ACLogUtil.i("蓝牙-->状态："+state);
			if(state == BluetoothAdapter.STATE_CONNECTED){
				ACLogUtil.i("蓝牙-->BluetoothAdapter.STATE_CONNECTED");
				Set<BluetoothDevice> devices = adapter.getBondedDevices();
				ACLogUtil.i("蓝牙-->devices:"+devices.size());

				for(BluetoothDevice device : devices){
					Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
					method.setAccessible(true);
					boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
					ACLogUtil.i("蓝牙-->可能匹配的:"+device.getName());
					if(isConnected){
						ACLogUtil.i("蓝牙-->connected:"+device.getName());
						deviceList.add(device);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    @Override
	public void onClick(View v) {
    	switch (v.getId()) {
		case 0:

			break;
		case 1:
		{	
			Intent intent = new Intent(this, GuaguakaActivity.class);
			startActivity(intent);
		}
			break;
			default:
			break;
		}
	}
}
