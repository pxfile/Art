package com.bobaoo.xiaobao.ui.fragment;

import android.view.View;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.InfoCollectionData;
import com.bobaoo.xiaobao.domain.TreasureCollectionData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.CollectionInfoAdapter;
import com.bobaoo.xiaobao.ui.adapter.CollectionTreasureAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * Created by you on 2015/6/2.
 */
public class CollectionFragment extends UserSubFragment {

    private List<TreasureCollectionData.DataEntity> mTreasureCollectionList;
    private List<InfoCollectionData.DataEntity> mInfoCollectionList;
    @Override
    protected void initData() {
        mCurrentPage = 1;
        switch (mType) {
            case 0:
                mAdapter = new CollectionTreasureAdapter();
                break;
            case 1:
                mAdapter = new CollectionInfoAdapter();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initContent() {
        initView();
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            if (mAdapter instanceof CollectionTreasureAdapter) {
                mListView.setOnItemClickListener((CollectionTreasureAdapter) mAdapter);
            }
            if (mAdapter instanceof CollectionInfoAdapter) {
                mListView.setOnItemClickListener((CollectionInfoAdapter) mAdapter);
            }
        }
    }

    @Override
    protected void loadData() {
        if (mCurrentPage == 1) {
            mLoadFailedUI.setVisibility(View.GONE);
        }
        // 请求数据
        if (mType == 0) {
            mHandlerList.add(new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                    NetConstant.getUserCollectionParams(mContext, mType, mCurrentPage), new TreasureCollectionListener()));
        }
        if (mType == 1) {
            mHandlerList.add(new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                    NetConstant.getUserCollectionParams(mContext, mType, mCurrentPage), new InfoCollectionListener()));
        }
    }

    @Override
    protected void attachData() {
        if (mAdapter instanceof CollectionTreasureAdapter) {
            if (mCurrentPage == 1) {
                if (mTreasureCollectionList != null) {
                    ((CollectionTreasureAdapter) mAdapter).setDataList(mTreasureCollectionList);
                    mIsNeedLoadData = false;
                }
            } else {
                if (mTreasureCollectionList != null) {
                    ((CollectionTreasureAdapter) mAdapter).addDataList(mTreasureCollectionList);
                    mIsNeedLoadData = false;
                }
            }
        }
        if (mAdapter instanceof CollectionInfoAdapter) {
            if (mCurrentPage == 1) {
                if (mInfoCollectionList != null) {
                    ((CollectionInfoAdapter) mAdapter).setDataList(mInfoCollectionList);
                    mIsNeedLoadData = false;
                }
            } else {
                if (mTreasureCollectionList != null) {
                    ((CollectionInfoAdapter) mAdapter).addDataList(mInfoCollectionList);
                    mIsNeedLoadData = false;
                }
            }
        }
        mSwipyRefreshLayout.setRefreshing(false);
    }

    private class TreasureCollectionListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<TreasureCollectionData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<TreasureCollectionData> task = new StringToBeanTask<>(TreasureCollectionData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mSwipyRefreshLayout.setRefreshing(false);
           if(mCurrentPage==1){
               mLoadingUI.setVisibility(View.GONE);
               handleLoadFailedUI();
           }
        }

        @Override
        public void onConvertSuccess(TreasureCollectionData response) {
            mTreasureCollectionList = response.getData();
            if (mCurrentPage == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (mTreasureCollectionList == null || mTreasureCollectionList.size() == 0) {
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

    private class InfoCollectionListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<InfoCollectionData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<InfoCollectionData> task = new StringToBeanTask<>(InfoCollectionData.class, this);
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
        public void onConvertSuccess(InfoCollectionData response) {
            mInfoCollectionList = response.getData();
            if (mCurrentPage == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (mInfoCollectionList == null || mInfoCollectionList.size() == 0) {
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
