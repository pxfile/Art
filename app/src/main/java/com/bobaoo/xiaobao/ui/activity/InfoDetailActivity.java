package com.bobaoo.xiaobao.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.NetWorkRequestConstants;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.AttentionCollectionResponse;
import com.bobaoo.xiaobao.domain.InfoDetailData;
import com.bobaoo.xiaobao.manager.UMengShareManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.InfoDetailAdapter;
import com.bobaoo.xiaobao.ui.fragment.InfoActivity;
import com.bobaoo.xiaobao.ui.popupwindow.CustomShareBoard;
import com.bobaoo.xiaobao.utils.AnimatorUtils;
import com.bobaoo.xiaobao.utils.BitmapUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.HashMap;

/**
 * Created by star on 15/6/10.
 */
public class InfoDetailActivity extends BaseActivity {
    private TextView mHeaderBackView;
    private RecyclerView mRecycleView;
    private InfoDetailAdapter mAdapter;
    private InfoDetailListener mListener;
    private String mInfoId;
    private String isClickCommit;
    private CustomShareBoard mShareBoard;
    private String mImageFileAbsPath;
    private ImageView mCollectInfoImg;
    private EditText mEditCommentView;
    private View mShareView;

    private View mAttentionAnimatorView;
    private boolean mCollectState;

