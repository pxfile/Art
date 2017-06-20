package com.bobaoo.xiaobao.ui.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by you on 2015/7/9.
 */
public class PhotoGalleryPagerAdapter extends PagerAdapter {
    private ArrayList<String> mData;
    private ArrayList<String> mRatios;

    private TextView mNumCurrentPic;
    public PhotoGalleryPagerAdapter(ArrayList<String> data, ArrayList<String> ratios) {
        mData = data;
        mRatios = ratios;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_gallery_item, null);

        TextView mBack = (TextView) itemView.findViewById(R.id.tv_back);
        mBack.setVisibility(View.GONE);
        mNumCurrentPic = (TextView) itemView.findViewById(R.id.tv_right);
        mNumCurrentPic.setText(StringUtils.getString(position + 1,"/",mData.size()));
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_photo);
        Uri uri = Uri.parse(mData.get(position));
        simpleDraweeView.setImageURI(uri);
        String ratioStr = mRatios.get(position);
        float ratio = 1.0f;
        if (!TextUtils.isEmpty(ratioStr)) {
            try {
                ratio = Float.parseFloat(ratioStr);
            } catch (NumberFormatException e) {
                ratio = 1.0f;
            }
        }
        simpleDraweeView.setAspectRatio(ratio);
        container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
