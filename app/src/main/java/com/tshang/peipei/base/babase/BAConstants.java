package com.tshang.peipei.base.babase;

import java.math.BigInteger;

import android.os.Environment;

public final class BAConstants {

	/**********************************************************************
	 * 
	 * 数据库
	 * 
	 *********************************************************************/
	// 版本号
	public final static int PEIPEI_DB_VERSION = 8;
	// 不维护的最低版本号
	public final static int PEIPEI_DB_VERSION_MIN = 1;

	/**********************************************************************
	 * 
	 *  崩溃日志
	 * 
	 **********************************************************************/
	/* PeiPei在SD卡的位置 */
	public static final String PEIPEI_PATH = Environment.getExternalStorageDirectory() + "/PeiPei";
	public static String CRASH_PATH = PEIPEI_PATH + "/crash/";
	/**********************************************************************
	 * 
	 * 系统常量
	 *********************************************************************/

	public final static String WEIXIN_PACKAGENAME = "com.tencent.mm";

	public static double CURR_LATITUDE = 0.0;
	public static double CURR_LONGITUDE = 0.0;

	// 录音时长
	public final static int AUDIO_LENGTH = 180;

	public static final int BITRATE_AMR = 2 * 1024 * 8; // bits/sec

	public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec

	/**********************************************************************
	 * 
	 * sharepreferences保存字段
	 * uid，auth，当前数据库db，client，更新url，是否更新（about界面有用）,imei号,nick 昵称
	 *********************************************************************/
	public final static String UID_VALUE = "uid_value";
	public final static String UID_INFO = "uid_info";
	public final static String AUTH_VALUE = "auth_value";
	public final static String DB_VALUE = "db_value";
	public final static String CLIENT = "client";
	public final static String UPDATE_URL = "update_url";
	public final static String UPDATE_VER = "update_ver";
	public final static String UPDATE_STR = "update_str";
	public final static String W_IMEI_MACADDR = "w_imei_macaddr";
	public final static String LOGIN_ID = "login_id";
	public final static String NICK_VALUE = "nick";
	public final static String SEX_VALUE = "sex";
	public final static String EMAIL_VALUE = "email";
	public final static String PEIPEI_TYPE = "peipei_type";// 登录方式
	public final static String PEIPEI_GUIDE = "peipei_guide";// 是否显示引导页，false为显示，true不显示
	public final static String PEIPEI_LOGIN_PROMPT = "peipei_login_prompt";// 登录界面上的使用私语帐号登录提示
	public final static String PEIPEI_CHAT_PROMPT = "peipei_chat_prompt";// 聊天界面上的提示
	public final static String PEIPEI_UPADTE_TIME = "update_time";//检测时间
	public final static String PEIPEI_BAIDU_TIME = "baidu_time";//获取百度云推userid时间
	public final static String PEIPEI_BAIDU_USERID = "baidu_userid";//百度id
	public final static String PEIPEI_DAILY_REWARD = "daily_reward1";//每天登录领取奖励
	public final static String PEIPEI_SHARE_REWARD = "share_reward1";//每天分享领取奖励
	public final static String PEIPEI_SHARE_DAILY = "share_daily";//是否分享过
	public final static String PEIPEI_REWARD_SHOW = "reward_show";//是否显示领取奖励弹框
	public final static String GIFTS_NUM = "gifts_num";//礼物计数
	public static final String PEIPEI_INTERESTED = "peipei_interested";//对我感兴趣的人,保存时间戳
	public static final String PEIPEI_INTERESTED_FIRST = "peipei_interested_first";//是否本地有数据
	public static final String PEIPEI_INTERESTED_NEW = "peipei_intersted_new";//是否有新的数据
	public static final String PEIPEI_GPS_TIME = "piepei_gps_time";
	public static final String PEIPEI_GPS_LA = "peipei_gps_la";
	public static final String PEIPEI_GPS_LO = "peipei_gps_lo";
	public static final String PEIPEI_APP_CONFIG = "peipei_app_config";
	public static final String PEIPEI_SELECT_USER = "peipei_select_time";//宠幸时间
	public static final String PEIPEI_IDENTY = "peipei_identy";
	public static final String PEIPEI_NOTIFICATION_CHAT_TIME = "peipei_notification_chat_time";
	public static final String PEIPEI_NOTIFICATION_PUSH_CHAT_NUM = "peipei_notification_push_chat_num";
	public static final String PEIPEI_NOTIFICATION_CHAT_NUM = "peipei_notification_chat_num";

	public static final String PEIPEI_APP_CONFIG_LOTION_TIME = "peipei_app_config_lotion_time";
	public static final String PEIPEI_APP_CONFIG_IS_CREATE_HAREM_PESSIMION = "peipei_app_config_is_create_harem_pessimion";//是否可以创建后宫权限0不可以，1可以
	public static final String PEIPEI_APP_CONFIG_TOP_TEXT_BROADCAST_COIN = "peipei_app_config_top_text_broadcast_coin";//广播文字置顶金币
	public static final String PEIPEI_APP_CONFIG_TOP_VOICE_BROADCAST_COIN = "peipei_app_config_top_voice_broadcast_coin";//广播语音置顶金币
	public static final String PEIPEI_APP_CONFIG_LOAD_PIC = "peipei_app_config_load_pic";
	public static final String PEIPEI_APP_CONFIG_ACTION_LOAD_PIC = "peipei_app_config_action_pic";
	public static final String PEIPEI_WIN_RATIO = "peipei_win_ratio";
	public static final String PEIPEI_WDJ_HUODONG_REGISTER = "peipei_wdj_register";
	public static final String PEIPEI_WDJ_HUODONG_REGISTER1 = "peipei_wdj_register1";
	public static final String PEIPEI_APP_CONFIG_GIFT_VERSION = "peipei_app_config_gift_version";//陪陪礼物版本
	public static final String PEIPEI_APP_CONFIG_GIFT_NEED_UPDATE = "peipei_app_config_gift_need_update";//陪陪礼物是否需要重新获取

	public static final String REWARD_END_TIME_KEY = "reward_end_time_key";

