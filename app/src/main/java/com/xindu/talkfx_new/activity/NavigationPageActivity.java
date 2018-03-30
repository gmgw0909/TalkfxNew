package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：LeeBoo on 2018/3/30 15:07
 * 邮箱：1003220931@qq.com
 * 描述：NavigationPageActivity
 */
public class NavigationPageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.viewpager)
    ViewPager vp;
    @Bind(R.id.go_in)
    Button goIn;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    private PagerAdapter vpAdapter;
    private List<View> views;

    //引导图片资源
    private static final int[] pics = {R.mipmap.navigation_page_1, R.mipmap.navigation_page_2, R.mipmap.navigation_page_3};

    //底部小店图片
    private ImageView[] dots;

    //记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_navigation_page);
        ButterKnife.bind(this);
        goIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(HomeActivity.class, true);
            }
        });
        views = new ArrayList<>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            mParams.gravity = Gravity.CENTER;
            linearLayout.setLayoutParams(mParams);
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            linearLayout.addView(iv);
            linearLayout.setGravity(Gravity.CENTER);
            views.add(linearLayout);
        }
        //初始化Adapter
        vpAdapter = new PagerAdapter() {

            //销毁arg1位置的界面
            @Override
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(views.get(arg1));
            }

            //获得当前界面数
            @Override
            public int getCount() {
                if (views != null) {
                    return views.size();
                }
                return 0;
            }


            //初始化arg1位置的界面
            @Override
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(views.get(arg1), 0);
                return views.get(arg1);
            }

            //判断是否由对象生成界面
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return (arg0 == arg1);
            }

        };
        vp.setAdapter(vpAdapter);
        //绑定回调
        vp.setOnPageChangeListener(this);
        //初始化底部小点
        initDots();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        setCurDot(arg0);
        goIn.setVisibility(View.GONE);
        switch (arg0) {
            case 0:
                tv1.setText("分享");
                tv2.setText("分享观点 交流心得");
                break;
            case 1:
                tv1.setText("倾听");
                tv2.setText("用心倾听 感受彼此");
                break;
            case 2:
                goIn.setVisibility(View.VISIBLE);
                tv1.setText("同行");
                tv2.setText("投资路上 你我同行");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }
}
