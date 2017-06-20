package com.bobaoo.xiaobao.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.HomeOrderResponse;
import com.bobaoo.xiaobao.ui.activity.OrderDetailActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.SizeUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UriUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by star on 15/6/4.
 */


public class HomeOrderAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomeOrderResponse.DataEntity> mList;
    private int mPictureWidth;

    public HomeOrderAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
        mPictureWidth = (int) ((SizeUtils.getScreenWidth(mContext) - SizeUtils.dp2Px(mContext.getResources(), 20.0f)) / 2);
    }

    public void addDataList(List<HomeOrderResponse.DataEntity> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setDataList(List<HomeOrderResponse.DataEntity> list) {
        mList = list;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public HomeOrderResponse.DataEntity getItem(int position) {
        return position < mList.size() ? mList.get(position) : null;
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
            convertView = View.inflate(mContext, R.layout.list_item_order, null);
            holder.container = convertView;
            holder.picture = (SimpleDraweeView) convertView.findViewById(R.id.sdv_picture);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.desc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.portrait = (SimpleDraweeView) convertView.findViewById(R.id.sdv_portrait);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final HomeOrderResponse.DataEntity data = getItem(position);
        if (data != null && data.getHeight() * data.getWidth() > 0) {
            // 设置宽度
            holder.picture.getLayoutParams().width = mPictureWidth;
            holder.picture.getLayoutParams().height = mPictureWidth * data.getHeight() / data.getWidth();
            // 设置图片
            holder.picture.setImageURI(Uri.parse(data.getPhoto()));
            // 设置头像
            if (TextUtils.isEmpty(data.getHead_img())) {
                holder.portrait.setImageURI(UriUtils.getResourceUri(mContext, R.drawable.icon_default_avator));
            } else {
                holder.portrait.setImageURI(Uri.parse(data.getHead_img()));
            }
            // 设置名称
            holder.name.setText(data.getUser_name());
            // 设置日期
            holder.date.setText(data.getCreated().substring(5));
            if (!TextUtils.isEmpty(data.getState())) {
                int label = R.string.goods_label_impeach;
                switch (Integer.valueOf(data.getState())) {
                    case AppConstant.GOODS_TRUE:
                        label = R.string.goods_label_true;
                        break;
                    case AppConstant.GOODS_FALSE:
                        label = R.string.goods_label_false;
                        break;
                    case AppConstant.GOODS_IMPEACH:
                        label = R.string.goods_label_impeach;
                        break;
                }
                //设置标签
                holder.desc.setVisibility(View.VISIBLE);
                holder.desc.setText(label);
            } else {
                holder.desc.setVisibility(View.GONE);
            }

            // 设置监听
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra(IntentConstant.ORDER_ID, data.getId());
                    intent.putExtra(IntentConstant.CHARGED_STATE, "1");
                    ActivityUtils.jump(mContext, intent);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(UmengConstants.KEY_WATER_FALL_ITEM_ID, data.getId());
                    UmengUtils.onEvent(mContext, EventEnum.HomePageWaterFallItemClick, hashMap);
                }
            });
        }
        return convertView;
    }

    private class Holder {
        private View container;
        private SimpleDraweeView picture;
        private SimpleDraweeView portrait;
        private TextView desc;
        private TextView name;
        private TextView date;
    }
}
