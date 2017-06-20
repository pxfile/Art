package com.bobaoo.xiaobao.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.InfoListData;
import com.bobaoo.xiaobao.ui.activity.InfoDetailActivity;
import com.bobaoo.xiaobao.ui.fragment.InfoActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by star on 15/6/8.
 */
public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private ArrayList<InfoListData.DataEntity.ListEntity> mList;

    private boolean isShowFooter;

    public InfoAdapter() {
        mList = new ArrayList<>();
        isShowFooter = false;
    }

    public void addData(List<InfoListData.DataEntity.ListEntity> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void resetData(List<InfoListData.DataEntity.ListEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setShowFooter(boolean showFooter) {
        this.isShowFooter = showFooter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return Types.footer;
        } else {
            return Types.normal;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Types.normal:
                return new ContentHolder(View.inflate(parent.getContext(), R.layout.list_item_info, null));
            case Types.footer:
                return new FooterHolder(View.inflate(parent.getContext(), R.layout.list_item_footer_refresh, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) holder;
            InfoListData.DataEntity.ListEntity data = mList.get(position);
            contentHolder.pictureView.setImageURI(Uri.parse(data.getImg()));
            contentHolder.nameView.setText(data.getName());
            contentHolder.descView.setText(data.getContext());
            contentHolder.commentView.setText(StringUtils.getString(data.getComment(), "评论"));
            contentHolder.commentView.setTag(data.getId());
            contentHolder.containerView.setTag(data.getId());
            contentHolder.containerView.setOnClickListener(this);
            contentHolder.commentView.setOnClickListener(this);
        }
        if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            footerHolder.progressBar.getProgress();
        }
    }

    @Override
    public int getItemCount() {
        int count = mList.size();
        if (isShowFooter) {
            count++;
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.tv_comment) {
            intent = new Intent(v.getContext(), InfoDetailActivity.class);
            intent.putExtra(IntentConstant.INFO_ID, (String) v.getTag());
            intent.putExtra(InfoActivity.IS_CLICK_COMMENT, InfoActivity.IS_CLICK_COMMENT);
            ActivityUtils.jump(v.getContext(), intent);
        } else {
            intent = new Intent(v.getContext(), InfoDetailActivity.class);
            intent.putExtra(IntentConstant.INFO_ID, (String) v.getTag());
            intent.putExtra(InfoActivity.IS_CLICK_COMMENT, "");
            ActivityUtils.jump(v.getContext(), intent);
            HashMap<String, String> map = new HashMap<>();
            map.put(UmengConstants.KEY_INFO_PAGE_ITEM_ID, (String) v.getTag());
            UmengUtils.onEvent(v.getContext(), EventEnum.InfoPageItemClick, map);
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder {
        private View containerView;
        private SimpleDraweeView pictureView;
        private TextView nameView;
        private TextView descView;
        private TextView commentView;

        public ContentHolder(View itemView) {
            super(itemView);
            containerView = itemView;
            pictureView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_picture);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);
            descView = (TextView) itemView.findViewById(R.id.tv_desc);
            commentView = (TextView) itemView.findViewById(R.id.tv_comment);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public FooterHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb);
        }
    }

    private static class Types {
        private static final int normal = 2;
        private static final int footer = 3;
    }
}
