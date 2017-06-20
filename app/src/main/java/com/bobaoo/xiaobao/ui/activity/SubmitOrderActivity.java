package com.bobaoo.xiaobao.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.application.IdentifyApplication;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.AuthCodeResponse;
import com.bobaoo.xiaobao.domain.UpLoadIdentifyData;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.SubmitOrderPictureAdapter;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.ActivityUtils;
import com.bobaoo.xiaobao.utils.BitmapUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.NetUtils;
import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by star on 15/6/2.
 * 提交订单
 */
public class SubmitOrderActivity extends BaseActivity {

    // 鉴定类型
    private int mIdentifyType;
    // 鉴定方式
    private int mIdentifyMethod;
    // 专家id 在线专家和机构专家
    private String mExpertId;
    private boolean mIsIOnlineExpertId;
    private String mIdentifyMethodSwitchInfos;
    private String mIdentifyMethodPrices;

    // 确定使用图片的路径
    private ArrayList<String> mUsingFilePathList;
    private ArrayList<String> mUploadFilePathList = new ArrayList<>();
    // adapter
    private SubmitOrderPictureAdapter mPicturePreviewAdapter;
    // dialog
    private ProgressDialog mProgressDialog;
    private ProgressDialog mCheckPhoneDialog;

    // Views
    private View mPictureContainerView;
    private RecyclerView mPicturePreviewView;
    private View mChooseType;
    private TextView mIdentifyTypeView;
    private TextView mIdentifyPriceView;
    private EditText mContentView;
    private ImageView mStepMethodView;
    private ImageView mStepDescView;
    private ImageView mStepExpertView;
    private ImageView mArrowsView;

    private long mTotalPictureSize = 0;
    private LinearLayout mWarmPrompt;
    private TextView mExpertName;
    private View mExpertView;

    private final static int CLIP_BP_WIDTH = 1000;
    private final static int CLIP_BP_HEIGHT = 750;

    private final String TAKE_PICTURE_COMPRESSED_DIR = StringUtils.getString(Environment.getExternalStorageDirectory(),
            "/", "art_compressedimgs", "/");

    private DialogInterface.OnClickListener mConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(mContext, MainActivity.class);
            jump(intent);
            finish();
        }
    };

    private DialogInterface.OnClickListener mWarnListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener mCommitListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