	public static final String PEIPEI_APP_BIND_PHONE = "peipei_app_bind_phone";//获取手机验证码计时

	public static final String PEIPEI_DYNAMIC_UNREAD_NUM = "peipei_dynamic_unread_num";//动态未读条数
	public static final String PEIPEI_DYNAMIC_UNREAD_TIME = "peipei_dynamic_unread_time";//动态未读时间
	public static final String PEIPEI_FANS_UNREAD_NUM = "peipei_fans_unread_num";//新增粉丝数量
	public static final String PEIPEI_FANS_UNREAD_UID = "peipei_fans_unread_uid";//新增粉丝uid
	public static final String PEIPEI_NEW_URL = "peipei_new_url";//新的url
	public static final String PEIPEI_POINTS_WALL = "peipei_points_wall";//积分墙
	public static final String PEIPEI_ADV_URL = "peipei_adv_url";
	public static final String PEIPEI_SHOW_ADV_URL = "peipei_show_adv_url";

	public static final String PEIPEI_FLOAT_X = "peipei_float_x";
	public static final String PEIPEI_FLOAT_Y = "peipei_float_y";

	public final static String SOUND = "sound";
	public final static String SHAKE = "shake";
	public final static String SHOW_ROOMINFO = "show_roominfo";
	public final static String SHOW_ADDHOT = "show_addhot";
	public final static String SHOW_BROADCAST = "show_broadcast";

	/*********************************************************************
	 * 
	 * 网络相关参数
	 * 
	 *********************************************************************/

	/**
	 * 是否使用正式金钱充值,true:测试环境，使用sgtest；false：正式环境，使用sg
	 */
	public final static boolean IS_TEST = true;
	/**
	 * 活动配制文件  
	 */
	public final static boolean IS_ACTIVITY = false;

	public final static String PEIPEI_SERVER_HOST_PRO = "sg.tshang.com";
//	public final static String PEIPEI_SERVER_HOST_TEST = "sgtest.tshang.com";
	//	public final static String PEIPEI_SERVER_HOST_TEST = "120.24.83.183";
	public final static String PEIPEI_SERVER_HOST_TEST = "120.25.238.212";

	/**
	 * 应用编号：
	 * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成 
	 */
	public final static String APP_ID = "3000240894";//正式

	/**
	 * 应用密钥：
	 * 用于保障应用与iAppPay云支付平台sdk通讯安全及sdk初始化
	 */
	public final static String APP_KEY = "MIICXAIBAAKBgQC38u36GmjNozQcojSqKeud/oV1W6g50FyMXnDwA5IPxcEPIsOogquFRXePRCRo7PSF8ob1jvlXr7hvx6dXRmSWZR1++NG5ZG57VPpbqdgWUwrrahd93xNh5OvFTT11bY0j/j6cigaodIAwhTodjX/KCdEmqdK6mTIvCqlLmyEvkwIDAQABAoGARA8l7buHu03/IfkB81O/UZ2yU1GuVHW7SFRJBQTNLfjFDYyXR+nQ/GZPbjqQHiJN9qFdPc3Ag6kIXJNKXwg0fq/M7PJkAOiyK7sbGW7r4dFE5rKSqJLg5Dp/p9tvMn7oHeQr82MAQOKXN2Fm+iJpXnQBADz55E38PK/UCKspMcECQQDnxXKzaSybZ64bj612Mjdu+tY0h7w4opt/NRbdMg47dt+nUpZUb8Pzh7MbBQn373lV6Zhpd11Pmb5HzC/BwVuvAkEAyy2u3W1p8Blrs3l/Y6ZeoBXpNfgzcc5d4kB/Bc2Zjd44DCeoeBsd6C4vnsJXedLcbaOaxd3XrBIetQErD35vXQJBAJt0JsPJVl8Bwpi/3LO7aMzqN4RSSl+eSTegLy67pSojSE2dKjGGKWMNcIc98Pq4R6TlIhITOqXqU+MqRBnsDAcCQBhx9aEEggN2O4QLcS0/HhFqsXVh2Gav6pHHKsE6/GeaPeoQd2+D67Q8olQak8W/fe/fCEcYKtO0Fme2rUSe9K0CQExeJ7W+PJpmDdjwMkFbOnupZJKcyWMCTgBlbeEErwrfDPaCebFWvS2GJqh67xAWJxTRdjQSfr6juoX0sURT2fo=";//正式

	public final static String APP_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDYc7WIiaRgTaGL60hX7sNyGo0geZkZ05Uw9bW2HQCscuXxsOh8zHlGV2W16Uf7wV0SqeK2WYU/ru+ztrRPl3UDDpYISiaJiXdEkqJxLQOC8sKeXVhDZOd1QP2WgFUn4kzXwcNq4Ms1hL/IE9aUVPslRNychXmNe8yNmVPEQCV+GwIDAQAB";
	/**
	 * 多盟id
	 */
	public final static String DOMD_ID = "96ZJ0QxAzeYErwTCvx";

	// 开发模式,stricmode启动
	public final static boolean isDevolpMode = true;

	//通过用户uid加载的图片需要拼接的字符串，uid+LOADHEADAPPENDSTR; 
	public final static String LOAD_HEAD_UID_APPENDSTR = "@true@80@80@uid";
	//通过key加载头像
	public final static String LOAD_HEAD_KEY_APPENDSTR = "@true@80@80";
	//加载210的图片
	public final static String LOAD_210_APPENDSTR = "@false@210@210";
	//加载425的图片
	public final static String LOAD_425_APPENDSTR = "@false@425@425";
	//加载礼物图片 180
	public final static String LOAD_180_APPENDSTR = "@false@180@180";
	//加载个人主页背景图
	public final static String LOAD_640_APPENDSTR = "@false@640@400";
	//加载原图
	public final static String LOAD_0APPENDSTR = "@false@0@0";
	//加载210的认证头像
	public final static String LOAD_210_IDENTITY = "@true@210@210";

	public final static String LOAD_HEAD_UID_GROUP = "@false@80@80@uid";

