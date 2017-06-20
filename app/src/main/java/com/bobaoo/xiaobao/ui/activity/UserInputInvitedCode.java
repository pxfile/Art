package com.bobaoo.xiaobao.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.InviteCodeResponse;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Administrator on 2015/8/21.
 */
public class UserInputInvitedCode extends BaseActivity {

    private EditText mInvitedEt;
    private Button mSubmitBtn;
    private String mInvitedContent;
    private ProgressDialog mProgressDialog;
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
        titleView.setText(getString(R.string.get_score_by_send_invited_code));
        titleView.setTextColor(getResources().getColor(R.color.gray5));
        setOnClickListener(backView);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_input_invited_code;
    }

    @Override
    protected void initContent() {
        mInvitedEt = (EditText) findViewById(R.id.et_invited_code);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        setOnClickListener(mSubmitBtn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                mInvitedContent = mInvitedEt.getText().toString().trim();
                if(TextUtils.isEmpty(mInvitedContent)){
                    DialogUtils.showShortPromptToast(mContext, R.string.please_input_invite_code);
                }else{
                    if(mProgressDialog == null){
                        mProgressDialog = DialogUtils.showProgressDialog(mContext);
                    }
                    new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getInvitedCodeParams(mContext, mInvitedContent), new InvitedCodeScore());
                }
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private class InvitedCodeScore extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<InviteCodeResponse>{

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String response = responseInfo.result;
            StringToBeanTask<InviteCodeResponse> task = new StringToBeanTask<>(InviteCodeResponse.class,this);
            task.execute(response);
        }

        @Override
        public void onFailure(HttpException e, String s) {
//             对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            DialogUtils.showShortPromptToast(mContext, R.string.send_invited_code_failed);
        }

        @Override
        public void onConvertSuccess(InviteCodeResponse response) {
            // 对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            if(response == null){
                DialogUtils.showShortPromptToast(mContext, R.string.send_invited_code_failed);
            }else{
                DialogUtils.showShortPromptToast(mContext,response.getMessage());
                finish();
            }

        }

        @Override
        public void onConvertFailed() {
            // 对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            DialogUtils.showShortPromptToast(mContext, R.string.send_invited_code_failed);
        }
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void refreshData() {

    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void initData() {

    }
}
