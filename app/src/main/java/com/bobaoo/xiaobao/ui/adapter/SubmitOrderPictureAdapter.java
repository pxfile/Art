package com.bobaoo.xiaobao.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.activity.PhotoViewActivity;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by star on 15/6/2.
 */
public class SubmitOrderPictureAdapter extends RecyclerView.Adapter<SubmitOrderPictureAdapter.Holder> {
    private Context mContext;
    private ArrayList<String> mPictureList;
    private String[] mViewStrings;
    private View.OnClickListener mListener;

    public SubmitOrderPictureAdapter(Context context, ArrayList<String> pictureList, int type,
            View.OnClickListener listener) {
        mContext = context;
        mPictureList = pictureList;
        mListener = listener;
        // 获取三视图描述
        switch (type) {
            case AppConstant.IdentifyTypeChina:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_china_sub);
                break;
            case AppConstant.IdentifyTypeJade:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_jade_sub);
                break;
            case AppConstant.IdentifyTypeSundry:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_sundry_sub);
                break;
            case AppConstant.IdentifyTypePainting:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_painting_sub);
                break;
            case AppConstant.IdentifyTypeBronze:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_bronze_sub);
                break;
            case AppConstant.IdentifyTypeWooden:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_wooden_sub);
                break;
            case AppConstant.IdentifyTypeMoneyPaper:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_money_paper_sub);
                break;
            case AppConstant.IdentifyTypeMoneySilver:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_money_silver_sub);
                break;
            case AppConstant.IdentifyTypeMoneyBronze:
                mViewStrings = context.getResources().getStringArray(R.array.view_3d_money_bronze_sub);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mPictureList == null ? 1 : Math.min(mPictureList.size() + 1, AppConstant.MaxTakePictureNum);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int type) {
        return new Holder(View.inflate(viewGroup.getContext(), R.layout.list_item_submit_order_picture, null));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        if (mPictureList != null && i < mPictureList.size()) {
            String picturePath = mPictureList.get(i);
            Uri imageUri = picturePath.startsWith("http") ? Uri.parse(picturePath) : Uri.fromFile(new File(picturePath));
            // 显示图片,实现图片方向的自动调整
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setAutoRotateEnabled(true)
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            holder.pictureView.setController(controller);
            // 显示右上角删除按钮
            holder.delete.setVisibility(View.VISIBLE);
            if (i < AppConstant.DefaultTakePictureNum) {
                holder.delete.setImageResource(R.drawable.icon_change);
            } else {
                holder.delete.setImageResource(R.drawable.icon_close);
            }
            // 显示描述
            holder.descView.setText(i < mViewStrings.length ? mViewStrings[i] : mContext.getString(R.string.detail_desc));
            // 添加监听
            holder.delete.setTag(i);
            holder.delete.setOnClickListener(mListener);
            holder.container.setTag(i);
            if(mNormalItemOnClickListener != null) {
                holder.container.setOnClickListener(mNormalItemOnClickListener);
            }
        } else {
            // 显示添加图片
            holder.pictureView.setImageResource(R.drawable.icon_add);
            // 去掉右上角删除按钮
            holder.delete.setVisibility(View.GONE);
            // 显示描述
            holder.descView.setText("添加更多图片");
            // 添加监听
            holder.container.setTag(i);
            holder.container.setOnClickListener(mListener);
        }
    }

    private View.OnClickListener mNormalItemOnClickListener;

    public void setOnNormalItemClickListener(View.OnClickListener listener) {
        mNormalItemOnClickListener = listener;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private View container;
        private SimpleDraweeView pictureView;
        private ImageView delete;
        private TextView descView;

        public Holder(View itemView) {
            super(itemView);
            container = itemView;
            pictureView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_picture);
            delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            descView = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
