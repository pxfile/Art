package com.bobaoo.xiaobao.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.HomeBannerResponse;
import com.bobaoo.xiaobao.domain.HomeOrderResponse;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.ExpertDetailActivity;
import com.bobaoo.xiaobao.ui.activity.InfoDetailActivity;
import com.bobaoo.xiaobao.ui.activity.OrderDetailActivity;
import com.bobaoo.xiaobao.ui.activity.WebViewActivity;
import com.bobaoo.xiaobao.ui.adapter.BannerAdapter;
import com.bobaoo.xiaobao.ui.adapter.HomeOrderAdapter;
import com.bobaoo.xiaobao.ui.widget.plaview.XListView;
import com.bobaoo.xiaobao.utils.SizeUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import github.chenupt.springindicator.SpringIndicator;

/**
 * Created by star on 15/5/29.
 */
public class HomeFragment extends BaseFragment implements XListView.IXListViewListener {
    private SpringIndicator mSpringIndicator;
    private ViewPager mBannerViewPager;
    private XListView mOrderView;

    private RelativeLayout mHeaderView;
    private BannerAdapter mBannerAdapter;
    private HomeOrderAdapter mOrderAdapter;

    private List<View> mBannerViewList;
    private int mCurrentPage;

    private List<HomeOrderResponse.DataEntity> mOrderList;

