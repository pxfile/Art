package com.bobaoo.xiaobao.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.utils.StringUtils;

/**
 * Created by Administrator on 2015/10/8.
 */
public class EvaluateExpertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private String[] mData = new String[]{};
    private EditText mEt;
    public EvaluateExpertAdapter(Context mContext,EditText mEt) {
        this.mContext = mContext;
        this.mEt = mEt;
        mData = mContext.getResources().getStringArray(R.array.choice_evaluate);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_evaluate_expert,parent,false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder){
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.mTextView.setText(mData[position]);
            contentViewHolder.mTextView.setTag(mData[position]);
            contentViewHolder.mTextView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.evaluate_rv_tv){
            mEt.setText(StringUtils.getString(mEt.getText(),v.getTag()));
        }
    }
    private class ContentViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        View mView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.evaluate_rv_tv);
        }
    }
}
