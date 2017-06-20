package com.bobaoo.xiaobao.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bobaoo.xiaobao.R;
import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.constant.IntentConstant;
import com.bobaoo.xiaobao.ui.activity.SplashActivity;
import com.bobaoo.xiaobao.utils.NotificationUtils;
import com.bobaoo.xiaobao.utils.SharedPreferencesUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;

/**
 * Created by kakaxicm on 2015/11/5.
 */
public class TimerStickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (SharedPreferencesUtils.getSharedPreferencesBoolean(context, AppConstant.HAS_NOCHARGE_IDENTIFY)
                && UserInfoUtils.checkUserLogin(context)){
            intent = new Intent(context, SplashActivity.class);
            intent.putExtra(IntentConstant.IS_FROM_NOTIFICATION, true);
            intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
            NotificationUtils.sendNotification(context, context.getString(R.string.app_name), context.getString(R.string.no_charge_tip), intent);
        }
    }
}
