package com.xindu.talkfx_new.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.necer.ncalendar.calendar.WeekCalendar;
import com.necer.ncalendar.listener.OnWeekCalendarChangedListener;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.CalendarAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.bean.TVInfo;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class CalendarFragment extends BaseFragment {

    @Bind(R.id.weekCalendar)
    WeekCalendar weekCalendar;
    @Bind(R.id.today)
    TextView today;
    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.country)
    TextView country;
    @Bind(R.id.choose_date_view)
    RelativeLayout chooseDateView;
    String countrys1[] = {"中国", "美国", "法国", "德国", "英国", "日本", "俄罗斯", "意大利", "波黑", "塞尔维亚", "加拿大", "墨西哥"};
    String countrys2[] = {"美国", "英国", "日本", "法国", "德国", "加拿大", "意大利", "俄罗斯", "澳大利亚", "中国", "巴西", "阿根廷", "墨西哥", "韩国", "印度尼西亚", "印度", "沙特阿拉伯", "南非", "土耳其", "欧盟"};

    @Override
    protected int setContentView() {
        return R.layout.fragment_calendar;
    }

    @Override
    protected void lazyLoad() {

    }

    int year;
    int month;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setContentView(), container, false);
        ButterKnife.bind(this, view);
//        weekCalendar.setDefaultSelect(false);
        weekCalendar.setOnWeekCalendarChangedListener(new OnWeekCalendarChangedListener() {
            @Override
            public void onWeekCalendarChanged(LocalDate date) {
                year = date.getYear();
                month = date.getMonthOfYear();
                today.setText(date.getYear() + "年" + date.getMonthOfYear() + "月");
            }
        });
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<TVInfo> list_ = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list_.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
        }
        list.setAdapter(new CalendarAdapter(list_));
        initPop();
        return view;
    }

    PopupWindow qmuiPopup;

    private void initPop() {
        if (qmuiPopup == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_country, null);
            final GridView gridView1 = view.findViewById(R.id.grid_view1);
            final GridView gridView2 = view.findViewById(R.id.grid_view2);
            view.findViewById(R.id.click_dismiss).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qmuiPopup.dismiss();
                }
            });
            view.findViewById(R.id.all).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridView1.setVisibility(View.VISIBLE);
                    gridView2.setVisibility(View.GONE);
                }
            });
            view.findViewById(R.id.g20).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridView2.setVisibility(View.VISIBLE);
                    gridView1.setVisibility(View.GONE);
                }
            });
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.test_list_item, countrys1);
            gridView1.setAdapter(adapter1);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.test_list_item, countrys2);
            gridView2.setAdapter(adapter2);
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    country.setText(countrys1[i]);
                }
            });
            gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    country.setText(countrys2[i]);
                }
            });
            qmuiPopup = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, true);
            qmuiPopup.setOutsideTouchable(true);
            qmuiPopup.setBackgroundDrawable(new BitmapDrawable());
        }
    }

    @OnClick({R.id.goto_today, R.id.choose_date_view, R.id.toLastPager, R.id.toNextPager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_date_view:
                if (!qmuiPopup.isShowing()) {
                    qmuiPopup.showAsDropDown(chooseDateView);
                } else {
                    qmuiPopup.dismiss();
                }
                break;
            case R.id.goto_today:
                weekCalendar.toToday();
                break;
            case R.id.toLastPager:
                weekCalendar.toLastPager();
                break;
            case R.id.toNextPager:
                weekCalendar.toNextPager();
                break;
        }
    }
}
