package com.bobaoo.xiaobao.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.ExpertData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.ExpertAdapter;
import com.bobaoo.xiaobao.ui.adapter.OrganizationAdapter;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by star on 15/5/29.
 */
public class ExpertFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ToggleButton.OnToggleChanged {
    private RecyclerView mExpertView;
    private SwipeRefreshLayout mRefreshLayout;

    private ExpertAdapter mExpertAdapter;
    private List<ExpertData.DataEntity> mList;

    private int mCurrentPage;
    private boolean mIsOnlineExpert;

    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
        mCurrentPage = 1;
        mIsOnlineExpert = false;
        mExpertAdapter = new ExpertAdapter(new OrganizationAdapter(), this);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_expert;
    }

    @Override
    protected void initContent() {
        // 下拉刷新
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.srl_expert);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        // 专家列表
        mExpertView = (RecyclerView) mRootView.findViewById(R.id.rv_expert);
        // 设置纵向滚动
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mExpertView.setLayoutManager(manager);
        // 添加上拉加载更多
        mExpertView.addOnScrollListener(new ExpertScrollListener(manager));
        // 设置adapter
        mExpertView.setAdapter(mExpertAdapter);
    }

    @Override
    protected void loadData() {
        // 请求专家列表数据
        mHandlerList.add(new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                mIsOnlineExpert ? NetConstant.getOnlineExpertListParams(mContext, mCurrentPage) :
                        NetConstant.getOrganizationExpertListParams(mContext, mCurrentPage), new ExpertListListener()));
    }

    @Override
    protected void attachData() {
        if (mCurrentPage == 1) {
            if (mList != null) {
                mExpertAdapter.resetData(mList);
                mIsNeedLoadData = false;
            }
            mExpertAdapter.setShowFooter(true);
            mRefreshLayout.setRefreshing(false);
        } else {
            if (mList != null) {
                mExpertAdapter.addData(mList);
                mIsNeedLoadData = false;
            }
            mExpertAdapter.setShowFooter(mList != null && mList.size() != 0);
        }
    }

    @Override
    // 下拉刷新
    public void onRefresh() {
        mCurrentPage = 1;
        loadData();
    }

    @Override
    public void onToggle(boolean on) {
        mCurrentPage = 1;
        mIsOnlineExpert = on;
        loadData();
        HashMap<String,String> map = new HashMap<>();
        map.put(UmengConstants.KEY_EXPERT_ONLINE_TOGGLE,on+"");
        UmengUtils.onEvent(mContext, EventEnum.ExpertPageOnlineToggleClick,map);
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
            if (getActivity() != null) {
                if(mList != null){
                    mList.clear();
                }
                attachData();
            }
        }

        @Override
        public void onConvertSuccess(ExpertData response) {
            if (getActivity() != null) {
                mList = new ArrayList<>();
                mList = response.getData();
                attachData();
            }
        }

        @Override
        public void onConvertFailed() {
            if (getActivity() != null) {
                mList.clear();
                attachData();
            }
        }
    }

    /**
     * 用于判断滑动到底部自动加载更多
     */
    private class ExpertScrollListener extends RecyclerView.OnScrollListener {

        private int lastVisibleItem;
        private LinearLayoutManager manager;

        private ExpertScrollListener(LinearLayoutManager manager) {
            this.manager = manager;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 3 >= mExpertAdapter.getItemCount()) {
                mCurrentPage++;
                loadData();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = manager.findLastVisibleItemPosition();
        }
    }
}
