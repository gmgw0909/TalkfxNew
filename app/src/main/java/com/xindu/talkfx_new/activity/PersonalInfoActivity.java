package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.utils.SPUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class PersonalInfoActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        initTopBar();
    }

    private void initTopBar() {
        title.setText("个人资料");
    }

    @OnClick({R.id.btn_back, R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
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
                                SPUtil.clear(mContext);
                                App.clearActivity();
                                startActivity(LoginActivity.class, false);
                            }
                        })
                        .show();
//                MessageBox.promptTwoDialog("取消", "确定", mContext, "确定要退出登录吗？", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        App.clearActivity();
//                        startActivity(LoginActivity.class, false);
//                        ((Dialog) view.getTag()).dismiss();
//                    }
//                });
                break;
        }
    }
}