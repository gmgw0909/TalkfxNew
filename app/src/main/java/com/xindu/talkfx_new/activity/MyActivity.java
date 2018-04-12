package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.utils.SPUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class MyActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        initTopBar();
    }

    private void initTopBar() {
        title.setText("我的");
    }

    @OnClick({R.id.btn_back, R.id.btn_setting, R.id.rl_user, R.id.my_personal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_setting:
                startActivity(new Intent(MyActivity.this, SettingActivity.class));
                break;
            case R.id.rl_user:
                startActivity(new Intent(MyActivity.this, MyInfoActivity.class));
                break;
            case R.id.my_personal:
                startActivity(new Intent(MyActivity.this, PersonalActivity.class)
                        .putExtra("customerId", SPUtil.getInt(Constants.USERID) + ""));
                break;
        }
    }
}