    private boolean mFlagIsLoadedBanner;
    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
        // 设置第0页
        mCurrentPage = 1;
        // 初始化banner
        mBannerViewList = new ArrayList<>();
        // 设置banner的adapter
        mBannerAdapter = new BannerAdapter(mBannerViewList);
        // 设置order的adapter
        mOrderAdapter = new HomeOrderAdapter(mContext);
        // 初始化order
        mOrderList = new ArrayList<>();
    }
    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContent() {
        // 渲染banner
        mHeaderView = (RelativeLayout) View.inflate(mContext, R.layout.header_home, null);
        mBannerViewPager = (ViewPager) mHeaderView.findViewById(R.id.vp_banner);
        // 设置banner的layout信息
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) SizeUtils.dp2Px(getResources(), 160.0f));
        mBannerViewPager.setLayoutParams(params);
        // 设置adapter
        mBannerViewPager.setAdapter(mBannerAdapter);
        // 订单瀑布流
        mOrderView = (XListView) mRootView.findViewById(R.id.list);
        mOrderView.setPullLoadEnable(true);
        mOrderView.setXListViewListener(this);
        // 添加banner到header
        mOrderView.addHeaderView(mHeaderView);
        // 设置order的adapter
        mOrderView.setAdapter(mOrderAdapter);
    }

    @Override
    protected void loadData() {
        if (!mFlagIsLoadedBanner){
            // 请求banner数据
            mHandlerList.add(new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getBannerListParams(mContext),
                    new BannerListener()));
        }

        // 请求订单数据
        mHandlerList.add(new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getOrderListParams(mContext, mCurrentPage),
                new OrderListener()));
    }

    @Override
    protected void attachData() {
        if (mCurrentPage == 1) {
            if (mOrderList != null) {
                mOrderAdapter.setDataList(mOrderList);
                mIsNeedLoadData = false;
            }
            mOrderView.stopRefresh();
        } else {
            if (mOrderList != null) {
                mOrderAdapter.addDataList(mOrderList);
                mIsNeedLoadData = false;
            }
            mOrderView.stopLoadMore();
        }
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        mCurrentPage++;
        loadData();
    }

    private void attachBannerData(List<HomeBannerResponse.DataEntity.SliderEntity> sliderList) {
        for (final HomeBannerResponse.DataEntity.SliderEntity slider : sliderList) {
            // 渲染banner的view
            View rootView = View.inflate(mContext, R.layout.list_item_home_banner_picture, null);
            SimpleDraweeView view = (SimpleDraweeView) rootView.findViewById(R.id.sdv_picture);
            // 设置宽度
            view.getLayoutParams().width = SizeUtils.getScreenWidth(mContext);
            view.getLayoutParams().height = (int) SizeUtils.dp2Px(getResources(), 160.0f);
            // 加载图片
            view.setImageURI(Uri.parse(slider.getUrl()));
            // 添加点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    switch (slider.getType()) {
                        case "expert":
                            intent = new Intent(mContext, ExpertDetailActivity.class);
                            intent.putExtra(IntentConstant.EXPERT_ID, slider.getId());
                            jump(intent);
                            hashMap.clear();
                            hashMap.put(UmengConstants.KEY_BANNER_TYPE_EXPERT, slider.getId());
                            UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick, hashMap);
                            break;
                        case "order":
                            intent = new Intent(mContext, OrderDetailActivity.class);
                            intent.putExtra(IntentConstant.ORDER_ID, slider.getId());
                            intent.putExtra(IntentConstant.CHARGED_STATE, "1");
                            jump(intent);
                            hashMap.clear();
                            hashMap.put(UmengConstants.KEY_BANNER_TYPE_IDENTIFY, slider.getId());
                            UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick, hashMap);
                            break;
                        case "info":
                            intent = new Intent(mContext, InfoDetailActivity.class);
                            intent.putExtra(IntentConstant.INFO_ID, slider.getId());
                            jump(intent);
                            hashMap.clear();
                            hashMap.put(UmengConstants.KEY_BANNER_TYPE_INFO, slider.getId());
                            UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick, hashMap);
                            break;
                        case "url":
                            intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra(IntentConstant.WEB_URL, slider.getId());
                            intent.putExtra(IntentConstant.WEB_TITLE, slider.getName());
                            jump(intent);
                            hashMap.clear();
                            hashMap.put(UmengConstants.KEY_BANNER_TYPE_URL, slider.getId());
                            UmengUtils.onEvent(mContext, EventEnum.HomePageBannerItemClick, hashMap);
                            break;
                        default:
                            break;
                    }
                }
            });
            // 添加到list中
            mBannerViewList.add(rootView);
        }
        mBannerAdapter.notifyDataSetChanged();
        // 设置indicator
        if (mSpringIndicator == null) {
            // 设置banner的indicator
            mSpringIndicator = (SpringIndicator) View.inflate(mContext, R.layout.indicator_spring, null);
            // 设置banner的layout信息
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(SizeUtils.getScreenWidth(mContext) / 2, (int) SizeUtils.dp2Px(getResources(), 50.0f));
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            params.topMargin = (int) SizeUtils.dp2Px(getResources(), 120.0f);
            mSpringIndicator.setLayoutParams(params);
            // 设置indicator的viewPager
            mSpringIndicator.setViewPager(mBannerViewPager);
            mSpringIndicator.setOnPageChangeListener(new PageChangeListener());
            // 添加indicator到view
            mHeaderView.addView(mSpringIndicator);
        }
    }


    private class BannerListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<HomeBannerResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<HomeBannerResponse> task = new StringToBeanTask<>(HomeBannerResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            e.printStackTrace();
        }

        @Override
        public void onConvertSuccess(HomeBannerResponse response) {
            if (getActivity() != null) {
                attachBannerData(response.getData().getSlider());
                mFlagIsLoadedBanner = true;
            }
        }

        @Override
        public void onConvertFailed() {
        }
    }


    private class OrderListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<HomeOrderResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<HomeOrderResponse> task = new StringToBeanTask<>(HomeOrderResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (getActivity() != null) {
                if (mOrderList != null){
                    mOrderList.clear();
                }
                attachData();
            }
        }

        @Override
        public void onConvertSuccess(HomeOrderResponse response) {
            if (getActivity() != null) {
                mOrderList = response.getData();
                attachData();
            }
        }

        @Override
        public void onConvertFailed() {
            if (getActivity() != null) {
                mOrderList.clear();
                attachData();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onPageStart("HomeFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPageEnd("HomeFragment");
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
