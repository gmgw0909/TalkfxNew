package com.xindu.talkfx_new.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.PersonalInfoActivity;
import com.xindu.talkfx_new.activity.SettingActivity;
import com.xindu.talkfx_new.adapter.ViewPagerAdapter;
import com.xindu.talkfx_new.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class ColumnFragment extends BaseFragment {

    @Bind(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @Bind(R.id.contentViewPager)
    ViewPager mViewPager;
    String[] tabTitles = {"动态", "分析", "教学", "心得", "业内新闻", "数据报告"};
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_column;
    }

    private void initTabAndPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tabTitles.length; i++) {
            if (i == 0) {
                fragments.add(ColumnHomeFragment.newInstance());
            } else {
                fragments.add(ColumnListFragment.newInstance(i + ""));
            }
        }
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mPagerAdapter.setItems(fragments, tabTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.text_normal));
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.blue));
        mTabSegment.setTypefaceProvider(new MyTypefaceProvider());
        mTabSegment.setupWithViewPager(mViewPager);//设置TabLayout与ViewPager联动
        int space = QMUIDisplayHelper.dp2px(getContext(), 26);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setPadding(space, 0, space, 0);
    }

    @Override
    protected void lazyLoad() {
        initTabAndPager();
    }

    @OnClick({R.id.btn_setting,R.id.btn_msg})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.btn_setting:
                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
                break;
            case R.id.btn_msg:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    //选中字体变粗
    class MyTypefaceProvider implements QMUITabSegment.TypefaceProvider {

        @Override
        public boolean isNormalTabBold() {
            return false;
        }

        @Override
        public boolean isSelectedTabBold() {
            return true;
        }
    }
}
