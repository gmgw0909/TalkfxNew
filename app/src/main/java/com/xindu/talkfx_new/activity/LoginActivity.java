package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.base.NetResponseCode;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.StringUtil;

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

public class LoginActivity extends BaseActivity {
    /*tab下划线改变动画状态*/
    public boolean isChecked = true;

    /*tab下划线动画*/
    private Animation mAnimationRight;
    private Animation mAnimationLeft;
    /*账号登录*/
    @Bind(R.id.tv_account_register)
    TextView mTvAccountRegister;
    /*手机登录*/
    @Bind(R.id.tv_phone_register)
    TextView mTvPhoneRegister;
    /*账号布局*/
    @Bind(R.id.ll_account_login)
    LinearLayout mllAccountLogin;
    /*手机登录布局*/
    @Bind(R.id.ll_phone_login)
    LinearLayout mllPhoneLogin;
    /*左边线*/
    @Bind(R.id.view_line_left)
    View mViewLineLeft;
    /*右边线*/
    @Bind(R.id.view_line_right)
    View mViewLineRight;
    /*手机号/邮箱/用户名*/
    @Bind(R.id.et_account)
    TextInputEditText etAccount;
    /*密码*/
    @Bind(R.id.et_password)
    TextInputEditText etPassword;
    /*手机号*/
    @Bind(R.id.et_phone)
    TextInputEditText etPhone;
    /*验证码*/
    @Bind(R.id.et_code)
    TextInputEditText etCode;
    /*验证码按钮*/
    @Bind(R.id.btn_send_code)
    TextView mBtnSendCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initAnimation();
    }

    @OnClick({R.id.tv_account_register, R.id.tv_phone_register, R.id.btn_send_code,
            R.id.login_btn, R.id.btn_back, R.id.ll_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*账号登录Tab*/
            case R.id.tv_account_register:
                   /*线条开启动画*/
                if (!isChecked) {
                    mViewLineRight.startAnimation(mAnimationRight);
                    /*改变动画状态。*/
                    isChecked = true;
                }
                   /*清除动画*/
                break;
            /*手机登录Tab*/
            case R.id.tv_phone_register:
                   /*线条开启动画*/
                /*改变!isChecked  表示flase*/
                if (isChecked) {
                    mViewLineLeft.startAnimation(mAnimationLeft);
                    isChecked = false;
                }
                break;
            /*登录按钮*/
            case R.id.login_btn:
                if (mllAccountLogin.getVisibility() == View.VISIBLE) {
                    accountLogin();
                } else {
                    phoneLogin();
                }
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_register:
                startActivity(RegisterActivity.class, false);
                break;
            case R.id.btn_send_code:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || !StringUtil.validatePhone(phone)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                //可以发送验证码啦 还得开始倒计时
                countDownTimerAndSendCode(phone);
                break;
        }
    }

    /**
     * 发送验证码
     */
    private void sendCode(String phone) {
        showDialog();
        OkGo.<BaseResponse>get(Constants.baseUrl + "/customer/login/code/" + phone)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        BaseResponse r = response.body();
                        if (r.code == 0 && r.msg.equals(NetResponseCode.验证码已发送.getCode())) {
                            showToast(NetResponseCode.验证码已发送.getValue());
                        } else {
                            showToast(NetResponseCode.getName(response.body().msg));
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        showToast(response.getException().getMessage());
                        dismissDialog();
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
        /*已经倒计时了。不能再次让他发送*/
        /*不用初始化*/
        mCount = 60;
        /*弄一个计时器*/
        final Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                /*在ui里面操作*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCount--;
                        /*把每一秒显示上去*/
                        mBtnSendCode.setText("(" + mCount + ")重新获取");
                        if (mCount <= 0) {
                            mBtnSendCode.setText("重新发送");
                            /*设置可以点击*/
                            mBtnSendCode.setEnabled(true);
                            /*别忘记   取消计时器*/
                            timer.cancel();
                        }
                    }
                });
            }
        };
        /*这句话不能忘记*/
        timer.schedule(timertask, 1000, 1000);
    }

    /**
     * 手机号登录
     */
    private void phoneLogin() {
        String phone = etPhone.getText().toString().trim();
        String smsCode = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !StringUtil.validatePhone(phone)) {
            showToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(smsCode) || smsCode.length() != 6) {
            showToast("请输入6位验证码");
            return;
        }
        showDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("loginPhone", phone);
            obj.put("smsCode", smsCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<BaseResponse>post(Constants.baseUrl + "/customer/login/fast")
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        BaseResponse r = response.body();
                        if (r.code == 0 && r.msg.equals(NetResponseCode.登录成功.getCode())) {
                            App.clearLoginActivity();
                            SPUtil.put(Constants.IS_LOGIN, true);
                            isLogin();
                        } else {
                            showToast(NetResponseCode.getName(response.body().msg));
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        showToast(response.getException().getMessage());
                        dismissDialog();
                    }
                });
    }

    /**
     * 帐号登录
     */
    private void accountLogin() {
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            showToast("输入帐号不能为空");
            return;
        }
        if (TextUtils.isEmpty(password) || !StringUtil.validatePwd(password)) {
            showToast("请输入6-18位的密码");
            return;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("userName", account);
            obj.put("passWord", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialog();
        OkGo.<BaseResponse>post(Constants.baseUrl + "/customer/login")
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        BaseResponse r = response.body();
                        if (r.code == 0 && r.msg.equals(NetResponseCode.登录成功.getCode())) {
                            App.clearLoginActivity();
                            SPUtil.put(Constants.IS_LOGIN, true);
                            isLogin();
                        } else {
                            showToast(NetResponseCode.getName(response.body().msg));
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        showToast(response.getException().getMessage());
                        dismissDialog();
                    }
                });
    }

    private void initAnimation() {
          /* 线左边移动 */
        mAnimationRight = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_left);
        mAnimationRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTvAccountRegister.setTextColor(getResources().getColor(R.color.blue));
                mTvPhoneRegister.setTextColor(getResources().getColor(R.color.text_gray));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewLineLeft.setVisibility(View.VISIBLE);
                mViewLineRight.setVisibility(View.INVISIBLE);
                /*手机*/
                mllPhoneLogin.setVisibility(View.GONE);
                /*账号*/
                mllAccountLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        /* 线右边移动 */
        mAnimationLeft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.view_line_move_right);
        mAnimationLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override/*动画开始*/
            public void onAnimationStart(Animation animation) {
                /*账号登录*/
                mTvAccountRegister.setTextColor(getResources().getColor(R.color.text_gray));
                /*手机登陆*/
                mTvPhoneRegister.setTextColor(getResources().getColor(R.color.blue));
            }

            @Override/*动画结束*/
            public void onAnimationEnd(Animation animation) {
                mViewLineLeft.setVisibility(View.INVISIBLE);
                mViewLineRight.setVisibility(View.VISIBLE);
                /*手机*/
                mllPhoneLogin.setVisibility(View.VISIBLE);
                /*账号*/
                mllAccountLogin.setVisibility(View.GONE);
            }

            @Override/*动画重复*/
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
