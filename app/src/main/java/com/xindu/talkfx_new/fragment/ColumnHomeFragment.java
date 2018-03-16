package com.xindu.talkfx_new.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
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

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class ColumnHomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    ConvenientBanner convenientBanner;
    private int currentPage = 2;
    private ColumnListAdapter columnListAdapter;
    private boolean isInitCache = false;
    private boolean hasLoad = false;

    private String url = Constants.baseUrl + "/column";

    @Override
    protected int setContentView() {
        return R.layout.activity_column_list;
    }

    public static ColumnHomeFragment newInstance() {
        ColumnHomeFragment fragment = new ColumnHomeFragment();
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
        initBanner();
    }

    public void initBanner() {
        Integer[] list = {R.mipmap.banner_shengdan, R.mipmap.banner_football, R.mipmap.banner_lzhibanner};
        List<Integer> list_ = Arrays.asList(list);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_banner, null);
        convenientBanner = view.findViewById(R.id.convenientBanner);
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, list_)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.indicatior_unfocus, R.mipmap.indicatior_focus})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        columnListAdapter.addHeaderView(view);
    }

    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
//            Glide.with(getActivity().getApplicationContext()).load(data).into(imageView);
            imageView.setImageResource(data);
        }
    }

    @Override
    protected void lazyLoad() {
        if (!hasLoad) {
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
        OkGo.<BaseResponse<Columns>>get(url)
                .cacheKey("ColumnHomeFragment")       //由于该fragment会被复用,必须保证key唯一,否则数据会发生覆盖
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .execute(new MJsonCallBack<BaseResponse<Columns>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<Columns>> response) {
                        if (response.body().datas != null && response.body().datas.columns != null) {
                            List<ColumnInfo> results = response.body().datas.columns;
                            if (results != null) {
                                currentPage = 2;
                                columnListAdapter.setNewData(results);
                            }
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<BaseResponse<Columns>> response) {
                        //一般来说,只需呀第一次初始化界面的时候需要使用缓存刷新界面,以后不需要,所以用一个变量标识
                        if (!isInitCache) {
                            //一般来说,缓存回调成功和网络回调成功做的事情是一样的,所以这里直接回调onSuccess
                            onSuccess(response);
                            isInitCache = true;
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<Columns>> response) {
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
        OkGo.<BaseResponse<List<ColumnInfo>>>get(url + "/list?tid=0&rc=0&limit=10&page=" + currentPage)
                .cacheMode(CacheMode.NO_CACHE)       //上拉不需要缓存
                .execute(new MJsonCallBack<BaseResponse<List<ColumnInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<ColumnInfo>>> response) {
                        if (response.body().datas != null) {
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

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(3000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    class Columns {
        public List<ColumnInfo> columns;
    }
}
