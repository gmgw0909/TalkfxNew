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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.TradersAdapter;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.TraderInfo;
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

        adapter = new TradersAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mContext, TraderServersActivity.class)
                        .putExtra("mtp", getIntent().getStringExtra("mtp"))
                        .putExtra("dealerId", list.get(position).dealerId + ""));
            }
        });

        getData();
    }

    private void getData() {
        showDialog();
        OkGo.<BaseResponse<TraderInfo>>get(Constants.baseDataUrl + "/tradeAcct/bind/init")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new MJsonCallBack<BaseResponse<TraderInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<TraderInfo>> response) {
                        if (response.body().datas != null && response.body().datas.dealer != null
                                && response.body().datas.dealer.size() > 0) {
                            list.addAll(response.body().datas.dealer);
                            adapter.setNewData(response.body().datas.dealer);
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse<TraderInfo>> response) {
                        Utils.errorResponse(mContext, response);
                        dismissDialog();
                    }
                });
    }

    private void initTopBar() {
        title.setText("选择交易商");
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
