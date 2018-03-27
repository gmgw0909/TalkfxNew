package com.xindu.talkfx_new.fragment;

import android.content.Intent;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.SettingActivity;
import com.xindu.talkfx_new.base.BaseFragment;

import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class GroupFragment extends BaseFragment {

    @Override
    protected int setContentView() {
        return R.layout.fragment_group;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }
}