	// 微信正式id
	public final static String WXAPPID = "wx2e982dd34c28c12d";

	public final static String WXAppSecret = "304d3963505a323ebe0ace6868b638ce";

	//牛牛APK下载路径
	public final static String PEIPEI_GAME_NIUNIUT_PATH = "/PeiPei/Download/Game/niuniu.apk";

	/** qq app_key */
	public static final String QQ_APP_KEY = "101071579";

	public static final String QQ_SCOPE = "get_user_info,get_simple_userinfo,add_t del_t add_pic_t,get_repost_list";

	/** 微博 APP_KEY */
	public static final String SINA_APP_KEY = "1886168773";

	/**
	 * 微博 url
	 */
	public static final String REDIRECT_URL = "http://www.tshang.com";

	public static final String SINA_USERINFO = "https://api.weibo.com/2/users/show.json";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

	public static final String BAIDU_APP_KEY = "aa32cb4685ca490c85070f451b47a507";
	public static String host = "bcs.duapp.com";
	public static String secretKey = "ba30453b394445e8aaefcff84933d6a8";
	public static String bucket = "peipei";

	public static final String BAIDU_APP_ID = "2992202";

	public final static int PEIPEI_SERVER_PORT = 35000;

	public final static int PEIPEI_SOCKET_TIMEOUT = 20000;
	public final static int PEIPEI_SHORT_SOCKET_TIMEOUT = 15000;

	public static final int SOCKET_TIME_OUT = 10 * 1000;
	public static final int SOCKET_TIME_OF_EXCEPTION = 6 * SOCKET_TIME_OUT;
	public static final int SOCKET_READ_TIME_OUT = 6 * SOCKET_TIME_OUT;

	/*********************************************************************
	 * 
	 * msg 消息协议常量
	 * 
	 *********************************************************************/
	public static byte[] CURR_IMEI = "".getBytes();
	public static BigInteger APP_VERSION = BigInteger.valueOf(0);
	public static BigInteger MSG_PKG_CONSTANT_0 = BigInteger.valueOf(0);
	public static BigInteger MSG_PKG_CONSTANT_1 = BigInteger.valueOf(1);
	public static BigInteger RECHARGE_OS_ANDROID = BigInteger.valueOf(1);

	/**********************************************************************
	 * 
	 * 储存缓存目录(图片，语音)与文件名
	 * 
	 * ********************************************************************/
	public final static String PEIPEI_FILE = "peipei";
	public final static String PEIPEI_AUDIO_FILE = "Audio";
	public final static String PEIPEI_Image_FILE = "Image";

	public final static String PEIPEI_AIBEI_LOG = "peipei_aipay_log.text";
	public final static String PEIPEI_FINGER_LOG = "peipei_finger_log.text";

	public static final int IMAGE_COMP_WIDTH_600 = 600;
	public static final int IMAGE_COMP_WIDTH_640 = 640;
	public static final int IMAGE_COMP_SIZE = 100;

	public static final int PEIPEI_XIAOPEI = 100000;//系统小陪
	public static final int PEIPEI_BROADCASET = 99999;
	public static final int PEIPEI_CHAT_XIAOPEI = 100001;//小陪
	public static final int PEIPEI_CHAT_TONGZHI = 100002;//小陪

	public static final String TEST_RECHARGE_URL = "http://ppapp.tshang.com/recharge/index?uid=";
	public static final String TEST_URL = "http://pptest.yidongmengxiang.com/props/index?u=";

	//	public static final String QUEENA_URL = "http://ppweb.tshang.com/share/raiders.html";//女王攻略： 
	public static final String FAQ_URL = "http://ppweb.tshang.com/share/help_v2.html";//	系统帮助：
	public static final String HEAD_LEVEL_URL = "http://ppweb.tshang.com/share/grade.php?uid=";//帽子等级
	public static final String SHARE_IMG = "http://ppweb.tshang.com/static/images/logo.png";//QQ分享图片
	public static final String RECHARGE_URL = "http://recharge.tshang.com/index_v1_2.php?uid=";
	public static final String GROUP_URL = "http://ppweb.tshang.com/share/harem.html";//后宫规则
	public static final String FORBIDSPEAK_URL = "http://ppweb.tshang.com/share/forbidspeak.php?u=";//封禁说明
	//http://ppweb.tshang.com/huodong/index.php?u=
	public static final String ACTIVITIES_URL = "http://ppapp.tshang.com/activities?u=";//活动专区
	public static final String INVITE_URL = "http://ppweb.tshang.com/share/receiveawards.php?uid=";//邀请好友界面
	public static final String GAMES_URL = "http://ppapp.tshang.com/game/index?u=";//游戏专区
	public static final String TEST_GAMES_URL = "http://pptest.yidongmengxiang.com/game/index?u=";//游戏专区
	public static final String WEBVIEW_UPDATE_PHOTO_URL = "http://ppapp.tshang.com/photo/accept?uid=";//上传数据
	public static final String WEBVIEW_UPDATE_VOICE_URL = "http://ppapp.tshang.com/voice/accept";//上传语音
	public static final String SEARCH_URL = "http://ppapp.tshang.com/search/user?u=";//搜索好友
	public static final String FINGER_URL = "http://ppapp.tshang.com/mora/record?u=";//猜拳记录
	public static final String SCENE_SHOP_URL = "http://ppapp.tshang.com/props/index?u=";//道具商城
	public static final String SCENE_TEST_SHOP_URL = "http://pptest.yidongmengxiang.com/props/index?u=";
	public static final String SHOW_SHARE_URL = "http://ppapp.tshang.com/show/share?uid=";//秀场分享URL
	public static final String FIND_APP_URL = "http://ppapp.tshang.com/discovery/app?u=";//发现应用
	public static final String SUSPENSION_URL_TEST = "http://pptest.yidongmengxiang.com/appapi/suspension/switch?u=";//悬浮框
	public static final String SUSPENSION_URL = "http://ppapp.tshang.com/appapi/suspension/switch?u=";//悬浮框
	public static final String XIAOMI_ACTIVITY_TEST = "http://pptest.yidongmengxiang.com/activity/xiaomi?u=";//小米活动测试环境
	public static final String XIAOMI_ACTIVITY_PRD = "http://ppapp.tshang.com/activity/xiaomi?u=";

