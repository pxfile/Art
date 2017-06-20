package com.bobaoo.xiaobao.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.CashCoupon;
import com.bobaoo.xiaobao.domain.IdentifyMeetPayResponse;
import com.bobaoo.xiaobao.domain.IdentifyMeetWXPayStatusResponse;
import com.bobaoo.xiaobao.domain.UserIdentifyMeetPayInfo;
import com.bobaoo.xiaobao.domain.UserIdentifyPayInfo;
import com.bobaoo.xiaobao.domain.UserPayData;
import com.bobaoo.xiaobao.domain.WXPayChargeResponse;
import com.bobaoo.xiaobao.domain.WXPayStatus;
import com.bobaoo.xiaobao.manager.AliPayManager;
import com.bobaoo.xiaobao.manager.WXDealManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.EditInfoDialog;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.BigDecimalUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zcw.togglebutton.ToggleButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by you on 2015/6/5.
 */
public class UserPayActivity extends BaseActivity implements EditInfoDialog.OnConfirmListener {
    private String mIdentifyId;
    private String mUserId;
    private double mGoodsPrice;//鉴定的原价
    private double mPayPrice;//可能扣除积分的价格
    private double mIntegerAmount;//积分抵价
    private double mUserAmount;//用户账户余额
    private double mCashCouponAmount;//现金券金额
    private String mGoodsId;//如果是鉴定会，goodsid是鉴定会报名id
    private String mIdentifyType;
    private String mCashCouponId;

    final private String USE_BALANCE_PAY = "0";
    final private String USE_WX_PAY = "1";
    final private String USE_BFB_PAY = "2";
    final private String USE_BALANCE_METHOD = "4";
    final private String NOT_USE_BALANCE_METHOD = "2";
    private String mPayMethod = NOT_USE_BALANCE_METHOD;

    private String mIdentifyMeetSignId;//鉴定会报名Id
    private boolean mIsIdentifyMeet;//是否是鉴定会的标记

    private TextView mIdentifyTypeTV;//鉴定类型
    private TextView mPriceTV;//商品价格
    private TextView mCashCouponView;
    private TextView mUserAccountTV;
    private View mCreditContainer;
    private TextView mIntegralIntroTV;
    private ToggleButton mUserIntegerCB;
    private TextView mPayPriceTV;
    private RadioButton mBalancePayBtn;
    private RadioButton mWeChatPayBtn;
    private RadioButton mBfbPayBtn;
    private RadioButton mAliPayBtn;
    private Button mUserPayTo;
    private View mRlAliPay;
    private View mRlBalancePay;
    private View mRlWeChatPay;
    private View mRlBfbPay;

    private ProgressDialog mGetWxPayStateProgressDialog;//获取微信支付状态进度框
    private ProgressDialog mGetIdentifyMeetWXPayResultProgressDialog;//获取鉴定会支付状态进度框
    private ProgressDialog mGetChargeProgressDialog;//生成支付订单进度框
    private ProgressDialog mGetPayInfoProgressDialog;//获取支付页面数据进度框

    private ProgressDialog mBobaoProgressDialog;
    private boolean mIsWeixinPaySuccess;//微信是否支付成功

