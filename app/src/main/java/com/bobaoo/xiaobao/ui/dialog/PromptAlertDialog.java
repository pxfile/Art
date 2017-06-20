package com.bobaoo.xiaobao.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.activity.MainActivity;
import com.bobaoo.xiaobao.ui.activity.UserIdentifyMeetingActivity;
import com.bobaoo.xiaobao.ui.fragment.UserIdentifyMeetingFragment;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by Ameng on 2016/3/16.
 */
public class PromptAlertDialog extends BaseCustomerDialog {
    private Context mContext;
//    private android.app.AlertDialog ad;
    private LinearLayout mLlContentActive, mLlOrderNoOk, mLlContentPay;
    private TextView mTvOrder,mTvCancel;
    private Button mBtnOk, mBtnNo, mBtnDelete;
    private UserIdentifyMeetingFragment fragment;
    private int pos = -1;
    private int flag;
    private boolean mIsIdentifyMeet;
    public PromptAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public PromptAlertDialog(Context context, UserIdentifyMeetingFragment fragment, int pos,int
            flag,boolean mIsIdentifyMeet) {
        super(context,R.style.CustomDialog);
        this.mContext = context;
        this.fragment = fragment;
        this.pos = pos;
        this.flag = flag;
        this.mIsIdentifyMeet = mIsIdentifyMeet;
    }
    @Override
    protected int setLayoutViewId() {
        return R.layout.prompt_alert_dialog;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mLlContentActive = (LinearLayout) findViewById(R.id.ll_content_active);
        mLlOrderNoOk = (LinearLayout) findViewById(R.id.ll_order_no_ok);
        mLlContentPay = (LinearLayout) findViewById(R.id.ll_content_pay);
        mTvOrder = (TextView) findViewById(R.id.tv_delete_order);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel_enroll);
        mBtnDelete = (Button) findViewById(R.id.active_end_ok);
        mBtnNo = (Button) findViewById(R.id.btn_cancel);
        mBtnOk = (Button) findViewById(R.id.btn_ok);

        if (AppConstant.MEET_CANCEL_ENROLL == flag){
            mTvCancel.setVisibility(View.VISIBLE);
            mLlOrderNoOk.setVisibility(View.VISIBLE);
            mTvOrder.setVisibility(View.GONE);
            mLlContentActive.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.GONE);
            mLlContentPay.setVisibility(View.GONE);
            mBtnOk.setOnClickListener(this);
            mBtnNo.setOnClickListener(this);
            return;
        }
        if (AppConstant.MEET_ORDER_DELETE == flag) {
            mTvOrder.setVisibility(View.VISIBLE);
            mLlOrderNoOk.setVisibility(View.VISIBLE);
            mLlContentActive.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mLlContentPay.setVisibility(View.GONE);
            mBtnOk.setOnClickListener(this);
            mBtnNo.setOnClickListener(this);
            return;
        }
        if (AppConstant.ACTIVE_OVER == flag){
            mLlContentActive.setVisibility(View.VISIBLE);
            mBtnDelete.setVisibility(View.VISIBLE);
            mTvOrder.setVisibility(View.GONE);
            mLlContentPay.setVisibility(View.GONE);
            mLlOrderNoOk.setVisibility(View.GONE);
            mTvCancel.setVisibility(View.GONE);
            mBtnDelete.setOnClickListener(this);
            return;
        }
        if (AppConstant.PAY_CANCEL == flag){
            mLlContentPay.setVisibility(View.VISIBLE);
            mLlOrderNoOk.setVisibility(View.VISIBLE);
            mTvCancel.setVisibility(View.GONE);
            mTvOrder.setVisibility(View.GONE);
            mLlContentActive.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.GONE);
            mBtnOk.setOnClickListener(this);
            mBtnNo.setOnClickListener(this);
            return;
        }
    }

    @Override
    protected void attachData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (AppConstant.PAY_CANCEL == flag){
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_CancelPay_Onclick);
                    Intent intent = null;
                    if (mIsIdentifyMeet){
                        //取消支付,跳转到用户鉴定会列表页面
                        intent = new Intent(mContext, UserIdentifyMeetingActivity.class);
                        intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX,
                                IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
                        mContext.startActivity(intent);
                    }else{
                        intent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);
                    }
                    ((Activity)mContext).finish();
                    break;
                }
                if (AppConstant.MEET_CANCEL_ENROLL == flag){
                    ((Activity)mContext).finish();
                    break;
                }
                if (AppConstant.MEET_ORDER_DELETE == flag) {
                    fragment.startDeleteIdentifyTreasureRequest(pos);
                    UmengUtils.onEvent(mContext, EventEnum.User_Delete_Order_OK);
                    dismiss();
                    break;
                }
            case R.id.btn_cancel:
                if (pos == -1){
                    dismiss();
                    break;
                }
                UmengUtils.onEvent(mContext, EventEnum.User_Delete_Order_NO);
                dismiss();
                break;
            case R.id.active_end_ok:
                UmengUtils.onEvent(mContext, EventEnum.Active_End_OK);
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
