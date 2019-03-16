package com.tshang.peipei.storage.database.entity;

/**
 * @Title: 会话列表表对象类
 *
 * @Description: 插入、获取会话列表表内的内容
 *
 * @author allen
 *
 * @version V1.0   
 */
public class SessionDatabaseEntity {
	private int UserID;
	private int UnreadCount;
	private long LatestUpdateTime;
	private String SessionData;
	private boolean isCheck;
	private String Nick;
	private int sex;
	private int type;//0为私聊，1为群聊,3匿名私聊

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;

	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public int getUnreadCount() {
		return UnreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		UnreadCount = unreadCount;
	}

	public long getLatestUpdateTime() {
		return LatestUpdateTime;
	}

	public void setLatestUpdateTime(long latestUpdateTime) {
		LatestUpdateTime = latestUpdateTime;
	}

	public String getSessionData() {
		return SessionData;
	}

	public void setSessionData(String sessionData) {
		SessionData = sessionData;
	}
}
