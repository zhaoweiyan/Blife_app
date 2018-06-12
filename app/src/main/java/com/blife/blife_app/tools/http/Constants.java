package com.blife.blife_app.tools.http;

/**
 * Created by w on 2016/8/22.
 */
public class Constants {


    public static String TOKEN = "ACCESS-TOKEN";
    public static String CONTACT_PHONE = "contact_phone";
    public static String WEIXIN_APPSRCERT = "e17e959fbaa2db1908c47b96806e2c59";
    public static String WEIXIN_APPID = "wx36e34c361a0b2b3f";
    public static String SINA_APPSRCERT = "4a76f017f343a701b02873e237b83765";
    public static String SINA_APP_KEY = "1854449777";
    //    public static String QQ_APP_KEY = "3gJOgoFGt2dmTmbB";
//    public static String QQ_APPID = "1105704878";
    public static String QQ_APP_KEY = "mbKxBqEzHcdta4ce";
    public static String QQ_APPID = "1105774820";
    public static String JPUSH_ALIAS = "push_alias_id";
    public static String ACCUSATION_OPTIONS = "accusation_options";
    public static String PWD_KEY = "password_secret_key";
    //shareprefe  文件名
    public static String BlifeName = "BlifeApp";
    //定位服务上传
    public static final String ACTION_JPUSH = "推送广播";
    public static String LOCATION_RESULT_LNG = "locationpoilng";
    public static String LOCATION_RESULT_LAT = "locationpoilat";
    public static String LOCATION_RESULT_ADDRESS = "locationpoiaddress";
    public static String LOCATION_RESULT_LATLNG = "locationpoilatlng";
    public static String LOCATION_RESULT_ACTION = "com.blife.blife_app.UPDATE_POI_ACTION";

    public static String LOCATION_CURRENT_ADDRESS = "locationcurrentaddress";
    public static String LOCATION_CURRENT_LAT = "lat";
    public static String LOCATION_CURRENT_LNG = "lng";
    //赵利冲
    public static String CITIZEN_OR_COMPANY = "citizen_or_company";
    public static String IS_IDENTIFY = "is_identify";
    public static String MINEHEADER = "mine_header";
    public static String MINE_NICKNAME = "mine_nickname";
    public static String TRANSFER_LIMIT_TIME = "transfer_limit_time";
    public static String TRANSFER_MIN_MONEY = "transfer_min_money";
    public static String TRANSFER_REQUEST = "transfer_request";
    public static String TRANSFER_REQUEST_NOTICE = "transfer_request_notice";
    public static String OPENID = "openid";
    public static String BALANCE = "balance";
    public static String IDENTITY = "IDENTITY";
    public static String IDENTITY_PASS = "IDENTITY";
    public static String JPUSH_JUMP = "jpush_jump";
    public static String JPUSH_IMAGE = "jpush_image";
    public static String JPUSH_BROADCAST = "broadcast";
    public static String JPUSH_NOTICE = "notice";
    public static String ERROR_MESSAGE = "{\"code\":\"6000\",\"message\":\"USER_UPDATE_FAILED\",\"detail\":\"请检查网络设置!\"}";
    public static String WX_OPENID = "wx_openid";
    public static String WX_SP = "wx_sp";
    public static String IMAGE_CACHE = "image_cache";
    public static String BOUNS_AMOUNT = "bouns_total_amount";

    //我的推广
    public static String EXTENDS_SHARE_TITLE = "extends_share_title";
    public static String EXTENDS_SHARE_CONTENT = "extends_share_content";
    public static String EXTENDS_SHARE_IAMGEURL = "extends_share_imageurl";
    public static String NICKNAME = "{USER_NICKNAME}";
    //全局配置
    public static String APP_ID = "d6423624d4248404550712141315f49b";
    public static String APP_SECRET_KEY = "df9458272715538a23c0f45dafbadf2c";

    //缓存key
    public static String CACHE_ACCESS_TOKEN_KEY = "cache_access_token_key";
    public static String CACHE_USER_ACCOUNT_KEY = "cache_user_account_key";
    public static String TOKEN_STATE_INVALID = "token_state";

