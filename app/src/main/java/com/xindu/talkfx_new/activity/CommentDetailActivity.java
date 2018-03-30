package com.xindu.talkfx_new.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.CommentDetailAdapter;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.CommentInfo;
import com.xindu.talkfx_new.utils.ImageGetterUtil;
import com.xindu.talkfx_new.utils.KeyboardUtil;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.WrapContentLinearLayoutManager;
import com.xindu.talkfx_new.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.rl1)
    RelativeLayout rl1;
    @Bind(R.id.rl2)
    LinearLayout rl2;
    @Bind(R.id.et_discuss)
    EditText etDiscuss;
    @Bind(R.id.discuss)
    QMUIRoundButton discuss;
    @Bind(R.id.click_close_kb)
    Button clickCloseKb;

    CommentDetailAdapter adapter;

    View topView;
    CircleImageView headImg;
    TextView userName;
    TextView data;
    TextView content;

    int currentPage = 1;
    int status = 0; //1下拉刷新 2上拉加载

    CommentInfo info;
    String commentId = "";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_detail);
        ButterKnife.bind(this);
        etDiscuss.setHint("请您发表回复");

        topView = LayoutInflater.from(this).inflate(R.layout.topview_comment_detail, null);
        headImg = topView.findViewById(R.id.user_icon);
        userName = topView.findViewById(R.id.userName);
        data = topView.findViewById(R.id.data);
        content = topView.findViewById(R.id.content);

        initData();
        initKeyboardChangeListener();
    }

    protected void initData() {
        info = (CommentInfo) getIntent().getSerializableExtra("CommentInfo");
        if (info != null) {
            commentId = info.commentId + "";
            if (!TextUtils.isEmpty(info.fromUserImg)) {
                Glide.with(App.getInstance().getApplicationContext())
                        .load(Constants.baseImgUrl + info.fromUserImg)
                        .error(R.mipmap.default_person_icon)
                        .into(headImg);
            }
            if (!TextUtils.isEmpty(info.fromUserName)) {
                userName.setText(info.fromUserName);
            } else {
                userName.setText("");
            }
            if (!TextUtils.isEmpty(info.createDate)) {
                data.setText(info.createDate);
            } else {
                data.setText("");
            }
            if (!TextUtils.isEmpty(info.content)) {
                content.setText(Html.fromHtml(info.content, new ImageGetterUtil(mContext, content), null));
            } else {
                content.setText("");
            }
        }
        url = Constants.baseDataUrl + "/comment/detail?gid=" + commentId + "&limit=" + Constants.PAGE_SIZE + "&page=";
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        adapter = new CommentDetailAdapter(null, info, discuss);
        adapter.setAuthor(getIntent().getStringExtra("author"));
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.addHeaderView(topView);
        recyclerView.setAdapter(adapter);

        refreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE);
        refreshLayout.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter quickAdapter, View view, int position) {
                if (SPUtil.getString(Constants.USERNAME).equals(((CommentInfo) (quickAdapter.getItem(position))).fromUserName)) {
                    showListPopup(view, (CommentInfo) quickAdapter.getItem(position), listItems2, position);
                } else {
                    showListPopup(view, (CommentInfo) quickAdapter.getItem(position), listItems1, position);
                }
                return false;
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
                discuss.callOnClick();
                adapter.setClickInfo((CommentInfo) quickAdapter.getItem(position));
            }
        });

        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        status = 1;
        currentPage = 1;
        adapter.setEnableLoadMore(false);
        requestData(currentPage);//第一页数据
    }

    @Override
    public void onLoadMoreRequested() {
        status = 2;
        requestData(currentPage);
    }

    public void requestData(int page) {
        OkGo.<BaseResponse<List<CommentInfo>>>get(url + page)
                .cacheMode(CacheMode.NO_CACHE)       //不需要缓存
                .execute(new MJsonCallBack<BaseResponse<List<CommentInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<CommentInfo>>> response) {
                        List<CommentInfo> results = response.body().datas;
                        if (status == 1) {
                            setData(true, results);
                        } else if (status == 2) {
                            setData(false, results);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<CommentInfo>>> response) {
                        if (status == 2) {
                            //显示数据加载失败,点击重试
                            adapter.loadMoreFail();
                        }
                        //网络请求失败的回调
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        if (status == 1) {
                            adapter.setEnableLoadMore(true);
                            //可能需要移除之前添加的布局
                            adapter.removeAllFooterView();
                            //最后调用结束刷新的方法
                            setRefreshing(false);
                        }
                    }
                });
    }

    private void setData(boolean isRefresh, List data) {
        currentPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            adapter.setNewData(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
            }
        }
        if (isRefresh) {
            if (size < 8) {
                adapter.loadMoreEnd(isRefresh);
            } else {
                adapter.loadMoreComplete();
            }
        } else {
            if (size < Constants.PAGE_SIZE) {
                //第一页如果不够一页就不显示没有更多数据布局
                adapter.loadMoreEnd(isRefresh);
            } else {
                adapter.loadMoreComplete();
            }
        }
    }

    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    @OnClick({R.id.discuss, R.id.collection, R.id.share, R.id.send, R.id.btn_back, R.id.click_close_kb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.discuss:
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    KeyboardUtil.openKeyboard(etDiscuss, this);
                    etDiscuss.requestFocus();
                } else {
                    startActivity(LoginActivity.class, false);
                }
                break;
            case R.id.collection:
                break;
            case R.id.share:
                break;
            case R.id.send:
                sendComment();
                break;
            case R.id.btn_back:
                finish();
                overridePendingTransition(0, R.anim.zoom_finish);
                break;
            case R.id.click_close_kb:
                KeyboardUtil.closeKeyboard(etDiscuss, this);
                break;
        }
    }

    /**
     * 发布评论
     */
    public void sendComment() {
        String content = etDiscuss.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("评论内容不能为空");
            return;
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "2");
            obj.put("columnId", info.columnId);
            obj.put("content", content);
            obj.put("groupId", info.commentId);
            if (adapter.getClickInfo() != null) {
                obj.put("toCustomerId", adapter.getClickInfo().fromUserId);
                obj.put("toCommentId", adapter.getClickInfo().commentId);
            } else {
                obj.put("toCustomerId", info.fromUserId);
                obj.put("toCommentId", info.commentId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<BaseResponse>post(Constants.baseDataUrl + "/comment/insert")
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        showToast("发送成功");
                        etDiscuss.setText("");
                        onRefresh();
                        KeyboardUtil.closeKeyboard(etDiscuss, CommentDetailActivity.this);
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        showToast(response.getException().getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.zoom_finish);
    }

    //键盘打开关闭监听器
    private void initKeyboardChangeListener() {
        new KeyboardUtil(this).setKeyBoardListener(new KeyboardUtil.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    if (adapter.getClickInfo() != null && !TextUtils.isEmpty(adapter.getClickInfo().fromUserName)) {
                        etDiscuss.setHint("回复 " + adapter.getClickInfo().fromUserName);
                    }
                    rl2.setVisibility(View.VISIBLE);
                    rl1.setVisibility(View.GONE);
                    clickCloseKb.setVisibility(View.VISIBLE);
                } else {
                    adapter.setClickInfo(null);
                    etDiscuss.setHint("请您发表回复");
                    rl2.setVisibility(View.GONE);
                    rl1.setVisibility(View.VISIBLE);
                    clickCloseKb.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 长按弹框
     */
    private QMUIListPopup mListPopup;
    String[] listItems1 = new String[]{
            "回复",
            "举报",
    };
    String[] listItems2 = new String[]{
            "回复",
            "删除",
    };

    private void showListPopup(View v, final CommentInfo model, final String[] listItems, final int p) {
        List<String> data = new ArrayList<>();
        Collections.addAll(data, listItems);
        ArrayAdapter adapter_ = new ArrayAdapter<>(mContext, R.layout.simple_list_item, data);
        mListPopup = new QMUIListPopup(mContext, QMUIPopup.DIRECTION_NONE, adapter_);
        mListPopup.create(QMUIDisplayHelper.dp2px(mContext, 250), QMUIDisplayHelper.dp2px(mContext, 200), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listItems[i].equals("回复")) {
                    discuss.callOnClick();
                    adapter.setClickInfo(model);
                } else if (listItems[i].equals("删除")) {
                    OkGo.<BaseResponse>delete(Constants.baseDataUrl + "/comment/delete/" + model.commentId)
                            .execute(new MJsonCallBack<BaseResponse>() {
                                @Override
                                public void onSuccess(Response<BaseResponse> response) {
                                    BaseResponse r = response.body();
                                    if (r.code == 0) {
                                        adapter.remove(p);
                                        showToast("删除成功");
                                    }
                                }

                                @Override
                                public void onError(Response<BaseResponse> response) {

                                }
                            });
                } else if (listItems[i].equals("举报")) {

                }
                mListPopup.dismiss();
            }
        });
        mListPopup.setAnimStyle(QMUIPopup.ANIM_AUTO);
        mListPopup.show(v);
    }

    /**
     * 删除确认框
     */
    private void showDeleteDialog() {
        new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle("删除评论")
                .setMessage("确定要删除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                        dialog.dismiss();
                    }
                })
                .show();
    }
}
