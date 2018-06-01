package com.xindu.talkfx_new.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.JYAccountListInfo;
import com.xindu.talkfx_new.utils.ToastUtil;
import com.xindu.talkfx_new.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class MyAccountAdapter extends BaseQuickAdapter<JYAccountListInfo.ListInfo, BaseViewHolder> {

    public MyAccountAdapter(List<JYAccountListInfo.ListInfo> data) {
        super(R.layout.item_account, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final JYAccountListInfo.ListInfo info) {
        baseViewHolder.setText(R.id.tv_name, info.name + " | " + info.acctNo)
                .setText(R.id.tv_status, info.isSelf.equals("1") ? "(私密)" : "(公开)");
        baseViewHolder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QMUIDialog.MessageDialogBuilder(mContext)
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
                                delete(info);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void delete(JYAccountListInfo.ListInfo info) {
        OkGo.<BaseResponse<JYAccountListInfo.ListInfo>>delete(Constants.baseDataUrl + "/tradeAcct/delete/" + info.tradeAcctId)
                .execute(new MJsonCallBack<BaseResponse<JYAccountListInfo.ListInfo>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<JYAccountListInfo.ListInfo>> response) {
                        if (response.body().code == 0) {
                            ToastUtil.showToast(mContext, "解绑成功");
                            EventBus.getDefault().post("bind_refresh");
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<JYAccountListInfo.ListInfo>> response) {
                        Utils.errorResponse(mContext, response);
                    }
                });
    }
}
