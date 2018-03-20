package com.example.angelsdemo.activity.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.angels.http.AsyncHttpClient;
import com.angels.http.AsyncHttpResponseHandler;
import com.angels.util.ACLog;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.activity.BaseActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GPRSActivity extends BaseActivity{
	String url = "https://www.baidu.com/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button btn = new Button(this);
		btn.setText("点击获取网络 "+url+" 的数据");
		setContentView(btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new ServerThread().start();
			}
		});
	}
	class ServerThread extends Thread{
		public void run(){
			try{
				DatagramSocket socket = new DatagramSocket(1234);
				InetAddress serverAddress = InetAddress.getByName("115.238.249.92");
				String str = "hello";
				byte data[] = str.getBytes();
				DatagramPacket packet = new DatagramPacket(data,data.length,serverAddress,1234);
				socket.send(packet);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
