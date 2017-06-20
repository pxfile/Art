package com.bobaoo.xiaobao.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.fragment.CollectionFragment;
import com.bobaoo.xiaobao.ui.fragment.CommentFragment;
import com.bobaoo.xiaobao.ui.fragment.PriceQueryFragment;
import com.bobaoo.xiaobao.ui.widget.indicator.SimpleIndicator;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * Created by you on 2015/6/1.
 */
public class UserSubActivity extends BaseActivity {

    public static final int Collection = 0;
    public static final int Attention = 1;
    public static final int Comment = 2;
    public static final int PriceQuery = 3;

    private ViewPager mViewPager;
    private SimpleIndicator mIndicator;

    private String mTitle;
    private String[] mTitles;
    private int mTarget;
    @Override
    protected void getIntentData() {
        mTarget = getIntent().getIntExtra(IntentConstant.TARGET_FRAGMENT, 0);
    }

    @Override
    protected void initData() {
        switch (mTarget) {
            case Collection:
                mTitle = "收藏";
                mTitles = new String[]{"宝物收藏", "资讯收藏"};
                break;
            case Comment:
                mTitle = "评论";
                mTitles = new String[]{"发出评论", "收到评论"};
                break;
            case PriceQuery:
                mTitle = "询价";
                mTitles = new String[]{"收到询价", "发出询价"};
                break;
            case Attention:
            default:
                mTitle = "";
                mTitles = new String[]{};
                break;
        }
    }
    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_sub;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        TextView headerBackView = (TextView) findViewById(R.id.tv_back);
        headerBackView.setText(mTitle);
        // 设置监听
        setOnClickListener(headerBackView);
    }

    @Override
    protected void initContent() {
        mViewPager = (ViewPager) findViewById(R.id.vp_container);
        mIndicator = (SimpleIndicator) findViewById(R.id.vpi_identify);
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
        mViewPager.setAdapter(new UserSubAdapter(getSupportFragmentManager()));
        mIndicator.setViewPager(mViewPager);
        mIndicator.setTabTitles(mTitles);
    }

    @Override
    protected void refreshData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    private class UserSubAdapter extends FragmentPagerAdapter {
        public UserSubAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle bundle;
            switch (mTarget) {
                case Collection:
                    fragment = new CollectionFragment();
                    bundle = new Bundle();
                    bundle.putInt(IntentConstant.TYPE, position);
                    fragment.setArguments(bundle);
                    return fragment;
                case Comment:
                    fragment = new CommentFragment();
                    bundle = new Bundle();
                    bundle.putInt(IntentConstant.TYPE, position);
                    fragment.setArguments(bundle);
                    return fragment;
                case PriceQuery:
                    fragment = new PriceQueryFragment();
                    bundle = new Bundle();
                    bundle.putInt(IntentConstant.TYPE, position);
                    fragment.setArguments(bundle);
                    return fragment;
                case Attention:
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
    }
}
