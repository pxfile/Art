package com.bobaoo.xiaobao.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.HeadImageUploaderResponse;
import com.bobaoo.xiaobao.domain.UserPrivateInfo;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.utils.BitmapUtils;
import com.bobaoo.xiaobao.utils.CommonUtils;
import com.bobaoo.xiaobao.utils.DialogUtils;
import com.bobaoo.xiaobao.utils.FileUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;

/**
 * Created by chenming on 2015/5/27.
 * 用户信息编辑提交
 */
public class UserPrivateInfoActivity extends BaseActivity {

    private SimpleDraweeView mPortraitView;
    private TextView mNameView;
    private View mChangePortraitView;
    private TextView mPhoneEditView;
    private TextView mNickNameEditView;
    private View mChangePasswordView;
    private View mChangeNickNameView;
    private View mChangePhoneView;

    private final String PHOTO_IMG_NAME = "head.jpg";//个人头像的拍照保存路径
    private final String HEAD_IMG_UPLOAD_FILE = "head_img_upload.png";
    public final static String USER_NICKNAME = "user_nickname";
    private Uri mHeadUri;//个人头像的Uir

    private boolean mIsPhoneCheckedFlag;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_private_userinfo;
    }

    @Override
    protected void initTitle() {
        View headView = findViewById(R.id.layout_title);
        headView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange2));
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.edit_introduce));
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mPortraitView = (SimpleDraweeView) findViewById(R.id.img_head);
        mNameView = (TextView) findViewById(R.id.tv_user_name);
        mChangePortraitView = findViewById(R.id.tl_change_head_img);
        mPhoneEditView = (TextView) findViewById(R.id.et_phone);
        mNickNameEditView = (TextView) findViewById(R.id.et_nick_name);
        mChangePasswordView = findViewById(R.id.rl_change_password);
        mChangeNickNameView = findViewById(R.id.rl_edit_nickname);
        mChangePhoneView = findViewById(R.id.rl_edit_phone_entry);
        if (UserInfoUtils.getSocialLoginFlg(this)) {
            mChangePasswordView.setVisibility(View.GONE);
        } else {
            mChangePasswordView.setVisibility(View.VISIBLE);
        }
        onScroll();
        setOnClickListener(mChangePasswordView, mChangePortraitView, mChangeNickNameView, mChangePhoneView);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
    }

    @Override
    protected void refreshData() {
        String cacheHeadImgUrl = UserInfoUtils.getCacheHeadImagePath(mContext);
        if (!TextUtils.isEmpty(cacheHeadImgUrl)) {
            mPortraitView.setImageURI(Uri.parse(cacheHeadImgUrl));
        }
        getData();
    }

    @Override
    public void onClick(View view) {
        final String mobile = mPhoneEditView.getText().toString().trim();
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_change_password:
                jump(mContext, EditPassWordActivity.class);
                break;
            case R.id.tl_change_head_img:
                showPickDialog();
                break;
            case R.id.rl_edit_nickname:
                intent = new Intent(mContext, EditNickNameActivity.class);
                intent.putExtra(USER_NICKNAME, mNickNameEditView.getText().toString().trim());
                jump(intent);
                break;
            case R.id.rl_edit_phone_entry:
                if (!CommonUtils.checkPhoneNumber(mobile)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
                    return;
                }
                intent = new Intent(mContext, CheckPhoneNumberActivity.class);
                intent.putExtra(IntentConstant.PHONE_NUMBER, mobile);
                jump(intent, 4);
                break;
            default:
                break;
        }
    }

    private void getData() {
        new HttpUtils().configCurrentHttpCacheExpiry(0)
                .send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getUserPrivateInfoParams(mContext), new UserPrivateInfoListener());
    }


    /**
     * 选择提示对话框
     */
    private void showPickDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_head_img, null);
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL);
        dialogWindow.setAttributes(lp);
        dialog.show();

        View mSelectCamera = view.findViewById(R.id.ll_select_camera);
        View mSelectPhoto = view.findViewById(R.id.ll_select_photo);

        if (mSelectCamera != null) {
            mSelectCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMG_NAME)));
                    startActivityForResult(intent, 2);
                }
            });
        }

        if (mSelectPhoto != null) {
            mSelectPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                mHeadUri = data.getData();
                startPhotoZoom(mHeadUri);
            } else if (requestCode == 2) {
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + PHOTO_IMG_NAME);
                mHeadUri = Uri.fromFile(temp);
                startPhotoZoom(mHeadUri);
            } else if (requestCode == 3) {
                Bitmap photo = BitmapUtils.decodeUriAsBitmap(this, mHeadUri);
                if (photo != null) {
                    Bitmap roundPhoto = BitmapUtils.getRoundCorner(photo, photo.getWidth() / 2, 1, 2, 3, 4);
                    FileUtils.saveBitmapToFile(roundPhoto, Environment.getExternalStorageDirectory() + File.separator + HEAD_IMG_UPLOAD_FILE);
                    startUpLoadHeadImg();
                }
            } else if (requestCode == 4) {
                mIsPhoneCheckedFlag = data.getBooleanExtra(IntentConstant.CHECK_PHONE_FLAG, false);
                if (mIsPhoneCheckedFlag) {
                    DialogUtils.showShortPromptToast(mContext, R.string.check_phone_success);
                } else {
                    DialogUtils.showShortPromptToast(mContext, R.string.check_phone_failed);
                }
            }
        }
    }

    private void startUpLoadHeadImg() {
        File headImg = new File(Environment.getExternalStorageDirectory(), HEAD_IMG_UPLOAD_FILE);
        new HttpUtils().send(HttpRequest.HttpMethod.POST, NetConstant.HOST, NetConstant.getUpLoadHeadImgParams(mContext, headImg), new UpLoadHeadImgListener());
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHeadUri);
        startActivityForResult(intent, 3);
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

    private class UserPrivateInfoListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<UserPrivateInfo> {

        @Override
        public void onConvertSuccess(UserPrivateInfo data) {
            if (data == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.load_user_info_failed);
                return;
            }

            if (!TextUtils.isEmpty(data.getData().getHead_img())) {
                mPortraitView.setImageURI(Uri.parse(data.getData().getHead_img()));
            }
            mNameView.setText(data.getData().getUser_name());
            mNickNameEditView.setText(data.getData().getNikename());
            String num = data.getData().getMobile();
            mPhoneEditView.setText(num);
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext, R.string.load_user_info_failed);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<UserPrivateInfo> task = new StringToBeanTask<>(UserPrivateInfo.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.load_user_info_failed);
        }
    }

    private class UpLoadHeadImgListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<HeadImageUploaderResponse> {
        @Override
        public void onConvertSuccess(HeadImageUploaderResponse headImageUploaderResponse) {
            if (headImageUploaderResponse == null || headImageUploaderResponse.isError()) {
                return;
            }
            String imgUrl = headImageUploaderResponse.getMessage();
            if (!TextUtils.isEmpty(imgUrl)) {
                UserInfoUtils.saveCacheHeadImagePath(UserPrivateInfoActivity.this, imgUrl);
                mPortraitView.setImageURI(Uri.parse(imgUrl));
            }
        }

        @Override
        public void onConvertFailed() {
            DialogUtils.showShortPromptToast(mContext, R.string.upload_head_image_failed);
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<HeadImageUploaderResponse> task = new StringToBeanTask<>(HeadImageUploaderResponse.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.upload_head_image_failed);
        }
    }
}
