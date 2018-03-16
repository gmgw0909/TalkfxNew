package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.xindu.talkfx_new.base.BaseActivity;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(HomeActivity.class, true);
            }
        }, 1000);
    }
}
