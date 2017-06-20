package com.bobaoo.xiaobao.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

public class PayActivity extends Activity {
    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_pay);

        Button wx = (Button) findViewById(R.id.wx_send);
        wx.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                getWxParameter();
            }
        });


        Button bai = (Button) findViewById(R.id.bai_send);
        bai.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                doBaifubao();
            }
        });


    }

    protected void getWxParameter() {
        final WebView nbView = new WebView(this);
        nbView.getSettings().getUserAgentString();
        //870434
        //796991
        String BaseUrl =
                "http://user.artxun.com/mobile/finance/gateway.jsp?app=com.bobaoo.xiaobao&uid=796991&amount=0.01&gateway=wxpay&version=2&name=xxxe";

        nbView.loadUrl(BaseUrl);

        nbView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("wxpay:")) {
                    doWxpayRequest(url);
                }
                return false;
            }
        });

    }

    //∞Ÿ∏∂±¶
    @SuppressLint("JavascriptInterface")
    protected void doBaifubao() {
        final WebView nbView = new WebView(this);

        String BaseUrl =
                "http://user.artxun.com/mobile/finance/gateway.jsp?app=com.bobaoo.xiaobao&uid=796991&amount=0.01&gateway=baifubao&version=2&name=xxxe";

        nbView.getSettings().setJavaScriptEnabled(true);
        nbView.addJavascriptInterface(this, "history");

        nbView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.indexOf("success") > -1 && url.startsWith("http://user.artxun.com/mobile/finance/")) {
                    return false;
                } else {
                    view.loadUrl(url);
                    return true;
                }

            }
        });


        nbView.loadUrl(BaseUrl);

        setContentView(nbView);

    }

    public void go(int type) {
        finish();
    }

    //Œ¢–≈÷ß∏∂
    public void doWxpayRequest(String url) {
        HashMap<String, String> result = new HashMap<>();
        String[] arr = url.substring(9).split("&");
        for (int i = 0; i < arr.length; i++) {
            int idx = arr[i].indexOf('=');
            if (idx == -1) continue;
            result.put(arr[i].substring(0, idx), arr[i].substring(idx + 1));
        }

        IWXAPI wxApi = WXAPIFactory.createWXAPI(this, null);
        wxApi.registerApp(result.get("appid"));

        if (wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI()) {
            PayReq pay = new PayReq();
            pay.appId = result.get("appid");
            pay.partnerId = result.get("mch_id");
            pay.prepayId = result.get("prepay_id");
            pay.timeStamp = result.get("timestamp");
            pay.packageValue = "prepay_id=" + result.get("prepay_id");
            pay.nonceStr = result.get("nonce_str");
            pay.sign = result.get("sign");

//            Toast.makeText(mContext, "pay", Toast.LENGTH_SHORT).show();
            wxApi.sendReq(pay);
        } else {
            Toast.makeText(mContext, R.string.request_install_wx, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }
}
