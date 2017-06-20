package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by cm on 2015/6/15.
 * 用户余额查询页面
 */
public class UserBanlanceActivity extends BaseActivity{
    private RelativeLayout mGotoRechargeRl;
    private TextView mBalanceTv;
    private String mBalance;
    private String mUserId;
    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mBalance = intent.getStringExtra(IntentConstant.USER_ACCOUNT_BALANCE);
        mUserId = intent.getStringExtra(IntentConstant.USER_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_balance;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.user_banlance);
        setOnClickListener(backView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_recharge:
                Intent intent = new Intent(UserBanlanceActivity.this,UserRechargeSelectActivity.class);
                intent.putExtra(IntentConstant.USER_ID,mUserId);
                startActivity(intent);
                finish();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected void initContent() {
        mGotoRechargeRl = (RelativeLayout) findViewById(R.id.rl_recharge);
        mBalanceTv = (TextView) findViewById(R.id.tv_balance);
        mBalanceTv.setText(mBalance);
        setOnClickListener(mGotoRechargeRl);
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
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }
}
