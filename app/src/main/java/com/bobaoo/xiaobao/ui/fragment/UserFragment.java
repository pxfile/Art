package com.bobaoo.xiaobao.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.UserInfo;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.activity.ExpertListActivity;
import com.bobaoo.xiaobao.ui.activity.InviteFriendActivity;
import com.bobaoo.xiaobao.ui.activity.UserBanlanceActivity;
import com.bobaoo.xiaobao.ui.activity.UserFeedBackActivity;
import com.bobaoo.xiaobao.ui.activity.UserIdentifyActivity;
import com.bobaoo.xiaobao.ui.activity.UserIdentifyMeetingActivity;
import com.bobaoo.xiaobao.ui.activity.UserLogInActivity;
import com.bobaoo.xiaobao.ui.activity.UserPrivateInfoActivity;
import com.bobaoo.xiaobao.ui.activity.UserRechargeSelectActivity;
import com.bobaoo.xiaobao.ui.activity.UserScoreActivity;
import com.bobaoo.xiaobao.ui.activity.UserSetActivity;
import com.bobaoo.xiaobao.ui.activity.UserSubActivity;
import com.bobaoo.xiaobao.ui.activity.UserWalletActivity;
import com.bobaoo.xiaobao.ui.activity.WebViewActivity;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.AnimatorUtils;
import com.bobaoo.xiaobao.utils.AppUtils;
import com.bobaoo.xiaobao.utils.DeviceUtil;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.NetUtils;
import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UriUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by star on 15/5/29.
 */
public class UserFragment extends BaseFragment {
    private static final int NO_LOGIN = 0;
    private static final int LOAD_SUCCESS = 1;
    private static final int LOAD_FAIL = 2;

    private View mSettingsEntryView;
    private SimpleDraweeView mPortraitView;
    private TextView mNickNameTv;
    private TextView mScanCode;

    private TextView mCollectionCountView;
    private TextView mFansCountView;
    private TextView mCommentCountView;
    private TextView mPriceQueryCountView;

    private TextView mBalanceTv;
    private TextView mScoreTv;
    private ImageView mChargeImg;

    private String mUserId;
    private String mBalance;
    private String mScore;

    private View mIdentifyNoPayView;
    private TextView mBubbleTvPayment;
    private TextView mBubbleTvIdentify;
    private TextView mBubbleTvFeedback;
    private TextView mBubbleTvIdentified;
    private View mIdentifyNoIdentifyView;
    private View mIdentifyPaidView;

