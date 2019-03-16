package com.tshang.peipei.base.handler;

/**
 * @Title: HandlerValue.java 
 *
 * @Description: 定义handler刷新数据的一些值
 *
 * @author Administrator  
 *
 * @date 2014年8月5日 下午5:40:50 
 *
 * @version V1.0   
 */
public class HandlerValue {

	/**
	 * 欢迎页面
	 */
	public static final int DESTROY_WELCOME_VALUE = 0x1000;//闪屏页面退出的值

	/**
	 * 主界面tab导航
	 */
	public static final int MAIN_LOGIN_BACK_VALUE = 0x1001;//主页登录回来
	public static final int MAIN_RECEIVE_BROADCAST_COUNT = 0x1002;//主界面接收到广播的数量
	public static final int MAIN_MESSAGE_COUNT = 0x1003;//主界面接收到的消息数量
	public static final int MAIN_CHAT_MESSAGE_OTHER = 0x1004;//有推送消息过来
	public static final int MAIN_LOGIN_SHOW_REWARD = 0x1005;//领取每日奖励
	public static final int MAIN_BROADCAST_LIST_SUCCESS = 0x1006;//获取成功的广播列表数据
	public static final int MAIN_BROADCAST_LIST_FAILED = 0x1007;//请求广播列表失败
	public static final int MAIN_BROADCAST_APPEND_DATA = 0x1008;//广播数据追加
	public static final int MAIN_BROADCAST_RECEIVE_NEW_BROADCAST = 0x1009;//收到了新的广播
	public static final int MAIN_LOGIN_OUT_VALUE = 0x1010;//注销了用户
	public static final int MAIN_BROADCAST_DELAY_LOAD_DATA_VALUE = 0x1011;//延迟一个时间去加载数据
	public static final int MAIN_ADV_ACTION_URL_VALUE = 0x1012;//程序主页的广告url
	public static final int MAIN_BROADCAST_ALL_LIST_SUCCESS = 0X1013;//获取所有广播成功
	public static final int MAIN_BROADCAST_TOP_LIST_SUCCESS = 0x1014;//获取置顶广播成功
	public static final int MAIN_BROADCAST_GAME_LIST_SUCCESS = 0x1015;//获取游戏场成功
	public static final int MAIN_BROADCAST_ALL_LIST_FAILED = 0X1016;//获取所有广播失败
	public static final int MAIN_BROADCAST_TOP_LIST_FAILED = 0x1017;//获取置顶广播失败
	public static final int MAIN_BROADCAST_GAME_LIST_FAILED = 0x1018;//获取游戏场失败
	public static final int MAIN_BROADCAST_APPEND_ALL_DATA = 0x1019;//追加广播数据
	public static final int MAIN_BROADCAST_APPEND_TOP_DATA = 0x1020;//追加置顶数据
	public static final int MAIN_BROADCAST_APPEND_GAME_DATA = 0x1021;//追加游戏场数据
	public static final int MAIN_BROADCAST_APPEND_MY_DATA = 0x1022;//追加我的广播
	public static final int MAIN_BROADCAST_APPEND_ABOUT_ME_DATA = 0x1023;//@我的广播
	public static final int MAIN_BROADCAST_MY_DATA_LIST = 0X1024;//我的广播列表
	public static final int MAIN_BROADCAST_ABOUT_ME_LIST = 0X1025;//@我的广播列表
	public static final int MAIN_BROADCAST_TOP_ADV_VALUE = 0X1026;//秀场广告位
	public static final int MAIN_BROADCAST_MAGIC_VALUE = 0x1027;//仙术回放
	public static final int MAIN_BROADCAST_NO_ADD_MAGIC_VALUE = 0x1028;//流星雨仙术没有@用户
	/**
	 * 个人主页的值
	 */
	public static final int SPACE_ALBUM_PHOTO_LIST_VALUE = 0x1050;//获取公开的相片
	public static final int SPACE_USER_INFO_VALUE = 0x1051;//获取用户基本信息
	public static final int SPACE_USER_PROPERTY_VALUE = 0x1052;//获取用户的财富值，包括魅力，积分
	public static final int SPACE_MY_FOLLOW_LIST_VALUE = 0x1053;//获取男人态我的关注动态
	public static final int SPACE_GET_TOPIC_COUNT_VALUE = 0x1054;//获取帖子的计数
	public static final int SPACE_GET_RELATIONSHIP_VALUE = 0x1055;//获取和该用户的关系
	public static final int SPACE_GET_MYSKILL_LIST_VALUE = 0x1056;//获取我的技能列表
	public static final int SPACE_TOPIC_LIST_VALUE = 0x1057;//获取动态列表
	public static final int SPACE_ADD_FOLLOW_VALUE = 0x1058;//获取动态列表
	public static final int SPACE_UPLOAD_BG_VALUE = 0x1059;//上传背景
	public static final int SPACE_DELETE_TOPICINFO_VALUE = 0x1060;//删除帖子回来
	public static final int SPACE_APPRECITATE_VALUE = 0x1061;//点赞加一
	public static final int SPACE_REPLAYCOUNT_VALUE = 0x1062;//回复加一
	public static final int SPACE_QUERY_LOCAL_TOPIC_VALUE = 0x1063;//查询本地发送失败的数据
	public static final int SPACE_DELETE_REPLY_VALUE = 0x1064;//删除二级回复
	public static final int SPACE_REDUCE_REPLY_COUNT_VALUE = 0x1065;//删除了回复，回复数要减一
	public static final int SPACE_REPORT_SKILL_VALUE = 0x1066;//举报技能
	public static final int SPACE_USER_FANSE_VALUE = 0x1067;//获取访问量和粉丝量
	public static final int SPACE_SPECIAL_VALUE = 0x1068;//这也特效
	public static final int USER_DISTANCE_VALUE = 0x1069;//用户间的距离

