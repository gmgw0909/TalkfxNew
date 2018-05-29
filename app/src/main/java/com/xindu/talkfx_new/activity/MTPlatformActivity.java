package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.TradersAdapter;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.bean.TraderInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class MTPlatformActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.fl)
    FrameLayout fl;
    TradersAdapter adapter;
    List<TraderInfo.DealerInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_jypz);
        ButterKnife.bind(this);
        initTopBar();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        TraderInfo.DealerInfo info1 = new TraderInfo.DealerInfo();
        info1.dealerName = "MT4";
        list.add(info1);
        TraderInfo.DealerInfo info2 = new TraderInfo.DealerInfo();
        info2.dealerName = "MT5";
        list.add(info2);
        adapter = new TradersAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mContext, TradersActivity.class)
                        .putExtra("mtp", list.get(position).dealerName));
            }
        });
    }

    private void initTopBar() {
        title.setText("选择交易平台");
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
