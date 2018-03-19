package com.xindu.talkfx_new.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.CommentDetailAdapter;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.CommentInfo;
import com.xindu.talkfx_new.utils.KeyboardChangeListener;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.WrapContentLinearLayoutManager;
import com.xindu.talkfx_new.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
    RelativeLayout rl2;
    @Bind(R.id.et_discuss)
    EditText etDiscuss;
    @Bind(R.id.discuss)
    QMUIRoundButton discuss;

    CommentDetailAdapter adapter;

    View topView;
    CircleImageView headImg;
    TextView userName;
    TextView data;
    TextView content;

    int currentPage = 2;
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
                        .load(info.fromUserImg).error(R.mipmap.default_person_icon)
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
                content.setText(info.content);
            } else {
                content.setText("");
            }
        }
        url = Constants.baseUrl + "/comment/detail?gid=" + commentId + "&limit=10&page=";
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        adapter = new CommentDetailAdapter(null, info, discuss);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.addHeaderView(topView);
        recyclerView.setAdapter(adapter);

        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        refreshLayout.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this);

        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        status = 1;
        requestData(1);//第一页数据
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
                            if (results != null && results.size() > 0) {
                                currentPage = 2;
                                adapter.setNewData(results);
                            }
                        } else if (status == 2) {
                            if (results != null && results.size() > 0) {
                                currentPage++;
                                adapter.addData(results);
                            } else {
                                //显示没有更多数据
                                adapter.loadMoreComplete();
                                TextView textView = new TextView(CommentDetailActivity.this);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                textView.setLayoutParams(params);
                                textView.setPadding(0, 0, 0, 40);
                                textView.setText("无更多数据");
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(getResources().getColor(R.color.text_gray));
                                textView.setBackgroundColor(getResources().getColor(R.color.bg_normal));
                                adapter.addFooterView(textView);
                            }
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
                            //可能需要移除之前添加的布局
                            adapter.removeAllFooterView();
                            //最后调用结束刷新的方法
                            setRefreshing(false);
                        }
                    }
                });
    }

    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    @OnClick({R.id.discuss, R.id.collection, R.id.share, R.id.send, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.discuss:
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
        OkGo.<BaseResponse>post(Constants.baseUrl + "/comment/insert")
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        showToast("发送成功");
                        etDiscuss.setText("");
                        onRefresh();
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        showToast(response.getException().getMessage());
                    }
                });
    }

    //键盘打开关闭监听器
    private void initKeyboardChangeListener() {
        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    if (adapter.getClickInfo() != null && !TextUtils.isEmpty(adapter.getClickInfo().fromUserName)) {
                        etDiscuss.setHint("回复 " + adapter.getClickInfo().fromUserName);
                    }
                    rl2.setVisibility(View.VISIBLE);
                    rl1.setVisibility(View.GONE);
                } else {
                    adapter.setClickInfo(null);
                    etDiscuss.setHint("请您发表回复");
                    rl2.setVisibility(View.GONE);
                    rl1.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