	/**
	 * 发送广播的值
	 */
	public static final int BRAODCAST_USERWEALTH_SUCCESS_VALUE = 0x1100;//用户的财富值成功
	public static final int BROADCAST_SEND_SUCCESS_VALUE = 0x1101;//发送广播成功
	public static final int BROADCAST_TIME_LENGTH_VALUE = 0x1102;//计时录音的长度
	public static final int BROADCAST_VOICE_CALL_STATE_RINGING_VALUE = 0x1103;//等待接电话
	public static final int BROADCAST_VOICE_CALL_STATE_IDLE_VALUE = 0x1104;//电话挂断
	public static final int BROADCAST_VOICE_CALL_STATE_OFFHOOK = 0X1105;//电话通话中
	public static final int BROADCAST_VOIDE_LOAD_COMPLETE_PLAY_VALUE = 0x1106;//广播下载完成，开始听了
	public static final int BROADCAST_VOIDE_PB_GONE_VALUE = 0x1107;//把语音播放进度条去掉
	public static final int BROADCAST_SHOW_ANIMATION_VALUE = 0x1108;//秀的连刷动画大厅显示
	public static final int BROADCAST_SHOW_ANIMATION_CONTINUE_VALUE = 0x1109;//秀的连刷动画大厅显示

	/**
	 * 封禁
	 * 
	 */
	public static final int WRITE_OPERATE_FORBIT = 0x1150;//封禁

	/**
	 * 聊天视频
	 */
	public static final int CHAT_VEDIO_CUT_VALUE = 0x1200;//视频截取
	public static final int CHAT_VEDIO_CUT_SUCCESS_VALUE = 0x1201;//视频截取成功
	public static final int CHAT_VEDIO_CUT_FAILED_VALUE = 0x1202;//视频截取失败
	public static final int CHAT_VEDIO_COMPRESSION_VALUE = 0X1203;//视频压缩
	public static final int CHAT_VEDIO_COMPRESSION_SUCCESS_VALUE = 0x1204;//压缩成功
	public static final int CHAT_VEDIO_COMPRESSION_FAILED_VALUE = 0x1205;//压缩失败
	public static final int CHAT_VEDIO_SENDING_VALUE = 0x1206;//正在上传
	public static final int CHAT_VEDIO_SEND_SUCCESS_VALUE = 0x1207;//视频上传成功
	public static final int CHAT_VEDIO_SEND_FAILED_VALUE = 0x1208;//视频上传失败
	public static final int CHAT_VEDIO_SDCARD_ALL_VALUE = 0x1209;//搜索SD卡所有的视频文件
	public static final int CHAT_VIDEO_SHOW_LIST_VALUE = 0x1210;//展示视频列表
	public static final int CHAT_VIDEO_MY_VALUE = 0x1211;//我的视频列表
	public static final int CHAT_VEDIO_SEND_APPEND_VALUE = 0x1212;//聊天添加视频数据
	public static final int CHAT_VEDIO_DOWNLOAD_SUCCESS_VALUE = 0x1213;//视频下载成功
	public static final int CHAT_VEDIO_DOWNLOAD_FAILED_VALUE = 0x1214;//视频下载失败
	public static final int CHAT_VEDIO_DOWNLOAD_CLICK_VALUE = 0x1215;//点击视频下载
	public static final int CHAT_CLEAR_CONTENT_INFO_SUCCESS_VALUE = 0x1216;//清除聊天信息
	public static final int CHAT_ADD_BLACK_LIST_VALUE = 0x1217;//加入黑名单
	public static final int CHAT_REFLESH_UI_VALUE = 0x1218;//通知刷新消息列表
	public static final int CHAT_APPEND_DATA_VALUE = 0x1219;//在聊天列表里面添加一条消息
	public static final int CHAT_WEALTH_NOT_ENGOUH_VALUE = 0x1220;//发送赌博猜拳财富不够
	public static final int CHAT_REMOVE_GUESS_FINGER_VALUE = 0x1221;//猜拳数据成功或者回复失败移除数据
	public static final int CHAT_GUESS_FINGER_TIME_OUT_VALUE = 0x1222;//猜拳超时失效
	public static final int CHAT_GROUP_GUESS_REPLY_SUCCESS_VALUE = 0x1223;//我抢到了的猜拳 
	public static final int CHAT_LOAD_HISTORY_DATA_VALUE = 0x1224;//加载更多的聊天数据
	public static final int CHAT_LOAD_HISTORY_NO_DATA_VALUE = 0x1225;//没有更多的数据了
	public static final int CHAT_SKILL_ORDER_FAILED = 0x1226;//聊天界面内拒绝或者接受失败
	public static final int CHAT_SKILL_ORDER_TIME_OUT_VALUE = 0x1227;//该订单已失效
	public static final int CHAT_RECEIVER_NEW_MESSAGE_SUCCESS_VALUE = 0x1228;//在聊天界面收到新消息 
	public static final int CHAT_FORBIT_MESSAGE_VALUE = 0x1229;//被禁言了
	public static final int CHAT_MONEY_NOT_ENGOUG_VALUE = 0x1230;//财富不足
	public static final int CHAT_MESSAGE_ORDER_TIME_VALUE = 0x1231;//会话列表改变
	public static final int CHAT_SMILE_VOICE_PLAY_ERROR_VALUE = 0X1232;//后宫语音播放错误
	public static final int CHAT_LOCAL_MESSAGE_LIST_VALUE = 0x1233;//获取本地聊天数据
	public static final int CHAT_REPORT_VALUE = 0x1234;//举报
	public static final int CHAT_OFFLINE_MESSAGE_START_VALUE = 0x1235;//离线消息开始
	public static final int CHAT_OFFLINE_MESSAGE_END_VALUE = 0x1236;//离线消息结束
	public static final int CHAT_RECEIVE_ONLINE_MESSAGE_NEW = 0x1237;//收到了新的消息
	public static final int CHAT_SYSTEM_NOTICE_SHARE_VALUE = 0X1238;//系统通知分享
	public static final int CHAT_DARE_INFO_SEND = 0X1239;//发起大冒险
	public static final int CHAT_DARE_INFO_ANIM = 0X123A;//大冒险骰子动画
	public static final int CHAT_DARE_SEND_TEXT = 0X123B;//大冒险文字
	public static final int CHAT_DARE_SEND_RESULT = 0X123C;//大冒险返回值
	public static final int CHAT_DARE_DIALOG_SHOW = 0X123D;//大冒险对话框
	public static final int CHAT_DARE_SEND_RESULT_CODE = 0X123E;//大冒险返回码
	public static final int CHTA_DARE_PASS_RESULT_CODE = 0X1240;//大冒险求通过
	public static final int CHAT_DARE_RPS_GRADE = 0X1241;//猜拳等级不够
	public static final int CHAT_ANONYM_NICK_PAST = 0x1242;//昵称过期

