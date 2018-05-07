package com.xindu.talkfx_new.fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.widget.CustomDayView;

import java.util.ArrayList;

import butterknife.Bind;

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
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarDate currentDate;
    private boolean hasLoad = false;
    private boolean initiated = false;

    @Override
    protected int setContentView() {
        return R.layout.fragment_calendar;
    }

    @Override
    protected void lazyLoad() {
        if (!hasLoad) {
            initCurrentDate();
            initCalendarView();
            hasLoad = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!initiated) {
            refreshMonthPager();
            initiated = true;
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
                CalendarAttr.CalendarType.MONTH,
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
}
