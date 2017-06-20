package com.bobaoo.xiaobao.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.application.IdentifyApplication;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.OrderDetailData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.BannerAdapter;
import com.bobaoo.xiaobao.ui.adapter.OrderDetailExpertAdapter;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.SizeUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
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
 * Created by star on 15/6/15.
 */
public class OrderToPayActivity extends BaseActivity {

    private TextView mPriceQueryView;
    private SpringIndicator mSpringIndicator;
    private RelativeLayout mContentView;
    private ViewPager mPictureViewPager;
    private SimpleDraweeView mUserPortraitView;//用户头像
    private TextView mUserNameView;//用户昵称
    private TextView mUserContentView;//用户要求


    private TextView mExpertIdentifyTypeView;


    private View mShareContentView;
    private ImageView mStateView;
    private TextView mCreateDate;
    private Button mPayTo;
    private TextView mOrderPrice;

    private BannerAdapter mBannerAdapter;
    private OrderDetailExpertAdapter mExpertAdapter;
//    private OrderDetailCommentAdapter mCommentAdapter;

    private String mOrderId;
    private List<View> mBannerViewList;
    // type:0全部 1未支付 2已支付未鉴定 3已鉴定
    private String mOwnerId;
    private String mChargeState;
    private boolean mIsMyOrderFlg;
    private String mOrderState;
    private String mFirstPictureUrl;
    private String mPrice;
    private String mReport;

    private String mImageFileAbsPath;


    private HashMap<String, String> mMap = new HashMap<>();
    private HashMap<String, String> mCollectMap = new HashMap<>();
    private ArrayList<String> mPhotoUrls;
    private ArrayList<String> mPhotoRatios;

    @Override
    protected void getIntentData() {
        mOrderId = getIntent().getStringExtra(IntentConstant.ORDER_ID);
        mChargeState = getIntent().getStringExtra(IntentConstant.CHARGED_STATE);
    }

    @Override
    protected void initData() {
        mBannerViewList = new ArrayList<>();
        mBannerAdapter = new BannerAdapter(mBannerViewList);
        mExpertAdapter = new OrderDetailExpertAdapter(mContext);
//        mCommentAdapter = new OrderDetailCommentAdapter();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_order_topay;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        TextView headerBackView = (TextView) findViewById(R.id.tv_back);
        headerBackView.setText(R.string.no_payment);
        // 询价按钮
        mPriceQueryView = (TextView) findViewById(R.id.tv_right);
        mPriceQueryView.setText(R.string.enquiry);
        mPriceQueryView.setVisibility(View.GONE);
        // 设置监听
        setOnClickListener(headerBackView);
    }

    @Override
    protected void initContent() {
        // ScrollView中唯一的View
        mContentView = (RelativeLayout) findViewById(R.id.rl_content);
        // 滚动图片
        mPictureViewPager = (ViewPager) findViewById(R.id.vp_picture);
        // 用户头像
        mUserPortraitView = (SimpleDraweeView) findViewById(R.id.sdv_portrait);
        // 用户名称
        mUserNameView = (TextView) findViewById(R.id.tv_user_name);
        // 用户内容
        mUserContentView = (TextView) findViewById(R.id.tv_user_content);
        //创建日期
        mCreateDate = (TextView) findViewById(R.id.tv_create_date);

        mPayTo = (Button) findViewById(R.id.user_pay_to_btn);
        mOrderPrice = (TextView) findViewById(R.id.tv_to_pay_price);
        // 专家内容
//        mExpertContainerView = findViewById(R.id.ll_expert);

        // 专家鉴定类型
        mExpertIdentifyTypeView = (TextView) findViewById(R.id.tv_identify_type);

        // 鉴定真假印章
        mStateView = (ImageView) findViewById(R.id.iv_state);
        // 鉴定结果
        if ("0".equals(mChargeState)) {
            mStateView.setVisibility(View.GONE);
        } else {
            mStateView.setVisibility(View.VISIBLE);
        }

        setOnClickListener(mPayTo);
        //分享的输出View
        mShareContentView = findViewById(R.id.share_content);

//        //收藏状态
//        mCollectionStateTv = (TextView) findViewById(R.id.tv_collect_state);

        mMap.put(UmengConstants.KEY_SHARE_CONTENT_ID, mOrderId);
        mCollectMap.put(UmengConstants.KEY_SHARE_COLLECT_ORDER_ID, mOrderId);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mPictureViewPager.setAdapter(mBannerAdapter);
    }

