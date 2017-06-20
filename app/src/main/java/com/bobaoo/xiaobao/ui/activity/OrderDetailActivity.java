package com.bobaoo.xiaobao.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.application.IdentifyApplication;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.NetWorkRequestConstants;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.AttentionCollectionResponse;
import com.bobaoo.xiaobao.domain.JsObj;
import com.bobaoo.xiaobao.domain.OrderDetailData;
import com.bobaoo.xiaobao.manager.UMengShareManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.BannerAdapter;
import com.bobaoo.xiaobao.ui.adapter.OrderDetailCommentAdapter;
import com.bobaoo.xiaobao.ui.adapter.OrderDetailExpertAdapter;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.ui.widget.fix.FixedListView;
import com.bobaoo.xiaobao.utils.BitmapUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
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
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import github.chenupt.springindicator.SpringIndicator;


/**
 * Created by star on 15/6/15.
 */
public class OrderDetailActivity extends BaseActivity implements OrderDetailCommentAdapter.OnReplyActionListener {
    private static final String SP_KEY_ORDER_DETAIL_INNER_GUIDER = "order_detail_inner_guider";

    private TextView mPriceQueryView;
    private SpringIndicator mSpringIndicator;
    private RelativeLayout mContentView;
    private ViewPager mPictureViewPager;
    private SimpleDraweeView mUserPortraitView;
    private TextView mUserNameView;
    private TextView mUserContentView;
    private View mExpertContainerView;
    private SimpleDraweeView mExpertPortraitView;
    private TextView mExpertNameView;
    private TextView mExpertTypeView;
    private TextView mExpertIdentifyTypeView;
    private TextView mExpertPriceView;
    private View mExpertGroupContainerView;
    private FixedListView mExpertGroupView;
    private View mShareContentView;
    private ImageView mStateView;
    private FixedListView mCommentView;
    private EditText mEditCommentView;
    private TextView mCreateDate;

    private BannerAdapter mBannerAdapter;
    private OrderDetailExpertAdapter mExpertAdapter;
    private OrderDetailCommentAdapter mCommentAdapter;

    private String mOrderId;
    private List<View> mBannerViewList;
    // type:0全部 1未支付 2已支付未鉴定 3已鉴定
    private String mOwnerId;
    private String mOwnerName;
    private String mChargeState;
    private boolean mIsMyOrderFlg;
    private String mOrderState;
    private String mFirstPictureUrl;
    private String mPrice;
    private String mReport;
    private String mShareContent;

    private String mImageFileAbsPath;

    private TextView mCollectionStateTv;

    private HashMap<String, String> mMap = new HashMap<>();
    private HashMap<String, String> mCollectMap = new HashMap<>();
    private ArrayList<String> mPhotoUrls;
    private ArrayList<String> mPhotoRatios;
    private ProgressDialog mProgressDialog;
    private ImageView mVideoIv;
    private boolean mTypeFlag;

    private TextView mTvComment;
    private LinearLayout mllComment;
    private String mVideoURL;
    private String mOwnerHead;

    private LinearLayout mllCommentClick;
    private RelativeLayout mRLCommitComment;
    private EditText mCommitCommentEt;
    private ImageView mIvCommit;
    private boolean mIsComment;
    private String mExpertId;

    //Activity最外层的Layout视图
    private View mActivityRootView;
    //屏幕高度
    private int mScreenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int mKeyHeight = 0;

    private WebView mWebView;
    private PopupWindow mKeyWordPopupWindow;//关键字解释的弹窗

    private View mInnerGuiderView;//引导图片
    private TextView mGroupPriceTv;

    private boolean mIsReplyFlag;//回复标记
    private String mReplyId;//回复的哪个评论的id

    @Override
    protected void getIntentData() {
        mOrderId = getIntent().getStringExtra(IntentConstant.ORDER_ID);
        mChargeState = getIntent().getStringExtra(IntentConstant.CHARGED_STATE);
        mTypeFlag = getIntent().getBooleanExtra(IntentConstant.IDENTIFY_TYPE_FLAG, false);
    }

