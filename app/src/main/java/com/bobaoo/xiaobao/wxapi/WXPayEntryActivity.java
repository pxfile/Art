package com.bobaoo.xiaobao.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.IdentifyMeetPayResponse;
import com.bobaoo.xiaobao.domain.UserPayData;
import com.bobaoo.xiaobao.manager.WXDealManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.PaySuccessActivity;
import com.bobaoo.xiaobao.ui.activity.UserRechargeRecordActivity;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wxfe76631ca3c86dbc");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq request) {
    }

    @Override
    public void onResp(BaseResp response) {
        Log.e("WXPayDebug", "User_Onresponse_WX_Entry");
        Map<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_PAY_ORDERID, WXDealManager.getInstance().getGoodsId());
        UmengUtils.onEvent(this, EventEnum.User_Onresponse_WX_Entry, map);
        if (response.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (response.errCode == -1) {
                UmengUtils.onEvent(this, EventEnum.User_Onresponse_WX_Entry_ERROCODE_1, map);
                Log.e("WXPayDebug", "User_Onresponse_WX_Entry_ERROCODE_1");
                finish();
            } else if (response.errCode == -2) {
                UmengUtils.onEvent(this, EventEnum.User_Onresponse_WX_Entry_ERROCODE_2, map);
                Log.e("WXPayDebug", "User_Onresponse_WX_Entry_ERROCODE_2");
                finish();
            } else {//微信支付成功
                if (WXDealManager.getInstance().getWXCashAction() == WXDealManager.WXCashAction.PAY) {
                    //开始扣费请求
                    UmengUtils.onEvent(this, EventEnum.User_Consume_WX_Start, map);
                    Log.e("WXPayDebug", "User_Consume_WX_Start");
                    //扣费请求,双保险
                    if(!WXDealManager.getInstance().isIsIdentifyMeet()){
                        startUserPayRequest(WXDealManager.getInstance().getGoodsId(), WXDealManager.getInstance().getPayMethod(),
                                WXDealManager.getInstance().getmCashCouponId());
                        Intent intent = new Intent(WXPayEntryActivity.this, PaySuccessActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        //鉴定会的扣费请求
                        startIdentifyMeetPayRequest(WXDealManager.getInstance().getGoodsId(),
                                WXDealManager.getInstance().getmCashCouponId());
                    }
                } else {
                    //充值成功后 跳转充值记录
                    String userId = WXDealManager.getInstance().getmUserId();
                    Intent intent = new Intent(WXPayEntryActivity.this, UserRechargeRecordActivity.class);
                    intent.putExtra(IntentConstant.USER_ID, userId);
                    startActivity(intent);
                    new HttpUtils().send(
                            HttpRequest.HttpMethod.GET,
                            NetConstant.HOST,
                            NetConstant.getRechargeScoreParams(WXPayEntryActivity.this,
                                    WXDealManager.getInstance().getRechargeAmount()), null);
                    finish();
                }
            }
        }

    }

    private void startUserPayRequest(String goodsId, String payMethodFlg, String cashCouponId) {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getPayParams(WXPayEntryActivity.this,
                goodsId, payMethodFlg, cashCouponId), new WXPayEntryListener());
    }

    private void startIdentifyMeetPayRequest(String goodsId, String cashCouponId) {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getIdentifyMeetPayParams(WXPayEntryActivity.this,
                goodsId, cashCouponId), new WXIdentifyMeetPayEntryListener());
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

    private class WXPayEntryListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<UserPayData> {

        @Override
        public void onConvertSuccess(UserPayData data) {
            if (data != null) {
                if (!data.getError()) {
                    finish();
                }
            }
            finish();
        }

        @Override
        public void onConvertFailed() {
            finish();
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserPayData> task = new StringToBeanTask<>(UserPayData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            finish();
        }
    }

    //扣费请求的回调
    private class WXIdentifyMeetPayEntryListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<IdentifyMeetPayResponse> {

        @Override
        public void onConvertSuccess(IdentifyMeetPayResponse data) {
            if (data != null) {
                if (!data.isError()) {
                    finish();
                    Log.e("WXPayTest", "鉴定会扣费成功");
                }
            }
            finish();
            //清除鉴定会标记位
            WXDealManager.getInstance().setIsIdentifyMeet(false);

        }

        @Override
        public void onConvertFailed() {
            finish();
            //清除鉴定会标记位
            WXDealManager.getInstance().setIsIdentifyMeet(false);
            Log.e("WXPayTest", "onConvertFailed");
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<IdentifyMeetPayResponse> task = new StringToBeanTask<>(IdentifyMeetPayResponse.class, this);
            task.execute(responseInfo.result);
            Log.e("WXPayTest", responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            finish();
            //清除鉴定会标记位
            WXDealManager.getInstance().setIsIdentifyMeet(false);
            Log.e("WXPayTest", "onFailure");
        }
    }
}
