package com.xindu.talkfx_new.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.ColumnListAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.ColumnInfo;
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
    private ColumnListAdapter columnListAdapter;
    private boolean isInitCache = false;
    private boolean hasLoad = false;

    private String url = Constants.baseUrl + "/column/list";
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
        columnListAdapter = new ColumnListAdapter(null);
        columnListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        columnListAdapter.isFirstOnly(true);
        recyclerView.setAdapter(columnListAdapter);
        refreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE);
        refreshLayout.setOnRefreshListener(this);
        columnListAdapter.setOnLoadMoreListener(this);
    }

    @Override
    protected void lazyLoad() {
        if (!hasLoad) {
            if (getArguments() != null) {
                type = getArguments().getString("type");
            }
            url = url + "?tid=" + type + "&rc=0&limit=10&page=";
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
        OkGo.<BaseResponse<List<ColumnInfo>>>get(url + "1")//
                .cacheKey("ColumnListFragment" + type)       //由于该fragment会被复用,必须保证key唯一,否则数据会发生覆盖
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .execute(new MJsonCallBack<BaseResponse<List<ColumnInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<ColumnInfo>>> response) {
                        List<ColumnInfo> results = response.body().datas;
                        if (results != null) {
                            currentPage = 2;
                            columnListAdapter.setNewData(results);
                        }
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
                        //网络请求失败的回调,一般会弹个Toast
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        //可能需要移除之前添加的布局
                        columnListAdapter.removeAllFooterView();
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
        OkGo.<BaseResponse<List<ColumnInfo>>>get(url + currentPage)//
                .cacheMode(CacheMode.NO_CACHE)       //上拉不需要缓存
                .execute(new MJsonCallBack<BaseResponse<List<ColumnInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<ColumnInfo>>> response) {
                        List<ColumnInfo> results = response.body().datas;
                        if (results != null && results.size() > 0) {
                            currentPage++;
                            columnListAdapter.addData(results);
                        } else {
                            //显示没有更多数据
                            columnListAdapter.loadComplete();
//                            TextView textView = new TextView(getContext());
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                            params.setMargins(0, 20, 0, 20);
//                            textView.setLayoutParams(params);
//                            textView.setText("无更多数据");
//                            textView.setGravity(Gravity.CENTER);
//                            textView.setTextColor(getResources().getColor(R.color.text_gray));
//                            columnListAdapter.addFooterView(textView);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<ColumnInfo>>> response) {
                        //显示数据加载失败,点击重试
                        columnListAdapter.showLoadMoreFailedView();
                        //网络请求失败的回调,一般会弹个Toast
                        showToast(response.getException().getMessage());
                    }
                });
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