    @Override
    protected void initData() {
        mBannerViewList = new ArrayList<>();
        mBannerAdapter = new BannerAdapter(mBannerViewList);
        mExpertAdapter = new OrderDetailExpertAdapter(mContext);
        mCommentAdapter = new OrderDetailCommentAdapter();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        TextView headerBackView = (TextView) findViewById(R.id.tv_back);
        headerBackView.setText(R.string.order_detail);
        mVideoIv = (ImageView) findViewById(R.id.video_iv);
        mVideoIv.setImageResource(R.drawable.img_identify_video);
        // 设置监听
        setOnClickListener(headerBackView, mVideoIv);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initContent() {
        mRLCommitComment = (RelativeLayout) findViewById(R.id.rl_comment_commit);
        mllCommentClick = (LinearLayout) findViewById(R.id.ll_comment_click);
        mIvCommit = (ImageView) findViewById(R.id.iv_commit_comment);
        mCommitCommentEt = (EditText) findViewById(R.id.et_comment_content);
        mActivityRootView = findViewById(R.id.rl_out);
        //获取屏幕高度
        mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        mKeyHeight = mScreenHeight / 3;
        mCommitCommentEt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mCommitCommentEt.setSingleLine(false);
        mCommitCommentEt.setHorizontallyScrolling(false);

        mActivityRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > mKeyHeight)) {
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > mKeyHeight)) {
                    mllCommentClick.setVisibility(View.VISIBLE);
                    mCommitCommentEt.setText("");
                }
            }
        });

        mPriceQueryView = (TextView) findViewById(R.id.tv_query);
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
        // 专家内容
        mExpertContainerView = findViewById(R.id.ll_expert);
        // 专家头像
        mExpertPortraitView = (SimpleDraweeView) findViewById(R.id.sdv_expert_portrait);
        // 专家名称
        mExpertNameView = (TextView) findViewById(R.id.tv_expert_name);
        // 专家类型
        mExpertTypeView = (TextView) findViewById(R.id.tv_expert_type);
        // 专家鉴定类型
        mExpertIdentifyTypeView = (TextView) findViewById(R.id.tv_identify_type);
        // 专家团内容
        mExpertGroupContainerView = findViewById(R.id.ll_expert_group);
        mExpertPriceView = (TextView) findViewById(R.id.tv_expert_price);
        mGroupPriceTv = (TextView) findViewById(R.id.tv_expert_group_price);
        // 专家团
        mExpertGroupView = (FixedListView) findViewById(R.id.flv_expert_group);
        // 分享
        mShareContentView = findViewById(R.id.share_content);
        View mShareWeChatView = findViewById(R.id.ll_share_wechat);
        View mShareQQView = findViewById(R.id.ll_share_qq);
        View mShareWeiBoView = findViewById(R.id.ll_share_weibo);
        View mShareMomentsView = findViewById(R.id.ll_share_moments);
        View mShareQQZoneView = findViewById(R.id.ll_share_qq_zone);
        View mOrderCollectView = findViewById(R.id.ll_order_collect);
        // 鉴定真假印章

        mStateView = (ImageView) findViewById(R.id.iv_state);
        // 鉴定结果
        if ("0".equals(mChargeState)) {
            mStateView.setVisibility(View.GONE);
        } else {
            mStateView.setVisibility(View.VISIBLE);
        }
        // 评论列表
        mCommentView = (FixedListView) findViewById(R.id.flv_comment);
        // 写评论
        mEditCommentView = (EditText) findViewById(R.id.et_comment);
        // 提交评论
        View mCommitCommentView = findViewById(R.id.tv_commit);
        mTvComment = (TextView) findViewById(R.id.tv_comment);
        mllComment = (LinearLayout) findViewById(R.id.ll_comment);
        //若为待鉴定跳转过来的，则隐藏评论
        if (mTypeFlag) {
            mTvComment.setVisibility(View.GONE);
            mllComment.setVisibility(View.GONE);
        }
        // 添加点击事件
        setOnClickListener(mShareWeChatView, mShareQQView, mShareWeiBoView, mShareMomentsView, mShareQQZoneView, mOrderCollectView,
                mCommitCommentView, mExpertContainerView, mEditCommentView, mIvCommit);

        //分享的输出View
        mShareContentView = findViewById(R.id.share_content);

        //收藏状态
        mCollectionStateTv = (TextView) findViewById(R.id.tv_collect_state);

        mMap.put(UmengConstants.KEY_SHARE_CONTENT_ID, mOrderId);
        mCollectMap.put(UmengConstants.KEY_SHARE_COLLECT_ORDER_ID, mOrderId);

        mWebView = (WebView) findViewById(R.id.wv_test);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final Handler handler = new Handler();
        mWebView.addJavascriptInterface(new JsObj(mContext) {
            @JavascriptInterface
            @Override
            public void share() {

            }

            @JavascriptInterface
            public void share(final String url, final String title) {
                //保证UI操作放在主线程
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showPopUpWindow(mWebView, url, title);
                    }
                });
            }
        }, "browser");
        //设置引导显示
        mInnerGuiderView = findViewById(R.id.img_inner_guider);
        mInnerGuiderView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SharedPreferencesUtils.setSharedPreferencesBoolean(mContext, SP_KEY_ORDER_DETAIL_INNER_GUIDER, true);
                mInnerGuiderView.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private void showPopUpWindow(View anchView, String url, String title) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_noun_interpretation, null);
        mKeyWordPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mKeyWordPopupWindow.setContentView(view);
        mKeyWordPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_button_transparent_black_stroke));
        mKeyWordPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        WebView wv = (WebView) view.findViewById(R.id.wv_noun_interpretation);
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl(url);

        TextView titileTv = (TextView) view.findViewById(R.id.popup_title);
        titileTv.setText(title);

        View dismissView = view.findViewById(R.id.img_popupwindow_dismiss);

        dismissView.setOnClickListener(this);

        View maskView = view.findViewById(R.id.bg_mask);
        maskView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mKeyWordPopupWindow != null && mKeyWordPopupWindow.isShowing()) {
                    mKeyWordPopupWindow.dismiss();
                }
                return true;
            }
        });

        mKeyWordPopupWindow.showAtLocation(anchView, Gravity.CENTER, 0, 0);


    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mPictureViewPager.setAdapter(mBannerAdapter);
        mExpertGroupView.setAdapter(mExpertAdapter);
        mCommentView.setAdapter(mCommentAdapter);
    }

    @Override
    protected void refreshData() {
        // 请求订单数据
        HttpUtils httpUtils = new HttpUtils();
        //设置当前请求的缓存时间
        httpUtils.configCurrentHttpCacheExpiry(0);
        httpUtils.send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getOrderDetailParams(mContext, mOrderId), new OrderListener());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.et_comment:
                mIsReplyFlag = false;//回复状态复位
                mCommitCommentEt.setHint(R.string.comment_info);//hint复位
                mRLCommitComment.setVisibility(View.VISIBLE);
                mCommitCommentEt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mllCommentClick.setVisibility(View.GONE);
                break;
            case R.id.iv_commit_comment://发送按钮点击
                commit();
                break;
            case R.id.video_iv:
                intent = new Intent(mContext, PlayVideoActivity.class);
                intent.putExtra(IntentConstant.ORDER_VIDEO_URL, mVideoURL);
                jump(intent);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_query:
                if (mIsMyOrderFlg) {//如果是我的订单，且未评价过则对专家进行评价
                    if (!mIsComment) {
                        // 将参数写入application
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_STATE, mOrderState);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PRICE, mPrice);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_REPORT, mReport);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, mFirstPictureUrl);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_TO, mOwnerId);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOOD_NAME, mOwnerName);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_HEAD, mOwnerHead);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, mOrderId);
                        IdentifyApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, mIsMyOrderFlg);
                        IdentifyApplication.setIntentData(IntentConstant.QUERY_EXPERT_ID, mExpertId);
                        if (UserInfoUtils.checkUserLogin(mContext)) {
                            jump(mContext, PriceQueryContentActivity.class);
                        } else {
                            intent = new Intent(mContext, UserLogInActivity.class);
                            jump(intent);
                        }
                    }
                } else {
                    DialogUtils.showResponsibilityDialog(mContext, this);
                    UmengUtils.onEvent(mContext, EventEnum.Order_Detail_Query_Click);
                }
                break;
            case R.id.tv_ok:
                // 将参数写入application
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_STATE, mOrderState);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PRICE, mPrice);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_REPORT, mReport);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, mFirstPictureUrl);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_TO, mOwnerId);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOOD_NAME, mOwnerName);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_HEAD, mOwnerHead);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, mOrderId);
                IdentifyApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, mIsMyOrderFlg);
                IdentifyApplication.setIntentData(IntentConstant.QUERY_EXPERT_ID, mExpertId);
                if (UserInfoUtils.checkUserLogin(mContext)) {
                    jump(mContext, PriceQueryContentActivity.class);
                } else {
                    intent = new Intent(mContext, UserLogInActivity.class);
                    intent.putExtra(IntentConstant.TARGET_ACTIVITY, PriceQueryContentActivity.class.getSimpleName());
                    jump(intent);
                }
                break;
            case R.id.ll_share_wechat:
                doShare(SHARE_MEDIA.WEIXIN);
                UmengUtils.onEvent(mContext, EventEnum.UmengOrderShareWX, mMap);
                break;
            case R.id.ll_share_qq:
                doShare(SHARE_MEDIA.QQ);
                UmengUtils.onEvent(mContext, EventEnum.UmengOrderShareQQ, mMap);
                break;
            case R.id.ll_share_weibo:
                doShare(SHARE_MEDIA.SINA);
                UmengUtils.onEvent(mContext, EventEnum.UmengOrderShareSina, mMap);
                break;
            case R.id.ll_share_moments:
                doShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                UmengUtils.onEvent(mContext, EventEnum.UmengOrderCircle, mMap);
                break;
            case R.id.ll_share_qq_zone:
                doShare(SHARE_MEDIA.QZONE);
                UmengUtils.onEvent(mContext, EventEnum.UmengOrderQZone, mMap);
                break;
            case R.id.ll_expert:
                OrderDetailData.DataEntity.ExpertEntity expert = (OrderDetailData.DataEntity.ExpertEntity) view.getTag();
                String state = expert.getState();
                if ("1".equals(state)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.no_expert_exist);
                    return;
                }
                intent = new Intent(mContext, ExpertDetailActivity.class);
                intent.putExtra(IntentConstant.EXPERT_ID, expert.getId());
                jump(intent);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(UmengConstants.KEY_ORDER_DETAIL_EXPERT_ID, expert.getId());
                UmengUtils.onEvent(this, EventEnum.OrderDetailExpertClick, hashMap);
                break;
            case R.id.ll_order_collect:
                collectOrder();
                UmengUtils.onEvent(mContext, EventEnum.UmengOrderCollect, mCollectMap);
                break;
            case R.id.img_popupwindow_dismiss:
                if (mKeyWordPopupWindow != null && mKeyWordPopupWindow.isShowing()) {
                    mKeyWordPopupWindow.dismiss();
                }
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.download));
        }
        UmengUtils.onPause(this);
    }

    private void collectOrder() {
        if (!UserInfoUtils.checkUserLogin(this)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            intent.putExtra(IntentConstant.TARGET_ACTIVITY, OrderDetailActivity.class.getSimpleName());
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            UmengUtils.onEvent(mContext, EventEnum.Collect_Order_Error);
            return;
        }

        HttpUtils.sHttpCache.setEnabled(HttpRequest.HttpMethod.GET, false);
        new HttpUtils().send(HttpRequest.HttpMethod.GET,
                NetConstant.HOST,
                NetConstant.getOrderCollectParams(this, mOrderId), new OrderCollectListener());
        UmengUtils.onEvent(mContext, EventEnum.Collect_Order_Success);
    }

    @Override
    public void finish() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningTasks(1).get(0).numActivities < 2) {
            jump(mContext, MainActivity.class);
        }
        super.finish();
    }

    private void doShare(SHARE_MEDIA sharePlatform) {
        if (!UserInfoUtils.checkUserLogin(this)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            intent.putExtra(IntentConstant.TARGET_ACTIVITY, OrderDetailActivity.class.getSimpleName());
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            return;
        }
        if (TextUtils.isEmpty(mImageFileAbsPath)) {
            mImageFileAbsPath = BitmapUtils.saveBitmap(this, BitmapUtils.convertViewToBitmap(mShareContentView), "order_detail_share.png");
            UMengShareManager.getInstance(mContext).setShareContent(mShareContent, mImageFileAbsPath, UmengConstants.BASE_SHARE_ORDER_URL + mOrderId);
            UMengShareManager.getInstance(mContext).setOnShareCompleteListener(new UMengShareManager.OnShareCompleteLisnener() {
                @Override
                public void onShareComplete() {
                    new HttpUtils().send(
                            HttpRequest.HttpMethod.GET,
                            NetConstant.HOST,
                            NetConstant.getScoreParams(OrderDetailActivity.this, NetWorkRequestConstants.GET_SCORE_METHOD_SHARE, UMengShareManager.TYPE_SHARE_ORDER, mOrderId),
                            null);
                }
            });
        }
        UMengShareManager.getInstance(mContext).doShare(sharePlatform);
    }

    //直接评论
    private void commit() {
        //判断用户是否登录
        if (!UserInfoUtils.checkUserLogin(mContext)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            intent.putExtra(IntentConstant.TARGET_ACTIVITY, OrderDetailActivity.class.getSimpleName());
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            jump(intent);
        } else {
            // 获取评价内容
            String content = mCommitCommentEt.getText().toString().trim();

            if (!TextUtils.isEmpty(content)) {
                //评论实时刷新，需要关闭缓存功能
                HttpUtils.sHttpCache.setEnabled(HttpRequest.HttpMethod.GET, false);
                HashMap<String, String> map = new HashMap<>();
                if (mIsReplyFlag) {
                    //回复请求
                    new HttpUtils().send(HttpRequest.HttpMethod.POST, NetConstant.HOST,
                            NetConstant.getReplyCommentParams(mContext, mOrderId, "2", content, mReplyId), new CommentListener());
                    map.put(UmengConstants.KEY_ORDER_DETAIL_REPLY_COMMIT, content);
                } else {
                    // 提交评价
                    new HttpUtils().send(HttpRequest.HttpMethod.POST, NetConstant.HOST,
                            NetConstant.getCommentParams(mContext, mOrderId, "2", content), new CommentListener());
                    map.put(UmengConstants.KEY_ORDER_DETAIL_COMMENT_COMMIT, content);
                }
                UmengUtils.onEvent(mContext, EventEnum.OrderDetailCommentsCommit, map);
            }
            // 收起键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditCommentView.getWindowToken(), 0);
            mCommitCommentEt.setText(StringUtils.getString(""));
            mRLCommitComment.setVisibility(View.GONE);
            mllCommentClick.setVisibility(View.VISIBLE);
            mReplyId = "";//回复标记数据清零
            mIsReplyFlag = false;
        }
    }

    private void attachData(OrderDetailData response) {
        if (response.getData() != null) {
            OrderDetailData.DataEntity.GoodsEntity goods = response.getData().getGoods();
            //是否评论过
            mIsComment = (goods.getIscomment() != 0);
            mVideoURL = goods.getVideo();
            // 用户信息
            mOwnerId = goods.getUser_id();
            //用户昵称
            mOwnerName = goods.getUser_name();
            //用户头像
            mOwnerHead = goods.getHead_img();
            mIsMyOrderFlg = TextUtils.equals(mOwnerId, UserInfoUtils.getUserId(mContext));
            mPriceQueryView.setText(mIsMyOrderFlg ? (mIsComment ? R.string.already_evaluate_to_expert : R.string.evaluate_to_expert) : R.string.enquiry);
            int color = mIsMyOrderFlg ? (mIsComment ? getResources().getColor(R.color.black3) : getResources().getColor(R.color.white)) : getResources().getColor(R.color.dark1);
            mPriceQueryView.setTextColor(color);
            mPriceQueryView.setBackgroundResource(mIsMyOrderFlg ? (mIsComment ? R.drawable.bg_query_price : R.drawable.bg_query_evaluate) : R.drawable.bg_query_price);
            mUserPortraitView.setImageURI(Uri.parse(goods.getHead_img()));
            mUserNameView.setText(goods.getNikename());
            mUserContentView.setText(TextUtils.isEmpty(goods.getNote().trim()) ? getString(R.string.identify_no_tips) : goods.getNote());
            mCreateDate.setText(goods.getCreated());
            // 专家信息
            OrderDetailData.DataEntity.ExpertEntity expert = response.getData().getExpert();
            if (expert != null) {
                mReport = goods.getReport();
                mPrice = goods.getPrice();
                mExpertId = expert.getId();
                mExpertContainerView.setTag(expert);
                mExpertPortraitView.setImageURI(Uri.parse(expert.getHead_img()));
                mExpertNameView.setText(expert.getName());
                mExpertTypeView.setText(expert.getHonors());
                mExpertIdentifyTypeView.setText(goods.getJb_type());
                if (!mVideoURL.equals("")) {
                    mWebView.setVisibility(View.GONE);
                    mVideoIv.setVisibility(View.VISIBLE);
                } else {
                    mVideoIv.setVisibility(View.GONE);
                }
                mWebView.loadData(goods.getReport(), "text/html;charset=utf-8", null);
                mShareContent = StringUtils.trimHTMLTag(goods.getReport());
                mExpertPriceView.setText(StringUtils.getString(getString(R.string.evaluate), goods.getPrice()));
                //判断是否出现引导
                boolean innerGuiderFlag = SharedPreferencesUtils.getSharedPreferencesBoolean(mContext, SP_KEY_ORDER_DETAIL_INNER_GUIDER);
                mInnerGuiderView.setVisibility((!innerGuiderFlag && goods.getIskey() == 1) ? View.VISIBLE : View.GONE);
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
            // 专家团
            if (goods.getGroup_report().size() > 0) {
                mExpertGroupContainerView.setVisibility(View.VISIBLE);
                mExpertContainerView.setVisibility(View.GONE);
                mExpertAdapter.setData(goods.getGroup_report());
                mGroupPriceTv.setText(StringUtils.getString(getString(R.string.evaluate), goods.getPrice()));
            } else {
                mExpertGroupContainerView.setVisibility(View.GONE);
                mExpertContainerView.setVisibility(View.VISIBLE);
            }
            //收藏状态
            String collectionState = goods.getCollection();
            if ("1".equals(collectionState)) {
                mCollectionStateTv.setText(getString(R.string.order_collect_state_true));
            } else {
                mCollectionStateTv.setText(getString(R.string.order_collect_state_false));
            }

            // 评论
            mCommentAdapter.setData(response.getData().getComment());
            mCommentAdapter.setOnReplyActionListener(this);
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

    //评论回复的回调
    @Override
    public void onReplyAction(String replyId, String replyUserNickerName) {
        //用户登录
        if (!UserInfoUtils.checkUserLogin(mContext)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            intent.putExtra(IntentConstant.TARGET_ACTIVITY, OrderDetailActivity.class.getSimpleName());
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            jump(intent);
            return;
        }
        //弹出发送评论UI
        mRLCommitComment.setVisibility(View.VISIBLE);
        mCommitCommentEt.setHint(StringUtils.getString("回复 ", replyUserNickerName));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mllCommentClick.setVisibility(View.GONE);
        //准备回复的UI
        mIsReplyFlag = true;//回复状态置位
        mReplyId = replyId;//回复id
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

    private class OrderCollectListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AttentionCollectionResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AttentionCollectionResponse> task = new StringToBeanTask<>(AttentionCollectionResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, "收藏失败" + s);
        }

        @Override
        public void onConvertSuccess(AttentionCollectionResponse response) {
            DialogUtils.showShortPromptToast(mContext, response.getData().getData());
            if (response.getData().isCollect()) {
                mCollectionStateTv.setText("取消收藏");
            } else {
                mCollectionStateTv.setText("收藏");
            }
        }

        @Override
        public void onConvertFailed() {

        }
    }


    private class CommentListener extends RequestCallBack<String> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            refreshData();
            DialogUtils.showShortPromptToast(mContext, getString(R.string.comment_success));
            //评论获取积分
            new HttpUtils().send(
                    HttpRequest.HttpMethod.GET,
                    NetConstant.HOST,
                    NetConstant.getScoreParams(mContext,
                            NetWorkRequestConstants.GET_SCORE_METHOD_COMMENT,
                            UMengShareManager.TYPE_SHARE_ORDER, mOrderId), null);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.comment_fail);
            e.printStackTrace();
        }
    }
}