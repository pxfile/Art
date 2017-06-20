package com.bobaoo.xiaobao.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.domain.UpdateInfo;
import com.bobaoo.xiaobao.manager.WXDealManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.ui.dialog.UpdateAlertDialog;
import com.bobaoo.xiaobao.utils.AppUtils;
import com.bobaoo.xiaobao.utils.CacheCleanUtil;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * Created by Administrator on 2015/8/21.
 */
public class UserSetActivity extends BaseActivity {

    private TextView mUserUpdate;
    private TextView mUserClear;
    private Button mUserQuit;
    private RelativeLayout mContactRl;
    private RelativeLayout mUpdateRl;
    private RelativeLayout mClearRl;

    private String localAppVersionCode;
    private ProgressDialog mProgressDialog;
    private DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            UmengUtils.onEvent(mContext, EventEnum.User_Setting_Quit_Success);
            UserInfoUtils.logOut(mContext);
            WXDealManager.getInstance().clearWXDealInfo();
            Intent exitIntent = new Intent(mContext, MainActivity.class);
            startActivity(exitIntent);
        }
    };

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.set));
        setOnClickListener(backView);
    }


    @Override
    protected void initContent() {

        mClearRl = (RelativeLayout) findViewById(R.id.rl_user_clear);
        mContactRl = (RelativeLayout) findViewById(R.id.rl_contact_us);
        mUpdateRl = (RelativeLayout) findViewById(R.id.rl_user_update);
        mUserClear = (TextView) findViewById(R.id.tv_user_clear_cache);
        mUserUpdate = (TextView) findViewById(R.id.tv_user_update);
        mUserQuit = (Button) findViewById(R.id.btn_user_quit_account);

        try {
            String size = CacheCleanUtil.getTotalCacheSize(mContext);
            mUserClear.setText(size);
        } catch (Exception e) {
        }

        localAppVersionCode = AppUtils.getAppVersionName(mContext);
        mUserUpdate.setText(StringUtils.getString(getString(R.string.current_version), localAppVersionCode));
        setOnClickListener(mClearRl, mContactRl, mUpdateRl, mUserQuit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_user_clear:
                UmengUtils.onEvent(mContext, EventEnum.User_Setting_Clear);
                DialogUtils.showConfirmDialog(mContext, R.string.submit_clear_cache, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String size = CacheCleanUtil.getTotalCacheSize(mContext);
                            if ("0K".equals(size)) {
                                DialogUtils.showShortPromptToast(mContext, StringUtils.getString(getString(R.string.cache_no_need_clean)));
                                return;
                            }
                            CacheCleanUtil.clearAllCache(mContext);
                            mUserClear.setText(CacheCleanUtil.getTotalCacheSize(mContext));
                            DialogUtils.showShortPromptToast(mContext, StringUtils.getString(getString(R.string.cache_clean_result)));
                            UmengUtils.onEvent(mContext, EventEnum.User_Setting_Clear_Success);
                        } catch (Exception e) {
                            DialogUtils.showShortPromptToast(mContext, StringUtils.getString());
                        }
                    }
                });

                break;
            case R.id.rl_user_update:
                UmengUtils.onEvent(mContext, EventEnum.User_Setting_Update);

                if (mProgressDialog == null) {
                    mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.update_loading_dialog_title));
                }
                checkVersion();
                break;
            case R.id.btn_user_quit_account:
                UmengUtils.onEvent(mContext, EventEnum.User_Setting_Quit_Account);
                DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, mListener);
                break;
            case R.id.rl_contact_us://联系我们
                jump(mContext, ContactUsActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.User_Contact_Us);
                break;
            default:
                break;
        }
    }

    /**
     * 版本检测
     */
    private void checkVersion() {
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus,
                                         UpdateResponse updateInfo) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
                if (updateStatus == 0 && updateInfo != null) {
                    int target_size = String2Double2M(updateInfo.target_size);
                    String newVersion = String.format("%s\n%s", new Object[]{"最新版本: " + updateInfo.version, "新版本大小: " + target_size+"M"});
                    String content = String.format("%s\n%s", new Object[]{"更新内容", updateInfo.updateLog});
                    showUpdateDialog(updateInfo.path, newVersion, content,updateInfo);
                    UmengUtils.onEvent(UserSetActivity.this,EventEnum.MyUpdateAlertDialogCreate);
                }else if(updateStatus == 1){
                    DialogUtils.showShortPromptToast(mContext, R.string.update_latest);
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
        updateAlertDialog.setMessage(newVersion,content);
    }


    private class UpdateConfirmListener implements DialogInterface.OnClickListener {
        private String mUrl;

        public UpdateConfirmListener(String apkUrl) {
            mUrl = apkUrl;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Uri uri = Uri.parse(mUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private class UpdateInfoListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UpdateInfo> {

        @Override
        public void onConvertSuccess(UpdateInfo response) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            final UpdateInfo.DataEntity data = response.getData();
            if (data != null && !TextUtils.isEmpty(data.getApkurl())) {
                int localVersionCode = AppUtils.getVersionCode(mContext);
                int lastestVersionCode = data.getVerCode();
                if (localVersionCode < lastestVersionCode) {
                    UmengUtils.onEvent(mContext,EventEnum.User_Setting_Update_HaveUpdate);
                    DialogUtils.showConfirmDialog(
                            mContext,
                            getString(R.string.update_dialog_title),
                            response.getData().getNote(),
                            new UpdateConfirmListener(data.getApkurl()));
                } else {
                    DialogUtils.showShortPromptToast(mContext, R.string.update_latest);
                    String lastestAppVersionCode = AppUtils.getAppVersionName(mContext);
                    mUserUpdate.setText(StringUtils.getString(getString(R.string.current_version),lastestAppVersionCode));
                    UmengUtils.onEvent(mContext,EventEnum.User_Setting_Update_NoUpdate);
                }
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.update_info_failed);
            }
        }

        @Override
        public void onConvertFailed() {

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            DialogUtils.showShortPromptToast(mContext, R.string.update_info_failed);
        }

        @Override
        public void onFailure(HttpException e, String s) {

            DialogUtils.showShortPromptToast(mContext, R.string.update_info_failed);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {

            StringToBeanTask<UpdateInfo> task = new StringToBeanTask<>(UpdateInfo.class, this);
            task.execute(responseInfo.result);
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

    }
}
