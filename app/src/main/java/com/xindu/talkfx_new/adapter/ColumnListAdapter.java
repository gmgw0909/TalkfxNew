package com.xindu.talkfx_new.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.bean.ColumnInfo;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.ColumnDetailActivity;
import com.xindu.talkfx_new.base.App;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class ColumnListAdapter extends BaseQuickAdapter<ColumnInfo> {

    public ColumnListAdapter(List<ColumnInfo> data) {
        super(R.layout.item_column, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ColumnInfo model) {
        baseViewHolder.setText(R.id.title, model.title)
                .setText(R.id.userName, model.userName)
                .setText(R.id.data, model.createDate)
                .setText(R.id.content, model.content)
                .setText(R.id.read_count, "阅读 " + model.readCount);

        ImageView headImg = baseViewHolder.getView(R.id.headImg);
        ImageView firstImg = baseViewHolder.getView(R.id.firstImg);
//        Glide.with(App.getInstance().getApplicationContext()).load(model.headImg).into(headImg);
        if (!TextUtils.isEmpty(model.firstImg)) {
            firstImg.setVisibility(View.VISIBLE);
            Glide.with(App.getInstance().getApplicationContext()).load(model.firstImg).error(R.mipmap.home_list_error_img).into(firstImg);
        } else {
            firstImg.setVisibility(View.GONE);
        }
        final View view = baseViewHolder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ColumnDetailActivity.class)
                        .putExtra("columnId", model.columnId + ""));
            }
        });
    }
}
