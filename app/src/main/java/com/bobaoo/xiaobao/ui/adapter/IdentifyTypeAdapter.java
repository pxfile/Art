package com.bobaoo.xiaobao.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 2015/6/12.
 */
public class IdentifyTypeAdapter extends RecyclerView.Adapter<IdentifyTypeAdapter.ContentHolder> {

    private View.OnClickListener mListener;

    private List<Data> mList;

    public IdentifyTypeAdapter(View.OnClickListener listener, String methodInfo, String methodPrice) {
        mListener = listener;
        mList = new ArrayList<>();
        if (TextUtils.isEmpty(methodInfo) || TextUtils.isEmpty(methodPrice)) {
            mList.add(new Data(1, AppConstant.IDENTIFY_IMG_TABLE[1], AppConstant.IDENTIFY_METHOD_TABLE[1], AppConstant.IDENTIFY_TIME_TABLE[1], AppConstant.IDENTIFY_PRICES[1]));//极速鉴定
            mList.add(new Data(5, AppConstant.IDENTIFY_IMG_TABLE[4], AppConstant.IDENTIFY_METHOD_TABLE[4], AppConstant.IDENTIFY_TIME_TABLE[4], AppConstant.IDENTIFY_PRICES[4]));//快速鉴定
            mList.add(new Data(0, AppConstant.IDENTIFY_IMG_TABLE[0], AppConstant.IDENTIFY_METHOD_TABLE[0], AppConstant.IDENTIFY_TIME_TABLE[0], AppConstant.IDENTIFY_PRICES[0]));//普通鉴定
            mList.add(new Data(3, AppConstant.IDENTIFY_IMG_TABLE[2], AppConstant.IDENTIFY_METHOD_TABLE[2], AppConstant.IDENTIFY_TIME_TABLE[2], AppConstant.IDENTIFY_PRICES[2]));//预约鉴定
        } else {
            String[] methodInfos = methodInfo.split(",");
            String[] methodPrices = methodPrice.split(",");
            for (int i = 0; i < methodInfos.length; i++) {
                String s = methodInfos[i];
                if (s.equals("1")) {
                    mList.add(new Data(
                            i,
                            AppConstant.IDENTIFY_IMG_TABLE[i],
                            AppConstant.IDENTIFY_METHOD_TABLE[i],
                            AppConstant.IDENTIFY_TIME_TABLE[i],
                            StringUtils.getString("￥", methodPrices[i])));
                }
            }
        }
        mList.add(new Data(4, AppConstant.IDENTIFY_IMG_TABLE[3], AppConstant.IDENTIFY_METHOD_TABLE[3], AppConstant.IDENTIFY_TIME_TABLE[3], AppConstant.IDENTIFY_PRICES[3]));//专家团鉴定
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(View.inflate(parent.getContext(), R.layout.list_item_identify_type, null));
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        Data data = mList.get(position);
        // 填充content
        holder.imageView.setImageResource(data.img);
        holder.name.setText(data.type);
        holder.desc.setText(data.desc);
        holder.price.setText(data.price);
        holder.container.setTag(data.id);
        holder.container.setOnClickListener(mListener);
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        private View container;
        private ImageView imageView;
        private TextView name;
        private TextView desc;
        private TextView price;

        private ContentHolder(View itemView) {
            super(itemView);
            container = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.img_type);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            desc = (TextView) itemView.findViewById(R.id.tv_desc);
            price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }

    private class Data {
        private int id;
        private int img;
        private String type;
        private String desc;
        private String price;

        public Data(int id, int img, String type, String desc, String price) {
            this.id = id;
            this.img = img;
            this.type = type;
            this.desc = desc;
            this.price = price;
        }
    }
}
