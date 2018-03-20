package com.example.angelsdemo.activity.control;

import java.util.ArrayList;
import java.util.List;

import com.angels.widget.pullableview.PullToRefreshLayout;
import com.angels.widget.pullableview.PullToRefreshLayout.OnRefreshListener;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class RefreshAndLoadMoreActivity extends BaseActivity{
	private ListView listView;
	private boolean isFirstIn = true;
	private PullToRefreshLayout pullToRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh_load);
		pullToRefreshLayout = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		pullToRefreshLayout.setOnRefreshListener(listener);
		pullToRefreshLayout.setCanPullUpload(true);
		listView = (ListView) findViewById(R.id.content_view);
		initListView();
//		pullToRefreshLayout.autoLoad();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		// 第一次进入自动刷新
		if (isFirstIn)
		{
			pullToRefreshLayout.autoRefresh();
			isFirstIn = false;
		}
	}

	private List<String> items;
	private MyAdapter adapter;
	/**
	 * ListView初始化方法
	 */
	private void initListView()
	{
		items = new ArrayList<String>();
		adapter = new MyAdapter(this, items);
		listView.setAdapter(adapter);
		refresh();
	}
	
	private void refresh(){
		items.clear();
		items.add("可下拉刷新上拉加载的ListView");
		items.add("可下拉刷新上拉加载的GridView");
		items.add("可下拉刷新上拉加载的ExpandableListView");
		items.add("可下拉刷新上拉加载的SrcollView");
		items.add("可下拉刷新上拉加载的WebView");
		items.add("可下拉刷新上拉加载的ImageView");
		items.add("可下拉刷新上拉加载的TextView");
		adapter.notifyDataSetChanged();
	}
	private void add(){
		for (int i = 0; i < 10; i++) {
			items.add("item"+i);
		}
		adapter.notifyDataSetChanged();
	}
	
private OnRefreshListener listener = new OnRefreshListener() {
		
		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
		{
			// 下拉刷新操作
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					// 千万别忘了告诉控件刷新完毕了哦！
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
					refresh();
				}
			}.sendEmptyMessageDelayed(0, 3000);
		}

		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
		{
			// 加载操作
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					// 千万别忘了告诉控件加载完毕了哦！
					pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					add();
				}
			}.sendEmptyMessageDelayed(0, 3000);
		}
	};
	class MyAdapter extends BaseAdapter {
		List<String> items;
		Context context;

		public MyAdapter(Context context, List<String> items) {
			this.context = context;
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.list_item_layout, null);
			TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(items.get(position));
			return view;
		}
	}
}
