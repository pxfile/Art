package com.bobaoo.xiaobao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


/**
 * Created by huyongsheng on 2015/3/18.
 * <p/>
 * 用于处理网络相关
 * <p/>
 * 已添加get请求相关
 */
public class NetUtils {

    /**
     * 判断网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断用户是否为移动网络
     *
     * @param context
     * @return
     */
    public static boolean isMobileNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否使用WiFi
     *
     * @param context
     * @return
     */
    public static boolean isWifiNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 返回运营商的名字
     *
     * @param context
     * @return "中国移动" "中国联通" "中国电信"
     */
    public static String getOperatorName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mOperatorName = telephonyManager.getNetworkOperatorName();
        return mOperatorName;
    }

    /**
     * 判断用户是否使用的为2G网络
     * GPRS | EDGE 中国移动和中国联通2G ，CDMA 中国电信2G
     *
     * @param context
     * @return
     */
    public static boolean isSecGenerationNet(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null && isMobileNet(context) && (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA)) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否使用3G网络
     * UMTS/HSDPA 中国联通3G，EVDO 中国电信3G
     *
     * @param context
     * @return
     */
    public static boolean isThirdGenerationNet(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null && isMobileNet(context) && (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EVDO_0)) {
            return true;
        }
        return false;
    }

    /**
     *
     * 获取用户当前网络状态
     * @param context
     * @return
     */
    public static String getNetworkState(Context context){

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!isNetworkConnected(context)){
            return StringUtils.getString("无网络");
        }else if (isWifiNet(context)){
            return StringUtils.getString("wifi");
        }else if (isMobileNet(context)){
            switch (telephonyManager.getNetworkType()){
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return StringUtils.getString("2G","100 kbps");
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return StringUtils.getString("50-100 kbps");
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return StringUtils.getString("100 kbps");
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return StringUtils.getString("3G","100 kbps");
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return StringUtils.getString("2G","14-64 kbps");
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return StringUtils.getString("400-1000 kbps");
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return StringUtils.getString("600-1400 kbps");
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return StringUtils.getString("3G","2-14 Mbps");
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return StringUtils.getString("1-23 Mbps");
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return StringUtils.getString("25 kbps ");
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return StringUtils.getString(" 10+ Mbps");
            }
        }
        return StringUtils.getString("Unknown");
    }
}