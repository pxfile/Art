package com.bobaoo.xiaobao.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;

public class PayAlertDialog extends BaseCustomerDialog {
    private TextView mDesView;
    private TextView mOkView;
    private TextView mCancelView;

    private int mPosition;
    private String mTitle;
    private String mContent;

    private View.OnClickListener mOkListener;

    public PayAlertDialog(Context context, String title, String content, int pos, View.OnClickListener okListener) {
        super(context, R.style.CustomDialog);
        mTitle = title;
        mContent = content;
        mPosition = pos;
        mOkListener = okListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_pay_alert;
    }

    @Override
    protected void initTitle() {
        TextView mTitleView = (TextView) findViewById(R.id.tv_title);
        mTitleView.setText(mTitle);
    }

    @Override
    protected void initView() {
        mDesView = (TextView) findViewById(R.id.tv_content);
        mCancelView = (TextView) findViewById(R.id.tv_cancel);
        mOkView = (TextView) findViewById(R.id.tv_ok);
        mOkView.setTag(mPosition);
        mOkView.setOnClickListener(mOkListener);
        setOnClickListener(mCancelView);
        setCancelable(false);
    }

    @Override
    protected void attachData() {
        mDesView.setText(mContent);
    }
}
