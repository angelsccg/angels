package com.example.angelsdemo;

import com.angels.imageselector.imageloader.ImageSelectorActivity;
import com.angels.util.ACToastUtils;
import com.angels.widget.ACCircleImageView;
import com.angels.widget.RadarView;
import com.example.angelsdemo.R.drawable;
import com.example.angelsdemo.R.id;
import com.example.angelsdemo.R.layout;
import com.example.angelsdemo.activity.AnimActivity;
import com.example.angelsdemo.activity.AnotherActivity;
import com.example.angelsdemo.activity.BaseActivity;
import com.example.angelsdemo.activity.CacheActivity;
import com.example.angelsdemo.activity.ControlActivity;
import com.example.angelsdemo.activity.DBActivity;
import com.example.angelsdemo.activity.DrawActivity;
import com.example.angelsdemo.activity.HttpActivity;
import com.example.angelsdemo.activity.JniActivity;
import com.example.angelsdemo.activity.OpenOtherAppActivity;
import com.example.angelsdemo.activity.QRCodeActivity;
import com.example.angelsdemo.activity.TabHostActivity;
import com.example.angelsdemo.activity.TestActivity;
import com.example.angelsdemo.activity.ToolsActivity;
import com.example.angelsdemo.activity.VideoActivity;
import com.example.angelsdemo.activity.bluetooth.BluetoothGattActivity;
import com.example.angelsdemo.service.DialogService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends BaseActivity implements OnClickListener{

	public static final String[] btnNames= {"控件","工具","动画","第三方架包","http","jni","绘图","数据库","TabHost","打开第三方应用","二维码(包含一维码)","视频","缓存","蓝牙","测试的"};
	public static final Button[] btns = new Button[btnNames.length];
	
	private LinearLayout llContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_mainc);

//		ACCircleImageView circleImageView = (ACCircleImageView) findViewById(id.circle);
//		ImageView imageView = (ImageView) findViewById(id.circle);
//		Bitmap bitmap = getImageBitmap("http://up.qqjia.com/z/24/tu29253_9.jpg");
//		circleImageView.setImageBitmap(bitmap);
//		imageView.setImageBitmap(bitmap);



        RadarView mRadarView = (RadarView) findViewById(R.id.rv);
        mRadarView.setSearching(true);
        mRadarView.addPoint();
        mRadarView.addPoint();



        llContent = (LinearLayout) findViewById(id.llContent);

        for (int i = 0; i < btnNames.length; i++) {
			Button button = new Button(this);
			button.setText(btnNames[i]);
			button.setId(i);
			button.setOnClickListener(this);
			llContent.addView(button);
			btns[i] = button;
		}
        initSlidingMenu();
        initIntentData();
//		ACLog.i("ccg---->2333333333333333333333333333333333");
		if (savedInstanceState != null) {
			String value = savedInstanceState.getString("key");
			ACToastUtils.showMessage(this,"savedInstanceState:"+value);
		}

		int badgeCount = 10;
		ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+

