package com.example.angelsdemo.activity.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.angels.widget.stickynav.ACSimpleViewPagerIndicator;
import com.angels.widget.stickynav.ACSimpleViewPagerIndicator3;
import com.angels.widget.stickynav.listenter.ACListenerConstans;
import com.angels.widget.stickynav.listenter.ACViewPagerListener;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;
import com.example.angelsdemo.activity.fragment.TabFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：angels3
 * 类描述：滑动置顶
 * 创建人：Administrator
 * 创建时间：2017/2/22 11:17
 */

public class StickyNavActivity3 extends BaseActivity {
    private ACSimpleViewPagerIndicator3 mIndicator;
    private ListView lv;
    private LinearLayout llContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav3);
        initViews();
        initDatas();
    }

    private void initDatas() {

    }

    private void initViews() {
        mIndicator = (ACSimpleViewPagerIndicator3) findViewById(R.id.id_stickynavlayout_indicator);
        lv = (ListView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        llContent = (LinearLayout) findViewById(R.id.id_stickynavlayout_buttomview);
		/*
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.id_stickynavlayout_topview);
		TextView tv = new TextView(this);
		tv.setText("我的动态添加的");
		tv.setBackgroundColor(0x77ff0000);
		ll.addView(tv, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 600));
		*/
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
    }
    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            data.add("测试数据"+i);
        }
        return data;
    }
}
