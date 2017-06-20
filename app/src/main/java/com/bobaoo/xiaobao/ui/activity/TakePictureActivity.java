package com.bobaoo.xiaobao.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.application.IdentifyApplication;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.FileUtils;
import com.bobaoo.xiaobao.utils.MPermissionUtil;
import com.bobaoo.xiaobao.utils.SizeUtils;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 15/6/1.
 */

public class TakePictureActivity extends BaseActivity implements SurfaceHolder.Callback {
    // View
    private SurfaceView mSurfaceView;
    private ImageView mFrameView;
    private View mTakePictureView;
    private View mSelectPictureView;
    private TextView mNumView;
    private TextView mDescView;
    // Camera
    private Camera mCamera;
    // 确定使用图片的路径
    private ArrayList<String> mUsingFilePathList;
    // 保存拍照的图片文件地址
    private String mFilePath;
    // 总共的图片个数
    private int mTotalPictureNum;
    // 目前的图片
    private int mCurrentPictureNum;
    // 鉴定类型
    private int mIdentifyType;
    // 3视图
    private String[] mViewsString;
    // 3视图
    private int[] mViewsFrameRes;

    //订单修改标记
    private boolean mIsOrderModify;

    //在线专家挂号鉴定数据
    private boolean mIsOnlineExpert;
    private String mExpertId;
    private String mIdentifyMethodSwitchInfos;//记录该专家是否开通了相应的鉴定方式
    private String mIdentifyMethodPrices;//记录该专家鉴定方式的价格

    //专家挂号鉴定数据
    private boolean mIdentifyTypeRegistration;

