package com.angels.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ACScrollView extends ScrollView {

	private ScrollViewListener scrollViewListener = null;

	public ACScrollView(Context context) {
		super(context);
		init();
	}

	public ACScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ACScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ACScrollView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {

	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public interface ScrollViewListener {
		void onScrollChanged(ACScrollView scrollView, int x, int y, int oldx, int oldy);
	}
}
