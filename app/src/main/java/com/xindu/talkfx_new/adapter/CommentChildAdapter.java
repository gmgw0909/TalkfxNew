package com.xindu.talkfx_new.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.CommentInfo;

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
                baseViewHolder.setText(R.id.text, info.fromUserName + "(作者) 回复 " + info.toUserName + "：" + info.content);
            } else if (info.toUserName.equals(author)) {
                baseViewHolder.setText(R.id.text, info.fromUserName + " 回复 " + info.toUserName + "(作者)：" + info.content);
            } else {
                baseViewHolder.setText(R.id.text, info.fromUserName + " 回复 " + info.toUserName + "：" + info.content);
            }
        } else {
            baseViewHolder.setText(R.id.text, "此条回复数据错误!!!");
        }
    }
}
