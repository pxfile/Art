package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by you on 2015/6/16.
 */
public class UserScoreActivity extends BaseActivity{
    private TextView mUserScoreTv;
    private String mUserScore;
    private View mUserScoreQueryRl;
    private View mSendInvitedCodeView;
    private View mScoreRulesView;
    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mUserScore = intent.getStringExtra(IntentConstant.USER_SCORE);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_user_integral_notes:
                Intent intent = new Intent(UserScoreActivity.this,UserScoreRecordActivity.class);
                startActivity(intent);
                UmengUtils.onEvent(mContext, EventEnum.User_Integral_Notes);
                break;
            case R.id.rl_send_invited_code:
                intent = new Intent(mContext,UserInputInvitedCode.class);
                jump(intent);
                UmengUtils.onEvent(mContext, EventEnum.UserInputInvitedCode);
                break;
            case R.id.rl_score_rules:
                intent = new Intent(mContext,WebViewActivity.class);
                intent.putExtra(IntentConstant.WEB_URL, NetConstant.SCORE_RULES_HOST);
                intent.putExtra(IntentConstant.WEB_TITLE, getString(R.string.score_regular));
                jump(intent);
                UmengUtils.onEvent(mContext, EventEnum.UserScoreRules);
                break;
            default:
                super.onClick(view);
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_score;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.user_score);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mUserScoreTv = (TextView) findViewById(R.id.tv_user_score);
        mUserScoreTv.setText(mUserScore);
        mUserScoreQueryRl = findViewById(R.id.rl_user_integral_notes);
        mSendInvitedCodeView = findViewById(R.id.rl_send_invited_code);
        mScoreRulesView = findViewById(R.id.rl_score_rules);
        setOnClickListener(mUserScoreQueryRl , mSendInvitedCodeView, mScoreRulesView);
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