	/**
	 * handler消息数据定义
	 */
	public static interface HandlerType {
		final int OPERATE_SUCCESS = 0;
		final int OPERATE_FAILED = 1;
		final int CREATE_TO_GETDATA = 2;//界面初始化时拉取数据
		final int CREATE_GETDATA_BACK = 3;//界面初始化拉取数据后返回
		final int LOAD_MORE = 4;//加载更多
		final int LOGIN_CALLBACK = 10;//登录后的返回操作
		final int LOGIN_THIRD_CALLBACK = 11;//第三方登录返回openid
		final int LOGIN_OTHER = 12;//在其他地方登陆
		final int LOGIN_GET_USERINFO = 13;//获取用户信息
		final int LOGIN_SHOW_REWARD = 15;//每天首次登陆显示弹出框
		final int REWARD_TOBACK = 16;//领取奖励接口返回
		final int GIFT_MESSAGE = 17;//礼物消息
		final int SENT_MESSAGE_CALLBACK = 20;//发送聊天消息后的返回
		final int SENT_RECEIPT_CHECK = 21;//聊天回执操作
		final int GIFT_BUY_RETURN = 30;//对话框购买礼物传递值
		final int USER_PROPERTY_BACK = 31;//用户金币
		final int USER_RECHARGE_GET_NO = 32;//用户充值获取订单
		final int USER_GIFT_REQUEST = 33;//用户送礼后的返回码
		final int GOLD_RECORD_BACK = 40;//金币消费记录返回
		final int SILVER_RECORD_BACK = 41;//银币消费记录返回
		final int GET_SHARE_URL_BACK = 51;//获取分享url
		final int FEED_BACK_KISS = 55;//回赠礼物
		final int CREATE_ALBUM_SURE = 90;//创建相册成功
		final int FRUSH_FINISH = 101;//刷新界面结束
		final int STATE_END_PLAYING = 121;// 结束播放录音文件
		final int INPUT_NULL = 122; //输入为空
		final int INPUT_NULL2 = 123;//输入为空
		final int INPUT_MESSAGE = 124;//
		final int CHAT_SENT_VOICE = 130;//聊天发送音频
		final int CHAT_REFRUSH = 131;//聊天阅后即焚焚烧后刷新
		final int CHAT_EVENT = 132;
		final int CHAT_FINGER_START = 140;//猜拳发起
		final int CHAT_FINGER_START_BACK = 141;//发起猜拳返回
		final int CHAT_FINGER_RESULT = 142;//回复猜拳
		final int VOICE_RECOD_OK = 150;//录音结束
		final int VOICE_RECOD_RESULT = 151;//录音上传返回
		final int REFRESH_MESSAGE_NUM = 169;//刷新未读数据
		final int GROUP_TRICK_NUM = 180;//宠信一次
		final int GROUP_TRICK = 181;//宠信回来
	}

	/**
	 * 界面之间使用 intent传值
	 */
	public static interface IntentType {

		final String ALBUMACTIVITY_ALBUMID = "albumactivity_albumid"; //上传相片时,传递相册ID,传递 albumid
		final String ALBUMACTIVITY_ALBUMNAME = "albumactivity_albumname";
		final String ALBUMACTIVITY_ALBUMCOUNT = "albumactivity_albumcount";
		final String DIRECTORYSDCPHOTOLISTACTIVITY_PHOTOLIST = "directorysdcphotolistactivity_photolist"; //展示本地相片 ,传递photolist
		final String SPACEWRITEACTIVITY_PHOTOLIST = "spacewriteactivity_photolist"; // 写贴时,从本地获取相片集合,
		final String MAINHALLFRAGMENT_USERID = "mainhallfragment_userid"; //聊天时,从主界面传递用户 ID  传递userid
		final String MAINHALLFRAGMENT_HEADPIC = "mainhallfragment_headpic";//主界面点击传递用户头像KEY
		final String MAINHALLFRAGMENT_USERNICK = "mainhallfragment_usernick";//主界面点击传递用户昵称
		final String MAINHALLFRAGMENT_USERSEX = "mainhallfragment_usersex";//主界面点击传递用户性别
		final String MAINHALLFRAGMENT_ISGROUPCHAT = "mainhallfragment_isgroupchat";//主界面点击传递是否是群聊
		final String MAINHALLFRAGMENT_USERGLAMOUR = "mainhallfragment_userglamour";//主界面点击传递用户魅力值
		final String MAINHALLFRAGMENT_USERLATESTTIME = "mainhallfragment_userlatesttime";//主界面点击传递用户最近在线时间
		final String SPACECUSTOMACTIVITY_TOPICID = "spaceCustomActivity_topicid"; //客人态动态列表点击动态,查看动态详情,传递 topicid
		final String SPACECUSTOMACTIVITY_TOPICUSERID = "spaceCustomActivity_topicuserid";//客人态动态列表点击动态,查看动态详情,传递 topicuserid;
		final String SETTINGUSERINFOACTIVITY_UPDATENICK = "settinguserinfoactivity_updatenick";//设置界面点击修改昵称,标记为修改昵称
		final String SETTINGUSERINFOACTIVITY_UPDATEBIRDTHAY = "settinguserinfoactivity_updatebirthday";//设置界面点击修改出生日期,标记为修改出生日期
		final String SETTINGUSERINFOACTIVITY_UPDATEPASSWORD = "settinguserinfoactivity_updatepassword";//设置界面点击修改密码,标记为修改密码
		final String SESSION_HEAD_STRING = "session_head_string";//头像
		final String MAINHALLFRAGMENT_LOYALTY = "mainhallfragment_loyalty";//聊天需要魅力贡献值
		final String PHOTOS_BACK = "photos_back";
		final String PHOTOS_TITLE = "photos_title";
		final String MINE_GOODSID = "mine_goodsid";//个人主页预览

	}

