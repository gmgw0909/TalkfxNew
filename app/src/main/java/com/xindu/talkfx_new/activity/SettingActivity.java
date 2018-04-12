package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.utils.DataCleanManager;
import com.xindu.talkfx_new.utils.SPUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class SettingActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.app_data_clear)
    TextView appDataClear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initTopBar();
        try {
            appDataClear.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTopBar() {
        title.setText("设置");
    }

    @OnClick({R.id.btn_back,R.id.rl_clear_data,R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_clear_data:
                DataCleanManager.clearAllCache(mContext);
                final QMUITipDialog tipDialog = new QMUITipDialog.Builder(mContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord("清除缓存完成")
                        .create();
                tipDialog.show();
                appDataClear.setText("0.0KB");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, 1000);
                break;
            case R.id.logout:
                new QMUIDialog.MessageDialogBuilder(mContext)
                        .setTitle("退出登录")
                        .setMessage("确定要退出登录吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                SPUtil.put(Constants.IS_LOGIN, false);
                                SPUtil.put(Constants.USERNAME, "");
                                SPUtil.put(Constants.TOKEN, "");
                                SPUtil.put(Constants.USERID, "");
                                //登陆成功网络请求头设置token
                                HttpHeaders headers = new HttpHeaders();
                                headers.put("token", SPUtil.getString(Constants.TOKEN));
                                OkGo.getInstance().addCommonHeaders(headers);
                                App.clearActivity();
                                startActivity(new Intent(SettingActivity.this, LoginActivity.class)
                                        .putExtra("goMain", true));
                            }
                        })
                        .show();
                break;
        }
    }
}
