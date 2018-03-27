package com.xindu.talkfx_new.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.fragment.ColumnFragment;
import com.xindu.talkfx_new.fragment.GroupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：主界面
 * 创建人：LeeBoo
 * 创建时间：2016/10/12 9:48
 */
public class HomeActivity extends BaseActivity {

    private List<BaseFragment> fragmentList = new ArrayList<>();
    private RadioButton r1;
    private RadioButton r2;
    private RadioButton r3;
    private RadioButton r4;
    private RadioButton r5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        r1 = (RadioButton) findViewById(R.id.rb_underling);
        r2 = (RadioButton) findViewById(R.id.rb_order);
        r3 = (RadioButton) findViewById(R.id.rb_income);
        r4 = (RadioButton) findViewById(R.id.rb_group);
        r4 = (RadioButton) findViewById(R.id.rb_my);
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        r5.setOnClickListener(this);
        r1.setSelected(true);
        initFragment();
    }

    private int currentIndex = 0;

    private void initFragment() {
        fragmentList.clear();
        ColumnFragment f1 = new ColumnFragment();
        GroupFragment f2 = new GroupFragment();
        GroupFragment f3 = new GroupFragment();
        GroupFragment f4 = new GroupFragment();
        GroupFragment f5 = new GroupFragment();
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
        fragmentList.add(f4);
        fragmentList.add(f5);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, f1, "0").commit();
    }

    @Override
    public void onClick(View v) {
        int index = 0;
        r1.setSelected(false);
        r2.setSelected(false);
        r3.setSelected(false);
        r4.setSelected(false);
        r5.setSelected(false);
        switch (v.getId()) {
            case R.id.rb_underling:
                index = 0;
                r1.setSelected(true);
                break;
            case R.id.rb_group:
                index = 1;
                r4.setSelected(true);
                break;
            case R.id.rb_my:
                index = 2;
                r5.setSelected(true);
                break;
            case R.id.rb_income:
                index = 3;
                r3.setSelected(true);
                break;
            case R.id.rb_order:
                index = 4;
                r2.setSelected(true);
                break;
        }

        if (index == currentIndex) {
            return;
        }
        BaseFragment fragment = fragmentList.get(index);
        //判断当前点击的Fragment有没有被添加到Activity中
        if (fragment.isAdded()) {
            //仅仅显示这个点击的Fragment，同时将老的隐藏掉
            showFragment(fragment, fragmentList.get(currentIndex));
        } else {
            //先添加当前点击的Fragment，同时将老的隐藏掉
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_content, fragment, index + "")
                    .hide(fragmentList.get(currentIndex))
                    .commit();
        }

        currentIndex = index;
    }

    private void showFragment(BaseFragment showFragment, BaseFragment hideFragment) {
        getSupportFragmentManager().beginTransaction()
                .show(showFragment)
                .hide(hideFragment)
                .commit();
    }

    public int backFlag = 0;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {// 过两秒后返回键按的第一次失效
                backFlag = 0;
            }
        }
    };

    /**
     * @author libao
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (backFlag == 0) {
                handler.sendEmptyMessageDelayed(1, 2000);
                backFlag = 1;
                showToast("再按一次退出程序");
            } else if (backFlag == 1) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
