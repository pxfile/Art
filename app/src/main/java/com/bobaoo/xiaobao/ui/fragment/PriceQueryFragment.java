package com.bobaoo.xiaobao.ui.fragment;
import android.view.View;

import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.PriceQueryData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.PriceQueryAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 2015/6/2.
 */
public class PriceQueryFragment extends UserSubFragment {

    private List<PriceQueryData.DataEntity> mQueryList = new ArrayList<>();

    PriceQueryAdapter mAdapter;
    @Override
    protected void initData() {
        mCurrentPage = 1;
        mAdapter = new PriceQueryAdapter(mType);
    }

    @Override
    protected void initContent() {
        initView();
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mAdapter);
        }
    }

    @Override
    protected void loadData() {
        if(mCurrentPage==1){
            mLoadFailedUI.setVisibility(View.GONE);
        }
        // 请求数据
        mHandlerList.add(new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                NetConstant.getUserPriceQueryParams(mContext, mType, mCurrentPage), new PriceQueryListener()));
    }

    @Override
    protected void attachData() {
        if (mCurrentPage == 1) {
            if (mQueryList != null) {
                mAdapter.setDataList(mQueryList);
                mQueryList.clear();
                mIsNeedLoadData = false;
            }
        } else {
            if (mQueryList != null) {
                mAdapter.addDataList(mQueryList);
                mQueryList.clear();
                mIsNeedLoadData = false;
            }
        }
        mSwipyRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPause() {
        mIsNeedLoadData = true;
        super.onPause();
    }

    private class PriceQueryListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<PriceQueryData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<PriceQueryData> task = new StringToBeanTask<>(PriceQueryData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mSwipyRefreshLayout.setRefreshing(false);
            if (mCurrentPage == 1) {
                mLoadingUI.setVisibility(View.GONE);
                handleLoadFailedUI();
            }
        }

        @Override
        public void onConvertSuccess(PriceQueryData response) {
            mQueryList = response.getData();
            if (mCurrentPage == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (mQueryList == null || mQueryList.size() == 0) {
                    handleNoDataUI();
                    return;
                }
                mLoadFailedUI.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
            attachData();
        }

        @Override
        public void onConvertFailed() {
            mSwipyRefreshLayout.setRefreshing(false);
            if (mCurrentPage == 1) {
                mLoadingUI.setVisibility(View.GONE);
                handleNoDataUI();
            }
        }
    }
}