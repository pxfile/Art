package com.bobaoo.xiaobao.ui.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.domain.OrderDetailData;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 15/6/15.
 */
public class OrderDetailCommentAdapter extends BaseAdapter  implements View.OnClickListener{
    public interface OnReplyActionListener {
        void onReplyAction(String replyId, String replyUserNickerName);//要回复的评论id, 回复的用户名称
    }
    private OnReplyActionListener mOnReplyActionListener;

    public void setOnReplyActionListener(OnReplyActionListener listener){
        mOnReplyActionListener = listener;
    }

    private List<OrderDetailData.DataEntity.CommentEntity> mList;

    public OrderDetailCommentAdapter() {
        mList = new ArrayList<>();
    }

    public void setData(List<OrderDetailData.DataEntity.CommentEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public OrderDetailData.DataEntity.CommentEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(parent.getContext(), R.layout.list_item_order_comment, null);
            holder.portrait = (SimpleDraweeView) convertView.findViewById(R.id.sdv_portrait);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.appraise = (TextView) convertView.findViewById(R.id.tv_appraise);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.reply = (TextView) convertView.findViewById(R.id.tv_action_reply);
            holder.replyContent = (TextView) convertView.findViewById(R.id.tv_reply_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        OrderDetailData.DataEntity.CommentEntity data = getItem(position);
        holder.portrait.setImageURI(Uri.parse(data.getHead_img()));
        holder.name.setText(data.getNikename());
        holder.time.setText(data.getAddtime());
        holder.appraise.setText(data.getZan());
        holder.content.setText(data.getContext());
        if(!UserInfoUtils.checkUserLogin(convertView.getContext())) {
            holder.reply.setVisibility(View.VISIBLE);
            holder.reply.setTag(data);//回复
            holder.reply.setOnClickListener(this);
        } else {
            //回复按钮处理
            if(TextUtils.equals(data.getUser_id(), UserInfoUtils.getUserId(convertView.getContext())) || TextUtils.equals(data.getIs_reply(), "0")) {
                holder.reply.setVisibility(View.GONE);
            } else {
                holder.reply.setVisibility(View.VISIBLE);
                holder.reply.setTag(data);//回复
                holder.reply.setOnClickListener(this);
            }
        }

        //回复内容处理
        if(data.getReply() == null || TextUtils.isEmpty(data.getReply().getContext())) {
            holder.replyContent.setVisibility(View.GONE);
        } else {
            holder.replyContent.setVisibility(View.VISIBLE);
            OrderDetailData.DataEntity.CommentEntity.ReplyEntity replyEntity = data.getReply();
            String userName = replyEntity.getNikename();
            if(TextUtils.isEmpty(userName)) {
                userName = replyEntity.getUser_name();
            }
            String replyContent = StringUtils.getString(userName,":", replyEntity.getContext());
            holder.replyContent.setText(replyContent);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_action_reply:
                OrderDetailData.DataEntity.CommentEntity data = (OrderDetailData.DataEntity.CommentEntity) v.getTag();
                if(mOnReplyActionListener != null) {
                    mOnReplyActionListener.onReplyAction(data.getId(), data.getUser_name());
                }
                break;
            default:
                break;
        }
    }

    private class Holder {
        private SimpleDraweeView portrait;
        private TextView name;
        private TextView time;
        private TextView appraise;
        private TextView content;
        private TextView reply;
        private TextView replyContent;
    }
}
