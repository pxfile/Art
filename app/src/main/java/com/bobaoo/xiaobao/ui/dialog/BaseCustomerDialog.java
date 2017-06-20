package com.bobaoo.xiaobao.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public abstract class BaseCustomerDialog extends Dialog implements View.OnClickListener {

    public Context context;

    public BaseCustomerDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    protected abstract int setLayoutViewId();

    protected abstract void initTitle();

    protected abstract void initView();

    protected abstract void attachData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutViewId());
        setCanceledOnTouchOutside(false);
        initTitle();
        initView();
        attachData();
    }

    /**
     * 统一为各种view添加点击事件
     */
    protected void setOnClickListener(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * 跳转到activity
     */
    protected void jump(Intent intent) {
        context.startActivity(intent);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Class<?> targetClass) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        jump(intent);
    }

}
