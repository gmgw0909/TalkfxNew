package com.xindu.talkfx_new.adapter;

import android.graphics.Color;
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
import com.xindu.talkfx_new.utils.SpannableStringUtils;
import com.xindu.talkfx_new.utils.TimeUtil;

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
        baseViewHolder.setText(R.id.title, model.title)
                .setText(R.id.comment_count, model.commentCount + "")
                .setText(R.id.read_count, model.readCount + "")
                .setText(R.id.content, SpannableStringUtils.getBuilder(model.userName)
                        .setForegroundColor(mContext.getResources().getColor(R.color.text_black))
                        .append(" | " + model.miniContent)
                        .setForegroundColor(mContext.getResources().getColor(R.color.text_gray))
                        .create())
                .setText(R.id.data, (TextUtils.isEmpty(model.createDate) ? "" : TimeUtil.convertToDifftime(TimeUtil.FORMAT_TIME_CN_2, Long.parseLong(model.createDate) * 1000)));
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
        baseViewHolder.getView(R.id.view).setBackgroundColor(Color.parseColor(getViewColor(model.typeTitle)));
    }

    private String getViewColor(String typeTitle) {
        String color = "";
        if (TextUtils.isEmpty(typeTitle)) {
            return "#ffffff";
        }
        switch (typeTitle) {
            case "分析":
                color = "#4CA0FF";
                break;
            case "教学":
                color = "#A628FF";
                break;
            case "心得":
                color = "#FF4C4C";
                break;
            case "业内新闻":
                color = "#FFA028";
                break;
            case "数据报告":
                color = "#20C43D";
                break;
        }
        return color;
    }
}
