package com.xindu.talkfx_new.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.ColumnDetailAdapter;
import com.xindu.talkfx_new.base.App;
import com.xindu.talkfx_new.base.BaseActivity;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.JavascriptInterface;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.bean.ColumnDetailResponse;
import com.xindu.talkfx_new.bean.CommentInfo;
import com.xindu.talkfx_new.utils.KeyboardUtil;
import com.xindu.talkfx_new.utils.SPUtil;
import com.xindu.talkfx_new.utils.SpannableStringUtils;
import com.xindu.talkfx_new.utils.TimeUtil;
import com.xindu.talkfx_new.utils.Utils;
import com.xindu.talkfx_new.utils.WrapContentLinearLayoutManager;
import com.xindu.talkfx_new.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ColumnDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

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
    @Bind(R.id.collection)
    ImageView collection;

    View topView;
    TextView title;
    CircleImageView headImg;
    TextView userName;
    TextView readCount;
    TextView data;
    TextView opinion;
    WebView content;
    TextView commentsCount;
    TextView zanUp;
    TextView zanDown;
    QMUIFloatLayout floatLayout;
    CardView hdLayout;
    LinearLayout opinionLayout;
    QMUIRoundButton follow;

    ColumnDetailAdapter mAdapter;
    int currentPage = 1;

    String columnId = "";
    boolean sendSuccess;
    String customerId = "";
    int collectStatus;
    int followStatus;
    String viewStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        topView = LayoutInflater.from(this).inflate(R.layout.topview_column_detail, null);
        title = topView.findViewById(R.id.title);
        readCount = topView.findViewById(R.id.read_count);
        headImg = topView.findViewById(R.id.headImg);
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ColumnDetailActivity.this, PersonalActivity.class)
                        .putExtra("customerId", customerId));
            }
        });
        userName = topView.findViewById(R.id.userName);
        data = topView.findViewById(R.id.data);
        opinion = topView.findViewById(R.id.opinion);
        content = topView.findViewById(R.id.content);
        commentsCount = topView.findViewById(R.id.commentsCount);
        follow = topView.findViewById(R.id.follow);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    if (followStatus == 0) {
                        follow("cancel");
                    } else {
                        follow("care");
                    }
                } else {
                    startActivity(LoginActivity.class, false);
                }
            }
        });

        floatLayout = topView.findViewById(R.id.float_layout);
        hdLayout = topView.findViewById(R.id.handan_layout);
        opinionLayout = topView.findViewById(R.id.opinion_layout);
        zanDown = topView.findViewById(R.id.zan_down);
        zanUp = topView.findViewById(R.id.zan_up);
        zanUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    opinion("support");
                } else {
                    startActivity(LoginActivity.class, false);
                }
            }
        });
        zanDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    opinion("oppose");
                } else {
                    startActivity(LoginActivity.class, false);
                }
            }
        });
        WebSettings webSettings = content.getSettings();// 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        content.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //这段js函数的功能就是注册监听，遍历所有的img标签，并添加onClick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
                content.loadUrl("javascript:(function(){"
                        + "var objs = document.getElementsByTagName(\"img\"); "
                        + "for(var i=0;i<objs.length;i++)  " + "{"
                        + "    objs[i].onclick=function()  " + "    {  "
                        + "        window.imagelistner.openImage(this.src);  "
                        + "    }  " + "}" + "})()");
            }
        });
        columnId = getIntent().getStringExtra("columnId");
        initData();
        initKeyboardChangeListener();
        //开启loading,获取数据
        setRefreshing(true);
        onRefresh();
    }

    protected void initData() {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        mAdapter = new ColumnDetailAdapter(null, discuss);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.addHeaderView(topView);
        recyclerView.setAdapter(mAdapter);

        refreshLayout.setColorSchemeColors(Color.BLACK, Color.BLUE);
        refreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter quickAdapter, View view, int position) {
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    if (SPUtil.getString(Constants.USERNAME).equals(((CommentInfo) (quickAdapter.getItem(position))).fromUserName)) {
                        showListPopup(view, (CommentInfo) quickAdapter.getItem(position), listItems2, position);
                    } else {
                        showListPopup(view, (CommentInfo) quickAdapter.getItem(position), listItems1, position);
                    }
                } else {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
                return false;
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    discuss.callOnClick();
                    mAdapter.setClickInfo((CommentInfo) quickAdapter.getItem(position));
                } else {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });
    }

    private void addItemToFloatLayout(QMUIFloatLayout floatLayout, CharSequence s) {
        TextView textView = new TextView(this);
        int textViewPadding = QMUIDisplayHelper.dp2px(this, 8);
        textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_normal));
        textView.setBackgroundColor(Color.parseColor("#f8f8f8"));
        textView.setText(s);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatLayout.addView(textView, lp);
    }

    @Subscribe
    public void onEvent(String msg) {
        if (msg.equals("isRefresh") || msg.equals("login_refresh")) {
            setRefreshing(true);
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        mAdapter.setEnableLoadMore(false);
        OkGo.<BaseResponse<ColumnDetailResponse>>get(Constants.baseDataUrl + "/column/detail/" + columnId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new MJsonCallBack<BaseResponse<ColumnDetailResponse>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<ColumnDetailResponse>> response) {
                        ColumnDetailResponse detailResponse = response.body().datas;
                        if (detailResponse != null) {
                            if (detailResponse.column != null) {
                                if (detailResponse.column.commentCount != 0) {
                                    commentsCount.setText("评论 (" + detailResponse.column.commentCount + ")");
                                } else {
                                    commentsCount.setText("暂无评论");
                                }
                                //喊单数据
                                setDataToTopView(detailResponse);
                                if (!TextUtils.isEmpty(detailResponse.column.title)) {
                                    title.setText(detailResponse.column.title);
                                } else {
                                    title.setText("暂无");
                                }
                                if (!TextUtils.isEmpty(detailResponse.column.createDate)) {
                                    data.setText(TimeUtil.convertToDifftime(TimeUtil.FORMAT_TIME_MM_dd_HH_mm, TimeUtil.covertToLong(TimeUtil.FORMAT_TIME_EN, detailResponse.column.createDate)));
                                } else {
                                    data.setText("暂无");
                                }
                                if (!TextUtils.isEmpty(detailResponse.column.content)) {
                                    String s = detailResponse.column.content;
                                    String s1 = s.replace("data/resource/showImg?path=", Constants.baseUrl + "data/resource/showImg?path=");
                                    String html_body = s1.replace(Constants.baseUrl + Constants.baseUrl, Constants.baseUrl);
                                    content.addJavascriptInterface(new JavascriptInterface(ColumnDetailActivity.this, Utils.returnImageUrlsFromHtml(Utils.getHtmlData(html_body))), "imagelistner");
                                    content.loadDataWithBaseURL(null, Utils.getHtmlData(html_body), "text/html", "utf-8", null);
                                }
                                if (!TextUtils.isEmpty(detailResponse.column.opinion)) {
                                    opinionLayout.setVisibility(View.VISIBLE);
                                    opinion.setText(detailResponse.column.opinion);
                                    if (detailResponse.column.supportCount != 0) {
                                        zanUp.setText(detailResponse.column.supportCount + "");
                                    } else {
                                        zanUp.setText("0");
                                    }
                                    if (detailResponse.column.opposeCount != 0) {
                                        zanDown.setText(detailResponse.column.opposeCount + "");
                                    } else {
                                        zanDown.setText("0");
                                    }
                                } else {
                                    opinionLayout.setVisibility(View.GONE);
                                }
                                if (!TextUtils.isEmpty(detailResponse.column.viewStatus)) {
                                    viewStatus = detailResponse.column.viewStatus;
                                    if (detailResponse.column.viewStatus.equals("1")) {
                                        Drawable drawable = getResources().getDrawable(R.mipmap.zan_up_s);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        zanUp.setCompoundDrawables(drawable, null, null, null);
                                        zanUp.setTextColor(getResources().getColor(R.color.orange));
                                    } else if (detailResponse.column.viewStatus.equals("0")) {
                                        Drawable drawable = getResources().getDrawable(R.mipmap.zan_down_s);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                        zanDown.setCompoundDrawables(drawable, null, null, null);
                                        zanDown.setTextColor(getResources().getColor(R.color.orange));
                                    }
                                }
                                collectStatus = detailResponse.column.collectStatus;
                                if (detailResponse.column.collectStatus == 1) {
                                    collection.setImageResource(R.mipmap.column_btn_collection);
                                } else {
                                    collection.setImageResource(R.mipmap.column_btn_collection_s);
                                }
                                if (detailResponse.column.readCount != 0) {
                                    readCount.setText(detailResponse.column.readCount + "");
                                } else {
                                    readCount.setText("0");
                                }
                            }
                            if (detailResponse.user != null) {
                                customerId = detailResponse.user.customerId + "";
                                if (!TextUtils.isEmpty(detailResponse.user.userName)) {
                                    userName.setText(detailResponse.user.userName);
                                } else {
                                    userName.setText("暂无");
                                }
                                if (!TextUtils.isEmpty(detailResponse.user.headImg)) {
                                    Glide.with(App.getInstance().getApplicationContext())
                                            .load(Constants.baseImgUrl + detailResponse.user.headImg)
                                            .error(R.mipmap.default_person_icon)
                                            .into(headImg);
                                }
                                if (detailResponse.user.concernStatus == 0) {
                                    follow.setText("已关注");
                                } else {
                                    follow.setText("+ 关注");
                                }
                                followStatus = detailResponse.user.concernStatus;
                                mAdapter.setAuthor(detailResponse.user.userName + "");
                            }
                            setData(true, detailResponse.comments);
                            if (sendSuccess) {
                                recyclerView.scrollToPosition(1);
                                showToast("发送成功");
                                sendSuccess = false;
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<ColumnDetailResponse>> response) {
                        Utils.errorResponse(mContext, response);
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                        mAdapter.setEnableLoadMore(true);
                        //可能需要移除之前添加的布局
                        mAdapter.removeAllFooterView();
                        //最后调用结束刷新的方法
                        setRefreshing(false);
                    }
                });
    }

    //初始化喊单数据
    private void setDataToTopView(ColumnDetailResponse detailResponse) {
//        if (!TextUtils.isEmpty(detailResponse.column.callSingle) && detailResponse.column.callSingle.equals("1")) {
        hdLayout.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(detailResponse.column.currencyPair)) {
            addItemToFloatLayout(floatLayout, SpannableStringUtils.getBuilder("货币对 ")
                    .setForegroundColor(getResources().getColor(R.color.text_gray))
                    .append(detailResponse.column.currencyPair)
                    .setForegroundColor(getResources().getColor(R.color.text_normal))
                    .create());
        }
        if (!TextUtils.isEmpty(detailResponse.column.cycleStart) && !TextUtils.isEmpty(detailResponse.column.cycleEnd)) {
            addItemToFloatLayout(floatLayout, SpannableStringUtils.getBuilder("交易时间 ")
                    .setForegroundColor(getResources().getColor(R.color.text_gray))
                    .append(TimeUtil.convertToDifftime(TimeUtil.FORMAT_TIME_MM_dd, TimeUtil.covertToLong(TimeUtil.FORMAT_TIME_EN, detailResponse.column.cycleStart))
                            + " - " + TimeUtil.convertToDifftime(TimeUtil.FORMAT_TIME_MM_dd, TimeUtil.covertToLong(TimeUtil.FORMAT_TIME_EN, detailResponse.column.cycleEnd)))
                    .setForegroundColor(getResources().getColor(R.color.text_normal))
                    .create());
        }
        if (!TextUtils.isEmpty(detailResponse.column.tactful)) {
            addItemToFloatLayout(floatLayout, SpannableStringUtils.getBuilder("交易策略 ")
                    .setForegroundColor(getResources().getColor(R.color.text_gray))
                    .append(detailResponse.column.tactful)
                    .setForegroundColor(getResources().getColor(R.color.blue))
                    .create());
        }
        if (detailResponse.column.openPosition != 0) {
            addItemToFloatLayout(floatLayout, SpannableStringUtils.getBuilder("开仓点位 ")
                    .setForegroundColor(getResources().getColor(R.color.text_gray))
                    .append(detailResponse.column.openPosition + "")
                    .setForegroundColor(getResources().getColor(R.color.text_normal))
                    .create());
        }
        if (detailResponse.column.stopLoss != 0) {
            addItemToFloatLayout(floatLayout, SpannableStringUtils.getBuilder("止损点位 ")
                    .setForegroundColor(getResources().getColor(R.color.text_gray))
                    .append(detailResponse.column.stopLoss + "")
                    .setForegroundColor(getResources().getColor(R.color.red))
                    .create());
        }
        if (detailResponse.column.takeprofit != 0) {
            addItemToFloatLayout(floatLayout, SpannableStringUtils.getBuilder("止盈点位 ")
                    .setForegroundColor(getResources().getColor(R.color.text_gray))
                    .append(detailResponse.column.takeprofit + "")
                    .setForegroundColor(getResources().getColor(R.color.green))
                    .create());
        }
//        } else {
//            hdLayout.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onLoadMoreRequested() {
        OkGo.<BaseResponse<List<CommentInfo>>>get(Constants.baseDataUrl + "/comment/list?cid=" + columnId + "&limit=" + Constants.PAGE_SIZE + "&page=" + currentPage)
                .cacheMode(CacheMode.NO_CACHE)       //上拉不需要缓存
                .execute(new MJsonCallBack<BaseResponse<List<CommentInfo>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<List<CommentInfo>>> response) {
                        List<CommentInfo> results = response.body().datas;
                        setData(false, results);
                    }

                    @Override
                    public void onError(Response<BaseResponse<List<CommentInfo>>> response) {
                        //显示数据加载失败,点击重试
                        mAdapter.loadMoreFail();
                        Utils.errorResponse(mContext, response);
                    }
                });
    }

    private void setData(boolean isRefresh, List data) {
        currentPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (isRefresh) {
            if (size < 8) {
                mAdapter.loadMoreEnd(isRefresh);
            } else {
                mAdapter.loadMoreComplete();
            }
        } else {
            if (size < Constants.PAGE_SIZE) {
                //第一页如果不够一页就不显示没有更多数据布局
                mAdapter.loadMoreEnd(isRefresh);
            } else {
                mAdapter.loadMoreComplete();
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

    @OnClick({R.id.discuss, R.id.collection, R.id.share, R.id.send,
            R.id.btn_back, R.id.click_close_kb})
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
                if (SPUtil.getBoolean(Constants.IS_LOGIN, false)) {
                    collect();
                } else {
                    startActivity(LoginActivity.class, false);
                }
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

    private void collect() {
        showDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("cid", columnId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<BaseResponse>post(Constants.baseDataUrl + "/column/collect")
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        if (response.body().code == 0) {
                            if (collectStatus == 1) {
                                collectStatus = 0;
                                collection.setImageResource(R.mipmap.column_btn_collection_s);
                            } else if (collectStatus == 0) {
                                collectStatus = 1;
                                collection.setImageResource(R.mipmap.column_btn_collection);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        Utils.errorResponse(mContext, response);
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                    }
                });
    }

    private void opinion(final String s) {
        showDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("cid", columnId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<BaseResponse>post(Constants.baseDataUrl + "/column/" + s)
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        if (response.body().code == 0) {
                            if (TextUtils.isEmpty(viewStatus)) {
                                if (s.equals("oppose")) {
                                    Drawable drawable = getResources().getDrawable(R.mipmap.zan_down_s);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    zanDown.setCompoundDrawables(drawable, null, null, null);
                                    zanDown.setText(Integer.parseInt(zanDown.getText().toString()) + 1 + "");
                                    zanDown.setTextColor(getResources().getColor(R.color.orange));
                                    viewStatus = "0";
                                } else {
                                    Drawable drawable = getResources().getDrawable(R.mipmap.zan_up_s);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    zanUp.setCompoundDrawables(drawable, null, null, null);
                                    zanUp.setText(Integer.parseInt(zanUp.getText().toString()) + 1 + "");
                                    zanUp.setTextColor(getResources().getColor(R.color.orange));
                                    viewStatus = "1";
                                }
                            } else {
                                if (viewStatus.equals("1")) {
                                    Drawable drawable = getResources().getDrawable(R.mipmap.zan_up);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    zanUp.setCompoundDrawables(drawable, null, null, null);
                                    zanUp.setText(Integer.parseInt(zanUp.getText().toString()) - 1 + "");
                                    zanUp.setTextColor(getResources().getColor(R.color.text_gray));
                                    viewStatus = "";
                                    if (s.equals("oppose")) {
                                        Drawable drawable1 = getResources().getDrawable(R.mipmap.zan_down_s);
                                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                                        zanDown.setCompoundDrawables(drawable1, null, null, null);
                                        zanDown.setText(Integer.parseInt(zanDown.getText().toString()) + 1 + "");
                                        zanDown.setTextColor(getResources().getColor(R.color.orange));
                                        viewStatus = "0";
                                    }
                                } else if (viewStatus.equals("0")) {
                                    Drawable drawable = getResources().getDrawable(R.mipmap.zan_down);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    zanDown.setCompoundDrawables(drawable, null, null, null);
                                    zanDown.setText(Integer.parseInt(zanDown.getText().toString()) - 1 + "");
                                    zanDown.setTextColor(getResources().getColor(R.color.text_gray));
                                    viewStatus = "";
                                    if (s.equals("support")) {
                                        Drawable drawable1 = getResources().getDrawable(R.mipmap.zan_up_s);
                                        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                                        zanUp.setCompoundDrawables(drawable1, null, null, null);
                                        zanUp.setText(Integer.parseInt(zanUp.getText().toString()) + 1 + "");
                                        zanUp.setTextColor(getResources().getColor(R.color.orange));
                                        viewStatus = "1";
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        Utils.errorResponse(mContext, response);
                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();
                    }
                });
    }

    private void follow(String s) {
        showDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("toid", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (s.equals("cancel")) {
            OkGo.<BaseResponse>put(Constants.baseDataUrl + "/concern/" + s)
                    .upJson(obj)
                    .execute(new MJsonCallBack<BaseResponse>() {
                        @Override
                        public void onSuccess(Response<BaseResponse> response) {
                            if (response.body().code == 0) {
                                if (followStatus == 0) {
                                    followStatus = 1;
                                    follow.setText("+ 关注");
                                } else if (followStatus == 1) {
                                    followStatus = 0;
                                    follow.setText("已关注");
                                }
                            }
                        }

                        @Override
                        public void onError(Response<BaseResponse> response) {
                            Utils.errorResponse(mContext, response);
                        }

                        @Override
                        public void onFinish() {
                            dismissDialog();
                        }
                    });
        } else {
            OkGo.<BaseResponse>post(Constants.baseDataUrl + "/concern/" + s)
                    .upJson(obj)
                    .execute(new MJsonCallBack<BaseResponse>() {
                        @Override
                        public void onSuccess(Response<BaseResponse> response) {
                            if (response.body().code == 0) {
                                if (followStatus == 0) {
                                    followStatus = 1;
                                    follow.setText("+ 关注");
                                } else if (followStatus == 1) {
                                    followStatus = 0;
                                    follow.setText("已关注");
                                }
                            }
                        }

                        @Override
                        public void onError(Response<BaseResponse> response) {
                            Utils.errorResponse(mContext, response);
                        }

                        @Override
                        public void onFinish() {
                            dismissDialog();
                        }
                    });
        }
    }

    /**
     * 发布评论
     */
    public void sendComment() {
        final String content = etDiscuss.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("评论内容不能为空");
            return;
        }
        showDialog("发送中...");
        JSONObject obj = new JSONObject();
        try {
            obj.put("columnId", columnId);
            obj.put("content", content);
            if (mAdapter.getClickInfo() != null) {
                obj.put("type", "2");
                obj.put("toCustomerId", mAdapter.getClickInfo().fromUserId);
                obj.put("toCommentId", mAdapter.getClickInfo().commentId);
                obj.put("groupId", mAdapter.getClickInfo().commentId);
            } else {
                obj.put("type", "1");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        etDiscuss.setText("");
        KeyboardUtil.closeKeyboard(etDiscuss, ColumnDetailActivity.this);
        OkGo.<BaseResponse>post(Constants.baseDataUrl + "/comment/insert")
                .upJson(obj)
                .execute(new MJsonCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        sendSuccess = true;
                        onRefresh();
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        dismissDialog();
                        Utils.errorResponse(mContext, response);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.zoom_finish);
    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(ColumnDetailActivity.this).clearDiskCache();//清理磁盘缓存需要在子线程中执行
            }
        }).start();
        Glide.get(this).clearMemory();//清理内存缓存可以在UI主线程中进行
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //键盘打开关闭监听器
    private void initKeyboardChangeListener() {
        new KeyboardUtil(this).setKeyBoardListener(new KeyboardUtil.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    if (mAdapter.getClickInfo() != null && !TextUtils.isEmpty(mAdapter.getClickInfo().fromUserName)) {
                        etDiscuss.setHint("回复 " + mAdapter.getClickInfo().fromUserName);
                    }
                    rl2.setVisibility(View.VISIBLE);
                    rl1.setVisibility(View.GONE);
                    clickCloseKb.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setClickInfo(null);
                    etDiscuss.setHint("请您发表评论");
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
                    mAdapter.setClickInfo(model);
                } else if (listItems[i].equals("删除")) {
                    OkGo.<BaseResponse>delete(Constants.baseDataUrl + "/comment/delete/" + model.commentId)
                            .execute(new MJsonCallBack<BaseResponse>() {
                                @Override
                                public void onSuccess(Response<BaseResponse> response) {
                                    BaseResponse r = response.body();
                                    if (r.code == 0) {
                                        mAdapter.remove(p);
                                        if (p == 0) {
                                            commentsCount.setText("暂无评论");
                                        }
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

}
