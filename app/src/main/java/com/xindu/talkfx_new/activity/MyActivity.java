package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.MyInfoResponse;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class MyActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.headImg)
    CircleImageView headImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initTopBar();
        refreshUser();
    }

    private void refreshUser() {
        showDialog("加载中");
        OkGo.<BaseResponse<MyInfoResponse>>get(Constants.baseDataUrl + "/customer")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new MJsonCallBack<BaseResponse<MyInfoResponse>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<MyInfoResponse>> response) {
                        MyInfoResponse detailResponse = response.body().datas;
                        if (detailResponse != null && detailResponse.user != null) {
                            if (!TextUtils.isEmpty(detailResponse.user.userName)) {
                                userName.setText(detailResponse.user.userName);
                            } else {
                                userName.setText("");
                            }
                            if (!TextUtils.isEmpty(detailResponse.user.headImg)) {
                                Glide.with(App.getInstance().getApplicationContext())
                                        .load(Constants.baseImgUrl + detailResponse.user.headImg)
                                        .into(headImg);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<MyInfoResponse>> response) {
                        Utils.errorResponse(mContext, response);
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                    }
                });
    }

    @Subscribe
    public void onEvent(String msg) {
        if (msg.equals("refresh_headImg")) {
            refreshUser();
        }
    }

    private void initTopBar() {
        title.setText("我的");
    }

    @OnClick({R.id.btn_back, R.id.btn_setting, R.id.headImg, R.id.my_personal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_setting:
                startActivity(new Intent(MyActivity.this, SettingActivity.class));
                break;
            case R.id.headImg:
//                startActivity(new Intent(MyActivity.this, MyInfoActivity.class));
                break;
            case R.id.my_personal:
                startActivity(new Intent(MyActivity.this, PersonalActivity.class)
                        .putExtra("customerId", SPUtil.getInt(Constants.USERID) + ""));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
