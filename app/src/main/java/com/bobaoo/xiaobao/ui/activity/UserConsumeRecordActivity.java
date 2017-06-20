package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.UserChargeRecordData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.utils.NetUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * Created by you on 2015/6/15.
 */
public class UserConsumeRecordActivity extends BaseActivity{
    private RecyclerView mRecordRecyclerView;
    private View mLoadFailedUI;
    private View mLoadingUI;
    private TextView mLoadFailedTipTv;
    private Button mLoadFailedBtn;
    private String mUserId;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra(IntentConstant.USER_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_recharge_record;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("我的消费记录");
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mRecordRecyclerView = (RecyclerView) findViewById(R.id.rcv_recharg_record);
        LinearLayoutManager lv = new LinearLayoutManager(this);
        mRecordRecyclerView.setLayoutManager(lv);

        mLoadingUI = findViewById(R.id.vg_loading);
        mLoadingUI.setVisibility(View.GONE);

        mLoadFailedUI = findViewById(R.id.vg_no_data);
        mLoadFailedUI.setVisibility(View.GONE);
        mLoadFailedTipTv = (TextView) mLoadFailedUI.findViewById(R.id.tv_no_data_tip);
        mLoadFailedBtn = (Button) mLoadFailedUI.findViewById(R.id.btn_no_data_action);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                super.onClick(view);
        }
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    private void startUserRechargeRecordRequest() {
        mLoadingUI.setVisibility(View.VISIBLE);
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getUserConsumeRecordParams(mContext), new UserConsumeRecordListener());
    }

    private void handleLoadFailedUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mRecordRecyclerView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText("获取充值记录失败");
        mLoadFailedBtn.setText("重试");
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserRechargeRecordRequest();
            }
        });
    }

    private void handleNoDataUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mRecordRecyclerView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText("您还没有消费记录");
        mLoadFailedBtn.setText("返回");
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void refreshData() {
        startUserRechargeRecordRequest();
    }

    class UserRechargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<UserChargeRecordData.DataEntity.OutEntity> mData;

        public UserRechargeAdapter(List<UserChargeRecordData.DataEntity.OutEntity> list){
            mData = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(UserConsumeRecordActivity.this).inflate(R.layout.item_score_record,null);
            return new RechargeRecordViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            UserChargeRecordData.DataEntity.OutEntity entity = mData.get(position);
            RechargeRecordViewHolder vh = (RechargeRecordViewHolder) holder;
            vh.tv_item_scrore_record_time.setText(entity.getCharge_time());
            vh.tv_item_scrore_record_num.setText(entity.getCharge_price());
            vh.tv_item_scrore_record_introduce.setText(entity.getType_name());
        }

        @Override
        public int getItemCount() {
            return mData == null?0:mData.size();
        }
    }

    class RechargeRecordViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_item_scrore_record_time;
        private TextView tv_item_scrore_record_num;
        private TextView tv_item_scrore_record_introduce;
        public RechargeRecordViewHolder(View itemView) {
            super(itemView);
            tv_item_scrore_record_time = (TextView) itemView.findViewById(R.id.tv_item_scrore_record_time);
            tv_item_scrore_record_num = (TextView) itemView.findViewById(R.id.tv_item_scrore_record_num);
            tv_item_scrore_record_introduce = (TextView) itemView.findViewById(R.id.tv_item_scrore_record_introduce);
        }
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

    private class UserConsumeRecordListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserChargeRecordData> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserChargeRecordData> task = new StringToBeanTask<>(UserChargeRecordData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mLoadingUI.setVisibility(View.GONE);
            handleLoadFailedUI();
        }

        @Override
        public void onConvertSuccess(UserChargeRecordData data) {
            mLoadingUI.setVisibility(View.GONE);
            if (data == null || data.isError()) {
                Toast.makeText(UserConsumeRecordActivity.this, "获取充值记录失败", Toast.LENGTH_SHORT).show();
                handleLoadFailedUI();
                return;
            }

            List<UserChargeRecordData.DataEntity.OutEntity> entities = data.getData().getOut();
            if (entities == null || entities.size() == 0) {
                handleNoDataUI();
            } else {
                mLoadFailedUI.setVisibility(View.GONE);
                mRecordRecyclerView.setVisibility(View.VISIBLE);
                UserRechargeAdapter adapter = new UserRechargeAdapter(entities);
                mRecordRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void onConvertFailed() {
            mLoadingUI.setVisibility(View.GONE);
            handleLoadFailedUI();
        }
    }
}
