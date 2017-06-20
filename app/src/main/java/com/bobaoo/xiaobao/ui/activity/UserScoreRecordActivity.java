package com.bobaoo.xiaobao.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.UserScoreRecordResponse;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 2015/6/15.
 */
public class UserScoreRecordActivity extends BaseActivity {

    private RecyclerView mScoreRecordRcv;
    private List<UserScoreRecordResponse.DataEntity> mData = new ArrayList<>();
    private UserScoreAdapter mAdapter;
    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_score_record;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("积分记录");
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mScoreRecordRcv = (RecyclerView) findViewById(R.id.rcv_score_record);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
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
        startUserScoreRecordRequest();
    }

    private void startUserScoreRecordRequest() {
        new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getUserScoreRecordParams(mContext), new UserScoreHistoryListener());
    }

    class UserScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(UserScoreRecordActivity.this).inflate(R.layout.item_score_record, null);
            return new ScoreViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ScoreViewHolder vh = (ScoreViewHolder) holder;
            UserScoreRecordResponse.DataEntity entity = mData.get(position);
            vh.tv_item_scrore_record_introduce.setText(entity.getObtain());
            vh.tv_item_scrore_record_time.setText(entity.getAddtime());
            String state = entity.getState();
            if("1".equals(state)) {
                vh.tv_item_scrore_record_num.setText("-" + entity.getNum());
                vh.tv_item_scrore_record_num.setTextColor(Color.BLACK);
            } else {
                vh.tv_item_scrore_record_num.setText("+" + entity.getNum());
                vh.tv_item_scrore_record_num.setTextColor(Color.RED);
            }

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    class ScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_scrore_record_time;
        private TextView tv_item_scrore_record_num;
        private TextView tv_item_scrore_record_introduce;

        public ScoreViewHolder(View itemView) {
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
    
    private class UserScoreHistoryListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserScoreRecordResponse> {

        @Override
        public void onConvertSuccess(UserScoreRecordResponse response) {
            if (response == null) {
                Toast.makeText(UserScoreRecordActivity.this, "获取积分记录失败", Toast.LENGTH_SHORT).show();
                return;
            }
             mData = response.getData();
            //适配器
            mAdapter = new UserScoreAdapter();
            mScoreRecordRcv.setAdapter(mAdapter);
            LinearLayoutManager lm = new LinearLayoutManager(mContext);
            mScoreRecordRcv.setLayoutManager(lm);
        }

        @Override
        public void onConvertFailed() {

        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserScoreRecordResponse> task = new StringToBeanTask<>(UserScoreRecordResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            Toast.makeText(UserScoreRecordActivity.this, "获取积分记录失败", Toast.LENGTH_SHORT).show();
        }
    }
}
