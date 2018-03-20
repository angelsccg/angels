package com.example.angelsdemo.activity.control;

import android.os.Bundle;

import com.angels.widget.ACGooeyMenu;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class MenuActivity extends BaseActivity implements ACGooeyMenu.GooeyMenuInterface{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_mueu);
		ACGooeyMenu mGooeyMenu = (ACGooeyMenu) findViewById(R.id.gooey_menu);
	    mGooeyMenu.setOnMenuListener(this);
	}

	@Override
	public void menuOpen() {
		ACToastUtils.showMessage(this, "打开");
	}

	@Override
	public void menuClose() {
		ACToastUtils.showMessage(this, "关闭");
		
	}

	@Override
	public void menuItemClicked(int menuNumber) {
		ACToastUtils.showMessage(this, "menuNumber："+menuNumber);
	}
}
