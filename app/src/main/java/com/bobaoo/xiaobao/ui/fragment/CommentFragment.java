package com.bobaoo.xiaobao.ui.fragment;

import android.view.View;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.CommentReceiveData;
import com.bobaoo.xiaobao.domain.CommentSendData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.CommentReceiverAdapter;
import com.bobaoo.xiaobao.ui.adapter.CommentSendAdapter;
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
public class CommentFragment extends UserSubFragment {
    public static final int CommentSend = 0;
    public static final int CommentReceive = 1;

    public static final int INFO_DETAIL = 1;
    public static final int ORDER_DETAIL = 2;
    public static final int EXPERT_DETAIL = 3;

    private List<CommentReceiveData.DataEntity> mReceiveCommentList = new ArrayList<>();
    private List<CommentSendData.DataEntity> mSendCommentList = new ArrayList<>();

    @Override
    protected void initData() {
        mCurrentPage = 1;
        switch (mType) {
            case CommentSend:
                mAdapter = new CommentSendAdapter();
                break;
            case CommentReceive:
                mAdapter = new CommentReceiverAdapter();
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
            if (mAdapter instanceof CommentReceiverAdapter) {
                mListView.setOnItemClickListener((CommentReceiverAdapter) mAdapter);
            }
            if (mAdapter instanceof CommentSendAdapter) {
                mListView.setOnItemClickListener((CommentSendAdapter) mAdapter);
            }
        }
    }

    @Override
    protected void loadData() {
        if (mCurrentPage == 1) {
            mLoadFailedUI.setVisibility(View.GONE);
        }
        // 请求数据
        switch (mType) {
            case CommentSend:
                mHandlerList.add(new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                        NetConstant.getUserCommentParams(mContext, mType, mCurrentPage), new CommentSendListener()));
                break;
            case CommentReceive:
                mHandlerList.add(new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                        NetConstant.getUserCommentParams(mContext, mType, mCurrentPage), new ReceiverCommentListener()));
                break;
            default:
                break;
        }
    }

    @Override
    protected void attachData() {
        if (mAdapter instanceof CommentReceiverAdapter) {
            if (mCurrentPage == 1) {
                if (mReceiveCommentList != null) {
                    ((CommentReceiverAdapter) mAdapter).setDataList(mReceiveCommentList);
                    mIsNeedLoadData = false;
                }
            } else {
                if (mReceiveCommentList != null) {
                    ((CommentReceiverAdapter) mAdapter).addDataList(mReceiveCommentList);
                    mIsNeedLoadData = false;
                }
            }
        }
        if (mAdapter instanceof CommentSendAdapter) {
            if (mCurrentPage == 1) {
                if (mSendCommentList != null) {
                    ((CommentSendAdapter) mAdapter).setDataList(mSendCommentList);
                    mIsNeedLoadData = false;
                }
            } else {
                if (mReceiveCommentList != null) {
                    ((CommentSendAdapter) mAdapter).addDataList(mSendCommentList);
                    mIsNeedLoadData = false;
                }
            }
        }
        mSwipyRefreshLayout.setRefreshing(false);
    }

    private class ReceiverCommentListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<CommentReceiveData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<CommentReceiveData> task = new StringToBeanTask<>(CommentReceiveData.class, this);
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
        public void onConvertSuccess(CommentReceiveData response) {
            mReceiveCommentList = response.getData();
            if (mCurrentPage == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (mReceiveCommentList == null || mReceiveCommentList.size() == 0) {
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
           if(mCurrentPage==1){
               mLoadingUI.setVisibility(View.GONE);
               handleNoDataUI();
           }
        }
    }

    private class CommentSendListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<CommentSendData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<CommentSendData> task = new StringToBeanTask<>(CommentSendData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mSwipyRefreshLayout.setRefreshing(false);
            mLoadingUI.setVisibility(View.GONE);
            if(mCurrentPage==1){
                handleLoadFailedUI();
            }
        }

        @Override
        public void onConvertSuccess(CommentSendData response) {
            mSendCommentList = response.getData();
            if (mCurrentPage == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (mSendCommentList == null || mSendCommentList.size() == 0) {
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
