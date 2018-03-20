package com.example.angelsdemo.activity.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.angels.widget.stickynav.ACSimpleViewPagerIndicator;
import com.angels.widget.stickynav.ACSimpleViewPagerIndicator3;
import com.angels.widget.stickynav.listenter.ACListenerConstans;
import com.angels.widget.stickynav.listenter.ACViewPagerListener;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;
import com.example.angelsdemo.activity.fragment.TabFragment;


/**
 * 项目名称：angels3
 * 类描述：滑动置顶
 * 创建人：Administrator
 * 创建时间：2017/2/22 11:17
 */

public class StickyNavActivity extends BaseActivity implements ACViewPagerListener {
    private String[] mTitles = new String[]{100 + "\n晒物", "100\n众测", "100\n关注", "100\n粉丝"};
    private ACSimpleViewPagerIndicator3 mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private TabFragment[] mFragments = new TabFragment[mTitles.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_nav);
        ACListenerConstans.mQunZuPager = this;
        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initDatas() {
        mIndicator.setTitles(mTitles);
        mFragments[0] = new TabFragment(0);
        mFragments[1] = new TabFragment(1);
        mFragments[2] = new TabFragment(2);
        mFragments[3] = new TabFragment(3);
        //        for (int i = 0; i < mTitles.length; i++) {
        //            mFragments[i] = TabFragment.newInstance(mTitles[i]);
        //        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void initViews() {
        mIndicator = (ACSimpleViewPagerIndicator3) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_buttomview);

		/*
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.id_stickynavlayout_topview);
		TextView tv = new TextView(this);
		tv.setText("我的动态添加的");
		tv.setBackgroundColor(0x77ff0000);
		ll.addView(tv, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 600));
		*/
    }

    @Override
    public void setCurrentItem(int page) {
        mViewPager.setCurrentItem(page);
    }
}