	// 数据类型
	public static interface rspContMsgType {
		final int E_GG_USER_NO_EXIST = -28001; // 用户不存存
		final int E_GG_USER_EXIST = -28002; // 用户已存在
		final int E_GG_ENCODE = -28003; // 编码错误
		final int E_GG_DECODE = -28004; // 解码错误
		final int E_GG_PARAM = -28005; // 请求的参数有错
		final int E_GG_PKT_INTEGRITY = -28006; // 注册登录的包完整性错
		final int E_GG_DECRYPT = -28007; // 解密错
		final int E_GG_ENCRYPT = -28008; // 加密错
		final int E_GG_PASSWD = -28009; // 密码错
		final int E_GG_LOGIN = -28010; // 登录态错
		final int E_GG_ALBUM_EXIST = -28011; // 相册已存在
		final int E_GG_ALBUM_NO_EXIST = -28012; // 相册不存在
		final int E_GG_FORBIT = -28013; // 被禁言了 
		final int E_GG_REDIRECT = -28014; // 需要重定向
		final int E_GG_USER_TYPE = -28015; // 用户类型错
		final int E_GG_NO_CONN = -28016; // 连接不存在
		final int E_GG_NO_OFFLINE_MSG = -28017; // 没有离线消息
		final int E_GG_NO_TOPIC_CONT = -28018; // 指定的主贴内容不存在
		final int E_GG_TOPIC_CONT_EXIST = -28019; // 内容已存在
		final int E_GG_GIFT_NO_EXIST = -28020; // 礼物不存在
		final int E_GG_PROPERTY_LACK = -28021; // 财富不足
		final int E_GG_SEX = -28022; // 性别错误
		final int E_GG_FOLLOWED = -28023; // 已关注
		final int E_GG_NO_FOLLOW = -28024; // 未关注
		final int E_GG_NO_RANKID = -28025; // 不存在排行id
		final int E_GG_REWARD = -28026; //  已领取奖励
		final int E_GG_GAME_NO_EXIST = -28037; // 游戏不存在
		final int E_GG_LOYALTY = -28031; // 忠诚度不够

		final int E_CACHE_NO_DATA = -21001; // 数据不存在
		final int E_NO_SPACE = -21002; // 没有存储空间
		final int E_CACHE_NO_USE = -21003; // cache块没启用
		final int E_INVALID_CACHE_ID = -21004; // cacheid非法, <0或大于最大的cacheid
		final int E_WRONG_CACHE = -21005; // cache不属于请求的bid
		final int E_CACHE_NO_EXIST = -21006; // cache块不存在
		final int E_CACHE_USED = -21007; // cache块已被使用
		final int E_CACHE_OP_DATA = -21008;
		final int E_CACHE_OP_STAMP = -21009;
		final int E_GG_CHAT_THRESHOLD = -28043; // 聊天门槛高于技能的最低魅力贡献值
		final int E_GG_BAN = -28046; // 设备被封
		final int E_GG_BLACKLIST = -28045; // 黑名单
		final int E_GG_NOT_ENGOUH_WELTH = -28021;//财富不足
		final int E_GG_FINGER_GUESSING_TIMEOUT = -28056; // 带赌注的猜拳超时
		final int E_GG_FINGER_GUESSING_INVALID = -28037; // 带赌注的猜拳超时
		final int E_GG_GROUP_TRICK_NUM = -28057; // 翻牌次数限制
		final int E_GG_IS_GROUP_MEMBER = -28053;// 已是群成员
		final int E_GG_GROUP_MEMBER_NUM = -28050; // 群成员数量限制
		final int E_GG_GROUP_NUM = -28049; // 群个数问题
		final int E_GG_SKILL_DEAL_RECLAIN = -28064; // 时间不到，还不能退礼物
		final int E_GG_GRADE = -28048; //等级不够
		final int E_GG_MSG_CODE = -28073; // 验证码错
		final int E_GG_LACK_OF_SILVER = -28076; // 银币不足
		final int E_GG_LOGIN_ONE = -28090;//一机登陆用户数限制
		final int E_GG_REGIST_ONE = -28091;//一机注册
		final int E_GG_GRADE_RPS = -28092;//猜拳等级不够
		final int E_GG_RED_TIMEOUT = -31005;//红包超时
		final int E_GG_CANNOT_PARTICIPATE_REDPACKETBET = -28120; //不可以参加红包
		final int E_GG_EFFECTS_GIFT_EXCEED_MAX_NUM = -28104; ////特效礼物超过最大数量
		final int E_GG_BROADCAST_REDPACKET_BET_CLOSE_YET = -28217; //广播红包接龙已关闭
		final int E_GG_BROADCAST_REDPACKET_CLOSE_YET = -28218; //大厅红包已关闭
	}

	public enum USER_ACT {
		DELIVER_GIFT(0), // 送礼
		RECEIVE_GIFT(1), // 收礼
		LOGIN(2), // 登录
		RECHARGE(3); // 充值

		private int value;

		public int getValue() {
			return value;
		}

		private USER_ACT(int value) {
			this.value = value;
		}
	}

	public enum Gender {

		MALE(1), FEMALE(0);
		private int value;

		public int getValue() {
			return value;
		}

		private Gender(int value) {
			this.value = value;
		}

	}

	/**
	 * 上传状态 
	 * @author vactor
	 *
	 */
	public enum UploadStatus {

		UPLOADING(0), SUCCESS(1), FAILURE(-1);

		private int value;

		public int getValue() {
			return value;
		}

		private UploadStatus(int value) {
			this.value = value;
		}

	}

	/**
	 * 聊天状态
	 */
	public enum ChatStatus {
		SENDING(1), SUCCESS(2), UNREAD(3), READED(4), FAILED(5), SEND_BURN_FAILED(6), SEND_BURN_SUCCESS(7), READED_BURN(8), BLACK(9);

		private int value;

		public int getValue() {
			return value;
		}

		private ChatStatus(int value) {
			this.value = value;
		}
	}

