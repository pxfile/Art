package com.bobaoo.xiaobao.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.AuthCodeResponse;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.utils.CommonUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by you on 2015/6/23.
 */
public class ForgetPswActivity extends BaseActivity {
    private final int AUTH_CODE_TIME_OUT = 60;
    private EditText mMobileEt;
    private EditText mAuthCodeEt;
    private TextView mGetAuthCodeTv;

    private EditText mNewPswdEt;
    private Button mChangePswd;
    private CheckBox mPswChoice;

    private int mTimeCount = 0;
    private Handler mAuthCodeTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimeCount > 0) {
                mGetAuthCodeTv.setText(mTimeCount + "s");
                mTimeCount--;
                mGetAuthCodeTv.setBackgroundResource(R.drawable.bg_button_balance_recharge2);
                mAuthCodeTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                mGetAuthCodeTv.setText(R.string.get_identify_code);
                mAuthCodeEt.setText("");
                mGetAuthCodeTv.setBackgroundResource(R.drawable.bg_button_balance_recharge);
                mAuthCodeTimeHandler.removeMessages(0);
            }
        }
    };
    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private class SMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    //短信内容
                    String content = smsMessage.getDisplayMessageBody();
                    Pattern pattern = Pattern.compile("[^0-9]");
                    Matcher matcher = pattern.matcher(content);
                    String all = matcher.replaceAll("");
                    mAuthCodeEt.setText(all);

                }
            }
        }
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_forget_pswd;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initContent() {
        mMobileEt = (EditText) findViewById(R.id.et_mobile);
        mAuthCodeEt = (EditText) findViewById(R.id.et_authcode);
        mGetAuthCodeTv = (TextView) findViewById(R.id.get_auth_code);
        mPswChoice = (CheckBox) findViewById(R.id.psw_choice);
        setOnClickListener(mGetAuthCodeTv);
        mMobileEt.addTextChangedListener(new MobileWatch());
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVED_ACTION);
        intentFilter.setPriority(1001);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mNewPswdEt = (EditText) findViewById(R.id.et_user_new_psw);
        mChangePswd = (Button) findViewById(R.id.bt_change_psw);
        setOnClickListener(mChangePswd);
        mPswChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mNewPswdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mNewPswdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        mGetAuthCodeTv.setBackgroundResource(R.drawable.bg_button_balance_recharge2);
        TextView titleBack = (TextView) findViewById(R.id.tv_back);
        titleBack.setText(R.string.forget_psw);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        String tel = mMobileEt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.get_auth_code:
                if (tel == null || "".equals(tel)){
                    return;
                }
                if (!CommonUtils.checkPhoneNumber(tel)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
                    return;
                }
                if (mTimeCount > 0) {
                    DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_wait);
                    return;
                }
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getAuthCodeRequestParams(mContext, tel), new AuthCodeListener());

                break;
            case R.id.bt_change_psw:
                if (!CommonUtils.checkPhoneNumber(tel)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
                }
                String newPswd = mNewPswdEt.getText().toString().trim();
                String authCode = mAuthCodeEt.getText().toString().trim();
                if (TextUtils.isEmpty(authCode)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.input_identify_code);
                    return;
                }
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getChangePasswdRequestParams(mContext, tel, authCode, newPswd), new ChangePasswdListener());
                break;
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
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mSMSBroadcastReceiver);
        if (mTimeCount > 0) {
            mTimeCount = 0;
            mAuthCodeTimeHandler.removeMessages(0);
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

    private class ChangePasswdListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AuthCodeResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AuthCodeResponse> task = new StringToBeanTask<>(AuthCodeResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_failed);
        }

        @Override
        public void onConvertSuccess(AuthCodeResponse response) {
            if (response.isError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_failed);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_success);
                Intent intent = new Intent(mContext, UserLogInActivity.class);
                jump(intent);
            }
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_failed);
        }
    }

    private class AuthCodeListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AuthCodeResponse> {

        @Override
        public void onConvertSuccess(AuthCodeResponse response) {
            if (response.isError()) {
                //返回信息有错误，则表示手机号已经登陆,停止计时
                if (mTimeCount > 0) {
                    mTimeCount = 0;
                    mAuthCodeTimeHandler.removeMessages(0);
                }
                String msgStr = response.getMessage();
                if (msgStr.contains("秒")){
                    String substring = msgStr.substring(msgStr.indexOf("在") + 1,msgStr.indexOf("秒"));
                    mTimeCount = Integer.parseInt(substring);
                    mAuthCodeTimeHandler.sendEmptyMessage(0);
                }else{
                    DialogUtils.showShortPromptToast(mContext,msgStr);
                }
            } else {
                if (mTimeCount > 0) {
                    mTimeCount = 0;
                    mAuthCodeTimeHandler.removeMessages(0);
                }
                mTimeCount = AUTH_CODE_TIME_OUT;//重置定时器
                DialogUtils.showShortPromptToast(mContext, R.string.get_auto_code_success);
                mAuthCodeTimeHandler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext, R.string.get_auto_code_failed);
            if (mTimeCount > 0) {
                mTimeCount = 0;
                mAuthCodeTimeHandler.removeMessages(0);
            }
            mGetAuthCodeTv.setText(R.string.get_auth_code);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AuthCodeResponse> task = new StringToBeanTask<>(AuthCodeResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.get_auto_code_failed);
            if (mTimeCount > 0) {
                mTimeCount = 0;
                mAuthCodeTimeHandler.removeMessages(0);
            }
            mGetAuthCodeTv.setText(R.string.get_auth_code);
        }
    }

    private class MobileWatch implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable == null || "".equals(editable.toString().trim())){
                mGetAuthCodeTv.setBackgroundResource(R.drawable.bg_button_balance_recharge2);
            }else{
                mGetAuthCodeTv.setBackgroundResource(R.drawable.bg_button_balance_recharge);
            }
        }
    }
}
