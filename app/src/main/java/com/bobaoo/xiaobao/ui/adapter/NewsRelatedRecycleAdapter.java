package com.bobaoo.xiaobao.ui.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.domain.InfoDetailData;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 2015/6/12.
 */
public class NewsRelatedRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public List<InfoDetailData.DataEntity.RelatedEntity> mDatas;

    public NewsRelatedRecycleAdapter(){
        mDatas = new ArrayList<>();
    }

    public void setData(List<InfoDetailData.DataEntity.RelatedEntity> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.e("NewsRelatedAdapter","onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_related_item,null);
        NewsRelatedItemViewHolder holder = new NewsRelatedItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Log.e("NewsRelatedAdapter","name:"+i);
        NewsRelatedItemViewHolder holder = (NewsRelatedItemViewHolder) viewHolder;
        InfoDetailData.DataEntity.RelatedEntity relatedEntity = mDatas.get(i);
        holder.mTitleTv.setText(relatedEntity.getName());
        holder.mContextTv.setText(relatedEntity.getContext());
        holder.mSdv.setImageURI(Uri.parse(relatedEntity.getZx_img()));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

   private class NewsRelatedItemViewHolder extends RecyclerView.ViewHolder{
       public SimpleDraweeView mSdv;
       public TextView mTitleTv;
       public TextView mContextTv;
       public NewsRelatedItemViewHolder(View itemView) {
           super(itemView);
           mSdv = (SimpleDraweeView) itemView.findViewById(R.id.news_related_item_img);
           mTitleTv = (TextView) itemView.findViewById(R.id.tv_news_related_item_title);
           mContextTv = (TextView) itemView.findViewById(R.id.tv_news_related_item_context);
       }
   }
}
