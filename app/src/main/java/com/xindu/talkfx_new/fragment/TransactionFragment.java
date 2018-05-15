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

public class TransactionFragment extends BaseFragment {

    @Bind(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @Bind(R.id.contentViewPager)
    ViewPager mViewPager;
    String[] tabTitles = {"排行榜", "我的交易", "我的跟随"};
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_quotes;
    }

    private void initTabAndPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new RankFragment());
        fragments.add(new MyJYFragment());
        fragments.add(new GroupFragment());
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mPagerAdapter.setItems(fragments, tabTitles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabSegment.setDefaultNormalColor(getResources().getColor(R.color.text_gray));
        mTabSegment.setDefaultSelectedColor(getResources().getColor(R.color.text_black));
        mTabSegment.setTypefaceProvider(new MyTypefaceProvider());
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
