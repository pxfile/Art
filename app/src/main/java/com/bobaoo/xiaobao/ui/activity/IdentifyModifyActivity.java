package com.bobaoo.xiaobao.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.constant.UmengConstants;
import com.bobaoo.xiaobao.domain.AuthCodeResponse;
import com.bobaoo.xiaobao.domain.ModifyIdnentifyData;
import com.bobaoo.xiaobao.domain.UpLoadIdentifyData;
import com.bobaoo.xiaobao.manager.IdentifyModifyManager;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.ui.adapter.SubmitOrderPictureAdapter;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by you on 2015/7/14.
 */
public class IdentifyModifyActivity extends BaseActivity {
    private final int MAX_PHOTOS_NUM = 8;
    //订单ID
    private String mOrderID;
    //藏品分类
    private String mParamsOrderKind;
    //是否公开
    private String mParamsIsPublic;
    //藏品描述
    private String mParamsRemark;
    //鉴定类型
    private int mParamsIdentifyMethod;
    //电话
    private String mParamsPhone;
    //指定的专家ID
    private String mParamsExpertId;
    //编辑后,未改动的OriginalString,";"分隔
    private String mParamsRemainOraginalString;

    //订单图片，包括网络的和修改后的文件路径
    private int mCurrentEditIndex = -1;//当前正在修改的索引

    //图片的Url,有可能是本地图片，有可能是网络图片
    private ArrayList<String> mPhotos;
    //原始的提交文件数据,如果某一元素已经修改，则剔除对应位置的字串
    private List<String> mOriginalPaths;
    //提交的文件列表
    private List<String> mSubmitFilePaths;

    //专家支持的鉴定方式及相应的价格
    private String mIdentifyMethodSwitchInfos;
    private String mIdentifyMethodPrices;
    //是否是挂号鉴定 0:是 1:否
    private String mExpertFrom;

    //Views
    private RecyclerView mPicturePreviewView;
    private SubmitOrderPictureAdapter mAdapter;

    private EditText mRemarkET;
    private View mSubmitView;
    private View mChooseType;
    private TextView mIdentifyTypeView;
    private TextView mIdentifyPriceView;
    private ImageView mStepDescView;
    private ImageView mArrowsView;
    // dialog
    private ProgressDialog mProgressDialog;
    private ProgressDialog mCheckPhoneDialog;

