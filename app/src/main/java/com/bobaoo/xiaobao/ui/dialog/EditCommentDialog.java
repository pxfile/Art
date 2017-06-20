package com.bobaoo.xiaobao.ui.dialog;

import android.content.Context;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;

/**
 * Created by you on 2015/6/1.
 */
public class EditCommentDialog extends BaseCustomerDialog{

    private EditText mEt;
    private ImageView mConfirmBtn;
    private TextView mTitleTv;
    public interface OnConfirmListener{
        void onConfirm();
    }

    public void setMyConfirmListener(OnConfirmListener mConfirmListener) {
        this.mConfirmListener = mConfirmListener;
    }

    private OnConfirmListener mConfirmListener;

    public EditCommentDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_edit_comment;
    }

    @Override
    protected void initTitle() {}

    @Override
    protected void initView() {
        mConfirmBtn = (ImageView) findViewById(R.id.tv_commit);
        mEt = (EditText) findViewById(R.id.et_comment);
        setCanceledOnTouchOutside(true);
        setOnClickListener(mConfirmBtn);
    }

    public void setTitleUI(String title){
    }

    @Override
    protected void attachData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_commit:
                if(mConfirmListener != null){
                    mConfirmListener.onConfirm();
                }
                dismiss();
                break;
        }
    }


    public String getContent() {
        return mEt.getText().toString().trim();
    }

    public void setContent(String mContent) {

        mEt.setText(mContent.trim());
    }
}