//		ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3
	}

	public Bitmap getImageBitmap(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse resp = httpclient.execute(httpget);
			// 判断是否正确执行
			if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
				// 将返回内容转换为bitmap
				HttpEntity entity = resp.getEntity();
				InputStream in = entity.getContent();
				Bitmap mBitmap = BitmapFactory.decodeStream(in);
				// 向handler发送消息，执行显示图片操作
				return mBitmap;
			}

		} catch (Exception e) {
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return null;
	}
    private void initIntentData(){
    	//通过网页链接ac://aa.bb:80/test?p=12&d=1 打开此app的获取的相关数据
    	((TextView)findViewById(id.textView1)).setText("ac://aa.bb:80/test?p=12&d=1");
    	Intent intent = getIntent();  
        String scheme = intent.getScheme();  
        Uri uri = intent.getData();  
        System.out.println("scheme:"+scheme);  
        if (uri != null) {  
            String host = uri.getHost();  
            String dataString = intent.getDataString();  
            String id = uri.getQueryParameter("d");  
            String path = uri.getPath();  
            String path1 = uri.getEncodedPath();  
            String queryString = uri.getQuery();  
            System.out.println("host:"+host);  
            System.out.println("dataString:"+dataString);  
            System.out.println("id:"+id);  
            System.out.println("path:"+path);  
            System.out.println("path1:"+path1);  
            System.out.println("queryString:"+queryString);  
        }  
    }
    /**
     * 初始化侧滑菜单
     */
    private void initSlidingMenu(){
    	SlidingMenu menu = new SlidingMenu(this);
	     // 滑动方向(LEFT,RIGHT,LEFT_RIGHT)
	     menu.setMode(SlidingMenu.LEFT);
	     /*
	      *  设置触摸屏幕的模式   
	      */
	     //滑动显示SlidingMenu的范围  TOUCHMODE_FULLSCREEN 全屏，  TOUCHMODE_MARGIN 边缘，TOUCHMODE_NONE 无
	     menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	     //设置影子宽度
	     menu.setShadowWidth(200);
	     //s设置影子图片
	     menu.setShadowDrawable(drawable.test_title);
	     // 设置渐入渐出效果的值  
	     menu.setFadeDegree(0.35f); 
	     // 设置滑动菜单视图的宽度  
	     menu.setBehindOffset(200);  
	     // 菜单的宽度
//	     menu.setBehindWidth(getResources().getDimensionPixelSize(200));
	     menu.setBehindWidth(400);
	     // 把SlidingMenu附加在Activity上
	     // SlidingMenu.SLIDING_WINDOW:菜单拉开后高度是全屏的
	     // SlidingMenu.SLIDING_CONTENT:菜单拉开后高度是不包含Title/ActionBar的内容区域
	     menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	     // 菜单的布局文件
	     menu.setMenu(layout.activity_guaguaka);
	     // 监听slidingmenu打开
	     menu.setOnOpenedListener(new OnOpenedListener() {
	         @Override
	         public void onOpened() {
	        	 Toast.makeText(MainActivity.this, "打开侧边菜单", Toast.LENGTH_SHORT).show();
	         }
	     });
	     // 监听slidingmenu关闭
	     menu.setOnClosedListener(new OnClosedListener() {
	         @Override
	         public void onClosed() {
	        	 Toast.makeText(MainActivity.this, "关闭侧边菜单", Toast.LENGTH_SHORT).show();
	         }
	     });
	     // 显示SlidingMenu
	     menu.showMenu(true);

	     // 关闭SlidingMenu
	     menu.toggle();
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 0:
		{
			Intent intent = new Intent(this, ControlActivity.class);
			startActivity(intent);
		}
			break;
		case 1:
		{	
			Intent intent = new Intent(this, ToolsActivity.class);
			startActivity(intent);
		}
			break;
		case 2:
		{
			Intent intent = new Intent(this, AnimActivity.class);
			startActivity(intent);
		}
			break;
		case 3:
		{
			Intent intent = new Intent(this, AnotherActivity.class);
			startActivity(intent);
			break;
		}
		case 4:
		{
			Intent intent = new Intent(this, HttpActivity.class);
			startActivity(intent);
		}
			break;
		case 5:
		{
			Intent intent = new Intent(this, JniActivity.class);
			startActivity(intent);
		}
			break;
		case 6:
		{
			Intent intent = new Intent(this, DrawActivity.class);
			startActivity(intent);
		}
			break;
		case 7:
		{
			Intent intent = new Intent(this, DBActivity.class);
			startActivity(intent);
		}
			break;
			case 8:
			{
				Intent intent = new Intent(this, TabHostActivity.class);
				startActivity(intent);
			}
			break;
			case 9:
			{
				Intent intent = new Intent(this, OpenOtherAppActivity.class);
				startActivity(intent);
			}
			break;
			case 10:
			{
				Intent intent = new Intent(this, QRCodeActivity.class);
				startActivity(intent);
			}
			break;
			case 11:
			{
				Intent intent = new Intent(this, VideoActivity.class);
				startActivity(intent);
			}
			break;
			case 12:
			{
				Intent intent = new Intent(this, CacheActivity.class);
				startActivity(intent);
			}
			break;
			case 13:
			{
				Intent intent = new Intent(this, BluetoothGattActivity.class);
				startActivity(intent);
			}
			break;
			case 14://测试的
			{
//				toActivity();
                Toast.makeText(this, "血压", Toast.LENGTH_SHORT).show();
				Intent startIntent = new Intent(this, com.urionapp.bp.MainActivity.class);
				startActivity(startIntent);
//				Intent startIntent = new Intent(this, DialogService.class);
//				startService(startIntent);
//				Intent intent = new Intent(this, TestActivity.class);
//				startActivity(intent);
				//===============
//				this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//				this.updateNotification = new Notification();
//				//设置通知栏显示内容
//				updateNotification.icon = R.drawable.ic_launcher;
////				updateNotification.setLatestEventInfo(this, "正在下载", i++, updatePendingIntent);
//				updateNotificationManager.notify(0, updateNotification);
				//==================
//				try {
//					Intent i = new Intent(Intent.ACTION_VIEW);
//					i.setData(Uri.parse("market://details?id="+getPackageName()));
//					startActivity(i);
//				} catch (Exception e) {
//					Toast.makeText(this, "您的手机上没有安装Android应用市场", Toast.LENGTH_SHORT).show();
//					e.printStackTrace();
//				}
//				HashMap<String,String> map = new HashMap<String,String>();
//				map.put(ACWebActivity.WEBVIEW_TYPE,"horizontal_no_all");
//				ACWeb.showWeb(this,"file:///android_asset/love/index.html",map,0);
			}
			break;
		default:
			break;
		}
	}

	public void toActivity(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent startIntent = new Intent(MainActivity.this, DialogService.class);
				startService(startIntent);
			}
		},5*1000);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		ACToastUtils.showMessage(this,"onSaveInstanceState");
		outState.putString("key","保存的数据");
	}
}
