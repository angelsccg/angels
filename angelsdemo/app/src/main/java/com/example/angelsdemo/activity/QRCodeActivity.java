package com.example.angelsdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angelsdemo.R;
import com.zxing.BarCodeUtil;
import com.zxing.activity.CaptureActivity;

/**
 * 项目名称：angels3
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/1/11 15:30
 */

public class QRCodeActivity  extends BaseActivity{
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private TextView tv1;
    private TextView tv2;
    private EditText et1;
    private BarCodeUtil util;
    private ImageView iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        WindowManager wm = getWindowManager();
        //设置一维码和二维码宽高
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        util = new BarCodeUtil(width * 2 / 3, height / 4);

        findViews();
        initViews();
    }

    private void findViews() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        et1 = (EditText) findViewById(R.id.editText1);
        iv1 = (ImageView) findViewById(R.id.iv1);
    }

    private void initViews() {
        /**
         * 扫码
         */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRCodeActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        /**
         * 生成一维码
         */
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et1.getText().toString().trim();
                try {
                    iv1.setImageBitmap(util.bitmap1(text));
                    tv2.setText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("barCoder", "draw bar code failed." + e.toString());
                }
            }
        });

        /**
         * 生成二维码
         */
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et1.getText().toString().trim();
                try {
                    iv1.setImageBitmap(util.bitmap2(text));
                    tv2.setText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("barCoder", "draw bar code failed." + e.toString());
                }
            }
        });
    }

    //获取扫码结果
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            String result = data.getExtras().getString("result");
            tv1.setText(result);
        }
    }
}
