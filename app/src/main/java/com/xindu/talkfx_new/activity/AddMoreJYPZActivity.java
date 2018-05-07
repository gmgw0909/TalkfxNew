package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.AddMoreJYPZAdapter;
import com.xindu.talkfx_new.adapter.SecondaryListAdapter;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.bean.ItemInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class AddMoreJYPZActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    AddMoreJYPZAdapter adapter;
    private List<SecondaryListAdapter.DataTree<ItemInfo, ItemInfo>> dataTrees = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_jypz);
        ButterKnife.bind(this);
        initTopBar();
        initRv();
    }

    {
        dataTrees.add(new SecondaryListAdapter.DataTree<ItemInfo, ItemInfo>(new ItemInfo("外汇-主要货币对", "FX Majors"),
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<ItemInfo, ItemInfo>(new ItemInfo("外汇-交叉货币对", "FX Crosses"),
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<ItemInfo, ItemInfo>(new ItemInfo("贵金属", "Spot Metals"),
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<ItemInfo, ItemInfo>(new ItemInfo("大宗商品", "Agriculture"),
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<ItemInfo, ItemInfo>(new ItemInfo("股票", "Stocks"),
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<ItemInfo, ItemInfo>(new ItemInfo("股指", "indicates"),
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
    }

    private void initRv() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new AddMoreJYPZAdapter(this);
        adapter.setData(dataTrees);
        recyclerView.setAdapter(adapter);
    }

    private void initTopBar() {
        title.setText("添加更多交易品种");
    }

    @OnClick({R.id.btn_back, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.search:
                startActivity(new Intent(AddMoreJYPZActivity.this, SearchActivity.class));
                break;
        }
    }
}