    private InfoDetailData mData;
    private RelativeLayout mRLComment;
    private ImageView mIvCommit;
    private EditText mCommitComment;
    private LinearLayout mLLCommit;
    //Activity最外层的Layout视图
    private View mActivityRootView;
    //屏幕高度
    private int mScreenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int mKeyHeight = 0;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mInfoId = intent.getStringExtra(IntentConstant.INFO_ID);
        isClickCommit = intent.getStringExtra(InfoActivity.IS_CLICK_COMMENT);
    }

    @Override
    protected void initData() {
        mAdapter = new InfoDetailAdapter();
        mListener = new InfoDetailListener();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_info_detail;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        mHeaderBackView = (TextView) findViewById(R.id.tv_back);
        setOnClickListener(mHeaderBackView);
    }

    @Override
    protected void initContent() {
        mRecycleView = (RecyclerView) findViewById(R.id.rv_info);
        mRLComment = (RelativeLayout) findViewById(R.id.rl_comment_commit);
        mIvCommit = (ImageView) findViewById(R.id.iv_commit_comment);
        mLLCommit = (LinearLayout) findViewById(R.id.ll_commit_comment);
        mCommitComment = (EditText) findViewById(R.id.et_comment_content);
        // 写评论
        mEditCommentView = (EditText) findViewById(R.id.et_comment);
        // 设置纵向滚动
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mActivityRootView = findViewById(R.id.ll_out);
        //获取屏幕高度
        mScreenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        mKeyHeight = mScreenHeight / 3;
        mCommitComment.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mCommitComment.setSingleLine(false);
        mCommitComment.setHorizontallyScrolling(false);

        mActivityRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > mKeyHeight)) {
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > mKeyHeight)) {
                    mLLCommit.setVisibility(View.VISIBLE);
                    mCommitComment.setText("");
                }
            }
        });
        UMengShareManager.getInstance(mContext).setOnShareCompleteListener(new UMengShareManager.OnShareCompleteLisnener() {
            @Override
            public void onShareComplete() {
                new HttpUtils().send(
                        HttpRequest.HttpMethod.GET,
                        NetConstant.HOST,
                        NetConstant.getScoreParams(mContext, NetWorkRequestConstants.GET_SCORE_METHOD_SHARE, UMengShareManager.TYPE_SHARE_INFO, mInfoId), null);
            }
        });


        mShareView = findViewById(R.id.tv_commit);
        //收藏与取消收藏
        mCollectInfoImg = (ImageView) findViewById(R.id.img_collection_info);
        //做收藏与取消收藏动画的View
        mAttentionAnimatorView = findViewById(R.id.img_animator_attention);
        setOnClickListener(mShareView, mCollectInfoImg, mEditCommentView, mIvCommit);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAttentionAnimatorView.setVisibility(View.GONE);
    }

    @Override
    protected void attachData() {
        mRecycleView.setAdapter(mAdapter);

    }

    @Override
    protected void refreshData() {
        // 请求咨询详情数据,评论数据可能随时刷新
        new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getInfoDetailParams(mContext, mInfoId), mListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_commit:
                showShareBoard();
                HashMap<String, String> umengMap = new HashMap<>();
                umengMap.put(UmengConstants.KEY_SHARE_CONTENT_ID, mInfoId);
                UmengUtils.onEvent(this, EventEnum.ShareInfoEntry, umengMap);
                break;
            case R.id.img_collection_info:
                collectInfo();
                HashMap<String, String> collectMap = new HashMap<>();
                collectMap.put(UmengConstants.KEY_COLLECT_INFO_ID, mInfoId);
                UmengUtils.onEvent(this, EventEnum.CollectInfoEntry, collectMap);
                break;
            case R.id.et_comment:
                mRLComment.setVisibility(View.VISIBLE);
                mCommitComment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mLLCommit.setVisibility(View.GONE);
                break;
            case R.id.iv_commit_comment:
                commit();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    private void showShareBoard() {
        //弹出分享面板
        if (!UserInfoUtils.checkUserLogin(this)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            return;
        }
        mShareBoard = new CustomShareBoard(this);
        mShareBoard.setId(mInfoId);
        mShareBoard.setShareType(UmengConstants.SHARE_TYPE_INFO);

        if (TextUtils.isEmpty(mImageFileAbsPath)) {
            mImageFileAbsPath = BitmapUtils.saveBitmap(this, BitmapUtils.convertViewToBitmap(mRecycleView), "info_share.png");
        }
        String title = mHeaderBackView.getText().toString().trim();
        mShareBoard.setShareContent(
                StringUtils.getString(
                        getString(R.string.info_share_title_prefix),
                        title),
                mImageFileAbsPath,StringUtils.getString(UmengConstants.BASE_SHARE_INFO_URL, mInfoId));
        mShareBoard.applayBlur();
        mShareBoard.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void collectInfo() {
        if (!UserInfoUtils.checkUserLogin(this)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            return;
        }
        HttpUtils.sHttpCache.setEnabled(HttpRequest.HttpMethod.GET, false);
        new HttpUtils().send(HttpRequest.HttpMethod.GET,
                NetConstant.HOST,
                NetConstant.getInfoCollectParams(this, mInfoId), new InfoCollectListener());
    }

    @Override
    public void finish() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningTasks(1).get(0).numActivities < 2) {
            jump(mContext, MainActivity.class);
        }
        super.finish();
    }

    /**
     * 请求专家列表数据
     */
    private class InfoDetailListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<InfoDetailData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<InfoDetailData> task = new StringToBeanTask<>(InfoDetailData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            e.printStackTrace();
        }

        @Override
        public void onConvertSuccess(InfoDetailData response) {
            mData = response;
            mAdapter.setData(response);
            mHeaderBackView.setText(response.getData().getName());
            String collectionState = response.getData().getCollection();
            boolean isCollected = "1".equals(collectionState);
            mAttentionAnimatorView.setVisibility(isCollected ? View.VISIBLE : View.GONE);
            mCollectInfoImg.setImageResource(isCollected ? R.drawable.attention : R.drawable.attention_cancle);
            if (!TextUtils.isEmpty(isClickCommit) && InfoActivity.IS_CLICK_COMMENT.equals(isClickCommit)) {
                mRecycleView.scrollToPosition(3 + mData.getData().getRelated().size());
            }
        }

        @Override
        public void onConvertFailed() {
        }
    }

    private void commit() {
        //判断用户是否登录
        if (!UserInfoUtils.checkUserLogin(mContext)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            intent.putExtra(IntentConstant.TARGET_ACTIVITY, InfoDetailActivity.class.getSimpleName());
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            jump(intent);
        } else {
            // 获取评价内容
            String content = mCommitComment.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                //评论实时刷新，需要关闭缓存功能
                HttpUtils.sHttpCache.setEnabled(HttpRequest.HttpMethod.GET, false);
                // 提交评价
                new HttpUtils().send(HttpRequest.HttpMethod.POST, NetConstant.HOST,
                        NetConstant.getCommentParams(mContext, mInfoId, "1", content), new CommentListener());
                HashMap<String, String> map = new HashMap<>();
                map.put(UmengConstants.KEY_ORDER_DETAIL_COMMENT_COMMIT, content);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.comment_prompt);
            }
        }
        // 收起键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditCommentView.getWindowToken(), 0);
        mCommitComment.setText(StringUtils.getString(""));
        mRLComment.setVisibility(View.GONE);
        mLLCommit.setVisibility(View.VISIBLE);
    }

    private class CommentListener extends RequestCallBack<String> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            refreshData();
            mRecycleView.scrollToPosition(3 + mData.getData().getRelated().size() + 3);
            DialogUtils.showShortPromptToast(mContext, getString(R.string.comment_success));
            //评论获取积分
            new HttpUtils().send(
                    HttpRequest.HttpMethod.GET,
                    NetConstant.HOST,
                    NetConstant.getScoreParams(mContext,
                            NetWorkRequestConstants.GET_SCORE_METHOD_COMMENT,
                            UMengShareManager.TYPE_SHARE_INFO, mInfoId), null);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, getString(R.string.comment_fail));
            e.printStackTrace();
        }
    }

    /**
     * 资讯收藏
     */
    private class InfoCollectListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<AttentionCollectionResponse> {

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
            mCollectState = response.getData().isCollect();
            mAttentionAnimatorView.setVisibility(View.VISIBLE);
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
            mCollectInfoImg.setImageResource(mCollectState ? R.drawable.attention : R.drawable.attention_cancle);
        }
    };

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
