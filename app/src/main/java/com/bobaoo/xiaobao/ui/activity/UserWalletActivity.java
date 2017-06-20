package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.utils.UmengUtils;

import java.util.HashMap;

/**
 * Created by you on 2015/6/16.
 */
public class UserWalletActivity extends BaseActivity{
    private String mUserId;
    private String mBanlance;
    private String mScore;

    private View mBanlanceEntry;
    private View mScoreEntry;
    private View mRechargeEntry;
//    private View mPayRecordEntry;
    private View mRechargeRecordEntry;
    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra(IntentConstant.USER_ID);
        mBanlance = intent.getStringExtra(IntentConstant.USER_ACCOUNT_BALANCE);
        mScore = intent.getStringExtra(IntentConstant.USER_SCORE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_wallet;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("我的钱包");
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mBanlanceEntry = findViewById(R.id.rl_user_banlance);
        mScoreEntry = findViewById(R.id.rl_user_score);
        mRechargeEntry = findViewById(R.id.rl_user_recharge);
        mRechargeRecordEntry = findViewById(R.id.rl_user_recharge_record);
        setOnClickListener(mBanlanceEntry,mScoreEntry,mRechargeEntry,mRechargeRecordEntry);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        HashMap<String, String> map = new HashMap<>();
        switch (view.getId()){
            case R.id.rl_user_banlance:
                intent = new Intent(this, UserBanlanceActivity.class);
                intent.putExtra(IntentConstant.USER_ACCOUNT_BALANCE,mBanlance);
                intent.putExtra(IntentConstant.USER_ID,mUserId);
                startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID,mUserId);
                UmengUtils.onEvent(this, EventEnum.UserWalletPageBalance,map);
                break;
            case R.id.rl_user_score:
                intent = new Intent(this,UserScoreActivity.class);
                intent.putExtra(IntentConstant.USER_SCORE,mScore);
                startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID,mUserId);
                UmengUtils.onEvent(this, EventEnum.UserWalletPageScore,map);
                break;
            case R.id.rl_user_recharge:
                intent = new Intent(this,UserRechargeSelectActivity.class);
                intent.putExtra(IntentConstant.USER_ID,mUserId);
                startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID,mUserId);
                UmengUtils.onEvent(this, EventEnum.UserWalletPageRecharge,map);
                break;
            case R.id.rl_user_recharge_record:
                intent = new Intent(this,UserRechargeRecordActivity.class);
                intent.putExtra(IntentConstant.USER_ID,mUserId);
                startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID,mUserId);
                UmengUtils.onEvent(this, EventEnum.UserWalletPageRechargeRecord,map);
                break;
            case R.id.tv_back:
                finish();
                break;
            default:break;
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
