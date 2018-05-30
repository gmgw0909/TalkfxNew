package com.xindu.talkfx_new.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.PingCangAdapter;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.FollowResponse;
import com.xindu.talkfx_new.bean.PingCangInfo;
import com.xindu.talkfx_new.bean.TVInfo;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PingCangActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.title)
    TextView title;

    PingCangAdapter adapter;

    int currentPage = 1;
    int status = 0; //1下拉刷新 2上拉加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow_personal);
        ButterKnife.bind(this);
        title.setText("历史平仓");
        initData();
    }

    protected void initData() {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter = new PingCangAdapter(null);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);
        refreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE);
        refreshLayout.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this, recyclerView);

        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        setRefreshing(false);
        getHistory(SPUtil.getInt(Constants.USERID) + "");
//        status = 1;
//        currentPage = 1;
//        adapter.setEnableLoadMore(false);
//        requestData(currentPage);//第一页数据
    }

    @Override
    public void onLoadMoreRequested() {
        adapter.loadMoreComplete();
//        status = 2;
//        requestData(currentPage);
    }

    public void requestData(int page) {
        OkGo.<BaseResponse<FollowResponse>>get(Constants.baseDataUrl + "/customer/concern/users?limit=10&page=" + page)
                .cacheMode(CacheMode.NO_CACHE)       //上拉不需要缓存
                .execute(new MJsonCallBack<BaseResponse<FollowResponse>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<FollowResponse>> response) {
//                        List<UserInfo> results = response.body().datas.list;
                        List<TVInfo> results = new ArrayList<>();
                        results.add(new TVInfo());
                        results.add(new TVInfo());
                        results.add(new TVInfo());
                        results.add(new TVInfo());
                        results.add(new TVInfo());
                        if (status == 1) {
                            setData(true, results);
                        } else if (status == 2) {
                            setData(false, results);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<FollowResponse>> response) {
                        if (status == 2) {
                            //显示数据加载失败,点击重试
                            adapter.loadMoreFail();
                        }
                        //网络请求失败的回调
                        Utils.errorResponse(mContext, response);
                    }

                    @Override
                    public void onFinish() {
                        if (status == 1) {
                            adapter.setEnableLoadMore(true);
                            //可能需要移除之前添加的布局
                            adapter.removeAllFooterView();
                            //最后调用结束刷新的方法
                            setRefreshing(false);
                        }
                    }
                });
    }

    //平仓历史
    private void getHistory(String userId) {
        OkGo.<BaseResponse<PingCangInfo>>get(Constants.baseDataUrl + "/tradeAcctData/detail/flow?status=1&cid=" + userId)
                .execute(new MJsonCallBack<BaseResponse<PingCangInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<PingCangInfo>> response) {
                        if (response != null && response.body().datas != null && response.body().datas.list != null) {
                            adapter.setNewData(response.body().datas.list);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<PingCangInfo>> response) {
                        Utils.errorResponse(mContext, response);
                    }
                });
    }

    private void setData(boolean isRefresh, List data) {
        currentPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            adapter.setNewData(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
            }
        }
        if (isRefresh) {
            if (size < 8) {
                adapter.loadMoreEnd(isRefresh);
            } else {
                adapter.loadMoreComplete();
            }
        } else {
            if (size < Constants.PAGE_SIZE) {
                //第一页如果不够一页就不显示没有更多数据布局
                adapter.loadMoreEnd(isRefresh);
            } else {
                adapter.loadMoreComplete();
            }
        }
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
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
