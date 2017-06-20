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
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.AuthCodeResponse;
import com.bobaoo.xiaobao.manager.UserRegistManeger;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserRegisterActivity extends BaseActivity {

    /**
     * 验证码请求
     */
    private final int AUTH_CODE_TIME_OUT = 60;
    private int mTimeCount = 0;
    private Handler mAuthCodeTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimeCount > 0) {
                mGetAuthCodeBtn.setText(StringUtils.getString(mTimeCount,getString(R.string.unit_second)));
                mTimeCount--;
                mGetAuthCodeBtn.setBackgroundResource(R.drawable.bg_button_balance_recharge2);
                mAuthCodeTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                mGetAuthCodeBtn.setText(R.string.get_identify_code);
                mGetAuthCodeBtn.setEnabled(true);
                mAuthCodeEt.setText("");
                mGetAuthCodeBtn.setBackgroundResource(R.drawable.bg_button_balance_recharge);
                mAuthCodeTimeHandler.removeMessages(0);
            }
        }
    };

    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    private EditText mPhoneEditView;
    private EditText mUserPasswordEt;
    private EditText mAuthCodeEt;
    private TextView mGetAuthCodeBtn;
    private Button mRegisterBtn;
    private CheckBox mPswChoice;
    private Class<?> mTargetActivity;

    @Override
    protected void getIntentData() {
        String targetActivityName = getIntent().getStringExtra(IntentConstant.TARGET_ACTIVITY);
        if (!TextUtils.isEmpty(targetActivityName)) {
            switch (targetActivityName) {
                case "PriceQueryContentActivity":
                    mTargetActivity = PriceQueryContentActivity.class;
                    break;
                case "SubmitOrderActivity":
                    mTargetActivity = SubmitOrderActivity.class;
                    break;
                case "OrderDetailActivity":
                    mTargetActivity = OrderDetailActivity.class;
                    break;
                case "InfoDetailActivity":
                    mTargetActivity = InfoDetailActivity.class;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_userregist;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.user_register);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initContent() {
        mPhoneEditView = (EditText) findViewById(R.id.et_userphone);
        mGetAuthCodeBtn = (TextView) findViewById(R.id.get_auth_code);
        mUserPasswordEt = (EditText) findViewById(R.id.et_userpsw);
        mAuthCodeEt = (EditText) findViewById(R.id.et_authcode);
        mRegisterBtn = (Button) findViewById(R.id.bt_regist);
        mPswChoice = (CheckBox) findViewById(R.id.psw_choice);
        mPswChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mUserPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    mUserPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        mGetAuthCodeBtn.setBackgroundResource(R.drawable.bg_button_balance_recharge2);
        setOnClickListener(mGetAuthCodeBtn, mRegisterBtn);
        mPhoneEditView.addTextChangedListener(new MobileWatch());
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

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_auth_code:
                sendAuthCode();
                break;
            case R.id.bt_regist:
                register();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (mTimeCount > 0) {
            mTimeCount = 0;
            mAuthCodeTimeHandler.removeMessages(0);
        }
        // 取消广播接受
        unregisterReceiver(mSMSBroadcastReceiver);
        super.onDestroy();
    }

    private void sendAuthCode() {
        // 校验电话号码有效性
        String tel = mPhoneEditView.getText().toString().trim();
        if (StringUtils.checkPhoneNumber(tel)) {
            // 验证码获取等待定时器开启
            if (mTimeCount <= 0) {
                // 提交数据
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.AUTH_CODE, NetConstant.getAuthCodeParams(mContext, tel),
                        new GetAuthCodeListener());

                // 设置不可点击
                mGetAuthCodeBtn.setEnabled(false);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_wait);
            }
        } else {
//            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        }
    }

    private void register() {
        String tel = mPhoneEditView.getText().toString().trim();
        String password = mUserPasswordEt.getText().toString().trim();
        String authCode = mAuthCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_number);
        } else if (TextUtils.isEmpty(password)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_password);
        } else if (password.length() < 6) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_password_too_short);
        } else if (!StringUtils.checkPhoneNumber(tel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        } else {
            UserRegistManeger.getsInstance(mContext).register(tel, password, authCode, mTargetActivity);
            UmengUtils.onEvent(mContext, EventEnum.UserRegist);
        }
    }

    /**
     * 获取验证码
     */
    private class GetAuthCodeListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AuthCodeResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AuthCodeResponse> task = new StringToBeanTask<>(AuthCodeResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mTimeCount > AUTH_CODE_TIME_OUT) {
                mTimeCount = AUTH_CODE_TIME_OUT;
                mAuthCodeTimeHandler.removeMessages(0);
            }
            mGetAuthCodeBtn.setEnabled(true);
            mGetAuthCodeBtn.setText(R.string.get_auth_code);
        }

        @Override
        public void onConvertSuccess(AuthCodeResponse response) {
            if (response.isError()) {
                //返回信息有错误，则表示手机号已经登陆,停止计时
                if (mTimeCount > 0) {
                    mTimeCount = 0;
                    mAuthCodeTimeHandler.removeMessages(0);
                }
                DialogUtils.showShortPromptToast(mContext,response.getMessage());
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
            if (mTimeCount > AUTH_CODE_TIME_OUT) {
                mTimeCount = AUTH_CODE_TIME_OUT;
                mAuthCodeTimeHandler.removeMessages(0);
            }
            mGetAuthCodeBtn.setEnabled(true);
            mGetAuthCodeBtn.setText(R.string.get_auth_code);
        }
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
                }
            }
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
                mGetAuthCodeBtn.setBackgroundResource(R.drawable.bg_button_balance_recharge2);
            }else{
                mGetAuthCodeBtn.setBackgroundResource(R.drawable.bg_button_balance_recharge);
            }
        }
    }
}