//            commit();
            startUploadTask();
        }
    };

    private void startUploadTask() {
        String phone = UserInfoUtils.getPhone(mContext);
        if (StringUtils.checkPhoneNumber(phone)) {
            new Thread(new CompressImagesAndUploadRunnable()).start();
            if (mProgressDialog == null) {
                mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.submit_loading));
                mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                        switch (keyCode) {
                            case KeyEvent.KEYCODE_BACK:
                                return true;
                        }
                        return false;
                    }
                });
            }
            UmengUtils.onEvent(mContext, EventEnum.SubmitOrder);
        } else {
            DialogUtils.showLongPromptToast(mContext, R.string.tip_wrong_number);
            gotoCheckPhoneNumber();
        }
    }

    //图片列表的普通ITEM的点击事件, 另外一种类型则统一为添加图片
    private View.OnClickListener mOnNormalItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, PhotoViewActivity.class);
            intent.putStringArrayListExtra(IntentConstant.UsingPictureFilePaths, mUsingFilePathList);
            intent.putExtra(IntentConstant.UsingPictureIndex, (int) (view.getTag()));
            ActivityUtils.jump(mContext, intent);
        }
    };

    @Override
    protected void getIntentData() {
//        mIdentifyType = getIntent().getIntExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeChina);
        mExpertId = getIntent().getStringExtra(IntentConstant.EXPERT_ID);
//        mIsIOnlineExpertId = getIntent().getBooleanExtra(IntentConstant.IS_ONLINE_EXPERT, false);

        mIdentifyMethodSwitchInfos = getIntent().getStringExtra(IntentConstant.IdentifyMethodInfo);
        mIdentifyMethodPrices = getIntent().getStringExtra(IntentConstant.IdentifyMethodPrices);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (IntentConstant.SubmitOrder.equals(intent.getStringExtra(IntentConstant.IntentAction))) {
            commit();
        }
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(mExpertId)) {
            mExpertId = "";
            // 默认极速鉴定
            mIdentifyMethod = 1;
        } else {
            // 预约鉴定
            mIdentifyMethod = 3;
        }
        //在线专家鉴定为普通鉴定
        if (mIsIOnlineExpertId) {
            mIdentifyMethod = 0;
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_submit_order;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.submit_order);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mArrowsView = (ImageView) findViewById(R.id.img_arrows);
        mWarmPrompt = (LinearLayout) findViewById(R.id.ll_arm_prompt);
        mWarmPrompt.setVisibility(mIdentifyMethod == 3 ? View.VISIBLE : View.GONE);
        mExpertView = findViewById(R.id.ll_expert);
        mExpertName = (TextView) findViewById(R.id.expert_name_tv);
        mExpertName.setText(getIntent().getStringExtra(IntentConstant.EXPERT_NAME));
        mExpertView.setVisibility(mIdentifyMethod == 3 ? View.VISIBLE : View.GONE);
        // 图片预览
        mPictureContainerView = findViewById(R.id.ll_picture);
        mPicturePreviewView = (RecyclerView) findViewById(R.id.rv);
        // 设置横向滚动
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPicturePreviewView.setLayoutManager(manager);
        // 选择鉴定方式
        mStepMethodView = (ImageView) findViewById(R.id.iv_step_type);
        mChooseType = findViewById(R.id.ll_type);
        mIdentifyTypeView = (TextView) findViewById(R.id.tv_identify_type);
        mIdentifyPriceView = (TextView) findViewById(R.id.tv_price);
        if (TextUtils.isEmpty(mExpertId) || mIsIOnlineExpertId) {
            setOnClickListener(mChooseType);
        }
        // 要求
        mStepDescView = (ImageView) findViewById(R.id.iv_step_attach);
        mContentView = (EditText) findViewById(R.id.et_content);
        //专家
        mStepExpertView = (ImageView) findViewById(R.id.iv_step_expert);
        // 提交
        View submitView = findViewById(R.id.tv_submit);
        setOnClickListener(submitView);

        //注册广播，监听用户网络状态的改变
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, mFilter);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        //获取在线专家的数据,单独处理挂号鉴定
        //是否是挂号鉴定
        if (IdentifyApplication.getIntentData(IntentConstant.IS_ONLINE_EXPERT) != null) {
            mIsIOnlineExpertId = (boolean) IdentifyApplication.getIntentData(IntentConstant.IS_ONLINE_EXPERT);
        } else {
            mIsIOnlineExpertId = false;
        }

        mArrowsView.setVisibility(mIsIOnlineExpertId ? View.GONE : View.VISIBLE);

        if (mIsIOnlineExpertId) {
            mExpertId = (String) IdentifyApplication.getIntentData(IntentConstant.EXPERT_ID);
            mIdentifyMethodSwitchInfos = (String) IdentifyApplication.getIntentData(IntentConstant.IdentifyMethodInfo);
            mIdentifyMethodPrices = (String) IdentifyApplication.getIntentData(IntentConstant.IdentifyMethodPrices);
        }
        if (TextUtils.isEmpty(mExpertId) || mIsIOnlineExpertId) {
            //获取鉴定类型
            mIdentifyType = (int) IdentifyApplication.getIntentData(IntentConstant.IdentifyType);
            // 获取图片信息
            mUsingFilePathList = (ArrayList<String>) IdentifyApplication.getIntentData(IntentConstant.UsingPictureFilePaths);
            // 设置adapter
            mPicturePreviewAdapter = new SubmitOrderPictureAdapter(mContext, mUsingFilePathList, mIdentifyType, this);
            mPicturePreviewAdapter.setOnNormalItemClickListener(mOnNormalItemClickListener);
            // 设置view
            mPicturePreviewView.setAdapter(mPicturePreviewAdapter);
            mStepDescView.setImageResource(mIdentifyMethod == 3 ? R.drawable.icon_step_4 : R.drawable.icon_step_3);
        } else {
            //预约鉴定
            mPictureContainerView.setVisibility(View.GONE);
            mStepMethodView.setImageResource(R.drawable.icon_step_1);
            mStepExpertView.setImageResource(R.drawable.icon_step_2);
            mStepDescView.setImageResource(mIdentifyMethod == 3 ? R.drawable.icon_step_3 : R.drawable.icon_step_2);
        }
    }

    @Override
    protected void refreshData() {
        if (TextUtils.isEmpty(mExpertId) || mIsIOnlineExpertId) {
            mPicturePreviewAdapter.notifyDataSetChanged();
        }
        updateIdentifyType();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IdentifyApplication.removeIntentData(IntentConstant.UsingPictureFilePaths);
        IdentifyApplication.removeIntentData(IntentConstant.IdentifyType);
        IdentifyApplication.removeIntentData(IntentConstant.IS_ONLINE_EXPERT);
        IdentifyApplication.removeIntentData(IntentConstant.EXPERT_ID);
        IdentifyApplication.removeIntentData(IntentConstant.IdentifyMethodInfo);
        IdentifyApplication.removeIntentData(IntentConstant.IdentifyMethodPrices);
        unregisterReceiver(mNetworkReceiver);
        clearTempImageFiles();//删除压缩过的图片暂存文件
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_delete://修改或者删除订单
                int index = (int) view.getTag();
                if (index < AppConstant.DefaultTakePictureNum) {
                    intent.putExtra(IntentConstant.CurrentPictureNum, (int) view.getTag());
                    intent.putExtra(IntentConstant.TotalPictureNum, mUsingFilePathList.size());
                    intent.putExtra(IntentConstant.IdentifyType, mIdentifyType);
                    //如果是挂号鉴定，还需要传专家ID，价格和鉴定方式列表
                    if (mIsIOnlineExpertId) {
                        intent.putExtra(IntentConstant.IS_ONLINE_EXPERT, mIsIOnlineExpertId);
                        intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
                        intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
                        intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
                    }
                    intent.setClass(mContext, TakePictureActivity.class);
                    jump(intent);
                } else {
                    mUsingFilePathList.remove(index);
                    mPicturePreviewAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_back:
                DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.order_no_save, mConfirmListener);
                break;
            case R.id.ll_container://添加图片
                intent.putExtra(IntentConstant.CurrentPictureNum, mUsingFilePathList.size());
                intent.putExtra(IntentConstant.TotalPictureNum, mUsingFilePathList.size() + 1);
                intent.putExtra(IntentConstant.IdentifyType, mIdentifyType);
                //如果是挂号鉴定，还需要传专家ID，价格和鉴定方式列表
                if (mIsIOnlineExpertId) {
                    intent.putExtra(IntentConstant.IS_ONLINE_EXPERT, mIsIOnlineExpertId);
                    intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
                    intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
                    intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
                }
                intent.setClass(mContext, TakePictureActivity.class);
                jump(intent);
                break;
            case R.id.tv_submit:
                commit();
                break;
            case R.id.ll_type:
                //挂号鉴定不对鉴定方式做修改
                if (!mIsIOnlineExpertId) {
                    intent.setClass(mContext, IdentifyTypeActivity.class);
                    intent.putExtra(IntentConstant.IdentifyId, mIdentifyMethod);
                    intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
                    intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
                    ActivityUtils.jump(mActivity, intent, IntentConstant.RequestCodeIdentifyType);
                }
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    private void checkPhoneState() {
        if (mCheckPhoneDialog == null) {
            mCheckPhoneDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.get_checked_phone_state));
        }
        new HttpUtils().configCurrentHttpCacheExpiry(0)
                .send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getIsPhoneNumberCheckedParams(mContext),
                        new CheckPhoneStateListener());
    }

    /**
     * 获取校验手机信息
     */
    private class CheckPhoneStateListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<AuthCodeResponse> {

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<AuthCodeResponse> task = new StringToBeanTask<>(AuthCodeResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            gotoCheckPhoneNumber();
            DialogUtils.showShortPromptToast(mContext, R.string.get_phone_check_state_failed);
            if (mCheckPhoneDialog != null) {
                mCheckPhoneDialog.dismiss();
            }
        }

        @Override
        public void onConvertSuccess(AuthCodeResponse response) {
            if (response.isError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.please_check_phone_number);
                gotoCheckPhoneNumber();
            } else {
                //保存已校验标记和手机号码
                String phone = response.getMessage();
                UserInfoUtils.setPhone(mContext, phone);
                SharedPreferencesUtils.setSharedPreferencesBoolean(mContext, AppConstant.SP_KEY_PHONE_CHECKED, true);
                commit();

            }
            if (mCheckPhoneDialog != null) {
                mCheckPhoneDialog.dismiss();
            }
        }

        @Override
        public void onConvertFailed() {
            //跳转校验手机号页面
            gotoCheckPhoneNumber();
            DialogUtils.showShortPromptToast(mContext, R.string.get_phone_check_state_failed);
            if (mCheckPhoneDialog != null) {
                mCheckPhoneDialog.dismiss();
            }
        }
    }

    private void gotoCheckPhoneNumber() {
        //跳转校验手机号页面
        Intent intent = new Intent();
        intent.setClass(mContext, CheckPhoneNumberActivity.class);
        intent.putExtra(IntentConstant.PHONE_NUMBER, "");
        jump(intent, IntentConstant.RequestCodeCheckPhoneNumber);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtils.isEmpty(mExpertId) || mIsIOnlineExpertId) {
                DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.order_no_save, mConfirmListener);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentConstant.RequestCodeIdentifyType:
                    mIdentifyMethod = data.getIntExtra(IntentConstant.IdentifyType, mIdentifyMethod);
                    updateIdentifyType();
                    break;
                case IntentConstant.RequestCodeCheckPhoneNumber:
                    boolean isPhoneChecked = data.getBooleanExtra(IntentConstant.CHECK_PHONE_FLAG, false);
                    if (isPhoneChecked) {
                        commit();
                    } else {
                        DialogUtils.showShortPromptToast(mContext, R.string.check_phone_failed);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传之前, 为了减小传输量,图片裁剪+质量些许损耗压缩到100Kb
     */
    private void compressImages() {
        //图片压缩后的文件目录处理,如果存在,则删除子文件，否则直接创建
        File compressedDir = clearTempImageFiles();
        for (String rawFilePath : mUsingFilePathList) {
            File f = new File(rawFilePath);
            String name = f.getName();
            //图片裁剪至 2000*1500
            Bitmap bitmap = BitmapUtils.compressImageFromFile(f.getAbsolutePath(), CLIP_BP_WIDTH, CLIP_BP_HEIGHT);

            File compressedFile = new File(compressedDir, name);
            if (!compressedFile.exists()) {
                try {
                    compressedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            BitmapUtils.compressBmpToFile(bitmap, compressedFile);
            String outFilePath = compressedFile.getAbsolutePath();
            mUploadFilePathList.add(outFilePath);
        }

    }

    @NonNull
    private File clearTempImageFiles() {
        File compressedDir = new File(TAKE_PICTURE_COMPRESSED_DIR);
        if (!compressedDir.exists()) {
            compressedDir.mkdirs();
        } else {//先清空上次压缩的图片文件
            File[] files = compressedDir.listFiles();
            for (File f : files) {
                f.delete();
            }
        }
        return compressedDir;
    }

    private void commit() {
        //照片达到4张才可以提交
        if (TextUtils.isEmpty(mExpertId) || mIsIOnlineExpertId) {
            //如果是直接鉴定,或者在线专家鉴定,需要弹窗提示
            if (mUsingFilePathList == null || mUsingFilePathList.size() < 4) {
                DialogUtils.showShortPromptToast(mContext, R.string.picture_min_num);
                return;
            }
        }
        //鉴定要求
        String content = mContentView.getText().toString();
        if (TextUtils.isEmpty(content)) {
            DialogUtils.showShortPromptToast(mContext, R.string.indented_requirement_filed);
            return;
        }
        //用户未登录，则在点击事件中直接跳转登录界面
        if (!UserInfoUtils.checkUserLogin(mContext)) {
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            intent.putExtra(IntentConstant.TARGET_ACTIVITY, SubmitOrderActivity.class.getSimpleName());
            jump(intent);
            DialogUtils.showShortPromptToast(mContext, R.string.user_not_login);
            return;
        }
        if (SharedPreferencesUtils.getSharedPreferencesBoolean(mContext, AppConstant.SP_KEY_PHONE_CHECKED)) {
            mUploadFilePathList.clear();
            if (mUsingFilePathList != null) {
                compressImages();
            }
            if (NetUtils.isNetworkConnected(mContext)) {
                if (NetUtils.isWifiNet(mContext)) {
                    startUploadTask();
                } else if (NetUtils.isSecGenerationNet(mContext)) {
                    DialogUtils.showConfirmDialog(mContext, getString(R.string.warn),
                            StringUtils.getString(getString(R.string.second_Generation_Net), getUploadFilesTotalSize()), mCommitListener);
                } else {
                    DialogUtils.showWarnDialog(mContext, getString(R.string.warn), StringUtils
                            .getString(getString(R.string.mobile_net), getUploadFilesTotalSize()), mCommitListener);
                }
            }
        } else {
            //判断是否验证过手机号
            checkPhoneState();
        }
    }

    //获取上传列表文件大小
    private String getUploadFilesTotalSize() {
        long size = 0;
        for (String filePath : mUploadFilePathList) {
            File f = new File(filePath);
            if (!f.exists()) {
                continue;
            }
            size += f.length() / 1000;
        }
        return StringUtils.getString(size, "KB");
    }

    /**
     * 由于压缩图片是耗时操作,放在后台线程中,压缩完毕,则开始上传
     */
    private class CompressImagesAndUploadRunnable implements Runnable {

        @Override
        public void run() {
            String phone = UserInfoUtils.getPhone(mContext);
            String content = mContentView.getText().toString();
            // 提交数据
            HttpUtils httpUtils = new HttpUtils(60000);
            httpUtils.send(HttpRequest.HttpMethod.POST, NetConstant.HOST,
                    NetConstant.getCommitOrderParams(mContext, mIdentifyType,
                            mIdentifyMethod, content, phone, mExpertId, mUploadFilePathList),
                    new CommitListener());
        }
    }

    private void updateIdentifyType() {
        //挂号鉴定
        if (mIsIOnlineExpertId) {
            mIdentifyTypeView.setText(R.string.identify_type_registration);
            mIdentifyPriceView.setText(getIdentifyPrice(0));
            return;
        }
        switch (mIdentifyMethod) {
            case 0:
                mIdentifyTypeView.setText(R.string.identify_type_normal);
                mIdentifyPriceView.setText(getIdentifyPrice(0));
                break;
            case 1:
                mIdentifyTypeView.setText(R.string.identify_type_speed);
                mIdentifyPriceView.setText(getIdentifyPrice(1));
                break;
//            case 2:
//                mIdentifyTypeView.setText(R.string.identify_type_video);
//                mIdentifyPriceView.setText(getIdentifyPrice(2));
//                break;
            case 3:
                mIdentifyTypeView.setText(R.string.identify_type_order);
                //设置预约鉴定的价格
                mIdentifyPriceView.setText(getIdentifyPrice(2));
                break;
            case 4:
                mIdentifyTypeView.setText(R.string.identify_type_expert);
                mIdentifyPriceView.setText(getIdentifyPrice(3));
                break;
            case 5://快速鉴定
                mIdentifyTypeView.setText(R.string.identify_type_fast);
                mIdentifyPriceView.setText(getIdentifyPrice(4));
                break;
            default:
                break;
        }
    }

    private String getIdentifyPrice(int mIdentifyMethodIndex) {
        String orderprice = "￥5";
        if (TextUtils.isEmpty(mIdentifyMethodPrices)) {
            orderprice = AppConstant.IDENTIFY_PRICES[mIdentifyMethodIndex];
        } else {
            String[] prices = mIdentifyMethodPrices.split(",");
            if (prices.length >= 0) {
                orderprice = StringUtils.getString("￥", prices[mIdentifyMethodIndex]);
            } else {
                orderprice = AppConstant.IDENTIFY_PRICES[mIdentifyMethodIndex];
            }
        }
        return orderprice;
    }

    /**
     * 提交数据
     */
    private class CommitListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<UpLoadIdentifyData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UpLoadIdentifyData> task = new StringToBeanTask<>(UpLoadIdentifyData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (!NetUtils.isSecGenerationNet(mContext) && !NetUtils.isThirdGenerationNet(mContext) &&
                    !NetUtils.isNetworkConnected(mContext)) {
                //若是WiFi，则弹出该dialog
                DialogUtils.showWarnDialog(mContext, getString(R.string.warn), getString(R.string.submit_failed_warn_server),
                        mWarnListener);
            } else {
                //若不是WiFi，则弹出dialog
                DialogUtils.showWarnDialog(mContext, getString(R.string.warn), getString(R.string.submit_failed_warn_net),
                        mWarnListener);
            }
            // 对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            Map<String, String> submitFailedMap = new HashMap<>();
            submitFailedMap.put(UmengConstants.KEY_UPLOAD_FAIL_MSG, s);
            UmengUtils.onEvent(mContext, EventEnum.SubmitOrderFailed, submitFailedMap,
                    Long.valueOf(mTotalPictureSize).intValue());
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            super.onLoading(total, current, isUploading);
            if (mProgressDialog != null) {
                mProgressDialog.setProgress(current, total);
            }
        }

        @Override
        public void onConvertSuccess(UpLoadIdentifyData response) {
            // 清空编辑内容
            mContentView.setText("");
            // 对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            finish();
            // 启动支付
            Intent intent = new Intent(mContext, UserPayActivity.class);
            intent.putExtra(IntentConstant.USER_PAY_GOODS_ID, String.valueOf(response.getMessage()));
            intent.putExtra(IntentConstant.IdentifyType, mIdentifyTypeView.getText());
            jump(intent);
            if (response.isError()) {
                DialogUtils.showShortPromptToast(mContext, response.getMessage());
            }
        }

        @Override
        public void onConvertFailed() {
            // 清空编辑内容
            mContentView.setText("");
            // 对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            DialogUtils.showShortPromptToast(mContext, getString(R.string.submit_failed));
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

    /**
     * 注册监听网络状态变化的广播
     */
    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (!NetUtils.isNetworkConnected(mContext)) {
                    //无网络连接
                    DialogUtils.showWarnDialog(mContext, getString(R.string.warn), getString(R.string.not_network),
                            mWarnListener);
                }
            }
        }
    };
}
