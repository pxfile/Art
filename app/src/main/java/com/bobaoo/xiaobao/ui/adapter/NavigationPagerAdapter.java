package com.bobaoo.xiaobao.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobaoo.xiaobao.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by you on 2015/7/15.
 */
public class NavigationPagerAdapter extends PagerAdapter{
    private int[] TOP_IMGS = {
            R.drawable.navigation_top1,
            R.drawable.navigation_top2,
            R.drawable.navigation_top3,
            R.drawable.navigation_top4};

    private int[] DOWN_IMGS = {
            R.drawable.navigation_down1,
            R.drawable.navigation_down2,
            R.drawable.navigation_down3,
            R.drawable.navigation_down4};

    private View.OnClickListener mListener;
    public void setOnClickListener(View.OnClickListener listener){
        mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_navigation_page, null);
        SimpleDraweeView sdvTop = (SimpleDraweeView) itemView.findViewById(R.id.sdv_top);
        SimpleDraweeView sdvDown = (SimpleDraweeView) itemView.findViewById(R.id.sdv_down);
        sdvTop.setImageResource(TOP_IMGS[position]);
        sdvDown.setImageResource(DOWN_IMGS[position]);
        container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View startBtn = itemView.findViewById(R.id.btn_navigation_start);
        View mLL = itemView.findViewById(R.id.ll_splash);
        if(position == getCount() - 1){
            startBtn.setVisibility(View.VISIBLE);
            if(mListener != null){
                startBtn.setOnClickListener(mListener);
                mLL.setOnClickListener(mListener);
            }
        }else{
            startBtn.setVisibility(View.GONE);
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
