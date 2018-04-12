package com.xindu.talkfx_new.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.LoginActivity;
import com.xindu.talkfx_new.activity.MyActivity;
import com.xindu.talkfx_new.adapter.ViewPagerAdapter;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.CustomerResponse;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class ColumnFragment extends BaseFragment {

    @Bind(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @Bind(R.id.contentViewPager)
    ViewPager mViewPager;
    String[] tabTitles = {"动态", "分析", "教学", "心得", "业内新闻", "数据报告"};
    @Bind(R.id.btn_setting)
    CircleImageView btnSetting;
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_column;
    }

    private void initTabAndPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabTitles.length; i++) {
            if (i == 0) {
                fragments.add(ColumnHomeFragment.newInstance());
            } else {
                fragments.add(ColumnListFragment.newInstance(i + ""));
            }
        }
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mPagerAdapter.setItems(fragments, tabTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.text_normal));
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.blue));
        mTabSegment.setTypefaceProvider(new MyTypefaceProvider());
        mTabSegment.setupWithViewPager(mViewPager);//设置TabLayout与ViewPager联动
        int space = QMUIDisplayHelper.dp2px(getContext(), 26);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setPadding(space, 0, space, 0);
    }

    @Override
    protected void lazyLoad() {
        initTabAndPager();
        if (!SPUtil.getBoolean(Constants.IS_LOGIN, false)) return;
        refreshHeadImg();
    }

    private void refreshHeadImg() {
        OkGo.<BaseResponse<CustomerResponse>>get(Constants.baseDataUrl + "/customer/personal/" + SPUtil.getInt(Constants.USERID))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new MJsonCallBack<BaseResponse<CustomerResponse>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CustomerResponse>> response) {
                        CustomerResponse detailResponse = response.body().datas;
                        if (detailResponse != null) {
                            Glide.with(App.getInstance().getApplicationContext())
                                    .load(Constants.baseImgUrl + detailResponse.getHeadImg())
                                    .into(btnSetting);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<CustomerResponse>> response) {
                        Utils.errorResponse(getActivity(), response);
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                    }
                });
    }

    @OnClick({R.id.btn_setting, R.id.btn_msg})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_setting:
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false))
                    startActivity(new Intent(getActivity(), MyActivity.class));
                else
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_msg:

                break;
        }
    }

    @Subscribe
    public void onEvent(String msg) {
        if (msg.equals("refresh_headImg") || msg.equals("login_refresh")) {
            refreshHeadImg();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    //选中字体变粗
    class MyTypefaceProvider implements QMUITabSegment.TypefaceProvider {

        @Override
        public boolean isNormalTabBold() {
            return false;
        }

        @Override
        public boolean isSelectedTabBold() {
            return true;
        }
    }
}
