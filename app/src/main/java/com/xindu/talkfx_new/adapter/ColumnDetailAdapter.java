package com.xindu.talkfx_new.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.xindu.talkfx_new.activity.LoginActivity;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.bean.CommentInfo;
import com.xindu.talkfx_new.utils.SPUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class ColumnDetailAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {

    QMUIRoundButton discuss;
    private CommentInfo clickInfo;
    private String author;
    public CommentChildAdapter childAdapter;

    public ColumnDetailAdapter(List<CommentInfo> data, QMUIRoundButton discuss) {
        super(R.layout.item_comment, data);
        this.discuss = discuss;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public CommentInfo getClickInfo() {
        return clickInfo;
    }

    public void setClickInfo(CommentInfo clickInfo) {
        this.clickInfo = clickInfo;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CommentInfo model) {
        baseViewHolder.setText(R.id.userName, model.fromUserName.equals(getAuthor()) ? model.fromUserName + "(作者)" : model.fromUserName)
                .setText(R.id.data, model.createDate)
                .setText(R.id.content, model.content);

        ImageView headImg = baseViewHolder.getView(R.id.headImg);
//        Glide.with(App.getInstance().getApplicationContext()).load(model.headImg).into(headImg);

        RecyclerView view = baseViewHolder.getView(R.id.child_list);
        if (model.childList != null && model.childList.size() > 0) {
            view.setVisibility(View.VISIBLE);
            childAdapter = new CommentChildAdapter(model.childList, getAuthor());
            childAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    clickEvent(model);
                }
            });
            view.setLayoutManager(new LinearLayoutManager(mContext));
            view.setAdapter(childAdapter);
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setPadding(0, 20, 20, 8);
            textView.setText("查看更多回复");
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
        if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
            mContext.startActivity(new Intent(mContext, CommentDetailActivity.class)
                    .putExtra("CommentInfo", model));
            ((Activity) mContext).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        } else {
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }

    }
}
