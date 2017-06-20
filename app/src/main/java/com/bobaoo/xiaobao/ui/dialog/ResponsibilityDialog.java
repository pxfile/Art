package com.bobaoo.xiaobao.ui.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;

public class ResponsibilityDialog extends BaseCustomerDialog implements CompoundButton.OnCheckedChangeListener {
    private TextView mContentView;
    private CheckBox mCheckBox;
    private TextView mEnterView;
    private View.OnClickListener mListener;

    public ResponsibilityDialog(Context context, View.OnClickListener listener) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
        mListener = listener;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_responsibility;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("免责声明");
    }

    @Override
    protected void initView() {
        mContentView = (TextView) findViewById(R.id.tv_content);
        mContentView.setMovementMethod(ScrollingMovementMethod.getInstance());

        mCheckBox = (CheckBox) findViewById(R.id.cb_agree);
        mCheckBox.setOnCheckedChangeListener(this);

        mEnterView = (TextView) findViewById(R.id.tv_ok);
        mEnterView.setText("确定");
        setOnClickListener(mEnterView);
        mEnterView.setEnabled(false);
    }

    @Override
    protected void attachData() {
        mContentView.setText("手机鉴宝目前只是提供鉴定服务、提供交流的平台，不提供任何担保交易服务。用户针对本平台上展示的藏品而自行发起的交易行为均属双方私人行为，与手机鉴宝无关。对于交易所产生的任何问题手机鉴宝无需承担任何责");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if (mCheckBox.isChecked()) {
                    mListener.onClick(view);
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mEnterView.setEnabled(isChecked);
    }
}
