package com.xindu.talkfx_new.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.UserInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class RankAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {

    public RankAdapter(List<UserInfo> data) {
        super(R.layout.item_rank, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final UserInfo model) {
        LinearLayout ll = baseViewHolder.getView(R.id.ll);
        TextView rankNo = baseViewHolder.getView(R.id.rank_no);
        if (baseViewHolder.getLayoutPosition() == 0) {
            ll.setVisibility(View.VISIBLE);
            rankNo.setText("1");
        } else if (baseViewHolder.getLayoutPosition() == 1) {
            ll.setVisibility(View.VISIBLE);
            rankNo.setText("2");
        } else if (baseViewHolder.getLayoutPosition() == 2) {
            ll.setVisibility(View.VISIBLE);
            rankNo.setText("3");
        } else {
            ll.setVisibility(View.GONE);
        }
    }
}
