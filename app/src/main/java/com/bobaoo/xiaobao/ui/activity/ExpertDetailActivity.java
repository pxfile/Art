package com.bobaoo.xiaobao.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.NetWorkRequestConstants;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.AttentionCollectionResponse;
import com.bobaoo.xiaobao.domain.ExpertDetailInfoData;
import com.bobaoo.xiaobao.manager.UMengShareManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.AlbumAdapter;
import com.bobaoo.xiaobao.ui.adapter.ExpertDetailCommentAdapter;
import com.bobaoo.xiaobao.ui.dialog.ChooseIdentifyTypeDialog;
import com.bobaoo.xiaobao.ui.popupwindow.CustomShareBoard;
import com.bobaoo.xiaobao.ui.widget.fix.FixedListView;
import com.bobaoo.xiaobao.utils.AnimatorUtils;
import com.bobaoo.xiaobao.utils.BitmapUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
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

/**
 * Created by you on 2015/6/5.
 */
public class ExpertDetailActivity extends BaseActivity {

    private SimpleDraweeView mPortraitView;
    private TextView mNameView;
    private TextView mDescView;
    private TextView mIdentifyNumView;
    private TextView mFansNumView;
    private TextView mAppreciateNumView;
    private TextView mGoodAtView;
    private TextView mLevelView;
    private TextView mAssistView;
    private TextView mIntroductionView;
    private View mAlbumContainerView;
    private RecyclerView mAlbumView;
    private TextView mSubmitView;
    private View mShareContentView;

    private ExpertDetailListener mListener;

    private AlbumAdapter mAlbumAdapter;

    private String mExpertId;
    private boolean mIsOnlineExpert;
    private String mImageFileAbsPath;
    private CustomShareBoard mShareBoard;
    private ImageView mAttentionImg;
    private View mAttentionAnimatorView;
    private boolean mCollectState;
    private LinearLayout mllScroll;
    private int mIdentifyType;
    private int mDefaultIdentifyType = AppConstant.IdentifyTypeBronze;
    private String mIdentifyMethodSwitchInfos;//记录该专家是否开通了相应的鉴定方式
    private String mIdentifyMethodPrices;//记录该专家鉴定方式的价格

    private TextView mShareView;
    private TextView mWarmPrompt;

    private TextView mAttentionTv;
    private final String CAN_NOT_APPOINTMENT = "0";

    private boolean mIdentifyTypeRegistration;