	/**
	 * 后宫
	 */
	public static final int HAREM_CREATE_SUCCESS_VALUE = 0x1250;//创建后宫成功
	public static final int HAREM_CREATE_FAILED_VALUE = 0x1251;//创建后宫失败
	public static final int HAREM_CREATE_LEVER_LOWER_FAILED_VALUE = 0X1252;//等级不够不能够创建
	public static final int HAREM_CREATE_LEVER_GROUP_LIMIT_FAILED_VALUE = 0x1253;//已达群上限
	public static final int HAREM_CREATE_MEMBERS_LIMIT_FAILED_VALUE = 0x1254;//群成员已达上限
	public static final int HAREM_GET_GROUP_LIST_SUCCESS_VALUE = 0x1255;//获取群列表成功
	public static final int HAREM_GET_GROUP_LIST_FAILED_VALUE = 0x1256;//获取群列表失败
	public static final int HAREM_GET_GROUP_MEMBER_LIST_SUCCESS_VALUE = 0x1257;//获取群成员列表成功
	public static final int HAREM_GET_GROUP_MEMBER_LIST_FAILED_VALUE = 0x1258;//获取群成员列表失败
	public static final int HAREM_UPDATE_GROUP_INFO_SUCCESS_VALUE = 0x1259;//修改群信息成功
	public static final int HAREM_UPDATE_GROUP_INFO_FAILED_VALUE = 0x1260;//修改群信息失败
	public static final int HAREM_JOIN_GROUP_SUCCESS_VALUE = 0x1261;//申请加入后宫成功
	public static final int HAREM_JOIN_GROUP_FAILED_VALUE = 0x1262;//申请加入后宫失败
	public static final int HAREM_JOIN_GROUP_REPEAT_JOIN_VALUE = 0x1263;//已经加入过后宫
	public static final int HAREM_AGREE_GROUP_SUCCESS_VALUE = 0x1264;//同意加入后宫成功
	public static final int HAREM_AGREE_GROUP_HAS_JOIN_VALUE = 0x1265;//已经是群成员
	public static final int HAREM_AGREE_GROUP_ARRAIVE_LIMIT_VALLUE = 0x1266;//已经到群人数上限
	public static final int HAREM_AGREE_GROUP_FAILED_VALUE = 0x1267;//同意加入后宫失败
	public static final int HAREM_AGREE_GROUP_IGNORE_VALUE = 0x1268;//忽略加入本群
	public static final int HAREM_GROUP_QUIT_SUCCESS_VALUE = 0x1269;//退出群成功
	public static final int HAREM_GROUP_QUIT_FAILED_VALUE = 0x1270;//退出群失败
	public static final int HAREM_GROUP_IS_OWNER_VALUE = 0x12671;//是否是群主
	public static final int HAREM_GROUP_MEMBER_IN_OR_OUT_VALUE = 0X1272;//群成员进出提醒
	public static final int HAREM_GROUP_INFO_SUCCESS_VALUE = 0x1273;//群消息详情成功
	public static final int HAREM_GROUP_INFO_FAILED_VALUE = 0x1274;//群消息失败

	/**
	 * 广播播放动画
	 */
	public static final int BROADCAST_BIG_GIFT = 0x1300;//大礼包
	public static final int BROADCAST_CASTLE = 0x1301;//城堡
	public static final int BROADCAST_ADD = 0x1302;//添加
	public static final int BROADCAST_GIFT_END = 0x1305;//结束
	public static final int BROADCAST_DECREE_VALUE = 0x1306;//圣旨或者懿旨动画
	public static final int BROADCAST_ROSE_VALUE = 0x1307;//99玫瑰动画
	public static final int BROADCAST_FIREWORD_VALUE = 0x1308;//99烟花动画
	public static final int BROADCAST_STARFALLFLAKE_VALUE = 0x1309;//仙术流星雨
	public static final int BROADCAST_FEATHER_VALUE = 0x1310;//红毛雨
	public static final int BROADCAST_ARROW_VALUE = 0x1311;//万剑阵
	public static final int BROADCAST_ROSE_RAIN_VALUE = 0x1312;//玫瑰花雨
	public static final int BROADCAST_FIVE_VALUE = 0x1313;//一箭种情
	public static final int BROADCAST_SIX_VALUE = 0x1314;//变变变
	public static final int BROADCAST_SEVEN_VALUE = 0x1315;//真爱永恒
	public static final int BROADCAST_EIGHT_VALUE = 0x1316;//烈焰红唇
	public static final int BROADCAST_NINE_VALUE = 0x1317;//天马流星拳
	public static final int BROADCAST_TEN_VALUE = 0x1318;//甜蜜热气球
	public static final int BROADCAST_CROWN_VALUE = 0x1319;//女王之冠
	public static final int BROADCAST_CHRISTMAS_VALUE = 0x1320;//圣诞节
	public static final int BROADCAST_YUANDAN_VALUE = 0x1321;//元旦节
	public static final int BROADCAST_REDPACKET = 0x1322;//红包雨
	public static final int BROADCAST_FIRE_VALUE = 0x1323;//鞭炮
	public static final int BROADCAST_COUPLET_VALUE = 0x1324;//对联
	public static final int BROADCAST_HAPPY_NEW_YEAR_VALUE = 0x1325;//猴子拜年
	public static final int BROADCAST_ARCHWAY_VALUE = 0x1326;//烟花
	public static final int BROADCAST_FLOWER_SEA_VALUE = 0x1327;//花海

