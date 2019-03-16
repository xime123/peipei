package com.tshang.peipei.storage.database.table;

/**
 * @Title: DynamicTable.java 
 *
 * @Description: 我的动态数据表
 *
 * @author allen  
 *
 * @date 2014-12-3 上午10:25:45 
 *
 * @version V1.0   
 */
public class DynamicTable {
	public static final String TABLE_NAME = "dynamictable"; //会话列表表名

	public static final String TableVer = "TableVer"; //表的版本
	public static final String UserID = "UserID"; //用户uid 群聊为群id
	public static final String LatestUpdateTime = "LatestUpdateTime"; //最新更改时间
	public static final String SessionData = "SessionData"; //回话内容
	public static final String Sex = "SessionSex"; // 用户性别
	public static final String Nick = "Nick"; // 用户名字 群聊为群名字
	public static final String Type = "Type"; 
	public static final String VerStr = "VerStr";
	public static final String Topicid = "Topicid";
	public static final String TopicUid = "TopicUid";

	public String getCreateSQL() {
		return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + UserID + " INTEGER NOT NULL, " + TableVer + " INTEGER NOT NULL,"
				+ LatestUpdateTime + " INTEGER NOT NULL, " + Sex + " INTEGER , " + Nick + " TEXT , " + SessionData + " TEXT , " + Type
				+ " INTEGER DEFAULT 0 ," + VerStr + " TEXT ," + Topicid + " INTEGER DEFAULT 0," + TopicUid + " INTEGER DEFAULT 0 );";

	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
