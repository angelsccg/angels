package com.example.angelsdemo.activity.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.angels.imageselector.imageloader.ImageSelectorActivity;
import com.angels.imageselector.utils.ImageLoader;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import java.util.ArrayList;

/**
 * 项目名称：angels
 * 类描述： 图片选择器
 * 创建人：Administrator
 * 创建时间：2017/5/4 11:11
 */

public class ImageSelectorMainActivity extends BaseActivity {

    private ListView lvImage;
    private ImageAdapter adapter;
    public ArrayList<String> mSelectedImage = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_main);
        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageSelectorMainActivity.this, ImageSelectorActivity.class);
                startActivityForResult(intent,1001);
            }
        });
        lvImage = (ListView) findViewById(R.id.lv_image);
        adapter = new ImageAdapter(this);
        lvImage.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        if(requestCode == 1001){
            mSelectedImage = data.getStringArrayListExtra("images");
            adapter.notifyDataSetChanged();

        }
    }

    class ImageAdapter extends BaseAdapter {
        //自定义图片Adapter以内部类形式存在于MainActivity中，方便访问MainActivity中的各个变量，特别是imgs数组
        private Context context;//用于接收传递过来的Context对象
        public ImageAdapter(Context context) {
            super();
            this.context = context;
        }


        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return mSelectedImage.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int position) {
            return position;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //针对每一个数据（即每一个图片ID）创建一个ImageView实例，
            ImageView iv = new ImageView(context);//针对外面传递过来的Context变量，
//            iv.setImageResource(R.drawable.a1);
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(mSelectedImage.get(position), iv);
            iv.setLayoutParams(new ViewGroup.LayoutParams(180, 180));//设置Gallery中每一个图片的大小为80*80。
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ACToastUtils.showMessage(v.getContext(),mSelectedImage.get(position));
                }
            });
            return iv;
        }

    }
}
