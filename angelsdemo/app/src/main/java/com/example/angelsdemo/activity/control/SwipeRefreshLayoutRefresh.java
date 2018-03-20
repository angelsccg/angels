package com.example.angelsdemo.activity.control;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import java.util.ArrayList;

/**
 * 项目名称：Angels_Demo
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/3/28 16:05
 */
public class SwipeRefreshLayoutRefresh extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh2);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData()));

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        mSwipeLayout.setDistanceToTriggerSync(200);// 设置手指在屏幕下拉多少距离会触发下拉刷新
//        mSwipeLayout.setProgressBackgroundColor(R.color.white_ac);
//        mSwipeLayout.setProgressBackgroundColorSchemeResource(R.color.halpha_ac);
//        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
    }

    private ArrayList<String> getData() {
        list.add("Hello");
        list.add("This is stormzhang=========================");
        list.add("An Android Developer");
        list.add("Love Open Source");
        list.add("weibo: googdev");
        list.add("weibo: googdev");
        list.add("weibo: googdev");
        list.add("weibo: googdev");
        list.add("weibo: googdev");
        list.add("Hello");
        list.add("This is stormzhang=========================");
        list.add("An Android Developer");
        list.add("Love Open Source"); list.add("Hello");
        list.add("This is stormzhang=========================");
        list.add("An Android Developer");
        list.add("Love Open Source"); list.add("Hello");
        list.add("This is stormzhang=========================");
        list.add("An Android Developer");
        list.add("Love Open Source"); list.add("Hello");
        list.add("This is stormzhang=========================");
        list.add("An Android Developer");
        list.add("Love Open Source");
        list.add("111111111111111111111111111111111111111111");
        return list;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                mSwipeLayout.setRefreshing(false);
            }
        }, 5000); // 5秒后发送消息，停止刷新
    }
}
