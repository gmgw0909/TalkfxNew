package com.xindu.talkfx_new.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.CommentInfo;
import com.xindu.talkfx_new.utils.ImageGetterUtil;
import com.xindu.talkfx_new.utils.SpannableStringUtils;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class CommentChildAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {

    String author;

    public CommentChildAdapter(List<CommentInfo> data, String author) {
        super(R.layout.item_comment_child, data);
        this.author = author;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CommentInfo info) {
        if (!TextUtils.isEmpty(info.fromUserName) && !TextUtils.isEmpty(info.toUserName) && !TextUtils.isEmpty(info.content)) {
            if (info.fromUserName.equals(author)) {
                baseViewHolder.setText(R.id.text, SpannableStringUtils
                        .getBuilder(info.fromUserName + "(作者)").setForegroundColor(mContext.getResources().getColor(R.color.text_orange))
                        .append(" 回复 ")
                        .append(info.toUserName).setForegroundColor(mContext.getResources().getColor(R.color.blue))
                        .append("：" + Html.fromHtml(info.content, new ImageGetterUtil(mContext, (TextView) baseViewHolder.getView(R.id.text)), null)).create());
            } else if (info.toUserName.equals(author)) {
                baseViewHolder.setText(R.id.text, SpannableStringUtils
                        .getBuilder(info.fromUserName).setForegroundColor(mContext.getResources().getColor(R.color.blue))
                        .append(" 回复 ")
                        .append(info.toUserName + "(作者)").setForegroundColor(mContext.getResources().getColor(R.color.text_orange))
                        .append("：" + Html.fromHtml(info.content, new ImageGetterUtil(mContext, (TextView) baseViewHolder.getView(R.id.text)), null)).create());
            } else {
                baseViewHolder.setText(R.id.text, SpannableStringUtils
                        .getBuilder(info.fromUserName).setForegroundColor(mContext.getResources().getColor(R.color.blue))
                        .append(" 回复 ")
                        .append(info.toUserName).setForegroundColor(mContext.getResources().getColor(R.color.blue))
                        .append("：" + Html.fromHtml(info.content, new ImageGetterUtil(mContext, (TextView) baseViewHolder.getView(R.id.text)), null)).create());
            }
        } else {
            baseViewHolder.setText(R.id.text, "此条回复数据错误!!!");
        }
    }
}
