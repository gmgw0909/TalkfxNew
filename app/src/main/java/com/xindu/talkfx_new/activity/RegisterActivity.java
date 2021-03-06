package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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
import com.xindu.talkfx_new.base.NetResponseCode;
import com.xindu.talkfx_new.bean.LoginInfo;
import com.xindu.talkfx_new.utils.StringUtil;
import com.xindu.talkfx_new.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.et_phone)
    TextInputEditText etPhone;
    @Bind(R.id.et_code)
    TextInputEditText etCode;
    /*验证码按钮*/
    @Bind(R.id.btn_send_code)
    TextView mBtnSendCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_send_code, R.id.register_btn, R.id.btn_back, R.id.ll_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !StringUtil.validatePhone(phone)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                //可以发送验证码啦 还得开始倒计时
                countDownTimerAndSendCode(phone);
                break;
            case R.id.register_btn:
                register();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_login:
                finish();
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode(String phone) {
        showDialog();
        OkGo.<BaseResponse>get(Constants.baseDataUrl + "/customer/regist/code/" + phone)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        BaseResponse r = response.body();
                        if (r.code == 0 && r.msg.equals(NetResponseCode.验证码已发送.getCode())) {
                            showToast(NetResponseCode.验证码已发送.getValue());
                            mCount = 60;
                            final Timer timer = new Timer();
                            TimerTask timertask = new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mCount--;
                                            mBtnSendCode.setText("(" + mCount + ")重新获取");
                                            if (mCount <= 0) {
                                                mBtnSendCode.setText("重新发送");
                                                mBtnSendCode.setEnabled(true);
                                                timer.cancel();
                                            }
                                        }
                                    });
                                }
                            };
                            timer.schedule(timertask, 1000, 1000);
                        } else {
                            mBtnSendCode.setText("重新发送");
                            mBtnSendCode.setEnabled(true);
                            showToast(NetResponseCode.getName(response.body().msg));
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        dismissDialog();
                        Utils.errorResponse(mContext,response);
                    }
                });
    }

    /*验证码的倒计时*/
    private int mCount;

    private void countDownTimerAndSendCode(String phone) {
        /*点了就不能点击啦*/
        mBtnSendCode.setEnabled(false);
        /*这个时候从服务器获取验证码*/
        sendCode(phone);
    }

    /**
     * 注册
     */
    private void register() {
        String phone = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !StringUtil.validatePhone(phone)) {
            showToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code) || code.length() != 6) {
            showToast("请输入6位验证码");
            return;
        }
        showDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("phone", phone);
            obj.put("zcSmsCode", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<BaseResponse<LoginInfo>>post(Constants.baseDataUrl + "/customer/regist/phone")
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse<LoginInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<LoginInfo>> response) {
                        BaseResponse r = response.body();
                        if (r.code == 0) {
                            isLogin(response.body().datas);
                        } else {
                            showToast(NetResponseCode.getName(response.body().msg));
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse<LoginInfo>> response) {
                        dismissDialog();
                        Utils.errorResponse(mContext,response);
                    }
                });
    }
}