    @Override
    protected void refreshData() {
        // 请求订单数据
        HttpUtils httpUtils = new HttpUtils();
        //设置当前请求的缓存时间
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getOrderDetailParams(mContext,mOrderId), new OrderListener());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_right:
                DialogUtils.showResponsibilityDialog(mContext, this);
                break;
            case R.id.tv_ok:
                // 将参数写入application
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_STATE, mOrderState);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PRICE, mPrice);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_REPORT, mReport);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, mFirstPictureUrl);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_TO, mOwnerId);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, mOrderId);
                if (UserInfoUtils.checkUserLogin(mContext)) {
                    jump(mContext, PriceQueryContentActivity.class);
                } else {
                    intent = new Intent(mContext, UserLogInActivity.class);
                    jump(intent);
                }
                break;
            case R.id.user_pay_to_btn:
                intent = new Intent(mContext, UserPayActivity.class);
                intent.putExtra(IntentConstant.USER_PAY_GOODS_ID, mOrderId);
                intent.putExtra(IntentConstant.IdentifyType,mExpertIdentifyTypeView.getText());
                jump(intent);
                break;
            default:
                super.onClick(view);
                break;
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

    @Override
    public void finish() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningTasks(1).get(0).numActivities < 2) {
            jump(mContext, MainActivity.class);
        }
        super.finish();
    }

    private void attachData(OrderDetailData response) {
        if (response.getData() != null) {
            OrderDetailData.DataEntity.GoodsEntity goods = response.getData().getGoods();
            // 用户信息
            mOwnerId = goods.getUser_id();
            mIsMyOrderFlg = TextUtils.equals(mOwnerId, UserInfoUtils.getUserId(mContext));
            mPriceQueryView.setVisibility(mIsMyOrderFlg ? View.GONE : View.VISIBLE);//如果是我的订单，则不用询价
            mUserPortraitView.setImageURI(Uri.parse(goods.getHead_img()));
            mUserNameView.setText(goods.getNikename());
            mUserContentView.setText(TextUtils.isEmpty(goods.getNote().trim()) ? getString(R.string.identify_no_tips) : goods.getNote());
            mCreateDate.setText(goods.getCreated());
            mOrderPrice.setText(goods.getCharge_price());
            // 专家信息
            OrderDetailData.DataEntity.ExpertEntity expert = response.getData().getExpert();
            if (expert != null) {
//                mExpertIdentifyTypeView.setText(goods.getJb_type());
                mExpertIdentifyTypeView.setText(getIntent().getStringExtra(IntentConstant.IdentifyType));
            }
            switch (goods.getState()) {
                case AppConstant.OrderState.Wait:
                    mStateView.setImageResource(R.drawable.stamp_wait);
                    mOrderState = "等待";
                    break;
                case AppConstant.OrderState.Cancel:
                    mStateView.setImageResource(R.drawable.stamp_cancel);
                    mOrderState = "取消";
                    break;
                case AppConstant.OrderState.Real:
                    mStateView.setImageResource(R.drawable.stamp_real);
                    mOrderState = "真品";
                    break;
                case AppConstant.OrderState.Fake:
                    mStateView.setImageResource(R.drawable.stamp_fake);
                    mOrderState = "赝品";
                    break;
                case AppConstant.OrderState.Doubt:
                    mStateView.setImageResource(R.drawable.stamp_doubt);
                    mOrderState = "存疑";
                    break;
            }
            // 首图
            if (goods.getPhoto().size() > 0) {
                mFirstPictureUrl = goods.getPhoto().get(0).getImg();
            }
            mPhotoUrls = new ArrayList<>();
            mPhotoRatios = new ArrayList<>();
            // 设置banner
            List<OrderDetailData.DataEntity.GoodsEntity.PhotoEntity> photoEntities = goods.getPhoto();
            for (int i = 0; i < photoEntities.size(); i++) {
                OrderDetailData.DataEntity.GoodsEntity.PhotoEntity photo = photoEntities.get(i);
                // 渲染banner的view
                View rootView = View.inflate(mContext, R.layout.list_item_home_banner_picture, null);
                SimpleDraweeView view = (SimpleDraweeView) rootView.findViewById(R.id.sdv_picture);
                // 设置宽度
                view.getLayoutParams().width = SizeUtils.getScreenWidth(mContext);
                view.getLayoutParams().height = (int) SizeUtils.dp2Px(getResources(), 200.0f);
                // 加载图片
                view.setImageURI(Uri.parse(photo.getImg()));
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoGalleryActivity.class);
                        intent.putStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_URLS, mPhotoUrls);
                        intent.putStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_RATIOS, mPhotoRatios);
                        intent.putExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_INDEX, (Integer) v.getTag());
                        jump(intent);
                    }
                });
                // 添加到list中
                mBannerViewList.add(rootView);
                mPhotoUrls.add(photo.getImg());
                mPhotoRatios.add(StringUtils.getString(photo.getRatio()));
            }

            mBannerAdapter.notifyDataSetChanged();
            // 添加Indicator
            if (mSpringIndicator == null) {
                // 设置banner的indicator
                mSpringIndicator = (SpringIndicator) View.inflate(mContext, R.layout.indicator_spring, null);
                // 设置banner的layout信息
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(SizeUtils.getScreenWidth(mContext) / 2,
                                (int) SizeUtils.dp2Px(getResources(), 50.0f));
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                params.topMargin = (int) SizeUtils.dp2Px(getResources(), 150.0f);
                mSpringIndicator.setLayoutParams(params);
                // 设置indicator的viewPager
                mSpringIndicator.setViewPager(mPictureViewPager);
                // 添加indicator到view
                mContentView.addView(mSpringIndicator);
            }
//            // 询价按钮
//            mPriceQueryView.setVisibility(View.VISIBLE);
            setOnClickListener(mPriceQueryView);
        }
    }


    private class OrderListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<OrderDetailData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<OrderDetailData> task = new StringToBeanTask<>(OrderDetailData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            e.printStackTrace();
        }

        @Override
        public void onConvertSuccess(OrderDetailData response) {
            attachData(response);
        }

        @Override
        public void onConvertFailed() {
        }
    }
}