package com.xindu.talkfx_new.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.TVInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class PingCangAdapter extends BaseQuickAdapter<TVInfo, BaseViewHolder> {

    public PingCangAdapter(List<TVInfo> data) {
        super(R.layout.item_pingcang_history, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final TVInfo info) {

    }
}
