package com.example.angelsdemo.activity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.tool.EditJustfyActivity;
import com.example.angelsdemo.activity.tool.TextJustfyActivity;

/**
 * 项目名称：angels3
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/5/30 11:24
 */
public class TabHostActivity extends TabActivity implements View.OnClickListener{
    private TabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //调用TabActivity的getTabHost()方法获取TabHost对象
        TabHost tabHost = getTabHost();
        //设置使用TabHost布局
        LayoutInflater.from(this).inflate(R.layout.layout, tabHost.getTabContentView(), true);
        //添加第一个标签页
        tabHost.addTab(tabHost.newTabSpec("tab01").setIndicator("已接电话").setContent(R.id.tab01));
        //添加第二个标签页,并在其标签上添加一个图片
        tabHost.addTab(tabHost.newTabSpec("tab02").setIndicator("未接电话", getResources().getDrawable(R.drawable.ic_launcher)).setContent(R.id.tab02));
        //添加第三个标签页
        tabHost.addTab(tabHost.newTabSpec("tab03").setIndicator("已拨电话").setContent(R.id.tab03));
    }


    @Override
    public void onClick(View v) {
    }
}
