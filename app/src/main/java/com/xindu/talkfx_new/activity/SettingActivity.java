package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class SettingActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initTopBar();
    }

    private void initTopBar() {
        title.setText("设置");
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                startActivity(new Intent(SettingActivity.this,PersonalInfoActivity.class));
                break;
        }
    }
}
