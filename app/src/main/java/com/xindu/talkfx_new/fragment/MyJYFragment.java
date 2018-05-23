package com.xindu.talkfx_new.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.JYAcountInfoActivity;
import com.xindu.talkfx_new.activity.PingCangActivity;
import com.xindu.talkfx_new.activity.TradersActivity;
import com.xindu.talkfx_new.adapter.PingCangAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.bean.TVInfo;
import com.xindu.talkfx_new.widget.RadarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class MyJYFragment extends BaseFragment {

    @Bind(R.id.radar_view)
    RadarView radarView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    PingCangAdapter adapter;
    boolean isInit;

    @Override
    protected int setContentView() {
        return R.layout.fragment_my_jy;
    }

    @Override
    protected void lazyLoad() {
        if (!isInit) {
            ArrayList<String> titles = new ArrayList<String>();
            titles.add("抗风险能力\n85分");
            titles.add("稳定性\n65分");
            titles.add("可复制性\n46分");
            titles.add("风险控制\n54分");
            titles.add("盈利能力\n76分");
            radarView.setTitles(titles);
            List<TVInfo> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(new TVInfo());
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter = new PingCangAdapter(list));
            isInit = true;
        }
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }

    @OnClick({R.id.pingcang_all, R.id.account_info, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pingcang_all:
                startActivity(new Intent(getActivity(), PingCangActivity.class));
                break;
            case R.id.account_info:
                startActivity(new Intent(getActivity(), JYAcountInfoActivity.class));
                break;
            case R.id.add:
                startActivity(new Intent(getActivity(), TradersActivity.class));
                break;
        }
    }
}
