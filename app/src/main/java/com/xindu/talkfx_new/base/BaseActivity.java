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
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
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
    QMUIDialog qmuiDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        App.addActivity(this);
//        if (SPUtil.getBoolean(Constants.IS_LOGIN, false) && TextUtils.isEmpty("")) {
//            final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(mContext);
//            builder.setTitle("修改用户名")
//                    .setPlaceholder("在此输入您的用户名")
//                    .setInputType(InputType.TYPE_CLASS_TEXT)
//                    .addAction("确定", new QMUIDialogAction.ActionListener() {
//                        @Override
//                        public void onClick(QMUIDialog dialog, int index) {
//                            CharSequence text = builder.getEditText().getText();
//                            if (text != null && text.length() > 0) {
//                                dialog.dismiss();
//                            }
//                        }
//                    });
//            qmuiDialog = builder.create();
//            qmuiDialog.setCancelable(false);
//            qmuiDialog.show();
//        }
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
        if (info != null) {
            SPUtil.put(Constants.AUTHORIZATION, info.token);
            //登陆成功网络请求头设置token
            HttpHeaders headers = new HttpHeaders();
            headers.put(Constants.AUTHORIZATION, SPUtil.getString(Constants.AUTHORIZATION));
            OkGo.getInstance().addCommonHeaders(headers);
            if (info.user != null) {
                SPUtil.put(Constants.USERNAME, info.user.userName);
                SPUtil.put(Constants.USERID, info.user.customerId);
            }
        }
        SPUtil.put(Constants.IS_LOGIN, true);
        App.clearLoginActivity();
        if (getIntent().getBooleanExtra("goMain", false)) {
            startActivity(new Intent(mContext, HomeActivity.class));
        }
        EventBus.getDefault().post("login_refresh");
        EventBus.getDefault().post("refresh_follow_columns");
    }
}