	/**
	 * 大厅数据
	 */
	public static final int HALL_GET_ONTIME_LIST_MALE_SUCCESS_VALUE = 0x1350;//获取大厅男在线用户成功
	public static final int HALL_GET_ONTIME_LIST_MALE_FAILED_VALUE = 0x1351;//获取大厅男在线用户失败
	public static final int HALL_GET_ONTIME_LIST_FEMALE_SUCCESS_VALUE = 0x1352;//获取大厅女在线用户成功
	public static final int HALL_GET_ONTIME_LIST_FEMALE_FAILED_VALUE = 0x1353;//获取大厅女在线用户失败
	public static final int HALL_GET_NEW_MALE_LIST_SUCCESS_VALUE = 0x1354;//获取大厅男在线用户成功
	public static final int HALL_GET_NEW_MALE_LIST_FAILED_VALUE = 0x1355;//获取大厅男在线用户失败
	public static final int HALL_GET_NEW_FEMALE_LIST_SUCCESS_VALUE = 0x1356;//获取大厅女在线用户成功
	public static final int HALL_GET_NEW_FEMALE_LIST_FAILED_VALUE = 0x1357;//获取大厅女在线用户失败
	public static final int HALL_GET_SKILL_LIST_SUCCESS_VALUE = 0x1358;//获取大厅技能列表成功
	public static final int HALL_GET_SKILL_LIST_FAILED_VALUE = 0x1359;//获取大厅技能列表失败
	public static final int HALL_GET_ONTIME_LIST_CACHE_DATA_SUCCESS_VALUE = 0x1360;//获取大厅缓存数据成功了
	public static final int HALL_GET_NEAR_LIST_MALE_SUCCESS_VALUE = 0x1361;//获取大厅附近男用户成功
	public static final int HALL_GET_NEAR_LIST_MALE_FAILED_VALUE = 0x1362;//获取大厅附近男用户失败
	public static final int HALL_GET_NEAR_LIST_FEMALE_SUCCESS_VALUE = 0x1363;//获取大厅附近女用户成功
	public static final int HALL_GET_NEAR_LIST_FEMALE_FAILED_VALUE = 0x1364;//获取大厅附近女用户失败

	/**
	 * 会话列表消息
	 */
	public static final int SESSION_CHAT_LIMET = 0x1370;//设置聊天限制
	public static final int SESSION_CHAT_BLACK = 0x1371;//我的黑名单
	public static final int SESSION_CHAT_DELETE = 0x1372;//删除

	/**
	 * 黑名单列表
	 */
	public static final int BLACK_LIST_SUCCESS = 0x1380;//拉取黑名单列表成功
	public static final int BLACK_LIST_FAILED = 0x1381;//拉取黑名单列表失败
	public static final int BLACK_LIST_REMOVE = 0x1382;//删除黑名单

	public static final int ACCOUNT_VERIFY_RESULT = 0x1390;//验证邀请码

	/**
	 * 红包
	 */
	public static final int RED_PACKET_CREATE_SUCCESS_VALUE = 0x1400;//创建成功
	public static final int RED_PACKET_CREATE_FAILED_VALUE = 0x1401;//创建失败
	public static final int RED_PACKET_GET_TOTAL_COIN_VALUE = 0x1402;//获取我的金币数量
	public static final int RED_PACKET_UNPAKCET_SUCCESS_VALUE = 0x1403;//领取红包成功
	public static final int RED_PACKET_UNPACKET_HAVE_RECEIVER_SUCCESS_VALUE = 0x1404;//领取过红包
	public static final int RED_PACKET_UNPACKET_FAILED_VALUE = 0x1405;//领取失败
	public static final int RED_PACKET_UNPACKET_TIMEOUT_VALUE = 0x1406;//红包超时失效
	public static final int RED_PACKET_UNPACKET_NO_MONEY_VALUE = 0X1407;//红包已经被领完了
	public static final int RED_PACKET_GET_REDPACKET_DETAIL_SUCCESS_VALUE = 0x1408;//获取红包详情成功
	public static final int RED_PACKET_GET_REDPACKET_DETAIL_FAILED_VALUE = 0x1409;//获取红包详情失败
	public static final int RED_PACKET_GET_DELIVER_REDPACKET_LIST_SUCCESS_VALUE = 0x1410;//获取我发的红包成功
	public static final int RED_PACKET_GET_DELIVER_REDPACKET_LIST_FAILED_VALUE = 0x1411;//获取我发的红包失败
	public static final int RED_PACKET_CREATE_OUT_OF_NUM_VALUE = 0x1412;//创建红包的份数大于宫成员人数
	public static final int RED_PACKET_ABOVE_NORM = 0x1413;//红包额度超限