	/**
	 * 聊天类型
	 */
	public enum MessageType {
		TEXT(0), //文本
		VOICE(2), VOICE_KEY(6), //语音
		IMAGE(1), IMAGE_KEY(3), //图片
		NEW_MESSAGE(4), //新评论
		CHAT_FROM(5), //聊天来源
		NEW_FEED(7), //新动态
		NOTICE(8), //通知
		RECEIPT(9), //回执 
		BURN_VOICE(12), BURN_VOICE_KEY(13), //阅后即焚语音
		BURN_IMAGE(10), BURN_IMAGE_KEY(11), //阅后即焚图片
		SYSTEM(14), //系统消息,pp端变灰显示的文字
		UPLOAD_PHOTO(15), // 相册有新照片的动态
		GIFT(16), // 相册有新照片的动态
		NEW_FOLLOW_SHOW_PUSH_DATA(17), // 关注的人有新动态时的推送消息
		PRIVATE_ALBUM(18), //私密相册 
		NEW_GIFT(19), //礼物
		BROADCAST_MESSAGE(20), //广播
		FINGER(21), //猜拳
		WITHANTEFINGER(28), PEIPEI_SYSTEM(22), //进入帮助
		PEIPEI_QUENE(23), //进入女王攻略
		FOOTPRINT(24), //看过我的
		VIDEO(25), //视频
		JOINHAREM(26), //申请加入后宫
		AGREEJOINHAREM(27), //同意加入后宫
		CREATEREDPACKET(29), //创建红包
		UNPACKETREDPACKET(30), //有人拆分红包群通知
		SKILLDEALINFO(31), //技能下单信息
		GGSKILLINFO(32), //女性对男性技能感兴趣
		BROADCASTCOLOR(33), //广播的特殊颜色
		MALE_DECREE(34), //圣旨
		FEMALE_DECREE(35), //懿旨
		GOGIRL_DATA_TYPE_SMILE(36), //陪陪表情
		GOGIRL_DATA_TYPE_TOPIC_FEED(37), //动态
		GOGIRL_DATA_TYPE_ABUNDANT_TXT(38), //带跳转的文字
		GOGIRL_DATA_TYPE_RECHARGE_BROADCAST(39), // 广播里的充值消息
		GOGIRL_DATA_TYPE_FANS(40), //新的粉丝数量
		NEWFINGER(41), //银币猜拳
		GOGIRL_DATA_TYPE_BEGIN_TRANS_OFFLINE_MSG(42), // 开始传输离线消息
		GOGIRL_DATA_TYPE_END_TRANS_OFFLINE_MSG(43), // 结束传输离线消息
		INOUT_BROADCASE(44), //进出广播特性
		SHOW_VOICE(45), //秀场语音
		GOGIRL_DATA_TYPE_SHOW_ROOM_ROLE(46), // 秀场成员角色改变
		GOGIRL_DATA_TYPE_ROOM_INFO(47), // 房间信息
		GOGIRL_DATA_TYPE_SHOW_GIFT_PUSH(48), SYSTEMNOTIFYINFO(49), SHOWSHAREBROADCASTINFO(50), //秀分享到广播大厅
		GOGIRL_DATA_TYPE_SERIES_SHOW_GIFT(51), GOGIRL_DATA_TYPE_ANIMATION_BROADCAST(52), //带动画的广播(datainfo存动画id)
		GOGIRL_DICE_TYPE(53), //骰子
		GOGIRL_TEXT_IMAGE(54), //点击筛子
		GOGIRL_ADVENTURE_DIALOG(55), //大冒险结果弹窗
		GOGIRL_ADVENTURE_DB(56), //大冒险结果本地显示
		GOGIRL_ADVENTURE_GIFT(57), //大冒险礼物
		GOGIRL_DICE_FINISH(59), //大冒险结束
		GOGIRL_DARE_PASS(60), GOGIRL_TRUTH(61), //真心话
		GOGIRL_DATA_TYPE_TRUTH_ANSWER(62), //真心话答案
		GOGIRL_DATA_TYPE_DYNAMICS_FEED(63), // 动态回复
		GOGIRL_DATA_TYPE_PARTICIPATE_AWARD(64), //悬赏私聊
		GOGIRL_DATA_TYPE_PUBLICTION_AWARD_BROADCAST(65), //广播悬赏
		GOGIRL_DATA_TYPE_NOTIFY_SNDAWARD_INFO(66), //悬赏结束
		GOGIRL_DATA_TYPE_NOTIFY_SNDAWARD_INFOV2(67), //新增悬赏结束定义
		GOGIRL_DATA_TYPE_SKILL_INVITED(68), // 技能邀请
		GOGIRL_DATA_TYPE_SKILL_INVITED_RESULT(69), // 技能邀请回应

		GOGIRL_DATA_TYPE_ANONYM_TEXT(70), //匿名 文本
		GOGIRL_DATA_TYPE_ANONYM_PIC(71), //匿名 图片
		GOGIRL_DATA_TYPE_ANONYM_VOICE(72), //匿名 语音
		GOGIRL_DATA_TYPE_ANONYM_PICKEY(73), //匿名 图片key
		GOGIRL_DATA_TYPE_ANONYM_VOICEKEY(76), // 匿名 语音key
		GOGIRL_DATA_TYPE_ANONYM_RECEIPT(79), // 匿名回执
		GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PIC(80), // 匿名 阅后图片
		GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PICKEY(81), // 匿名 阅后图片Key
		GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_VOICE(82), // 匿名 阅后语音
		DATA_TYPE_TRANSITORY_ANONYM_VOICEKEY(83), //匿名 阅后语音Key
		GOGIRL_DATA_TYPE_ANONYM_VEDIO(85), //匿名 视频
		GOGIRL_DATA_TYPE_ANONYM_SMILE(86), //匿名表情
		GOGIRL_DATA_TYPE_ALBUM_ANONYM_CHAT(87), //匿名私密相册
		GOGIRL_DATA_TYPE_NEW_RECEIVE_ANONYM_GIFT_PUSH_DATA(89), //匿名聊天 私物

