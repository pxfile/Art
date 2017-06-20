package com.bobaoo.xiaobao.ui.fragment;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.NetUtils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

/**
 * Created by you on 2015/6/2.
 */
public abstract class UserSubFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {
    protected int mType;
    protected int mCurrentPage;

    protected SwipyRefreshLayout mSwipyRefreshLayout;

    protected BaseAdapter mAdapter;
    protected ListView mListView;

    protected View mLoadFailedUI;
    protected View mLoadingUI;
    protected TextView mLoadFailedTipTv;
    protected Button mLoadFailedBtn;

    @Override
    protected void getArgumentsData() {
        mType = getArguments().getInt(IntentConstant.TYPE);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_user_sub;
    }

    @Override
    protected void initContent() {
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
        if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
            //下拉刷新
            mCurrentPage = 1;
            loadData();
        } else {
            mCurrentPage++;
            loadData();
        }
    }

    protected void initView() {
        mLoadingUI = mRootView.findViewById(R.id.vg_loading);
        mLoadingUI.setVisibility(View.GONE);
        mLoadFailedUI = mRootView.findViewById(R.id.vg_no_data);
        mLoadFailedUI.setVisibility(View.GONE);
        mLoadFailedTipTv = (TextView) mLoadFailedUI.findViewById(R.id.tv_no_data_tip);
        mLoadFailedBtn = (Button) mLoadFailedUI.findViewById(R.id.btn_no_data_action);

        mSwipyRefreshLayout = (SwipyRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        mListView = (ListView) mRootView.findViewById(R.id.lv_collections);
    }

    protected void handleLoadFailedUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText(R.string.loading_failed);
        mLoadFailedBtn.setVisibility(View.VISIBLE);
        mLoadFailedBtn.setText(R.string.retry);
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingUI.setVisibility(View.VISIBLE);
                if (!NetUtils.isNetworkConnected(mContext)) {
                    mLoadingUI.setVisibility(View.GONE);
                    DialogUtils.showShortPromptToast(mContext, R.string.cannot_connect_network);
                } else {
                    loadData();
                }
            }
        });
    }

    protected void handleNoDataUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText(R.string.no_more_data);
        mLoadFailedBtn.setVisibility(View.INVISIBLE);
    }
}
