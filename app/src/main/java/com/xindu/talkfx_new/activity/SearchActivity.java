package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.SearchAdapter;
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

public class SearchActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.history_float_layout)
    QMUIFloatLayout historyFloatLayout;
    @Bind(R.id.hot_float_layout)
    QMUIFloatLayout hotFloatLayout;
    @Bind(R.id.ll_search)
    LinearLayout llSearch;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    SearchAdapter adapter;
    private List<SecondaryListAdapter.DataTree<String, ItemInfo>> dataTrees = new ArrayList<>();

    String hots[] = {"USD/A", "USD", "AS", "USD/AUD", "美元/澳元", "美元澳元", "U", "人民币", "中国"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initTopBar();
        initData();
        initRv();
    }

    {
        dataTrees.add(new SecondaryListAdapter.DataTree<String, ItemInfo>("已添加",
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<String, ItemInfo>("外汇-主要货币对",
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<String, ItemInfo>("大宗商品",
                new ArrayList<ItemInfo>() {{
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                    add(new ItemInfo("澳元/美元", "AUD/USD"));
                    add(new ItemInfo("英镑/美元", "GBP/USD"));
                }}));
        dataTrees.add(new SecondaryListAdapter.DataTree<String, ItemInfo>("股票",
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
        adapter = new SearchAdapter(this);
        adapter.setItemOpen(true);
        adapter.setData(dataTrees);
        adapter.setCanClosed(false);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < hots.length; i++) {
            addItemToFloatLayout(hotFloatLayout, hots[i]);
        }
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                        addItemToFloatLayout(historyFloatLayout, etSearch.getText().toString());
                        llSearch.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    recyclerView.setVisibility(View.GONE);
                    llSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initTopBar() {
        title.setText("快速筛选");
    }

    private void addItemToFloatLayout(QMUIFloatLayout floatLayout, CharSequence s) {
        QMUIRoundButton tv = (QMUIRoundButton) LayoutInflater.from(mContext).inflate(R.layout.item_round_btn, null);
        tv.setText(s);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatLayout.addView(tv, lp);
    }

    @OnClick({R.id.btn_back, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.delete:
                historyFloatLayout.removeAllViews();
                break;
        }
    }
}
