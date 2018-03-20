
package com.urionapp.bp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.angels.util.ACLogUtil;
import com.example.urionclass.BleServiceHelper;
import com.example.urionclass.L;
import com.example.urionclass.SampleGattAttributes;
import com.example.urionservice.BluetoothLeService;


/**
 *	@author Terry<br>
 *	@Time 2015��5��15������11:53:53<br>
 */
public abstract class BleFragmentActivity extends FragmentActivity {

	
	
	public final static int ble_scaning = 0;
	public final static int ble_connecting = 1;
	public final static int ble_connected = 3;
	public final static int ble_on = 2;
	public final static int ble_off = -1;
	public final static int ble_disConnected = -2;
	
	
	public int bleState = ble_off;

	private boolean isBindServise;
	public Handler handler = new Handler(Looper.getMainLooper());
	
	
	protected ArrayList<BluetoothDevice> mLeDevices = new ArrayList<BluetoothDevice>();
	// ����GATT Serveice
	protected BluetoothLeService mBluetoothLeService;
	protected BluetoothAdapter mBluetoothAdapter;
	protected BluetoothDevice mDevice;
	
	private BleBroadCastRecever myBleRecever;
	private boolean isBleseviceRegiste;
	private int reTryCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
		myBleRecever = new BleBroadCastRecever();
		registerReceiver(myBleRecever, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
		
		initBlue();
		
		
//		if(mBluetoothAdapter.isEnabled()){
			startScan();
//		}else{
//			showBleDialog();
//		}
		
		//L.d("-------------------->"+mBluetoothAdapter);
	}
	
	
	public BleFragmentActivity() {
	}



	
	public void initBlue() {
		IntentFilter filter =  BleServiceHelper.makeGattUpdateIntentFilter();
		filter.addAction(SampleGattAttributes.DISCONNECTEDBLE);
		registerReceiver(getBroadCastReceiver(), filter);
		isBleseviceRegiste = true;

		// ��ʱ��ʼ��������
		// FIXME ��������
		//getTipText().setText(R.string.ble_scan_);
		
		L.d("-------------------->"+mBluetoothAdapter);
		L.d("-------------------->"+mLeScanCallback);
	}
	
	public void startScan(){
		mDevice = null; 
		mBluetoothAdapter.startLeScan(mLeScanCallback);
		bleState = ble_scaning;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				stopScan();
			}
		}, 15000);
	}
	
	

	public void stopScan() {
		// mScanning = false;
		L.d("-------------------->"+mBluetoothAdapter);
		L.d("-------------------->"+mLeScanCallback);
		
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		// center_button.setText("ֹͣ");
		if(mDevice == null){
			bleState = ble_scaning;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		}
	}
	
	
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (!mLeDevices.contains(device)) {
						mLeDevices.add(device);
						L.d("device-->" + device.getName());
						if (getDeviceName().equals(device.getName()) ||"Wileless BP".equals(device.getName()) ||"Urion BP".equals(device.getName())||"BLE to UART_2".equals(device.getName())||"Bluetooth BP".equals(device.getName())) {
							ACLogUtil.i("蓝牙-->" +  device.getName() + "--" +  device.getType() + "--" +  device.getAddress()+ "--" +  device.getBondState()+ "--" +  device.getUuids().toString() + "--" + device.getBluetoothClass().getMajorDeviceClass());
							bleState = ble_connecting;
							mDevice = device;
							startService();
						}
					}
				}
			});
		}
	};

	
	
	protected void startService() {
		if(!isBindServise ){
			Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
			// ����services
			isBindServise = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		}else{
			if(mBluetoothLeService != null){
				mBluetoothLeService.connect(mDevice.getAddress());// ���ݵ�ַͨ����̨servicesȥ����BLE����
			}
			
		}
	}

	// ��̨services,ͨ��ServiceConnection�ҵ�IBinder-->service
	private final ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service) {
			// ��ȡservices����
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				finish();
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					boolean isOk = mBluetoothLeService.connect(mDevice.getAddress());// ���ݵ�ַͨ����̨servicesȥ����BLE����
					L.d("first  -----connect ok  ? >"+ isOk);
					if(!isOk){
						reTryConected();
					}
				}
			});
			
		}

		// service���쳣���Ͽ����ӵ�ʱ����������Ż��õ���
		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}

	};
//	private DialogFragment bleDialog;

	
	private void reTryConected(){
		reTryCount++;
		if(reTryCount < 4){
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean isOk = mBluetoothLeService.connect(getDeviceName());// ���ݵ�ַͨ����̨servicesȥ����BLE����
					L.d(reTryCount+" -----connect ok  ? >"+ isOk);
					if(!isOk){
						reTryConected();
					}
				}
			}, 500);
		}
	}
	
	protected void showBleDialog(){
		if (!mBluetoothAdapter.isEnabled()) {
			
			new AlertDialog.Builder(this)
			.setMessage("please open your bluetooth")
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int arg1) {
							// TODO Auto-generated method stub
							dialog.cancel();
							Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
							startActivity(intent);
						}

					}).show();
			
		}
	}
	
	
	
	
//	@Override
//	public void onNeutralButtonClicked(int requestCode) {
//		if(42 ==  requestCode){
//			getTipText().setText("�뿪������");
//			getTipText().setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					showBleDialog();
//				}
//			});
//			bleDialog.dismiss();
//		}
//	}

//	@Override
//	public void onPositiveButtonClicked(int requestCode) {
//		if (42 == requestCode) {
//			bleDialog.dismiss();
//
//			Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
//			startActivity(intent);
//
//			getTipText().setText("�뿪������");
//			getTipText().setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					showBleDialog();
//				}
//			});
//		}
//	}
	

	class BleBroadCastRecever extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String stateExtra = BluetoothAdapter.EXTRA_STATE;
			int state = intent.getIntExtra(stateExtra, -1);
			switch (state) {
			case BluetoothAdapter.STATE_TURNING_ON:
				break;
			case BluetoothAdapter.STATE_ON:
//				getTipText().setText("�ѿ�������");
//				getTipText().setOnClickListener(null);
//				initBlue();
				bleState = ble_on;
				startScan();
				break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				bleState = ble_off;
				break;
			case BluetoothAdapter.STATE_OFF:
				bleState = ble_off;
				break;

			}
		}
	}
	
	

	@Override
	protected void onDestroy() {
		if(isBleseviceRegiste){
			unregisterReceiver(getBroadCastReceiver());
		}
		if (isBindServise) {
			this.unbindService(mServiceConnection);
		}
		unregisterReceiver(myBleRecever);
		super.onDestroy();
	
	}
	
	public abstract BroadcastReceiver getBroadCastReceiver();
	
	public abstract TextView getTipText();
	
	public abstract String getDeviceName();
	
	public abstract String getUUID();
	
}
