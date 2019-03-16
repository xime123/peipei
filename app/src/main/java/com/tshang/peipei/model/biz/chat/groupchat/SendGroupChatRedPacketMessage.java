package com.tshang.peipei.model.biz.chat.groupchat;

import android.app.Activity;
import android.text.TextUtils;

import com.tshang.peipei.base.ILog;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.chat.SaveChatData;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.RedpacketOperate;

/**
 * @Title: SendTextMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么)  发送红包
 *
 * @author Jeff  
 *
 * @date 2014年9月24日 下午3:39:38 
 *
 * @version V1.3.0   
 */
public class SendGroupChatRedPacketMessage extends BaseGroupChatSendMessage {
	public static ChatDatabaseEntity sendRedPacketMsg(Activity activity, RedPacketInfo packetInfo, int touid, String toName) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return null;
		}
		ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();
		chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(0);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("0");
		chatDatabaseEntity.setProgress(0);
		chatDatabaseEntity.setType(29);
		chatDatabaseEntity.setCreateTime(packetInfo.createtime.longValue() * 1000);
		String message = ChatMessageBiz.saveRedPacketInfo(packetInfo);

		if (TextUtils.isEmpty(message)) {
			message = "";
		}
		chatDatabaseEntity.setMessage(message);

		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, true);
		chatDatabase.getLastestMessage();
		if (chatDatabase != null) {
			chatDatabaseEntity.setMesLocalID(chatDatabase.insert(chatDatabaseEntity));
			isHaveChatRecord(activity, toName, info.sex.intValue(), touid, GROUP_CHAT_TYPE, "[发送红包]", chatDatabaseEntity.getCreateTime());
		}
		return chatDatabaseEntity;

	}
	
	public static ChatDatabaseEntity sendSolitaireRedPacketMsg(Activity activity, RedPacketBetCreateInfo solitaireRedPacket, int touid, String toName) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return null;
		}
		if(solitaireRedPacket == null){
			return null;
		}
		ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();
		chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(0);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("1");
		chatDatabaseEntity.setProgress(solitaireRedPacket.redpacketstatus.intValue());
		chatDatabaseEntity.setType(100);
		chatDatabaseEntity.setCreateTime(solitaireRedPacket.clicktime.longValue() * 1000);
		ILog.d("DYH", "createtime : " + solitaireRedPacket.clicktime.longValue());
		String message = ChatMessageBiz.saveSolitaireRedPacketInfo(activity, solitaireRedPacket);
		
		if (TextUtils.isEmpty(message)) {
			message = "";
		}
		chatDatabaseEntity.setMessage(message);
		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, true);
		chatDatabase.getLastestMessage();
		if (chatDatabase != null) {
			chatDatabaseEntity.setMesLocalID(chatDatabase.insert(chatDatabaseEntity));
			isHaveChatRecord(activity, toName, info.sex.intValue(), touid, GROUP_CHAT_TYPE, "[红包接龙]", chatDatabaseEntity.getCreateTime());
		}
		
		saveCanGrapSolitaireRedPacketMsg(activity, solitaireRedPacket, touid, toName, 100);
		
		return chatDatabaseEntity;
	}
	
	private static void saveCanGrapSolitaireRedPacketMsg(Activity activity, RedPacketBetCreateInfo solitaireRedPacket, int touid, String toName, int type) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}
		if(solitaireRedPacket == null){
			return;
		}
		ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();
		chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(0);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setProgress(solitaireRedPacket.redpacketstatus.intValue());
		chatDatabaseEntity.setType(type);
		chatDatabaseEntity.setCreateTime(solitaireRedPacket.createtime.longValue() * 1000);
		String message = ChatMessageBiz.saveSolitaireRedPacketInfo(activity, solitaireRedPacket);
		
		if (TextUtils.isEmpty(message)) {
			message = "";
		}
		chatDatabaseEntity.setMessage(message);
		chatDatabaseEntity.setMesSvrID(solitaireRedPacket.id.intValue() + "");
		RedpacketOperate operate = RedpacketOperate.getInstance(activity, touid);
		if (operate != null) {
			chatDatabaseEntity.setMesLocalID(operate.insert(chatDatabaseEntity));
			SaveChatData.updateCanGrapSolitaireInfo(activity, GROUP_CHAT_TYPE, info.sex.intValue(), new String(info.nick), chatDatabaseEntity, solitaireRedPacket);
		}
		
		
		
	}
	
	public static ChatDatabaseEntity sendGrapSolitaireRedPacketMsg(Activity activity, RedPacketBetCreateInfo solitaireRedPacket, int touid, String toName) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return null;
		}
		if(solitaireRedPacket == null){
			return null;
		}
		ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();
		chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());//拼接本地数据
		chatDatabaseEntity.setDes(0);
		chatDatabaseEntity.setFromID(info.uid.intValue());
		chatDatabaseEntity.setToUid(touid);
		chatDatabaseEntity.setMesSvrID("1");
		chatDatabaseEntity.setProgress(solitaireRedPacket.redpacketstatus.intValue());
		chatDatabaseEntity.setType(101);
		chatDatabaseEntity.setCreateTime(solitaireRedPacket.clicktime.longValue() * 1000);
		String message = ChatMessageBiz.saveSolitaireRedPacketInfo(activity, solitaireRedPacket);
		
		if (TextUtils.isEmpty(message)) {
			message = "";
		}
		ILog.d("DYH", "createtime2 : " + solitaireRedPacket.createtime.longValue());
		chatDatabaseEntity.setMessage(message);
		ChatOperate chatDatabase = ChatOperate.getInstance(activity, touid, true);
		chatDatabase.getLastestMessage();
		if (chatDatabase != null) {
			chatDatabaseEntity.setMesLocalID(chatDatabase.insert(chatDatabaseEntity));
			isHaveChatRecord(activity, toName, info.sex.intValue(), touid, GROUP_CHAT_TYPE, "[红包接龙]", chatDatabaseEntity.getCreateTime());
		}
		
		saveCanGrapSolitaireRedPacketMsg(activity, solitaireRedPacket, touid, toName, 101);
		
		return chatDatabaseEntity;
	}

}
