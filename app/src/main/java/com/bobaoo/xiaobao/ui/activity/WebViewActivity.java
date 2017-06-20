package com.bobaoo.xiaobao.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;

/**
 * Created by kakaxicm on 2015/12/8.
 */
public class WebViewActivity extends BaseActivity {
    private String mUrl;
    private String mTitle;
    private WebView mWebView;

    @Override
    protected void getIntentData() {
        mUrl = getIntent().getStringExtra(IntentConstant.WEB_URL);
        mTitle = getIntent().getStringExtra(IntentConstant.WEB_TITLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activiy_webview;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(mTitle);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mWebView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
