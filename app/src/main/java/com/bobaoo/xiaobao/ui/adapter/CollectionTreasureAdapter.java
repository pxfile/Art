package com.bobaoo.xiaobao.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.TreasureCollectionData;
import com.bobaoo.xiaobao.domain.UserDeleteResponse;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.OrderDetailActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * Created by star on 15/7/2.
 */
public class CollectionTreasureAdapter extends BaseAdapter implements View.OnClickListener, AdapterView.OnItemClickListener {
    private List<TreasureCollectionData.DataEntity> mList;

    public void setDataList(List<TreasureCollectionData.DataEntity> list) {
        mList = list;
        notifyDataSetInvalidated();
    }

    public void addDataList(List<TreasureCollectionData.DataEntity> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public TreasureCollectionData.DataEntity getItem(int position) {
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
            holder.delete = convertView.findViewById(R.id.img_delete_collect);
            holder.desc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TreasureCollectionData.DataEntity dataEntity = getItem(position);
        if (dataEntity != null) {
            holder.photo.setImageURI(Uri.parse(dataEntity.getPhoto()));
            holder.state.setText(dataEntity.getState());
            holder.price.setText(dataEntity.getPrice());
            holder.desc.setText(dataEntity.getReport());
            holder.delete.setTag(position);
            holder.delete.setOnClickListener(this);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(parent.getContext(), OrderDetailActivity.class);
        intent.putExtra(IntentConstant.ORDER_ID, getItem((int) id).getFid());
        ActivityUtils.jump(parent.getContext(), intent);
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                NetConstant.getUserDeleteParams(view.getContext(), 3, getItem(position).getId()), new DeleteListener());
        removeData(position);
    }

    private class ViewHolder {
        SimpleDraweeView photo;
        TextView state;
        TextView price;
        View delete;
        TextView desc;
    }


    private class DeleteListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserDeleteResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserDeleteResponse> task = new StringToBeanTask<>(UserDeleteResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }

        @Override
        public void onConvertSuccess(UserDeleteResponse response) {
        }

        @Override
        public void onConvertFailed() {
        }
    }
}
