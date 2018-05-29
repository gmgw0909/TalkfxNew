package com.xindu.talkfx_new.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.TraderServersAdapter;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.TraderServerInfo;
import com.xindu.talkfx_new.utils.ToastUtil;
import com.xindu.talkfx_new.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/15.
 */

public class TraderServersActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.fl)
    FrameLayout fl;
    TraderServersAdapter adapter;
    Dialog dialog;

    List<TraderServerInfo> list = new ArrayList<>();
    String mtp;
    String dealerId;
    String platformServer;
    int isSelf = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_jypz);
        ButterKnife.bind(this);
        mtp = getIntent().getStringExtra("mtp");
        dealerId = getIntent().getStringExtra("dealerId");
        initTopBar();
        initDialog();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new TraderServersAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                platformServer = list.get(position).serverName;
                serverName.setText(platformServer);
                dialog.show();
            }
        });
        getCurrentTraderCount();
        getData(mtp, dealerId);
    }

    private void getData(String mtp, String dealerId) {
        OkGo.<BaseResponse<List<TraderServerInfo>>>get(Constants.baseDataUrl + "/tradeAcct/bind/service?dealerId=" + dealerId + "&platform=" + mtp)
                .execute(new MJsonCallBack<BaseResponse<List<TraderServerInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<TraderServerInfo>>> response) {
                        if (response.body().datas != null && response.body().datas.size() > 0) {
                            list.addAll(response.body().datas);
                            adapter.setNewData(response.body().datas);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<TraderServerInfo>>> response) {
                        Utils.errorResponse(mContext, response);
                    }
                });
    }

    private void getCurrentTraderCount() {
        OkGo.<BaseResponse>post(Constants.baseDataUrl + "/tradeAcct/bind/number")
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {

                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        Utils.errorResponse(mContext, response);
                    }
                });
    }

    TextView serverName;

    private void initDialog() {
        dialog = new Dialog(mContext, R.style.sc_FullScreenDialog);
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View reNameView = mLayoutInflater.inflate(R.layout.pop_bind_account, null);
        serverName = reNameView.findViewById(R.id.title);
        final Button button1 = reNameView.findViewById(R.id.btn1);
        final Button button2 = reNameView.findViewById(R.id.btn2);
        final Button btnBind = reNameView.findViewById(R.id.btn_bind);
        final EditText etAccount = reNameView.findViewById(R.id.et_account);
        final EditText etPassword = reNameView.findViewById(R.id.et_password);
        etAccount.setHint("请输入平台" + mtp + "账号");
        button1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                isSelf = 1;
                button1.setBackgroundResource(R.drawable.shape_dialog_gk_btn_s);
                button2.setBackgroundResource(R.drawable.shape_dialog_sm_btn);
                button1.setTextColor(getResources().getColor(R.color.white));
                button2.setTextColor(getResources().getColor(R.color.text_gray));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelf = 0;
                button1.setBackgroundResource(R.drawable.shape_dialog_gk_btn);
                button2.setBackgroundResource(R.drawable.shape_dialog_sm_btn_s);
                button1.setTextColor(getResources().getColor(R.color.text_gray));
                button2.setTextColor(getResources().getColor(R.color.white));
            }
        });
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(mContext, "输入不能为空");
                    return;
                }
                bindAccount(account, password);
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (Utils.getScreenWidth(mContext) * 0.9), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.addContentView(reNameView, params);
        dialog.setCancelable(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
        dialogWindow.setAttributes(lp);
    }

    private void bindAccount(String account, String password) {
        showDialog();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("platform", mtp);
            jsonObject.put("dealerId", dealerId);
            jsonObject.put("platformServer", platformServer);
            jsonObject.put("acctNo", account);
            jsonObject.put("readPass", password);
            jsonObject.put("isDefault", 0);
            jsonObject.put("isSelf", isSelf);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<BaseResponse>post(Constants.baseDataUrl + "/tradeAcct/bind/acct")
                .upJson(jsonObject)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        if (response.body().code == 0) {
                            ToastUtil.showToast(mContext, "绑定成功");
                            dialog.dismiss();
                            EventBus.getDefault().post("bind_refresh");
                            App.clearBindActivity();
                        } else {
                            ToastUtil.showToast(mContext, "绑定失败，账号信息不对");
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        Utils.errorResponse(mContext, response);
                        dismissDialog();
                    }
                });
    }

    private void initTopBar() {
        title.setText("绑定交易账号");
    }

    @OnClick({R.id.btn_back, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.search:

                break;
        }
    }
}
