package com.tshang.peipei.model.broadcast;

import java.math.BigInteger;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.BaseUtils;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.Gender;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.BaseLogic;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.store.StoreUserBiz;
import com.tshang.peipei.model.bizcallback.BizCallBackGetUserProperty;
import com.tshang.peipei.model.request.RequestGetMagic;
import com.tshang.peipei.model.request.RequestGetMagic.IReqInquiryBroadcastEffectCallBack;
import com.tshang.peipei.model.request.RequestGetPrivilegeStatus;
import com.tshang.peipei.model.request.RequestGetPrivilegeStatus.IGetPrivilegeStatus;
import com.tshang.peipei.model.request.RequestPlayFingerGuessingWithAnte;
import com.tshang.peipei.model.request.RequestPlayFingerGuessingWithAnte.iPlayFingerGuessing;
import com.tshang.peipei.model.request.RequestSendBroadcastVoice;
import com.tshang.peipei.model.request.RequestSendBroadcastVoice.BizCallBackSendBroadcastVoice;
import com.tshang.peipei.model.request.RequestSendColorfulBroadcast;
import com.tshang.peipei.model.request.RequestSendColorfulBroadcast.ISendColorBroadCallBack;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlDataInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlIntList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.UserPropertyInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: WriteBroadCastBiz.java 
 *
 * @Description: 写广播的基础逻辑
 *
 * @author Jeff 
 *
 * @date 2014年8月12日 下午1:37:49 
 *
 * @version V1.0   
 */
