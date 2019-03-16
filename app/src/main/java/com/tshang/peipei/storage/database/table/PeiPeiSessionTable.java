package com.tshang.peipei.storage.database.table;

/**
 * @Title: PeiPeiSessionTable.java 
 *
 * @Description: 私聊会话列表 
 *
 * @author allen  
 *
 * @date 2014-7-23 下午2:25:12 
 *
 * @version V1.0   
 */
public class PeiPeiSessionTable {
	public static final String TABLE_NAME = "peipeisession"; //会话列表表名

	public static final String TableVer = "TableVer"; //表的版本
	public static final String UserID = "UserID"; //用户uid 群聊为群id
	public static final String UnreadCount = "UnreadCount"; //未读条数
	public static final String LatestUpdateTime = "LatestUpdateTime"; //最新更改时间
	public static final String SessionData = "SessionData"; //回话内容
	public static final String Sex = "SessionSex"; // 用户性别
	public static final String Nick = "Nick"; // 用户名字 群聊为群名字
	public static final String Type = "Type"; //数据类型 0为私聊会话，1为群聊绘画

	public String getCreateSQL() {
		return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + UserID + " INTEGER PRIMARY KEY NOT NULL, " + TableVer + " INTEGER NOT NULL,"
				+ UnreadCount + " INTEGER NOT NULL, " + LatestUpdateTime + " INTEGER NOT NULL, " + Sex + " INTEGER , " + Nick + " TEXT , "
				+ SessionData + " TEXT , " + Type + " INTEGER DEFAULT 0);";

	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

	public String getColumns() {
		return TableVer + "," + UserID + "," + UnreadCount + "," + LatestUpdateTime + "," + SessionData + "," + Sex + "," + Nick;
	}

	public String getColumns4() {
		return TableVer + "," + UserID + "," + UnreadCount + "," + LatestUpdateTime + "," + SessionData + "," + Sex + "," + Nick + "," + Type;
	}
}
