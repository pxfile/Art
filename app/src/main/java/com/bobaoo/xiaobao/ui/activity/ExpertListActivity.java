package com.bobaoo.xiaobao.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.ExpertData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.ExpertAdapter;
import com.bobaoo.xiaobao.ui.widget.recycler.DividerItemDecoration;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

/**
 * Created by star on 15/6/9.
 */
public class ExpertListActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {

    private SwipyRefreshLayout mRefreshLayout;
    private RecyclerView mExpertView;

    private ExpertAdapter mExpertAdapter;

    private ExpertListListener mListener;

    private int mOrganizationId;
    private int mOrganizationName;
    private int mCurrentPage;

    @Override
    protected void getIntentData() {
        mOrganizationId = getIntent().getIntExtra(IntentConstant.ORGANIZATION_ID, -1);
        mOrganizationName = getIntent().getIntExtra(IntentConstant.ORGANIZATION_NAME, R.string.app_name);
    }

    @Override
    protected void initData() {
        mCurrentPage = 1;

        mExpertAdapter = new ExpertAdapter();

        mListener = new ExpertListListener();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_expert_list;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(mOrganizationName);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        // 下拉刷新
        mRefreshLayout = (SwipyRefreshLayout) mRootView.findViewById(R.id.srl_expert);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        // 专家列表
        mExpertView = (RecyclerView) mRootView.findViewById(R.id.rv_expert);
        // 设置纵向滚动
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mExpertView.setLayoutManager(manager);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mExpertView.setAdapter(mExpertAdapter);
        getData();
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    // 获取到专家列表数据，包括初始化、下拉刷新和上拉加载更多
    private void attachAdapterData(List<ExpertData.DataEntity> data) {
        if (mCurrentPage == 1) {
            mExpertAdapter.resetData(data);
            mExpertAdapter.setShowFooter(false);
            mRefreshLayout.setRefreshing(false);
        } else {
            mExpertAdapter.addData(data);
            mExpertAdapter.setShowFooter(false);
            mRefreshLayout.setRefreshing(false);
        }
    }

    private void getData() {
        if (mOrganizationId > 0) {
            // 请求机构专家列表数据
            new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                    NetConstant.getOrganizationExpertListParams(mContext, mCurrentPage, mOrganizationId), mListener);
        } else {
            // 请求关注专家列表数据
            new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                    NetConstant.getAttentionExpertListParams(mContext, mCurrentPage), mListener);
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
        if(swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP){
            //下拉刷新
            mCurrentPage = 1;
            getData();
        }else{
            mCurrentPage++;
            getData();
        }
    }

    /**
     * 请求专家列表数据
     */
    private class ExpertListListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<ExpertData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<ExpertData> task = new StringToBeanTask<>(ExpertData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            e.printStackTrace();
            mExpertAdapter.setShowFooter(false);
            mRefreshLayout.setRefreshing(false);
            DialogUtils.showShortPromptToast(mContext, getString(R.string.loading_failed));
        }

        @Override
        public void onConvertSuccess(ExpertData response) {
            attachAdapterData(response.getData());
        }

        @Override
        public void onConvertFailed() {
            mExpertAdapter.setShowFooter(false);
            mExpertAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            DialogUtils.showShortPromptToast(mContext, getString(R.string.no_more_data));
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
}
