package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.adapter.IdentifyTypeAdapter;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by star on 15/6/17.
 */
public class IdentifyTypeActivity extends BaseActivity {
    private TextView mHeaderBackView;
    private RecyclerView mRecycleView;
    private IdentifyTypeAdapter mAdapter;

    private int mIdentifyId;
    private String mIdentifyMethodSwitchInfos;
    private String mIdentifyMethodPrices;

    @Override
    protected void getIntentData() {
        mIdentifyMethodSwitchInfos = getIntent().getStringExtra(IntentConstant.IdentifyMethodInfo);
        mIdentifyMethodPrices = getIntent().getStringExtra(IntentConstant.IdentifyMethodPrices);
    }

    @Override
    protected void initData() {
        mIdentifyId = getIntent().getIntExtra(IntentConstant.IdentifyId, 1);
        mAdapter = new IdentifyTypeAdapter(this, mIdentifyMethodSwitchInfos, mIdentifyMethodPrices);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_identify_type;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        mHeaderBackView = (TextView) findViewById(R.id.tv_back);
        mHeaderBackView.setText("鉴定选项");
        setOnClickListener(mHeaderBackView);
    }

    @Override
    protected void initContent() {
        mRecycleView = (RecyclerView) findViewById(R.id.rv_type);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mRecycleView.setAdapter(mAdapter);
        // 设置纵向滚动
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_container:
                mIdentifyId = (int) view.getTag();
                finish();
                break;
            case R.id.tv_back:
                finish();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(IntentConstant.IdentifyType, mIdentifyId);
        setResult(RESULT_OK, intent);
        super.finish();
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
