package com.bobaoo.xiaobao.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobaoo.xiaobao.R;

/**
 * Created by Ameng on 2016/3/16.
 */
public class TimeRangeAlertDialog implements View.OnClickListener {
    private Context mContext;
    private android.app.AlertDialog ad;
    private String[] mTimeArr;
    private Handler mHandler;

    public TimeRangeAlertDialog(Context context,String[] timeArr,Handler mHandler) {
        this.mContext =context;
        this.mTimeArr = timeArr;
        this.mHandler = mHandler;
        ad=new android.app.AlertDialog.Builder(context).create();
        ad.show();
        LinearLayout llAddTime = (LinearLayout) View.inflate(mContext,R.layout.time_range_dialog,null);
        for (int i = 0; i < mTimeArr.length; i++) {
            LinearLayout llAddTimeItem = (LinearLayout) View.inflate(mContext, R.layout.time_range_dialog_item, null);
            llAddTimeItem.setTag(i);
            TextView tvTime = (TextView) llAddTimeItem.findViewById(R.id.tv_time);
            tvTime.setText(mTimeArr[i]);
            llAddTimeItem.setOnClickListener(this);
            llAddTime.addView(llAddTimeItem);
        }
        //关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
//        ad.setView(llAddTime);
//        ad.setInverseBackgroundForced(true);
//        ad.setContentView(R.layout);
        Window window = ad.getWindow();
        window.setContentView(llAddTime);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();

    }

    @Override
    public void onClick(View v) {
//        int i = (Integer)v.getTag();
//        AppConstant.TIME_RANGE = mTimeArr[i];
////        mTvTimeRange.setText(mTimeArr[i]);
        Message msg = Message.obtain();
        msg.obj = mTimeArr[(Integer)v.getTag()];
        mHandler.sendMessage(msg);
        dismiss();
    }
}
