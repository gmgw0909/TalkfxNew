package com.xindu.talkfx_new.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.TVInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class JYPZEditAdapter extends BaseQuickAdapter<TVInfo, BaseViewHolder> {

    public JYPZEditAdapter(List<TVInfo> data) {
        super(R.layout.item_transaction_variety, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TVInfo info) {
        TextView text = ((TextView) baseViewHolder.getView(R.id.text));
        text.setText(info.a);
    }
}