		GOGIRL_DATA_TYPE_HAREM_SOLITAIRE_RED_PACKET(100), //后宫红包接龙 
		GOGIRL_DATA_TYPE_HAREM_GRAP_SOLITAIRE_RED_PACKET(101), //后宫抢红包接龙 
		GOGIRL_DATA_TYPE_RED_PACKET_BET_TIMEOUT(102), //红包接龙超时 
		GOGIRL_DATA_TYPE_ORNAMENT_BROADCAST(103), //广播悬赏
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET(104), //大厅红包接龙
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET_TIMEOUT(105), //大厅 广播红包接龙过期
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET_UNPACK(106), // 大厅广播拆红包接龙
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET(107), // 创建大厅红包
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_TIMEOUT(108), //大厅 广播红包过期
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_UNPACK(109), // 大厅广播拆红包
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_DONE(110), // 大厅广播红包已领完
		GOGIRL_DATA_TYPE_GET_BROADCAST_RED_PACKET_YET(111), // 大厅广播红包已经被领取
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_BET_NOT_AVAIL(112), //大厅 广播红包接龙不可用
		GOGIRL_DATA_TYPE_BROADCAST_RED_PACKET_NOT_AVAIL(113), //大厅 广播红包雨不可用
		GOGIRL_DATA_TYPE_BROADCAST_SPECIAL_EFFECT(114); //礼物特效

		private int value;

		public int getValue() {
			return value;
		}

		private MessageType(int value) {
			this.value = value;
		}

		public static MessageType getMessage(int index) {
			for (MessageType mt : MessageType.values()) {
				if (mt.getValue() == index) {
					return mt;
				}
			}
			return null;
		}
	}

	/**
	 * 聊天消息目标
	 */
	public enum ChatDes {
		TO_FRIEDN(0), TO_ME(1);

		private int value;

		public int getValue() {
			return value;
		}

		private ChatDes(int value) {
			this.value = value;
		}
	}

	public enum ChatFromType {
		CHAT_PRIVATE(0), CHAT_GROUP(1), CHAT_ANONYM(3);

		private int value;

		public int getValue() {
			return value;
		}

		private ChatFromType(int value) {
			this.value = value;
		}
	}

	/**
	 * 悬赏状态
	 * @author Aaron
	 *
	 */
	public enum RewardType {
		ALL(0), PROCCED(1), END(2), JOIN(3), PULISH(4), WIN(5);
		private int value;

		public int getValue() {
			return value;
		}

		private RewardType(int value) {
			this.value = value;
		}
	}

	//设置界面消息开关
	public static interface SwitchType {
		public static final int const_req0 = 0;//0：聊天消息推送；
		public static final int const_req1 = 1;// 1：系统通知推送;
		public static final int const_req2 = 2;// 2: 礼物通知推送;
		public static final int const_req3 = 3;//  3:防骚扰模式
	}

	public static interface SwitchStatus {
		public int GG_US_CHAT_PUSH_FLAG = 1 << 0; // 是否 接收聊天 信息离线推送标志
		public int GG_US_SYS_PUSH_FLAG = 1 << 1; // 是否 接收系统通知
		public int GG_US_GIFT_PUSH_FLAG = 1 << 2; // 是否 接收礼物 通知
		public int GG_US_ANTIANNOY_FLAG = 1 << 3; // 是否打开 防骚扰 模式\
		public int GG_US_BAN_FLAG = 1 << 4; // 是否被封号
		public int GG_US_AUTH_FLAG = 1 << 5; // 证证用户标志
		public int GG_US_FIRST_RECHARGE_FLAG = 1 << 6; // 首充标志
	}

	//上传头像类型
	public static interface UploadHeadType {
		public static final int HEAD = 0;//0：头像；
		public static final int HEAD_BACKGROUND = 1;// 1：背景封面;
		public static final int IDENTIFY = 2;//认证
	}

	//排行类型
	public static interface RankType {
		public static final int femalDayRank = 1;
		public static final int femalWeekRank = 2;
		public static final int femalMonthRank = 3;//总榜
		public static final int cosumeDayRank = 101;
		public static final int cosumeWeekRank = 102;
		public static final int cosumeTotalRank = 103;
		public static final int newComersFemale = 201;//新人女
		public static final int newComersMale = 202;//新人男
		public static final int gameRank = 301;//游戏排行
		public static final int haremRank = 302;//后宫排行

	}

	public static interface RelationshipType {
		public static final int followed = 1;//已经关注
	}

	/**
	 * 数值类型
	 */
	public enum NumericalType {
		SCORE(0), // 积分
		CHARM(1), // 魅力值
		SILVER_COIN(2), // 银币
		GOLD_COIN(3), // 金币
		CONSUME_GOLD_COIN(4);// 消费的金币

		private int value;

		public int getValue() {
			return value;
		}

		private NumericalType(int value) {
			this.value = value;
		}

	}

	public enum LoginType {
		PHONE(0), // 按手机注册的用户
		USESR(1), // 由用户名注册的用户
		QQ(2), // 用qq号码注册的用户
		SINA(3), // 新浪微博用户
		TWX(4), //微信用户
		BIND_PHONE(5);//绑定手机

		private int value;

		public int getValue() {
			return value;
		}

		private LoginType(int value) {
			this.value = value;
		}
	}

	public enum UpdateType {
		UPDATE_LEVEL_NO(0), // 无需升级
		UPDATE_LEVEL_OPTIONAL(1), // 可选升级
		UPDATE_LEVEL_FORCE(2); // 强制升级

		private int value;

		public int getValue() {
			return value;
		}

		private UpdateType(int value) {
			this.value = value;
		}
	}

	public enum PushMessageType {
		CHAT(0), //聊天
		GIFT(1), //礼物
		DYNAMIC(2);//动态

		private int value;

		public int getValue() {
			return value;
		}

		private PushMessageType(int value) {
			this.value = value;
		}
	}

	//	public enum RewardType {
	//		DAILY_LOGIN(2), //每天登陆
	//		DAILY_SHARE(3);//每天分享
	//
	//		private int value;
	//
	//		public int getValue() {
	//			return value;
	//		}
	//
	//		private RewardType(int value) {
	//			this.value = value;
	//		}
	//	}

