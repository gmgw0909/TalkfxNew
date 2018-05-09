package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.necer.ncalendar.listener.OnClickWeekViewListener;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.Utils;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * Created by necer on 2017/8/25.
 * QQ群:127278900
 */

public class WeekView extends CalendarView {


    private OnClickWeekViewListener mOnClickWeekViewListener;
    private List<String> lunarList;

    public WeekView(Context context, LocalDate date, OnClickWeekViewListener onClickWeekViewListener) {
        super(context);

        this.mInitialDate = date;
        Utils.NCalendar weekCalendar2 = Utils.getWeekCalendar2(date, Attrs.firstDayOfWeek);

        dates = weekCalendar2.dateList;
        lunarList = weekCalendar2.lunarList;
        mOnClickWeekViewListener = onClickWeekViewListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        mRectList.clear();

        for (int i = 0; i < 7; i++) {
            Rect rect = new Rect(i * mWidth / 7, 0, i * mWidth / 7 + mWidth / 7, mHeight);
            mRectList.add(rect);
            LocalDate date = dates.get(i);
            Paint.FontMetricsInt fontMetrics = mSorlarPaint.getFontMetricsInt();
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;

            if (mSelectDate != null && date.equals(mSelectDate)) {
                mSorlarPaint.setColor(Color.GRAY);
                canvas.drawRect(i * mWidth / 7 + Utils.dp2px(getContext(), 5), 0, i * mWidth / 7 + mWidth / 7 - Utils.dp2px(getContext(), 5), mHeight, mSorlarPaint);
                mSorlarPaint.setColor(Color.WHITE);
                canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), baseline + getHeight() / 4 - Utils.dp2px(getContext(), 6), mSorlarPaint);
                mLunarPaint.setColor(Color.WHITE);
                mLunarPaint.setTextSize(Utils.sp2px(getContext(), 14));
                String lunar = weeks[i];
                canvas.drawText(lunar, rect.centerX(), baseline - getHeight() / 4 + Utils.dp2px(getContext(), 2), mLunarPaint);
            } else {
                mSorlarPaint.setColor(mSolarTextColor);
                canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), baseline + getHeight() / 4 - Utils.dp2px(getContext(), 6), mSorlarPaint);
                mLunarPaint.setColor(mLunarTextColor);
                mLunarPaint.setTextSize(Utils.sp2px(getContext(), 14));
                String lunar = weeks[i];
                canvas.drawText(lunar, rect.centerX(), baseline - getHeight() / 4 + Utils.dp2px(getContext(), 2), mLunarPaint);
            }
        }
    }

    String weeks[] = {"日", "一", "二", "三", "四", "五", "六"};

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < mRectList.size(); i++) {
                Rect rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    LocalDate selectDate = dates.get(i);
                    mOnClickWeekViewListener.onClickCurrentWeek(selectDate);
                    break;
                }
            }
            return true;
        }
    });


    public boolean contains(LocalDate date) {
        return dates.contains(date);
    }
}
