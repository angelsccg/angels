package com.angels.widget.imgscroll;


import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.angels.model.ACImage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片滚动类
 *
 * @author Administrator
 *
 */
public class ACImgScroll extends ViewPager {
	/**
	 * 滑动速度
	 */
	private static final int SCROLL_SPEED = 500;
	private Activity mActivity; // 上下文
	private List<View> mListViews; // 图片组
	private MyPagerAdapter adapter;
	private int mScrollTime = 0;
	private Timer timer;
	private int oldIndex = 0;
	private int curIndex = 0;

	private  LinearLayout ovalLayout;
	private int ovalLayoutId;
	private int scrollTime;
	private int ovalLayoutItemId;
	private int focusedId;
	private int normalId;

	public ACImgScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 开始广告滚动
	 *
	 * @param mainActivity
	 *            显示广告的主界面
	 * @param imgList
	 *            图片列表, 不能为null ,最少一张
	 * @param scrollTime
	 *            滚动间隔 ,0为不滚动
	 * @param ovalLayout
	 *            圆点容器,可为空,LinearLayout类型
	 * @param ovalLayoutId
	 *            ovalLayout为空时 写0, 圆点layout XMl
	 * @param ovalLayoutItemId
	 *            ovalLayout为空时 写0,圆点layout XMl 圆点XMl下View ID
	 * @param focusedId
	 *            ovalLayout为空时 写0, 圆点layout XMl 选中时的动画
	 * @param normalId
	 *            ovalLayout为空时 写0, 圆点layout XMl 正常时背景
	 */
	public void start(Activity mainActivity, List<View> imgList,
					  int scrollTime, LinearLayout ovalLayout, int ovalLayoutId,
					  int ovalLayoutItemId, int focusedId, int normalId,ArrayList<ACImage> list) {
		this.scrollTime = scrollTime;
		this.ovalLayout = ovalLayout;
		this.ovalLayoutId = ovalLayoutId;
		this.ovalLayoutItemId = ovalLayoutItemId;
		this.focusedId = focusedId;
		this.normalId = normalId;
		mActivity = mainActivity;
		mListViews = imgList;
		mScrollTime = scrollTime;
		adapter = new MyPagerAdapter(list);
		this.setAdapter(adapter);// 设置适配器
		// 设置圆点
		setOvalLayout(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId,
				normalId);
		if (scrollTime != 0 && imgList.size() > 1) {
			// 设置滑动动画时间  ,如果用默认动画时间可不用 ,反射技术实现
			controlViewPagerSpeed(SCROLL_SPEED);
//			 new FixedSpeedScroller(mActivity).setmDuration(SCROLL_SPEED);

			startTimer();
			// 触摸时停止滚动
			this.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						startTimer();
					} else {
						stopTimer();
					}
					return false;
				}
			});
		}
		if (mListViews.size() > 1) {
			this.setCurrentItem((Integer.MAX_VALUE / 2)
					- (Integer.MAX_VALUE / 2) % mListViews.size());// 设置选中为中间/图片为和第0张一样
		}
	}
	/**
	 * 滚动速度
	 */
	private void controlViewPagerSpeed(int time) {
		try {
			Field mField;

			mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);

			FixedSpeedScroller mScroller = new FixedSpeedScroller(mActivity,new AccelerateInterpolator());
			mScroller.setmDuration(time); // 速度  ms  
			mField.set(ACImgScroll.this, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 设置圆点
	private void setOvalLayout(final LinearLayout ovalLayout, int ovalLayoutId,
							   final int ovalLayoutItemId, final int focusedId, final int normalId) {
		if (ovalLayout != null) {
			LayoutInflater inflater= LayoutInflater.from(mActivity);
			ovalLayout.removeAllViews();
			for (int i = 0; i < mListViews.size(); i++) {
				ovalLayout.addView(inflater.inflate(ovalLayoutId, null));
			}
			//选中第一个
			ovalLayout.getChildAt(0).findViewById(ovalLayoutItemId).setBackgroundResource(focusedId);
			this.setOnPageChangeListener(new OnPageChangeListener() {
				public void onPageSelected(int i) {
					curIndex = i % mListViews.size();
					//取消圆点选中
					ovalLayout.getChildAt(oldIndex).findViewById(ovalLayoutItemId)
							.setBackgroundResource(normalId);
					//圆点选中
					ovalLayout.getChildAt(curIndex).findViewById(ovalLayoutItemId)
							.setBackgroundResource(focusedId);
					oldIndex = curIndex;
				}

				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}
	}
	/**
	 * 取得当明选中下标
	 * @return
	 */
	public int getCurIndex() {
		return curIndex;
	}
	/**
	 * 停止滚动
	 */
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * 开始滚动
	 */
	public void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						ACImgScroll.this.setCurrentItem(ACImgScroll.this
								.getCurrentItem() + 1);
					}
				});
			}
		}, mScrollTime, mScrollTime);
	}

	/**
	 * 刷新
	 */
	public void refresh(ArrayList<ACImage> list,List<View> imgView){
		stopTimer();
		mListViews = imgView;

		adapter = null;
		adapter = new MyPagerAdapter(list);
		this.setAdapter(adapter);// 设置适配器
//		adapter.notifyDataSetChanged();

		oldIndex = 0;
		// 设置圆点
		setOvalLayout(ovalLayout, ovalLayoutId, ovalLayoutItemId, focusedId,
				normalId);
		startTimer();
	}

	float curX = 0f;
	float downX = 0f;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		curX = ev.getX();
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downX = curX;
		}
		int curIndex = getCurrentItem();
		if (curIndex == 0) {
			if (downX <= curX) {
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		} else if (curIndex == getAdapter().getCount() - 1) {
			if (downX >= curX) {
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		} else {
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		return super.onTouchEvent(ev);
	}


	// 适配器 //循环设置
	private class MyPagerAdapter extends PagerAdapter {

		ImageView imageView;
		private ArrayList<ACImage> list;

		public MyPagerAdapter() {
			super();
			// TODO Auto-generated constructor stub
		}
		public MyPagerAdapter( ArrayList<ACImage> list) {
			super();
			// TODO Auto-generated constructor stub
			this.list=list;

		}
		public void finishUpdate(View arg0) {
		}

		public int getCount() {
			if (mListViews.size() == 1) {// 一张图片时不用流动
				return mListViews.size();
			}
			return Integer.MAX_VALUE;
		}

		public Object instantiateItem(final View v, final int i) {
//			if (((ViewPager) v).getChildCount() == mListViews.size()) {

			((ViewPager) v).removeView(mListViews.get(i % mListViews.size()));

			if(list==null||list.size()==0){

			}else{
				imageView = (ImageView) mListViews.get(i % mListViews.size());
/*					Drawable drawable = asyncLoadImage.loadDrawable(list
							.get(i % mListViews.size()).getImgUrl(),
							new AsyncLoadImage.ImageCallback() {
						@Override
						public void imageLoad(Drawable image,
								String imageUrl) {
							imageView.setImageDrawable(image);
						}
					});
*/
				if(listener!=null){
					listener.imageLoading(list.get(i % mListViews.size()).getImgUrl(),imageView);
				}
			}
			((ViewPager) v).addView(mListViews.get(i % mListViews.size()), 0);

			return mListViews.get(i % mListViews.size());
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {
		}

		public void destroyItem(View v, int i, Object arg2) {
		}

		//		private int mChildCount = 0;
		@Override
		public void notifyDataSetChanged() {
//			mChildCount = getCount();
			super.notifyDataSetChanged();
		}

		public ArrayList<ACImage> getList() {
			return list;
		}

		public void setList(ArrayList<ACImage> list) {
			this.list = list;
		}
	}

	public interface ImageLoaderListener{
		public void imageLoading(String imgUrl, ImageView imageView);
	}

	private ImageLoaderListener listener;

	public void setOnImageLoaderListener(ImageLoaderListener listener){
		this.listener = listener;
	}

}

