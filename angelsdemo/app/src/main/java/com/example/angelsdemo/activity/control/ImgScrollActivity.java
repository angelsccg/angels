package com.example.angelsdemo.activity.control;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;

import com.angels.cache.ACImageLoader;
import com.angels.model.ACImage;
import com.angels.util.ACToastUtils;
import com.angels.widget.imgscroll.ACImgScroll.ImageLoaderListener;
import com.angels.widget.imgscroll.ACImgScrollRelativeLayout;
import com.angels.widget.imgscroll.ACImgScrollRelativeLayout.ImgScrollClickListener;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class ImgScrollActivity extends BaseActivity{
	public ArrayList<ACImage> list = new ArrayList<ACImage>();

	private ACImgScrollRelativeLayout imgScrollRelativeLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imgscroll);


		 /**
         * 模拟数据
         */
        for (int i = 0; i < 5; i++) {
			ACImage image = new ACImage();
			image.setTitle("标题"+i);
			image.setImgUrl("http://static.oschina.net/uploads/space/2013/1024/070531_uqzn_12.jpg");
			image.setLinkUrl("http://image.baidu.com/");
			list.add(image);
		}
        initImagePage();

		Button buton = (Button) findViewById(R.id.btn_refresh);
		buton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();
			}
		});
	}

	private void refresh(){
		int num = 0;
		num = (int) (Math.random()*10);
		list.clear();
		/**
		 * 模拟数据
		 */
		for (int i = 0; i < num; i++) {
			ACImage image = new ACImage();
			image.setTitle("标题"+i);
			image.setImgUrl("http://static.oschina.net/uploads/space/2013/1024/070531_uqzn_12.jpg");
			image.setLinkUrl("http://image.baidu.com/");
			list.add(image);
		}

		imgScrollRelativeLayout.refresh(list);
	}
	
	  /**
     * 图片滚动展示 初始化
     */
    private void initImagePage(){
		imgScrollRelativeLayout = (ACImgScrollRelativeLayout) findViewById(R.id.imgScroll);
		// 开始滚动
		imgScrollRelativeLayout.start(this, list,4000,ScaleType.CENTER_CROP);
		//点击监听
		imgScrollRelativeLayout.setOnImgScrollClickListener(new ImgScrollClickListener() {
			@Override
			public void click(View v, ACImage acImage, int position) {
				ACToastUtils.showMessage(v.getContext(), acImage.getImgUrl(), 10);
			}
		});
		//图片载入监听
		imgScrollRelativeLayout.getMyPager().setOnImageLoaderListener(new ImageLoaderListener() {
			@Override
			public void imageLoading(String imgUrl, ImageView imageView) {
				 ACImageLoader.getInstance().LoadImage(imgUrl,imageView,R.drawable.ic_launcher,R.drawable.ic_launcher);
			}
		});
    }
}
