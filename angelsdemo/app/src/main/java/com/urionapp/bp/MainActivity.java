package com.urionapp.bp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelsdemo.R;
import com.example.urionbean.Msg;
import com.example.urionclass.L;
import com.example.urionclass.MySpinnerButton;
import com.example.urionclass.SampleGattAttributes;
import com.example.urionservice.BluetoothLeService;

//UrionApp_2015_6_7_1.apk
public class MainActivity extends BleFragmentActivity implements OnClickListener {

	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;

	private ImageButton start, user, thread, history, edit;
	private ImageView bluetooth;
	private TextView state;
	private MySpinnerButton mSpinnerBtn;
	private List<String> list = new ArrayList<String>();
	int i = 0, lu = 0;
	String str;
	private static final String TAG = "Activity2";
	String nametwo;
	public static int fan = 0, ji = 0;
	// private String bleAddress;

	protected boolean isClickOn;
	private boolean backStop;
	
	private boolean RecievedDataFix;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_bloom);

//		if (!mBluetoothAdapter.isEnabled()) {
//			// Intent enableIntent = new Intent(
//			// BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			// startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//			bleState = ble_off;
//			showBleDialog();
//		} else {
//			bleState = ble_on;
//		}
		initViews();
	}

	private void initViews() {
		start = (ImageButton) findViewById(R.id.start);
		thread = (ImageButton)  findViewById(R.id.treads);
		history = (ImageButton)  findViewById(R.id.history1);
		edit = (ImageButton)  findViewById(R.id.bianji);
		bluetooth = (ImageView)  findViewById(R.id.blue);
		bluetooth.setImageResource(R.drawable.bluetoothno);
		mSpinnerBtn = (MySpinnerButton) findViewById(R.id.spinner_btn);
		state = (TextView) findViewById(R.id.war);
		state.setTextColor(Color.TRANSPARENT);
		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, list);
		start.setOnClickListener(this);
		thread.setOnClickListener(this);
		edit.setOnClickListener(this);
		history.setOnClickListener(this);
		list.add("User");
		if (fan == 0) {
			mSpinnerBtn.setText("User");
		} else {
			mSpinnerBtn.setText(getIntent().getExtras().getString("gname"));
			state.setText("aaaa");
		}
		i++;
		fan++;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.treads:
			nametwo = mSpinnerBtn.getText().toString();
			Intent serverIntent = new Intent(MainActivity.this,
					TwoActivity.class);
			serverIntent.putExtra("gname", nametwo);
			startActivity(serverIntent);

			break;
		case R.id.history1:
			nametwo = mSpinnerBtn.getText().toString();

			Intent two = new Intent(MainActivity.this, ThreadActivity.class);
			String nameone = mSpinnerBtn.getText().toString();
			two.putExtra("gname", nameone);
			startActivity(two);

			break;
		case R.id.start:
			if (isFastDoubleClick()) {
				return;
			}

			if (!mBluetoothAdapter.isEnabled()) {
				// if (bleState == ble_turning) {
				// Toast.makeText(this,
				// "bluetooth is turning on, please wait......",
				// Toast.LENGTH_SHORT).show();
				// } else if (bleState == ble_off) {
				showBleDialog();
				// }
			} else {
				L.d("bleSTATE_--->" + bleState);
				if (mSpinnerBtn.getText().toString().equals("User")) {
					new AlertDialog.Builder(MainActivity.this)
							.setMessage(" Please choose a user")
							.setPositiveButton("Yes", null).show();
				} else {
					if (state.getText().toString().equals("The connected")) {
						bluetooth.setImageResource(R.drawable.bluetooth);
						if(isRecivced){
							toOneoneActivity();
							isRecivced = false;
						}else{
							isClickOn = true;
							toShowDataPage();
						}
						
						// rbxt.getService().connect();
					} else {
						if (bleState == ble_scaning) {
							Toast.makeText(
									this,
									"bluetooth is scaning, please make sure your device is turning on......",
									Toast.LENGTH_SHORT).show();
						}
//						else if (bleState == ble_connecting) {
//							Toast.makeText(
//									this,
//									"bluetooth is connecting, please wait......",
//									Toast.LENGTH_SHORT).show();
//						}
//						else if (bleState == ble_disConnected) {
						else {
							startScan();
							Toast.makeText(this, "bluetooth is disconected, will be scaning again",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
			break;
		case R.id.bianji:
			// state.setText(mSpinnerBtn.getText());
			Intent oneone = new Intent(MainActivity.this, ZhuceActivity.class);
			String gname = mSpinnerBtn.getText().toString();
			oneone.putExtra("gname", gname);
			startActivityForResult(oneone, 100);
			break;
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;

		}

		return super.dispatchKeyEvent(event);
	}

	private void toShowDataPage() {

		// [0xFD,0xFD,0xFA,0x05,0X0D, 0x0A]

		if (isClickOn) {
			isClickOn = false;
			byte[] send = { (-3), (-3), -6, 5, 13, 10 };
			// rbxt.getService().write(send);
			gattCharacteristicWrite.setValue(send);
			mBluetoothLeService.getmBluetoothGatt().writeCharacteristic(
					gattCharacteristicWrite);
			
			RecievedDataFix = true;
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					RecievedDataFix = false;
				}
			}, 2000);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (KeyEvent.KEYCODE_HOME == keyCode)
			android.os.Process.killProcess(android.os.Process.myPid());
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		// this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		super.onAttachedToWindow();
	}

	public void exitProgrames() {

		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		android.os.Process.killProcess(android.os.Process.myPid());

	}

	public void onMessage(Msg message) {
		// super.onMessage(message);
		if (message.getHead() == null) {
			state.setText(getResources().getStringArray(R.array.connect_state)[message
					.getMsg_code()]);
			// + message.getDevice_name());
			if (state.getText().toString().equals("connecting...")) {
				// bluetooth.setImageResource(R.drawable.bluetooth);
				Toast.makeText(this, "Pairing, please wait......",
						Toast.LENGTH_SHORT).show();
				// rbxt.getService().connect();

			}
			if (state.getText().toString().equals("The connected")) {
				bluetooth.setImageResource(R.drawable.bluetooth);
				toShowDataPage();

				/*
				 * Toast .makeText( this,"Pairing, please wait......",
				 * Toast.LENGTH_SHORT).show();
				 */

				// rbxt.getService().connect();
			}

		} else {
			// Toast.makeText(this, "������ʼ!", Toast.LENGTH_SHORT).show();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.d("onActivityResult" + "requestCode---->" + requestCode);
		L.d("onActivityResult" + "resultCode---->" + resultCode);
		switch (requestCode) {
		case REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				// rbxt.setupChat();
			} else {
				Toast.makeText(this, "The bluetooth is not available.",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case 100:
			if (20 == resultCode) {
				String sp = mSpinnerBtn.getText().toString();
				if (sp.equals("User") && i != 0) {
					String bname = data.getExtras().getString("bname");
					mSpinnerBtn.setText(bname);
				}
			}
			break;

		case 30:
			isRecivced = false;
			// if (20 == resultCode) {
			// state.setText("aaaa");
			// bluetooth.setImageResource(R.drawable.bluetoothno);
			// }
			if (21 == resultCode) {
				backStop = true;
			}

			break;
		case REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				System.out.println(state.getText() + "dfdfdfdfdfdfdfdfdfdfdf");
				if ((state.getText().toString().equals("aaaa"))) {// &rbxt.getService().getmState()
																	// ==
																	// Msg.MESSAGE_STATE_CONNECTED
					state.setText("The connected");
					bluetooth.setImageResource(R.drawable.bluetooth);

				}
				// rbxt.getService().setDevice(device);
				// rbxt.getService().connect();

			}

			// ѡ���豸������
			// if (resultCode == 300) {
			// bleAddress = data.getExtras().getString(
			// DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			//
			// startService();
			// mState = Msg.MESSAGE_STATE_CONNECTING;
			// onMessage(new Msg(mState, ""));
			// // rbxt.getService().setDevice(device);
			// // rbxt.getService().connect();
			//
			// }
			break;
		}

	}

	/*
	 */
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {

				displayGattServices(mBluetoothLeService
						.getSupportedGattServices());

			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				L.d("ble get data ");
				byte[] data = intent.getExtras().getByteArray("data");
				L.d(Arrays.toString(data));
				doWithData(data);

			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				state.setText("disConnected");
				bluetooth.setImageResource(R.drawable.bluetoothno);
				bleState = ble_disConnected;
				
				// isConnected = false;

			} else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

			} else if (BluetoothLeService.ACTION_GATT_WRITE_SUCCESS
					.equals(action)) {
				// isNotifyAble = true;

				mState = Msg.MESSAGE_STATE_CONNECTED;
				onMessage(new Msg(mState, ""));
				backStop = false;
				isRecivced = false;
				bleState = ble_connected;
				// byte[] sends = { (-2), (-3), (-86), (-96), 13, 10 };
				// gattCharacteristicWrite.setValue(sends);
				// mBluetoothLeService.getmBluetoothGatt().writeCharacteristic(gattCharacteristicWrite);

			} else if (SampleGattAttributes.DISCONNECTEDBLE.equals(action)) {
				// if(intent.getBooleanExtra("stop", false)){
				// byte[] send = {(byte)0xFD,(byte)0xFD,(byte)0xFE, 0x06, 0X0D,
				// 0x0A};
				// gattCharacteristicWrite.setValue(send);
				// mBluetoothLeService.getmBluetoothGatt().writeCharacteristic(gattCharacteristicWrite);
				// }
				// else{
				// mBluetoothLeService.disconnect();
				// mBluetoothLeService.close();
				// state.setText("disconnected");
				// }

			}
		}
	};
	private BluetoothGattCharacteristic gattCharacteristicWrite;
	private int mState;
	private boolean isRecivced;

	@SuppressLint("NewApi")
	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null)
			return;
		for (BluetoothGattService gattService : gattServices) {
			String uuid = gattService.getUuid().toString();
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			if (uuid.equalsIgnoreCase(SampleGattAttributes.SERVICE_UU)) {
				for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
					String uuid1 = gattCharacteristic.getUuid().toString();
					if (uuid1.equalsIgnoreCase(SampleGattAttributes.NOTIFY_UU)) {
						mBluetoothLeService.setCharacteristicNotification(
								gattCharacteristic, true);
					}
					if (uuid1.equalsIgnoreCase(SampleGattAttributes.WRITE_UU)) {
						gattCharacteristicWrite = gattCharacteristic;
					}
				}
			}
		}
	}

	protected void doWithData(byte[] data) {
		if (data.length == 1 && (byte) data[0] == -91) {
			// mState = Msg.MESSAGE_STATE_CONNECTED;
			// onMessage(new Msg(mState, ""));
		} else if (data.length == 5 && data[0] == data[1] && data[1] == -3
				&& data[2] == 6) {
//			if (backStop) {
//				isRecivced = true;
//				backStop = false;
//			}
			toOneoneActivity();
		} else if (data.length == 6 && data[1] == data[2] && data[1] == -3
				&& data[3] == 6) {
//			if (backStop) {
//				isRecivced = true;
//				backStop = false;
//			}
			toOneoneActivity();
			
		}else if (data.length == 7 && data[0] == data[1] && data[1] == -3
				&& data[2] == -5) {
			if (backStop) {
				isRecivced = true;
				backStop = false;
			}
			if(RecievedDataFix){
				toOneoneActivity();
			}
		}else if(RecievedDataFix && data.length > 0){
			toOneoneActivity();
		}
	}

	
	
	
	private void toOneoneActivity() {
		Intent one = new Intent(MainActivity.this, OneoneActivity.class);
		String gname = mSpinnerBtn.getText().toString();
		one.putExtra("gname", gname);
		startActivityForResult(one, 30);
		RecievedDataFix = false;
	}

	@Override
	public BroadcastReceiver getBroadCastReceiver() {
		// TODO Auto-generated method stub
		return mGattUpdateReceiver;
	}

	@Override
	public TextView getTipText() {
		// TODO Auto-generated method stub
		return state;
	}

	@Override
	public String getDeviceName() {
		// TODO Auto-generated method stub
		return "Bluetooth BP";
	}

	@Override
	public String getUUID() {
		// TODO Auto-generated method stub
		return SampleGattAttributes.SERVICE_UU;
	}

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

}
