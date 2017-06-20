package com.bobaoo.xiaobao.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.ActiveData;
import com.bobaoo.xiaobao.domain.UserNoChargeResponse;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.UpdateAlertDialog;
import com.bobaoo.xiaobao.ui.fragment.BaseFragment;
import com.bobaoo.xiaobao.ui.fragment.ExpertFragment;
import com.bobaoo.xiaobao.ui.fragment.FindFragment;
import com.bobaoo.xiaobao.ui.fragment.HomeFragment;
import com.bobaoo.xiaobao.ui.fragment.IdentifyFragment;
import com.bobaoo.xiaobao.ui.fragment.UserFragment;
import com.bobaoo.xiaobao.ui.popupwindow.IdentifyWindow;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.MPermissionUtil;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * Created by star on 15/5/29.
 */
public class MainActivity extends BaseActivity {
    // 当前fragment的id
    private int currentFragmentId;
    // 当前fragment
    private BaseFragment currentFragment;
    // 是否需要改变fragment
    private boolean isNeedChangeFragment;
    // footer
    private TextView mHomeView;
    private TextView mExpertView;
    private ImageView mIdentifyView;
    private TextView mInfoView;
    private RelativeLayout mUserView;
    private TextView mUnChargeCountBubbleTv;
    // header
    private View mHeaderView;
    private TextView mHeaderBackView;
    private TextView mHeaderTitleView;
    //上一次back键的时间
    private long mLastKeyTime;

    private boolean mIsFromNotification;
    private int mIdentifyPageIndex;

    @Override
    protected void getIntentData() {
        currentFragmentId = getIntent().getIntExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.iv_identify);
        mIsFromNotification = getIntent().getBooleanExtra(IntentConstant.IS_FROM_NOTIFICATION, false);
        if(mIsFromNotification){
            mIdentifyPageIndex = getIntent().getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
            Intent intent = new Intent(this, UserIdentifyActivity.class);
            intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, mIdentifyPageIndex);
            jump(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int exitCode = intent.getIntExtra(IntentConstant.APP_EXIT_KEY, 0);
        if (exitCode == IntentConstant.APP_EXIT_CODE) {
            finish();
        }

        mIsFromNotification = intent.getBooleanExtra(IntentConstant.IS_FROM_NOTIFICATION, false);
        if(mIsFromNotification){
            mIdentifyPageIndex = intent.getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
            Intent jumpIntent = new Intent(mContext, UserIdentifyActivity.class);
            jumpIntent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, mIdentifyPageIndex);
            jump(jumpIntent);
        }

