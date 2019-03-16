package com.tshang.peipei.model.biz.chat;

import android.app.Activity;

import com.tshang.peipei.base.babase.BAHandler;
import com.tshang.peipei.model.request.RequestPlayFingerGuessingWithAnte.iPlayFingerGuessing;
import com.tshang.peipei.protocol.asn.gogirl.FingerGuessingInfo;
import com.tshang.peipei.storage.database.entity.ChatDatabaseEntity;

/**
 * @Title: BaseChatSendMessage.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Jeff 聊天的基础逻辑累
 *
 * @date 2014年10月24日 下午3:27:53 
 *
 * @version V1.4.0   
 */
public class BaseChatSendMessage implements iPlayFingerGuessing {
	public static final int GROUP_CHAT_TYPE = 1;//群聊
	public static final int PRIVATE_CHAT_TYPE = 0;//私聊
	public static final int SHOW_CHAT_TYPE = 2;//秀场
	public static final int PRIVATE_CHAT_ANONYM_TYPE = 3;//匿名私聊

	protected BAHandler handler;
	protected Activity activiy;

	/**
	 * 
	 * @author Jeff
	 *
	 * @param activity
	 * @param toName
	 * @param sex
	 * @param toUid
	 * @param type 0是私聊，1是群聊
	 * @param sessionName
	 */
	public static void isHaveChatRecord(Activity activity, String toName, int sex, int toUid, int type, String sessionName, long time) {//判断是否之间有过聊天记录，有就改最后最后一条数据

		boolean b = ChatSessionManageBiz.isHaveSession(activity, toUid, type);
		if (b) {//存在就更新最后一条数据
			ChatSessionManageBiz.upDataSession(activity, sessionName, toName, toUid, time, type);
		} else {//不存在就插入一条新的数据
			ChatManageBiz.haveNotSession(activity, toUid, toName, sex, type, sessionName);
		}
	}

	@Override
	public void playFingerBack(int retcode, int uid, boolean isGroupGuess, ChatDatabaseEntity chatentity, FingerGuessingInfo info, String nick,
			int sex, int antetype) {}

}
