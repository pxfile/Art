package com.bobaoo.xiaobao.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.NetConstant;
import com.bobaoo.xiaobao.domain.EditPassWdResp;
import com.bobaoo.xiaobao.task.StringToBeanTask;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by you on 2015/6/17.
 */
public class EditNickNameActivity extends BaseActivity {
    private static int nicknameLength = 6;
    private EditText mNickNameEt;
    private ImageView mDeleteImg;
    private View mCommitBtn;

    private String nickName;
    private boolean isSubmit;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        nickName = intent.getStringExtra(UserPrivateInfoActivity.USER_NICKNAME);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_edit_nickname;
    }

    @Override
    protected void initTitle() {
        View headView = findViewById(R.id.layout_title);
        headView.setBackgroundColor(Color.TRANSPARENT);
        TextView backView = (TextView) findViewById(R.id.tv_back);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back_dark);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        backView.setCompoundDrawables(drawable, null, null, null);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.modify_nickname));
        titleView.setTextColor(getResources().getColor(R.color.gray10));
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mNickNameEt = (EditText) findViewById(R.id.et_nickname);
        mDeleteImg = (ImageView) findViewById(R.id.img_delete_nickname);
        mCommitBtn = findViewById(R.id.btn_edit_nickname_commit);

        setOnClickListener(mCommitBtn, mDeleteImg);

        if (!TextUtils.isEmpty(nickName)) {
            mNickNameEt.setText(nickName);
            CharSequence mNickNameEtText = mNickNameEt.getText();
            if (mNickNameEtText instanceof Spannable) {
                Spannable spanText = (Spannable) mNickNameEtText;
                Selection.setSelection(spanText, mNickNameEtText.length());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                if (isSubmit) {
                    finish();
                } else {
                    showPickDialog();
                }
                break;
            case R.id.btn_edit_nickname_commit:
                if (!UserInfoUtils.checkUserLogin(EditNickNameActivity.this)) {
                    Toast.makeText(this, R.string.not_login, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, UserLogInActivity.class);
                    startActivity(intent);
                    return;
                }
                String nickNameStr = mNickNameEt.getText().toString().trim();
                if (TextUtils.isEmpty(nickNameStr)) {
                    Toast.makeText(this, R.string.nickname_can_not_be_empty, Toast.LENGTH_SHORT).show();
                    mNickNameEt.setText("");
                    return;
                }
                if (nickNameStr.length() > nicknameLength) {
                    Toast.makeText(this, R.string.nickname_length_to_long, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    // 仅需对昵称做URL编码
                    nickNameStr = URLEncoder.encode(nickNameStr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //TODO 提交密码修改信息
                commitEditNicknameRequest(nickNameStr);
                break;
            case R.id.img_delete_nickname:
                mNickNameEt.setText("");
                break;
        }
    }

    private void commitEditNicknameRequest(String nickName) {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getCommitEditNicknameParams(mContext, nickName), new EditPasswordListener());
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

    private class EditPasswordListener extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<EditPassWdResp> {

        @Override
        public void onConvertSuccess(EditPassWdResp data) {
            if (data == null) {
                Toast.makeText(EditNickNameActivity.this, R.string.modify_nickname_fail, Toast.LENGTH_SHORT).show();
                return;
            }

            if (data.isError()) {
                Toast.makeText(EditNickNameActivity.this, R.string.nickname_length_to_long, Toast.LENGTH_SHORT).show();
            } else {
                isSubmit = true;
                Toast.makeText(EditNickNameActivity.this, R.string.modify_nickname_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onConvertFailed() {
            Toast.makeText(EditNickNameActivity.this, R.string.modify_nickname_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<EditPassWdResp> task = new StringToBeanTask<>(EditPassWdResp.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(EditNickNameActivity.this, R.string.modify_nickname_fail, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.cancel_modify_nickname).setNegativeButton(R.string.continue_modify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.confirm_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                finish();
            }
        }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSubmit) {
                finish();
            } else {
                showPickDialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
