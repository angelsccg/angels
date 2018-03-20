package com.example.angelsdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.angels.model.ACRecordMap;
import com.angels.util.ACToastUtils;
import com.angels.web.ui.ACWebActivity;
import com.angels.web.util.ACWeb;
import com.angels.widget.ACDoubleDatePickerDialog;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.control.DialogActivity;
import com.example.angelsdemo.activity.control.GuaguakaActivity;
import com.example.angelsdemo.activity.control.ImgScroll2Activity;
import com.example.angelsdemo.activity.control.ImgScrollActivity;
import com.example.angelsdemo.activity.control.MenuActivity;
import com.example.angelsdemo.activity.control.RefreshAndLoadMoreActivity;
import com.example.angelsdemo.activity.control.SeldingFinishActivity;
import com.example.angelsdemo.activity.control.SwipeMenuListviewActivity;
import com.example.angelsdemo.activity.control.SwipeRefreshLayoutRefresh;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

/**
 * 项目名称：angels3
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/11/3 15:12
 */

public class OpenOtherAppActivity extends BaseActivity implements View.OnClickListener {
    public static final String[] btnNames= {"百度地图app","高德地图app","Google地图app","应用市场-打开应用详情页","应用市场-在应用市场查找app"};
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
                openBaiduMap();
            }
                break;
            case 1:
            {
                openGaoDeMap();
            }
            case 2:
            {
                openGoogleMap();
            }
            case 3:
            {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://details?id="+getPackageName()));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "您的手机上没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            case 4:
            {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("market://search?q="+"273二手车"));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, "您的手机上没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            default:
                break;
        }
    }

    private void openBaiduMap(){
        try {
            Intent intent = Intent.getIntent("intent://map/marker?location=40.047669,116.313082&title=我的位置&content=百度奎科大厦&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if(isInstallByread("com.baidu.BaiduMap")){
                startActivity(intent); //启动调用
                Log.e("GasStation", "百度地图客户端已经安装") ;
            }else{
                Log.e("GasStation", "没有安装百度地图客户端") ;
                ACToastUtils.showMessage(this,"没有安装百度地图客户端");

                String url = "http://api.map.baidu.com/marker?location="+40.047669+","+116.313082+"&title="+"angels百度奎科大厦"+"&content="+"地址233333"+"&output=html&";
                System.out.println("map-->百度地图web："+url);
                Uri uri = Uri.parse(url);

                intent = new Intent(Intent.ACTION_VIEW, uri);
                this.startActivity(intent);
            }
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void openGaoDeMap()
    {
        try {
            Intent intent = Intent.getIntent("androidamap://viewMap?sourceApplication=厦门通&poiname=百度奎科大厦&lat=40.047669&lon=116.313082&dev=0");
            if(isInstallByread("com.autonavi.minimap")){
                startActivity(intent); //启动调用
                Log.e("GasStation", "高德地图客户端已经安装") ;
            }else{
                Log.e("GasStation", "没有安装高德地图客户端") ;
                ACToastUtils.showMessage(this,"没有安装高德地图客户端");

                Uri uri = Uri.parse("http://m.amap.com/?q="+40.047669+","+116.313082+"&src=273二手车&name="+"angels百度奎科大厦");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private void openGoogleMap()
    {
        try {
            if(isInstallByread("com.google.android.apps.maps")){
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=39.940409,116.355257(西直门)"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(i);
                Log.e("GasStation", "Google地图客户端已经安装") ;
            }else{
                Log.e("GasStation", "没有安装Google地图客户端") ;
                ACToastUtils.showMessage(this,"没有安装Google地图客户端");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
