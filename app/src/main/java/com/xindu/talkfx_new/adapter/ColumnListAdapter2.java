package com.xindu.talkfx_new.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.bean.ColumnInfo;
import com.xindu.talkfx_new.utils.TimeUtil;

import java.text.ParseException;
import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class ColumnListAdapter2 extends BaseQuickAdapter<ColumnInfo, BaseViewHolder> {

    public ColumnListAdapter2(List<ColumnInfo> data) {
        super(R.layout.item_column_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ColumnInfo model) {
        try {
            baseViewHolder.setText(R.id.title, model.title)
                    .setText(R.id.userName, model.userName)
                    .setText(R.id.data, "/" + (TextUtils.isEmpty(model.createDate) ? "" : TimeUtil.convertToTime(model.createDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ImageView firstImg = baseViewHolder.getView(R.id.firstImg);
        if (!TextUtils.isEmpty(model.firstImg)) {
            firstImg.setVisibility(View.VISIBLE);
            if (!model.firstImg.contains("http")) {
                Glide.with(App.getInstance().getApplicationContext())
                        .load(Constants.baseUrl + model.firstImg)
                        .error(R.mipmap.home_list_error_img)
                        .into(firstImg);
            } else {
                Glide.with(App.getInstance().getApplicationContext())
                        .load(model.firstImg).error(R.mipmap.home_list_error_img)
                        .into(firstImg);
            }

        } else {
            firstImg.setVisibility(View.GONE);
        }
    }
}
