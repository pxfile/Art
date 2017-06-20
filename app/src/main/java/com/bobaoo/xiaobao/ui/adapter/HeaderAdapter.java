package com.bobaoo.xiaobao.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bobaoo.xiaobao.listener.ISurpportHeadView;

/**
 * Created by star on 16/1/8.
 */
public class HeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ISurpportHeadView {
    protected final int TYPE_HEADER = Integer.MIN_VALUE;
    protected View mHeadView;

    protected RecyclerView.Adapter<RecyclerView.ViewHolder> mProxyAdapter;//HeaderAdapter代理的adapter

    public HeaderAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mProxyAdapter = adapter;
        registerAdapterObserver();
    }

    protected void registerAdapterObserver() {
        if(mProxyAdapter != null) {
            this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                @Override
                public void onChanged() {
                    super.onChanged();
                    mProxyAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeadView == null) {
            return mProxyAdapter.getItemViewType(position);//如果没设置headview, 这走原adapter的方法
        }

        if(position == 0) {//设置了headview,第一个位置为head
            return TYPE_HEADER;
        }

        return mProxyAdapter.getItemViewType(position - 1);//从1开始，走原adapter的方法
    }

    @Override
    public int getItemCount() {
        return mHeadView == null ? mProxyAdapter.getItemCount() : mProxyAdapter.getItemCount() + 1;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            return new RecyclerView.ViewHolder(mHeadView){};
        }
        return mProxyAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) {
            return;
        }

        final int pos = getRealPosition(holder);
        mProxyAdapter.onBindViewHolder(holder, pos);
    }


    /**
     * 将headview强制加到目标的adater上
     */
    @Override
    public void bindHeadView(View headView) {
        if(headView != null) {
            mHeadView = headView;
            notifyItemInserted(0);
        }
    }

    /**
     * 一下方法是代理notify
     */
    public void proxyNotifyDataSetChanged() {
        mProxyAdapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    //传入的位置是真实数据的位置
    public void proxyNotifyItemChanged(int position) {
        mProxyAdapter.notifyItemChanged(position);
        notifyItemChanged(position+1);
    }

    public void proxyNotifyItemInserted(int position) {
        mProxyAdapter.notifyItemInserted(position);
        notifyItemInserted(position+1);
    }

    public void proxyNotifyItemRemoved(int position) {
        mProxyAdapter.notifyItemRemoved(position);
        notifyItemRemoved(position+1);
    }

    public void proxyNotifyItemRangeChanged(int startPosition, int itemCount) {
        mProxyAdapter.notifyItemRangeChanged(startPosition, itemCount);
        notifyItemRangeChanged(startPosition+1,itemCount+1);
    }

    public void proxyNotifyItemRangeInserted(int startPosition, int itemCount) {
        mProxyAdapter.notifyItemRangeInserted(startPosition, itemCount);
        notifyItemRangeInserted(startPosition + 1, itemCount + 1);
    }

    public void proxyNotifyItemRangeRemoved(int startPosition, int itemCount) {
        mProxyAdapter.notifyItemRangeRemoved(startPosition - 1, itemCount - 1);
        notifyItemRangeRemoved(startPosition, itemCount);
    }

    // 获取正事数据的索引
    protected int getRealPosition(RecyclerView.ViewHolder holder) {
        int itemPos = holder.getLayoutPosition();
        return mHeadView == null ? itemPos : itemPos - 1;
    }
}
