package com.bobaoo.xiaobao.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.domain.IdentifyMeetPayResponse;
import com.bobaoo.xiaobao.domain.IdentifyMeetWXPayStatusResponse;
import com.bobaoo.xiaobao.ui.dialog.ChooseDialog;
import com.bobaoo.xiaobao.ui.dialog.ChooseIdentifyTypeDialog;
import com.bobaoo.xiaobao.ui.dialog.DebugDialog;
import com.bobaoo.xiaobao.ui.dialog.EditInfoDialog;
import com.bobaoo.xiaobao.ui.dialog.IdentifyMeetPaySuccessDialog;
import com.bobaoo.xiaobao.ui.dialog.PayAlertDialog;
import com.bobaoo.xiaobao.ui.dialog.ProgressDialog;
import com.bobaoo.xiaobao.ui.dialog.PromptAlertDialog;
import com.bobaoo.xiaobao.ui.dialog.RefundDetailDialog;
import com.bobaoo.xiaobao.ui.dialog.ResponsibilityDialog;
import com.bobaoo.xiaobao.ui.dialog.ScanCodeDialog;
import com.bobaoo.xiaobao.ui.dialog.SendMessageSuccessDialog;
import com.bobaoo.xiaobao.ui.fragment.UserIdentifyMeetingFragment;

public class DialogUtils {
    private static Toast mShortPromptToast;
    private static Toast mLongPromptToast;

    public static void showShortPromptToast(Context context, int resid) {
        if (mShortPromptToast == null) {
            mShortPromptToast = Toast.makeText(context, resid, Toast.LENGTH_SHORT);
        }
        mShortPromptToast.setText(resid);
        mShortPromptToast.show();
    }

    public static void showShortPromptToast(Context context, String res) {
        if (mShortPromptToast == null) {
            mShortPromptToast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        }
        mShortPromptToast.setText(res);
        mShortPromptToast.show();
    }

    public static void showLongPromptToast(Context context, String... res) {
        StringBuilder content = new StringBuilder();
        for (String string : res) {
            content.append(string);
        }
        if (mLongPromptToast == null) {
            mLongPromptToast = Toast.makeText(context, content.toString(), Toast.LENGTH_SHORT);
        }
        mLongPromptToast.setText(content.toString());
        mLongPromptToast.show();
    }

    public static void showLongPromptToast(Context context, int resid) {
        if (mLongPromptToast == null) {
            mLongPromptToast = Toast.makeText(context, resid, Toast.LENGTH_LONG);
        }
        mLongPromptToast.setText(resid);
        mLongPromptToast.show();
    }

