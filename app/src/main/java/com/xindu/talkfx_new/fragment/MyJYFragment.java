package com.xindu.talkfx_new.fragment;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class MyJYFragment extends BaseFragment {

    @Bind(R.id.chart1)
    RadarChart mChart;

    boolean isInit;

    @Override
    protected int setContentView() {
        return R.layout.fragment_my_jy;
    }

    @Override
    protected void lazyLoad() {
        if (!isInit) {
            mChart.getDescription().setEnabled(false);
            mChart.setWebLineWidth(1f);
            mChart.setWebColor(Color.WHITE);
            mChart.setWebLineWidthInner(1f);
            mChart.setWebColorInner(Color.WHITE);
            mChart.setWebAlpha(100);
            //设置初始值
            setData();
            mChart.animateXY(
                    1400, 1400,
                    Easing.EasingOption.EaseInOutQuad,
                    Easing.EasingOption.EaseInOutQuad);

            XAxis xAxis = mChart.getXAxis();
//        xAxis.setTypeface(mTfLight);
            xAxis.setTextSize(9f);
            xAxis.setYOffset(0f);
            xAxis.setXOffset(0f);
            xAxis.setValueFormatter(new IAxisValueFormatter() {

                private String[] mActivities = new String[]{"Burger", "Steak", "Salad", "Pasta", "Pizza"};

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mActivities[(int) value % mActivities.length];
                }
            });
            xAxis.setTextColor(Color.WHITE);

            YAxis yAxis = mChart.getYAxis();
//        yAxis.setTypeface(mTfLight);
            yAxis.setLabelCount(5, false);
            yAxis.setTextSize(9f);
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(80f);
            yAxis.setDrawLabels(false);

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setEnabled(false);
            isInit = true;
        }
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }

    public void setData() {

        float mult = 100;
        float min = 0;
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();

        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mult) + min;
            entries1.add(new RadarEntry(val1));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
//        data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
