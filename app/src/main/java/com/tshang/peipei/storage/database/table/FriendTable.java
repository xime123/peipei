package com.tshang.peipei.storage.database.table;

/**
 * @Title: 好友表
 *
 * @Description: 声明好友表字段，创建好友表语句，删除好友表语句 
 *
 * @author allen
 *
 * @version V1.0   
 */
public class FriendTable {

	public static String TABLE_NAME = "friend";

	private String TableVer = "TableVer";
	private String UserID = "UserID";
	private String UserName = "UserName";
	private String UserNick = "UserNick";
	private String Gender = "Gender";
	private String Email = "Email";
	private String Mobile = "Mobile";
	private String Avatar = "Avatar";
	private String Type = "Type";
	private String LastChatTime = "LastChatTime";
	private String Memo = "Memo";
	private String Mood = "Mood";

	public String getUserID() {
		return UserID;
	}

	public String getUserName() {
		return UserName;
	}

	public String getUserNick() {
		return UserNick;
	}

	public String getGender() {
		return Gender;
	}

	public String getEmail() {
		return Email;
	}

	public String getMobile() {
		return Mobile;
	}

	public String getAvatar() {
		return Avatar;
	}

	public String getType() {
		return Type;
	}

	public String getLastChatTime() {
		return LastChatTime;
	}

	public String getMemo() {
		return Memo;
	}

	public String getMood() {
		return Mood;
	}

	public String getTableVer() {
		return TableVer;
	}

	public String getCreateSQL() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + UserID + " INTEGER PRIMARY KEY NOT NULL, " + TableVer
				+ " INTEGER NOT NULL," + UserName + " TEXT, " + UserNick + " TEXT," + Gender + " INTEGER NOT NULL," + Email + " TEXT," + Mobile
				+ " TEXT," + Mood + " TEXT," + Avatar + " TEXT," + Type + " INTEGER NOT NULL," + LastChatTime + " INTEGER NOT NULL," + Memo
				+ " TEXT);";

		return sql;
	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
