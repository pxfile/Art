package com.bobaoo.xiaobao.ui.activity;


import android.content.Intent;
import android.view.View;

import com.bobaoo.xiaobao.R;

/**
 * Created by kakaxicm on 2015/12/15.
 */
public class PaySuccessActivity extends BaseActivity {
    private View mIdentifyView;
    private View mInviteFriendView;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_pay_success;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initContent() {
        mIdentifyView = findViewById(R.id.tv_continue_identify);
        mInviteFriendView = findViewById(R.id.tv_invite_friend);
        setOnClickListener(mIdentifyView, mInviteFriendView);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_continue_identify:
                intent = new Intent(mContext, MainActivity.class);
                jump(intent);
                finish();
                break;
            case R.id.tv_invite_friend:
                intent = new Intent(mContext, InviteFriendActivity.class);
                jump(intent);
                finish();
                break;
            default:
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
}
