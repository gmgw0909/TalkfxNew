package com.xindu.talkfx_new.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.CommentInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class CommentDetailAdapter extends BaseQuickAdapter<CommentInfo,BaseViewHolder> {

    QMUIRoundButton discuss;
    private CommentInfo clickInfo;
    private CommentInfo parentInfo;

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
    protected void convert(BaseViewHolder baseViewHolder, final CommentInfo model) {
        baseViewHolder.setText(R.id.userName, model.fromUserName)
                .setText(R.id.data, model.createDate)
                .setText(R.id.content, model.toUserName.equals(parentInfo.fromUserName + "") ? model.content : "回复 " + model.toUserName + "：" + model.content);

        ImageView headImg = baseViewHolder.getView(R.id.headImg);
//        Glide.with(App.getInstance().getApplicationContext()).load(model.headImg).into(headImg);

        RecyclerView view = baseViewHolder.getView(R.id.child_list);
        view.setVisibility(View.GONE);
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discuss.callOnClick();
                setClickInfo(model);
            }
        });
    }
}
