package com.bobaoo.xiaobao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.manager.AliPayManager;
import com.bobaoo.xiaobao.manager.WXDealManager;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * Created by you on 2015/6/12.
 */
public class UserRechargeActivity extends BaseActivity{
    private View mWxRechargeRl;
    private View mBaifubaoRechargeRl;
    private View mAlipayRechargeRl;
    private TextView mRechargePrice;
    private String mUserId;
    private String mRechargeValue;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if(intent != null){
            mUserId = intent.getStringExtra(IntentConstant.USER_ID);
            mRechargeValue = intent.getStringExtra(IntentConstant.USER_RECHARG_VALUE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_recharge;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.user_recharge);
        setOnClickListener(backView);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initContent() {
        mWxRechargeRl = findViewById(R.id.rl_recharge_wx);
        mBaifubaoRechargeRl = findViewById(R.id.rl_recharge_bfb);
        mAlipayRechargeRl = findViewById(R.id.rl_recharge_zfb);
        mRechargePrice = (TextView) findViewById(R.id.tv_recharge_price);
        mRechargePrice.setText(mRechargeValue);
        setOnClickListener(mWxRechargeRl, mBaifubaoRechargeRl, mAlipayRechargeRl);
    }

    @Override
    public void onClick(View view) {
        HashMap<String,String> map = new HashMap<>();
        double rechargePay = 0.0d;
        try {
            rechargePay = Double.parseDouble(mRechargeValue);
        }catch (NumberFormatException e){
            Toast.makeText(UserRechargeActivity.this,"金额有误，请重新设置",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()){
            case R.id.rl_recharge_wx:
                WXDealManager.getInstance().clearWXDealInfo();
                WXDealManager.getInstance().setmUserId(mUserId);
                WXDealManager.getInstance().setWXCashAction(WXDealManager.WXCashAction.Recharge);
                WXDealManager.getInstance().setRechargeAmount(StringUtils.getString(rechargePay));
                getWxParameter(rechargePay);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(this, EventEnum.UserRechargePageWXRecharge, map);
                break;
            case R.id.rl_recharge_bfb:
                doBaifubao(rechargePay);//百付宝充值
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(this, EventEnum.UserRechargePageBFBRecharge, map);
                finish();
                break;
            case R.id.rl_recharge_zfb:
                AliPayManager.getsInstance().doAliPayRecharge(mContext, mUserId, mRechargeValue);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID,mUserId);
                UmengUtils.onEvent(this, EventEnum.UserRechargePageAliPayRecharge, map);
                finish();
                break;
            default:break;
        }
    }

    /**
     * 百付宝充值
     */
    @SuppressLint("JavascriptInterface")
    protected void doBaifubao(double amount)
    {
        Intent intent = new Intent(this,BaifuBaoPayActivity.class);
        intent.putExtra("userid",mUserId);
        intent.putExtra("CashCouponId","");
        intent.putExtra("amount", amount);
        intent.putExtra("payflg",false);
        startActivity(intent);
    }

    /**
     * 微信支付
     */
    protected void getWxParameter(double rechargPay) {
        final WebView nbView = new WebView(this);
        nbView.getSettings().getUserAgentString();

        String BaseUrl =
                "http://user.artxun.com/mobile/finance/gateway.jsp?app=com.bobaoo.xiaobao&gateway=wxpay&version=2&name=xxxe";
        BaseUrl = StringUtils.getString(BaseUrl, "&uid=", mUserId);
        BaseUrl = StringUtils.getString(BaseUrl,"&amount=", rechargPay+"");
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

        if(wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI()){
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
            finish();
        }else{
            Toast.makeText(mContext, R.string.request_install_wx, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {

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