    private DialogInterface.OnClickListener mConfirmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mUsingFilePathList != null) {
                mUsingFilePathList.clear();
            }
            finish();
        }
    };

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        // 总共需要拍摄几张图片
        mTotalPictureNum = intent.getIntExtra(IntentConstant.TotalPictureNum, 0);
        // 当前拍摄第几张图片
        mCurrentPictureNum = getIntent().getIntExtra(IntentConstant.CurrentPictureNum, 0);
        // 拍摄图片的类型
        mIdentifyType = getIntent().getIntExtra(IntentConstant.IdentifyType, AppConstant.IdentifyTypeChina);

        //暂存鉴定类型,周期(鉴定--拍照--提交)
        IdentifyApplication.setIntentData(IntentConstant.IdentifyType, mIdentifyType);

        //暂存专家ID和是否是在线专家挂号鉴定
        mIsOnlineExpert = intent.getBooleanExtra(IntentConstant.IS_ONLINE_EXPERT, false);
        mExpertId = intent.getStringExtra(IntentConstant.EXPERT_ID);
        mIdentifyMethodSwitchInfos = intent.getStringExtra(IntentConstant.IdentifyMethodInfo);
        mIdentifyMethodPrices = intent.getStringExtra(IntentConstant.IdentifyMethodPrices);

        mIdentifyTypeRegistration = intent.getBooleanExtra(IntentConstant.IDENTIFY_TYPE_REGISTRATION, false);
        //挂号鉴定的数据,单独处理
        if (mIsOnlineExpert || mIdentifyTypeRegistration) {
            IdentifyApplication.setIntentData(IntentConstant.IS_ONLINE_EXPERT, mIsOnlineExpert ? mIsOnlineExpert : mIdentifyTypeRegistration);
            IdentifyApplication.setIntentData(IntentConstant.EXPERT_ID, mExpertId);
            IdentifyApplication.setIntentData(IntentConstant.IdentifyMethodInfo, mIdentifyMethodSwitchInfos);
            IdentifyApplication.setIntentData(IntentConstant.IdentifyMethodPrices, mIdentifyMethodPrices);
        }

        // 是否是修改订单
        mIsOrderModify = getIntent().getBooleanExtra(IntentConstant.MODIFY_ORDER, false);
    }

    @Override
    protected void initData() {
        // 禁止右滑返回
        setSwipeBackEnable(false);
        MPermissionUtil.requestPermissionCamera(this);
        // 根据鉴定类型初始化轮廓和描述
        switch (mIdentifyType) {
            case AppConstant.IdentifyTypeChina:
                mViewsString = getResources().getStringArray(R.array.view_3d_china);
                mViewsFrameRes = new int[]{R.drawable.frame_china_1, R.drawable.frame_china_2, R.drawable.frame_china_3};
                break;
            case AppConstant.IdentifyTypeJade:
                mViewsString = getResources().getStringArray(R.array.view_3d_jade);
                mViewsFrameRes = new int[]{R.drawable.frame_jade_1, R.drawable.frame_jade_2, R.drawable.frame_jade_3};
                break;
            case AppConstant.IdentifyTypeSundry:
                mViewsString = getResources().getStringArray(R.array.view_3d_sundry);
                mViewsFrameRes = new int[]{};
                break;
            case AppConstant.IdentifyTypePainting:
                mViewsString = getResources().getStringArray(R.array.view_3d_painting);
                mViewsFrameRes = new int[]{R.drawable.frame_painting_1, R.drawable.frame_painting_2};
                break;
            case AppConstant.IdentifyTypeBronze:
                mViewsString = getResources().getStringArray(R.array.view_3d_bronze);
                mViewsFrameRes = new int[]{R.drawable.frame_bronze_1, R.drawable.frame_bronze_2, R.drawable.frame_bronze_3};
                break;
            case AppConstant.IdentifyTypeWooden:
                mViewsString = getResources().getStringArray(R.array.view_3d_wooden);
                mViewsFrameRes = new int[]{R.drawable.frame_wooden_1};
                break;
            case AppConstant.IdentifyTypeMoneyPaper:
                mViewsString = getResources().getStringArray(R.array.view_3d_money_paper);
                mViewsFrameRes = new int[]{R.drawable.frame_money_paper_1, R.drawable.frame_money_paper_2,
                        R.drawable.frame_money_paper_3};
                break;
            case AppConstant.IdentifyTypeMoneySilver:
                mViewsString = getResources().getStringArray(R.array.view_3d_money_silver);
                mViewsFrameRes =
                        new int[]{R.drawable.frame_money_silver_1, R.drawable.frame_money_silver_2,
                                R.drawable.frame_money_silver_3};
                break;
            case AppConstant.IdentifyTypeMoneyBronze:
                mViewsString = getResources().getStringArray(R.array.view_3d_money_bronze);
                mViewsFrameRes =
                        new int[]{R.drawable.frame_money_bronze_1, R.drawable.frame_money_bronze_2,
                                R.drawable.frame_money_bronze_3};
                break;
        }
        // 获取目前拍摄好的图片列表
        mUsingFilePathList = (ArrayList<String>) IdentifyApplication.getIntentData(IntentConstant.UsingPictureFilePaths);
        // 如果目前没有拍摄好的图片列表，现在创建
        if (mUsingFilePathList == null) {
            mUsingFilePathList = new ArrayList<>();
            IdentifyApplication.setIntentData(IntentConstant.UsingPictureFilePaths, mUsingFilePathList);
        }
        // 获取缓存图片的位置
        mFilePath = FileUtils.getImageCacheFile(mContext);
        // 修正总共需要拍摄几张图片
        mTotalPictureNum = Math.max(mTotalPictureNum, mViewsString.length);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.PERMISSION_REQ_CODE_READ_CAMERA) {
            MPermissionUtil.requestPermissionStorage(this);
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_take_picture;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(R.string.take_picture_identify);
        setOnClickListener(backView);
        // 第几张
        mNumView = (TextView) findViewById(R.id.tv_right);
    }

    @Override
    protected void initContent() {
        // 拍照预览
        mSurfaceView = (SurfaceView) findViewById(R.id.sv);
        // 框架
        mFrameView = (ImageView) findViewById(R.id.iv_frame);
        //选择相册
        mSelectPictureView = findViewById(R.id.tv_select_picture);
        // 拍照按钮
        mTakePictureView = findViewById(R.id.tv_take_picture);
        // 拍摄描述
        mDescView = (TextView) findViewById(R.id.tv_desc);
        // 添加单击监听
        setOnClickListener(mTakePictureView, mSelectPictureView);
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
        // 获取holder
        SurfaceHolder holder = mSurfaceView.getHolder();
        // deprecated setting, but required on Android versions prior to 3.0
        // 设置SurfaceView不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 添加回调接口
        holder.addCallback(this);
    }

    @Override
    protected void refreshData() {
        // 显示第几张
        mNumView.setText(StringUtils.getString(mCurrentPictureNum + 1, "/", mTotalPictureNum));
        // 现实描述信息
        if (mCurrentPictureNum < mViewsString.length) {
            mDescView.setText(mViewsString[mCurrentPictureNum]);
        } else {
            mDescView.setText(R.string.detail_view);
        }
        // 显示frame
        if (mCurrentPictureNum < mViewsFrameRes.length) {
            mFrameView.setVisibility(View.VISIBLE);
            mFrameView.setImageResource(mViewsFrameRes[mCurrentPictureNum]);
        } else {
            mFrameView.setVisibility(View.INVISIBLE);
        }
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    /**
     * SurfaceView创建时，建立Camera和SurfaceView的联系
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            // 开启相机
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    mCamera = Camera.open(0);
                    // i=0 表示后置相机
                } else {
                    mCamera = Camera.open();
                }
            } catch (Exception e) {
                e.printStackTrace();
                DialogUtils.showLongPromptToast(mContext, "无法打开摄像头，请授予相关权限");
                finish();
            }
        }
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            mCamera = null;
            e.printStackTrace();
            DialogUtils.showLongPromptToast(mContext, "无法打开摄像头，请授予相关权限");
            finish();
        }
        //实现相机的参数初始化
        if (mCamera != null) {
            initCamera();
        }
    }

    /**
     * SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            // 启动Camera
            try {
                mCamera.startPreview();
            } catch (Exception e) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                e.printStackTrace();
            }
        }
    }

    /**
     * SurfaceView销毁时，取消Camera预览
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_picture:
                mSelectPictureView.setEnabled(false);
                mTakePictureView.setEnabled(false);
                // 点击拍照
                mCamera.takePicture(new CameraShutter(), null, new CameraPicture());
                break;
            case R.id.tv_select_picture:
                mSelectPictureView.setEnabled(false);
                mTakePictureView.setEnabled(false);
                //打开图片选择界面
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                jump(intent, IntentConstant.RequestCodeSelectPicture);
                UmengUtils.onEvent(mContext, EventEnum.RequestCodeSelectPicture);
                break;
            case R.id.tv_back:
                if (mUsingFilePathList != null && mUsingFilePathList.size() > 0) {
                    DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.picture_no_save, mConfirmListener);
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 释放相机
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        UmengUtils.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mUsingFilePathList != null && mUsingFilePathList.size() > 0) {
                DialogUtils.showConfirmDialog(mContext, R.string.submit_quit, R.string.picture_no_save, mConfirmListener);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentConstant.RequestCodeTakePicture:
                    break;
                case IntentConstant.RequestCodeSelectPicture:
                    // 获取到图片路径
                    String fileName;
                    Uri contentUri = data.getData();
                    fileName = contentUri.getPath();
                    File file = new File(fileName);
                    if (!file.exists()) {
                        fileName = FileUtils.getPhotoPath(this, contentUri);
                    }
                    onPictureTake(fileName);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
        mSelectPictureView.setEnabled(true);
        mTakePictureView.setEnabled(true);
    }

    private void onPictureTake(String fileName) {
        // 判断是否是修改订单
        if (mIsOrderModify) {
            // 如果是修改订单，直接返回拍摄的文件
            Intent intent = new Intent(mContext, IdentifyModifyActivity.class);
            intent.putExtra(IntentConstant.PictureFilePath, fileName);
            jump(intent);
        } else {
            // 如果不是修改订单，判断是否是修改照片
            if (mCurrentPictureNum < mUsingFilePathList.size()) {
                // 如果当前图片的num小于正在使用的图片列表的长度，则认为是在修改图片
                mUsingFilePathList.remove(mCurrentPictureNum);
                mUsingFilePathList.add(mCurrentPictureNum, fileName);
                // 回到提交订单页面
                jump(mContext, SubmitOrderActivity.class);
            } else {
                // 如果当前图片的num大于等于正在使用的图片列表的长度，则认为是在添加图片
                mUsingFilePathList.add(fileName);
                // 增加完图片后判断拍摄工作是否完成
                if (mUsingFilePathList.size() >= mTotalPictureNum) {
                    // 如果这在使用的图片列表的长度与所有图片的大小相等，则认为拍摄任务完,回到提交订单页面
                    jump(mContext, SubmitOrderActivity.class);
                    finish();
                } else {
                    // 如果这在使用的图片列表的长度小于所有图片的大小相等，则认为拍摄任务没有完成，继续拍摄
                    mCurrentPictureNum++;
                    refreshData();
                }
            }
        }
        mSelectPictureView.setEnabled(true);
        mTakePictureView.setEnabled(true);
    }

    private void initCamera() {
        // 获取Camera的参数对象
        Parameters parameters = mCamera.getParameters();
        // 设置预览图片尺寸
        Size largestSize = getBestSupportedSize(parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(largestSize.width, largestSize.height);
        // 设置捕捉图片尺寸
        largestSize = getBestSupportedSize(parameters.getSupportedPictureSizes());
        parameters.setPictureSize(largestSize.width, largestSize.height);
        // 设置图片格式
        parameters.setPictureFormat(PixelFormat.JPEG);
//        // 开启闪光灯
//        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        // 连续对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        // 如果要实现连续的自动对焦，这一句必须加上
        mCamera.cancelAutoFocus();

        // 设置画面垂直
        mCamera.setDisplayOrientation(90);
        parameters.setRotation(90);
        parameters.set("orientation", "portrait");
        parameters.set("rotation", 90);

        // 设置Camera的参数对象
        mCamera.setParameters(parameters);
        //实现自动对焦
        try {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        //只有加上了这一句，才会自动对焦。
                        camera.cancelAutoFocus();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得最接近频幕宽度的支持尺寸,该尺寸必须是系统支持的尺寸
     */
    private Size getBestSupportedSize(List<Size> sizeList) {
        if (sizeList != null && sizeList.size() > 0) {
            int screenWidth = SizeUtils.getScreenWidth(this);
            int[] array = new int[sizeList.size()];
            int temp = 0;
            for (Size size : sizeList) {
                array[temp++] = Math.abs(size.width - screenWidth);
            }
            //取差值最小的那个size
            temp = 0;
            int bestIndex = 0;
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    temp = array[i];
                    bestIndex = 0;
                } else {
                    if (array[i] < temp) {
                        bestIndex = i;
                        temp = array[i];
                    }
                }
            }
            return sizeList.get(bestIndex);
        }
        return null;
    }

    /**
     * 图像数据处理还未完成时的回调函数
     */
    private class CameraShutter implements Camera.ShutterCallback {

        @Override
        public void onShutter() {
            // 一般显示进度条
        }
    }

    /**
     * 图像数据处理完成后的回调函数
     */
    private class CameraPicture implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 保存图片
            String fileName = StringUtils.getString(mFilePath, System.currentTimeMillis(), ".jpg");
            FileUtils.saveByteToFile(data, fileName);
            onPictureTake(fileName);
        }
    }
}