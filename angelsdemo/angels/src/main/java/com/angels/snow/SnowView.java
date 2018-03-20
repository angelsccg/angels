package com.angels.snow;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

public class SnowView extends View{

	/** "雪花位图" */
	Bitmap snowBitmap;
	/** 当前"雪花"总数 */
	int numSnows = 0;
	/** 当前"雪花"集合 */
	ArrayList<SnowEntity> snows = new ArrayList<SnowEntity>();
	
	public SnowView(Context context) {
		super(context);
		init();
	}
	public SnowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public SnowView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	@SuppressLint("NewApi")
	public SnowView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		
	}

}
