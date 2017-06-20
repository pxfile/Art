package com.bobaoo.xiaobao.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.IdentifyMeetPayResponse;
import com.bobaoo.xiaobao.domain.UserPayData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by you on 2015/6/12.
 */
public class BaifuBaoPayActivity extends BaseActivity{
    private WebView mWebView;
    private String mUserId;
    private double mPrice;
    private String mGoodsId;
    private String mPayMethod;
    private String mCashCouponId;
    private boolean mPayFlg;

    private boolean mIsIdentifyMeet;

    private ProgressDialog mBaifubaoProgressDialog;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userid");
        mPrice = intent.getDoubleExtra("amount", 0);
        mGoodsId = intent.getStringExtra("goodsid");
        mPayMethod = intent.getStringExtra("paymethod");
        mCashCouponId = intent.getStringExtra("CashCouponId");
        mPayFlg = intent.getBooleanExtra("payflg", false);
        mIsIdentifyMeet = intent.getBooleanExtra("isidentifymeet", false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_baifubaopay_layout;
    }

    @Override
    protected void initTitle() {

    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initContent() {
        mWebView = (WebView) findViewById(R.id.wb_baifubao_pay);
        Log.e("UserRecharge","加载充值页面");
        String BaseUrl = "http://user.artxun.com/mobile/finance/gateway.jsp?app=com.bobaoo.xiaobao&gateway=baifubao&version=2";
        BaseUrl = StringUtils.getString(BaseUrl,"&uid=",mUserId);
        BaseUrl = StringUtils.getString(BaseUrl,"&amount=",mPrice);
        Log.e("UserPay","BaseUrl == "+BaseUrl);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this,"history");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("success") && url.startsWith("http://user.artxun.com/mobile/finance/")) {
                    Log.e("UserPay", "充值成功");
                    UmengUtils.onEvent(mContext, EventEnum.User_Bfb_Recharge_Success);
                    if (mPayFlg) {
                        Log.e("UserPay", "开始扣费请求");
                        if(!mIsIdentifyMeet){
                            startUserPayRequest(mGoodsId, mPayMethod, mCashCouponId);
                        }else {
                            //鉴定会支付请求
                            startIdentifyMeetPayRequest(mGoodsId, mCashCouponId);
                        }

                    } else {
                        //充值成功，跳转充值记录页面
                        Intent intent = new Intent(BaifuBaoPayActivity.this, UserRechargeRecordActivity.class);
                        intent.putExtra(IntentConstant.USER_ID, mUserId);
                        startActivity(intent);
                        //充值送积分
                        new HttpUtils().send(
                                HttpRequest.HttpMethod.GET,
                                NetConstant.HOST,
                                NetConstant.getRechargeScoreParams(mContext, StringUtils.getString(mPrice)), null);
                        finish();
                    }
                    return false;
                } else {
                    Log.e("UserPay", "充值失败 url=" + url);
                    UmengUtils.onEvent(mContext,EventEnum.User_Bfb_ReCharge_Failed);
                    view.loadUrl(url);
                    return true;
                }
            }
        });
        mWebView.loadUrl(BaseUrl);
//        setContentView(mWebView);
    }

    private void startUserPayRequest(String goodsId, String payMethodFlg, String cashCouponId) {
        //扣费请求
        new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getPayParams(mContext, goodsId,
                payMethodFlg, cashCouponId), new UserPayListener());
    }

    private void startIdentifyMeetPayRequest(String goodsId, String cashCouponId) {
        //鉴定会支付请求
        if(mBaifubaoProgressDialog == null) {
            mBaifubaoProgressDialog = DialogUtils.showProgressDialog(mContext, "正在百付宝支付");
        }
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(
                HttpRequest.HttpMethod.GET,
                NetConstant.HOST,
                NetConstant.getIdentifyMeetPayParams(mContext, goodsId, cashCouponId),
                new IdentifyMeetBalancePayListener());

    }

    //鉴定会百付宝支付回调
    private class IdentifyMeetBalancePayListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<IdentifyMeetPayResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<IdentifyMeetPayResponse> task = new StringToBeanTask<>(IdentifyMeetPayResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mBaifubaoProgressDialog != null) {
                mBaifubaoProgressDialog.dismiss();
            }
            Map<String,String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
        }

        @Override
        public void onConvertSuccess(IdentifyMeetPayResponse data) {
            if (mBaifubaoProgressDialog != null) {
                mBaifubaoProgressDialog.dismiss();
            }
            if (data != null) {
                if (!data.isError()) {
                    Map<String,String> payFailedMap = new HashMap<>();
                    payFailedMap.put(UmengConstants.KEY_USER_PAY_SUCCESS, data.getData().toString());
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Success, payFailedMap);
                    // 鉴定会支付成功弹窗
                    showIdentifyMeetPaySucessDialog(data);
                }else {
                    showIdentifyMeetPayWarningDialog(data);
                }
            }
        }

        @Override
        public void onConvertFailed() {
            if (mBaifubaoProgressDialog != null) {
                mBaifubaoProgressDialog.dismiss();
            }
        }
    }

    private void showIdentifyMeetPayWarningDialog(IdentifyMeetPayResponse data) {
        DialogUtils.showWarnDialog(mContext, "支付信息", data.getMessage(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //鉴定会未支付页面跳转
                Intent intent = new Intent(mContext, UserIdentifyMeetingActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
                mContext.startActivity(intent);
                finish();
            }
        });
    }

    private void showIdentifyMeetPaySucessDialog(IdentifyMeetPayResponse data){
        DialogUtils.showIdentifyMeetPaySuccessDialog(mContext, data, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //鉴定会支付成功,跳转到鉴定会页面
                Intent intent = new Intent(mContext, UserIdentifyMeetingActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX,IntentConstant.IDENTIFY_PAGE_INDEX_NO_IDENTIFY);
                mContext.startActivity(intent);
                finish();
            }
        });
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

    private class UserPayListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserPayData> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserPayData> task = new StringToBeanTask<>(UserPayData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(BaifuBaoPayActivity.this, "失败", Toast.LENGTH_SHORT).show();
            finish();
            Log.e("UserPay", "扣费失败");
            Map<String,String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
        }

        @Override
        public void onConvertSuccess(UserPayData data) {
            if (data != null) {
                if (!data.getError()) {
                    Log.e("UserPay", "扣费成功！");
                    Intent intent = new Intent(mContext, PaySuccessActivity.class);
                    startActivity(intent);
                    finish();
                    Map<String,String> payFailedMap = new HashMap<>();
                    payFailedMap.put(UmengConstants.KEY_USER_PAY_SUCCESS, data.getData().toString());
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Success, payFailedMap);
                } else {
                    Log.e("UserPay", data.getData() + "");
                }
            } else {
                Log.e("UserPay", "data == null");
            }
            DialogUtils.showShortPromptToast(mContext,data.getData());
            finish();
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext,R.string.failed);
            finish();
            Log.e("UserPay", "扣费失败");
        }
    }
}
