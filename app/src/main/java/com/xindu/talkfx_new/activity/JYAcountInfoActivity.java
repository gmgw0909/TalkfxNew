package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.JYAccountListInfo;
import com.xindu.talkfx_new.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class JYAcountInfoActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.trader_name)
    TextView traderName;
    @Bind(R.id.trader_mt)
    TextView traderMt;
    @Bind(R.id.trader_account)
    TextView traderAccount;
    @Bind(R.id.trader_time)
    TextView traderTime;
    @Bind(R.id.trader_status)
    SwitchCompat traderStatus;
    private Object data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_info);
        ButterKnife.bind(this);
        getData();
        initTopBar();
    }

    private void initTopBar() {
        title.setText("我的账号");
    }

    @OnClick({R.id.btn_back, R.id.rl_unbind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_unbind:
                break;
        }
    }

    public void getData() {
        OkGo.<BaseResponse<JYAccountListInfo.ListInfo>>get(Constants.baseDataUrl + "/tradeAcct/info/" + getIntent().getIntExtra("id", 0))
                .execute(new MJsonCallBack<BaseResponse<JYAccountListInfo.ListInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<JYAccountListInfo.ListInfo>> response) {
                        if (response != null && response.body().datas != null) {
                            JYAccountListInfo.ListInfo info = response.body().datas;
//                            traderName.setText(info.name);
                            traderMt.setText(info.platform);
                            traderAccount.setText(info.acctNo);
//                            traderTime.setText(TimeUtil.convertToDifftime(TimeUtil.FORMAT_TIME_CN_2, Long.parseLong(info.createDate) * 1000));
                            if (!TextUtils.isEmpty(info.isSelf) && info.isSelf.equals("0")) {
                                traderStatus.setChecked(true);
                            } else {
                                traderStatus.setChecked(false);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<JYAccountListInfo.ListInfo>> response) {
                        Utils.errorResponse(mContext, response);
                    }
                });
    }
}
