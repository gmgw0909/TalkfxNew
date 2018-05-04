package com.xindu.talkfx_new.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.TVInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class JYPZAdapter extends BaseQuickAdapter<TVInfo, BaseViewHolder> {

    public JYPZAdapter(List<TVInfo> data) {
        super(R.layout.item_transaction_variety, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TVInfo info) {
        TextView text = ((TextView) baseViewHolder.getView(R.id.text));
        LinearLayout ll = (LinearLayout) baseViewHolder.getView(R.id.ll);
        text.setText(info.a);
        if (baseViewHolder.getLayoutPosition() % 2 == 0) {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.bg_normal));
        }
    }
}
