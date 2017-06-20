package com.bobaoo.xiaobao.application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bobaoo.xiaobao.listener.XGPushCallback;
import com.bobaoo.xiaobao.receiver.TimerStickReceiver;
import com.bobaoo.xiaobao.utils.DeviceUtil;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UmengUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.socialize.PlatformConfig;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by you on 2015/5/27.
 */
public class IdentifyApplication extends Application {
    private static final String TAG = IdentifyApplication.class.getSimpleName();
    private static HashMap<String, Object> hashMap;
    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化推送
        XGPushConfig.enableDebug(this, DeviceUtil.isApkDebugable(this));
        XGPushConfig.setInstallChannel(this, StringUtils.getMetaData(this, "UMENG_CHANNEL"));
        XGPushManager.registerPush(this, getPushName(this), new XGPushCallback(this, TAG));
        // 初始化控件
        Fresco.initialize(this);
        UmengUtils.init(this);
        // 初始化传值HashMap
        hashMap = new HashMap<>();
        startPushTimerAlarm();
    }

    /**
     * 定时弹通知
     */
    private void startPushTimerAlarm() {
        Intent intent = new Intent(this, TimerStickReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // 创建时间对象
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, sender);
    }

    public static void setIntentData(String key, Object value) {
        hashMap.put(key, value);
    }

    public static Object getIntentData(String key) {
        return hashMap.get(key);
    }

    public static void removeIntentData(String key) {
        hashMap.remove(key);
    }

    public static String getPushName(Context context) {
        if (DeviceUtil.isApkDebugable(context)) {
            return "Test";
        } else {
            return StringUtils.getString("JQ", UserInfoUtils.getUserId(context));
        }
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信
        String appWeixinId = "wxfe76631ca3c86dbc";
        String appWeixinSecret = "ee4c7f9329ab613d06b49e5b90bd4278";
        PlatformConfig.setWeixin(appWeixinId, appWeixinSecret);
        //新浪微博
        String appSinaWeiboId = "2505269697";
        String appSinaWeiboSecret = "fb907fbb9dd99977d11656fbe170b44c";
        PlatformConfig.setSinaWeibo(appSinaWeiboId, appSinaWeiboSecret);
        //QQ、QZone、TencentWeibo
        String appQQId = "1104233438";
        String appQQSecret = "hKLkSLoIlZMvddbZ";
        PlatformConfig.setQQZone(appQQId, appQQSecret);
    }
}
