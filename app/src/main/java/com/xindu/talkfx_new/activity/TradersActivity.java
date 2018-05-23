package com.xindu.talkfx_new.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.TradersAdapter;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.bean.TVInfo;
import com.xindu.talkfx_new.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class TradersActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.fl)
    FrameLayout fl;
    TradersAdapter adapter;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_jypz);
        ButterKnife.bind(this);
        initTopBar();
        initDialog();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        List list = new ArrayList();
        list.add(new TVInfo());
        list.add(new TVInfo());
        list.add(new TVInfo());
        list.add(new TVInfo());
        list.add(new TVInfo());
        adapter = new TradersAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dialog.show();
            }
        });
    }

    private void initDialog() {
        dialog = new Dialog(mContext, R.style.sc_FullScreenDialog);
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reNameView = mLayoutInflater.inflate(R.layout.pop_bind_account, null);
        final QMUIRoundButton button1 = reNameView.findViewById(R.id.btn1);
        final QMUIRoundButton button2 = reNameView.findViewById(R.id.btn2);
        button1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                button1.setBackgroundColor(getResources().getColor(R.color.blue));
                button2.setBackgroundColor(getResources().getColor(R.color.bg_normal));
                button1.setTextColor(getResources().getColor(R.color.white));
                button2.setTextColor(getResources().getColor(R.color.text_gray));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button1.setBackgroundColor(getResources().getColor(R.color.bg_normal));
                button2.setBackgroundColor(getResources().getColor(R.color.blue));
                button1.setTextColor(getResources().getColor(R.color.text_gray));
                button2.setTextColor(getResources().getColor(R.color.white));
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (Utils.getScreenWidth(mContext) * 0.9), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.addContentView(reNameView, params);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        dialogWindow.setAttributes(lp);
    }

    private void initTopBar() {
        title.setText("绑定交易账号");
    }

    @OnClick({R.id.btn_back, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.search:

                break;
        }
    }
}
