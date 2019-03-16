package com.tshang.peipei.model.biz.chat.groupchat;

import java.math.BigInteger;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.request.RequestGoGirlGroupChat;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.SharedPreferencesTools;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;

/**
 * @Title: SendTextMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么)  发送消息数据
 *
 * @author Jeff  
 *
 * @date 2014年9月24日 下午3:39:38 
 *
 * @version V1.3.0   
 */
public class SendGroupChatTextMessage extends BaseGroupChatSendMessage {

	private static SendGroupChatTextMessage instance = null;

	public static SendGroupChatTextMessage getInstance() {
		if (instance == null) {
			synchronized (SendGroupChatTextMessage.class) {
				if (instance == null) {
					instance = new SendGroupChatTextMessage();
				}
			}
		}
		return instance;
	}

	/**
	 * 发送群聊纯文本信息
	 * @author Jeff
	 *
	 * @param activity
	 * @param data
	 * @param touid
	 * @param toName
	 * @param handler
	 * @param chatDatabaseEntity
	 * @param isResend
	 */
	public void sendTextGroupMsg(Activity activity, byte[] data, int touid, String toName, BAHandler handler, ChatDatabaseEntity chatDatabaseEntity,
			boolean isResend) {
		RequestGoGirlGroupChat req = new RequestGoGirlGroupChat();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		if (chatDatabaseEntity == null) {
			chatDatabaseEntity = new ChatDatabaseEntity();
		}
		this.activiy = activity;
		this.handler = handler;

		GoGirlChatData chatdata = getGoGirlChatData(info, 0, touid, toName);

		chatdata.chatdatalist = getGoGirlDataInfoList(data, 0, 0);

		chatDatabaseEntity.setStatus(ChatStatus.SENDING.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(BAConstants.ChatDes.TO_FRIEDN.getValue());
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(MessageType.TEXT.getValue());
		chatDatabaseEntity.setCreateTime(System.currentTimeMillis());
		chatDatabaseEntity.setRevStr2(new String(BAApplication.mLocalUserInfo.nick));
		String message = new String(data);

		if (TextUtils.isEmpty(message)) {
			message = "";
		}
		chatDatabaseEntity.setMessage(message);

		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, true);
		if (chatDatabase != null) {
			if (!isResend) {
				chatDatabaseEntity.setMesLocalID(chatDatabase.insert(chatDatabaseEntity));
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_APPEND_DATA_VALUE, chatDatabaseEntity));
				//处理用户聊天背景
				String key = "Group_" + String.valueOf(touid) + "#" + String.valueOf(BAApplication.mLocalUserInfo.uid.intValue());
				int saveValue = SharedPreferencesTools.getInstance(activity).getIntValueByKey(key, -1);
				if (saveValue == -1) {
					SharedPreferencesTools.getInstance(activity).saveIntKeyValue(chatdata.revint3.intValue(), key);
				}else {
					chatdata.revint3=BigInteger.valueOf(saveValue);
				}
			}
			isHaveChatRecord(activity, toName, 1, touid, GROUP_CHAT_TYPE, message, chatDatabaseEntity.getCreateTime());
		}
		req.reqSendGroupChat(info.auth, BAApplication.app_version_code, info.uid.intValue(), touid, chatdata, chatDatabaseEntity, this);
	}

	public void sendHaremEmotionGroupMsg(Activity activity, byte[] data, int touid, String toName, BAHandler handler,
			ChatDatabaseEntity chatDatabaseEntity, boolean isResend) {
		RequestGoGirlGroupChat req = new RequestGoGirlGroupChat();
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		if (chatDatabaseEntity == null) {
			chatDatabaseEntity = new ChatDatabaseEntity();
		}
		this.activiy = activity;
		this.handler = handler;

		GoGirlChatData chatdata = getGoGirlChatData(info, MessageType.GOGIRL_DATA_TYPE_SMILE.getValue(), touid, toName);

		chatdata.chatdatalist = getGoGirlDataInfoList(data, MessageType.GOGIRL_DATA_TYPE_SMILE.getValue(), 0);

		chatDatabaseEntity.setStatus(ChatStatus.SENDING.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(BAConstants.ChatDes.TO_FRIEDN.getValue());
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(MessageType.GOGIRL_DATA_TYPE_SMILE.getValue());
		chatDatabaseEntity.setCreateTime(System.currentTimeMillis());
		String message = new String(data);

		if (TextUtils.isEmpty(message)) {
			message = "";
		}
		chatDatabaseEntity.setMessage(message);

		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, true);
		if (chatDatabase != null) {
			if (!isResend) {
				chatDatabaseEntity.setMesLocalID(chatDatabase.insert(chatDatabaseEntity));
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_APPEND_DATA_VALUE, chatDatabaseEntity));
			}
			String strEmotion = HaremEmotionUtil.haremFaceMaps.get(new String(data));
			if (TextUtils.isEmpty(strEmotion)) {
			}
			isHaveChatRecord(activity, toName, 1, touid, GROUP_CHAT_TYPE, strEmotion, chatDatabaseEntity.getCreateTime());
		}
		req.reqSendGroupChat(info.auth, BAApplication.app_version_code, info.uid.intValue(), touid, chatdata, chatDatabaseEntity, this);
	}
}
