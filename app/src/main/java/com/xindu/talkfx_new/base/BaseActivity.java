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

import com.xindu.talkfx_new.utils.ToastUtil;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public Context mContext;
    protected Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        App.addActivity(this);
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

}
