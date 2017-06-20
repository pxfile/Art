package com.bobaoo.xiaobao.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.application.IdentifyApplication;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.PriceQueryData;
import com.bobaoo.xiaobao.domain.TPushCustomContent;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.NavigationPagerAdapter;
import com.bobaoo.xiaobao.ui.widget.indicator.CirclePageIndicator;
import com.bobaoo.xiaobao.utils.DeviceUtil;
import com.bobaoo.xiaobao.utils.LogUtils;
import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by you on 2015/6/1.
 */
public class SplashActivity extends BaseActivity implements StringToBeanTask.ConvertListener<TPushCustomContent>, ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private NavigationPagerAdapter mAdapter;
    private static final String SP_KEY_IS_NAVIGATION_SHOWED = "is_navigation_showed";
    private Handler mHandler = new Handler();
    private CirclePageIndicator mCirclePagerIndicator;

    private boolean mIsFromNotification;
    private int mPageIndex = -1;
    private String mGoodsId;

    private List<PriceQueryData.DataEntity> mQueryList = new ArrayList<>();

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        //定点推送传古来的数据
        mIsFromNotification = intent.getBooleanExtra(IntentConstant.IS_FROM_NOTIFICATION, false);
        mPageIndex = intent.getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_splash_layout;
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initContent() {
        mViewPager = (ViewPager) findViewById(R.id.vp_navigation);
        mAdapter = new NavigationPagerAdapter();
        mAdapter.setOnClickListener(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mCirclePagerIndicator = (CirclePageIndicator) findViewById(R.id.cpi_navigation_indicator);
        mCirclePagerIndicator.setViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_navigation_start:
                mHandler.post(new AutoStart(new Intent(mContext, MainActivity.class)));
                break;
            case R.id.ll_splash:
                mHandler.post(new AutoStart(new Intent(mContext,MainActivity.class)));
                break;
        }
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    protected void refreshData() {
        // 判断app打开的方式
        XGPushClickedResult result = XGPushManager.onActivityStarted(mActivity);
        if (result != null) {
            StringToBeanTask<TPushCustomContent> task = new StringToBeanTask<>(TPushCustomContent.class, this);
            task.execute(result.getCustomContent());
            if (DeviceUtil.isApkDebugable(mContext)) {
                LogUtils.d(mContext, TAG, "TPush--", "onResumeXGPushClickedResult:", result);
            }
        } else {
            if(SharedPreferencesUtils.getSharedPreferencesBoolean(mContext,SP_KEY_IS_NAVIGATION_SHOWED)){
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(IntentConstant.IS_FROM_NOTIFICATION, mIsFromNotification);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, mPageIndex);
                mHandler.post(new AutoStart(intent));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onConvertSuccess(TPushCustomContent response) {
        if(response == null){
            return;
        }
        Intent intent = new Intent();
        switch (response.getOpen()) {
            case "expert":
                intent.setClass(mContext, ExpertDetailActivity.class);
                intent.putExtra(IntentConstant.EXPERT_ID, response.getId());
                new Handler().postDelayed(new AutoStart(intent), 1000);
                break;
            case "info":
                intent.setClass(mContext, InfoDetailActivity.class);
                intent.putExtra(IntentConstant.INFO_ID, response.getId());
                new Handler().postDelayed(new AutoStart(intent), 1000);
                break;
            case "order":
                intent.setClass(mContext, OrderDetailActivity.class);
                intent.putExtra(IntentConstant.ORDER_ID, response.getId());
                new Handler().postDelayed(new AutoStart(intent), 1000);
                break;
            case "feedback":
                intent.setClass(mContext, UserFeedBackActivity.class);
                new Handler().postDelayed(new AutoStart(intent), 1000);
                break;
            case "enquiry":
                mGoodsId = response.getId();
                // 请求数据
                new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                        NetConstant.getUserPriceQueryParams(mContext, 0, 1), new PriceQueryListener());
                break;
            case "comment":
                mGoodsId = response.getId();
                intent.setClass(SplashActivity.this, OrderDetailActivity.class);
                intent.putExtra(IntentConstant.ORDER_ID, mGoodsId);
                mGoodsId = null;
                new Handler().postDelayed(new AutoStart(intent), 500);
                break;
            case "invite":
                intent.setClass(SplashActivity.this, InviteFriendActivity.class);
                new Handler().postDelayed(new AutoStart(intent), 1000);
                break;
            case "html":
            default:
                intent.setClass(SplashActivity.this, MainActivity.class);
                intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.iv_identify);
                break;
        }
//        new Handler().postDelayed(new AutoStart(intent), 1000);
    }

    @Override
    public void onConvertFailed() {
        new Handler().postDelayed(new AutoStart(new Intent(mContext, MainActivity.class)), 1000);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == mAdapter.getCount() - 1){
            mCirclePagerIndicator.setVisibility(View.INVISIBLE);
        }else{
            mCirclePagerIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 启动activity
     */
    private class AutoStart implements Runnable {
        private Intent intent;

        private AutoStart(Intent intent) {
            this.intent = intent;
        }

        @Override
        public void run() {
            jump(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
            SharedPreferencesUtils.setSharedPreferences(mContext, SP_KEY_IS_NAVIGATION_SHOWED,true);
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


    private class PriceQueryListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<PriceQueryData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<PriceQueryData> task = new StringToBeanTask<>(PriceQueryData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }

        @Override
        public void onConvertSuccess(PriceQueryData response) {
            if (response == null){
                return;
            }
            mQueryList = response.getData();
            PriceQueryData.DataEntity dataEntity = null;
            for (int i = 0; i < mQueryList.size(); i++) {
                if (mGoodsId.equals(mQueryList.get(i).getId())){
                    dataEntity = mQueryList.get(i);
                    break;
                }
            }
            if (dataEntity == null){
                return;
            }
            mGoodsId = null;
            // 将参数写入application
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_STATE, dataEntity.getState());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PRICE, dataEntity.getPrice());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_REPORT, dataEntity.getReport());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, dataEntity.getPhoto());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_FROM, dataEntity.getFrom());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_TO, dataEntity.getTo());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, dataEntity.getId());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_HEAD,dataEntity.getAsk_head_img());
            IdentifyApplication.setIntentData(IntentConstant.QUERY_GOOD_NAME, dataEntity.getAsk_user_name());
            IdentifyApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, false);
            Intent intent = new Intent(SplashActivity.this, PriceQueryContentActivity.class);
            new Handler().postDelayed(new AutoStart(intent), 500);
        }

        @Override
        public void onConvertFailed() {
        }
    }
}
