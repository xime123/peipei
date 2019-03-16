package com.tshang.peipei.storage.database.table;

/**
 * @Title: 聊天会话列表表
 *
 * @Description: 声明聊天会话列表表字段，创建会话表语句，删除会话表语句 
 *
 * @author allen
 *
 * @version V1.0   
 */
public class SessionTable {

	public static String TABLE_NAME = "session";

	private String TableVer = "TableVer";
	private String UserID = "UserID";
	private String UnreadCount = "UnreadCount";
	private String LatestUpdateTime = "LatestUpdateTime";
	private String SessionData = "SessionData";

	public String getUserID() {
		return UserID;
	}

	public String getTableVer() {
		return TableVer;
	}

	public String getUnreadCount() {
		return UnreadCount;
	}

	public String getLatestUpdateTime() {
		return LatestUpdateTime;
	}

	public String getSessionData() {
		return SessionData;
	}

	public String getCreateSQL() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + UserID + " INTEGER PRIMARY KEY NOT NULL, " + TableVer
				+ " INTEGER NOT NULL," + UnreadCount + " INTEGER NOT NULL, " + LatestUpdateTime + " INTEGER NOT NULL," + SessionData + " TEXT);";

		return sql;
	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
