package com.tshang.peipei.model.biz.chat.groupchat;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Message;
import android.text.TextUtils;

import com.tshang.peipei.R;
import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ChatStatus;
import com.tshang.peipei.base.babase.BAConstants.MessageType;
import com.tshang.peipei.base.babase.BAConstants.rspContMsgType;
import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.base.handler.HandlerUtils;
import com.tshang.peipei.base.handler.HandlerValue;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.BaseChatSendMessage;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.biz.chat.ChatMessageBiz;
import com.tshang.peipei.model.biz.chat.SaveChatData;
import com.tshang.peipei.model.request.RequestPlayFingerGuessingWithAnte;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.table.ChatTable;

/**
 * @Title: SendVoiceMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么)  猜拳数据
 *
 * @author Jeff  
 *
 * @date 2014年9月24日 下午4:42:23 
 *
 * @version V1.3.0   
 */
public class FingerGruessMessage extends BaseGroupChatSendMessage {

	private static FingerGruessMessage instance = null;

	public static FingerGruessMessage getInstance() {
		if (instance == null) {
			synchronized (FingerGruessMessage.class) {
				if (instance == null) {
					instance = new FingerGruessMessage();
				}
			}
		}
		return instance;
	}

	//ante是赌注
	public void playFinger(Activity activity, int fuid, ChatDatabaseEntity chatentity, boolean isGroupGuess, FingerGuessingInfo fingerInfo,
			String nick, int sex, BAHandler handler, boolean isResend, int finger1, int ante, int antetype) {
		GoGirlUserInfo info = UserUtils.getUserEntity(activity);
		if (info == null) {
			return;
		}

		this.activiy = activity;
		this.handler = handler;

		String sessionMsg = "";
		String message = "";

		int requestId = fuid;//临时变量，如果是群猜拳回复，是回复个人的数据
		if (fingerInfo == null) {//第一次发起猜拳
			fingerInfo = GroupChatUtils.getFirstGingerGuessInfo(activity, finger1, fuid, nick.getBytes(), ante, antetype);
			sessionMsg = activity.getString(R.string.finger_right_content1);
			if (isGroupGuess) {//群猜拳传负的Id,以区分是群猜拳还是私聊猜拳
				requestId = 0 - fuid;
			}
		} else {//回复的猜拳
			if (fingerInfo.winuid.intValue() == 0) {
				sessionMsg = activity.getString(R.string.finger_winner_content2);
			} else if (fingerInfo.winuid.intValue() == fingerInfo.uid1.intValue()) {
				sessionMsg = String.format(activity.getString(R.string.finger_winner_content1), new String(fingerInfo.nick1),
						fingerInfo.ante.intValue());
			} else {
				sessionMsg = String.format(activity.getString(R.string.finger_winner_content1), new String(fingerInfo.nick2),
						fingerInfo.ante.intValue());
			}
			requestId = fingerInfo.uid1.intValue();

		}

		message = ChatMessageBiz.saveFingerMessage(fingerInfo);
		if (TextUtils.isEmpty(message)) {
			message = "";
		}

		ChatOperate chatDatabase = ChatOperate.getInstance(activity, fuid, isGroupGuess);
		if (chatDatabase != null) {

			if (!TextUtils.isEmpty(new String(fingerInfo.globalid)) && chatentity != null && !isResend) { //需要把回复的数据删除掉
				chatDatabase.delete(chatentity.getMesLocalID());//删除掉选择回复的数据
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_REMOVE_GUESS_FINGER_VALUE, chatentity));
				chatentity = null;
			}
			if (chatentity == null) {
				chatentity = new ChatDatabaseEntity();
			}
			chatentity.setStatus(ChatStatus.SENDING.getValue());//拼接本地数据
			chatentity.setDes(BAConstants.ChatDes.TO_FRIEDN.getValue());
			chatentity.setFromID(info.uid.intValue());
			chatentity.setToUid(fuid);
			chatentity.setMesSvrID("");
			chatentity.setProgress(0);
			if (antetype == 0) {
				chatentity.setType(MessageType.WITHANTEFINGER.getValue());
			} else {
				chatentity.setType(MessageType.NEWFINGER.getValue());
			}
			chatentity.setCreateTime(System.currentTimeMillis());
			chatentity.setMessage(message);

