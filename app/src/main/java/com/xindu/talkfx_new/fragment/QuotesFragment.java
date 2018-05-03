package com.xindu.talkfx_new.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.ViewPagerAdapter;
import com.xindu.talkfx_new.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class QuotesFragment extends BaseFragment {

    @Bind(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @Bind(R.id.contentViewPager)
    ViewPager mViewPager;
    String[] tabTitles = {"交易品种", "日历"};
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_quotes;
    }

    private void initTabAndPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TransactionVarietyFragment());
        fragments.add(ColumnFollowFragment.newInstance());
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mPagerAdapter.setItems(fragments, tabTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.text_gray));
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.text_black));
        mTabSegment.setupWithViewPager(mViewPager);//设置TabLayout与ViewPager联动
        mTabSegment.setIndicatorWidthAdjustContent(false);
    }

    @Override
    protected void lazyLoad() {
        initTabAndPager();
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }

}
