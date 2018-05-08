package com.xindu.talkfx_new.adapter;

import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.TVInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class CalendarAdapter extends BaseQuickAdapter<TVInfo, BaseViewHolder> {

    public CalendarAdapter(List<TVInfo> data) {
        super(R.layout.item_calendar, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TVInfo info) {
        LinearLayout ll = (LinearLayout) baseViewHolder.getView(R.id.ll);
        if (baseViewHolder.getLayoutPosition() % 2 == 0) {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.bg_normal));
        }
    }
}
