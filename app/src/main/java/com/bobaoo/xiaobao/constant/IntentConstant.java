package com.bobaoo.xiaobao.constant;

/**
 * Created by star on 15/6/1.
 */
public class IntentConstant {
    // 拍照、提交订单
    public static final String IdentifyId = "identify_id";
    public static final String IdentifyType = "identify_type";
    public static final String IdentifyMethodInfo = "identify_method_info";
    public static final String IdentifyMethodPrices = "identify_method_prices";
    public static final String UsingPictureFilePaths = "using_picture_file_paths";
    public static final String UsingPictureIndex = "using_pickture_index";
    public static final String PictureFilePath = "picture_file_path";
    public static final String TotalPictureNum = "total_picture_num";
    public static final String CurrentPictureNum = "current_picture_num";
    public static final String NextPictureNum = "next_picture_num";
    public static final String RefreshPictureNum = "refresh_picture_num";

    public static final String KEY_MAIN_PAGER_FRAGMENT_ID = "key_main_page_fragment_id";

    public static final String USER_PAY_GOODS_ID = "goods_id";
    public static final String EXPERT_ID = "expert_id";
    public static final String IS_ONLINE_EXPERT = "is_online_expert";
    public static final String ORGANIZATION_ID = "organization_id";
    public static final String ORGANIZATION_NAME = "organization_name";
    public static final String EXPERT_NAME = "expert_name";

    public static final String INFO_ID = "info_id";
    public static final String ORDER_ID = "order_id";
    public static final String FEEDBACK_ID = "feedback_id";
    public static final String ENQUIRY_ID = "ENQUIRY_ID";

    public static final String IDENTIFY_PAGE_INDEX = "identify_page_index";
    public static final int IDENTIFY_PAGE_INDEX_NO_PAY = 0;
    public static final int IDENTIFY_PAGE_INDEX_NO_IDENTIFY = 1;
    public static final int IDENTIFY_PAGE_INDEX_IDENTIFIED = 2;

    public static final String USER_ACCOUNT_BALANCE = "user_account_banlance";
    public static final String USER_ID = "userid";
    public static final String USER_RECHARG_VALUE = "recharge_value";
    public static final String USER_SCORE = "user_score";

    public static final String APP_EXIT_KEY = "app_exit";
    public static final int APP_EXIT_CODE = 1;

    public static final String QUERY_GOODS_STATE = "ask_goods_state";
    public static final String QUERY_GOODS_PRICE = "ask_goods_price";

    public static final String QUERY_REPORT = "ask_report";
    public static final String QUERY_GOODS_PHOTO = "ask_photo";
    public static final String QUERY_GOODS_FROM = "ask_from";
    public static final String QUERY_GOODS_TO = "ask_to";
    public static final String QUERY_GOODS_ID = "gid";
    public static final String QUERY_TYPE = "query_type";

    public static final String CHARGED_STATE = "identify_state";

    public static final int RequestCodeCheckPhoneNumber = 10009;
    public static final int RequestCodeTakePicture = 10010;
    public static final int RequestCodeSelectPicture = 10011;
    public static final int RequestCodeIdentifyType = 10000;

    public static final String TARGET_ACTIVITY = "target_activity";
    public static final String TARGET_FRAGMENT = "target_fragment";
    public static final String TYPE = "type";
    //详情页banner进图片浏览页面
    public static final String ORDER_DETAIL_BANNER_IMG_URLS = "home_banner_img_urls";
    public static final String ORDER_DETAIL_BANNER_IMG_INDEX = "home_banner_img_index";
    public static final String ORDER_DETAIL_BANNER_IMG_RATIOS = "home_banner_img_ratios";

    //修改订单
    public static final String MODIFY_ORDER = "modify_order";
    public static final String MODIFY_CURRENT_INDEX= "modify_current_index";

    public static final String CHECK_PHONE_FLAG = "code_success";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String QUERY_GOOD_NAME = "query_good_name";

    public static final String IDENTIFY_TYPE_FLAG = "identify_type_flag";

    public static final String ORDER_VIDEO_URL = "order_video_url";
    public static final String QUERY_GOODS_HEAD = "order_query_head";

    public static final String IS_MY_ORDER_FLAGS = "is_my_order_flags";
    public static final String QUERY_EXPERT_ID = "query_expert_id";

    public static final String IntentAction = "intent_action";
    public static final String SubmitOrder = "submit_order";

    public static final String IS_FROM_NOTIFICATION = "is_from_notification";

    public static final String WEB_URL = "web_url";
    public static final String WEB_TITLE = "web_title";

    public static final String IDENTIFY_TYPE_REGISTRATION = "identify_type_registration";

    //鉴定会
    public static final String IdentifyMeetingId = "identify_meeting_id";
    public static final int IdentifyMeetingSendMessageCount = 3;//鉴定会发送短信的最大次数

    public static final String ACTIVE_INFO = "activeInfo";

    public static final String EnrollId = "enroll_id";
    public static final String IDENTIFY_SKILL_DATA = "identify_skill_data";
    public static final String IDENTIFY_SKILL_TYPE = "identify_skill_type";
    public static final String FIND_CHILD_TITLE = "find_child_title";

    public static final String COMMENT_POSITION = "comment_position";
    public static final String IDENTIFY_INFO = "identify_info";
}
