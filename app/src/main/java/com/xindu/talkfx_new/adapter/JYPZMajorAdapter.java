package com.xindu.talkfx_new.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.WebViewActivity;
import com.xindu.talkfx_new.bean.TVInfo;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class JYPZMajorAdapter extends BaseQuickAdapter<TVInfo, BaseViewHolder> {
    DecimalFormat df = new DecimalFormat("0.00000");

    public JYPZMajorAdapter(List<TVInfo> data) {
        super(R.layout.item_transaction_variety, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final TVInfo info) {
        LinearLayout ll = (LinearLayout) baseViewHolder.getView(R.id.ll);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("title", info.name)
                        .putExtra("curr", info.symbol));
            }
        });
        if (baseViewHolder.getLayoutPosition() % 2 == 0) {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            ll.setBackgroundColor(mContext.getResources().getColor(R.color.bg_normal));
        }
        TextView price = ((TextView) baseViewHolder.getView(R.id.price));
        TextView upDown = ((TextView) baseViewHolder.getView(R.id.up_down));
        ((TextView) baseViewHolder.getView(R.id.name)).setText(info.name);
        if (info.dailyChange < 0.00000D) {
            upDown.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            upDown.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        price.setText(df.format(info.last));
        upDown.setText(df.format(info.dailyChange));
    }
}