        currentFragmentId = getIntent().getIntExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.iv_identify);
        if(currentFragmentId != 0) {
            changeFragment(currentFragmentId);
        }
    }

    @Override
    protected void initData() {
        isNeedChangeFragment = true;
        setSwipeBackEnable(false);
        MPermissionUtil.requestPermissionCamera(this);

        update(24 * 60 * 60 * 1000);

        startActive();
    }
    private void startActive() {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getActiveParams(mContext), new ActiveListener());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == AppConstant.PERMISSION_REQ_CODE_READ_CAMERA){
            MPermissionUtil.requestPermissionStorage(this);
        }
    }

    private class ActiveListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<ActiveData>{

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<ActiveData> task = new StringToBeanTask<>(ActiveData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }

        @Override
        public void onConvertSuccess(ActiveData response) {
            if(response != null&& "1".equals(response.data.display)){
                Intent intent = new Intent(MainActivity.this,ActiveActivity.class);
                intent.putExtra(IntentConstant.ACTIVE_INFO,response);
                startActivity(intent);
            }
        }

        @Override
        public void onConvertFailed() {

        }
    }
    /**
     * @param internal
     * 控制自动更新请求的频率，单位毫秒,eg：update(context,24*60*60*1000) ，每天更新一次
     */
    public void update(final long internal) {
        SharedPreferences preference = getSharedPreferences(AppConstant.UPDATE_TIME, Activity.MODE_PRIVATE);
        long lastUpdateTime = preference.getLong(AppConstant.KEY_LAST_UPDATE_TIME, 0);
        long now = System.currentTimeMillis();
        if ((now - lastUpdateTime) > internal) {
            checkVersion();
            preference.edit().putLong(AppConstant.KEY_LAST_UPDATE_TIME, now).commit();
        }
    }

    /** 版本检测 */
    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus,
                                         UpdateResponse updateInfo) {
                if (updateStatus == 0 && updateInfo != null) {//((int)(updateInfo.target_size/2048))
                    int target_size = String2Double2M(updateInfo.target_size);
                    String newVersion = String.format("%s\n%s", new Object[]{"最新版本: " + updateInfo.version, "新版本大小: " + target_size+"M"});
                    String content = String.format("%s\n%s", new Object[]{"更新内容", updateInfo.updateLog});
                    UmengUtils.onEvent(MainActivity.this,EventEnum.UpdateAlertDialogCreate);
                    showUpdateDialog(updateInfo.path, newVersion, content,updateInfo);
                }
                // case 0: // has update
                // case 1: // has no update
                // case 2: // none wifi
                // case 3: // time out
            }
        });

        UmengUpdateAgent.update(this);
    }

    private int String2Double2M(String target_size) {
        return (int)Double.parseDouble(target_size)/(1024*1024);
    }

    private void showUpdateDialog(final String downloadUrl, final String newVersion,final String content,UpdateResponse updateInfo) {
            UpdateAlertDialog updateAlertDialog = new UpdateAlertDialog(this,updateInfo);
            updateAlertDialog.setMessage(newVersion, content);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {
        mHeaderView = findViewById(R.id.layout_title);
        // 返回按钮
        mHeaderBackView = (TextView) findViewById(R.id.tv_back);
        mHeaderBackView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        // title
        mHeaderTitleView = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    protected void initContent() {
    }

    @Override
    protected void initFooter() {
        mHomeView = (TextView) findViewById(R.id.tv_home);
        mExpertView = (TextView) findViewById(R.id.tv_expert);
        mIdentifyView = (ImageView) findViewById(R.id.iv_identify);
        mInfoView = (TextView) findViewById(R.id.tv_info);
        mUserView = (RelativeLayout) findViewById(R.id.tv_user);
        mUnChargeCountBubbleTv = (TextView) findViewById(R.id.tv_bubble_payment);
        mIdentifyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIdentifyView.setImageResource(R.drawable.icon_camera_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        mIdentifyView.setImageResource(R.drawable.icon_camera_normal);
                        break;
                }
                return false;
            }
        });

        setOnClickListener(mHomeView, mExpertView, mIdentifyView, mInfoView, mUserView);
    }

    @Override
    protected void attachData() {
        changeFragment(currentFragmentId);
    }

    @Override
    protected void refreshData() {
        if (UserInfoUtils.checkUserLogin(mContext)) {
            new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getUnPayCountParams(mContext), new UserUnChargeCountListener());
        }else {
            mUnChargeCountBubbleTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_identify:
            case R.id.tv_home:
            case R.id.tv_expert:
            case R.id.tv_info:
            case R.id.tv_user:
                changeFragment(view.getId());
                break;
            default:
                super.onClick(view);
                break;
        }
    }
    private IdentifyWindow mIdentifyWindow;
    private void showIdentifyWindow(View view) {
        if (null == mIdentifyWindow) {
            mIdentifyWindow = new IdentifyWindow(this);
            mIdentifyWindow.init();
        }
        mIdentifyWindow.showMoreWindow(view,100);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mLastKeyTime > 2000) {
                mLastKeyTime = System.currentTimeMillis();
                DialogUtils.showShortPromptToast(mContext, R.string.press_back_again);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeFragment(int id) {
        switch (id) {
            case R.id.tv_home:
                if (!(currentFragment instanceof HomeFragment)) {
                    currentFragment = new HomeFragment();
                    isNeedChangeFragment = true;
                    changeNavigationIconState(true, false, false, false, false);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_Home);
                }
                break;
            case R.id.tv_expert:
                if (!(currentFragment instanceof ExpertFragment)) {
                    currentFragment = new ExpertFragment();
                    isNeedChangeFragment = true;
                    changeNavigationIconState(false, true, false, false, false);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_Expert);
                }
                break;
            case R.id.iv_identify:
                if (currentFragment == null || !(currentFragment instanceof IdentifyFragment)) {
                    currentFragment = new IdentifyFragment();
                    isNeedChangeFragment = true;
                    changeNavigationIconState(false, false, true, false, false);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_Identify);
                }
                break;
            case R.id.tv_info:
//                if (!(currentFragment instanceof InfoActivity)) {
//                    currentFragment = new InfoActivity();
//                    isNeedChangeFragment = true;
//                    changeNavigationIconState(false, false, false, true, false);
//                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_Info);
//                }
                if (!(currentFragment instanceof FindFragment)){
                    currentFragment = new FindFragment();
                    isNeedChangeFragment = true;
                    changeNavigationIconState(false, false, false, true, false);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_Info);
                }
                break;

            case R.id.tv_user:
                if (!(currentFragment instanceof UserFragment)) {
                    currentFragment = new UserFragment();
                    isNeedChangeFragment = true;
                    changeNavigationIconState(false, false, false, false, true);
                    UmengUtils.onEvent(this, EventEnum.MainPageTabClick_User);
                }
                break;
            default:
                break;
        }
        if (isNeedChangeFragment) {
            // FragmentManager
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_container, currentFragment);
            fragmentTransaction.commit();
        }
        isNeedChangeFragment = false;
    }

    private class UserUnChargeCountListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<UserNoChargeResponse>{

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserNoChargeResponse> task = new StringToBeanTask<>(UserNoChargeResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onConvertSuccess(UserNoChargeResponse response) {
              if(response != null && !response.isError() && response.getData() > 0){
                    mUnChargeCountBubbleTv.setVisibility(View.VISIBLE);
                    int count = response.getData();
                    if(count > 99) {
                        count = 99;
                    }
                    mUnChargeCountBubbleTv.setText(String.valueOf(count));
                }else {
                    mUnChargeCountBubbleTv.setVisibility(View.GONE);
                }
        }

        @Override
        public void onConvertFailed() {
            mUnChargeCountBubbleTv.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            mUnChargeCountBubbleTv.setVisibility(View.GONE);
        }
    }

    private void changeNavigationIconState(boolean home, boolean expert, boolean camera, boolean info, boolean user) {
        mHomeView.setSelected(home);
        mExpertView.setSelected(expert);
        mInfoView.setSelected(info);
        mUserView.setSelected(user);
        if (home) {
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderTitleView.setText(R.string.home);
        }
        if (expert) {
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderTitleView.setText(R.string.expert);
        }
        if (camera) {
            mHeaderView.setVisibility(View.GONE);
        }
        if (info) {
            mHeaderView.setVisibility(View.VISIBLE);
            mHeaderTitleView.setText(R.string.find);
        }
        if (user) {
            mHeaderView.setVisibility(View.GONE);
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