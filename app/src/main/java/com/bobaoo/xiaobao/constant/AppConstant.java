package com.bobaoo.xiaobao.constant;

import com.bobaoo.xiaobao.R;

/**
 * Created by star on 15/6/1.
 */
public class AppConstant {
    // 获取短信通知
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public static final String SP_KEY_PHONE_CHECKED = "phone_number_checked";
    public static final String UPDATE_TIME = "update_time";

    public static final String KEY_LAST_UPDATE_TIME = "umeng_last_update_time";
    public static final int DefaultTakePictureNum = 4;
    public static final int MaxTakePictureNum = 8;
    public static final String DefaultPageItemNum = "10";
    public static final String HAS_NOCHARGE_IDENTIFY = "has_no_charge_idntify";
    // 鉴定类别
    public static final int IdentifyTypeChina = 1;
    public static final int IdentifyTypeJade = 2;
    public static final int IdentifyTypePainting = 3;
    public static final int IdentifyTypeBronze = 4;
    public static final int IdentifyTypeMoney = 5;
    public static final int IdentifyTypeWooden = 6;
    public static final int IdentifyTypeSundry = 7;
    public static final int IdentifyTypeMoneyPaper = 51;
    public static final int IdentifyTypeMoneySilver = 52;
    public static final int IdentifyTypeMoneyBronze = 53;

    //6.0权限管理
    public static final int PERMISSION_REQ_CODE_READ_STORAGE = 100;
    public static final int PERMISSION_REQ_CODE_WRITE_STORAGE = 101;
    public static final int PERMISSION_REQ_CODE_READ_CAMERA = 102;
    public static String CALL_CENTER = "";


    public class OrderState {
        public static final String Wait = "0";
        public static final String Cancel = "1";
        public static final String Real = "2";
        public static final String Fake = "3";
        public static final String Doubt = "4";
    }

    public class IdentifyType {
        public static final int Normal = 0;
        public static final int Speed = 1;
        public static final int Video = 2;
        public static final int Order = 3;
        public static final int Expert = 4;
    }

    public static int getIdentifyType(int type) {
        switch (type) {
            case IdentifyType.Normal:
                return R.string.identify_type_normal;
            case IdentifyType.Speed:
                return R.string.identify_type_speed;
            case IdentifyType.Video:
                return R.string.identify_type_video;
            case IdentifyType.Order:
                return R.string.identify_type_order;
            case IdentifyType.Expert:
                return R.string.identify_type_expert;
            default:
                return R.string.identify_type_normal;
        }
    }

    public static final String SCORE_OBTAIN_TYPE_PAY = "支付";
    public static final String SCORE_OBTAIN_TYPE_REGISTER = "注册";
    public static final String SCORE_OBTAIN_TYPE_SHARE = "分享";
    public static final String SCORE_OBTAIN_TYPE_PROMOTION = "活动";
    public static final String SCORE_OBTAIN_TYPE_RECHARGE = "充值";
    public static final String SCORE_OBTAIN_TYPE_COMMENTS = "评论";
    public static final String SCORE_OBTAIN_TYPE_IDENTIFY = "鉴定";

    public static final String INTENT_MOBILE_NUMBER = "mobile";
    public static final String INTENT_FRAGMENT_TYPE = "fragment";
    public static final int INTENT_FRAGMENT_TYPE_1 = 1;
    public static final int INTENT_FRAGMENT_TYPE_2 = 2;
    public static final int INTENT_FRAGMENT_TYPE_3 = 3;

    public static final String ARR_HEADER= "活动预约时间段";


    public static String[] SCORE_OBTAIN_TYPES = {
            SCORE_OBTAIN_TYPE_PAY,
            SCORE_OBTAIN_TYPE_REGISTER,
            SCORE_OBTAIN_TYPE_SHARE,
            SCORE_OBTAIN_TYPE_PROMOTION,
            SCORE_OBTAIN_TYPE_RECHARGE,
            SCORE_OBTAIN_TYPE_COMMENTS,
            SCORE_OBTAIN_TYPE_IDENTIFY
    };

    public static String[] IDENTIFY_KIND_TABLE = {
            "陶瓷",
            "玉器",
            "书画",
            "铜器",
            "钱币",
            "木器",
            "杂项"
    };


    public static int[] IDENTIFY_IMG_TABLE = {
            R.drawable.icon_normal,
            R.drawable.icon_speed,
            R.drawable.icon_order,
            R.drawable.icon_expert_panel,
            R.drawable.icon_fast,
    };
    public static String[] IDENTIFY_METHOD_TABLE = {
            "普通鉴定",
            "极速鉴定",
            "预约鉴定",
            "专家团鉴定",
            "快速鉴定",
    };

    public static String[] IDENTIFY_TIME_TABLE = {
            "3天",
            "1小时",
            "权威专家当面鉴定",
            "5天",
            "24小时",
    };

    public static String[] IDENTIFY_PRICES = {
            "￥5", "￥20", "￥800", "￥30", "￥10"
    };

    public static final int GOODS_TRUE = 2;
    public static final int GOODS_FALSE = 3;
    public static final int GOODS_IMPEACH = 4;

    public static String TIME_RANGE_ERROR = "活动预约时间段";

    public static int ANTIQUE_POSITION_0 = 0;
    public static int ANTIQUE_VALUE_1 = 1;

    public static int MEET_CANCEL_ENROLL = 1;
    public static int MEET_ORDER_DELETE = 2;
    public static int ACTIVE_OVER = 3;
    public static int PAY_CANCEL = 4;
}
