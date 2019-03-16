package com.tshang.peipei.model.biz.chat.privatechat;

import android.app.Activity;
import android.text.TextUtils;

import com.tshang.peipei.activity.chat.bean.HaremEmotionUtil;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.groupchat.SendGroupChatTextMessage;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;

/**
 * @Title: SendPrivateChatTextMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Jeff  发送私聊文本信息
 *
 * @date 2014年10月24日 下午3:48:52 
 *
 * @version V1.4.0   
 */
public class SendPrivateChatTextMessage extends BasePrivateChatSendMessage {

	private static SendPrivateChatTextMessage instance = null;

	public static SendPrivateChatTextMessage getInstance() {
		if (instance == null) {
			synchronized (SendGroupChatTextMessage.class) {
				if (instance == null) {
					instance = new SendPrivateChatTextMessage();
				}
			}
		}
		return instance;
	}

	public void sendPrivateChatTextMessage(Activity activity, String data, int touid, String toName, int fsex, BAHandler handler,
			ChatDatabaseEntity chatDatabaseEntity, boolean isResend, int chatType) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		if (chatDatabaseEntity == null) {
			chatDatabaseEntity = new ChatDatabaseEntity();
		}
		this.activiy = activity;
		this.handler = handler;
		data = saveChatMessage(activity, data, touid, toName, fsex, handler, chatDatabaseEntity, isResend, info, ChatStatus.SENDING.getValue(), 0,
				chatType);
		if (chatType == RewardListActivity.CHAT_FROM_REWARD) {
			sendPrivateChatMsg(activity, data.getBytes(), MessageType.GOGIRL_DATA_TYPE_ANONYM_TEXT.getValue(), 0, touid, "", toName, fsex,
					chatDatabaseEntity.getMesLocalID(), chatType, chatDatabaseEntity);
		} else {
			sendPrivateChatMsg(activity, data.getBytes(), MessageType.TEXT.getValue(), 0, touid, "", toName, fsex,
					chatDatabaseEntity.getMesLocalID(), chatType, chatDatabaseEntity);
		}
	}

	public String saveChatMessage(Activity activity, String data, int touid, String toName, int fsex, BAHandler handler,
			ChatDatabaseEntity chatDatabaseEntity, boolean isResend, GoGirlUserInfo info, int status, int des, int from) {
		chatDatabaseEntity.setStatus(status);//拼接本地数据
		chatDatabaseEntity.setDes(des);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("0");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(MessageType.TEXT.getValue());//纯文本信息
		chatDatabaseEntity.setCreateTime(System.currentTimeMillis());

		if (TextUtils.isEmpty(data)) {
			data = "";
		}
		chatDatabaseEntity.setMessage(data);
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_REFLESH_UI_VALUE);//通知刷新界面
		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, false);
		if (chatDatabase != null) {
			if (!isResend) {
				chatDatabaseEntity.setMesLocalID(chatDatabase.insert(chatDatabaseEntity));
				HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_APPEND_DATA_VALUE, chatDatabaseEntity);
			}
			if (from == RewardListActivity.CHAT_FROM_REWARD) {
				isHaveChatRecord(activity, toName, fsex, touid, PRIVATE_CHAT_ANONYM_TYPE, data, chatDatabaseEntity.getCreateTime());
			} else
				isHaveChatRecord(activity, toName, fsex, touid, PRIVATE_CHAT_TYPE, data, chatDatabaseEntity.getCreateTime());
		}
		return data;
	}

	public void sendPrivateChatHaremEmotionMessage(Activity activity, String data, int touid, String toName, int fsex, BAHandler handler,
			ChatDatabaseEntity chatDatabaseEntity, boolean isResend, int chatType) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		if (chatDatabaseEntity == null) {
			chatDatabaseEntity = new ChatDatabaseEntity();
		}
		this.activiy = activity;
		this.handler = handler;

		chatDatabaseEntity.setStatus(ChatStatus.SENDING.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(0);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("0");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(MessageType.GOGIRL_DATA_TYPE_SMILE.getValue());//纯文本信息
		chatDatabaseEntity.setCreateTime(System.currentTimeMillis());

		if (TextUtils.isEmpty(data)) {
			data = "0";
		}
		chatDatabaseEntity.setMessage(data);
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_REFLESH_UI_VALUE);//通知刷新界面
		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, false);
		if (chatDatabase != null) {
			if (!isResend) {
				chatDatabaseEntity.setMesLocalID(chatDatabase.insert(chatDatabaseEntity));
				HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_APPEND_DATA_VALUE, chatDatabaseEntity);
			}
			String strEmotion = HaremEmotionUtil.haremFaceMaps.get(data);
			if (TextUtils.isEmpty(strEmotion)) {
				strEmotion = "[后宫表情]";
			}
			if (chatType == RewardListActivity.CHAT_FROM_REWARD) {
				isHaveChatRecord(activity, toName, fsex, touid, PRIVATE_CHAT_ANONYM_TYPE, strEmotion, chatDatabaseEntity.getCreateTime());
			} else
				isHaveChatRecord(activity, toName, fsex, touid, PRIVATE_CHAT_TYPE, strEmotion, chatDatabaseEntity.getCreateTime());
		}
		if (chatType == RewardListActivity.CHAT_FROM_REWARD) {
			sendPrivateChatMsg(activity, data.getBytes(), MessageType.GOGIRL_DATA_TYPE_ANONYM_SMILE.getValue(), 0, touid, "", toName, fsex,
					chatDatabaseEntity.getMesLocalID(), chatType, chatDatabaseEntity);
		} else {
			sendPrivateChatMsg(activity, data.getBytes(), MessageType.GOGIRL_DATA_TYPE_SMILE.getValue(), 0, touid, "", toName, fsex,
					chatDatabaseEntity.getMesLocalID(), chatType, chatDatabaseEntity);
		}

	}

}
