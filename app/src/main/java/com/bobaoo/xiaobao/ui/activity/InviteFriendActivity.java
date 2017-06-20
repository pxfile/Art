package com.bobaoo.xiaobao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.JsObj;
import com.bobaoo.xiaobao.manager.UMengShareManager;
import com.bobaoo.xiaobao.utils.BitmapUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by wangfens on 2015/9/29.
 */
public class InviteFriendActivity extends BaseActivity {

    private String mImageFileAbsPath;
    private WebView mShareInviteView;
    private View mSharePanel;
    private Handler mHandler = new Handler();

    @Override
    protected void getIntentData() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.invite_friend);
        backView.setTextColor(getResources().getColor(R.color.white));
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        backView.setCompoundDrawables(drawable, null, null, null);
        setOnClickListener(backView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.ll_share_moments:
                doShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                UmengUtils.onEvent(mContext, EventEnum.User_InviteFriend_WEIXIN_CIRCLE);
                break;
            case R.id.ll_share_qq:
                doShare(SHARE_MEDIA.QQ);
                UmengUtils.onEvent(mContext, EventEnum.User_InviteFriend_QQ);
                break;
            case R.id.ll_share_wechat:
                doShare(SHARE_MEDIA.WEIXIN);
                UmengUtils.onEvent(mContext, EventEnum.User_InviteFriend_WEIXIN);
                break;
            case R.id.ll_share_weibo:
                doShare(SHARE_MEDIA.SINA);
                UmengUtils.onEvent(mContext, EventEnum.User_InviteFriend_SINAWEIBO);
                break;
            default:
                break;
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initContent() {
        LinearLayout mShareWeChat = (LinearLayout) findViewById(R.id.ll_share_wechat);
        LinearLayout mShareQQ = (LinearLayout) findViewById(R.id.ll_share_qq);
        LinearLayout mShareMoments = (LinearLayout) findViewById(R.id.ll_share_moments);
        LinearLayout mShareMicroBlog = (LinearLayout) findViewById(R.id.ll_share_weibo);
        mShareInviteView = (WebView) findViewById(R.id.wv_invite_friend);

        WebSettings webSettings = mShareInviteView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url = StringUtils.getString("http://jianbao.artxun.com/index.php?module=jbapp&act=jb&api=h5&op=invite&user_id=", UserInfoUtils.getUserId(mContext));
        mShareInviteView.loadUrl(url);

        mSharePanel = findViewById(R.id.ll_share_panel);
        //js调用java代码
        //<a href="javascript:;" onclick="share()">立即邀请&gt;</a>
        mShareInviteView.addJavascriptInterface(new JsObj(mContext) {
            @JavascriptInterface
            public void share() {
                //保证UI操作放在主线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mSharePanel.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void share(String s, String title) {

            }
        }, "browser");
        mShareInviteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mSharePanel.getVisibility() == View.VISIBLE) {
                    mSharePanel.setVisibility(View.GONE);
                }
                return false;
            }
        });
        setOnClickListener(mShareMicroBlog, mShareMoments, mShareQQ, mShareWeChat);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSharePanel.getVisibility() == View.VISIBLE) {
                mSharePanel.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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

    /**
     * 分享功能
     *
     * @param sharePlatform 平台
     */
    private void doShare(SHARE_MEDIA sharePlatform) {
        if (!UserInfoUtils.checkUserLogin(this)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            return;
        }
        if (TextUtils.isEmpty(mImageFileAbsPath)) {
            mImageFileAbsPath = BitmapUtils.saveBitmap(this, BitmapUtils.convertViewToBitmap(mShareInviteView), "invite_friend_share.png");
            UMengShareManager.getInstance(mContext).setShareContent(StringUtils.getString("注册手机鉴宝输入我的邀请码", UserInfoUtils.getUserId(mContext), "助我领50积分！你也有份哦~"),
                    mImageFileAbsPath,UmengConstants.DOWNLOAD_H5_APK_URL);
            UMengShareManager.getInstance(mContext).setOnShareCompleteListener(new UMengShareManager.OnShareCompleteLisnener() {
                @Override
                public void onShareComplete() {
                    //完成分享后的动作
                }
            });
        }
        UMengShareManager.getInstance(mContext).doShare(sharePlatform);
        mSharePanel.setVisibility(View.GONE);
    }
}