    public static String API_SUCCESS_CODE = "00000001";
    //Token的置换时间--失效前7天
    public static long TOKEN_REFRESH_TIME = 7 * 24 * 60 * 60 * 1000;
    //Token失效标识
    public static String ACCESS_TOKEN_EXPIRED_CODE = "10000005";
    //测试时使用的缓存时间
    public static long COMMONTIME = 12 * 60 * 60 * 1000;
    public static long COMMONTIME_ONE_DAY = 24 * 60 * 60 * 1000;
    public static long COMMONTIME_THREE_DAY = 3 * 24 * 60 * 60 * 1000;
    public static long COMMONTIME_HOUR = 60 * 60 * 1000;

    //缓存时间
    public static long CACHE_TIME_24_HOUR = 24 * 60 * 60 * 1000;

    //APP第一次运行的标识
    public static String APP_FIRST_OPEN = "app_first_open";

    //位置信息上传时间间隔
//    public static long UPLOAD_LOCATION_TIME = 10 * 1000;
    public static long UPLOAD_LOCATION_TIME = 60 * 10 * 1000;
    //创建的广告ID
    public static String CREATE_ADV_ID = "create_adv_id";
    public static String CREATE_AGAIN_ADV_ID = "create_again_adv_id";
    public static String ADV_CURRENT_TYPE = "adv_current_type";//新建广告
    public static int ADV_CURRENT_TYPE_CREATE = 30;//新建广告
    public static int ADV_CURRENT_TYPE_DRAFT = 31;//草稿广告
    public static int ADV_CURRENT_TYPE_AGAIN = 32;//再次广告
    public static String ADV_CURRENT_START_TIME = "adv_current_start_time";//广告开始时间
    public static String ADV_CURRENT_END_TIME = "adv_current_end_time";//广告结束时间
    public static String ADV_CURRENT_TITLE = "adv_current_title";
    public static String ADV_CURRENT_FIRST_IMAGE = "adv_current_first_image";
    public static String ADV_CURRENT_EDITING_ID = "adv_current_editing_id";
    public static String ADV_CURRENT_EDITING_TYPE = "adv_current_editing_type";
    //Share_Info
    public static String SHARE_INFO_LINK = "link";
    public static String SHARE_INFO_TITLE = "title";
    public static String SHARE_INFO_DESCRIPTION = "description";
    public static String SHARE_INFO_DESCRIPTION_ON_ACCEPTED = "description_on_accepted";
    public static String SHARE_INFO_TITLE_ON_PAY_SUCCESS = "title_on_pay_success";
    public static String SHARE_INFO_DESCRIPTION_ON_PAY_SUCCESS = "description_on_pay_success";
    public static String SHARE_INFO_SUPER_BONUS_TITLE = "super_bonus_title";
    public static String SHARE_INFO_SUPER_BONUS_DESCRIPTION = "super_bonus_description";

    public static String SHARE_INFO_PAY_SUCCESS_ADV_TITLE = "share_info_pay_success_adv_info";
    public static String SHARE_INFO_PAY_SUCCESS_ADV_FIRST_IMAGE = "share_info_pay_success_adv_first_image";

