package com.bobaoo.xiaobao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huyongsheng on 2014/7/18.
 */
public class StringUtils {

    private static final String LOG_TAG = StringUtils.class.getSimpleName();

    /**
     * 最优化String的构建
     */
    public static final String getString(Object... objects) {
        StringBuffer buffer = new StringBuffer();
        for (Object object : objects) {
            buffer.append(object);
        }
        return buffer.toString();
    }

    /**
     * 生成base64编码
     */
    public static final String encodeBase64(String string) {
        return Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);
    }

    /**
     * 对double数据进行截断
     */
    public static final String cutDouble2(double value) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(value);
    }

    /**
     * 对double数据进行截断
     */
    public static final String cutFloat2(float value) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(value);
    }

    /**
     * base64解码
     */
    public static final String decodeBase64(String string) {
        String result = null;
        if (!StringUtils.isNullOrEmpty(string)) {
            try {
                result = new String(Base64.decode(string, Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                LogForTest.logW(LOG_TAG, "解析base64出错 \n");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                LogForTest.logW(LOG_TAG, "解析base64出错 \n");
                e.printStackTrace();
            }
        }
        return result;
    }

    public static final boolean isNullOrEmpty(String inputString) {
        if (null == inputString) {
            return true;
        } else {
            return inputString.trim().equals("");
        }
    }

    public static final boolean isNullOrEmpty(byte[] bytes) {
        if (null == bytes) {
            return true;
        } else {
            return bytes.length == 0;
        }
    }

    /**
     * 生成google play连接地址
     */
    public static final String getGooglePlayString(Activity activity, String packageName) {
        return getGooglePlayString(packageName, "flip", activity.getPackageName());
    }

    /**
     * 生成google play连接地址
     */
    public static final String getGooglePlayString(String packageName, String source, String medium) {
        return StringUtils.getString("market://details?id=", packageName, "&referrer=", "utm_source%3D", source,
                "%26utm_medium%3D", medium);
    }


    /**
     * 得到配置文件中的MetaData数据
     */
    public static String getMetaData(Context context, String keyName) {
        try {
            ApplicationInfo appi = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appi.metaData;
            Object value = bundle.get(keyName);
            return value.toString();
        } catch (Exception e) {
            LogForTest.logE(LOG_TAG, "can not get metaData:" + keyName);
            return "";
        }
    }

    /**
     * 获取package信息
     */
    public static final PackageInfo getPackageInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            LogForTest.logE(LOG_TAG, "Could not get package info.");
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 对double数据进行截断
     */
    public static final String cutDouble0(double value) {
        DecimalFormat fnum = new DecimalFormat("##0");
        return fnum.format(value);
    }

    /**
     * 对double数据进行截断
     */
    public static final String cutFloat0(float value) {
        DecimalFormat fnum = new DecimalFormat("##0");
        return fnum.format(value);
    }


    /**
     * 获取post请求中的参数
     */
    public static final String getPostParams(String preString, Object object) {
        String result = getString(preString, "{");
        boolean isFirst = true;
        // 获取object对象对应类中的所有属性域
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 对于每个属性，获取属性名
            String varName = field.getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                // 修改访问控制权限
                field.setAccessible(true);
                // 获取在对象object中属性field对应的对象中的变量
                Object value = field.get(object);
                // 生成参数,其实跟get的URL中'?'后的参数字符串一致
                if (isFirst) {
                    if (value instanceof String) {
                        result += getString("\"", URLEncoder.encode(varName, "utf-8"), "\":\"",
                                URLEncoder.encode(String.valueOf(value), "utf-8"), "\"");
                    } else {
                        result += getString("\"", URLEncoder.encode(varName, "utf-8"), "\":",
                                URLEncoder.encode(String.valueOf(value), "utf-8"));
                    }
                    isFirst = false;
                } else {
                    if (value instanceof String) {
                        result += getString(",\"", URLEncoder.encode(varName, "utf-8"), "\":\"",
                                URLEncoder.encode(String.valueOf(value), "utf-8"), "\"");
                    } else {
                        result += getString(",\"", URLEncoder.encode(varName, "utf-8"), "\":",
                                URLEncoder.encode(String.valueOf(value), "utf-8"));
                    }
                }
                // 恢复访问控制权限
                field.setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        result += "}";
        return result;
    }

    /**
     * 获取post请求中的参数
     */
    public static String getSimplePostParams(Object object) {
        String result = "";
        boolean isFirst = true;
        // 获取object对象对应类中的所有属性域
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 对于每个属性，获取属性名
            String varName = field.getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                // 修改访问控制权限
                field.setAccessible(true);
                // 获取在对象object中属性field对应的对象中的变量
                Object value = field.get(object);
                // 生成参数,其实跟get的URL中'?'后的参数字符串一致
                if (value != null) {
                    if (isFirst) {
                        result += getString(URLEncoder.encode(varName, "utf-8"), "=",
                                URLEncoder.encode(String.valueOf(value), "utf-8"));
                        isFirst = false;
                    } else {
                        result += getString("&", URLEncoder.encode(varName, "utf-8"), "=",
                                URLEncoder.encode(String.valueOf(value), "utf-8"));
                    }
                }
                // 恢复访问控制权限
                field.setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 使用sha加密
     */
    public static String getSHA(String val) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return getString(m);
    }

    /**
     * 手机号合法性校验
     */
    public static boolean checkPhoneNumber(String value) {
        String regExp = "^1\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        return m.find();
    }

    /**
     * @param htmlStr
     * @return
     *  删除Html标签
     */
    public static String trimHTMLTag(String htmlStr) {

        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

}
