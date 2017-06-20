package com.bobaoo.xiaobao.utils;

/**
 * Created by chenming on 2015/5/22.
 */

import android.content.Context;
import android.text.TextUtils;

import com.bobaoo.xiaobao.constant.AppConstant;
import com.bobaoo.xiaobao.domain.LoginStepTwoResponse;
import com.bobaoo.xiaobao.domain.UserLoginInfo;


public class UserInfoUtils {
    //鉴宝登陆或者注册成功后的信息
    private static final String SP_KEY_USER_ID = "user_id";
    private static final String SP_KEY_USER_NICKNAME = "nickname";
    private static final String SP_KEY_USER_TOKEN = "token";
    private static final String SP_KEY_USER_HEAD_URL = "head";
    private static final String SP_KEY_USER_PHONE = "phone";
    private static final String SP_KEY_SOCIAL_LOGIN_FLG = "is_social_flg";
    private static final String SP_KEY_USER_CACHE_HEAD_URL = "head_cache";//用于缓存重置头像后的新URL

    public static void saveUserLoginInfo(Context context, LoginStepTwoResponse.DataEntity info) {
        if (info != null) {
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_ID, String.valueOf(info.getUser_id()));
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_NICKNAME, info.getUser_name());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, info.getToken());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HEAD_URL, info.getHeadimg());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, info.getMobile());
        }
    }

    public static void saveUserLoginInfo(Context context, UserLoginInfo info) {
        if (info != null) {
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_ID, info.getId());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_NICKNAME, info.getNickName());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, info.getToken());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HEAD_URL, info.getPortraitUrl());
        }
    }

    public static boolean checkUserLogin(Context context) {
        String token = getUserToken(context);
        return !TextUtils.isEmpty(token);
    }

    public static void saveNickName(Context context, String nickName) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_NICKNAME, nickName);
    }

    public static void setSocialLoginFlg(Context context, boolean isSocialLogin) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_SOCIAL_LOGIN_FLG, isSocialLogin);
    }

    public static boolean getSocialLoginFlg(Context context) {
        return SharedPreferencesUtils.getSharedPreferences(context, SP_KEY_SOCIAL_LOGIN_FLG);
    }

    public static void setPhone(Context context, String phone) {
        if (!TextUtils.isEmpty(phone)){
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, phone);
        }
    }

    public static String getPhone(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_PHONE);
    }

    public static void saveCacheHeadImagePath(Context context, String path) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_CACHE_HEAD_URL, path);
    }

    public static String getCacheHeadImagePath(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_CACHE_HEAD_URL);
    }


    public static String getUserId(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_ID);
    }

    public static String getUserNickName(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_NICKNAME);
    }

    public static String getUserToken(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_TOKEN);
    }

    public static String getUserHeadImage(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_HEAD_URL);
    }

    public static void clearToken(Context context) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, "");
    }

    public static void clearCacheImageUrl(Context context) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_CACHE_HEAD_URL, "");
    }

    public static void logOut(Context context) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_ID, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_NICKNAME, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HEAD_URL, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, "");
        SharedPreferencesUtils.setSharedPreferencesBoolean(context, AppConstant.SP_KEY_PHONE_CHECKED, false);
        setSocialLoginFlg(context, false);
        clearCacheImageUrl(context);
    }
}
