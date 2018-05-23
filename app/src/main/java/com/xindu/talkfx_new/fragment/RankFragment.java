package com.xindu.talkfx_new.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.RankAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.bean.UserInfo;
import com.xindu.talkfx_new.utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class RankFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.title_bar)
    View title;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    RankAdapter adapter;
    boolean isInit;

    @Override
    protected int setContentView() {
        return R.layout.activity_my_follow_personal;
    }

    @Override
    protected void lazyLoad() {
        if (!isInit) {
            title.setVisibility(View.GONE);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
            adapter = new RankAdapter(null);
            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            recyclerView.setAdapter(adapter);
            refreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE);
            adapter.setEnableLoadMore(false);
            refreshLayout.setOnRefreshListener(this);
            //开启loading,获取数据
            setRefreshing(true);
            onRefresh();
            isInit = true;
        }
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    @Override
    public void onRefresh() {
        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new UserInfo());
        }
        adapter.setNewData(list);
        setRefreshing(false);
    }

    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }
}
