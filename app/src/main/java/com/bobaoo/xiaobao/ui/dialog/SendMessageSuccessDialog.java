package com.bobaoo.xiaobao.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;

public class SendMessageSuccessDialog extends BaseCustomerDialog {
    private TextView mDesView;
    private TextView mOkView;

    private Context mContext;
    private String mMobile;
    private String mCount;

    public SendMessageSuccessDialog(Context context, String mobile, String count) {
        super(context, R.style.CustomDialog);
        mContext = context;
        mMobile = mobile;
        mCount = count;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_send_message_success;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.send_message_success);
    }

    @Override
    protected void initView() {
        mDesView = (TextView) findViewById(R.id.tv_send_message_des);
        mOkView = (TextView) findViewById(R.id.tv_ok);
        setOnClickListener(mOkView);
    }

    @Override
    protected void attachData() {
        String des = String.format("已发送至%s，还剩余%s次发送机会，请在活动现场出示该短信。", mMobile, mCount);
        mDesView.setText(des);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                dismiss();
                break;
            default:
                break;
        }
    }
}
