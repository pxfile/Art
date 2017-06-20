package com.bobaoo.xiaobao.ui.dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.EventEnum;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;

import java.io.File;

/**
 * Created by Ameng on 2016/3/16.
 */
public class UpdateAlertDialog implements View.OnClickListener {
    private Context mContext;
    private android.app.AlertDialog ad;
    private TextView mNewVersion;
    private TextView mMessageView;
    private Button mBtnOk,mBtnNo;
    private UpdateResponse mUpdateInfo;

    public UpdateAlertDialog(Context context,UpdateResponse updateInfo) {
        this.mContext =context;
        this.mUpdateInfo = updateInfo;
        ad=new android.app.AlertDialog.Builder(context).create();
        ad.setCancelable(false);
        ad.show();
        //关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = ad.getWindow();
        window.setContentView(R.layout.umeng_update_dialog);
        mNewVersion =(TextView)window.findViewById(R.id.new_version);
        mMessageView =(TextView)window.findViewById(R.id.umeng_update_content);
        mBtnOk = (Button) window.findViewById(R.id.umeng_update_id_ok);
        mBtnNo = (Button) window.findViewById(R.id.umeng_update_id_cancel);
        mBtnOk.setOnClickListener(this);
        mBtnNo.setOnClickListener(this);
    }

    public void setMessage(String newVersoin,String content)
    {
        mNewVersion.setText(newVersoin);
        mMessageView.setText(content);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.umeng_update_id_ok:
                File file = UmengUpdateAgent.downloadedFile(mContext, mUpdateInfo);
                if (file == null) {
                    UmengUpdateAgent.startDownload(mContext, mUpdateInfo);
                } else {
                    UmengUpdateAgent.startInstall(mContext, file);
                }

                UmengUtils.onEvent(mContext, EventEnum.UserUmengUpdateOK);
                dismiss();
                break;
            case R.id.umeng_update_id_cancel:
                UmengUpdateAgent.ignoreUpdate(mContext, mUpdateInfo);
                UmengUtils.onEvent(mContext, EventEnum.UserUmengUpdateNo);
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