public class WriteBroadCastBiz extends BaseLogic implements BizCallBackGetUserProperty, ISendColorBroadCallBack, BizCallBackSendBroadcastVoice,
		iPlayFingerGuessing, IGetPrivilegeStatus, IReqInquiryBroadcastEffectCallBack {

	public static final int MAGIC_ONE_VALUE = 1;//流星雨
	public static final int MAGIC_TWO_VALUE = 2;//万箭阵
	public static final int MAGIC_THREE_VALUE = 3;//鸿毛雨
	public static final int MAGIC_FOUR_VALUE = 4;//玫瑰花语
	public static final int MAGIC_FIVE_VALUE = 5;//一箭钟情
	public static final int MAGIC_SIX_VALUE = 6;//变变变
	public static final int MAGIC_SEVEN_VALUE = 7;//真爱永恒
	public static final int MAGIC_EIGHT_VALUE = 8;//烈焰红唇
	public static final int MAGIC_NINE_VALUE = 9;//天马流星拳
	public static final int MAGIC_TEN_VALUE = 10;//甜蜜热气球
	public static final int MAGIC_ELEVEN_VALUE = 11;//大厅进场特效

	public static final int COMMON_BROADCAST = 0;//普通广播
	public static final int TOP_BRAODCAST = 2;//置顶广播
	public static final int BROADCAST_TEXT_TYPE = 0;//文字广播
	public static final int BROADCAST_VOICE_TYPE = 1;//语音广播
	public static final int BROADCAST_FINGER_TYPE = 2;//猜拳
	public static final int BROADCAST_SOLITAIRE_REDPACKET = 3;//红包接龙
	private String priceSendBroad = "-50银币";
	private boolean isDecree = false;//是否为圣旨
	private boolean isUseMagic = false;//是否使用仙术
	private int currentMagicValue = MAGIC_ONE_VALUE;

	public boolean isUseMagic() {
		return isUseMagic;
	}

	public int getCurrentMagicValue() {
		return currentMagicValue;
	}

	public void setCurrentMagicValue(int currentMagicValue) {
		this.currentMagicValue = currentMagicValue;
	}

	public void setUseMagic(boolean isUseMagic) {
		this.isUseMagic = isUseMagic;
	}

	public void setDecree(boolean isDecree) {
		this.isDecree = isDecree;
	}

	public WriteBroadCastBiz(Activity activity, Handler handler) {
		super(activity, handler);
		priceSendBroad = activity.getString(R.string.str_silver_fifty);
	}

	private int reloadcount = 0;//加载失败继续加载财富值的次数
	private int iGoldcoin = 0;//金币数量 发帖用来用的
	private int iSilvercoin = 0;//银币数量
	private int broadcastType = COMMON_BROADCAST;
	private int type = BROADCAST_TEXT_TYPE;
	private int leftNumDecree = 0;//圣旨剩余条数

	public int getLeftNumDecree() {
		return leftNumDecree;
	}

	public void setLeftNumDecree(int leftNumDecree) {
		this.leftNumDecree = leftNumDecree;
	}

	public int getBroadcastType() {
		return broadcastType;
	}

	public void setBroadcastType(int broadcastType) {
		this.broadcastType = broadcastType;
		calculateSendBroadCastPrice();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		calculateSendBroadCastPrice();
	}

	private void calculateSendBroadCastPrice() {//计算发布广播的价格
		if (type == BROADCAST_TEXT_TYPE && broadcastType == COMMON_BROADCAST) {//文字普通广播
			if (iSilvercoin >= 50) {
				priceSendBroad = activity.getString(R.string.str_silver_fifty);
			} else if (iSilvercoin < 50 && iGoldcoin >= 5) {
				priceSendBroad = activity.getString(R.string.str_gold_coin_five);
			} else {
				priceSendBroad = activity.getString(R.string.str_not_enough_money);
			}
		} else if (type == BROADCAST_TEXT_TYPE && broadcastType == TOP_BRAODCAST) {//文字置顶广播
			if (isUseMagic) {
				if (currentMagicValue == MAGIC_FIVE_VALUE || currentMagicValue == MAGIC_SIX_VALUE || currentMagicValue == MAGIC_SEVEN_VALUE
						|| currentMagicValue == MAGIC_EIGHT_VALUE || currentMagicValue == MAGIC_NINE_VALUE || currentMagicValue == MAGIC_TEN_VALUE) {
					if (iSilvercoin >= 50000) {
						priceSendBroad = "-50000银币";
					} else if (iSilvercoin < 50000 && iGoldcoin >= 1000) {
						priceSendBroad = "-1000金币";
					} else {
						priceSendBroad = activity.getString(R.string.str_not_enough_money);
					}
				} else {
					if (iSilvercoin >= 5000) {
						priceSendBroad = "-5000银币";
					} else if (iSilvercoin < 5000 && iGoldcoin >= 500) {
						priceSendBroad = "-500金币";
					} else {
						priceSendBroad = activity.getString(R.string.str_not_enough_money);
					}
				}
			} else {
				int textTopCoinCost = SharedPreferencesTools.getInstance(activity).getIntValueByKey(
						BAConstants.PEIPEI_APP_CONFIG_TOP_TEXT_BROADCAST_COIN, 100);
				if (iGoldcoin >= textTopCoinCost) {
					priceSendBroad = String.format(activity.getString(R.string.str_gold_coin_hundred), textTopCoinCost);
				} else {
					priceSendBroad = activity.getString(R.string.str_not_enough_money);
				}
			}
		} else if (type == BROADCAST_VOICE_TYPE && broadcastType == COMMON_BROADCAST) {//语音普通广播
			if (iGoldcoin >= 5) {
				priceSendBroad = activity.getString(R.string.str_gold_coin_five);
			} else {
				priceSendBroad = activity.getString(R.string.str_not_enough_money);
			}
		} else if (type == BROADCAST_VOICE_TYPE && broadcastType == TOP_BRAODCAST) {//语音置顶广播
			int voiceTopCoinCost = SharedPreferencesTools.getInstance(activity).getIntValueByKey(
					BAConstants.PEIPEI_APP_CONFIG_TOP_VOICE_BROADCAST_COIN, 100);
			if (iGoldcoin >= voiceTopCoinCost) {
				priceSendBroad = String.format(activity.getString(R.string.str_gold_coin_hundred), voiceTopCoinCost);
			} else {
				priceSendBroad = activity.getString(R.string.str_not_enough_money);
			}
		}
		if (isDecree) {
			priceSendBroad = "剩余" + leftNumDecree + "条";
		}
		//		System.out.println("type===" + type + "=========" + broadcastType);
		//		System.out.println("isDecree===" + isDecree);
		//		System.out.println("有没有进来=============" + priceSendBroad);
		sendHandlerMessage(HandlerValue.BRAODCAST_USERWEALTH_SUCCESS_VALUE, priceSendBroad);
	}

	public void getWealth() {//获取财富值
		StoreUserBiz.getInstance().getUserProperty(activity, BAApplication.mLocalUserInfo.uid.intValue(), this);
	}

	public void getPrivilegeStatus(Activity activity, int type) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return;
		}
		RequestGetPrivilegeStatus req = new RequestGetPrivilegeStatus();
		req.getPrivilegeStatus(userEntity.auth, BAApplication.app_version_code, type, userEntity.uid.intValue(), this);

	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param broadcasttype 是置顶广播还是普通广播
	 * @param voicedata 语音内容
	 * @param voicelen 语音时长 单位为秒
	 */
	public boolean sendVoiceBroadCast(byte[] voicedata, int voicelen) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return false;
		}
		if (priceSendBroad.equals(activity.getString(R.string.str_not_enough_money))) {
			BaseUtils.showTost(activity, R.string.str_not_enough_money);
			return false;
		}
		GoGirlIntList gogirlIntList = new GoGirlIntList();
		BaseUtils.showDialog(activity, R.string.write_sending);
		if (voicelen > 60) {
			voicelen = 60;
		}
		new RequestSendBroadcastVoice().sendVoiceBroadcast(userEntity.auth, BAApplication.app_version_code, broadcastType, gogirlIntList,
				userEntity.uid.intValue(), voicedata, voicelen, this);
		return false;
	}

	private int typecolor = BAConstants.MessageType.BROADCASTCOLOR.getValue();

	public int getTypecolor() {
		return typecolor;
	}

	public void setTypecolor(int typecolor) {
		this.typecolor = typecolor;
	}

	/**
	 * 
	 * @param toUids 有@的用户
	 * @param content 文字广播类型
	 * @param toast 判断用户是否有金币或者银币发送广播
	 */
	@SuppressWarnings("unchecked")
	public boolean sendTextBroadCast(List<GoGirlUserInfo> userInfos, byte[] content, int color, boolean share) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return false;
		}
		if (!share) {
			typecolor = BAConstants.MessageType.BROADCASTCOLOR.getValue();
			if (isDecree) {
				if (userEntity.sex.intValue() == Gender.FEMALE.getValue()) {
					typecolor = BAConstants.MessageType.FEMALE_DECREE.getValue();
				} else {
					typecolor = BAConstants.MessageType.MALE_DECREE.getValue();
				}
			} else {
				if (isUseMagic) {
					typecolor = BAConstants.MessageType.GOGIRL_DATA_TYPE_ANIMATION_BROADCAST.getValue();
					color = currentMagicValue;
					if (color == MAGIC_ONE_VALUE || color == MAGIC_THREE_VALUE || color == MAGIC_FIVE_VALUE || color == MAGIC_SIX_VALUE
							|| color == MAGIC_EIGHT_VALUE || color == MAGIC_SEVEN_VALUE || color == MAGIC_NINE_VALUE || color == MAGIC_TEN_VALUE) {
						if (userInfos.isEmpty()) {
							HandlerUtils.sendHandlerMessage(handler, HandlerValue.MAIN_BROADCAST_NO_ADD_MAGIC_VALUE);
							return false;
						}
					}
				} else {
					typecolor = BAConstants.MessageType.BROADCASTCOLOR.getValue();
				}
			}
		} else {//分享了走新类型
			typecolor = BAConstants.MessageType.SHOWSHAREBROADCASTINFO.getValue();
		}

		if (content == null || content.length == 0) {
			BaseUtils.showTost(activity, R.string.str_input_broadcast);
			return false;
		}
		if (priceSendBroad.equals(activity.getString(R.string.str_not_enough_money)) && !isDecree) {
			BaseUtils.showTost(activity, R.string.str_not_enough_money);
			return false;
		}
		GoGirlIntList gogirlIntList = new GoGirlIntList();
		if (userInfos.size() > 0) {
			for (GoGirlUserInfo userinfo : userInfos)
				gogirlIntList.add(userinfo.uid);//添加队列
		}
		BaseUtils.showDialog(activity, R.string.write_sending);
		GoGirlDataInfoList dataInfoList = getGoGirlDataInfoList(content, typecolor, color);
		new RequestSendColorfulBroadcast().sendColorBroadCast(userEntity.auth, BAApplication.app_version_code, broadcastType, dataInfoList,
				gogirlIntList, userEntity.uid.intValue(), this);
		return true;
	}

	/**
	 * 
	 * @author Jeff
	 *
	 * @param content 发送的文本字节数组
	 * @param type 发送文本广播的类型，有圣旨，懿旨，特权等等type值
	 * @param color 特殊字体的颜色再加仙术
	 * @return
	 */
	private GoGirlDataInfoList getGoGirlDataInfoList(byte[] content, int type, int color) {
		GoGirlDataInfoList dataInfoList = new GoGirlDataInfoList();
		GoGirlDataInfo goGirlDataInfo = new GoGirlDataInfo();
		goGirlDataInfo.data = content;
		goGirlDataInfo.dataid = "".getBytes();
		goGirlDataInfo.revint0 = BigInteger.valueOf(0);
		goGirlDataInfo.revint1 = BigInteger.valueOf(0);
		goGirlDataInfo.type = BigInteger.valueOf(type);//后台定的
		goGirlDataInfo.revstr1 = "".getBytes();
		goGirlDataInfo.revstr0 = "".getBytes();
		goGirlDataInfo.datainfo = BigInteger.valueOf(color);

		dataInfoList.add(goGirlDataInfo);
		return dataInfoList;
	}

	/**
	 * 发猜拳广播
	 */
	public boolean sendFingerBroadcast(Activity context, int toUid, int finger, int ante, String guessingstrid, int antetype) {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return false;
		}

		RequestPlayFingerGuessingWithAnte req = new RequestPlayFingerGuessingWithAnte();
		req.playFingerGuessingWithAnte(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), toUid, ante, finger,
				guessingstrid, this, null, false, new String(userEntity.nick), userEntity.sex.intValue(), antetype);
		return true;
	}

	@Override
	public void getUserProperty(int retCode, UserPropertyInfo userPropertyInfo) {//获取财富值
		if (retCode == 0) {
			reloadcount = 0;
			if (userPropertyInfo != null) {
				iGoldcoin = userPropertyInfo.goldcoin.intValue();//获取到金币
				HandlerUtils.sendHandlerMessage(handler, HandlerValue.RED_PACKET_GET_TOTAL_COIN_VALUE, iGoldcoin);
				iSilvercoin = userPropertyInfo.silvercoin.intValue();//获取到银币
				calculateSendBroadCastPrice();
			}
		} else {
			reloadcount++;
			if (reloadcount < 4) {
				getWealth();
			}
		}
	}

	@Override
	public void sendBraodCallBack(int retCode, String str) {//普通文字广播回调
		handler.sendMessage(handler.obtainMessage(HandlerValue.BROADCAST_SEND_SUCCESS_VALUE, retCode, type, ""));
	}

	@Override
	public void sendCallBack(int retCode, BroadcastInfo info) {
		if (retCode == 0 && info != null) {
			String voice = new String(info.voiceinfo);
			if (!TextUtils.isEmpty(voice)) {
				String[] voices = voice.split(",");
				if (voices != null && voices.length == 2) {//数组位置1保存文件名字，2为时长
					if (!Environment.getExternalStorageDirectory().exists()) {
						return;
					}
					GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
					if (userEntity == null) {
						return;
					}
					//					String path = new SdCardUtils().saveFile(fileByte, userEntity.uid.intValue(), voices[0]);
					handler.sendMessage(handler.obtainMessage(HandlerValue.BROADCAST_SEND_SUCCESS_VALUE, retCode, type, voice));
				}
			}
		} else if (retCode == rspContMsgType.E_GG_FORBIT) {
			handler.sendMessage(handler.obtainMessage(HandlerValue.BROADCAST_SEND_SUCCESS_VALUE, retCode, type, ""));
		}

	}

	@Override
	public void playFingerBack(int retcode, int uid, boolean isGroupGuess, ChatDatabaseEntity chatentity, FingerGuessingInfo info, String nick,
			int sex, int antetype) {
		info.antetype = BigInteger.valueOf(antetype);
		handler.sendMessage(handler.obtainMessage(HandlerValue.BROADCAST_SEND_SUCCESS_VALUE, retcode, type, info));
	}

	@Override
	public void getPrivilegeStatus(int retCode, int type, int curnum, int maxnum) {
		if (retCode == 0) {
			if (type == RequestGetPrivilegeStatus.BROADCAST_COLOR_TYPE) {
				if (maxnum > 0) {
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.BROADCAST_COLOR_PRIVILEGE_STATUS_SUCCESS_VALUE);
				} else {//权限不足
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.BROADCAST_COLOR_PRIVILEGE_STATUS_LOWER_VALUE);
				}
			} else if (type == RequestGetPrivilegeStatus.BROADCAST_DECREE_TYPE) {
				if (maxnum > 0) {
					int leftNum = maxnum - curnum;
					if (leftNum == 0) {//不能够再发更多的了
						HandlerUtils.sendHandlerMessage(handler, HandlerValue.BROADCAST_DECREE_PRIVILEGE_STATUS_NO_MORE_VALUE);
					} else if (leftNum > 0) {//可以继续发送
						HandlerUtils.sendHandlerMessage(handler, HandlerValue.BROADCAST_DECREE_PRIVILEGE_STATUS_SUCCESS_VALUE, leftNum, leftNum);
					}

				} else {//权限不足
					HandlerUtils.sendHandlerMessage(handler, HandlerValue.BROADCAST_DECREE_PRIVILEGE_STATUS_LOWER_VALUE);
				}
			}
		}

	}

	public boolean requestMagic() {
		GoGirlUserInfo userEntity = UserUtils.getUserEntity(activity);
		if (userEntity == null) {
			return false;
		}
		BaseUtils.showDialog(activity, R.string.loading);
		RequestGetMagic req = new RequestGetMagic();
		req.getEnterBroadcast(userEntity.auth, BAApplication.app_version_code, userEntity.uid.intValue(), this);
		return true;
	}

	@Override
	public void getReqInquiryBroadcastEffect(int retCode, List<Integer> list) {
		if (list == null || list.isEmpty()) {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.BROADCAST_MAGIC_LOWER_VALUE);
		} else {
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.BROADCAST_MAGIC_VALUE, list);
		}

	}
}
