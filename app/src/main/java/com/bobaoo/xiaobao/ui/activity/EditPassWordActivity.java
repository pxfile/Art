package com.bobaoo.xiaobao.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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

/**
 * Created by you on 2015/6/17.
 */
public class EditPassWordActivity extends BaseActivity {
    private EditText mOldPaswdEt;
    private EditText mNewPaswdEt1;
    private EditText mNewPaswdEt2;
    private View mCommitBtn;

    private boolean isSubmit;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_edit_password;
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
        titleView.setText(getString(R.string.modify_password));
        titleView.setTextColor(getResources().getColor(R.color.gray5));
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mOldPaswdEt = (EditText) findViewById(R.id.et_old_password);
        mNewPaswdEt1 = (EditText) findViewById(R.id.et_new_password1);
        mNewPaswdEt2 = (EditText) findViewById(R.id.et_new_password2);
        mCommitBtn = findViewById(R.id.btn_edit_paswd_commit);


        setOnClickListener(mCommitBtn);
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
            case R.id.btn_edit_paswd_commit:
                if (!UserInfoUtils.checkUserLogin(EditPassWordActivity.this)) {
                    Toast.makeText(this, "您还未登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, UserLogInActivity.class);
                    startActivity(intent);
                    return;
                }
                String oldPswd = mOldPaswdEt.getText().toString().trim();
                String newPswd1 = mNewPaswdEt1.getText().toString().trim();
                String newPswd2 = mNewPaswdEt2.getText().toString().trim();
                if (TextUtils.isEmpty(oldPswd)) {
                    Toast.makeText(this, "请填写原密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPswd1.length() < 6 || newPswd1.length() > 20) {
                    Toast.makeText(this, "新密码长度长度需要6-20之间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPswd2) || !newPswd2.equals(newPswd1)) {
                    Toast.makeText(this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //TODO 提交密码修改信息
                commitEditPasswordRequest(oldPswd, newPswd1);
                break;
        }
    }

    private void commitEditPasswordRequest(String oldPwd, String newPwd) {
        new HttpUtils().configCurrentHttpCacheExpiry(0).send(HttpRequest.HttpMethod.GET, NetConstant.HOST, NetConstant.getCommitEditPasswordParams(mContext, oldPwd, newPwd), new EditPasswordListener());
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
                Toast.makeText(EditPassWordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                return;
            }

            if (data.isError()) {
                Toast.makeText(EditPassWordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
            } else {
                isSubmit = true;
                Toast.makeText(EditPassWordActivity.this, "修改密码成功!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onConvertFailed() {
            Toast.makeText(EditPassWordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<EditPassWdResp> task = new StringToBeanTask<>(EditPassWdResp.class, this);
            task.execute(responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(EditPassWordActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.cancel_modify_password).setNegativeButton(R.string.continue_modify, new DialogInterface.OnClickListener() {
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
