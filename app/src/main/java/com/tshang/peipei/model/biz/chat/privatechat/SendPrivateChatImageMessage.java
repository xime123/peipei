package com.tshang.peipei.model.biz.chat.privatechat;

import android.app.Activity;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.reward.RewardListActivity;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.biz.chat.groupchat.SendGroupChatTextMessage;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;

/**
 * @Title: SendPrivateChatTextMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Jeff  发送私聊图片信息
 *
 * @date 2014年10月24日 下午3:48:52 
 *
 * @version V1.4.0   
 */
public class SendPrivateChatImageMessage extends BasePrivateChatSendMessage {

	private static SendPrivateChatImageMessage instance = null;

	public static SendPrivateChatImageMessage getInstance() {
		if (instance == null) {
			synchronized (SendGroupChatTextMessage.class) {
				if (instance == null) {
					instance = new SendPrivateChatImageMessage();
				}
			}
		}
		return instance;
	}

	public void sendPrivateChatImageMessage(Activity activity, byte[] data, int type, int touid, String toName, int fsex, BAHandler handler,
			ChatDatabaseEntity chatDatabaseEntity, boolean isResend, int from) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		if (data == null) {
			return;
		}
		if (chatDatabaseEntity == null) {
			chatDatabaseEntity = new ChatDatabaseEntity();
		}
		this.activiy = activity;
		this.handler = handler;
		String burnId = "";
		if (type == MessageType.BURN_IMAGE.getValue() || type == MessageType.GOGIRL_DATA_TYPE_TRANSITORY_ANONYM_PIC.getValue()) {//阅后即焚的ID
			burnId = String.valueOf(System.currentTimeMillis());
		}

		chatDatabaseEntity.setStatus(ChatStatus.SENDING.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(0);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);//发送给对方的uid
		chatDatabaseEntity.setMesSvrID(burnId);
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(type);//图片
		chatDatabaseEntity.setCreateTime(System.currentTimeMillis());
		String messge = ChatMessageBiz.saveImageMessage(data.length, "");
		if (TextUtils.isEmpty(messge)) {
			return;
		}
		chatDatabaseEntity.setMessage(messge);
		HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_REFLESH_UI_VALUE);//通知刷新界面
		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, false);
		if (chatDatabase != null) {
			if (!isResend) {
				long localId = chatDatabase.insert(chatDatabaseEntity);
				if (localId >= 0) {
					chatDatabaseEntity.setMesLocalID(localId);
					if (TextUtils.isEmpty(ChatRecordBiz.saveFile(activity, touid, localId, data, true))) {
						chatDatabase.delete(localId);
					}
				}
				HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_APPEND_DATA_VALUE, chatDatabaseEntity);
			}

			String session = activity.getString(R.string.session_image);
			//			if(type==MessageType.BURN_IMAGE.getValue()||type==MessageType.BURN_IMAGE_KEY.getValue()){
			//				session = activity.getString(R.string.session_image);
			//			}
			if (from == RewardListActivity.CHAT_FROM_REWARD) {
				isHaveChatRecord(activity, toName, fsex, touid, PRIVATE_CHAT_ANONYM_TYPE, session, chatDatabaseEntity.getCreateTime());
			} else
				isHaveChatRecord(activity, toName, fsex, touid, PRIVATE_CHAT_TYPE, session, chatDatabaseEntity.getCreateTime());
		}
		if (from == RewardListActivity.CHAT_FROM_REWARD) {
			sendPrivateChatMsg(activity, data, type, 0, touid, burnId, toName, fsex, chatDatabaseEntity.getMesLocalID(), from, chatDatabaseEntity);
		} else {
			sendPrivateChatMsg(activity, data, type, 0, touid, burnId, toName, fsex, chatDatabaseEntity.getMesLocalID(), from, chatDatabaseEntity);
		}
	}
}