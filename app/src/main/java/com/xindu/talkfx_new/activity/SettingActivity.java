package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.utils.DataCleanManager;

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

    @OnClick({R.id.btn_back,R.id.rl_clear_data})
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
        }
    }
}
