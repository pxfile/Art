package com.bobaoo.xiaobao.ui.activity;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.adapter.PhotoPagerAdapter;
import com.bobaoo.xiaobao.ui.widget.viewpager.HackyViewPager;
import java.util.ArrayList;

/**
 * Created by you on 2015/6/29.
 */
public class PhotoViewActivity extends BaseActivity {
    private ArrayList<String> mUsingFilePathList;
    private int mPhotoPagerIndex;
    private HackyViewPager mViewPager;
    private PhotoPagerAdapter mAdapter;

    @Override
    protected void getIntentData() {
        mUsingFilePathList = getIntent().getStringArrayListExtra(IntentConstant.UsingPictureFilePaths);
        mPhotoPagerIndex = getIntent().getIntExtra(IntentConstant.UsingPictureIndex, 0);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initContent() {
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mAdapter = new PhotoPagerAdapter(mUsingFilePathList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPhotoPagerIndex);
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
