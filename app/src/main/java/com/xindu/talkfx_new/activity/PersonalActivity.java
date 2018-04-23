package com.xindu.talkfx_new.activity;

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
import com.xindu.talkfx_new.fragment.ColumnPersonalListFragment;
import com.xindu.talkfx_new.fragment.GroupFragment;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
    int followStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        initTabAndPager();
    }

    private void initTabAndPager() {
        customerId = getIntent().getStringExtra("customerId");
        if (customerId.equals(SPUtil.getInt(Constants.USERID) + "")) {
            follow.setVisibility(View.GONE);
        }
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    if (followStatus == 0) {
                        follow("cancel");
                    } else {
                        follow("care");
                    }
                } else {
                    startActivity(LoginActivity.class, false);
                }
            }
        });
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
                        if (detailResponse != null && detailResponse.user != null) {
                            followStatus = detailResponse.user.concernStatus;
                            if (detailResponse.user.concernStatus == 0) {
                                follow.setText("已关注");
                            } else {
                                follow.setText("+ 关注");
                            }
                            if (!TextUtils.isEmpty(detailResponse.user.userName)) {
                                userName.setText(detailResponse.user.userName);
                            } else {
                                userName.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.user.headImg)) {
                                Glide.with(App.getInstance().getApplicationContext())
                                        .load(Constants.baseImgUrl + detailResponse.user.headImg)
                                        .error(R.mipmap.default_person_icon)
                                        .into(headImg);
                            }
                            if (detailResponse.user.concernCount != 0) {
                                fans.setText("粉丝：" + detailResponse.user.concernCount + "人");
                            } else {
                                fans.setText("粉丝：0人");
                            }
                            if (!TextUtils.isEmpty(detailResponse.user.summary)) {
                                llSummary.setVisibility(View.VISIBLE);
                                summary.setText(detailResponse.user.summary);
                            } else {
                                llSummary.setVisibility(View.GONE);
                                summary.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.user.authenInfor)) {
                                llAuthenInfor.setVisibility(View.VISIBLE);
                                authenInfor.setText(detailResponse.user.authenInfor);
                            } else {
                                llAuthenInfor.setVisibility(View.GONE);
                                authenInfor.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.user.summary) && !TextUtils.isEmpty(detailResponse.user.authenInfor)) {
                                summaryAuthenInfor.setText("个人简介\u0026认证信息");
                            } else if (TextUtils.isEmpty(detailResponse.user.summary) && !TextUtils.isEmpty(detailResponse.user.authenInfor)) {
                                summaryAuthenInfor.setText("认证信息");
                            } else if (!TextUtils.isEmpty(detailResponse.user.summary) && TextUtils.isEmpty(detailResponse.user.authenInfor)) {
                                summaryAuthenInfor.setText("个人简介");
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<CustomerResponse>> response) {
                        Utils.errorResponse(mContext, response);
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
                break;
        }
    }

    private void follow(String s) {
        showDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("toid", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (s.equals("cancel")) {
            OkGo.<BaseResponse>put(Constants.baseDataUrl + "/concern/" + s)
                    .upJson(obj)
                    .execute(new MJsonCallBack<BaseResponse>() {
                        @Override
                        public void onSuccess(Response<BaseResponse> response) {
                            if (response.body().code == 0) {
                                if (followStatus == 0) {
                                    followStatus = 1;
                                    follow.setText("+ 关注");
                                } else if (followStatus == 1) {
                                    followStatus = 0;
                                    follow.setText("已关注");
                                }
                            }
                        }

                        @Override
                        public void onError(Response<BaseResponse> response) {
                            Utils.errorResponse(mContext, response);
                        }

                        @Override
                        public void onFinish() {
                            dismissDialog();
                        }
                    });
        } else {
            OkGo.<BaseResponse>post(Constants.baseDataUrl + "/concern/" + s)
                    .upJson(obj)
                    .execute(new MJsonCallBack<BaseResponse>() {
                        @Override
                        public void onSuccess(Response<BaseResponse> response) {
                            if (response.body().code == 0) {
                                if (followStatus == 0) {
                                    followStatus = 1;
                                    follow.setText("+ 关注");
                                } else if (followStatus == 1) {
                                    followStatus = 0;
                                    follow.setText("已关注");
                                }
                            }
                        }

                        @Override
                        public void onError(Response<BaseResponse> response) {
                            Utils.errorResponse(mContext, response);
                        }

                        @Override
                        public void onFinish() {
                            dismissDialog();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.zoom_finish);
    }
}
