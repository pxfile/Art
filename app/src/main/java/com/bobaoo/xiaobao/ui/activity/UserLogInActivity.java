package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.LoginStepOneResponse;
import com.bobaoo.xiaobao.domain.LoginStepTwoResponse;
import com.bobaoo.xiaobao.listener.XGPushCallback;
import com.bobaoo.xiaobao.manager.SocialPlatformLoginManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.android.tpush.XGPushManager;

public class UserLogInActivity extends BaseActivity {

    private EditText mUserNameEt;
    private EditText mUserPasswordEt;
    private Class<?> mTargetActivity;
    private ProgressDialog mProgressDialog;
    private CheckBox mPswChoice;
    private String enrollId;

    @Override
    protected void getIntentData() {
        String targetActivityName = getIntent().getStringExtra(IntentConstant.TARGET_ACTIVITY);
        enrollId = getIntent().getStringExtra(IntentConstant.EnrollId);
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
        return R.layout.activity_userlogin;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        TextView titleView = (TextView) findViewById(R.id.tv_back);
        titleView.setText(R.string.user_login);
        // 注册按钮
        TextView registerView = (TextView) findViewById(R.id.tv_right);
        registerView.setText(R.string.register);
        setOnClickListener(titleView, registerView);

    }

    @Override
    protected void initContent() {
        mUserNameEt = (EditText) findViewById(R.id.et_username);
        mUserPasswordEt = (EditText) findViewById(R.id.et_userpsw);
        mPswChoice = (CheckBox) findViewById(R.id.psw_choice);
        View loginView = findViewById(R.id.tv_login);
        View qqLoginView = findViewById(R.id.tv_qq_login);
        View weChatLoginView = findViewById(R.id.tv_wx_login);
        View forgetView = findViewById(R.id.tv_forget_psw);
        setOnClickListener(loginView, qqLoginView, weChatLoginView, forgetView);
        mPswChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mUserPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mUserPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_right:
                Intent intent = new Intent(mContext, UserRegisterActivity.class);
                if (mTargetActivity != null) {
                    intent.putExtra(IntentConstant.TARGET_ACTIVITY, mTargetActivity.getSimpleName());
                }
                UmengUtils.onEvent(mContext, EventEnum.User_LoginPage_Regist_Onclick);
                jump(intent);
                break;
            case R.id.tv_login:

                String name = mUserNameEt.getText().toString().trim();
                String password = mUserPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    DialogUtils.showShortPromptToast(mContext, "号码不能为空,请重新输入");
                } else if (TextUtils.isEmpty(password)) {
                    DialogUtils.showShortPromptToast(mContext, "密码不能为空,请重新输入");
                } else if (password.length() < 6) {
                    DialogUtils.showShortPromptToast(mContext, "密码长度不能小于6位,手机号码格式有误,请重新输入");
                } else {
                    // 登录
                    mProgressDialog = DialogUtils.showProgressDialog(mContext, "正在登陆...");
                    new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.LOGIN,
                            NetConstant.getLoginParams(mContext, name, password), new LoginStepOneListener(new LoginStepTwoListener()));
                    UmengUtils.onEvent(mContext, EventEnum.UserLogIn);
                }
                break;
            case R.id.tv_qq_login:
                SocialPlatformLoginManager.getsInstance(mContext).tencentAuthAndLogIn(mActivity, mTargetActivity);
                UmengUtils.onEvent(mContext, EventEnum.UserQQLogIn);
                break;
            case R.id.tv_wx_login:
                SocialPlatformLoginManager.getsInstance(mContext).weixinAuthAndLogIn(mActivity, mTargetActivity);
                UmengUtils.onEvent(mContext, EventEnum.UserWXLogIn);
                break;
            case R.id.tv_forget_psw:
                jump(mContext, ForgetPswActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.ForgetPsw);
                break;
            default:
                super.onClick(view);
                break;
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

    /**
     * Step1 博宝登陆
     */
    private class LoginStepOneListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<LoginStepOneResponse> {

        private RequestCallBack<String> listener;

        private LoginStepOneListener(RequestCallBack<String> listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<LoginStepOneResponse> task = new StringToBeanTask<>(LoginStepOneResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            listener.onFailure(e, s);
        }

        @Override
        public void onConvertSuccess(LoginStepOneResponse response) {
            // 登录
            if (response == null || response.getData() == null) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (response != null) {
                    DialogUtils.showShortPromptToast(UserLogInActivity.this, response.getMessage());
                }
                return;
            }
            new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getLoginStepTwoParams(mContext,
                    response.getData()), listener);
        }

        @Override
        public void onConvertFailed() {
        }
    }

    /**
     * Step2 鉴宝登陆
     */
    private class LoginStepTwoListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<LoginStepTwoResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<LoginStepTwoResponse> task = new StringToBeanTask<>(LoginStepTwoResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, "登录失败");
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }

        @Override
        public void onConvertSuccess(LoginStepTwoResponse response) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            DialogUtils.showShortPromptToast(mContext, "登录成功");
            Intent intent = new Intent();
            //保存信息到本地
            UserInfoUtils.saveUserLoginInfo(mContext, response.getData());
            UserInfoUtils.saveCacheHeadImagePath(mContext, response.getData().getHeadimg());
            UserInfoUtils.setSocialLoginFlg(mContext, false);
            //登陆成功后，刷新信鸽推送的注册名
            XGPushManager.registerPush(mContext, StringUtils.getString("JQ", UserInfoUtils.getUserId(mContext)),
                    new XGPushCallback(mContext, TAG));
            if (mTargetActivity != null) {
                if (TextUtils.equals("OrderDetailActivity", mTargetActivity.getSimpleName())) {
                    finish();
                } else {
                    intent.setClass(mContext, mTargetActivity);
                    intent.putExtra(IntentConstant.IntentAction, IntentConstant.SubmitOrder);
                    jump(intent);
                }
            } else {
                if (!TextUtils.isEmpty(enrollId)){
                    intent.setClass(mContext, FindActivity.class);
                    intent.putExtra(AppConstant.INTENT_FRAGMENT_TYPE,2);
                    jump(intent);
                    return;
                }
                //跳转回到主页----"我的鉴宝"
                intent.setClass(mContext, MainActivity.class);
                intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_user);
                jump(intent);
            }

        }

        @Override
        public void onConvertFailed() {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            DialogUtils.showShortPromptToast(mContext, "登录失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialPlatformLoginManager.getsInstance(mContext).mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
