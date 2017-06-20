package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by you on 2015/6/16.
 */
public class UserRechargeFailTipActivity extends BaseActivity{
    private Button mContactUsBtn;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_recharge_fail;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("我要充值");
        setOnClickListener(backView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_contact_us:
                //todo 联系我们
                Intent intent = new Intent(UserRechargeFailTipActivity.this,ContactUsActivity.class);
                startActivity(intent);
                break;
            default: super.onClick(view);
        }
    }

    @Override
    protected void initContent() {
        mContactUsBtn = (Button) findViewById(R.id.btn_contact_us);
        setOnClickListener(mContactUsBtn);
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
