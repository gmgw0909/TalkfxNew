package com.xindu.talkfx_new.adapter;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.bean.UserInfo;
import com.xindu.talkfx_new.widget.CircleImageView;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class ColumnFollowListAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {

    public ColumnFollowListAdapter(List<UserInfo> data) {
        super(R.layout.item_icon, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final UserInfo model) {
        CircleImageView firstImg = baseViewHolder.getView(R.id.iv);
        if (!TextUtils.isEmpty(model.headImg)) {
            Glide.with(App.getInstance().getApplicationContext())
                    .load(Constants.baseImgUrl + model.headImg)
                    .error(R.mipmap.default_person_icon)
                    .into(firstImg);
        }
    }
}
