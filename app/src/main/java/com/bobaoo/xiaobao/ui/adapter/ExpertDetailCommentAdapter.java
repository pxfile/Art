package com.bobaoo.xiaobao.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.domain.ExpertDetailInfoData;
import com.bobaoo.xiaobao.ui.activity.OrderDetailActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class ExpertDetailCommentAdapter extends BaseAdapter implements View.OnClickListener {

    private List<ExpertDetailInfoData.DataEntity.CommentArrEntity> mList;
    private Context mContext;
    private String test;

    public ExpertDetailCommentAdapter(Context mContext) {
        mList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setData(List<ExpertDetailInfoData.DataEntity.CommentArrEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ExpertDetailInfoData.DataEntity.CommentArrEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(parent.getContext(), R.layout.list_item_expert_comment, null);
            holder.portrait = (SimpleDraweeView) convertView.findViewById(R.id.sdv_portrait);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.appraise = (TextView) convertView.findViewById(R.id.tv_appraise);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.mRbLabel = (RatingBar) convertView.findViewById(R.id.rb_label);
            holder.mRbLabelLL = (RelativeLayout) convertView.findViewById(R.id.rl_rb_label);
            holder.mRbLabelLL.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ExpertDetailInfoData.DataEntity.CommentArrEntity data = getItem(position);
        holder.portrait.setImageURI(Uri.parse(data.getHead_img()));
        holder.name.setText(data.getNikename());
        holder.time.setText(data.getAddtime());
//        holder.appraise.setText(data.getZan());
        holder.mRbLabel.setRating(data.getStars());
        holder.content.setText(data.getContext());
        convertView.setTag(R.id.tag_order_id, data.getGoods_id());
        test = data.getGoods_id();
//        holder.mNoEvaluate.setVisibility(!data.getContext().equals("") ? View.GONE : View.VISIBLE);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra(IntentConstant.ORDER_ID, (String) v.getTag(R.id.tag_order_id));
        ActivityUtils.jump(mContext, intent);
    }

    private class Holder {
        private SimpleDraweeView portrait;
        private TextView name;
        private TextView time;
        private TextView appraise;
        private TextView content;
        private RatingBar mRbLabel;
        private RelativeLayout mRbLabelLL;
    }
}