    public static String SHARE_INFO_DESCRIPTION_REPLACE_MONEY = "{BONUS_MONEY}";
    public static String SHARE_INFO_DESCRIPTION_ON_PAY_SUCCESS_REPLACE_TEXT = "{ADVERTISEMENT_TITLE}";
    //广告id
    public static String ADV_ID = "adv_id";
    public static String PUB_ID = "pub_id";
    public static String BONUS_LOCATION_ADDRESS = "bonus_location_address";
    public static String BONUS_LOCATION_LAT_LNG = "bonus_location_lat_lng";
    public static String BONUS_GRAB_MONEY = "bonus_grab_money";
    public static String BONUS_GRAB_TIME = "bonus_grab_time";
    public static String ADV_TIME_END = "adv_time_end";
    //超级大红包
    public static String SUPER_BONUS_EVENT_ID = "super_bonus_event_id";
    public static String SUPER_BONUS_RANDOM_IMAGE = "super_bonus_random_image";
    public static String SUPER_BONUS_BEAN = "super_bonus_bean";
    public static String SUPER_BONUS_JOINED = "super_bonus_user_joined";
    public static String SUPER_BONUS_TICKET_ID = "super_bonus_ticket_id";
    public static String SUPER_BONUS_STATE = "superbonus_state";
    public static final int SUPER_BONUS_STATE_NO_JOIN = 0x501;
    public static final int SUPER_BONUS_STATE_NO_CASH = 0x502;
    public static String SUPER_BONUS_STATE_WINNING_MONEY = "superbonus_state_winning_money";
    public static final int SUPER_BONUS_STATE_WINNING = 0x503;
    //广告金额
    public static String CREATE_ADV_TOTAL_AMOUNT = "create_adv_total_amount";
    //位置上传个数
    public static String SEND_TARGET_NUM = "send_target_num";

    //图片压缩最小值
    public static int COMPRESS_KB_VALUE = 200;

    //支付
    public static String PAYMENT_ORDER = "payment_order";
    public static String PAYMENT_TYPE = "payment_type";
    public static String PAYMENT_ACCOUNT = "payment_account";
    public static String PAYMENT_AMOUNT = "payment_amount";
    public static String PAYMENT_RESULT = "payment_result";
    public static int PAYMENT_SUCCESS = 9001;
    public static int PAYMENT_FAILED = 9002;

    //更新版本服务
    public static String UPDATE_URL = "update_url";
    public static String UPDATE_SAVE_PATH = "update_saev_path";
    public static String UPDATE_IS_TRUE = "update_is_true";
    public static String UPDATE_MANDATORY = "update_mandatory";

    //H5地址传输TAG
    public static String H5_URL_TAG = "h5_url_tag";
    public static String H5_TITLE_TAG = "h5_title_tag";

    //H5地址
    public static String H5_HTTP = "http://cdn.blife-tech.com/agreement";

    //请求地址
    public static String HTTP = "http://api.blife-tech.com/v1";

    // 	获取服务器Common配置
    public static String HTTP_SERVICE_COMMON = HTTP + "/server/common";
    //获取验证码
    public static String HTTP_LOGINCODE = HTTP + "/user/identifyingcode";
    //注册
    public static String HTTP_REGITS_COMMIT = HTTP + "/user/authentication";
    //登录
    public static String HTTP_LOGIN = HTTP + "/user/authentication";
    //重置密码
    public static String HTTP__RESET_COMMIT = HTTP + "/user/password";
    //TAG
    //获取服务器Common配置
    public static int TAG_SERVICE_COMMON = 301;
    public static int TAG_LOGINCODE = 302;
    public static int TAG_REGITS_COMMIT = 303;
    public static int TAG_LOGIN = 304;
    public static int TAG_RESET_COMMIT = 305;


    //各种协议h5地址及说明h5
    //
    //关于便联      企业
    public static String ABOUT_COMPANY = "http://cdn.blife-tech.com/agreement/about_blife_com.html";
    //关于便联      公民
    public static String ABOUT_CITIZEN = "http://cdn.blife-tech.com/agreement/about_blife_user.html";
    //使用条款      企业
    public static String AGREEMENT_COMPANY = "http://cdn.blife-tech.com/agreement/com_agreement.html";
    //使用条款      公民
    public static String AGREEMENT_CITIZEN = "http://cdn.blife-tech.com/agreement/user_agreement.html";
    //发票条款
    public static String INVOICE = "http://cdn.blife-tech.com/agreement/invoice.html";
    //支付条款
    public static String PAYMENT = "http://cdn.blife-tech.com/agreement/payment.html";
    //提现条款
    public static String WITHDRAW = "http://cdn.blife-tech.com/agreement/withdraw.html";
    //提现说明
    public static String WITHDRAW_INTRO = "http://cdn.blife-tech.com/agreement/withdraw_intro.html";


}