	/**
	 * 技能
	 */
	public static final int SKILL_INTERESTIN_JOIN_SUCCESS_VALUE = 0x1450;//感兴趣技能成功
	public static final int SKILL_INTERESTIN_JOIN_FAILED_VALUE = 0x1451;//感兴趣技能失败
	public static final int SKILL_INTERESTIN_JOIN_REPEAT_VALUE = 0x1452;//感兴趣技能已经留言了
	public static final int SKILL_GET_SKILL_INTERESTIN_LIST_SUCCESS_VALUE = 0x1453;//获取感兴趣列表成功
	public static final int SKILL_GET_SKILL_INTERESTIN_LIST_FAILED_VALUE = 0x1454;//获取感兴趣列表失败
	public static final int SKILL_ADD_SKILL_DEAL_SUCCESS_VALUE = 0x1455;//男人下单成功
	public static final int SKILL_ADD_SKILL_DEAL_MONEY_NOT_ENGOUH = 0x1456;//钱不够
	public static final int SKILL_ADD_SKILL_DEAL_FAILED_VALUE = 0x1457;//男人下单失败
	public static final int GIFT_LIST_SUCCESS_VALUE = 0X1458;//获取礼物列表成功
	public static final int GIFT_LIST_FAILED_VALUE = 0X1459;//获取礼物列表成功
	public static final int GIFT_CHAT_LIMIT_SUCCESS_VALUE = 0x1460;//设置忠诚度成功
	public static final int GIFT_CHAT_LIMIT_FAILED_VALUE = 0x1461;//设置忠诚度失败

	/**
	 * 技能列表
	 */
	public static final int SKILL_DEAL_LIST_RESULT = 0x1500;//获取技能列表
	public static final int SKILL_DEAL_ORDER_OPERATE = 0x1501;//订单操作，确认或拒绝
	public static final int SKILL_DEAL_ORDER_RECLAINGIFT = 0x1502;//退礼物
	public static final int SKILL_DEAL_ORDER_APPEAL = 0x1503;//申诉
	public static final int SKILL_DEAL_ORDER_POINT = 0x1504;//申诉

	/**
	 * 微信登录
	 */
	public static final int WX_LOGIN_RESULT = 0x1550;
	public static final int WX_LOGIN_GET_USERINFO = 0x1551;

	/**
	 * 绑定手机号
	 */
	public static final int BIND_GET_PHONE = 0x1560;//获取验证码
	public static final int BIND_PHONE_RESULT = 0x1561;//绑定手机返回值
	public static final int BIND_GET_PHONE_RESULT = 0x1562;//获取验证码返回值
	public static final int BIND_PHONE_REGISTER_RESULT = 0x1563;//判断验证码是否正确

	/**
	 * 验证特权
	 */
	public static final int BROADCAST_COLOR_PRIVILEGE_STATUS_SUCCESS_VALUE = 0x1600;//颜色特权
	public static final int BROADCAST_COLOR_PRIVILEGE_STATUS_LOWER_VALUE = 0x1601;//颜色权限不足
	public static final int BROADCAST_DECREE_PRIVILEGE_STATUS_SUCCESS_VALUE = 0x1602;//圣旨特权
	public static final int BROADCAST_DECREE_PRIVILEGE_STATUS_LOWER_VALUE = 0x1603;//圣旨权限不足
	public static final int BROADCAST_DECREE_PRIVILEGE_STATUS_NO_MORE_VALUE = 0x1604;//圣旨不能够多发
	public static final int BROADCAST_DECREE_ANIMATION_VALUE = 0x1605;//圣旨展开动画
	public static final int BROADCAST_MAGIC_LOWER_VALUE = 0x1606;//不能够发送仙术
	public static final int BROADCAST_MAGIC_VALUE = 0x1607;//能够发送仙术
	/**
	 * SD卡缓存的数据
	 */
	public static final int CACHE_RANK_GAME_VALUE = 0x1650;//游戏排行榜缓存值
	public static final int CACHE_RANK_HAREM_VALUE = 0x1651;//后宫排行榜缓存值
	public static final int CACHE_RANK_NEW_FEMALE_VALUE = 0x1652;//新人女排行榜缓存值
	public static final int CACHE_RANK_NEW_MALE_VALUE = 0x1653;//新人女排行榜缓存值
	public static final int CACHE_RANK_CHARM_DAY_VALUE = 0x1654;//魅力日榜
	public static final int CACHE_RANK_CHARM_WEEK_VALUE = 0x1655;//魅力周榜
	public static final int CACHE_RANK_CHARM_TOTAL_VALUE = 01656;//魅力总榜
	public static final int CACHE_RANK_WEALTH_DAY_VALUE = 0x1657;//财富日榜
	public static final int CACHE_RANK_WEALTH_WEEK_VALUE = 0x1658;//财富周榜
	public static final int CACHE_RANK_WEALTH_TOTAL_VALUE = 0X1659;//财富总榜
	public static final int CACHE_BRAODCAST_ALL_VALUE = 0x1660;//全部广播缓存
	public static final int CACHE_BROADCAST_TOP_VALUE = 0x1661;//置顶广播
	public static final int CACHE_HALL_USER_VALUE = 0x1662;//大厅的缓存文件
	public static final int CACHE_HALL_REFRESH_VALUE = 0x1663;//重新加载刷新文件
	public static final int CACHE_BROADCAST_GAME_VALUE = 0x1664;//广播游戏数据

	/**
	 * 修改备注
	 */
	public static final int ALIAS_UPDATE_RESULT = 0x1700;//修改备注

	public static final int SET_PWD_BY_PHONE = 0x1710;//找回手机密码
	public static final int CLEAN_DYNAMIC_VALUE = 0x1711;//清空动态
	public static final int GET_TOPIC_COUNTER = 0x1712;//获取计数
	public static final int GET_TITLE_FOR_HTML = 0x1713;//从网页获取标题
	public static final int RESULT_UPDATE_BY_WEBVIEW = 0x1714;//通过webview上传数据返回
	public static final int RESULT_DOWNLOAD_BY_WEBVIEW = 0x1715;//通过webview下载文件

	public static final int REPOPRT_SUCCESS_VALUE = 0X1750;//举报成功
	public static final int REPOPRT_FAILED_VALUE = 0X1751;//举报失败

	public static final int GET_MY_FANS_LIST_VALUE = 0x1800;//获取我的粉丝列表
	public static final int GET_MY_FANS_LIST_FAILED_VALUE = 0x1801;//获取我的粉丝列表
	public static final int GET_MY_FOLLOWS_LIST_VALUE = 0x1802;//获取我的关注列表
	public static final int GET_MY_FOLLOWS_LIST_FAILED_VALUE = 0x1803;//获取我的关注列表
	public static final int DELETE_MY_FOLLOWS_VALUE = 0x1804;//取消关注

