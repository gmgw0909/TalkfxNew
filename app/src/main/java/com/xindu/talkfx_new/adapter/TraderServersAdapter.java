package com.xindu.talkfx_new.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.TraderServerInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class TraderServersAdapter extends BaseQuickAdapter<TraderServerInfo, BaseViewHolder> {

    public TraderServersAdapter(List<TraderServerInfo> data) {
        super(R.layout.item_traders, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final TraderServerInfo info) {
        baseViewHolder.setText(R.id.tv, info.serverName);
    }
}