    /**
     * 弹出显示进度的dialog
     */
    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出显示进度的dialog
     */
    public static ProgressDialog showProgressDialog(Context context, String title) {
        ProgressDialog dialog = new ProgressDialog(context, title);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出调试信息对话框
     */
    public static DebugDialog showDebugInfoDialog(Context context, String info) {
        DebugDialog dialog = new DebugDialog(context, info);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出调试信息对话框
     */
    public static DebugDialog showDebugInfoDialog(Context context) {
        DebugDialog dialog = new DebugDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出调试信息对话框
     */
    public static ChooseDialog showChooseDialog(Context context) {
        ChooseDialog dialog = new ChooseDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出免责声明对话框
     */
    public static ResponsibilityDialog showResponsibilityDialog(Context context, View.OnClickListener listener) {
        ResponsibilityDialog dialog = new ResponsibilityDialog(context, listener);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出扫描二维码对话框
     */
    public static ScanCodeDialog showScanCodeDialog(Context context) {
        ScanCodeDialog dialog = new ScanCodeDialog(context, R.style.CustomDialog);
        ImageView codeImage = (ImageView) dialog.findViewById(R.id.img_qr_code);
        TextView inviteCodeTv = (TextView) dialog.findViewById(R.id.tv_invite_code);
        String userId = UserInfoUtils.getUserId(context);
        inviteCodeTv.setText(userId);
        inviteCodeTv.setVisibility(TextUtils.isEmpty(userId) ? View.GONE : View.VISIBLE);
        codeImage.setImageResource(R.drawable.icon_scan_code);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出输入对话框
     */
    public static EditInfoDialog showEditDialog(Context context, EditInfoDialog.OnConfirmListener listener, int titleRes,
                                                int hintRes) {
        EditInfoDialog dialog = new EditInfoDialog(context, listener, titleRes, hintRes);
        dialog.show();
        return dialog;
    }

    @NonNull
    public static void showConfirmDialog(Context context, int strTitleResId, int strMsgResId, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitleResId).setMessage(strMsgResId)
                .setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, null).show();
    }

    @NonNull
    public static void showConfirmDialog(Context context, String strTitle, String strMsg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitle).setMessage(strMsg)
                .setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, null).show();
    }

    @NonNull
    public static void showConfirmDialog(Context context, int strTitleResId, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitleResId).setPositiveButton(R.string.positive, listener)
                .setNegativeButton(R.string.negative, null).show();
    }

    public static void showWarnDialog(Context context, String strTitle, String strMessage, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(strTitle).setMessage(strMessage).setNegativeButton(R.string.positive, listener).show();
    }

    public static void showItemsDialog(Context context, int resTitle, String[] mItemsList, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(resTitle);
        builder.setItems(mItemsList, listener).show();
    }

    public static ChooseIdentifyTypeDialog showItemsDialog(Context context, String[] mItemList, AdapterView.OnItemClickListener mListener) {
        ChooseIdentifyTypeDialog mItemsDialog = new ChooseIdentifyTypeDialog(context, mItemList, mListener);
        mItemsDialog.show();
        return mItemsDialog;
    }

    public static RefundDetailDialog showRefundDetailDialog(Context context, String content) {
        RefundDetailDialog refundDetailDialog = new RefundDetailDialog(context, content);
        refundDetailDialog.show();
        return refundDetailDialog;
    }

    /**
     * 弹出发送短信成功对话框
     */
    public static SendMessageSuccessDialog showSendMessageSuccessDialog(Context context, String mobile, String count) {
        SendMessageSuccessDialog dialog = new SendMessageSuccessDialog(context, mobile, count);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出鉴定会支付成功对话框
     */
    public static IdentifyMeetPaySuccessDialog showIdentifyMeetPaySuccessDialog(Context context, IdentifyMeetPayResponse mData, View.OnClickListener listener) {
        IdentifyMeetPaySuccessDialog dialog = new IdentifyMeetPaySuccessDialog(context, mData, listener);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出鉴定会支付成功对话框
     */
    public static IdentifyMeetPaySuccessDialog showIdentifyMeetPaySuccessDialog(Context context, IdentifyMeetWXPayStatusResponse mData, View.OnClickListener listener) {
        IdentifyMeetPaySuccessDialog dialog = new IdentifyMeetPaySuccessDialog(context, mData, listener);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出支付提示对话框
     */
    public static PayAlertDialog showPayAlertDialog(Context context, String title, String content,int pos, View.OnClickListener listener) {
        PayAlertDialog dialog = new PayAlertDialog(context, title, content,pos, listener);
        dialog.show();
        return dialog;
    }

    /**
     * 鉴定会没到时间弹窗，删除鉴定会支付订单弹窗，退出鉴定会包名页面弹窗
     * @param context
     * @param fragment
     * @param pos
     * @param flag
     * @param mIsIdentifyMeet
     * @return
     */
    public static PromptAlertDialog showPromtAlertDialog(Context context, UserIdentifyMeetingFragment fragment, int pos,int
            flag,boolean mIsIdentifyMeet){
        PromptAlertDialog dialog = new PromptAlertDialog(context,fragment,pos,flag,mIsIdentifyMeet);
        dialog.show();
        return dialog;
    }
}