	public static final int LOOK_ME_NUM = 0x1805;//看过我的

	/**
	 * 秀场
	 */
	public static final int SHOW_TITLE_HINT = 0x1810;//秀场标题隐藏
	public static final int SHOW_ROOM_LISTS_VALUE = 0x1811;//秀场房间
	public static final int SHOW_ROOM_OPEN_SHOW = 0x1812;//开秀
	public static final int SHOW_ROOM_CHAT_BACK = 0x1813;//发送内容
	public static final int SHOW_ROOM_GET_MEMBER_LIST = 0x1814;//获取成员列表
	public static final int SHOW_ROOM_GET_DEVOTERANK = 0x1815;//获取贡献值
	public static final int SHOW_ROOM_GET_HISTORDATA = 0x1816;//获取秀场历史记录
	public static final int SHOW_ROOM_IN_OUT = 0x1817;//进出房间
	public static final int SHOW_ROOM_VOICE_ISEXITS = 0x1818;//语音文件已存在
	public static final int SHOW_ROOM_VOICE_NEEDLOAD = 0x1819;//语音文件需要下载
	public static final int SHOW_ROOM_GIFT_LIST = 0x1820;//秀场礼物列表
	public static final int SHOW_ROOM_MEMBER_ROLE = 0x1821;//秀场设置嘉宾
	public static final int SHOW_ROOM_SEND_GIFT = 0x1822;//秀场送礼物
	public static final int SHOW_ROOM_PUSH_MESSAGE = 0x1823;//秀场推送消息
	public static final int SHOW_ROOM_PUSH_VOICE = 0x1824;//秀场推送语音
	public static final int SHOW_ROOM_PUSH_ROLE = 0x1825;//秀场角色改变
	public static final int SHOW_ROOM_PUSH_ROOMSINFO = 0x1826;//秀场信息改变
	public static final int SHOW_ROOM_SERVER_PLAY_VOICE = 0x1827;//秀场语音直接播放
	public static final int SHOW_ROOM_ADD_HOT = 0x1828;//点赞
	public static final int SHOW_ROOM_HOT_TIME = 0x1829;//点赞时间
	public static final int SHOW_ROOM_CLOSE = 0x1830;//关闭秀场
	public static final int SHOW_ROOM_DELATESHOW = 0x1831;//举报秀场
	public static final int SHOW_ROOM_GET_SINGLE_ROOM = 0x1832;//举报秀场
	public static final int SHOW_ROOM_PUSH_ROOMSINFO1 = 0x1833;//秀场信息改变
	public static final int SHOW_ROOM_LONG_CLICK = 0x1834;//秀场长按
	public static final int SHOW_ROOM_VOICE_CLOSE = 0x1835;//秀场语音关闭
	public static final int SHOW_ROOM_GIFT_HISTORY = 0x1836;//秀场礼物动态
	public static final int SHOW_ROOM_OPEN_BOX = 0x1837;//秀场宝箱
	public static final int SHOW_ROOM_OPEN_BOX_RESULT = 0x1838;//秀场宝箱
	public static final int SHOW_ROOM_OPEN_BOX_RESULT_PIC = 0x1839;//秀场宝箱图片
	public static final int SHOW_ROOM_OPEN_BOX_RESULT_CLOSE = 0x1840;//秀场宝箱图片
	public static final int SHOW_ROOM_OPEN_BOX_TIPS = 0x1841;//秀场宝箱图片

	/**
	 * 清理缓存
	 */
	public static final int CLEAR_MEMORY_VALUE = 0x1850;

	/**
	 * 真心话
	 */
	public static final int CHAT_TRUTH_SEND_SUCCESS_RESULT_CODE = 0x1903;//真心成功
	public static final int CHAT_TRUTH_SEND_ERROR_RESULT_CODE = 0X1900;//真心话错误
	public static final int CHAT_TRUTH_ANSWER_SUCCESS_RESULT_CODE = 0x1901;//发送真心话答案成功
	public static final int CHAT_TRUTH_ANSWER_ERROR_RESULT_CODE = 0x1902;//发送真心话失败

	/**
	 * 新的动态
	 */
	public static final int IMG_TEXT_VIEWPAGER_ITEM_CLICK = 0x1910;//配图viewPager item点击
	public static final int IMG_CLIP_SUCCESS = 0x1911;//图片裁剪成功
	public static final int PUBLISH_DYNAMIC_SUCCESS = 0x1912;//发贴成功
	public static final int PUBLISH_DYNAMIC_ERROR = 0x1913;//发贴失败
	public static final int GET_DYNAMIC_SUCCESS = 0x1914;//获取动态成功
	public static final int GET_DYNAMIC_ERROR = 0x1915;//获取动态失败
	public static final int GET_DYNAMIC_REPLY_SUCCESS = 0x1916;//获取动态回复成功
	public static final int GET_DYNAMIC_REPLY_ERROR = 0x1917;//获取动态回复失败
	public static final int DYNAMIC_REPLY_SUCCESS = 0x1918;//评论成功
	public static final int DYNAMIC_REPLY_ERROR = 0x1919;//评论失败
	public static final int DYNAMIC_REPLY2_SUCCESS = 0x1920;//评论成功
	public static final int DYNAMIC_REPLY2_ERROR = 0x1921;//评论失败
	public static final int DYNAMIC_DELETE_SUCCESS = 0x1922;//删除动态成功
	public static final int DYNAMIC_DELETE_ERROR = 0x1923;//删除动态失败
	public static final int DYNAMIC_OFFICIAL_SUCCESS = 0x1924;//获取官方动态成功
	public static final int DYNAMIC_OFFICIAL_ERROR = 0x1925;//获取官方动态失败
	public static final int GET_ABOUT_DYNAMIC_SUCCESS = 0x1926;//获取关于我回复成功
	public static final int GET_ABOUT_DYNAMIC_ERROR = 0x1927;//获取关于我回复失败

