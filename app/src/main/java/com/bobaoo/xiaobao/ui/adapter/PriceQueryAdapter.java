package com.bobaoo.xiaobao.ui.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.application.IdentifyApplication;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.domain.PriceQueryData;
import com.bobaoo.xiaobao.ui.activity.PriceQueryContentActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 15/7/2.
 */
public class PriceQueryAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private List<PriceQueryData.DataEntity> mList;
    private int mType;

    private String mUrl;
    public void setDataList(List<PriceQueryData.DataEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addDataList(List<PriceQueryData.DataEntity> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    public PriceQueryAdapter(int type) {
        mType = type;
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PriceQueryData.DataEntity getItem(int position) {
        return position < mList.size() ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(viewGroup.getContext(), R.layout.list_item_user_sub, null);
            holder.photo = (SimpleDraweeView) convertView.findViewById(R.id.sdv_photo);
            holder.state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.date = (TextView) convertView.findViewById(R.id.tv_query_date);
            holder.delete = convertView.findViewById(R.id.img_delete_collect);
            holder.desc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PriceQueryData.DataEntity dataEntity = getItem(position);
        if (dataEntity != null) {
            holder.photo.setImageURI(Uri.parse(dataEntity.getPhoto()));
            holder.state.setText(dataEntity.getAsk_user_name());
            holder.date.setText(dataEntity.getAsk_addtime());
            holder.desc.setText(dataEntity.getAsk_content());
            holder.delete.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PriceQueryData.DataEntity item = getItem((int) id);

        // 将参数写入application
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_STATE, item.getState());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PRICE, item.getPrice());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_REPORT, item.getReport());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, item.getPhoto());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_FROM, item.getFrom());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_TO, item.getTo());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, item.getId());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_HEAD,item.getAsk_head_img());
        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOOD_NAME,item.getAsk_user_name());
        IdentifyApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, false);
        ActivityUtils.jump(parent.getContext(), PriceQueryContentActivity.class);
    }

    private class ViewHolder {
        SimpleDraweeView photo;
        TextView state;
        TextView price;
        View delete;
        TextView desc;
        TextView date;
    }
}
