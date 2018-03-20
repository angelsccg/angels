package com.example.angelsdemo.activity.control;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angels.util.ACToast;
import com.angels.widget.stickynav.ACSimpleViewPagerIndicator3;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：angels3
 * 类描述：滑动置顶
 * 创建人：Administrator
 * 创建时间：2017/2/22 11:17
 */

public class StickyNavActivity4 extends BaseActivity implements View.OnClickListener{
    private ACSimpleViewPagerIndicator3 mIndicator;
//    private ListView lv;
    private LinearLayout llContent;

    private ScrollView sv;

    private TextView tv01,tv02,tv03,tv04;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav4);
        initViews();
        initDatas();
    }

    private void initDatas() {

    }

    private void initViews() {
        mIndicator = (ACSimpleViewPagerIndicator3) findViewById(R.id.id_stickynavlayout_indicator);
//        lv = (ListView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        sv = (ScrollView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        llContent = (LinearLayout) findViewById(R.id.id_stickynavlayout_buttomview);
		/*
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.id_stickynavlayout_topview);
		TextView tv = new TextView(this);
		tv.setText("我的动态添加的");
		tv.setBackgroundColor(0x77ff0000);
		ll.addView(tv, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 600));
		*/
//        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));

        Button btn01 = (Button) findViewById(R.id.btn01);
        Button btn02 = (Button) findViewById(R.id.btn02);
        Button btn03 = (Button) findViewById(R.id.btn03);
        Button btn04 = (Button) findViewById(R.id.btn04);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn03.setOnClickListener(this);
        btn04.setOnClickListener(this);

         tv01 = (TextView) findViewById(R.id.tv01);
         tv02 = (TextView) findViewById(R.id.tv02);
         tv03 = (TextView) findViewById(R.id.tv03);
         tv04 = (TextView) findViewById(R.id.tv04);
    }
    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            data.add("测试数据"+i);
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn01:
                scrollTo(0,tv01.getTop());
                break;
            case R.id.btn02:
                scrollTo(0 ,tv02.getTop());
                break;
            case R.id.btn03:
                scrollTo(0, tv03.getTop());
                break;
            case R.id.btn04:
                scrollTo(0,tv04.getTop());
                break;
        }
    }

    private void scrollTo(int x,int y){
        sv.scrollTo(x,y);
    }
}
