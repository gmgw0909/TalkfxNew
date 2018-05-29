package com.xindu.talkfx_new.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.JYAccountListInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class MyAccountAdapter extends BaseQuickAdapter<JYAccountListInfo.ListInfo, BaseViewHolder> {

    public MyAccountAdapter(List<JYAccountListInfo.ListInfo> data) {
        super(R.layout.item_account, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final JYAccountListInfo.ListInfo info) {
        baseViewHolder.setText(R.id.tv_name, info.platformServer + " | " + info.acctNo)
                .setText(R.id.tv_status, info.isSelf.equals("1") ? "(公开)" : "(私密)");
    }
}
