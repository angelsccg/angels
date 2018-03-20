package com.example.angelsdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.angelsdemo.R;

/**
 * 项目名称：Angels_Demo
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/3/25 15:42
 */
public class DBActivity extends BaseActivity implements View.OnClickListener {

    public static final String[] btnNames= {"创建数据库","更新数据库","更新表"};
    public static final Button[] btns = new Button[btnNames.length];

    public static final String DB_NAME = "ac_db";
    public static final int DB_VERSION = 1;

    private LinearLayout llContent;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainc);

        textView = (TextView) findViewById(R.id.textView1);
        llContent = (LinearLayout) findViewById(R.id.llContent);

        for (int i = 0; i < btnNames.length; i++) {
            Button button = new Button(this);
            button.setText(btnNames[i]);
            button.setOnClickListener(this);
            llContent.addView(button);
            btns[i] = button;
            btns[i].setId(i);
        }
        textView.setText("==============================================================");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0://创建数据库
            {
            }
            break;
            case 1://更新数据库
            {
            }
            break;
            case 2://表插入
            {
//                ContentValues value = new ContentValues();
//                value.put("name", "update_thinkpad");
//                ACSQLiteDatabase.ACDBParams params = new ACSQLiteDatabase.ACDBParams(ACSQLiteDatabase.DB_NAME,2);
//                database.update("user", value, "id=?", new String[]{"2012"});
            }
            break;
            case 3:
            {
            }
            break;
            default:
                break;
        }
    }
}