	public enum ShareType {
		SHARE_TO_QQ(0), SHARE_TO_WX(1), SHARE_TO_LINE(2), SHARE_TO_SINA(3), SHARE_TO_TWB(4), SHARE_TO_QQZONE(5);
		private int value;

		public int getValue() {
			return value;
		}

		private ShareType(int value) {
			this.value = value;
		}
	}

	public enum Finger {
		STONE(0), CLOTH(1), SCISSORS(2);

		private int value;

		public int getValue() {
			return value;
		}

		private Finger(int value) {
			this.value = value;
		}
	}

	//排行类型
	public static interface GiftId {
		public static final int GIFT_CASTLE = 11;//城堡
		public static final int GIFT_BIG = 12;//大礼包
		public static final int GIFT_ROSE = 20;//99玫瑰
		public static final int GIFT_FIREWORK = 21;//99烟花
		public static final int GIFT_CROWN = 176;//女王之冠
		public static final int GIFT_CHRISTMAS = 227;//圣诞节
		public static final int GIFT_YUANDAN = 229;//元旦节
		public static final int GIFT_REDPACKET = 230;//红包雨
		public static final int GIFT_FIRE = 232;//鞭炮
		public static final int GIFT_COUPLET = 240;//对联
		public static final int GIFT_HAPPY_NEW_YEAR = 244;//猴子拜年
		public static final int GIFT_ARCHWAY = 245;//烟花
		public static final int GIFT_FLOWER_SEA = 248;//花海

	}

	public static interface SkillStutas {
		public final int GG_SKILL_DEAL_STEP_START = 0; // 订单初始状态
		public final int GG_SKILL_DEAL_STEP_ACCEPT = 1; // 女方已接受
		public final int GG_SKILL_DEAL_STEP_REFUSE = 2; // 女方已拒绝
		public final int GG_SKILL_DEAL_STEP_RECLAIN = 3; // 支付方已回收礼物
		public final int GG_SKILL_DEAL_STEP_MARK = 4; // 已评分
		public final int GG_SKILL_DEAL_STEP_APPEALING = 5; // 申诉中
		public final int GG_SKILL_DEAL_STEP_APPEALED = 6; // 申诉完成
		public final int GG_SKILL_DEAL_STEP_END = 100; // 结束
	}

	public static interface SkillAct {
		public final String YES = "yes";
		public final String NO = "no";
	}

	public static interface NotificationManagerId {
		public final int PUSH_CHAT = 0x7535f;
		public final int PUSH_DYNAMIC = 0x7535e;
		public final int OTHER_CHAT = 0x7535d;
		public final int PUSH_OTHER = 0x7534d;
	}

	public static interface InOutAct {
		public final int in = 1;
		public final int out = -1;
		public final int kick = 2;
	}

	public static interface ShowMemberType {
		public final int normal = 3;
		public final int owner = 1;
		public final int vip = 2;// 3：普通成员；1: 房主,2: 嘉宾
	}

	public static interface ProtobufErrorCode {
		final int ParameterslackError = -28201; //#UID 或 Auth值错误
		final int GetDataError = -28202; //#调用工具返回数据错误
		final int UserAuthError = -28203; //#用户AUTH值错误
		final int ShowRoomFullError = -28204; //#开秀房间满了
		final int AddShowRoomError = -28205; //#开秀失败
		final int IsShowingError = -28206; //#用户已经开秀，不能重复开秀
		final int InShowRoomError = -28207; //#进房间失败
		final int OutShowRoomError = -28208; //#出房间失败
		final int KickShowRoomError = -28209; //#踢出房间失败
		final int IsNotOwnerRoomUserError = -28210; //#只有房主才能踢人,踢人者不是房主
		final int NotFoundInOUtActError = -28211; //#非法操作
		final int IsNotKickOwnerError = -28212; //#房主不能踢自己
		final int NotChatDataError = -28213; //#没有消息数据
		final int NoRoleAccessError = -28214; //无权限设置嘉宾
		final int IsOnleSetMemberError = -28215; //只能设置房间成员为嘉宾
		final int SetGuestsRoleError = -28216; //设置嘉宾失败
		final int CancelGuestsRoleError = -28217; //取消嘉宾失败
		final int OpenShowLessGradeError = -28218; //开秀等级不够
		final int NoridingError = -28219; //没有坐骑
		final int AuthDifferentError = -28220; //Auth不一致
		final int KickUserLessTimeError = -28221;//踢出成员5分钟内无法再次进入房间
		final int AddHotNumError = -28222;//点赞失败
		final int CloseShowRoomError = -28223;//关闭房间失败
		final int ShowRoomIsCloseError = -28224;//房间已关闭
		final int ReportRoomError = -28225;//举报房间失败
		final int MaxRoleCountError = -28226;//设置嘉宾数量超额
		final int OpenShowIsBusyError = -28227;//开秀太频繁5分钟内不能再次开秀
		final int ShowRoomMemberIsFull = -28228;//秀场成员已满
	}

	//爱贝init时间，临时用,进入商城界面判断每过半小时重新init
	public static long PEIPEI_AIBEI_INIT_TIME;

	public static String PP_PERSONAL = "pp://personal";//跳转到个人主页
	public static String PP_RECHARGE = "pp://recharge"; //充值
	public static String PP_MAINHALL = "pp://mainhall";// 大厅
	public static String PP_MAINRANK = "pp://mainrank";// 排行
	public static String PP_BROADCAST = "pp://rankhank";// 广播	
	public static String PP_PHOTO = "pp://photo";//照片
	public static String PP_VOICE = "pp://voice";//录音
	public static String PP_PLAY_VOICE = "pp://play_voice";//播放语音
	public static String PP_MAKEPROFIT = "pp://makeprofit";//免费赚币
	public static String PP_SHARE = "pp://share";//分享
	public static String PP_MYHOME = "pp://myhome";//我的页面
	public static String PP_TO_NIUNIU_GAME = "pp://to_niuniu_game";//跳转到牛牛界面
	public static String PP_TO_PUBLIC = "pp://public";//公共界面，带URL
}
