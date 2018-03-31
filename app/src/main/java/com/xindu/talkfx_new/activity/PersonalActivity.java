package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.ViewPagerAdapter;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.CustomerResponse;
import com.xindu.talkfx_new.fragment.ColumnListFragment;
import com.xindu.talkfx_new.fragment.ColumnPersonalListFragment;
import com.xindu.talkfx_new.fragment.GroupFragment;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class PersonalActivity extends BaseActivity {

    String[] tabTitles = {"专栏", "动态", "群组"};
    @Bind(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @Bind(R.id.contentViewPager)
    ViewPager mViewPager;
    @Bind(R.id.headImg)
    CircleImageView headImg;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.rz_s)
    ImageView rzS;
    @Bind(R.id.rz_v)
    ImageView rzV;
    @Bind(R.id.fans)
    TextView fans;
    @Bind(R.id.follow)
    QMUIRoundButton follow;
    @Bind(R.id.summary_authenInfor)
    TextView summaryAuthenInfor;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.authenInfor)
    TextView authenInfor;
    @Bind(R.id.ll_summary)
    LinearLayout llSummary;
    @Bind(R.id.ll_authenInfor)
    LinearLayout llAuthenInfor;
    private ViewPagerAdapter mPagerAdapter;
    String customerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        initTabAndPager();
    }

    private void initTabAndPager() {
        customerId = getIntent().getStringExtra("customerId");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ColumnPersonalListFragment.newInstance(customerId));
        fragments.add(new GroupFragment());
        fragments.add(new GroupFragment());
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setItems(fragments, tabTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.text_normal));
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.blue));
        mTabSegment.setupWithViewPager(mViewPager);//设置TabLayout与ViewPager联动
        int space = QMUIDisplayHelper.dp2px(this, 26);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.setPadding(space, 0, space, 0);
        showDialog("正在加载");
        OkGo.<BaseResponse<CustomerResponse>>get(Constants.baseDataUrl + "/customer/personal/" + customerId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new MJsonCallBack<BaseResponse<CustomerResponse>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CustomerResponse>> response) {
                        CustomerResponse detailResponse = response.body().datas;
                        if (detailResponse != null) {
                            if (!TextUtils.isEmpty(detailResponse.getUserName())) {
                                userName.setText(detailResponse.getUserName());
                            } else {
                                userName.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.getHeadImg())) {
                                Glide.with(App.getInstance().getApplicationContext())
                                        .load(Constants.baseImgUrl + detailResponse.getHeadImg())
                                        .error(R.mipmap.default_person_icon)
                                        .into(headImg);
                            }
                            if (detailResponse.getConcernCount() != 0) {
                                fans.setText("粉丝：" + detailResponse.getConcernCount() + "人");
                            } else {
                                fans.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.getSummary())) {
                                llSummary.setVisibility(View.VISIBLE);
                                summary.setText(detailResponse.getSummary());
                            } else {
                                llSummary.setVisibility(View.GONE);
                                userName.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.getAuthenInfor())) {
                                llAuthenInfor.setVisibility(View.VISIBLE);
                                authenInfor.setText(detailResponse.getAuthenInfor());
                            } else {
                                llAuthenInfor.setVisibility(View.GONE);
                                authenInfor.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.getSummary()) && !TextUtils.isEmpty(detailResponse.getAuthenInfor())) {
                                summaryAuthenInfor.setText("个人简介\u0026认证信息");
                            } else if (TextUtils.isEmpty(detailResponse.getSummary()) && !TextUtils.isEmpty(detailResponse.getAuthenInfor())) {
                                summaryAuthenInfor.setText("认证信息");
                            } else if (!TextUtils.isEmpty(detailResponse.getSummary()) && TextUtils.isEmpty(detailResponse.getAuthenInfor())) {
                                summaryAuthenInfor.setText("个人简介");
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<CustomerResponse>> response) {
                        Utils.errorResponse(mContext,response);
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                    }
                });
    }

    @OnClick({R.id.btn_back, R.id.headImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                overridePendingTransition(0, R.anim.zoom_finish);
                break;
            case R.id.headImg:
                startActivity(new Intent(PersonalActivity.this, PersonalInfoActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.zoom_finish);
    }
}