    private FixedListView mCommentListView;
    private ExpertDetailCommentAdapter mCommentAdapter;
    private String[] mIdentifyItemArr = new String[]{};
    private List<ExpertDetailInfoData.DataEntity.KindArrEntity> mIdentifyItemList = new ArrayList<>();
    private String strItems;
    private ChooseIdentifyTypeDialog mDialog;
    private AdapterView.OnItemClickListener mDialogListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mIdentifyType = getDefaultIdentifyType(mIdentifyItemArr[position]);
            Intent intent = new Intent(mContext, TakePictureActivity.class);
            intent.putExtra(IntentConstant.IS_ONLINE_EXPERT, mIsOnlineExpert);
            intent.putExtra(IntentConstant.IDENTIFY_TYPE_REGISTRATION, mIdentifyTypeRegistration);
            intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
            intent.putExtra(IntentConstant.IdentifyType, mIdentifyType);
            intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
            intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
            mDialog.dismiss();
            jump(intent);
        }
    };
    private TextView mNoEvaluateTv;

    @Override
    protected void getIntentData() {
        mExpertId = getIntent().getStringExtra(IntentConstant.EXPERT_ID);
        mIsOnlineExpert = getIntent().getBooleanExtra(IntentConstant.IS_ONLINE_EXPERT, false);
    }

    @Override
    protected void initData() {
        mListener = new ExpertDetailListener();
        mAlbumAdapter = new AlbumAdapter();
        mCommentAdapter = new ExpertDetailCommentAdapter(mContext);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_expert_detail;
    }

    @Override
    protected void initTitle() {
        // 设置背景色为透明
        View titleView = findViewById(R.id.layout_title);
        titleView.setBackgroundColor(Color.TRANSPARENT);
        // 设置返回按钮
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("");

        setOnClickListener(backView, mAttentionImg);
    }

    @Override
    protected void initContent() {
        mCommentListView = (FixedListView) findViewById(R.id.flv_comment);
        mNoEvaluateTv = (TextView) findViewById(R.id.no_evaluate_tv);
        mWarmPrompt = (TextView) findViewById(R.id.warm_prompt_online_tv);
        mShareView = (TextView) findViewById(R.id.share_tv);
        mPortraitView = (SimpleDraweeView) findViewById(R.id.sdv_portrait);
        mNameView = (TextView) findViewById(R.id.tv_name);
        mDescView = (TextView) findViewById(R.id.tv_desc);
        mIdentifyNumView = (TextView) findViewById(R.id.tv_identify);
        mFansNumView = (TextView) findViewById(R.id.tv_fans);
        mAppreciateNumView = (TextView) findViewById(R.id.tv_appreciate);
        mGoodAtView = (TextView) findViewById(R.id.tv_good_at);
        mLevelView = (TextView) findViewById(R.id.tv_level);
        mAssistView = (TextView) findViewById(R.id.tv_assist);
        mIntroductionView = (TextView) findViewById(R.id.tv_introduction);
        // 人群和场景
        mAlbumContainerView = findViewById(R.id.ll_album);
        mAlbumView = (RecyclerView) mRootView.findViewById(R.id.rv_album);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAlbumView.setLayoutManager(manager);
        // 提交
        mSubmitView = (TextView) findViewById(R.id.tv_submit);
        mShareContentView = findViewById(R.id.share_content);
        mllScroll = (LinearLayout) findViewById(R.id.ll_scroll);
        setOnClickListener(mSubmitView);
        UMengShareManager.getInstance(mContext).setOnShareCompleteListener(new UMengShareManager.OnShareCompleteLisnener() {
            @Override
            public void onShareComplete() {
                new HttpUtils().send(
                        HttpRequest.HttpMethod.GET,
                        NetConstant.HOST,
                        NetConstant.getScoreParams(mContext, NetWorkRequestConstants.GET_SCORE_METHOD_SHARE, UMengShareManager.TYPE_SHARE_EXPERT, mExpertId), null);
            }
        });
        //关注
        mAttentionImg = (ImageView) mRootView.findViewById(R.id.img_attention_expert);
        mAttentionAnimatorView = mRootView.findViewById(R.id.img_animator_attention);
        mAttentionAnimatorView.setVisibility(View.GONE);
        mAttentionTv = (TextView) findViewById(R.id.attention_tv);
        mAttentionTv.setText(R.string.attention);
        setOnClickListener(mAttentionImg, mShareView);
    }
    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        // 请求专家详情数据
        HttpUtils expertHttp = new HttpUtils();
        expertHttp.send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getExpertDetailParams(mContext, mExpertId), mListener);
        // 相册
        mAlbumView.setAdapter(mAlbumAdapter);
        mCommentListView.setAdapter(mCommentAdapter);
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        HashMap<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.tv_submit:
                //在线专家鉴定
                if (getString(R.string.identify_type_registration).equals(mSubmitView.getText().toString().trim())) {
                    mIdentifyItemArr = getItemArr();
                    if (mIdentifyItemArr.length > 1) {
                        mDialog = DialogUtils.showItemsDialog(mContext, mIdentifyItemArr, mDialogListener);
                    } else {
                        intent = new Intent(mContext, TakePictureActivity.class);
                        intent.putExtra(IntentConstant.IS_ONLINE_EXPERT, mIsOnlineExpert);
                        intent.putExtra(IntentConstant.IDENTIFY_TYPE_REGISTRATION, mIdentifyTypeRegistration);
                        intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
                        intent.putExtra(IntentConstant.IdentifyType, mDefaultIdentifyType);
                        intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
                        intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
                        jump(intent);
                    }
                } else {
                    intent = new Intent(mActivity, SubmitOrderActivity.class);
                    intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
                    intent.putExtra(IntentConstant.EXPERT_NAME, mNameView.getText().toString().trim());
                    map.put(UmengConstants.KEY_EXPERT_ORDER_ID, mExpertId);
                    intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
                    intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
                    jump(intent);
                }
                UmengUtils.onEvent(this, EventEnum.ExpertPageListOrderExpertClick, map);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.share_tv:
                showShareBoard();
                HashMap<String, String> unengMap = new HashMap<>();
                unengMap.put(UmengConstants.KEY_SHARE_CONTENT_ID, mExpertId);
                UmengUtils.onEvent(this, EventEnum.ShareExpertEntry, unengMap);
                break;
            case R.id.img_attention_expert:
                attentionExpert();
                HashMap<String, String> attentionMap = new HashMap<>();
                attentionMap.put(UmengConstants.KEY_ATTENTION_EXPERT_ID, mExpertId);
                UmengUtils.onEvent(this, EventEnum.AttentionExpertEntry, attentionMap);
                break;
            default:
                super.onClick(view);
                break;
        }
    }


    private String[] getItemArr() {
        strItems = null;
        for (int i = 0; i < mIdentifyItemList.size(); i++) {
            strItems = (strItems == null ? StringUtils.getString(mIdentifyItemList.get(i).getName()) :
                    StringUtils.getString(strItems, ",", mIdentifyItemList.get(i).getName()));
        }
        return strItems.split(",");
    }

    private void attentionExpert() {

        Intent intent;//收藏专家
        if (!UserInfoUtils.checkUserLogin(this)) {
            intent = new Intent(mContext, UserLogInActivity.class);
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.not_login);
            return;
        }
        HttpUtils.sHttpCache.setEnabled(HttpRequest.HttpMethod.GET, false);
        new HttpUtils().send(HttpRequest.HttpMethod.GET,
                NetConstant.HOST,
                NetConstant.getExpertAttentionParams(this, mExpertId), new ExpertAttentionListener());
    }

    /**
     * 资讯收藏
     */
    private class ExpertAttentionListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AttentionCollectionResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AttentionCollectionResponse> task = new StringToBeanTask<>(AttentionCollectionResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, StringUtils.getString(getString(R.string.attention_failed), s));
        }

        @Override
        public void onConvertSuccess(AttentionCollectionResponse response) {
            DialogUtils.showShortPromptToast(mContext, response.getData().getData());
            mCollectState = response.getData().isCollect();
            mAttentionAnimatorView.setVisibility(View.VISIBLE);
            mAttentionTv.setText(R.string.attention_already);
            AnimatorUtils.startStarAnimator(mAttentionAnimatorView, 600, mCollectState, mCollectionAnimatorListenerAdapter);
        }

        @Override
        public void onConvertFailed() {
        }
    }

    private AnimatorListenerAdapter mCollectionAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mAttentionAnimatorView.setVisibility(mCollectState ? View.VISIBLE : View.GONE);
            mAttentionTv.setText(mCollectState ? R.string.attention_already : R.string.attention);
            mAttentionImg.setImageResource(mCollectState ? R.drawable.attention : R.drawable.attention_cancle);
        }
    };

    private void showShareBoard() {
        Intent intent;//弹出分享面板
        if (!UserInfoUtils.checkUserLogin(this)) {
            intent = new Intent(mContext, UserLogInActivity.class);
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.not_login);
            return;
        }
        mShareBoard = new CustomShareBoard(this);
        mShareBoard.setId(mExpertId);
        mShareBoard.setShareType(UmengConstants.SHARE_TYPE_EXPERT);
        if (TextUtils.isEmpty(mImageFileAbsPath)) {
            mImageFileAbsPath = BitmapUtils.saveBitmap(this, BitmapUtils.convertViewToBitmap(mShareContentView), "expert_share.png");
        }
        String introduce = mIntroductionView.getText().toString().trim();
        mShareBoard.setShareContent(
                StringUtils.getString(
                        getString(R.string.expert_share_title_prefix),
                        mNameView.getText().toString().trim(),
                        ": ",
                        introduce),
                mImageFileAbsPath,StringUtils.getString(UmengConstants.BASE_SHARE_EXPERT_URL, mExpertId));
        mShareBoard.applayBlur();
        mShareBoard.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void finish() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningTasks(1).get(0).numActivities < 2) {
            jump(mContext, MainActivity.class);
        }
        super.finish();
    }

    private void attachData(ExpertDetailInfoData.DataEntity data) {
        mIdentifyItemList.addAll(data.getKindArr());
        mCommentAdapter.setData(data.getCommentArr());
        mNoEvaluateTv.setVisibility(data.getCommentArr().size() == 0 ? View.VISIBLE : View.GONE);
        mPortraitView.setImageURI(Uri.parse(data.getHead_img()));
        mNameView.setText(data.getName());
        mDescView.setText(data.getHonors());
        mIdentifyNumView.setText(String.format(getString(R.string.indicator_num), data.getJbcount()));
        mFansNumView.setText(String.format(getString(R.string.fans_num), data.getGzcount()));
        mAppreciateNumView.setText(String.format(getString(R.string.appreciate_num), data.getComment()));
        mGoodAtView.setText(data.getKind());
//        mIdentifyType = getDefaultIdentifyType(data.getKind());
        mDefaultIdentifyType = getDefaultIdentifyType(data.getKind());
        mLevelView.setText(data.getStar_level());
        mAssistView.setText(data.getZhuli());
        mIntroductionView.setText(data.getIntro());
        if (data.getPhoto().size() > 0) {
            mAlbumContainerView.setVisibility(View.VISIBLE);
            mAlbumAdapter.setData(data.getPhoto());
            mAlbumAdapter.notifyDataSetChanged();
        } else {
            mAlbumContainerView.setVisibility(View.GONE);
        }
        boolean isCollected = "1".equals(data.getIsfans());
        mAttentionAnimatorView.setVisibility(isCollected ? View.VISIBLE : View.GONE);
        mAttentionTv.setText(isCollected ? R.string.attention_already : R.string.attention);
        mAttentionImg.setImageResource(isCollected ? R.drawable.attention : R.drawable.attention_cancle);

        if (data.getLevel() > 55 || data.getLevel() < 11) {
            mSubmitView.setVisibility(CAN_NOT_APPOINTMENT.equals(data.getJbapp_yy()) ? View.GONE : View.VISIBLE);
            mSubmitView.setText(R.string.appointment);
            mIdentifyTypeRegistration=false;

        } else {
            mSubmitView.setVisibility(data.getLevel() < 33 ? View.GONE : View.VISIBLE);
            mWarmPrompt.setVisibility(data.getLevel() < 33 ? View.GONE : View.VISIBLE);
            mSubmitView.setText(R.string.identify_type_registration);
            mIdentifyTypeRegistration=true;
        }
    }

    /**
     * 获取该挂号订单的默认藏品类型
     */
    private int getDefaultIdentifyType(String kind) {
        if (TextUtils.isEmpty(kind)) {
            return AppConstant.IdentifyTypeBronze;
        }
        String[] kinds = kind.split("、");
        String defaultIdentifyTypeName = kinds[0];
        for (int i = 0; i < AppConstant.IDENTIFY_KIND_TABLE.length; i++) {
            if (AppConstant.IDENTIFY_KIND_TABLE[i].equals(defaultIdentifyTypeName)) {
                int result = i + 1;
                if (result == AppConstant.IdentifyTypeMoney) {
                    result = AppConstant.IdentifyTypeMoneyBronze;
                }
                return result;
            }
        }
        return AppConstant.IdentifyTypeChina;
    }

    /**
     * 请求专家详情数据
     */
    private class ExpertDetailListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<ExpertDetailInfoData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<ExpertDetailInfoData> task = new StringToBeanTask<>(ExpertDetailInfoData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            e.printStackTrace();
        }

        @Override
        public void onConvertSuccess(ExpertDetailInfoData response) {
            getIdentifyMethodAndPriceInfo(response);
            attachData(response.getData());
        }

        @Override
        public void onConvertFailed() {
        }
    }

    /**
     * 抽取专家支持的鉴定方式及价格
     */
    private void getIdentifyMethodAndPriceInfo(ExpertDetailInfoData expertDetail) {
        //Jbapp_pt等鉴定方式，0代表未开通，1代表已开通
        ExpertDetailInfoData.DataEntity data = expertDetail.getData();
        mIdentifyMethodSwitchInfos = StringUtils.getString(data.getJbapp_pt(), ",", data.getJbapp_js(), ",", data.getJbapp_sp(), ",", data.getJbapp_yy());
        mIdentifyMethodPrices = StringUtils.getString(data.getPt_price(), ",", data.getJs_price(), ",", data.getSp_price(), ",", data.getYy_price());
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