    private boolean mShowAnimatorFlag = true;
    private ProgressDialog mProgressDialog;
    private int mShowDialogFlag;
    private ShowDialogHandler showDialogHandler = new ShowDialogHandler(this);

    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_userjianbaoinfo;
    }

    @Override
    protected void initContent() {
        mSettingsEntryView = mRootView.findViewById(R.id.iv_setting);
        mPortraitView = (SimpleDraweeView) mRootView.findViewById(R.id.img_head);
        mNickNameTv = (TextView) mRootView.findViewById(R.id.tv_nick_name);
        mScanCode = (TextView) mRootView.findViewById(R.id.tv_scan_code);

        View mCommentView = mRootView.findViewById(R.id.ll_comments_entry);
        View mFansView = mRootView.findViewById(R.id.ll_fans_entry);
        View mCollectionView = mRootView.findViewById(R.id.ll_collections_entry);
        View mPriceQueryView = mRootView.findViewById(R.id.ll_price_query_entry);

        mCollectionCountView = (TextView) mRootView.findViewById(R.id.tv_collection_count);
        mFansCountView = (TextView) mRootView.findViewById(R.id.tv_fans_count);
        mCommentCountView = (TextView) mRootView.findViewById(R.id.tv_comment_count);
        mPriceQueryCountView = (TextView) mRootView.findViewById(R.id.tv_price_query_count);

        setOnClickListener(mCollectionView, mCommentView, mFansView, mPortraitView, mScanCode, mPriceQueryView, mSettingsEntryView);

        mIdentifyNoPayView = mRootView.findViewById(R.id.ll_identify_no_pay);
        mBubbleTvPayment = (TextView) mRootView.findViewById(R.id.tv_bubble_payment);
        mBubbleTvIdentify = (TextView) mRootView.findViewById(R.id.tv_bubble_identity);
        mBubbleTvFeedback = (TextView) mRootView.findViewById(R.id.tv_bubble_feedback);
        mBubbleTvIdentified = (TextView) mRootView.findViewById(R.id.tv_bubble_identified);
        mIdentifyNoIdentifyView = mRootView.findViewById(R.id.ll_identify_no_identify);
        mIdentifyPaidView = mRootView.findViewById(R.id.ll_identify_identified);
        View mAccountBalanceView = mRootView.findViewById(R.id.ll_account_balance);
        View mAccountScoreView = mRootView.findViewById(R.id.ll_account_score);
        View mAccountRechargeView = mRootView.findViewById(R.id.ll_account_recharge);
        View mCheckWalletView = mRootView.findViewById(R.id.rl_check_wallet);

        View mCheckIdentifyMeetingView = mRootView.findViewById(R.id.rl_identify_meeting);
        View mInviteFriendView = mRootView.findViewById(R.id.rl_invite_friend);
        View mIdentifyTipView = mRootView.findViewById(R.id.rl_user_identify_tip);
        View mProblemsView = mRootView.findViewById(R.id.rl_user_problems);
        View mFeedBackView = mRootView.findViewById(R.id.rl_suggestion_feedback);

        setOnClickListener(mIdentifyNoPayView, mIdentifyNoIdentifyView, mIdentifyPaidView, mAccountBalanceView, mAccountScoreView,
                mAccountRechargeView, mCheckWalletView, mIdentifyTipView, mFeedBackView, mProblemsView, mCheckIdentifyMeetingView, mInviteFriendView);

        mBalanceTv = (TextView) mRootView.findViewById(R.id.tv_user_account_balance);
        mScoreTv = (TextView) mRootView.findViewById(R.id.tv_user_score);
        mChargeImg = (ImageView) mRootView.findViewById(R.id.img_charge);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<>();
        if (!NetUtils.isNetworkConnected(mContext)) {
            DialogUtils.showShortPromptToast(mContext, R.string.cannot_connect_network);
        }
        Intent intent;
        //用户未登录，则在点击事件中直接跳转登录界面
        if (!UserInfoUtils.checkUserLogin(mContext)) {
            intent = new Intent(mContext, UserLogInActivity.class);
            getActivity().startActivity(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.not_login);
            return;
        }
        switch (view.getId()) {
            case R.id.iv_setting://设置
                intent = new Intent(mContext, UserSetActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.User_Setting);
                jump(intent);
                break;
            case R.id.ll_identify_no_pay://待支付
                intent = new Intent(mContext, UserIdentifyActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageIdentifyNoPayClick, map);
                break;
            case R.id.ll_identify_no_identify://待鉴定
                intent = new Intent(mContext, UserIdentifyActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_IDENTIFY);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageIdentifyNoidentifyClick, map);
                break;
            case R.id.ll_identify_identified://已完成
                intent = new Intent(mContext, UserIdentifyActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_IDENTIFIED);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageIdentifiedClick, map);
                break;
            case R.id.rl_check_wallet://查看我的钱包
                intent = new Intent(mContext, UserWalletActivity.class);
                intent.putExtra(IntentConstant.USER_ID, mUserId);
                intent.putExtra(IntentConstant.USER_ACCOUNT_BALANCE, mBalance);
                intent.putExtra(IntentConstant.USER_SCORE, mScore);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageWalletClick, map);
                getActivity().startActivity(intent);
                break;
            case R.id.ll_account_balance://账户余额
                intent = new Intent(mContext, UserBanlanceActivity.class);
                intent.putExtra(IntentConstant.USER_ACCOUNT_BALANCE, mBalance);
                intent.putExtra(IntentConstant.USER_ID, mUserId);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageBalance, map);
                break;
            case R.id.ll_account_score://剩余积分
                intent = new Intent(mContext, UserScoreActivity.class);
                intent.putExtra(IntentConstant.USER_SCORE, mScore);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageScore, map);
                break;

            case R.id.ll_account_recharge://我要充值
                intent = new Intent(mContext, UserRechargeSelectActivity.class);
                intent.putExtra(IntentConstant.USER_ID, mUserId);
                startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageRechargeClick, map);
                break;
            case R.id.rl_identify_meeting://我的鉴定会
                intent = new Intent(mContext, UserIdentifyMeetingActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageIdentifyMeetingClick, map);
                break;
            case R.id.rl_invite_friend://邀请好友
                // 2015/9/29 跳转到邀请好友页
                jump(mContext, InviteFriendActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.UserInvite);
                break;
            case R.id.rl_user_identify_tip://鉴定须知
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(IntentConstant.WEB_URL, NetConstant.IDENTIFY_TIP);
                intent.putExtra(IntentConstant.WEB_TITLE, getString(R.string.user_identify_tip));
                jump(intent);
                UmengUtils.onEvent(mContext, EventEnum.UserIdentifyTip, map);
                break;
            case R.id.rl_user_problems://常见问题