    private DialogInterface.OnClickListener mConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_CancelPay_Onclick);
            Intent intent = null;
            if (mIsIdentifyMeet){
                //取消支付,跳转到用户鉴定会列表页面
                intent = new Intent(mContext, UserIdentifyMeetingActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
                mContext.startActivity(intent);
            }else{
                intent = new Intent(mContext, MainActivity.class);
                jump(intent);
            }
            finish();
        }
    };

    @Override
    protected void getIntentData() {
        mIdentifyId = getIntent().getStringExtra(IntentConstant.USER_PAY_GOODS_ID);
        mIdentifyMeetSignId = getIntent().getStringExtra(IntentConstant.IdentifyMeetingId);
        mIsIdentifyMeet = !TextUtils.isEmpty(mIdentifyMeetSignId);//mIdentifyMeetSignId不为空, 则为鉴定会页面
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_identify_payinfo;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.pay_order);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        View couponView = findViewById(R.id.rl_coupon);
        mIdentifyTypeTV = (TextView) findViewById(R.id.user_identify_type);//鉴定类型
        mPriceTV = (TextView) findViewById(R.id.user_identify_price);//鉴定价格
        mIntegralIntroTV = (TextView) findViewById(R.id.user_choice_integral);//积分说明
        mCashCouponView = (TextView) findViewById(R.id.tv_cash_coupon);
        mUserAccountTV = (TextView) findViewById(R.id.use_account_available);//账户余额
        mUserIntegerCB = (ToggleButton) findViewById(R.id.use_score_pay_checkbox);//使用积分
        mBalancePayBtn = (RadioButton) findViewById(R.id.user_balance_pay_radioBTN);
        mWeChatPayBtn = (RadioButton) findViewById(R.id.user_wx_pay_radioBTN);
        mBfbPayBtn = (RadioButton) findViewById(R.id.user_bfb_pay_radioBTN);
        mAliPayBtn = (RadioButton) findViewById(R.id.user_zfb_pay_radioBTN);
        mPayPriceTV = (TextView) findViewById(R.id.user_actual_pay);
        mUserPayTo = (Button) findViewById(R.id.user_pay_to);
        mRlAliPay = findViewById(R.id.rl_AliPay);
        mRlBalancePay = findViewById(R.id.rl_balance_pay);
        mRlBfbPay = findViewById(R.id.rl_bfb_pay);
        mRlWeChatPay = findViewById(R.id.rl_wechat_pay);

        mCreditContainer = findViewById(R.id.rl_credit);
        //鉴定会UI处理
        if(mIsIdentifyMeet) {
            mCreditContainer.setVisibility(View.GONE);//鉴定会不支持积分支付
        }else {
            mCreditContainer.setVisibility(View.VISIBLE);//鉴定会不支持积分支付
            //默认勾选积分
            mUserIntegerCB.setToggleOn();
        }
        setOnClickListener(couponView, mUserPayTo, mRlAliPay, mRlBalancePay, mRlBfbPay, mRlWeChatPay);
        setSwipeBackEnable(false);
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
        if(mGetPayInfoProgressDialog == null) {
            mGetPayInfoProgressDialog = DialogUtils.showProgressDialog(mContext,getString(R.string.dialog_loading));
        }
        if(!mIsIdentifyMeet) {
            //获取鉴定订单的支付信息
            new HttpUtils().configCurrentHttpCacheExpiry(0)
                    .send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getUserPayParams(mContext, mIdentifyId),
                            new UserPayListener());
        }else {
            //获取鉴定会的支付信息
            new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                    NetConstant.getIdentifyMeetPayInfoParams(mContext, mIdentifyMeetSignId), new UserIdentifyMeetListener());
        }
    }

    @Override
    protected void refreshData() {
    }

    private void checkedPayMethod(RadioButton radioButton, final boolean Alipay, final boolean balance, final boolean WeChat,
                                  final boolean bfb) {
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAliPayBtn.setChecked(Alipay);
                    mBalancePayBtn.setChecked(balance);
                    mWeChatPayBtn.setChecked(WeChat);
                    mBfbPayBtn.setChecked(bfb);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkedPayMethod(mAliPayBtn, true, false, false, false);
        checkedPayMethod(mBfbPayBtn, false, false, false, true);
        checkedPayMethod(mBalancePayBtn, false, true, false, false);
        checkedPayMethod(mWeChatPayBtn, false, false, true, false);
        UmengUtils.onResume(this);
        //查询微信支付接口
        if(mWeChatPayBtn.isChecked()){

            if(!mIsIdentifyMeet){
                if(mGetWxPayStateProgressDialog == null) {
                    mGetWxPayStateProgressDialog = DialogUtils.showProgressDialog(mContext, "获取支付信息");
                }
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getQureyPaySatusParams(mContext,mGoodsId), new WXPayStatusListener());
            }else {
                //请求鉴定的支付结果查询
                if(mGetIdentifyMeetWXPayResultProgressDialog == null) {
                    mGetIdentifyMeetWXPayResultProgressDialog = DialogUtils.showProgressDialog(mContext, "获取鉴定会支付信息");
                }
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getIdentifyMeetPayResultParams(mContext, mGoodsId), new IdentifyMeetWXPayResultListener());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                if (mIsIdentifyMeet){
                    DialogUtils.showPromtAlertDialog(this,null,-1, AppConstant.PAY_CANCEL,true);
                }else{
                    DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.user_pay_quit, mConfirmListener);
                }
                break;
            case R.id.rl_coupon:
                DialogUtils.showEditDialog(mContext, this, R.string.input_cash_coupon, 0);
                UmengUtils.onEvent(mContext, EventEnum.User_Pay_Coupon);
                break;
            case R.id.user_pay_to:
                doPay();
                break;
            case R.id.rl_AliPay:
                mAliPayBtn.setChecked(true);
                break;
            case R.id.rl_balance_pay:
                mBalancePayBtn.setChecked(true);
                break;
            case R.id.rl_wechat_pay:
                mWeChatPayBtn.setChecked(true);
                break;
            case R.id.rl_bfb_pay:
                mBfbPayBtn.setChecked(true);
                break;
            default:
                break;
        }
    }

    private void doPay() {
        if (mBalancePayBtn.isChecked()) {
            if (mUserAmount < mPayPrice) {
                DialogUtils.showShortPromptToast(mContext, R.string.balance_not_enough);
                return;
            }
            UmengUtils.onEvent(mContext, EventEnum.BoBao_Pay);
            if(mIsIdentifyMeet) {
                //鉴定会支付接口
                if(mBobaoProgressDialog == null) {
                    mBobaoProgressDialog = DialogUtils.showProgressDialog(mContext, "正在余额支付");
                }
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(
                        HttpRequest.HttpMethod.GET,
                        NetConstant.HOST,
                        NetConstant.getIdentifyMeetPayParams(mContext, mGoodsId, mCashCouponId),
                        new IdentifyMeetBalancePayListener());

            }else {
                doBobaoPay(mGoodsId, mPayMethod);
            }
        }
        //百付宝支付
        if (mBfbPayBtn.isChecked()) {
            UmengUtils.onEvent(mContext, EventEnum.Bfb_Pay);
            if (mPayPrice < 0.000001d && USE_BALANCE_METHOD.equals(mPayMethod)) {
                doBobaoPay(mGoodsId, mPayMethod);
                return;
            }
            doBaifubao();
        }
        //支付宝支付
        if (mAliPayBtn.isChecked()) {
            UmengUtils.onEvent(UserPayActivity.this, EventEnum.Zfb_Pay);
            if (mPayPrice < 0.000001d && USE_BALANCE_METHOD.equals(mPayMethod)) {
                doBobaoPay(mGoodsId, mPayMethod);
                return;
            }
            AliPayManager.getsInstance().doAliPay(mContext, mUserId, StringUtils.getString(mPayPrice), mGoodsId, mPayMethod, mCashCouponId, mIsIdentifyMeet);
        }
        if (mWeChatPayBtn.isChecked()) {
            if (mPayPrice < 0.000001d && USE_BALANCE_METHOD.equals(mPayMethod)) {
                doBobaoPay(mGoodsId, mPayMethod);
                return;
            }
            WXDealManager.getInstance().clearWXDealInfo();
            WXDealManager.getInstance().setGoodsId(mGoodsId);
            WXDealManager.getInstance().setWXCashAction(WXDealManager.WXCashAction.PAY);
            WXDealManager.getInstance().setPayMethod(mPayMethod);
            WXDealManager.getInstance().setmCashCouponId(mCashCouponId);
            WXDealManager.getInstance().setIsIdentifyMeet(mIsIdentifyMeet);//TODO 是否是鉴定会支付
            //充值统计跟踪
            Map<String, String> map = new HashMap<>();
            map.put(UmengConstants.KEY_PAY_ORDERID, WXDealManager.getInstance().getGoodsId());
            UmengUtils.onEvent(this, EventEnum.User_Recharge_WX_Start, map);
            if(mGetChargeProgressDialog == null) {
                mGetChargeProgressDialog = DialogUtils.showProgressDialog(mContext, "正在支付");
            }
            getWxParameter();
            UmengUtils.onEvent(mContext, EventEnum.WX_Pay);
        }
    }

    //鉴定会微信支付查询结果回调
    private class IdentifyMeetWXPayResultListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<IdentifyMeetWXPayStatusResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<IdentifyMeetWXPayStatusResponse> task = new StringToBeanTask<>(IdentifyMeetWXPayStatusResponse.class, this);
            task.execute(responseInfo.result);
            Log.e("WXPayTest", "查询微信支付状态"+responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mGetIdentifyMeetWXPayResultProgressDialog != null) {
                mGetIdentifyMeetWXPayResultProgressDialog.dismiss();
            }
            Map<String,String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
            Log.e("WXPayTest", "查询微信支付状态失败:"+s);
        }

        @Override
        public void onConvertSuccess(IdentifyMeetWXPayStatusResponse response) {
            if (mGetIdentifyMeetWXPayResultProgressDialog != null) {
                mGetIdentifyMeetWXPayResultProgressDialog.dismiss();
            }
            if(response != null){
                if(!response.isError()) {
                    //如果微信没有回调，则走下面的补丁提示
                    if(TextUtils.equals("1",response.getData().getCharged())) {
                        //支付成功弹窗提示
                        showIdentifyMeetPaySucessDialog(response);
                        mIsWeixinPaySuccess = true;
                    }else {
                        if(TextUtils.equals("1",response.getData().getIsPay())) {//支付成功
                            //支付成功弹窗提示
                            showIdentifyMeetPaySucessDialog(response);
                            mIsWeixinPaySuccess = true;
                        }else {//未支付
                            DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),getString(R.string.dialog_pay_not_pay), mConfirmListener);
                        }
                    }
                }else {
                    DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),response.getMessage(), mConfirmListener);
                }
            }else {
                //数据为空, 提示检查网络设置
                DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_failed), "", null);
            }
        }

        @Override
        public void onConvertFailed() {
            if (mGetIdentifyMeetWXPayResultProgressDialog != null) {
                mGetIdentifyMeetWXPayResultProgressDialog.dismiss();
            }
            DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_failed), "", null);
        }
    }

    //余额支付回调
    private class IdentifyMeetBalancePayListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<IdentifyMeetPayResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<IdentifyMeetPayResponse> task = new StringToBeanTask<>(IdentifyMeetPayResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mBobaoProgressDialog != null) {
                mBobaoProgressDialog.dismiss();
            }
            Map<String,String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
        }

        @Override
        public void onConvertSuccess(IdentifyMeetPayResponse data) {
            if (mBobaoProgressDialog != null) {
                mBobaoProgressDialog.dismiss();
            }
            if (data != null) {
                if (!data.isError()) {
                    Map<String,String> payFailedMap = new HashMap<>();
                    payFailedMap.put(UmengConstants.KEY_USER_PAY_SUCCESS, data.getData().toString());
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Success, payFailedMap);
                    showIdentifyMeetPaySucessDialog(data);
                }else {
                    //警告对话框
                    showIdentifyMeetPayWarningDialog(data);
                }
            }
        }

        @Override
        public void onConvertFailed() {
            if (mBobaoProgressDialog != null) {
                mBobaoProgressDialog.dismiss();
            }
        }
    }

    //支付结果警告弹窗
    private void showIdentifyMeetPayWarningDialog(IdentifyMeetPayResponse data) {
        DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),data.getMessage(), mConfirmListener);
    }

    //鉴定会支付结果成功弹窗
    private void showIdentifyMeetPaySucessDialog(IdentifyMeetPayResponse data){
        DialogUtils.showIdentifyMeetPaySuccessDialog(mContext, data, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //鉴定会支付成功,跳转到鉴定会鉴定列表页面
                Intent intent = new Intent(mContext, UserIdentifyMeetingActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX,IntentConstant.IDENTIFY_PAGE_INDEX_NO_IDENTIFY);
                mContext.startActivity(intent);
                finish();
            }
        });
    }

    private void showIdentifyMeetPaySucessDialog(IdentifyMeetWXPayStatusResponse data){
        DialogUtils.showIdentifyMeetPaySuccessDialog(mContext, data, new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserIdentifyMeetingActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_IDENTIFY);
                mContext.startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 微信支付
     */
    protected void getWxParameter() {
        final Map<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_PAY_ORDERID, mGoodsId);
        UmengUtils.onEvent(mContext, EventEnum.User_WX_Request_Pramas_Start, map);
        final WebView nbView = new WebView(this);
        nbView.getSettings().getUserAgentString();

        String baseUrl = "http://jianbao.artxun.com/index.php?module=jbapp&act=api&api=pay&op=wx&app=com.bobaoo.xiaobao";
        baseUrl = StringUtils.getString(baseUrl, "&uid=", mUserId);
        baseUrl = StringUtils.getString(baseUrl, "&amount=", mPayPrice);
        baseUrl = StringUtils.getString(baseUrl, "&id=", mGoodsId);
        baseUrl = StringUtils.getString(baseUrl, "&jflag=", mPayMethod);
        //现金券
        baseUrl = StringUtils.getString(baseUrl, "&rid=", mCashCouponId);//现金券id
        if(mIsIdentifyMeet) {//鉴定会支付type=1
            baseUrl = StringUtils.getString(baseUrl, "&type=", "1");
        } else {
            baseUrl = StringUtils.getString(baseUrl, "&type=", "0");
        }
        Log.e("WXPayTest","开始请求微信支付参数"+baseUrl);
        new HttpUtils().send(HttpRequest.HttpMethod.GET, baseUrl,new WXPayRequestCallback());
    }

    //获取微信支付对象回调
    private class WXPayRequestCallback extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<WXPayChargeResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<WXPayChargeResponse> task = new StringToBeanTask<>(WXPayChargeResponse.class, this);
            task.execute(responseInfo.result);
            Log.e("WXPayTest", responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Log.e("WXPayTest", "onFailure");
            if(mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
        }

        @Override
        public void onConvertSuccess(WXPayChargeResponse response) {
            Log.e("WXPayTest", response.getData());
            doWxpayRequest(response.getData());
            if(mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
        }

        @Override
        public void onConvertFailed() {
            Log.e("WXPayTest", "onConvertFailed");
            if(mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
        }
    }
    public void doWxpayRequest(String url) {
        final Map<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_PAY_ORDERID, mGoodsId);

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
            UmengUtils.onEvent(mContext, EventEnum.User_WX_Invoked, map);
            wxApi.sendReq(pay);
        } else {
            Toast.makeText(mContext, R.string.request_install_wx, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 百付宝支付
     */
    protected void doBaifubao() {
        Intent intent = new Intent(this, BaifuBaoPayActivity.class);
        intent.putExtra("userid", mUserId);
        intent.putExtra("amount", mPayPrice);
        intent.putExtra("goodsid", mGoodsId);
        intent.putExtra("paymethod", mPayMethod);
        intent.putExtra("CashCouponId", mCashCouponId);
        intent.putExtra("payflg", true);
        intent.putExtra("isidentifymeet", mIsIdentifyMeet);//鉴定会标记
        startActivity(intent);
    }

    /**
     * 账户扣费请求
     */
    private void doBobaoPay(String goodsId, String payMethodFlg) {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                NetConstant.getPayParams(mContext, goodsId, payMethodFlg, mCashCouponId), new BobaoPayListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mWeChatPayBtn.isChecked() && mIsWeixinPaySuccess){//如果微信支付成功,则直接返回
                return super.onKeyDown(keyCode, event);
            }
            if (mIsIdentifyMeet){
                DialogUtils.showPromtAlertDialog(this,null,-1, AppConstant.PAY_CANCEL,true);
            }else{
                DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.user_pay_quit, mConfirmListener);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfirm(String text) {
        mCashCouponId = text;
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                NetConstant.getCashCouponParams(mContext, mIdentifyType, text), new CashCouponListener());
    }

    private class WXPayStatusListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<WXPayStatus> {


        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<WXPayStatus> task = new StringToBeanTask<>(WXPayStatus.class, this);
            task.execute(responseInfo.result);
            Log.e("WXPayTest", "查询微信支付状态"+responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if(mGetWxPayStateProgressDialog != null) {
                mGetWxPayStateProgressDialog.dismiss();
                mGetWxPayStateProgressDialog = null;
            }
            DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),"获取支付订单状态失败,请检查网络设置", null);
        }

        @Override
        public void onConvertSuccess(WXPayStatus response) {
            if(mGetWxPayStateProgressDialog != null) {
                mGetWxPayStateProgressDialog.dismiss();
                mGetWxPayStateProgressDialog = null;
            }
            if(!response.isError()) {
                //如果微信没有回调，则走下面的补丁提示
                if(TextUtils.equals("1",response.getData().getCharged())) {
                    DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),"订单已支付", new OnFinishPayBtnClickListener());
                    mIsWeixinPaySuccess = true;
                }else {
                    if(response.getData().getIsPay() == 1) {
                        DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),"订单已支付", new OnFinishPayBtnClickListener());
                        mIsWeixinPaySuccess = true;
                    }else {
                        DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),"订单未支付", null);
                    }
                }
            }else {
                DialogUtils.showWarnDialog(mContext, getString(R.string.dialog_pay_result_title),response.getMessage(), null);
            }
        }

        @Override
        public void onConvertFailed() {
            if(mGetWxPayStateProgressDialog != null) {
                mGetWxPayStateProgressDialog.dismiss();
                mGetWxPayStateProgressDialog = null;
            }
        }
    }
    //用户订单支付完成,点击事件
    private class OnFinishPayBtnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    private class UserPayListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<UserIdentifyPayInfo> {

        @Override
        public void onConvertSuccess(UserIdentifyPayInfo data) {
            if(mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
            if (data == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
                return;
            }

            if (data.isError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
            } else {
                UserIdentifyPayInfo.DataEntity dataEntity = data.getData();
                UserIdentifyPayInfo.DataEntity.GoodsEntity goods = dataEntity.getGoods();
                UserIdentifyPayInfo.DataEntity.Payment_typeEntity payment_types = dataEntity.getPayment_type();
                //0为老用户，1为新用户
                if (dataEntity.getIs_new_user() == 1) {
                    mWeChatPayBtn.setChecked(true);
                } else if (dataEntity.getIs_new_user() == 0) {
                    int pay_max = Math.max(Math.max(payment_types.getBAIFUBAO_WAP(), payment_types.getMALIPAY()),
                            payment_types.getWXPAY());
                    if (pay_max == payment_types.getWXPAY()) {
                        mWeChatPayBtn.setChecked(true);
                    } else if (pay_max == payment_types.getMALIPAY()) {
                        mAliPayBtn.setChecked(true);
                    } else {
                        mBfbPayBtn.setChecked(true);
                    }
                }
                final UserIdentifyPayInfo.DataEntity.IntegralEntity integral = dataEntity.getIntegral();
                if (integral != null) {
                    mIntegralIntroTV.setText(integral.getName());
                    mIntegerAmount = integral.getAmount();
                }
                List<UserIdentifyPayInfo.DataEntity.PaylistEntity> paylist = dataEntity.getPaylist();
                if (goods != null) {
                    mUserId = goods.getUser_id();
                    mIdentifyId = goods.getId();
                    mIdentifyType = goods.getJb_type();
                    mIdentifyTypeTV.setText(getIntent().getStringExtra(IntentConstant.IdentifyType));
                    mPriceTV.setText(goods.getCharge_price());
                    mGoodsId = goods.getId();//支付的扣费请求需要goodsid
                    mGoodsPrice = Double.parseDouble(goods.getCharge_price());
                    mUserIntegerCB.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                        @Override
                        public void onToggle(boolean on) {
                            if (on) {
                                mPayMethod = USE_BALANCE_METHOD;
                                mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mIntegerAmount, mCashCouponAmount);
                            } else {
                                mPayMethod = NOT_USE_BALANCE_METHOD;
                                mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
                            }
                            mPayPriceTV.setText(StringUtils.getString("¥", Math.max(mPayPrice, 0)));
                        }
                    });
                    if (mUserIntegerCB.isToggleOn()) {
                        mPayMethod = USE_BALANCE_METHOD;
                        mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mIntegerAmount, mCashCouponAmount);
                    } else {
                        mPayMethod = NOT_USE_BALANCE_METHOD;
                        mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
                    }
                    mUserAmount = dataEntity.getMoney();
                    mUserAccountTV.setText(StringUtils.getString(getString(R.string.available_balance), mUserAmount));
                    mPayPriceTV.setText(StringUtils.getString("¥", Math.max(mPayPrice, 0)));
                }

                if (paylist != null) {
                    for (int i = 0; i < paylist.size(); i++) {
                        UserIdentifyPayInfo.DataEntity.PaylistEntity paylistEntity = paylist.get(i);
                        String id = paylistEntity.getId();
                        if (USE_BALANCE_PAY.equals(id)) {
                            mBalancePayBtn.setVisibility(View.VISIBLE);
                        } else if (USE_WX_PAY.equals(id)) {
                            mWeChatPayBtn.setVisibility(View.VISIBLE);
                        } else if (USE_BFB_PAY.equals(id)) {
                            mBfbPayBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
            if(mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserIdentifyPayInfo> task = new StringToBeanTask<>(UserIdentifyPayInfo.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
            if(mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
        }
    }


    private class UserIdentifyMeetListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<UserIdentifyMeetPayInfo>
    {

        @Override
        public void onConvertSuccess(UserIdentifyMeetPayInfo data) {
            if(mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
            if (data == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
                return;
            }

            if (data.isError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
            } else {
                UserIdentifyMeetPayInfo.DataEntity dataEntity = data.getData();
//                UserIdentifyPayInfo.DataEntity.GoodsEntity goods = dataEntity.getGoods();
                UserIdentifyMeetPayInfo.DataEntity.PaymentTypeEntity payment_types = dataEntity.getPayment_type();
                //0为老用户，1为新用户,根据支付次数选择用户的支付方式
                if (dataEntity.getIs_new_user() == 1) {
                    mWeChatPayBtn.setChecked(true);
                } else if (dataEntity.getIs_new_user() == 0) {
                    int pay_max = Math.max(Math.max(payment_types.getBaifubao_wap(), payment_types.getMalipay()),
                            payment_types.getWxpay());
                    if (pay_max == payment_types.getWxpay()) {
                        mWeChatPayBtn.setChecked(true);
                    } else if (pay_max == payment_types.getMalipay()) {
                        mAliPayBtn.setChecked(true);
                    } else {
                        mBfbPayBtn.setChecked(true);
                    }
                }
//                final UserIdentifyPayInfo.DataEntity.IntegralEntity integral = dataEntity.getIntegral();
//                if (integral != null) {
//                    mIntegralIntroTV.setText(integral.getName());
//                    mIntegerAmount = integral.getAmount();
//                }
                List<UserIdentifyMeetPayInfo.DataEntity.PaylistEntity> paylist = dataEntity.getPaylist();
//                if (goods != null) {
//                    mUserId = goods.getUser_id();
                mUserId = dataEntity.getUser_id();
//                    mIdentifyId = goods.getId();
//                    mIdentifyType = goods.getJb_type();
                mIdentifyType =  "6";
                mIdentifyTypeTV.setText(getIntent().getStringExtra(IntentConstant.IdentifyType));
//                    mPriceTV.setText(goods.getCharge_price());
                mPriceTV.setText(StringUtils.getString(dataEntity.getPrice()));
//                    mGoodsId = dataEntity.getId();//支付的扣费请求需要goodsid
                mGoodsId = mIdentifyMeetSignId;//鉴定会的id当成原来的goodsid
                mGoodsPrice = dataEntity.getPrice();//鉴定原价
//                    mUserIntegerCB.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
//                        @Override
//                        public void onToggle(boolean on) {
//                            if (on) {
//                                mPayMethod = USE_BALANCE_METHOD;
//                                mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mIntegerAmount, mCashCouponAmount);
//                            } else {
//                                mPayMethod = NOT_USE_BALANCE_METHOD;
//                                mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
//                            }
//                            mPayPriceTV.setText(StringUtils.getString("¥", Math.max(mPayPrice, 0)));
//                        }
//                    });
//                    if (mUserIntegerCB.isToggleOn()) {
//                        mPayMethod = USE_BALANCE_METHOD;
//                        mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mIntegerAmount, mCashCouponAmount);
//                    } else {
//                        mPayMethod = NOT_USE_BALANCE_METHOD;
//                        mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
//                    }
                //TODO
                    mPayMethod = NOT_USE_BALANCE_METHOD;
                    mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);

                    mUserAmount = dataEntity.getMoney();
                    mUserAccountTV.setText(StringUtils.getString(getString(R.string.available_balance), mUserAmount));
                    mPayPriceTV.setText(StringUtils.getString("¥", Math.max(mPayPrice, 0)));
//                }

                if (paylist != null) {
                    for (int i = 0; i < paylist.size(); i++) {
                        UserIdentifyMeetPayInfo.DataEntity.PaylistEntity paylistEntity = paylist.get(i);
                        String id = paylistEntity.getId();
                        if (USE_BALANCE_PAY.equals(id)) {
                            mBalancePayBtn.setVisibility(View.VISIBLE);
                        } else if (USE_WX_PAY.equals(id)) {
                            mWeChatPayBtn.setVisibility(View.VISIBLE);
                        } else if (USE_BFB_PAY.equals(id)) {
                            mBfbPayBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

        }

        @Override
        public void onConvertFailed() {
            if(mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
            DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserIdentifyMeetPayInfo> task = new StringToBeanTask<>(UserIdentifyMeetPayInfo.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if(mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
            DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
        }
    }

    private class BobaoPayListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserPayData> {

        @Override
        public void onConvertSuccess(UserPayData data) {
            if (data != null) {
                if (!data.getError()) {
                    DialogUtils.showShortPromptToast(mContext, R.string.pay_success);
                    Intent intent = new Intent(mContext, PaySuccessActivity.class);
                    startActivity(intent);
                    finish();
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Bobao_Success);
                } else {
                    DialogUtils.showShortPromptToast(mContext, data.getMessage());
                    Map<String, String> payFailedMap = new HashMap<>();
                    payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, data.getMessage());
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
                }
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
            }
        }

        @Override
        public void onConvertFailed() {
            finish();
            DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserPayData> task = new StringToBeanTask<>(UserPayData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            finish();
            DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
            Map<String, String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
        }
    }

    private class CashCouponListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<CashCoupon> {

        @Override
        public void onConvertSuccess(CashCoupon data) {
            if (data != null) {
                if (!data.getError()) {
                    mCashCouponAmount = Double.valueOf(data.getData().getAmount());
                    mCashCouponView.setText(getString(R.string.with_money, mCashCouponAmount));
                    if (mUserIntegerCB.isToggleOn()) {
                        mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mIntegerAmount, mCashCouponAmount);
                    } else {
                        mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
                    }
                    mPayPriceTV.setText(StringUtils.getString("¥", Math.max(mPayPrice, 0)));
                    UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Success);
                } else {
                    DialogUtils.showShortPromptToast(mContext, data.getMessage());
                    UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
                }
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.get_info_failed);
                UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
            }
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext, R.string.get_info_failed);
            UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<CashCoupon> task = new StringToBeanTask<>(CashCoupon.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.get_info_failed);
            UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
        }
    }

}
