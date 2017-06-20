package com.bobaoo.xiaobao.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.InfoDetailData;
import com.bobaoo.xiaobao.ui.activity.InfoDetailActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

/**
 * Created by you on 2015/6/12.
 */
public class InfoDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private InfoDetailData mData;

    public void setData(InfoDetailData data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    /**
     * 获取消息的类型
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            // 用于显示时间
            return Types.title;
        }
        if (position == 1) {
            // 用于显示内容
            return Types.content;
        }
        if (position == 2) {
            // 用于显示相关阅读title
            return Types.title;
        }
        if (position > 2 && position < 3 + mData.getData().getRelated().size()) {
            // 相关阅读
            return Types.related;
        }
        if (position == 3 + mData.getData().getRelated().size()) {
            // 用于显示评论title
            return Types.title;
        }
        if (position > 3 + mData.getData().getRelated().size() &&
                position < 4 + mData.getData().getRelated().size() + mData.getData().getComment().size()) {
            // 评论
            return Types.comment;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mData != null && !TextUtils.isEmpty(mData.getData().getContext())) {
            // title + content
            count += 2;
        }
        if (mData != null && mData.getData().getRelated().size() > 0) {
            // title + size
            count += (1 + mData.getData().getRelated().size());
        }
        if (mData != null && mData.getData().getComment().size() > 0) {
            // title + size
            count += (1 + mData.getData().getComment().size());
        }
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Types.title:
                View view = View.inflate(parent.getContext(), R.layout.list_item_info_title, null);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (params == null){
                    params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                view.setLayoutParams(params);
                return new TitleHolder(view);
//            return new TitleHolder(View.inflate(parent.getContext(), R.layout.list_item_info_title, null));
            case Types.content:
                return new ContentHolder(View.inflate(parent.getContext(), R.layout.list_item_info_content, null));
            case Types.related:
                return new RelatedHolder(View.inflate(parent.getContext(), R.layout.list_item_info_related, null));
            case Types.comment:
                return new CommentHolder(View.inflate(parent.getContext(), R.layout.list_item_info_comment, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 填充title
        if (holder instanceof TitleHolder) {
            TitleHolder titleHolder = (TitleHolder) holder;
            if (position == 0) {
                titleHolder.mTitleView.setText(mData.getData().getAddtime());
                titleHolder.mTitleView.setBackgroundColor(titleHolder.mTitleView.getResources().getColor(R.color.gray13));
                titleHolder.mTitleView.setTextColor(titleHolder.mTitleView.getResources().getColor(R.color.gray4));
                titleHolder.mTitleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            if (position == 2) {
                titleHolder.mTitleView.setText("相关阅读");
                titleHolder.mTitleView.setBackgroundColor(titleHolder.mTitleView.getResources().getColor(R.color.gray13));
                titleHolder.mTitleView.setTextColor(titleHolder.mTitleView.getResources().getColor(R.color.gray5));
                titleHolder.mTitleView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_line_vertical, 0, 0, 0);
            }
            if (position == 3 + mData.getData().getRelated().size()) {
                titleHolder.mTitleView.setText("评论");
                titleHolder.mTitleView.setBackgroundColor(titleHolder.mTitleView.getResources().getColor(R.color.white));
                titleHolder.mTitleView.setTextColor(titleHolder.mTitleView.getResources().getColor(R.color.gray5));
                titleHolder.mTitleView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_line_vertical, 0, 0, 0);
            }
        }
        // 填充content
        if (holder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) holder;
            contentHolder.mContentView.loadDataWithBaseURL("about:blank", mData.getData().getContext(), "text/html", "utf-8", null);
        }
        // 填充相关阅读
        if (holder instanceof RelatedHolder) {
            RelatedHolder relatedHolder = (RelatedHolder) holder;
            final InfoDetailData.DataEntity.RelatedEntity relatedData = mData.getData().getRelated().get(position - 3);
            relatedHolder.mPictureView.setImageURI(Uri.parse(relatedData.getZx_img()));
            relatedHolder.mTitleView.setText(relatedData.getName());
            relatedHolder.mContentView.setText(relatedData.getContext());
            relatedHolder.mContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), InfoDetailActivity.class);
                    intent.putExtra(IntentConstant.INFO_ID, relatedData.getId());
                    ActivityUtils.jump(v.getContext(), intent);
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put(UmengConstants.KEY_INFO_DETAIL_PAGE_ITEM_ID,relatedData.getId());
                    UmengUtils.onEvent(v.getContext(), EventEnum.InfoPageDetailRelatedItemClick,map);
                }
            });
        }
        // 填充评论
        if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            InfoDetailData.DataEntity.CommentEntity commentData =
                    mData.getData().getComment().get(position - 4 - mData.getData().getRelated().size());
            commentHolder.mPortraitView.setImageURI(Uri.parse(commentData.getHead_img()));
            commentHolder.mTitleView.setText(commentData.getUser_name());
            commentHolder.mDateView.setText(commentData.getAddtime());
            commentHolder.mLevelView.setText(commentData.getLou());
            commentHolder.mContentView.setText(commentData.getContext());
        }
    }

    private class TitleHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;

        private TitleHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder {
        private WebView mContentView;

        private ContentHolder(View itemView) {
            super(itemView);
            mContentView = (WebView) itemView.findViewById(R.id.wv_content);
        }
    }

    private class RelatedHolder extends RecyclerView.ViewHolder {
        private View mContainerView;
        private SimpleDraweeView mPictureView;
        private TextView mTitleView;
        private TextView mContentView;

        private RelatedHolder(View itemView) {
            super(itemView);
            mContainerView = itemView;
            mPictureView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_picture);
            mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
            mContentView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private class CommentHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView mPortraitView;
        private TextView mTitleView;
        private TextView mDateView;
        private TextView mLevelView;
        private TextView mContentView;

        private CommentHolder(View itemView) {
            super(itemView);
            mPortraitView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_portrait);
            mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
            mDateView = (TextView) itemView.findViewById(R.id.tv_date);
            mLevelView = (TextView) itemView.findViewById(R.id.tv_level);
            mContentView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private static class Types {
        private static final int title = 1;
        private static final int content = 2;
        private static final int related = 3;
        private static final int comment = 4;
    }

}
