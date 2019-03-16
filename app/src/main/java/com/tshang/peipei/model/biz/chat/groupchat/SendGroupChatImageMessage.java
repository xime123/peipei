package com.tshang.peipei.model.biz.chat.groupchat;

import android.app.Activity;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.chat.ChatRecordBiz;
import com.tshang.peipei.model.request.RequestGoGirlGroupChat;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;

/**
 * @Title: SendImageMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么)  发送图片数据
 *
 * @author Jeff  
 *
 * @date 2014年9月24日 下午4:42:23 
 *
 * @version V1.3.0   
 */
public class SendGroupChatImageMessage extends BaseGroupChatSendMessage {

	private static SendGroupChatImageMessage instance = null;

	public static SendGroupChatImageMessage getInstance() {
		if (instance == null) {
			synchronized (SendGroupChatImageMessage.class) {
				if (instance == null) {
					instance = new SendGroupChatImageMessage();
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
	public void sendImageGroupMsg(Activity activity, byte[] data, int touid, String toName, BAHandler handler, ChatDatabaseEntity chatDatabaseEntity,
			boolean isResend) {
		RequestGoGirlGroupChat req = new RequestGoGirlGroupChat();
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

		GoGirlChatData chatdata = getGoGirlChatData(info, MessageType.IMAGE.getValue(), touid, toName);

		chatdata.chatdatalist = getGoGirlDataInfoList(data, MessageType.IMAGE.getValue(), 0);

		chatDatabaseEntity.setStatus(ChatStatus.SENDING.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(0);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("0");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(MessageType.IMAGE.getValue());
		chatDatabaseEntity.setCreateTime(System.currentTimeMillis());
		String message = ChatMessageBiz.saveImageMessage(data.length, "");

		if (TextUtils.isEmpty(message)) {
			return;
		}
		chatDatabaseEntity.setMessage(message);

		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, true);
		if (chatDatabase != null) {
			if (!isResend) {
				long localId = chatDatabase.insert(chatDatabaseEntity);
				if (localId >= 0) {
					chatDatabaseEntity.setMesLocalID(localId);
					if (TextUtils.isEmpty(ChatRecordBiz.saveFile(activity, touid, localId, data, true))) {
						chatDatabase.delete(localId);
					}
				}
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_APPEND_DATA_VALUE, chatDatabaseEntity));
			}
			String session = activity.getString(R.string.session_image);
			isHaveChatRecord(activity, toName, 1, touid, GROUP_CHAT_TYPE, session, chatDatabaseEntity.getCreateTime());
		}
		req.reqSendGroupChat(info.auth, BAApplication.app_version_code, info.uid.intValue(), touid, chatdata, chatDatabaseEntity, this);
	}

}