	//悬赏
	public static final int GET_REWARD_SKILL_SUCCESS = 0x1930;//获取悬赏技能成功
	public static final int GET_REWARD_SKILL_ERROR = 0x1931;//获取悬赏技能失败
	public static final int GET_REWARD_GIFT_SUCCESS = 0x1932;//获取悬赏礼物成功
	public static final int GET_REWARD_GIFT_ERROR = 0x1933;//获取悬赏实物失败
	public static final int GET_REWARD_TIP_SUCCESS = 0x1934;//获取悬赏提示语成功
	public static final int GET_REWARD_TIP_ERROR = 0x1935;//获取悬赏提示语失败
	public static final int PULISH_REWARD_SUCCESS = 0x1936;//发布悬赏成功
	public static final int PULISH_REWARD_ERROR = 0x1937;//发布悬赏失败
	public static final int GET_REWARD_LIST_SUCCESS = 0x1938;//获取悬赏成功
	public static final int GET_REWARD_LIST_ERROR = 0x1939;//获取悬赏失败
	public static final int GET_JOIN_REWARD_LIST_SUCCESS = 0x1940;//参加悬赏回调成功
	public static final int GET_JOIN_REWARD_LIST_ERROR = 0x1941;//参加悬赏回调失败
	public static final int GET_MINE_REWARD_LIST_SUCCESS = 0x1942;//发布悬赏回调成功
	public static final int GET_MINE_REWARD_LIST_ERROR = 0x1943;//发布悬赏回调失败
	public static final int GET_WIN_REWARD_LIST_SUCCESS = 0x1944;//赢得悬赏回调成功
	public static final int GET_WIN_REWARD_LIST_ERROR = 0x1945;//赢得悬赏回调失败
	public static final int JOIN_REWARD_SUCCESS = 0x1946;//参加悬赏回调成功
	public static final int JOIN_REWARD_ERROR = 0x1947;//参加悬赏回调失败
	public static final int REWARD_CHAT_SUCCESS = 0x1948;//请求私聊成功
	public static final int REWARD_CHAT_ERROR = 0x1949;//请求私聊失败
	public static final int GET_PARTICIPATOR_SUCCSS = 0x1950;//请求参与悬赏成功
	public static final int GET_PARTICIPATOR_ERROR = 0x1951;//请求参悟悬赏失败
	public static final int PARTICIPATOR_SUCCESS = 0x1952;//悬赏成功
	public static final int PARTICIPATOR_ERROR = 0x1953;//悬赏失败

	//女神技
	public static final int GET_GODDESS_SKILL_LIST_SUCCESS = 0x2001;//获取技能列表成功
	public static final int GET_GODDESS_SKILL_LIST_ERROR = 0x2002;//获取技能列表失败
	public static final int GET_SAVE_SKILL_LIST_SUCCESS = 0x2003;//保存技能列表成功
	public static final int GET_SAVE_SKILL_LIST_ERROR = 0x2004;//保存技能列表失败
	public static final int GET_PERSON_GODDESS_SKILL_INFO_SUCCESS = 0x2005;//获取个人技能列表成功
	public static final int GET_PERSON_GODDESS_SKILL_INFO_ERROR = 0x2006;//获取个人技能列表失败
	public static final int GET_GODDESS_SKILL_GIFT_LIST_SUCCESS = 0x2007;//获取技能礼物成功
	public static final int GET_GODDESS_SKILL_GIFT_LIST_ERROR = 0x2008;//获取技能礼物失败
	public static final int GET_GODDESS_SKILL_TIP_INFO_SUCCESS = 0x2009;//获取技能邀请提示成功
	public static final int GET_GODDESS_SKILL_TIP_INFO_ERROR = 0x2010;//获取技能邀请提示失败
	public static final int GET_GODDESS_SKILL_INVITE_SUCCESS = 0x2011;//发出邀请成功
	public static final int GET_GODDESS_SKILL_INVITE_ERROR = 0x2012;//发出邀请失败
	public static final int GET_GODDESS_SKILL_INVITE_RES_SUCCESS = 0x2013;//回应技能邀请成功
	public static final int GET_GODDESS_SKILL_INVITE_RES_ERROR = 0x2014;//回应技能邀请失败
	public static final int UPDATE_GODDESS_SKILL_INVITE_TIME = 0x2015;//更新技能邀请时间
	public static final int ADD_GODDESS_SKILL = 0x2016;//选中女神技
	public static final int DELETE_GODDESS_SKILL = 0x2017;//删除女神技\
	public static final int PULL_REFRESH_GODDESS_SKILL_COMPLEMENT = 0x2018;//获取女神技能完成

	//匿名悬赏
	public static final int BIND_ANONYM_NICK_REWARD_SUCCESS = 0x3000;//检测匿名绑定成功
	public static final int BIND_ANONYM_NICK_REWARD_ERROR = 0x3001;//检测匿名绑定失败
	public static final int GET_ANONYM_NICK_SUCCESS = 0x3002;//获取匿名Nick成功
	public static final int GET_ANONYM_NICK_ERROR = 0x3003;//获取匿名Nick失败
	public static final int USER_ANONYM_NICK_SUCCESS = 0x3004;//使用匿名Nick失败
	public static final int USER_ANONYM_NICK_ERROR = 0x3005;//使用匿名Nick失败
	public static final int BIND_ANONYM_NICK_SUCCESS = 0x3006;//匿名绑定成功

