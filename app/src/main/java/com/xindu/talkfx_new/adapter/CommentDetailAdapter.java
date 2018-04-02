package com.xindu.talkfx_new.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.bean.CommentInfo;
import com.xindu.talkfx_new.utils.ImageGetterUtil;
import com.xindu.talkfx_new.utils.SpannableStringUtils;
import com.xindu.talkfx_new.utils.TimeUtil;
import com.xindu.talkfx_new.widget.CircleImageView;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class CommentDetailAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {

    QMUIRoundButton discuss;
    private CommentInfo clickInfo;
    private CommentInfo parentInfo;
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public CommentDetailAdapter(List<CommentInfo> data, CommentInfo parentInfo, QMUIRoundButton discuss) {
        super(R.layout.item_comment, data);
        this.parentInfo = parentInfo;
        this.discuss = discuss;
    }

    public CommentInfo getClickInfo() {
        return clickInfo;
    }

    public void setClickInfo(CommentInfo clickInfo) {
        this.clickInfo = clickInfo;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CommentInfo model) {
        baseViewHolder.setText(R.id.userName, model.fromUserName.equals(getAuthor())
                ? SpannableStringUtils.getBuilder(model.fromUserName + "(作者)")
                .setForegroundColor(mContext.getResources().getColor(R.color.text_orange)).create()
                : SpannableStringUtils.getBuilder(model.fromUserName)
                .setForegroundColor(mContext.getResources().getColor(R.color.blue)).create())
                .setText(R.id.data, TimeUtil.convertToDifftime(TimeUtil.FORMAT_TIME_MM_dd_HH_mm, TimeUtil.covertToLong(TimeUtil.FORMAT_TIME_EN, model.createDate)))
                .setText(R.id.content, setCommenter(baseViewHolder, model));

        CircleImageView headImg = baseViewHolder.getView(R.id.user_icon);
        if (!TextUtils.isEmpty(model.fromUserImg)) {
            Glide.with(App.getInstance().getApplicationContext())
                    .load(Constants.baseImgUrl + model.fromUserImg)
                    .error(R.mipmap.default_person_icon)
                    .into(headImg);
        }
        RecyclerView view = baseViewHolder.getView(R.id.child_list);
        view.setVisibility(View.GONE);
    }

    private CharSequence setCommenter(BaseViewHolder baseViewHolder, CommentInfo model) {
        if (model.toUserName.equals(parentInfo.fromUserName + "")) {
            return Html.fromHtml(model.content, new ImageGetterUtil(mContext, (TextView) baseViewHolder.getView(R.id.content)), null);
        } else {
            return model.toUserName.equals(getAuthor())
                    ? SpannableStringUtils.getBuilder("回复 ").append(model.toUserName + "(作者)")
                    .setForegroundColor(mContext.getResources().getColor(R.color.text_orange))
                    .append("：" + Html.fromHtml(model.content, new ImageGetterUtil(mContext, (TextView) baseViewHolder.getView(R.id.content)), null)).create()
                    : SpannableStringUtils.getBuilder("回复 ").append(model.toUserName)
                    .setForegroundColor(mContext.getResources().getColor(R.color.blue))
                    .append("：" + Html.fromHtml(model.content, new ImageGetterUtil(mContext, (TextView) baseViewHolder.getView(R.id.content)), null)).create();
        }
    }
}
