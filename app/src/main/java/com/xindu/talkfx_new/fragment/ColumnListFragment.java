package com.xindu.talkfx_new.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.ColumnDetailActivity;
import com.xindu.talkfx_new.adapter.ColumnListAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.ColumnInfo;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.utils.WrapContentLinearLayoutManager;

import java.util.List;

import butterknife.Bind;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class ColumnListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int currentPage = 2;
    private ColumnListAdapter mAdapter;
    private boolean isInitCache = false;
    private boolean hasLoad = false;

    private String url = Constants.baseDataUrl + "/column/list";
    private String type;

    @Override
    protected int setContentView() {
        return R.layout.activity_column_list;
    }

    public static ColumnListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        ColumnListFragment fragment = new ColumnListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected void configView() {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter = new ColumnListAdapter(null);
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.isFirstOnly(false);
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE);
        refreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this,recyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), ColumnDetailActivity.class)
                        .putExtra("columnId", ((ColumnInfo) (adapter.getItem(position))).columnId + ""));
                getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!hasLoad) {
            if (getArguments() != null) {
                type = getArguments().getString("type");
            }
            url = url + "?tid=" + type + "&rc=0&limit=" + Constants.PAGE_SIZE + "&page=";
            //配置view属性
            configView();
            //开启loading,获取数据
            showDialog();
            onRefresh();
            hasLoad = true;
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        currentPage = 1;
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        OkGo.<BaseResponse<List<ColumnInfo>>>get(url + "1")//
                .cacheKey("ColumnListFragment" + type)       //由于该fragment会被复用,必须保证key唯一,否则数据会发生覆盖
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .execute(new MJsonCallBack<BaseResponse<List<ColumnInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<ColumnInfo>>> response) {
                        List<ColumnInfo> results = response.body().datas;
                        setData(true, results);
                        mAdapter.setEnableLoadMore(true);
                    }

                    @Override
                    public void onCacheSuccess(Response<BaseResponse<List<ColumnInfo>>> response) {
                        //一般来说,只需呀第一次初始化界面的时候需要使用缓存刷新界面,以后不需要,所以用一个变量标识
                        if (!isInitCache) {
                            //一般来说,缓存回调成功和网络回调成功做的事情是一样的,所以这里直接回调onSuccess
                            onSuccess(response);
                            isInitCache = true;
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<ColumnInfo>>> response) {
                        mAdapter.setEnableLoadMore(true);
                        Utils.errorResponse(getActivity(),response);
                    }

                    @Override
                    public void onFinish() {
                        //可能需要移除之前添加的布局
                        mAdapter.removeAllFooterView();
                        //最后调用结束刷新的方法
                        dismissDialog();
                        setRefreshing(false);
                    }
                });
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        OkGo.<BaseResponse<List<ColumnInfo>>>get(url + currentPage)
                .cacheMode(CacheMode.NO_CACHE)       //上拉不需要缓存
                .execute(new MJsonCallBack<BaseResponse<List<ColumnInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<ColumnInfo>>> response) {
                        if (response.body().datas != null) {
                            List<ColumnInfo> results = response.body().datas;
                            setData(false, results);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<ColumnInfo>>> response) {
                        //显示数据加载失败,点击重试
                        mAdapter.loadMoreFail();
                        Utils.errorResponse(getActivity(),response);
                    }
                });
    }

    private void setData(boolean isRefresh, List data) {
        currentPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < Constants.PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
        } else {
            mAdapter.loadMoreComplete();
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
