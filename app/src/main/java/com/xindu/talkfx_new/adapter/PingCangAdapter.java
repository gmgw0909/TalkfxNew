package com.xindu.talkfx_new.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.PingCangInfo;
import com.xindu.talkfx_new.utils.TimeUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class PingCangAdapter extends BaseQuickAdapter<PingCangInfo.PCInfo, BaseViewHolder> {

    public PingCangAdapter(List<PingCangInfo.PCInfo> data) {
        super(R.layout.item_pingcang_history, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final PingCangInfo.PCInfo info) {
        baseViewHolder.setText(R.id.tv, info.symbol.toUpperCase())
                .setText(R.id.tv1, "")
                .setText(R.id.time, TimeUtil.convertToDifftime(TimeUtil.FORMAT_TIME_CN_2, info.time1 * 1000))
                .setText(R.id.tv_p, info.sl + " —— " + info.tp)
                .setText(R.id.all, info.holdPrice + "点");
    }
}
