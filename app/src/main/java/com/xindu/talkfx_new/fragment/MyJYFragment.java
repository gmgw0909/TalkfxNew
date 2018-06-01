package com.xindu.talkfx_new.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.JYAcountInfoActivity;
import com.xindu.talkfx_new.activity.LoginActivity;
import com.xindu.talkfx_new.activity.MTPlatformActivity;
import com.xindu.talkfx_new.activity.PingCangActivity;
import com.xindu.talkfx_new.adapter.MyAccountAdapter;
import com.xindu.talkfx_new.adapter.PingCangAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.CurrentActInfo;
import com.xindu.talkfx_new.bean.JYAccountListInfo;
import com.xindu.talkfx_new.bean.PingCangInfo;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.ToastUtil;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.widget.RadarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class MyJYFragment extends BaseFragment {

    @Bind(R.id.radar_view)
    RadarView radarView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.ll_no_data)
    LinearLayout llNoData;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.ll_account)
    LinearLayout llAccount;
    @Bind(R.id.all)
    TextView all;
    @Bind(R.id.real)
    TextView real;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.win)
    TextView win;
    @Bind(R.id.delete)
    ImageView delete;

    List<PingCangInfo.PCInfo> list = new ArrayList<>();
    PingCangAdapter adapter;

    List<JYAccountListInfo.ListInfo> data = new ArrayList<>();
    MyAccountAdapter popAdapter;
    int currentAccountId;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected int setContentView() {
        return R.layout.fragment_my_jy;
    }

    @Override
    protected void lazyLoad() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new PingCangAdapter(list));
        popAdapter = new MyAccountAdapter(data);
        initPop();
        if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
            getHistory(SPUtil.getInt(Constants.USERID) + "");
            getTradeAcctList();
            getCurrentAccountDetail(SPUtil.getInt(Constants.USERID) + "");
        } else {
            llAccount.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
        }
    }

    private void getTradeAcctList() {
        OkGo.<BaseResponse<JYAccountListInfo>>get(Constants.baseDataUrl + "/tradeAcct/list")
                .execute(new MJsonCallBack<BaseResponse<JYAccountListInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<JYAccountListInfo>> response) {
                        if (response.body() != null && response.body().datas != null && response.body().datas.list != null
                                && response.body().datas.list.size() > 0) {
                            llAccount.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            data.clear();
                            data.addAll(response.body().datas.list);
                            if (data.size() == 1) {
                                delete.setVisibility(View.VISIBLE);
                            } else {
                                delete.setVisibility(View.GONE);
                            }
                            List list_ = new ArrayList();
                            for (int i = 0; i < data.size(); i++) {
                                if (!TextUtils.isEmpty(data.get(i).isDefault) && data.get(i).isDefault.equals("1")) {
                                    tvName.setText(response.body().datas.list.get(i).name + " | "
                                            + response.body().datas.list.get(i).acctNo);
                                    tvStatus.setText(response.body().datas.list.get(i).isSelf.equals("1") ? "(私密)" : "(公共)");
                                    currentAccountId = response.body().datas.list.get(i).tradeAcctId;
                                } else {
                                    list_.add(data.get(i));
                                }
                            }
                            popAdapter.setNewData(list_);
                        } else {
                            llAccount.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<JYAccountListInfo>> response) {
                        Utils.errorResponse(getActivity(), response);
                    }
                });
    }

    private void getCurrentAccountDetail(String userId) {
        OkGo.<BaseResponse<CurrentActInfo>>get(Constants.baseDataUrl + "/tradeAcctData/detail/mark?cycle=2&cid=" + userId)
                .execute(new MJsonCallBack<BaseResponse<CurrentActInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CurrentActInfo>> response) {
                        if (response != null && response.body().datas != null) {
                            CurrentActInfo info = response.body().datas;
                            all.setText(df.format(info.yield * 100) + "%");
                            real.setText(df.format(info.actualLeverage * 100) + "%");
                            back.setText(df.format(info.maxWithdrawlRate * 100) + "%");
                            time.setText(info.averageHoldingTime + "s");
                            win.setText(df.format(info.winRate * 100) + "%");

                            ArrayList<String> titles = new ArrayList<>();
                            ArrayList<Double> doubles = new ArrayList<>();
                            titles.add("抗风险能力\n" + info.antiRiskAbility + "分");
                            doubles.add(info.antiRiskAbility);
                            titles.add("稳定性\n" + info.veracity + "分");
                            doubles.add(info.veracity);
                            titles.add("可复制性\n" + info.replicability + "分");
                            doubles.add(info.replicability);
                            titles.add("风险控制\n" + info.riskRate + "分");
                            doubles.add(info.riskRate);
                            titles.add("盈利能力\n" + info.profitability + "分");
                            doubles.add(info.profitability);
                            radarView.setTitles(titles);
                            radarView.setData(doubles);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<CurrentActInfo>> response) {
                        Utils.errorResponse(getActivity(), response);
                    }
                });
    }

    //平仓历史
    private void getHistory(String userId) {
        OkGo.<BaseResponse<PingCangInfo>>get(Constants.baseDataUrl + "/tradeAcctData/detail/flow?status=1&cid=" + userId)
                .execute(new MJsonCallBack<BaseResponse<PingCangInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<PingCangInfo>> response) {
                        if (response != null && response.body().datas != null && response.body().datas.list != null) {
                            adapter.setNewData(response.body().datas.list);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<PingCangInfo>> response) {
                        Utils.errorResponse(getActivity(), response);
                    }
                });
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }

    @OnClick({R.id.pingcang_all, R.id.account_info, R.id.bt, R.id.ll_pop, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pingcang_all:
                startActivity(new Intent(getActivity(), PingCangActivity.class));
                break;
            case R.id.account_info:
                startActivity(new Intent(getActivity(), JYAcountInfoActivity.class)
                        .putExtra("id", currentAccountId));
                break;
            case R.id.bt:
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    startActivity(new Intent(getActivity(), MTPlatformActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_pop:
                if (Build.VERSION.SDK_INT >= 24) {
                    Rect visibleFrame = new Rect();
                    view.getGlobalVisibleRect(visibleFrame);
                    int height = view.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    qmuiPopup.setHeight(height);
                }
                qmuiPopup.showAsDropDown(view);
                break;
            case R.id.delete:
                new QMUIDialog.MessageDialogBuilder(getActivity())
                        .setTitle("删除账户")
                        .setMessage("确定要删除此账户吗？")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                delete();
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    PopupWindow qmuiPopup;

    private void initPop() {
        if (qmuiPopup == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_account_list, null);
            view.findViewById(R.id.click_dismiss).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qmuiPopup.dismiss();
                }
            });
            view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MTPlatformActivity.class));
                }
            });
            RecyclerView rv = view.findViewById(R.id.list_view);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(popAdapter);
            qmuiPopup = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, true);
            qmuiPopup.setOutsideTouchable(true);
            qmuiPopup.setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void delete() {
        showDialog();
        OkGo.<BaseResponse<JYAccountListInfo.ListInfo>>delete(Constants.baseDataUrl + "/tradeAcct/delete/" + currentAccountId)
                .execute(new MJsonCallBack<BaseResponse<JYAccountListInfo.ListInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<JYAccountListInfo.ListInfo>> response) {
                        if (response.body().code == 0) {
                            ToastUtil.showToast(getActivity(), "解绑成功");
                            EventBus.getDefault().post("bind_refresh");
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse<JYAccountListInfo.ListInfo>> response) {
                        Utils.errorResponse(getActivity(), response);
                        dismissDialog();
                    }
                });
    }

    @Subscribe
    public void onEvent(String msg) {
        if (msg.equals("bind_refresh")) {
            getTradeAcctList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }
}
