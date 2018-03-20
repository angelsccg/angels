package com.example.angelsdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angels.adapter.ACBaseAdapter;
import com.example.angelsdemo.R;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名称：angels3
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/2/22 14:43
 */

public class MyAdapter extends ACBaseAdapter{

    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();
    /**
     * 文件夹路径
     */
    private String mDirPath;

    private int itemLayoutId;

    public MyAdapter(List data, Context context, int itemLayoutId,String dirPath) {
        super(data, context);
        this.mDirPath = dirPath;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public View getView(View convertView, Object o, int position) {
        final String item = (String) o;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayoutId, null);
            holder.ivPhoto = (ImageView) convertView.findViewById(R.id.id_item_image);
            holder.ibSelector = (ImageButton)convertView.findViewById(R.id.id_item_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.ivPhoto.setColorFilter(null);
        //设置ImageView的点击事件
        final ViewHolder finalHolder = holder;
//        holder.ivPhoto.setImageResource();
        System.out.println("item-->"+item);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener(){
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v)
            {
                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item))
                {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    finalHolder.ibSelector .setImageResource(R.drawable.picture_unselected);
                    finalHolder.ivPhoto.setColorFilter(null);
                } else
                // 未选择该图片
                {
                    mSelectedImage.add(mDirPath + "/" + item);
                    finalHolder.ibSelector .setImageResource(R.drawable.pictures_selected);
                    finalHolder.ivPhoto.setColorFilter(Color.parseColor("#77000000"));
                }
            }
        });

        /**
         * 已经选择过的图片，显示出选择过的效果
         */
        if (mSelectedImage.contains(mDirPath + "/" + item))
        {
            holder.ibSelector.setImageResource(R.drawable.pictures_selected);
            holder.ivPhoto.setColorFilter(Color.parseColor("#77000000"));
        }

        return convertView;
    }

    private final class ViewHolder {
        ImageView ivPhoto;
        ImageButton ibSelector;
    }
}