    private DialogInterface.OnClickListener mDialogConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            IdentifyModifyManager.getInstance().clearCache();
            finish();
        }
    };

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mOrderID = intent.getStringExtra(IntentConstant.ORDER_ID);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //保存当前的修改信息
        String fileName = intent.getStringExtra(IntentConstant.PictureFilePath);
        IdentifyModifyManager.getInstance().saveCurrentEditInfo(mOrderID, mCurrentEditIndex, fileName);
    }

    @Override
    protected void initData() {
        // 获取图片信息
        if (mPhotos == null) {
            mPhotos = new ArrayList<>();
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_submit_order;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.modify_order_info);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mArrowsView = (ImageView) findViewById(R.id.img_arrows);
        mArrowsView.setVisibility(View.VISIBLE);
        // 要求
        mStepDescView = (ImageView) findViewById(R.id.iv_step_attach);
        mStepDescView.setImageResource(R.drawable.icon_step_3);
        mPicturePreviewView = (RecyclerView) findViewById(R.id.rv);
        mRemarkET = (EditText) findViewById(R.id.et_content);
        mSubmitView = findViewById(R.id.tv_submit);

        //Identify type
        mChooseType = findViewById(R.id.ll_type);
        mIdentifyTypeView = (TextView) findViewById(R.id.tv_identify_type);
        mIdentifyPriceView = (TextView) findViewById(R.id.tv_price);
        setOnClickListener(mSubmitView, mChooseType);
    }

    private void initPictureRecyclerView() {
        mAdapter = new SubmitOrderPictureAdapter(mContext, mPhotos, Integer.parseInt(mParamsOrderKind), this);
        mPicturePreviewView.setAdapter(mAdapter);
        // 设置横向滚动
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPicturePreviewView.setLayoutManager(manager);
    }

    /**
     * 根据修改信息，设置有关的订单修改信息
     */
    private void prepareParamsForIdentifyModify(ModifyIdnentifyData.DataEntity modifyIdnentifyDataEntity) {
        mOriginalPaths = modifyIdnentifyDataEntity.getOriginal();
        mParamsRemainOraginalString =
                IdentifyModifyManager.getInstance().getOriginalPathsAfterEdit(mOrderID, mOriginalPaths);
        mSubmitFilePaths = IdentifyModifyManager.getInstance().getCommitFilePaths(mOrderID);
    }

    /**
     * 提交订单数据
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
            // 对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            DialogUtils.showShortPromptToast(mContext, getString(R.string.submit_failed));
            Map<String, String> submitFailedMap = new HashMap<>();
            submitFailedMap.put(UmengConstants.KEY_UPLOAD_FAIL_MSG, s);
            UmengUtils.onEvent(mContext, EventEnum.SubmitOrderFailed, submitFailedMap);
            IdentifyModifyManager.getInstance().clearCache();
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
            // 对话框消失
            IdentifyModifyManager.getInstance().clearCache();
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
            } else {
                DialogUtils.showShortPromptToast(mContext, getString(R.string.submit_success));
            }
        }

        @Override
        public void onConvertFailed() {
            // 对话框消失
            mProgressDialog.dismiss();
            mProgressDialog = null;
            DialogUtils.showShortPromptToast(mContext, getString(R.string.submit_failed));
        }
    }

    /**
     * 获取订单信息
     */
    private class IdentifyModifyDataListener extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<ModifyIdnentifyData> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<ModifyIdnentifyData> task = new StringToBeanTask<>(ModifyIdnentifyData.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
        }

        @Override
        public void onConvertSuccess(ModifyIdnentifyData response) {
            ModifyIdnentifyData.DataEntity dataEntity = response.getData();
            mPhotos.addAll(dataEntity.getPhoto());
            mParamsIsPublic = dataEntity.getIs_public();
            mParamsOrderKind = dataEntity.getKind();

            mOriginalPaths = dataEntity.getOriginal();
            mParamsRemainOraginalString = IdentifyModifyManager.getInstance().getStringFromList(mOriginalPaths);
            mParamsExpertId = dataEntity.getSpecify_expert_id();
            try {
                mParamsIdentifyMethod = Integer.parseInt(dataEntity.getJb_type());
            } catch (NumberFormatException e) {
                mParamsIdentifyMethod = 1;
            }

            mParamsPhone = dataEntity.getPhone();
            mParamsRemark = dataEntity.getNote();

            //缓存订单信息
            IdentifyModifyManager.getInstance().saveModifyIdnentifyData(mOrderID, response);
            if (!TextUtils.isEmpty(mParamsRemark.trim())) {
                mRemarkET.setText(mParamsRemark);
            } else {
                mRemarkET.setHint(R.string.et_content_hint);
            }

            mIdentifyMethodSwitchInfos = dataEntity.getServe_open();
            mIdentifyMethodPrices = dataEntity.getServe_price();
            mExpertFrom = dataEntity.getExpert_from();
            if (TextUtils.equals("0", mExpertFrom)) {
                mParamsIdentifyMethod = 0;//如果是挂号鉴定，则指定为普通鉴定
            }
            updateIdentifyTypeUI();
            initPictureRecyclerView();
        }

        @Override
        public void onConvertFailed() {
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        HashMap<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_PAY_GOODS_ID, mOrderID);
        switch (view.getId()) {
            case R.id.iv_delete://修改图片
                mCurrentEditIndex = (int) view.getTag();//当前正在编辑的索引
                if (mCurrentEditIndex < AppConstant.DefaultTakePictureNum) {
                    //告诉相机和预览页面，为修改订单操作
                    intent.putExtra(IntentConstant.MODIFY_ORDER, true);
                    intent.setClass(mContext, TakePictureActivity.class);
                    jump(intent);
                    UmengUtils.onEvent(mContext, EventEnum.Submit_Identify_Modify_Update, map);
                } else {
                    mPhotos.remove(mCurrentEditIndex);
                    mAdapter.notifyDataSetChanged();
                    IdentifyModifyManager.getInstance().deleteEditItem(mOrderID, mCurrentEditIndex);
                    UmengUtils.onEvent(mContext, EventEnum.Submit_Identify_Modify_Delete, map);
                }
                //缓存电话号码和鉴定要求,用于拍照返回显示
                savePhoneAndRemark();
                break;
            case R.id.ll_container:
                if (mPhotos != null && mPhotos.size() >= MAX_PHOTOS_NUM) {
                    DialogUtils.showShortPromptToast(mContext, R.string.submit_max_photos_num_tip);
                    return;
                }
                mCurrentEditIndex = (int) view.getTag();//当前正在编辑的索引
                intent.putExtra(IntentConstant.MODIFY_ORDER, true);
                intent.putExtra(IntentConstant.IdentifyType, Integer.parseInt(mParamsOrderKind));
                intent.setClass(mContext, TakePictureActivity.class);
                jump(intent);
                //缓存电话号码和鉴定要求
                savePhoneAndRemark();
                UmengUtils.onEvent(mContext, EventEnum.Submit_Identify_Modify_Add, map);
                break;
            case R.id.tv_submit:
                if (SharedPreferencesUtils.getSharedPreferencesBoolean(mContext, AppConstant.SP_KEY_PHONE_CHECKED)) {
                    commitModifiedIdentify();
                    UmengUtils.onEvent(mContext, EventEnum.Submit_Identify_Item_Modify, map);
                } else {
                    //获取手机是否校验信息
                    checkPhoneState();
                }
                break;
            case R.id.ll_type:
                //在线专家鉴定,不能修改鉴定方式
                if (!TextUtils.equals("0", mExpertFrom)) {
                    intent.setClass(mContext, IdentifyTypeActivity.class);
                    intent.putExtra(IntentConstant.IdentifyId, mParamsIdentifyMethod);
                    intent.putExtra(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
                    intent.putExtra(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
                    jump(intent, IntentConstant.RequestCodeIdentifyType);
                    
                }
                UmengUtils.onEvent(mContext,EventEnum.User_Identify_Type);
                break;
            case R.id.tv_back:
                DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.modify_order, mDialogConfirmListener);
                break;
            default:
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
     * 提交数据
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
                commitModifiedIdentify();

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

    private void commitModifiedIdentify() {
        if (TextUtils.isEmpty(mParamsPhone)) {
            mParamsPhone = UserInfoUtils.getPhone(mContext);
        }
        if (StringUtils.checkPhoneNumber(mParamsPhone)) {
            if (mProgressDialog == null) {
                mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.submit_loading));
            }
            mParamsRemark = mRemarkET.getText().toString().trim();
            prepareParamsForIdentifyModify(IdentifyModifyManager.getInstance().getModifyIdnentifyData(mOrderID).getData());
            new HttpUtils().send(HttpRequest.HttpMethod.POST, NetConstant.HOST,
                    NetConstant.getCommitModifyIndentifyParams(
                            mContext, mOrderID, mParamsOrderKind,
                            mParamsIsPublic, mParamsRemark, mParamsIdentifyMethod,
                            mParamsPhone, mParamsExpertId, mParamsRemainOraginalString,
                            IdentifyModifyManager.getInstance().getEdittedIndexs(mOrderID),
                            mSubmitFilePaths
                    ), new CommitListener());
        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.input_valid_phone_number);
            gotoCheckPhoneNumber();
        }
    }

    private void savePhoneAndRemark() {
        String remark;
        remark = mRemarkET.getText().toString().trim();
        IdentifyModifyManager.getInstance().saveRemark(mOrderID, remark);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentConstant.RequestCodeIdentifyType:
                    mParamsIdentifyMethod = data.getIntExtra(IntentConstant.IdentifyType, mParamsIdentifyMethod);
                    IdentifyModifyManager.getInstance().saveIdentifyMethod(mOrderID, StringUtils.getString(mParamsIdentifyMethod));
                    updateIdentifyTypeUI();
                    break;
                case IntentConstant.RequestCodeCheckPhoneNumber:
                    boolean isPhoneChecked = data.getBooleanExtra(IntentConstant.CHECK_PHONE_FLAG, false);
                    if (isPhoneChecked) {
                        commitModifiedIdentify();
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
     * 设置鉴定方式UI
     */
    private void updateIdentifyTypeUI() {
        if (TextUtils.equals("0", mExpertFrom)) {
            mIdentifyTypeView.setText(R.string.identify_type_registration);
            mIdentifyPriceView.setText(getIdentifyPrice(0));
            mArrowsView.setVisibility(View.GONE);
            return;
        }else {
            mArrowsView.setVisibility(View.VISIBLE);
        }
        switch (mParamsIdentifyMethod) {
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

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {
        if (IdentifyModifyManager.getInstance().checkIdentifyDataEmpty(mOrderID)) {
            new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.HOST,
                    NetConstant.getModifyIdnetifyParams(mContext, mOrderID), new IdentifyModifyDataListener());
        } else {
            ModifyIdnentifyData modifyIdnentifyData = IdentifyModifyManager.getInstance().getModifyIdnentifyData(mOrderID);
            ModifyIdnentifyData.DataEntity modifyIdnentifyDataEntity = modifyIdnentifyData.getData();
            //网络请求参数
            mParamsPhone = modifyIdnentifyDataEntity.getPhone();
            mParamsOrderKind = modifyIdnentifyDataEntity.getKind();
            mParamsIsPublic = modifyIdnentifyDataEntity.getIs_public();
            mParamsIdentifyMethod = Integer.parseInt(modifyIdnentifyDataEntity.getJb_type());
            mParamsExpertId = modifyIdnentifyDataEntity.getSpecify_expert_id();
            mParamsRemark = modifyIdnentifyDataEntity.getNote().trim();
            try {
                mParamsIdentifyMethod = Integer.parseInt(modifyIdnentifyDataEntity.getJb_type());
            } catch (NumberFormatException e) {
                mParamsIdentifyMethod = 1;
            }

            mIdentifyMethodSwitchInfos = modifyIdnentifyDataEntity.getServe_open();
            mIdentifyMethodPrices = modifyIdnentifyDataEntity.getServe_price();
            mExpertFrom = modifyIdnentifyDataEntity.getExpert_from();
            if (TextUtils.equals("0", mExpertFrom)) {
                mParamsIdentifyMethod = 0;//如果是挂号鉴定，则指定为普通鉴定
            }
            mPhotos.clear();
            //获得编辑结果的最终图片列表
            mPhotos.addAll(IdentifyModifyManager.getInstance().getInvalidDisplayPhotos(mOrderID));
            initPictureRecyclerView();
            if (!TextUtils.isEmpty(mParamsRemark.trim())) {
                mRemarkET.setText(mParamsRemark);
            } else {
                mRemarkET.setHint(R.string.et_content_hint);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.modify_order, mDialogConfirmListener);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
