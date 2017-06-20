package com.bobaoo.xiaobao.ui.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.utils.BitmapUtils;
import com.bobaoo.xiaobao.utils.SizeUtils;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by you on 2015/6/29.
 */
public class PhotoPagerAdapter extends PagerAdapter{
    private ArrayList<String> mData;

    public PhotoPagerAdapter(ArrayList<String> data){
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0:mData.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_item,null);

        PhotoView photoView = (PhotoView) itemView.findViewById(R.id.photo_view);
        Bitmap bp = BitmapUtils.getBitmapFromFile(mData.get(position), SizeUtils.getScreenWidth(container.getContext()), SizeUtils.getScreenHeight(container.getContext()));
        photoView.setImageBitmap(bp);
        container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
