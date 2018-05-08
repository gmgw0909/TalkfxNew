package com.xindu.talkfx_new.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
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

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.CalendarAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.bean.TVInfo;
import com.xindu.talkfx_new.widget.CustomDayView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class CalendarFragment extends BaseFragment {

    @Bind(R.id.today)
    TextView today;
    @Bind(R.id.calendar_view)
    MonthPager monthPager;
    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.content)
    CoordinatorLayout content;
    @Bind(R.id.country)
    TextView country;
    @Bind(R.id.choose_date_view)
    RelativeLayout chooseDateView;
    String countrys[] = {"中国", "美国", "法国", "德国", "英国", "日本", "俄罗斯", "意大利", "波黑", "塞尔维亚", "加拿大", "墨西哥"};

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarDate currentDate;


    @Override
    protected int setContentView() {
        return R.layout.fragment_calendar;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Utils.scrollTo(content, list, monthPager.getCellHeight(), 200);
            calendarAdapter.switchToWeek(monthPager.getRowIndex());
        } else {
            //相当于Fragment的onPause
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setContentView(), container, false);
        ButterKnife.bind(this, view);
        monthPager.setViewHeight(Utils.dpi2px(getActivity(), 270));
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<TVInfo> list_ = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list_.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
        }
        list.setAdapter(new CalendarAdapter(list_));
        initCurrentDate();
        initCalendarView();
        initPop();
        return view;
    }

    PopupWindow qmuiPopup;

    private void initPop() {
        if (qmuiPopup == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_country, null);
            GridView gridView = view.findViewById(R.id.grid_view);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.test_list_item, countrys);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    country.setText(countrys[i]);
                }
            });
            qmuiPopup = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            qmuiPopup.setOutsideTouchable(true);
            qmuiPopup.setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void refreshMonthPager() {
        CalendarDate currentDate = new CalendarDate();
        calendarAdapter.notifyDataChanged(currentDate);
        today.setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
    }

    private void initCurrentDate() {
        currentDate = new CalendarDate();
        today.setText(currentDate.getYear() + "年" + currentDate.getMonth() + "月");
    }

    /**
     * 初始化CustomDayView
     */
    private void initCalendarView() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
        CustomDayView customDayView = new CustomDayView(getActivity(), R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                getActivity(),
                onSelectDateListener,
                CalendarAttr.CalendarType.WEEK,
                CalendarAttr.WeekArrayType.Sunday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                list.scrollToPosition(0);
            }
        });
        initMonthPager();
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        today.setText(date.getYear() + "年" + date.getMonth() + "月");
    }

    /**
     * 初始化monthPager
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    today.setText(date.getYear() + "年" + date.getMonth() + "月");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.country, R.id.goto_today})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.country:
                if (qmuiPopup.isShowing()) {
                    qmuiPopup.dismiss();
                } else {
                    qmuiPopup.showAsDropDown(chooseDateView);
                }
                break;
            case R.id.goto_today:
                refreshMonthPager();
                break;
        }
    }
}
