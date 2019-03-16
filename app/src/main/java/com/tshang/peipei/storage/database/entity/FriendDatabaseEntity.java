package com.tshang.peipei.storage.database.entity;

/**
 * @Title: 好友表对象类
 *
 * @Description: 插入、获取好友表内的内容
 *
 * @author allen
 *
 * @version V1.0   
 */
public class FriendDatabaseEntity {
	private int UserID;
	private String UserName;
	private String UserNick;
	private int Gender;
	private String Email;
	private String Mobile;
	private String Avatar;
	private int Type;
	private int LastChatTime;
	private String Memo;
	private String Mood;

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserNick() {
		return UserNick;
	}

	public void setUserNick(String userNick) {
		UserNick = userNick;
	}

	public int getGender() {
		return Gender;
	}

	public void setGender(int gender) {
		Gender = gender;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getAvatar() {
		return Avatar;
	}

	public void setAvatar(String avatar) {
		Avatar = avatar;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getLastChatTime() {
		return LastChatTime;
	}

	public void setLastChatTime(int lastChatTime) {
		LastChatTime = lastChatTime;
	}

	public String getMemo() {
		return Memo;
	}

	public void setMemo(String memo) {
		Memo = memo;
	}

	public String getMood() {
		return Mood;
	}

	public void setMood(String mood) {
		Mood = mood;
	}
}
