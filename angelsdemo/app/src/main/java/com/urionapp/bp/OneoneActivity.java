package com.urionapp.bp;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.angelsdemo.R;
import com.example.urionbean.Data;
import com.example.urionbean.Error;
import com.example.urionbean.Head;
import com.example.urionbean.IBean;
import com.example.urionbean.Msg;
import com.example.urionbean.Pressure;
import com.example.urionclass.L;
import com.example.urionclass.SampleGattAttributes;
import com.example.uriondb.DBOpenHelper;
import com.example.urionservice.BluetoothLeService;
import com.example.urionuntil.CodeFormat;

public class OneoneActivity extends Activity implements OnClickListener {
	private ImageButton stop, home, thread, history;
	private Button user;
	private ImageView main_imageview;
	private TextView sdp, state, xs;
	private ProgressBar progressbar;
	private String ggname;
	private DBOpenHelper dbOpenHelper;

	private int i = 0;
	Timer timer = new Timer();

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (i > 2) {
				i = 0;
			} else {
				switch (i) {
				case 1:
					main_imageview.setImageResource(R.drawable.heart2);
					break;
				case 2:
					main_imageview.setImageResource(R.drawable.heart);
					break;

				default:
					break;
				}
				sdp.invalidate();
			}
			super.handleMessage(msg);
		}
	};
	private boolean isBleseviceRegiste;
	private ImageView blue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout1);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		
		registerReceiver(mGattUpdateReceiver , intentFilter);
		isBleseviceRegiste = true;
		
		blue = (ImageView) this.findViewById(R.id.blue);
		stop = (ImageButton) this.findViewById(R.id.stop);
		home = (ImageButton) this.findViewById(R.id.home);
		thread = (ImageButton) this.findViewById(R.id.treads);
		history = (ImageButton) this.findViewById(R.id.history1);
		sdp = (TextView) this.findViewById(R.id.mmhg);
		// xs = (TextView) this.findViewById(R.id.state);
		ggname = getIntent().getExtras().getString("gname");
		user = (Button) this.findViewById(R.id.user);
		user.setText(ggname);
		progressbar = (ProgressBar) this.findViewById(R.id.progress);
		progressbar.setMax(300);
		stop.setOnClickListener(this);
		thread.setOnClickListener(this);
		history.setOnClickListener(this);
		// sdp.setPadding(255, 500, 255,10);
		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.stop:
			sendBroadcast(new Intent(SampleGattAttributes.DISCONNECTEDBLE).putExtra("stop", true));
			Intent one = new Intent();
			one.putExtra("bname", ggname);
			setResult(21, one);
			timer.cancel();
			OneoneActivity.this.finish();
			break;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;

		}

		return super.dispatchKeyEvent(event);
	}

	public void initView() {
		// li = (LinearLayout) findViewById(R.id.inmages);
		main_imageview = (ImageView) findViewById(R.id.heart);

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				i++;
				Message mesasge = new Message();
				mesasge.what = i;
				handler.sendMessage(mesasge);

			}
		}, 0, 250);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isBleseviceRegiste){
			unregisterReceiver(mGattUpdateReceiver);
		}
		OneoneActivity.this.finish();
	}

	public void onReceive(IBean bean) {
		switch (bean.getHead().getType()) {
		case Head.TYPE_PRESSURE:
			progressbar.setProgress(((Pressure) bean).getPressure());
			sdp.setText(((Pressure) bean).getPressureHL() + "");
			break;
		case Head.TYPE_RESULT:
			Data data = (Data) bean;
			ContentValues value = new ContentValues();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm aaa");
			Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
			String str = formatter.format(curDate);
			int sys = data.getSys();
			int dia = data.getDia();
			int pul = data.getPul();

			Intent intent = new Intent(OneoneActivity.this, OneActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", ggname);
			bundle.putString("time", str);
			bundle.putInt("sys", sys);
			bundle.putInt("dia", dia);
			bundle.putInt("pul", pul);
			/* ��bundle����assign��Intent */
			intent.putExtras(bundle);
			startActivity(intent);
			
			sendBroadcast(new Intent(SampleGattAttributes.DISCONNECTEDBLE));
			finish();
		}
	}

	public void onError(Error error) {
		/*if (error.getHead() == null)
			Toast.makeText(
					this,
					getResources().getStringArray(R.array.connect_message)[error
							.getError_code()], Toast.LENGTH_SHORT).show();
		else*/
			sendBroadcast(new Intent(SampleGattAttributes.DISCONNECTEDBLE));
		
			switch (error.getError()) {
			case Error.ERROR_EEPROM:
				new AlertDialog.Builder(OneoneActivity.this)
						.setMessage(
								"The blood pressure monitor is abnormal, please contact the dealer")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Intent one = new Intent();
										one.putExtra("bname", ggname);
										setResult(20, one);
										OneoneActivity.this.finish();
									}

								}).show();
				break;
			case Error.ERROR_HEART:
				new AlertDialog.Builder(OneoneActivity.this)
						.setMessage(
								"Incorrect measurement, please follow the instruction manual, then please re-wrap the cuff, keep quiet and remeasure.")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Intent one = new Intent();
										one.putExtra("bname", ggname);
										setResult(20, one);
										OneoneActivity.this.finish();
									}

								}).show();
				break;
			case Error.ERROR_DISTURB:
				// Toast.makeText(this, "E-2 ��Ѷ����!", Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(OneoneActivity.this)
						.setMessage(
								"Incorrect measurement, please follow the instruction manual, then please re-wrap the cuff, keep quiet and remeasure.")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Intent one = new Intent();
										one.putExtra("bname", ggname);
										setResult(20, one);
										OneoneActivity.this.finish();
									}

								}).show();
				break;
			case Error.ERROR_GASING:
				// Toast.makeText(this, "E-3 ����ʱ�����!",
				// Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(OneoneActivity.this)
						.setMessage(
								"Incorrect measurement, please follow the instruction manual, then please re-wrap the cuff, keep quiet and remeasure.")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Intent one = new Intent();
										one.putExtra("bname", ggname);
										setResult(20, one);
										finish();
									}

								}).show();
				break;
			case Error.ERROR_TEST:
				new AlertDialog.Builder(OneoneActivity.this)
						.setMessage(
								"Incorrect measurement, please follow the instruction manual, then please re-wrap the cuff, keep quiet and remeasure.")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Intent one = new Intent();
										one.putExtra("bname", ggname);
										setResult(20, one);
										OneoneActivity.this.finish();
									}

								}).show();
				break;
			case Error.ERROR_REVISE:
				// Toast.makeText(this, "E-C У���쳣!", Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(OneoneActivity.this)
						.setMessage(
								"Incorrect measurement, please follow the instruction manual, then please re-wrap the cuff, keep quiet and remeasure.")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Intent one = new Intent();
										one.putExtra("bname", ggname);
										setResult(20, one);
										OneoneActivity.this.finish();
									}

								}).show();
				break;
			case Error.ERROR_POWER:
				// Toast.makeText(this, "E-B ��Դ�͵�ѹ!",
				// Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(OneoneActivity.this)
						.setMessage(
								"Low batteries, please replace the batteries.")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Intent one = new Intent();
										one.putExtra("bname", ggname);
										setResult(20, one);
										OneoneActivity.this.finish();
									}

								}).show();
				break;
			}
	}
	
	
	
	
	/*
	 * �����㲥�࣬����ʵʩ�Ľ�������
	 */
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				// ��ȡ���ͼ������
				L.d("ble get data ");
				byte[] data = intent.getExtras().getByteArray("data");
				L.d(Arrays.toString(data));
				if(data.length >=6){
					doWithData(data);
				}
			}else if(BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)){
				blue.setImageResource(R.drawable.bluetooth);
			}else if(BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)){
				blue.setImageResource(R.drawable.bluetoothno);
			}
		}
	};

	protected void doWithData(byte[] buffer) {
		// TODO Auto-generated method stub
		Head head = new Head();
		int[] f = CodeFormat.bytesToHexStringTwo(buffer, 6);
		head.analysis(f);
		if (head.getType() == Head.TYPE_ERROR) {
			// APP���յ�Ѫѹ�ǵĴ�����Ϣ

			Error error = new Error();
			error.analysis(f);
			error.setHead(head);
			// ǰ̨���ݴ��������ʾ��Ӧ����ʾ
			
			onError(error);
			
			
			
		}
		if (head.getType() == Head.TYPE_RESULT) {
			// APP���յ�Ѫѹ�ǵĲ������
			Data data = new Data();
			data.analysis(f);
			data.setHead(head);
			// ǰ̨���ݲ��Խ����������ͼ
			onReceive(data);
//			send(IBean.DATA, data);
		}

		if (head.getType() == Head.TYPE_MESSAGE) {
			// APP���յ�Ѫѹ�ǿ�ʼ������֪ͨ
			Msg msg = new Msg();
			msg.analysis(f);

			msg.setHead(head);
			onReceive(msg);
			//send(IBean.MESSAGE, msg);
		}
		if (head.getType() == Head.TYPE_PRESSURE) {
			// APP���ܵ�Ѫѹ�ǲ�����ѹ������
			Pressure pressure = new Pressure();
			pressure.analysis(f);
			pressure.setHead(head);
			// ÿ���յ�һ�����ݾͷ��͵�ǰ̨���Ըı����������ʾ
			onReceive(pressure);
//			send(IBean.DATA, pressure);
	}
	}
	
	
}