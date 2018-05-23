package com.xindu.talkfx_new.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/5/16.
 */

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        title.setText(getIntent().getStringExtra("title"));
        WebSettings websetting = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        websetting.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        websetting.setJavaScriptCanOpenWindowsAutomatically(true);
        /*支持内容重新布局*/
        websetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        /** 支持自动加载图片 */
        websetting.setLoadsImagesAutomatically(true);
        /*默认是true 如果不改变会阻塞页面。下载不出图片*/
        websetting.setBlockNetworkImage(false);
        /*开启DOM storage API功能（HTML5 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。*/
        websetting.setDomStorageEnabled(true);
        //开启 database storage API 功能
        websetting.setDatabaseEnabled(true);
        /*支持通过JS打开新窗口*/
        //websetting.setJavaScriptCanOpenWindowsAutomatically(true);
        /*设置默认编码*/
        websetting.setDefaultTextEncodingName("utf-8");
        /** 支持缩放 */
//        websetting.setSupportZoom(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        websetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        /** 将图片调整到适应WebView大小 */
        websetting.setUseWideViewPort(true);

        /** 缩放至屏幕大小 */
        websetting.setLoadWithOverviewMode(true);
        webView.loadUrl("http://192.168.1.191:3000/markets/forex/andriod?symbol=" + getIntent().getStringExtra("curr") + "&name=" + getIntent().getStringExtra("title"));
//        webView.loadUrl("http://beta.talkfx.com/markets/forex/" + getIntent().getStringExtra("title"));
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
