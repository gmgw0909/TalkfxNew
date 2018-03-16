package com.xindu.talkfx_new.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.CommentDetailActivity;
import com.xindu.talkfx_new.bean.CommentInfo;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class ColumnDetailAdapter extends BaseQuickAdapter<CommentInfo> {

    QMUIRoundButton discuss;
    private CommentInfo clickInfo;
    public BaseQuickAdapter childAdapter;

    public ColumnDetailAdapter(List<CommentInfo> data, QMUIRoundButton discuss) {
        super(R.layout.item_comment, data);
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
        baseViewHolder.setText(R.id.userName, model.fromUserName)
                .setText(R.id.data, model.createDate)
                .setText(R.id.content, model.content);

        ImageView headImg = baseViewHolder.getView(R.id.headImg);
//        Glide.with(App.getInstance().getApplicationContext()).load(model.headImg).into(headImg);
        baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discuss.callOnClick();
                setClickInfo(model);
            }
        });
        RecyclerView view = baseViewHolder.getView(R.id.child_list);
        if (model.childList != null && model.childList.size() > 0) {
            view.setVisibility(View.VISIBLE);
            childAdapter = new BaseQuickAdapter<CommentInfo>(R.layout.item_comment_child, model.childList) {
                @Override
                protected void convert(BaseViewHolder baseViewHolder, CommentInfo info) {
                    if (!TextUtils.isEmpty(info.fromUserName) && !TextUtils.isEmpty(info.toUserName) && !TextUtils.isEmpty(info.content)) {
                        baseViewHolder.setText(R.id.text, info.fromUserName + " 回复 " + info.toUserName + "：" + info.content);
                    } else {
                        baseViewHolder.setText(R.id.text, "此条回复数据错误!!!");
                    }
                    baseViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickEvent(model);
                        }
                    });
                }
            };
            view.setLayoutManager(new LinearLayoutManager(mContext));
            view.setAdapter(childAdapter);
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setPadding(0, 20, 20, 8);
            textView.setText("查看评论 >");
            textView.setGravity(Gravity.RIGHT);
            textView.setTextColor(mContext.getResources().getColor(R.color.blue));
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            childAdapter.addFooterView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEvent(model);
                }
            });
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void clickEvent(CommentInfo model) {
        mContext.startActivity(new Intent(mContext, CommentDetailActivity.class)
                .putExtra("CommentInfo", model));
    }
}
