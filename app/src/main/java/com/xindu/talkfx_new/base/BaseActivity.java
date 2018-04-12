package com.xindu.talkfx_new.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xindu.talkfx_new.activity.HomeActivity;
import com.xindu.talkfx_new.bean.LoginInfo;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public Context mContext;
    protected Handler handler = new Handler();
    protected QMUITipDialog tipDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        App.addActivity(this);
    }

    public void showDialog(String s) {
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(s)
                .create();
        tipDialog.show();
    }

    public void showDialog() {
        showDialog("正在请求");
    }

    public void dismissDialog() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {

    }

    public void showToast(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (Looper.myLooper() == handler.getLooper()) {
            ToastUtil.showToast(this, msg);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(getApplicationContext(), msg);
                }
            });
        }

    }

    protected void startActivity(Class clazz, boolean isFinish) {
        startActivity(new Intent(mContext, clazz));
        if (isFinish) {
            finish();
        }
    }

    /**
     * 保存用户信息
     */
    public void isLogin(LoginInfo info) {
        SPUtil.put(Constants.USERNAME, info.user.userName);
        SPUtil.put(Constants.TOKEN, info.token);
        SPUtil.put(Constants.IS_LOGIN, true);
        App.clearLoginActivity();
        if (getIntent().getBooleanExtra("goMain", false)) {
            startActivity(new Intent(mContext, HomeActivity.class));
        }
        EventBus.getDefault().post("login_refresh");
        //登陆成功网络请求头设置token
        HttpHeaders headers = new HttpHeaders();
        headers.put("token", SPUtil.getString(Constants.TOKEN));
        OkGo.getInstance().addCommonHeaders(headers);
    }
}
