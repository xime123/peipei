package com.tshang.peipei.model.biz.chat;

import java.util.ArrayList;

import android.content.Context;

import com.tshang.peipei.model.request.RequestGetVisitorList;
import com.tshang.peipei.model.request.RequestGetVisitorList.IGetVisitorList;
import com.tshang.peipei.storage.database.entity.SessionDatabaseEntity;
import com.tshang.peipei.storage.database.operate.ChatOperate;
import com.tshang.peipei.storage.database.operate.PeipeiSessionOperate;
import com.tshang.peipei.storage.database.operate.RedpacketOperate;

/**
 * @Title: 聊天会话列表管理类
 *
 * @Description: 聊天会话列表管理类
 *
 * @author allen  
 *
 * @date 2014-4-4 上午10:37:57 
 *
 * @version V1.0   
 */
public class ChatSessionManageBiz {

	/**
	 * 添加会话
	 *
	 */
	public static void addChatSessionWithUserID(Context context, SessionDatabaseEntity sessionEntity) {
		PeipeiSessionOperate.getInstance(context).insert(sessionEntity);
	}

	/**
	 * 删除会话
	 *
	 */
	public static void removeChatSessionWithUserID(Context context, int userId, int type) {
		PeipeiSessionOperate sessionOperate = PeipeiSessionOperate.getInstance(context);
		if (sessionOperate != null) {
			sessionOperate.deleteByUserID(userId, type);
		}
		sessionOperate = null;

		ChatOperate chatOperate = null;
		RedpacketOperate redOperate = null;
		if (type == BaseChatSendMessage.PRIVATE_CHAT_TYPE) {
			chatOperate = ChatOperate.getInstance(context, userId, false);
		} else if (type == BaseChatSendMessage.GROUP_CHAT_TYPE) {
			chatOperate = ChatOperate.getInstance(context, userId, true);
			redOperate = RedpacketOperate.getInstance(context, userId);
		}
		if (chatOperate != null)
			chatOperate.deleteTable();
		if(redOperate != null)
			redOperate.deleteAll();
		chatOperate = null;
		redOperate = null;
	}

	/**
	 * 获取会话列表
	 *
	 */
	public static ArrayList<SessionDatabaseEntity> chatSessionDataWithRange(Context context) {
		return PeipeiSessionOperate.getInstance(context).selectChatList();
	}

	/**
	 * 是否存在未读内容
	 *
	 */
	public static int isExistUnreadMessage(Context context) {

		return PeipeiSessionOperate.getInstance(context).selectUnreadCount();
	}

	/**
	 * 判断是否有好友
	 *
	 * @param context
	 * @param uid
	 */
	public static boolean isHaveSession(Context context, int uid, int type) {
		return PeipeiSessionOperate.getInstance(context).isHaveSession(uid, type);
	}

	/**
	 * 升级聊天回话内容
	 *
	 * @param context
	 * @param sessionMsg
	 * @param nick
	 * @param dbfriend
	 */
	public static void upDataSession(Context context, String sessionMsg, String nick, int dbfriend, long time, int type) {
		PeipeiSessionOperate.getInstance(context).updateSessionData(sessionMsg, dbfriend, nick, time, type);
	}

	public void getVisitorList(byte[] auth, int ver, int uid, int hostuid, int start, int num, IGetVisitorList callback) {
		RequestGetVisitorList req = new RequestGetVisitorList();
		req.getVisitorList(auth, ver, uid, hostuid, start, num, callback);
	}
}
