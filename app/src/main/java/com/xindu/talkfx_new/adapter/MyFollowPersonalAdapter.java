package com.xindu.talkfx_new.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.UserInfo;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by LeeBoo on 2018/3/8.
 */

public class MyFollowPersonalAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {

    public MyFollowPersonalAdapter(List<UserInfo> data) {
        super(R.layout.item_follow_personal, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final UserInfo model) {
        CircleImageView firstImg = baseViewHolder.getView(R.id.user_icon);
        if (!TextUtils.isEmpty(model.headImg)) {
            Glide.with(App.getInstance().getApplicationContext())
                    .load(Constants.baseImgUrl + model.headImg)
                    .error(R.mipmap.default_person_icon)
                    .into(firstImg);
        }
        ((TextView) baseViewHolder.getView(R.id.userName)).setText(TextUtils.isEmpty(model.userName) ? "" : model.userName);
        ((TextView) baseViewHolder.getView(R.id.data)).setText("粉丝 " + model.concernCount + " | " + "文章 " + model.columnCount);

        final Button bt = baseViewHolder.getView(R.id.bt);
        if (!model.follow) {
            bt.setText("关注");
            bt.setTextColor(mContext.getResources().getColor(R.color.white));
            bt.setBackgroundResource(R.mipmap.btn_follow_bg);
        } else {
            bt.setText("取消关注");
            bt.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            bt.setBackgroundResource(R.color.white);
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QMUIDialog.MessageDialogBuilder(mContext)
                        .setTitle("取消关注")
                        .setMessage("确定要取消关注Ta嘛？")
                        .addAction("我再想想", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction(0, "取消关注", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("toid", model.customerId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                OkGo.<BaseResponse>put(Constants.baseDataUrl + "/concern/cancel")
                                        .upJson(obj)
                                        .execute(new MJsonCallBack<BaseResponse>() {
                                            @Override
                                            public void onSuccess(Response<BaseResponse> response) {
                                                if (response.body().code == 0) {
                                                    if (!model.follow) {
                                                        model.follow = true;
                                                    } else {
                                                        model.follow = false;
                                                    }
                                                    notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onError(Response<BaseResponse> response) {
                                                Utils.errorResponse(mContext, response);
                                            }
                                        });
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }
}