			if (!isResend) {//是否是重发
				long localId = chatDatabase.insert(chatentity);
				chatentity.setMesLocalID(localId);
				handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_APPEND_DATA_VALUE, chatentity));
			}
			if (isGroupGuess) {
				isHaveChatRecord(activity, nick, sex, fuid, GROUP_CHAT_TYPE, "[群猜拳]", chatentity.getCreateTime());
			} else {
				isHaveChatRecord(activity, nick, sex, fuid, PRIVATE_CHAT_TYPE, sessionMsg, chatentity.getCreateTime());
			}
			chatDatabase = null;
		}

		new RequestPlayFingerGuessingWithAnte().playFingerGuessingWithAnte(info.auth, BAApplication.app_version_code, info.uid.intValue(), requestId,
				fingerInfo.ante.intValue(), finger1, new String(fingerInfo.globalid), this, chatentity, isGroupGuess, nick, sex, antetype);
	}

	@Override
	public void playFingerBack(int retcode, int uid, boolean isGroupGuess, ChatDatabaseEntity chatentity, FingerGuessingInfo info, String nick,
			int sex, int antetype) {
		if (chatentity == null) {
			return;
		}
		if (BAApplication.mLocalUserInfo == null) {
			return;
		}
		int queryId = uid;
		if (isGroupGuess) {
			queryId = chatentity.getToUid();
		}
		ChatOperate chatDatabase = ChatOperate.getInstance(activiy, queryId, isGroupGuess);

		if (retcode == rspContMsgType.E_GG_NOT_ENGOUH_WELTH) {//财富不够
			chatentity.setStatus(ChatStatus.SUCCESS.getValue());
			chatDatabase.delete(chatentity.getMesLocalID());
			handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_REMOVE_GUESS_FINGER_VALUE, chatentity));
			handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_WEALTH_NOT_ENGOUH_VALUE, antetype));
		} else if (retcode == rspContMsgType.E_GG_LACK_OF_SILVER) {//银币不足
			chatentity.setStatus(ChatStatus.SUCCESS.getValue());
			chatDatabase.delete(chatentity.getMesLocalID());
			handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_REMOVE_GUESS_FINGER_VALUE, chatentity));
			handler.sendMessage(handler.obtainMessage(rspContMsgType.E_GG_LACK_OF_SILVER, antetype));
		} else if (retcode == 0) {
			chatentity.setStatus(ChatStatus.SUCCESS.getValue());
			if (info != null) {//第一次发起
				String message = ChatMessageBiz.saveFingerMessage(info);
				if (chatDatabase != null) {
					ContentValues values = new ContentValues();
					values.put(ChatTable.Message, message);

					if (BAApplication.mLocalUserInfo != null) {
						if (info.uid2.intValue() == BAApplication.mLocalUserInfo.uid.intValue()) {//回复的猜拳
							values.put(ChatTable.CreateTime, info.playtime2.longValue() * 1000 - 10);
							chatentity.setCreateTime(info.playtime2.longValue() * 1000 - 10);
						}
					}
					String whereClause = ChatTable.MesLocalID + "=?";
					String[] whereArgs = new String[] { String.valueOf(chatentity.getMesLocalID()) };
					chatDatabase.update(values, whereClause, whereArgs);
				}

				if (isGroupGuess && info.winuid.intValue() != -1) {//回复的猜拳 //只有群猜拳才需要自己拼接结果
					ChatDatabaseEntity chatDatabaseEntity = new ChatDatabaseEntity();//拼接第二条猜拳结果数据
					chatDatabaseEntity.setStatus(ChatStatus.SUCCESS.getValue());//拼接本地数据
					chatDatabaseEntity.setDes(BAConstants.ChatDes.TO_ME.getValue());
					chatDatabaseEntity.setFromID(uid);
					chatDatabaseEntity.setToUid(queryId);
					chatDatabaseEntity.setMesSvrID("");
					chatDatabaseEntity.setProgress(0);
					if (antetype == 0) {
						chatDatabaseEntity.setType(MessageType.WITHANTEFINGER.getValue());
					} else {
						chatDatabaseEntity.setType(MessageType.NEWFINGER.getValue());
					}
					chatDatabaseEntity.setCreateTime(info.playtime2.longValue() * 1000);
					chatentity.setCreateTime(info.playtime2.longValue() * 1000 - 10);
					handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_APPEND_DATA_VALUE, chatDatabaseEntity));
					int type = BaseChatSendMessage.PRIVATE_CHAT_TYPE;
					if (isGroupGuess) {
						type = BaseChatSendMessage.GROUP_CHAT_TYPE;
					}
					SaveChatData.saveFingerGuessInfoMsg(activiy, type, sex, nick, chatDatabaseEntity, info);
				}
			}

		} else if (retcode == rspContMsgType.E_GG_FORBIT) {//被禁言
			chatentity.setStatus(ChatStatus.SUCCESS.getValue());
			HandlerUtils.sendHandlerMessage(handler, HandlerValue.CHAT_FORBIT_MESSAGE_VALUE);

		} else if (retcode == rspContMsgType.E_GG_FINGER_GUESSING_TIMEOUT || retcode == rspContMsgType.E_GG_FINGER_GUESSING_INVALID) {//猜拳回复失效了或者游戏结束
			chatentity.setStatus(ChatStatus.SUCCESS.getValue());
			chatDatabase.delete(chatentity.getMesLocalID());
			handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_GUESS_FINGER_TIME_OUT_VALUE, chatentity));
		} else if (retcode==rspContMsgType.E_GG_GRADE_RPS) {//等级不够
			chatentity.setStatus(ChatStatus.SUCCESS.getValue());
			chatDatabase.delete(chatentity.getMesLocalID());
			handler.sendMessage(handler.obtainMessage(HandlerValue.CHAT_REMOVE_GUESS_FINGER_VALUE, chatentity));
			handler.sendMessageAtFrontOfQueue(handler.obtainMessage(HandlerValue.CHAT_DARE_RPS_GRADE));
		}else {
			chatentity.setStatus(ChatStatus.FAILED.getValue());
		}
		ChatManageBiz.getInManage(activiy).changeMessageStatusByLocalID(activiy, queryId, chatentity.getStatus(), chatentity.getMesLocalID(),
				chatentity.getCreateTime(), isGroupGuess);
		Message msg = handler.obtainMessage();
		msg.what = HandlerValue.CHAT_REFLESH_UI_VALUE;
		msg.arg1 = retcode;
		handler.sendMessage(msg);

	}

}
