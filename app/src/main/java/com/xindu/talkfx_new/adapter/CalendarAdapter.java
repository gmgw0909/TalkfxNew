package com.xindu.talkfx_new.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.CalendarsInfo;
import com.xindu.talkfx_new.utils.SpannableStringUtils;
import com.xindu.talkfx_new.utils.TimeUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class CalendarAdapter extends BaseQuickAdapter<CalendarsInfo.ListInfo, BaseViewHolder> {

    public CalendarAdapter(List<CalendarsInfo.ListInfo> data) {
        super(R.layout.item_calendar, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CalendarsInfo.ListInfo info) {
        LinearLayout ll = (LinearLayout) baseViewHolder.getView(R.id.ll);
        if (baseViewHolder.getLayoutPosition() % 2 == 0) {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.bg_normal));
        }
        ImageView star1 = baseViewHolder.getView(R.id.star1);
        ImageView star2 = baseViewHolder.getView(R.id.star2);
        ImageView star3 = baseViewHolder.getView(R.id.star3);
        TextView title = baseViewHolder.getView(R.id.title);
        TextView teforecast = baseViewHolder.getView(R.id.teforecast);
        TextView previous = baseViewHolder.getView(R.id.previous);
        TextView forecast = baseViewHolder.getView(R.id.forecast);
        TextView data = baseViewHolder.getView(R.id.data);
        data.setText(TimeUtil.convertToTime("HH:mm", info.dateTime));
        teforecast.setText(SpannableStringUtils.getBuilder("现值 ").setProportion(0.8f).append(info.teforecast).create());
        previous.setText(SpannableStringUtils.getBuilder("前次 ").setProportion(0.8f).append(info.previous).create());
        forecast.setText(SpannableStringUtils.getBuilder("预测 ").setProportion(0.8f).append(info.forecast).create());
        title.setText(info.event);
        if (info.importance == 0) {
            star1.setImageResource(R.mipmap.calendar_star);
            star2.setImageResource(R.mipmap.calendar_star);
            star3.setImageResource(R.mipmap.calendar_star);
        } else if (info.importance == 1) {
            star1.setImageResource(R.mipmap.calendar_star);
            star2.setImageResource(R.mipmap.calendar_star);
            star3.setImageResource(R.mipmap.calendar_star_s);
        } else if (info.importance == 2) {
            star1.setImageResource(R.mipmap.calendar_star);
            star2.setImageResource(R.mipmap.calendar_star_s);
            star3.setImageResource(R.mipmap.calendar_star_s);
        } else if (info.importance == 3) {
            star1.setImageResource(R.mipmap.calendar_star_s);
            star2.setImageResource(R.mipmap.calendar_star_s);
            star3.setImageResource(R.mipmap.calendar_star_s);
        }
    }
}
