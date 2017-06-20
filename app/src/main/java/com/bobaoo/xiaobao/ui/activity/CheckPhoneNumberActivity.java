package com.bobaoo.xiaobao.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.AuthCodeResponse;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by you on 2015/8/10.
 */
public class CheckPhoneNumberActivity extends BaseActivity {
    private EditText mPhoneEditView;
    private EditText mAuthCodeEt;
    private TextView mGetAuthCodeBtn;
    private View mSubmitView;
    private ProgressDialog mCheckPhoneProgressDialog;
    private String mPhoneNumber;

    //自动填充校验码
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private boolean isSubmit;
    /**
     * 验证码请求
     */
    private final int AUTH_CODE_TIME_OUT = 60;
    private int mTimeCount = 0;
    private Handler mAuthCodeTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimeCount > 0) {
                mGetAuthCodeBtn.setText(StringUtils.getString(mTimeCount, getString(R.string.unit_second)));
                mTimeCount--;
                mAuthCodeTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.auth_code_invalid);
                mGetAuthCodeBtn.setText(R.string.get_auth_code);
                mAuthCodeEt.setText("");
                mAuthCodeTimeHandler.removeMessages(0);
            }
        }
    };

    @Override
    protected void getIntentData() {
        mPhoneNumber = getIntent().getStringExtra(IntentConstant.PHONE_NUMBER);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_check_phone_number;
    }

    @Override
    protected void initTitle() {
        View headView = findViewById(R.id.layout_title);
        headView.setBackgroundColor(Color.TRANSPARENT);
        TextView backView = (TextView) findViewById(R.id.tv_back);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back_dark);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        backView.setCompoundDrawables(drawable, null, null, null);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.check_phone_number));
        titleView.setTextColor(getResources().getColor(R.color.gray10));
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mPhoneEditView = (EditText) findViewById(R.id.et_userphone);
        mAuthCodeEt = (EditText) findViewById(R.id.et_authcode);
        mGetAuthCodeBtn = (TextView) findViewById(R.id.get_auth_code);
        mSubmitView = findViewById(R.id.bt_submit);

        if (!TextUtils.isEmpty(mPhoneNumber)) {
            mPhoneEditView.setText(mPhoneNumber);
            CharSequence mPhoneEtText = mPhoneEditView.getText();
            if (mPhoneEtText instanceof Spannable) {
                Spannable spanText = (Spannable) mPhoneEtText;
                Selection.setSelection(spanText, mPhoneEtText.length());
            }
        }
        setOnClickListener(mGetAuthCodeBtn, mSubmitView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                if (isSubmit) {
                    finish();
                } else {
                    showPickDialog();
                }
                break;
            case R.id.get_auth_code:
                mAuthCodeEt.setText("");
                sendAuthCode();
                break;
            case R.id.bt_submit:
                checkPhoneNumber();
                break;
            default:
                break;
        }
    }

    private void checkPhoneNumber() {
        String tel = mPhoneEditView.getText().toString().trim();
        String code = mAuthCodeEt.getText().toString().trim();
        if (StringUtils.checkPhoneNumber(tel) && !TextUtils.isEmpty(code)) {
            if (mCheckPhoneProgressDialog == null) {
                mCheckPhoneProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.checking_phone_number));
            }
            new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getCheckPhoneAuthCodeParams(mContext, tel, code),
                    new CheckPhoneListener());
        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.wrong_number_or_code);
        }
    }

    private void sendAuthCode() {
        // 校验电话号码有效性
        String tel = mPhoneEditView.getText().toString().trim();
        if (StringUtils.checkPhoneNumber(tel)) {
            // 验证码获取等待定时器开启
            if (mTimeCount <= 0) {
                // 提交数据
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getCheckPhoneAuthCodeParams(mContext, tel),
                        new GetAuthCodeListener());
                //重置定时器
                mTimeCount = AUTH_CODE_TIME_OUT;
                mAuthCodeTimeHandler.sendEmptyMessage(0);
                mGetAuthCodeBtn.setText(getString(R.string.get_identify_code));
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_wait);
            }
        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        }
    }

    private class CheckPhoneListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AuthCodeResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AuthCodeResponse> task = new StringToBeanTask<>(AuthCodeResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.check_phone_failed);
            mCheckPhoneProgressDialog.dismiss();
            mCheckPhoneProgressDialog = null;
        }

        @Override
        public void onConvertSuccess(AuthCodeResponse response) {
            if (response.isError()) {
                DialogUtils.showShortPromptToast(mContext, response.getMessage());
                resetTimer();
            } else {
                SharedPreferencesUtils.setSharedPreferencesBoolean(mContext, AppConstant.SP_KEY_PHONE_CHECKED, true);
                //保存手机号码到本地
                String phone = mPhoneEditView.getText().toString().trim();
                UserInfoUtils.setPhone(mContext, phone);
                isSubmit = true;
                Intent intent = new Intent();
                intent.putExtra(IntentConstant.CHECK_PHONE_FLAG, true);
                setResult(RESULT_OK, intent);
                finish();
            }

            mCheckPhoneProgressDialog.dismiss();
            mCheckPhoneProgressDialog = null;
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext, R.string.check_phone_failed);
            mCheckPhoneProgressDialog.dismiss();
            mCheckPhoneProgressDialog = null;
        }
    }

    /**
     * 获取验证码回调
     */
    private class GetAuthCodeListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AuthCodeResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AuthCodeResponse> task = new StringToBeanTask<>(AuthCodeResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            resetTimer();
        }

        @Override
        public void onConvertSuccess(AuthCodeResponse response) {
            if (response.isError()) {
                DialogUtils.showShortPromptToast(mContext, response.getMessage());
                resetTimer();
            } else {
                DialogUtils.showShortPromptToast(mContext, response.getData());
            }

        }

        @Override
        public void onConvertFailed() {
            resetTimer();
        }
    }

    private void resetTimer() {
        mTimeCount = 0;
//        mAuthCodeTimeHandler.removeMessages(0);
//        mGetAuthCodeBtn.setText(R.string.retry);
//        mGetAuthCodeBtn.setEnabled(true);
    }

    private class SMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstant.SMS_RECEIVED_ACTION)) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                if (pdus != null && pdus.length > 0) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    Pattern pattern = Pattern.compile("[^0-9]");
                    Matcher matcher = pattern.matcher(smsMessage.getDisplayMessageBody());
                    mAuthCodeEt.setText(matcher.replaceAll(""));
                    resetTimer();
                }
            }
        }
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(AppConstant.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(1000);
        //注册广播
        registerReceiver(mSMSBroadcastReceiver, intentFilter);
    }

    @Override
    protected void refreshData() {

    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.cancel_modify_auth_code).setNegativeButton(R.string.continue_modify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.confirm_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                finish();
            }
        }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSubmit) {
                finish();
            } else {
                showPickDialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
