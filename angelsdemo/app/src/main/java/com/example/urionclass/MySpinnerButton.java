package com.example.urionclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.angelsdemo.R;
import com.example.urionbean.User;
import com.example.uriondb.DBOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MySpinnerButton extends Button {

	private Context context;
	private DBOpenHelper dbOpenHelper;

	public MySpinnerButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		// ���ü����¼�
		setOnClickListener(new MySpinnerButtonOnClickListener());

	}

	public MySpinnerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// ���ü����¼�
		setOnClickListener(new MySpinnerButtonOnClickListener());
	}

	public MySpinnerButton(Context context) {
		super(context);
		this.context = context;

		// ���ü����¼�
		setOnClickListener(new MySpinnerButtonOnClickListener());
	}

	/**
	 * MySpinnerButton�ĵ���¼�
	 * 
	 * @author haozi
	 * 
	 */
	class MySpinnerButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			final MySpinnerDropDownItems mSpinnerDropDrownItems = new MySpinnerDropDownItems(
					context);
			if (!mSpinnerDropDrownItems.isShowing()) {
				mSpinnerDropDrownItems.showAsDropDown(MySpinnerButton.this);
			}
		}
	}

	/**
	 * MySpinnerButton�������б�
	 * 
	 * @author haozi
	 * 
	 */

	class MySpinnerDropDownItems extends PopupWindow {

		private Context context;
		private LinearLayout mLayout; // �����б�Ĳ���
		private ListView mListView; // �����б�ؼ�
		private ArrayList<HashMap<String, String>> mData;

		public MySpinnerDropDownItems(Context context) {
			super(context);
			this.context = context;
			// �����б�Ĳ���
			mLayout = new LinearLayout(context);
			mLayout.setOrientation(LinearLayout.VERTICAL);
			// mLayout.setBackgroundColor(R.color.red);
			// �����б�ؼ�
			mListView = new ListView(context);
			dbOpenHelper = new DBOpenHelper(context);
			mListView.setLayoutParams(new LayoutParams(200, 300));
			mData = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> mHashmap1 = new HashMap<String, String>();
			mHashmap1.put("spinner_dropdown_item_textview", "User");
			mData.add(mHashmap1);
			SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
			Cursor cursor = db.query("user", new String[] { "name" }, null,
					null, null, null, null);
			while (cursor.moveToNext()) {
				HashMap<String, String> mHashmap = new HashMap<String, String>();
				String uname = cursor.getString(cursor.getColumnIndex("name"));

				mHashmap.put("spinner_dropdown_item_textview", uname);
				mData.add(mHashmap);
			}
			cursor.close(); // �α�ʹ����ɺ�ǵùر�
			db.close();

			// ΪlistView����������
			mListView.setAdapter(new MyAdapter(context, mData,
					R.layout.spinner_dropdown_item,
					new String[] { "spinner_dropdown_item_textview" },
					new int[] { R.id.spinner_dropdown_item_textview }));
			// ����listView�ĵ���¼�
			mListView
					.setOnItemClickListener(new MyListViewOnItemClickedListener());
			// �������б���ӵ�layout�С�
			mLayout.addView(mListView);

			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setContentView(mLayout);
			setFocusable(true);
			mLayout.setBackgroundColor(R.color.black_ac);
			mLayout.setFocusableInTouchMode(true);
		}

		/**
		 * �ҵ�������
		 * 
		 * @author haozi
		 * 
		 */
		public class MyAdapter extends BaseAdapter {

			private Context context;
			private List<? extends Map<String, ?>> mData;
			private int mResource;
			private String[] mFrom;
			private int[] mTo;
			private LayoutInflater mLayoutInflater;

			/**
			 * �ҵ��������Ĺ��췽��
			 * 
			 * @param context
			 *            ���÷���������
			 * @param data
			 *            ����
			 * @param resource
			 * @param from
			 * @param to
			 */
			public MyAdapter(Context context,
					List<? extends Map<String, ?>> data, int resource,
					String[] from, int[] to) {

				this.context = context;
				this.mData = data;
				this.mResource = resource;
				this.mFrom = from;
				this.mTo = to;
				this.mLayoutInflater = (LayoutInflater) context
						.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			}

			/**
			 * ϵͳ�ڻ���ListView֮ǰ�������ȵ���getCount��������ȡItem�ĸ���
			 */
			public int getCount() {

				return this.mData.size();
			}

			public Object getItem(int position) {

				return this.mData.get(position);
			}

			public long getItemId(int position) {

				return position;
			}

			/**
			 * ÿ����һ�� Item�ͻ����һ��getView������
			 * �ڴ˷����ھͿ����������ȶ���õ�xml��ȷ����ʾ��Ч��������һ��View������Ϊһ��Item��ʾ������ Ҳ
			 * ������������������������������Ҫת�����ܣ������ݺ���Դ�Կ�������Ҫ��Ч����ʾ������
			 * Ҳ����getView���ظ����ã�ʹ��ListView��ʹ�ø� Ϊ�򵥺���
			 * �������������Զ�ListView��ʾЧ������Ϊ��Ҫ�ģ�ͬʱֻҪ��д���˾�����������ListView������ȫ�������ߵ�Ҫ����ʾ�� ��
			 * getItem��getItemId���������ڵ���ListView����Ӧ������ʱ�򱻵��õ���
			 * ����Ҫ��֤ListView�ĸ���������Ч�Ļ�������������Ҳ����д��
			 */
			public View getView(int position, View contentView, ViewGroup parent) {

				contentView = this.mLayoutInflater.inflate(this.mResource,
						parent, false);
				// ����contentView�����ݺ���ʽ�������ص�������contentView�����ֵĴ�С
				contentView.setBackgroundColor(R.color.black);
				for (int index = 0; index < this.mTo.length; index++) {
					TextView textView = (TextView) contentView
							.findViewById(this.mTo[index]);
					textView.setText(this.mData.get(position)
							.get(this.mFrom[index]).toString());
					textView.setTextColor(Color.WHITE);

				}

				return contentView;
			}
		}

		/**
		 * listView�ĵ���¼�
		 * 
		 * @author haozi
		 * 
		 */
		class MyListViewOnItemClickedListener implements
				AdapterView.OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView mTextView = (TextView) view
						.findViewById(R.id.spinner_dropdown_item_textview);
				String content = mTextView.getText().toString();
				MySpinnerButton.this.setText(content);
				MySpinnerDropDownItems.this.dismiss();
			}
		}
	}
}
