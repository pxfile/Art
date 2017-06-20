package com.bobaoo.xiaobao.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.domain.ExpertDetailInfoData;
import com.bobaoo.xiaobao.ui.activity.PhotoGalleryActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.SizeUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 15/6/2.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {
    private List<ExpertDetailInfoData.DataEntity.PhotoEntity> mPictureList;
    private ArrayList<String>  mExpertPhotoUrls = new ArrayList<>();
    private ArrayList<String>  mExpertPhotoRatios = new ArrayList<>();

    public void setData(List<ExpertDetailInfoData.DataEntity.PhotoEntity> list) {
        mPictureList = list;
        for (int i = 0;i < mPictureList.size(); i++){
            mExpertPhotoRatios.add(mPictureList.get(i).getRatio());
            mExpertPhotoUrls.add(mPictureList.get(i).getImg());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View
        View view = View.inflate(viewGroup.getContext(), R.layout.list_item_album, null);
        // 创建一个ViewHolder
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder,final int i) {
        holder.pictureView.setImageURI(Uri.parse(mPictureList.get(i).getImg()));
        holder.pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PhotoGalleryActivity.class);
                intent.putStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_URLS, mExpertPhotoUrls);
                intent.putStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_RATIOS, mExpertPhotoRatios);
                intent.putExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_INDEX, i);
                ActivityUtils.jump(v.getContext(),intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPictureList == null ? 0 : mPictureList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private SimpleDraweeView pictureView;

        public Holder(View itemView) {
            super(itemView);
            pictureView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_picture);
            pictureView.getLayoutParams().width = (int) SizeUtils.dp2Px(pictureView.getContext().getResources(), 100.0f);
            pictureView.getLayoutParams().height = (int) SizeUtils.dp2Px(pictureView.getContext().getResources(), 100.0f);
        }
    }
}
