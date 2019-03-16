package com.tshang.peipei.storage.database.entity;

/**
 * @Title: DynamicEntity.java 
 *
 * @Description: 我的动态数据 
 *
 * @author allen  
 *
 * @date 2014-12-3 上午10:31:38 
 *
 * @version V1.0   
 */
public class DynamicEntity {
	private int UserID;
	private long LatestUpdateTime;
	private String SessionData;
	private String Nick;
	private int sex;
	private int type;//预留
	private String verStr;//预留
	private int topicId;//帖子id
	private int topicUid;//帖子uid

	public int getTopicUid() {
		return topicUid;
	}

	public void setTopicUid(int topicUid) {
		this.topicUid = topicUid;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getVerStr() {
		return verStr;
	}

	public void setVerStr(String verStr) {
		this.verStr = verStr;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
