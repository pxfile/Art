package com.bobaoo.xiaobao.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.OrderDetailData;
import com.bobaoo.xiaobao.ui.activity.ExpertDetailActivity;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by star on 15/6/15.
 */
public class OrderDetailExpertAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private List<OrderDetailData.DataEntity.GoodsEntity.GroupReportEntity> mList;

    public OrderDetailExpertAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    public void setData(List<OrderDetailData.DataEntity.GoodsEntity.GroupReportEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public OrderDetailData.DataEntity.GoodsEntity.GroupReportEntity getItem(int position) {
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
            convertView = View.inflate(parent.getContext(), R.layout.list_item_order_expert, null);
            holder.itemContainer = convertView.findViewById(R.id.ll_item_click_rec);
            holder.portrait = (SimpleDraweeView) convertView.findViewById(R.id.sdv_portrait);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.level = (TextView) convertView.findViewById(R.id.tv_level);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        OrderDetailData.DataEntity.GoodsEntity.GroupReportEntity data = getItem(position);
        holder.portrait.setImageURI(Uri.parse(data.getHead()));
        holder.name.setText(data.getName());
        holder.level.setText(AppConstant.getIdentifyType(data.getType()));
        holder.content.setText(data.getReport());
        holder.itemContainer.setTag(position);
        if(position == getCount() - 1){//最后一个为专家团意见汇总
            holder.itemContainer.setOnClickListener(null);
        }else {
            holder.itemContainer.setOnClickListener(this);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_item_click_rec:
                int position = (int) v.getTag();
                OrderDetailData.DataEntity.GoodsEntity.GroupReportEntity expert = getItem(position);
                String state = expert.getState();
                if (state.equals("1")) {
                    DialogUtils.showShortPromptToast(mContext, R.string.no_expert_exist);
                    return;
                }
                Intent intent = new Intent(mContext, ExpertDetailActivity.class);
                intent.putExtra(IntentConstant.EXPERT_ID, expert.getEid());
                mContext.startActivity(intent);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(UmengConstants.KEY_ORDER_DETAIL_EXPERT_ID, expert.getEid());
                UmengUtils.onEvent(mContext, EventEnum.OrderDetailExpertClick, hashMap);
                break;
            default:
                break;
        }
    }

    private class Holder {
        private View itemContainer;
        private SimpleDraweeView portrait;
        private TextView name;
        private TextView level;
        private TextView content;
    }
}