//                jump(mContext, ProblemsActivity.class);
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(IntentConstant.WEB_URL, NetConstant.IDENTIFY_FAQ);
                intent.putExtra(IntentConstant.WEB_TITLE, getString(R.string.user_problems));
                jump(intent);
                UmengUtils.onEvent(mContext, EventEnum.UserProblems);
                break;
            case R.id.rl_suggestion_feedback://意见反馈
                jump(mContext, UserFeedBackActivity.class);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageFeedBackClick, map);
                break;
            case R.id.img_head:
                jump(mContext, UserPrivateInfoActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.UserPageChangeInfoClick);
                break;
            case R.id.ll_collections_entry:
                intent = new Intent(mContext, UserSubActivity.class);
                intent.putExtra(IntentConstant.TARGET_FRAGMENT, UserSubActivity.Collection);
                jump(intent);
                break;
            case R.id.ll_fans_entry:
                intent = new Intent(mContext, ExpertListActivity.class);
                intent.putExtra(IntentConstant.ORGANIZATION_NAME, R.string.attention);
                jump(intent);
                break;
            case R.id.ll_comments_entry:
                intent = new Intent(mContext, UserSubActivity.class);
                intent.putExtra(IntentConstant.TARGET_FRAGMENT, UserSubActivity.Comment);
                jump(intent);
                break;
            case R.id.ll_price_query_entry:
                intent = new Intent(mContext, UserSubActivity.class);
                intent.putExtra(IntentConstant.TARGET_FRAGMENT, UserSubActivity.PriceQuery);
                jump(intent);
                break;
            case R.id.tv_scan_code:
                //添加dialog用于显示二维码
                // 2015/9/29 跳转到邀请好友页
                jump(mContext, InviteFriendActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.UserScanCode);
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.data_download));
        }
        if (UserInfoUtils.checkUserLogin(mContext)) {
            //用户登录后 头像可能随时改动,需要刷新
            mIdentifyNoIdentifyView.setSelected(false);
            mIdentifyNoPayView.setSelected(false);
            mIdentifyPaidView.setSelected(false);
            mBubbleTvPayment.setVisibility(View.GONE);//先隐藏掉气泡
            mBubbleTvIdentify.setVisibility(View.GONE);
            mBubbleTvFeedback.setVisibility(View.GONE);
            mBubbleTvIdentified.setVisibility(View.GONE);
            startUserInfoRequest();
            updateHeadImageView();
        } else {
            mShowDialogFlag = NO_LOGIN;
            new Thread(new DialogDismiss()).start();
            mNickNameTv.setText(R.string.no_registered_account);
            mBalanceTv.setText("0");
            mScoreTv.setText("0");
            mFansCountView.setText("");
            mCommentCountView.setText("");
            mPriceQueryCountView.setText("");
            mCollectionCountView.setText("");
            mPortraitView.setImageURI(UriUtils.getResourceUri(mContext, R.drawable.icon_default));

            mIdentifyNoIdentifyView.setSelected(true);
            mIdentifyNoPayView.setSelected(true);
            mIdentifyPaidView.setSelected(true);
            mBubbleTvPayment.setVisibility(View.GONE);
            mBubbleTvIdentify.setVisibility(View.GONE);
            mBubbleTvFeedback.setVisibility(View.GONE);
            mBubbleTvIdentified.setVisibility(View.GONE);
        }
    }

    private void updateHeadImageView() {
        String cacheHeadImgUrl = UserInfoUtils.getCacheHeadImagePath(mContext);
        if (!TextUtils.isEmpty(cacheHeadImgUrl)) {
            mPortraitView.setImageURI(Uri.parse(cacheHeadImgUrl));
        }
    }

    private void startUserInfoRequest() {
        String versionName = AppUtils.getAppVersionName(mContext);
        String channelName = AppUtils.getMetaDataValue(mContext, "UMENG_CHANNEL", "baidu");
        String model = DeviceUtil.getPhoneModel();
        try {
            versionName = URLEncoder.encode(versionName, "UTF-8");
            channelName = URLEncoder.encode(channelName, "UTF-8");
            model = URLEncoder.encode(model, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        new HttpUtils().configCurrentHttpCacheExpiry(0)
                .send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getUserInfoParams(mContext, versionName, channelName, model), new UserInfoListener());
    }

    private class DialogDismiss implements Runnable {
        @Override
        public void run() {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            Message msg = Message.obtain();
            msg.what = mShowDialogFlag;
            showDialogHandler.sendMessage(msg);
        }
    }

    private DialogInterface.OnClickListener mConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mShowDialogFlag == NO_LOGIN) {
                Intent intent = new Intent(mContext, UserLogInActivity.class);
                jump(intent);
            } else {
                if (!NetUtils.isNetworkConnected(mContext)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.cannot_connect_network);
                } else {
                    startUserInfoRequest();
                }
            }
        }
    };

    static class ShowDialogHandler extends Handler {
        WeakReference<UserFragment> mUserFragmentWeakReference;

        public ShowDialogHandler(UserFragment mUserFragment) {
            this.mUserFragmentWeakReference = new WeakReference<>(mUserFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            UserFragment userFragment = mUserFragmentWeakReference.get();
            switch (msg.what) {
                case NO_LOGIN:
                    DialogUtils.showConfirmDialog(userFragment.mContext, R.string.not_login, R.string.confirm_login, userFragment.mConfirmListener);
                    break;
                case LOAD_FAIL:
                    DialogUtils.showConfirmDialog(userFragment.mContext, R.string.loading_failed, R.string.confirm_loading, userFragment.mConfirmListener);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private class UserInfoListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserInfo> {
        @Override
        public void onConvertSuccess(UserInfo data) {
            if (data != null) {
                mShowDialogFlag = LOAD_SUCCESS;
                new Thread(new DialogDismiss()).start();
                UserInfo.DataEntity.UserEntity user = data.getData().getUser();
                UserInfo.DataEntity.CostEntity cost = data.getData().getCost();
                if (!TextUtils.isEmpty(user.getHead_img())) {
                    mPortraitView.setImageURI(Uri.parse(user.getHead_img()));
                    UserInfoUtils.saveCacheHeadImagePath(mContext, user.getHead_img());
                } else {
                    mPortraitView.setImageURI(UriUtils.getResourceUri(mContext, R.drawable.icon_default));
                }
                String userName = user.getUser_name();
                if (userName.length() > 7) {
                    userName = StringUtils.getString(userName.substring(0, 6), "...");
                }
                mNickNameTv.setText(userName);
                mCollectionCountView.setText(String.valueOf(cost.getJb() + cost.getNews()));
                mFansCountView.setText(cost.getFans());
                mCommentCountView.setText(cost.getComment());
                mPriceQueryCountView.setText(cost.getAskprice());

                mScore = user.getIntegral();
                mScoreTv.setText(mScore);
                mBalance = cost.getBalance();
                mBalanceTv.setText(mBalance);

                mUserId = user.getUser_id();
                if (mShowAnimatorFlag) {
                    AnimatorUtils.startRotationZAnim(mChargeImg, 2, 160, null, 0f, 20f, 0f, -20f, 0f);
                    mShowAnimatorFlag = false;
                }
                int noChargNum = 0;
                try {
                    noChargNum = Integer.parseInt(cost.getNocharg());
                } catch (NumberFormatException e) {

                }
                SharedPreferencesUtils.setSharedPreferencesBoolean(mContext, AppConstant.HAS_NOCHARGE_IDENTIFY, noChargNum > 0);
                showBubble(cost.getNocharg(), mBubbleTvPayment);
                showBubble(cost.getWait(), mBubbleTvIdentify);
                showBubble(cost.getFeedback(), mBubbleTvFeedback);
                showBubble(cost.getFinish(), mBubbleTvIdentified);

            }
        }

        @Override
        public void onConvertFailed() {
            if (mContext != null) {
                mShowDialogFlag = LOAD_FAIL;
                new Thread(new DialogDismiss()).start();
                mNickNameTv.setText(UserInfoUtils.getUserNickName(mContext));
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserInfo> task = new StringToBeanTask<>(UserInfo.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mContext != null) {
                mShowDialogFlag = LOAD_FAIL;
                new Thread(new DialogDismiss()).start();
                mNickNameTv.setText(UserInfoUtils.getUserNickName(mContext));
            }
        }
    }

    private void showBubble(String noCharg, TextView noChargeTv) {
        if (TextUtils.isEmpty(noCharg)) {
            return;
        }
        int noChargeCount = Integer.parseInt(noCharg);
        if (noChargeCount > 0) {
            if (noChargeCount > 99) {
                noCharg = String.valueOf(99);
            }
            noChargeTv.setText(noCharg);
            noChargeTv.setVisibility(View.VISIBLE);
        } else {
            noChargeTv.setVisibility(View.GONE);
        }
    }
}
