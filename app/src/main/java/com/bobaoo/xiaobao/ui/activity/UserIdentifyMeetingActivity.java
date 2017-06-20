package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.fragment.UserIdentifyMeetingFragment;
import com.bobaoo.xiaobao.ui.widget.indicator.SimpleIndicator;
import com.bobaoo.xiaobao.utils.UmengUtils;

/**
 * 我的鉴定会页面
 */
public class UserIdentifyMeetingActivity extends FragmentActivity {
    private ViewPager mFragmentsContainer;
    private FragmentManager mChildFragmentManager;
    private UserIdentifyMeetingFragmentPagerAdapter mUserIdentifyMeetingFragmentPagerAdapter;
    //    private TabPageIndicator mVpi;
    private SimpleIndicator mVpi;
    //    type:1待支付 2已支付 3已完成
    private String[] TITLES = {"待支付", "已支付", "已完成"};

    private int mPageIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sub);
        initView();
    }

    protected void initView() {
        mFragmentsContainer = (ViewPager) findViewById(R.id.vp_container);
        mChildFragmentManager = getSupportFragmentManager();
        mUserIdentifyMeetingFragmentPagerAdapter = new UserIdentifyMeetingFragmentPagerAdapter(mChildFragmentManager);
        mFragmentsContainer.setAdapter(mUserIdentifyMeetingFragmentPagerAdapter);
        mVpi = (SimpleIndicator) findViewById(R.id.vpi_identify);
        mVpi.setViewPager(mFragmentsContainer);
        mVpi.setTabTitles(TITLES);
        Intent intent = getIntent();
        mPageIndex = intent.getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, 0);
        mFragmentsContainer.setCurrentItem(mPageIndex);
        initTile();
    }

    private void initTile() {
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText(getString(R.string.identify_meeting));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class UserIdentifyMeetingFragmentPagerAdapter extends FragmentPagerAdapter {

        public UserIdentifyMeetingFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return UserIdentifyMeetingFragment.creatUserIdentifyMeetingFragment(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 这里Destroy的是Fragment的视图层次，并不是Destroy Fragment对象
            super.destroyItem(container, position, object);
        }
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
}
