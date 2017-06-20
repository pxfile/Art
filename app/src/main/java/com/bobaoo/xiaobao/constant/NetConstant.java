package com.bobaoo.xiaobao.constant;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bobaoo.xiaobao.domain.LoginStepOneResponse;
import com.bobaoo.xiaobao.utils.AppUtils;
import com.bobaoo.xiaobao.utils.DeviceUtil;
import com.bobaoo.xiaobao.utils.StringUtils;
import com.bobaoo.xiaobao.utils.UserInfoUtils;
import com.lidroid.xutils.http.RequestParams;

import java.io.File;
import java.util.List;

/**
 * Created by star on 15/6/4.
 */
public class NetConstant {

    public static final String AUTH_CODE = "http://user.artxun.com/mobile/user/captcha.jsp";
    public static final String LOGIN = "http://user.artxun.com/mobile/user/login.jsp";
    public static final String HOST = "http://jianbao.artxun.com/index.php";
    public static final String ALIPAY_HOST = "http://user.artxun.com/mobile/finance/malipay/charge.jsp";
    public static final String ENROLL_MEET = "http://jianbao.artxun.com/index.php?module=jbapp&act=api&api=teyao&op=index&from=app";
    public static final String UPDATE_HOST = "http://artist.app.artxun.com/recomment/op_version.jsp?app=xiaobao";
    public static final String SCORE_RULES_HOST = "http://jianbao.artxun.com/index.php?module=jbapp&act=jb&api=h5&op=rules";
    public static final String IDENTIFY_TIP = "http://jianbao.artxun.com/index.php?module=jbapp&act=jb&api=h5&op=notice";
    public static final String IDENTIFY_FAQ = "http://jianbao.artxun.com/index.php?module=jbapp&act=jb&api=h5&op=faq";
    /**
     * 获取我的询价数据
     */
    public static RequestParams getUserPriceQueryParams(Context context, int type, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "message");
        params.addQueryStringParameter("op", "asklist");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", StringUtils.getString(page));
        params.addQueryStringParameter("type", StringUtils.getString(type));
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取我的收藏数据
     */
    public static RequestParams getUserCommentParams(Context context, int type, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "comment");
        params.addQueryStringParameter("op", "get");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", StringUtils.getString(page));
        params.addQueryStringParameter("type", StringUtils.getString(type));
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取我的评论数据
     */
    public static RequestParams getUserCollectionParams(Context context, int type, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "collect");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", StringUtils.getString(page));
        params.addQueryStringParameter("type", StringUtils.getString(type));
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 评论,分享获取积分请求
     */
    public static RequestParams getScoreParams(Context context, String operation, String type, String id) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "api");
        params.addQueryStringParameter("api", "integral");
        params.addQueryStringParameter("op", operation);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 充值获取积分
     */
    public static RequestParams getRechargeScoreParams(Context context, String amount) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "api");
        params.addQueryStringParameter("api", "integral");
        params.addQueryStringParameter("op", "send");
        params.addQueryStringParameter("amount", amount);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 删除我的数据
     */
    public static RequestParams getUserDeleteParams(Context context, int type, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "delete");
        params.addQueryStringParameter("id", StringUtils.getString(id));
        params.addQueryStringParameter("type", StringUtils.getString(type));
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取登陆数据
     */
    public static RequestParams getLoginParams(Context context, String name, String password) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("username", name);
        params.addQueryStringParameter("app", context.getPackageName());
        params.addQueryStringParameter("password", password);
        return params;
    }

    /**
     * 获取登陆数据
     */
    public static RequestParams getLoginStepTwoParams(Context context, LoginStepOneResponse.DataEntity data) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("app", context.getPackageName());
        params.addQueryStringParameter("op", "login");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("act", "api");
        params.addQueryStringParameter("user_id", String.valueOf(data.getUid()));
        params.addQueryStringParameter("user_name", data.getUsername());
        params.addQueryStringParameter("token", data.getToken());
        params.addQueryStringParameter("uuid", DeviceUtil.getUUID(context));
        return params;
    }


    /**
     * 获取验证码的参数
     */
    public static RequestParams getAuthCodeParams(Context context, String tel) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("mobile", tel);
        return params;
    }

    /**
     * 提交订单和修改信息时校验手机获取验证码的参数
     */
    public static RequestParams getCheckPhoneAuthCodeParams(Context context, String tel) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "AuthPhone");
        params.addQueryStringParameter("mobile", tel);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 校验手机号参数
     */
    public static RequestParams getCheckPhoneAuthCodeParams(Context context, String tel, String code) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "AuthPhone");
        params.addQueryStringParameter("save", "1");
        params.addQueryStringParameter("mobile", tel);
        params.addQueryStringParameter("code", code);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取提交订单数据
     */
    public static RequestParams getCommitOrderParams(Context context, int kind, int type, String desc, String tel, String expertId,
                                                     List<String> pictures) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "upload");
        params.addQueryStringParameter("op", "index");
        params.addBodyParameter("kind", String.valueOf(kind));
        params.addBodyParameter("jb_type", String.valueOf(type));
        params.addBodyParameter("remark", desc);
        params.addBodyParameter("tel", tel);
        params.addBodyParameter("specify_expert_id", expertId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        if (pictures != null) {
            for (int i = 0; i < pictures.size(); i++) {
                params.addBodyParameter(StringUtils.getString("file", i), new File(pictures.get(i)), "application/octet-stream");
            }
        }
        params.addHeader("Content-Type", "multipart/form-data;charset=UTF-8");
        return params;
    }

    /**
     * 获取提交订单数据
     */
    public static RequestParams getCommitModifyIndentifyParams(
            Context context, String orderId, String kind, String ispublic,
            String remark, int jb_type, String tel, String expertId, String imgStr, String index, List<String> pictures) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "upload");
        params.addBodyParameter("id", orderId);
        params.addBodyParameter("kind", kind);
        params.addBodyParameter("public", ispublic);
        params.addBodyParameter("remark", remark);
        params.addBodyParameter("jb_type", String.valueOf(jb_type));
        params.addBodyParameter("tel", tel);
        params.addBodyParameter("specify_expert_id", expertId);
        params.addBodyParameter("original", imgStr);
        params.addBodyParameter("index", index);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        if (pictures != null) {
            for (int i = 0; i < pictures.size(); i++) {
                params.addBodyParameter(StringUtils.getString("file", i), new File(pictures.get(i)), "application/octet-stream");
            }
        }
        params.addHeader("Content-Type", "multipart/form-data;charset=UTF-8");
        return params;
    }

    /**
     * 直接评论请求
     */
    public static RequestParams getCommentParams(Context context, String id, String type, String content) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "comment");
        params.addQueryStringParameter("op", "index");
        params.addBodyParameter("oid", id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("content", content);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 回复评论请求
     */
    public static RequestParams getReplyCommentParams(Context context, String id, String type, String content, String replyId) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "comment");
        params.addQueryStringParameter("op", "index");
        params.addBodyParameter("content", content);
        params.addBodyParameter("oid", id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("reply", replyId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     *对专家进行评价
     */
    public static RequestParams getEvaluateParams(Context context, String id, String type, String content,int score,String orderId) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "comment");
        params.addQueryStringParameter("op", "index");
        params.addBodyParameter("oid", id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("content", content);
        params.addBodyParameter("stars", String.valueOf(score));
        params.addBodyParameter("from_id", orderId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    public static RequestParams getOrderCollectParams(Context context, String ordId) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "goods");
        params.addQueryStringParameter("op", "collect");
        params.addQueryStringParameter("id", ordId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    public static RequestParams getInfoCollectParams(Context context, String newsId) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "news");
        params.addQueryStringParameter("op", "collect");
        params.addQueryStringParameter("id", newsId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    public static RequestParams getExpertAttentionParams(Context context, String expertId) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "expert");
        params.addQueryStringParameter("op", "fans");
        params.addQueryStringParameter("id", expertId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取订单详情数据
     */
    public static RequestParams getOrderDetailParams(Context context,String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "goods");
        params.addQueryStringParameter("op", "sun");
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("key", "show");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取咨询详情数据
     */
    public static RequestParams getInfoDetailParams(Context context, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "news");
        params.addQueryStringParameter("op", "detail");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取支付宝充值订单数据
     */
    public static RequestParams getAliRechargeInfoParams(Context context, String userId, String amount) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("app", context.getPackageName());
        params.addQueryStringParameter("uid", userId);
        params.addQueryStringParameter("amount", amount);
        params.addQueryStringParameter("gateway", "malipay");
        return params;
    }

    /**
     * 获取专家详情数据
     */
    public static RequestParams getExpertDetailParams(Context context, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "expert");
        params.addQueryStringParameter("op", "detail");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取banner列表的参数
     */
    public static RequestParams getBannerListParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "index");
        params.addQueryStringParameter("op", "slider");
        return params;
    }

    /**
     * 获取订单列表的参数
     */
    public static RequestParams getOrderListParams(Context context, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "index");
        params.addQueryStringParameter("op", "goods");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", StringUtils.getString(page));
        return params;
    }

    /**
     * 获取机构专家列表的参数
     */
    public static RequestParams getOrganizationExpertListParams(Context context, int page) {
        RequestParams params = getExpertListParams(context, page);
        params.addQueryStringParameter("org", "1,2,3,4,5,6,7,8");
        Log.e("ExpertPage", page + "");
        return params;
    }

    /**
     * 获取机构专家列表的参数
     */
    public static RequestParams getOrganizationExpertListParams(Context context, int page, int organization) {
        RequestParams params = getExpertListParams(context, page);
        params.addQueryStringParameter("org", String.valueOf(organization));
        return params;
    }

    /**
     * 获取在线专家列表的参数
     */
    public static RequestParams getOnlineExpertListParams(Context context, int page) {
        RequestParams params = getExpertListParams(context, page);
        params.addQueryStringParameter("org", "0");
        //机构专家+在线专家
//        params.addQueryStringParameter("org", "0,1,2,3,4,5,6,7,8");
        return params;
    }

    /**
     * 获取咨询列表的参数
     */
    public static RequestParams getInfoListParams(Context context, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "news");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", String.valueOf(page));
        return params;
    }

    /**
     * 获取咨询列表的参数
     */
    public static RequestParams getInfoBannerParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "news");
        params.addQueryStringParameter("op", "slider");
        return params;
    }

    /**
     * 获取专家列表的参数
     */
    private static RequestParams getExpertListParams(Context context, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "expert");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", String.valueOf(page));
        return params;
    }

    /**
     * 获取关注专家列表的参数
     */
    public static RequestParams getAttentionExpertListParams(Context context, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "fans");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取修改订单的数据参数
     */
    public static RequestParams getModifyIdnetifyParams(Context context, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "goods");
        params.addQueryStringParameter("op", "sun");
        params.addQueryStringParameter("type", "goods");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取发送邀请码请求的参数
     */
    public static RequestParams getInvitedCodeParams(Context context, String code) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "api");
        params.addQueryStringParameter("api", "integral");
        params.addQueryStringParameter("op", "invite");
        params.addQueryStringParameter("code", code);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取是否已经校验手机号的参数
     */
    public static RequestParams getIsPhoneNumberCheckedParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "PhoneIsAuth");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取基本的参数
     */
    private static RequestParams getBaseParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        return params;
    }

    /**
     * 获得带基本版本信息和设备信息的请求参数
     */
    private static RequestParams getBaseParamsWithHeader(Context context) {
        RequestParams params = new RequestParams();
        params.addHeader("app-version-code", String.valueOf(AppUtils.getVersionCode(context)));
        params.addHeader("device-model", DeviceUtil.getPhoneModel());
        return params;
    }



    /**
     * 获取修改密码参数
     */
    public static RequestParams getCommitEditPasswordParams(Context context, String oldPwd, String newPwd) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "ChangePassword");
        params.addQueryStringParameter("oldpwd", oldPwd);
        params.addQueryStringParameter("newpwd", newPwd);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取询价参数
     */
    public static RequestParams getPriceQueryContentParams(Context context, String mFrom, String mTo, String mGoodsId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "message");
        params.addQueryStringParameter("op", "askspeak");
        params.addQueryStringParameter("from", mFrom);
        params.addQueryStringParameter("to", mTo);
        params.addQueryStringParameter("gid", mGoodsId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取发送用户消息参数
     */
    public static RequestParams getSendUserMsgParams(Context context, String content, String mTo, String mGoodsId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "message");
        params.addQueryStringParameter("op", "askgoods");
        params.addQueryStringParameter("to", mTo);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("gid", mGoodsId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取查询用户消费记录参数
     */
    public static RequestParams getUserConsumeRecordParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "log");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户反馈参数
     */
    public static RequestParams getUserFeedBackParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "feedback_get");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获得用户发送消息参数
     */
    public static RequestParams getSendUserMsgParams(Context context, String content) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "feedback");
        params.addQueryStringParameter("type", "0");
        params.addQueryStringParameter("model", DeviceUtil.getPhoneModel());
        params.addQueryStringParameter("android_version", DeviceUtil.getAndroidOSVersion());
        params.addQueryStringParameter("app_version", StringUtils.getString("xiaobao", AppUtils.getAppVersionName(context)));
        params.addQueryStringParameter("package", context.getPackageName());
        params.addQueryStringParameter("content", content);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户支付参数
     */
    public static RequestParams getUserPayParams(Context context, String mIdentifyId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "goods");
        params.addQueryStringParameter("op", "sun");
        params.addQueryStringParameter("type", "pay1");
        params.addQueryStringParameter("id", mIdentifyId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户支付参数
     */
    public static RequestParams getIdentifyMeetPayInfoParams(Context context, String mIdentifyMeetSignId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "teyao");
        params.addQueryStringParameter("op", "order");
        params.addQueryStringParameter("id", mIdentifyMeetSignId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    public static RequestParams getQureyPaySatusParams(Context context, String goodsId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "goods");
        params.addQueryStringParameter("op", "wxorder");
        params.addQueryStringParameter("id", goodsId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取支付参数
     */
    public static RequestParams getPayParams(Context context, String goodsId, String payMethodFlg, String coupon) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("act", "api");
        params.addQueryStringParameter("api", "pay");
        params.addQueryStringParameter("op", "goods");
        params.addQueryStringParameter("id", goodsId);
        params.addQueryStringParameter("jflag", payMethodFlg);
        params.addQueryStringParameter("rid", coupon);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 鉴定会提交支付参数
     */
    public static RequestParams getIdentifyMeetPayParams(Context context, String meetsignid, String rid) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "teyao");
        params.addQueryStringParameter("op", "pay");
        params.addQueryStringParameter("id", meetsignid);
        if(!TextUtils.isEmpty(rid)){
            params.addQueryStringParameter("rid", rid);
        }
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    //鉴定会支付时的查询支付结果接口
    public static RequestParams getIdentifyMeetPayResultParams(Context context, String identifyMeetId){
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "teyao");
        params.addQueryStringParameter("op", "info");
        params.addQueryStringParameter("id", identifyMeetId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获得用户个人信息参数
     */
    public static RequestParams getUserPrivateInfoParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "info");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取提交用户信息参数
     */
    public static RequestParams getCommitUserPrivateInfoParams(Context context, String mobile, String nickName) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "edit");
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("name", nickName);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取修改用户头像参数
     */
    public static RequestParams getUpLoadHeadImgParams(Context context, File headImg) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "head");
        params.addBodyParameter("FILES", headImg);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        params.addHeader("Content-Type", "multipart/form-data;charset=UTF-8");
        return params;
    }

    /**
     * 获取用户充值记录参数
     */
    public static RequestParams getUserRechargeRecordParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "logs");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户积分参数
     */
    public static RequestParams getUserScoreRecordParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "integral");
        params.addQueryStringParameter("from", "ios");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户信息参数
     */
    public static RequestParams getUserInfoParams(Context context, String versionName, String channelName, String model) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "index");
        params.addQueryStringParameter("appname", versionName);
        params.addQueryStringParameter("channel", channelName);
        params.addQueryStringParameter("model", model);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户鉴定参数
     */
    public static RequestParams getUserIdentifyParams(Context context, String mType) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "goods");
        params.addQueryStringParameter("sp", String.valueOf(10));
        params.addQueryStringParameter("type", mType);
        params.addQueryStringParameter("page", "1");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户鉴定会参数
     */
    public static RequestParams getUserIdentifyMeetingParams(Context context, String mType) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "teyao");
        params.addQueryStringParameter("op", "me");
        params.addQueryStringParameter("sp", String.valueOf(10));
        params.addQueryStringParameter("type", mType);
        params.addQueryStringParameter("page", "1");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }


    /**
     * 获取删除鉴定宝物参数
     */
    public static RequestParams getDeleteIdentifyTreasureParams(Context context, String id) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "delete");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取删除鉴定会参数
     */
    public static RequestParams getDeleteIdentifyMeetingTreasureParams(Context context, String id) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "teyao");
        params.addQueryStringParameter("op", "delete");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取发送短信的参数
     */
    public static RequestParams getSendMessageParams(Context context, String id) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "teyao");
        params.addQueryStringParameter("op", "send");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取修改昵称参数
     */
    public static RequestParams getCommitEditNicknameParams(Context context, String nickname) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "nikename");
        params.addQueryStringParameter("name", nickname);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取公开已完成鉴定参数
     */
    public static RequestParams getOpenIdentifiedParams(Context context, String id) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "public");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取忘记密码参数
     */
    public static RequestParams getChangePasswdRequestParams(Context context, String tel, String authCode, String newPswd) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "ForgotPassword");
        params.addQueryStringParameter("mobile", tel);
        params.addQueryStringParameter("code", authCode);
        params.addQueryStringParameter("password", newPswd);
        return params;
    }

    /**
     * 获取忘记密码验证码参数
     */
    public static RequestParams getAuthCodeRequestParams(Context context, String tel) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "ForgotPassword");
        params.addQueryStringParameter("mobile", tel);
        return params;
    }

    /**
     * 获取现金券参数
     */
    public static RequestParams getCashCouponParams(Context context, String type, String number) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "roll");
        params.addQueryStringParameter("op", "select");
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("rid", number);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取未支付订单数
     */
    public static RequestParams getUnPayCountParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "nocharg");
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取活动Json
     */
    public static  RequestParams getActiveParams(Context context){
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act","jb");
        params.addQueryStringParameter("api","index");
        params.addQueryStringParameter("op", "activity");
        params.addQueryStringParameter("op","activity");
        params.addQueryStringParameter("uuid", DeviceUtil.getUUID(context));
        return params;
    }

    /**
     * 获取鉴定会Json
     */
    public static  RequestParams getMeetParams(Context context){
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jian");
        params.addQueryStringParameter("act","api_teyao");
        params.addQueryStringParameter("op","info");
        return params;
    }

    /**
     * 发送报名数据
     */
    public static RequestParams getEnrollParams(Context context, String stage, String timeRange, String tel, String name, String taoci, String zaxiang) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("module","jbapp");
        params.addQueryStringParameter("act","jb");
        params.addQueryStringParameter("api","teyao");
        params.addQueryStringParameter("op","apply");
        params.addQueryStringParameter("stage",stage);
        params.addQueryStringParameter("time",timeRange);
        params.addQueryStringParameter("tel",tel);
        params.addQueryStringParameter("name",name);
        params.addQueryStringParameter("taoci",taoci);
        params.addQueryStringParameter("zaxiang",zaxiang);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));

        return params;
    }
    /**
     * 获取预约时间数据
     */
    public static RequestParams getTimeRangeParams(Context mContext) {
        RequestParams params = getBaseParamsWithHeader(mContext);
        params.addQueryStringParameter("module", "jian");
        params.addQueryStringParameter("act","api_teyao");
        params.addQueryStringParameter("op", "apply_time");
        return params;
    }

    /**
     * 获取鉴定技巧数据
     * @param mContext
     * @return
     */
    public static RequestParams getIdentifySkillParams(Context mContext) {
        RequestParams params = getBaseParamsWithHeader(mContext);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act","jb");
        params.addQueryStringParameter("api", "news");
        params.addQueryStringParameter("op", "skills");
        return params;
    }
//http://jianbao.artxun.com/index.php?module=jbapp&act=jb&api=news&op=index
    public static RequestParams getIdentifySkillDetailParams(Context context, String id,int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("module","jbapp");
        params.addQueryStringParameter("act","jb");
        params.addQueryStringParameter("api","news");
        params.addQueryStringParameter("op","index");
        params.addQueryStringParameter("type",id);
        params.addQueryStringParameter("page", String.valueOf(page));
        return params;
    }
}
