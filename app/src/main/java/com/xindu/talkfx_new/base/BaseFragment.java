package com.xindu.talkfx_new.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xindu.talkfx_new.utils.ToastUtil;

import butterknife.ButterKnife;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "LazyLoadFragment";
    private View view;
    protected QMUITipDialog tipDialog;
    protected Handler handler = new Handler();

    public void showToast(final String msg) {

        if (Looper.myLooper() == handler.getLooper()) {
            ToastUtil.showToast(getActivity(), msg);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(App.getInstance().getApplicationContext(), msg);
                }
            });
        }

    }

    public void showDialog() {
        tipDialog = new QMUITipDialog.Builder(getContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
    }

    public void dismissDialog() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setContentView(), container, false);
        ButterKnife.bind(this, view);
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
        return view;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        isInit = false;
        isLoad = false;

    }

    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract int setContentView();

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }
}
