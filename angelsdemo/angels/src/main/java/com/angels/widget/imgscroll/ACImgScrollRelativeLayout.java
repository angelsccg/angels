package com.angels.widget.imgscroll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.angels.R;
import com.angels.model.ACImage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @ClassName: ACImgScrollRelativeLayout
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 * 使用方法
private void initImagePage(){
ACImgScrollRelativeLayout imgScrollRelativeLayout = (ACImgScrollRelativeLayout) findViewById(R.id.imgScroll);
// 开始滚动
imgScrollRelativeLayout.start(this, list,4000,ScaleType.CENTER_CROP);
//点击监听
imgScrollRelativeLayout.setOnImgScrollClickListener(new ImgScrollClickListener() {
@Override
public void click(View v, ACImage acImage, int position) {
Toast.makeText(v.getContext(), acImage.getImgUrl(), 0).show();
}
});
//图片载入监听
imgScrollRelativeLayout.getMyPager().setOnImageLoaderListener(new ImageLoaderListener() {
@Override
public void imageLoading(String imgUrl, ImageView imageView) {
ACImageLoader.getInstance().LoadImage(imgUrl,imageView);
}
});
}
 *
 *
 */
public class ACImgScrollRelativeLayout extends RelativeLayout {
	public final static String TAG = "ACImgScrollRelativeLayout";

	/**
	 * 图片滚动展示
	 * */
	private ACImgScroll myPager; // 图片容器
	private LinearLayout ovalLayout; // 圆点容器

	private ScaleType type;

	public ACImgScrollRelativeLayout(Context context) {
		super(context);
		init();
	}

	public ACImgScrollRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ACImgScrollRelativeLayout(Context context, AttributeSet attrs,
								   int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("NewApi")
	public ACImgScrollRelativeLayout(Context context, AttributeSet attrs,
								   int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.ac_part_imgscroll, null);
		addView(view);

		myPager = (ACImgScroll) findViewById(R.id.ac_ad);
		ovalLayout = (LinearLayout) findViewById(R.id.ac_vb);
	}

	/**
	 * 开始滚动
	 */
	/**
	 *
	 * @param acitvity
	 * @param list
	 *            图片集合
	 * @param scrollTime
	 *            滚动时间
	 */
	public void start(Activity acitvity, ArrayList<ACImage> list, int scrollTime) {
		start(acitvity, list, scrollTime,null);
	}
	/**
	 *
	 * @param acitvity
	 * @param list
	 *            图片集合
	 * @param scrollTime
	 *            滚动时间
	 * @param type
	 *            图片显示类型
	 */
	public void start(Activity acitvity, ArrayList<ACImage> list, int scrollTime,ScaleType type) {
		this.type = type;
		// 开始滚动
		myPager.start(acitvity, initViewPager(list, type), scrollTime, ovalLayout,
				R.layout.ac_item_ad_bottom, R.id.ac_ad_item_v,
				R.drawable.ac_dot_focused, R.drawable.ac_dot_normal, list);
		// 开始滚动
//		myPager.start(acitvity, initViewPager(list, type), scrollTime, ovalLayout,
//				R.layout.item_ad_bottom, R.id.ac_ad_item_v,
//				R.drawable.ac_dot_focused, R.drawable.ac_dot_normal, list);
	}

	/**
	 * 初始化图片控件
	 */
	private List<View> initViewPager(final ArrayList<ACImage> list,ScaleType scaleType) {
		List<View> views = new ArrayList<View>();
		if (list == null || list.size() == 0) {
			int[] imageResId = new int[] { R.drawable.ac_default };
			for (int i = 0; i < imageResId.length; i++) {
				ImageView imageView = new ImageView(getContext());
				imageView.setImageResource(imageResId[i]);
				if(scaleType!=null)
					imageView.setScaleType(scaleType);
				views.add(imageView);
			}
		} else {

			for (int i = 0; i < list.size(); i++) {
				ImageView imageView = new ImageView(getContext());
				final int j = i;
				imageView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {// 设置图片点击事件

//						if(list.get(j).getActivityName()!=null){
//							ACLog.i("initViewPager-->PackageName:"+ACImgScrollRelativeLayout.this.getContext().getPackageName()+",ActivityName:"+list.get(j).getActivityName());
//							Intent intent = new Intent();
//							intent.setClassName(ACImgScrollRelativeLayout.this.getContext().getPackageName(), list.get(j).getActivityName());
//							ACImgScrollRelativeLayout.this.getContext().startActivity(intent);
//						}
						// 打开网页
//						Uri uri = Uri.parse(list.get(j).getLinkUrl());
//						Intent it = new Intent(Intent.ACTION_VIEW, uri);
//						ACImgScrollRelativeLayout.this.getContext().startActivity(it);
						/*
						 * Intent intent = new Intent(this,WebActivity.class);
						 * intent.putExtra("title",
						 * WelcomeActivity.list.get(j).getTitle());
						 * intent.putExtra("url",
						 * WelcomeActivity.list.get(j).getLinkUrl());
						 * startActivity(intent);
						 */
						listener.click(v,list.get(j),j);
					}
				});
				if(scaleType!=null)
					imageView.setScaleType(scaleType);
				views.add(imageView);
			}
		}
		return views;
	}

	public ACImgScroll getMyPager() {
		return myPager;
	}

	public interface ImgScrollClickListener{
		public void click(View v, ACImage acImage, int position);
	}

	private ImgScrollClickListener listener;
	/**
	 * 滚动图片点击监听
	 * @param listener
	 */
	public void setOnImgScrollClickListener(ImgScrollClickListener listener){
		this.listener = listener;
	}

	public void refresh(ArrayList<ACImage> list){
		myPager.refresh(list,initViewPager(list,type));
	}
}
