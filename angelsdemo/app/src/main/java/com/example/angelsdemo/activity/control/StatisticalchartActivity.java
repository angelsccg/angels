package com.example.angelsdemo.activity.control;

import android.os.Bundle;
import android.view.View;

import com.angels.widget.statisticalchart.HistogramView;
import com.angels.widget.statisticalchart.LineChartView;
import com.angels.widget.statisticalchart.PinChart;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

/**
 * 项目名称：angels
 * 类描述：统计
 * 创建人：Administrator
 * 创建时间：2017/5/9 14:47
 */

public class StatisticalchartActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_histogram);

        final HistogramView hv = (HistogramView) findViewById(R.id.hv);
        final LineChartView lvc = (LineChartView) findViewById(R.id.lcv);
        final PinChart pc = (PinChart) findViewById(R.id.pc);

        findViewById(R.id.btn_start1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hv.start(2);
//                lvc.start(1);
//                pc.start(1);
            }
        });
        findViewById(R.id.btn_start2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hv.start(1);
                lvc.start(2);
//                pc.start(1);
            }
        });
        findViewById(R.id.btn_start3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hv.start(1);
//                lvc.start(1);
                pc.start(2);
            }
        });
    }
}
