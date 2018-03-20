package com.example.angelsdemo.activity.control;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：angels3
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/9/13 11:24
 */
public class ImgScroll2Activity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private List<View> views = new ArrayList<View>();

    private LinearLayout container;
    private TextView tvTitle;
    private LayoutInflater inflater;

    private String dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgscroll2);

        inflater = getLayoutInflater();
        container = (LinearLayout) findViewById(R.id.container);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tvTitle = (TextView) findViewById(R.id.title);

        //这里预先在sdcard准备一些轮播图片资源
        dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        // 得到views集合
        views = new ArrayList<View>();
        //此处可以根据需要自由设定，这里只是简单的测试
        for (int i = 1; i <= 12; i++) {
            View view = inflater.inflate(R.layout.part_img, null);
            views.add(view);
        }

        /////////////////////主要配置//////////////////////////////////////

        // 1.设置幕后item的缓存数目
        viewPager.setOffscreenPageLimit(3);
        // 2.设置页与页之间的间距
        viewPager.setPageMargin(5);
        // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });

        ////////////////////////////////////////////////////////////////
        viewPager.setAdapter(new MyAdapter()); // 为viewpager设置adapter
        viewPager.setOnPageChangeListener(this);// 设置监听器
    }

    // PagerAdapter是object的子类
    class MyAdapter extends PagerAdapter {

        /**
         * PagerAdapter管理数据大小
         */
        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * 关联key 与 obj是否相等，即是否为同一个对象
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj; // key
        }

        /**
         * 销毁当前page的相隔2个及2个以上的item时调用
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key
        }

        /**
         * 当前的page的前一页和后一页也会被调用，如果还没有调用或者已经调用了destroyItem
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            System.out.println("pos:" + position);
            View view = views.get(position);
            // 如果访问网络下载图片，此处可以进行异步加载
            ImageView img = (ImageView) view.findViewById(R.id.img);
//            img.setImageBitmap(BitmapFactory.decodeFile(dir + getFile(position)));
            container.addView(view);

//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    viewPager.setCurrentItem(position-1);
//                    Toast.makeText(v.getContext(),"ccg"+position,Toast.LENGTH_SHORT).show();
//
//                    setSelect(position);
//                }
//            });
            return views.get(position); // 返回该view对象，作为key
        }
    }

    private void setSelect(int position){
        for (int i = 0; i < views.size(); i++) {
            ImageView img = (ImageView) views.get(i).findViewById(R.id.img);
            img.setBackgroundColor(getResources().getColor(R.color.green_ac));
            if(position == i){
                img.setBackgroundColor(getResources().getColor(R.color.blue_ac));
            }
        }

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (viewPager != null) {
            viewPager.invalidate();
        }
    }

    // 一个新页被调用时执行,仍为原来的page时，该方法不被调用
    public void onPageSelected(int position) {
        tvTitle.setText(getFile(position));
    }

    /*
     * SCROLL_STATE_IDLE: pager处于空闲状态 SCROLL_STATE_DRAGGING： pager处于正在拖拽中
     * SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
     */
    public void onPageScrollStateChanged(int state) {

    }

    public String getFile(int position) {
        return "/img" + ((position + 1) >= 10 ? (position + 1) : "0" + (position + 1)) + ".png";
    }
}
