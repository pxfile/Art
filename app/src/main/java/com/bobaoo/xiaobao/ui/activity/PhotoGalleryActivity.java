package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.adapter.PhotoGalleryPagerAdapter;

import java.util.ArrayList;

/**
 * Created by you on 2015/7/9.
 */
public class PhotoGalleryActivity extends BaseActivity{
    private ArrayList<String> mPhotoUrls;
    private ArrayList<String> mPhotoRatios;
    private int mPhotoIndex;
    private ViewPager mViewPager;
    private PhotoGalleryPagerAdapter mAdapter;
    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mPhotoUrls = intent.getStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_URLS);
        mPhotoRatios = intent.getStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_RATIOS);
        mPhotoIndex = intent.getIntExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_INDEX, 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_photo_gallery;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initContent() {
        mViewPager = (ViewPager) findViewById(R.id.vp_photos);
        mAdapter = new PhotoGalleryPagerAdapter(mPhotoUrls,mPhotoRatios);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPhotoIndex);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {

    }
}