	//后宫接龙红包
	public static final int HAREM_SOLITAIRE_REDPACKET_INFO_SUCCESS = 0x3050; //获取后宫发送接龙红包信息成功
	public static final int HAREM_SOLITAIRE_REDPACKET_INFO_ERROR = 0x3051; //获取后宫发送接龙红包信息失败
	public static final int HAREM_SEND_SOLITAIRE_REDPACKET_SUCCESS = 0x3052; //获取后宫发送接龙红包信息成功
	public static final int HAREM_SEND_SOLITAIRE_REDPACKET_ERROR = 0x3053; //获取后宫发送接龙红包信息成功
	public static final int HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS = 0x3054; //检查红包状态
	public static final int HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS_SUCCESS = 0x3055; //检查红包状态
	public static final int HAREM_CHECK_SOLITAIRE_REDPACKET_STATUS_ERROR = 0x3056; //检查红包状态
	public static final int HAREM_GRAB_SOLITAIRE_REDPACKET_ENSURE = 0x3057; //抢红包确认
	public static final int HAREM_GRAB_SOLITAIRE_REDPACKET_SUCCESS = 0x3058; //抢红包成功
	public static final int HAREM_GRAB_SOLITAIRE_REDPACKET_ERROR = 0x3059; //抢红包失败
	public static final int HAREM_UPDATE_CAN_GRAB_REDPACKET = 0x3060; //更新可以抢的红包数量
	
	//大厅红包接龙
	public static final int HALL_CHECK_SOLITAIRE_REDPACKET_STATUS = 0x3101; //检查红包状态
	public static final int HALL_CHECK_SOLITAIRE_REDPACKET_STATUS_SUCCESS = 0x3102; //检查红包状态成功
	public static final int HALL_CHECK_SOLITAIRE_REDPACKET_STATUS_ERROR = 0x3103; //检查红包状态失败
	public static final int HALL_SOLITAIRE_REDPACKET_INFO_SUCCESS = 0x3104; //获取大厅发送接龙红包信息成功
	public static final int HALL_SOLITAIRE_REDPACKET_INFO_ERROR = 0x3105; //获取大厅发送接龙红包信息失败
	public static final int HALL_SEND_SOLITAIRE_REDPACKET_SUCCESS = 0x3106; //获取大厅发送接龙红包信息成功
	public static final int HALL_SEND_SOLITAIRE_REDPACKET_ERROR = 0x3107; //获取大厅发送接龙红包信息成功
	public static final int HALL_GRAB_SOLITAIRE_REDPACKET_ENSURE = 0x3108; //大厅抢接龙红包确认
	public static final int HALL_GRAB_SOLITAIRE_REDPACKET_SUCCESS = 0x3109; //大厅抢接龙红包成功
	public static final int HALL_GRAB_SOLITAIRE_REDPACKET_ERROR = 0x3110; //大厅抢接龙红包失败
	
	//大厅红包
	public static final int HALL_GET_REDPACKET_TIP_SUCCESS = 0x3150; //获取大厅发红包信息成功
	public static final int HALL_GET_REDPACKET_TIP_ERROR = 0x3151; //获取大厅发红包信息失败
	public static final int HALL_SEND_REDPACKET_SUCCESS = 0x3152; //大厅发红包成功
	public static final int HALL_SEND_REDPACKET_ERROR = 0x3153; //大厅发红包失败
	public static final int HALL_CHECK_REDPACKET_STATUS = 0x3154; //检查红包状态
	public static final int HALL_GRAB_REDPACKET_ENSURE = 0x3155; //大厅抢红包确认
	public static final int HALL_GRAB_REDPACKET_SUCCESS = 0x3156; //大厅抢红包成功
	public static final int HALL_GRAB_REDPACKET_ERROR = 0x3157; //大厅抢红包失败
	public static final int HALL_GRAB_REDPACKET_SEE_LUCK = 0x3158; //大厅红包看看大家的手气
	public static final int HALL_CHECK_REDPACKET_STATUS_SUCCESS = 0x3159; //检查红包状态成功
	public static final int HALL_CHECK_REDPACKET_STATUS_ERROR = 0x3160; //检查红包状态失败
	public static final int HALL_GET_REDPACKET_HELP_SUCCESS = 0x3162; //获取大厅发红包信息成功
	public static final int HALL_GET_HALL_REDPACKET_AVAIL = 0x3163; //获取大厅是否有可用的红包信息
	public static final int HALL_GET_HALL_SOLITAIRE_REDPACKET_AVAIL = 0x3164; //获取大厅是否有可用的接龙红包信息
	public static final int HALL_GET_HALL_REDPACKET_ERROR = 0x3165; //获取大厅是否有可用的红包信息失败
	public static final int HALL_GET_AVAIL_HALL_REDPACKET_LIST_SUCCESS = 0x3166; //获取大厅可用的红包列表成功
	public static final int HALL_GET_AVAIL_HALL_SOLITAIRE_REDPACKET_LIST_SUCCESS = 0x3167; //获取大厅可用的红包接龙列表成功
	public static final int HALL_GET_AVAIL_HALL_REDPACKET_LIST_ERROR = 0x3168; //获取大厅可用的红包列表失败
	public static final int HALL_GET_ENABLE_SEND_REDPACKET_SUCCESS = 0x3169; //检查是否可以发送红包雨成功
	public static final int HALL_GET_ENABLE_SEND_SOLITAIRE_REDPACKET_SUCCESS = 0x3170; //检查是否可以发送红包红包接龙成功
	public static final int HALL_GET_ENABLE_SEND_REDPACKET_ERROR = 0x3171; //检查是否可以发送红包红包接龙失败
	public static final int HALL_GET_SHOW_SOLITAIRE_REDPACKET_BUBBLE = 0x3172; //展示红包接龙气泡
	public static final int HALL_GET_GONE_SOLITAIRE_REDPACKET_BUBBLE = 0x3173; //隐藏红包接龙气泡
	public static final int HALL_GET_SHOW_REDPACKET_BUBBLE = 0x3174; //展示大厅红包气泡
	public static final int HALL_GET_GONE_REDPACKET_BUBBLE = 0x3175; //隐藏大厅红包气泡
	
}
