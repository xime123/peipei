package com.tshang.peipei.storage.database.table;

/**
 * @Title: NewDynamicReplyTable.java 
 *
 * @Description: 新动态回复字段 
 *
 * @author Aaron  
 *
 * @date 2015-9-17 下午4:49:27 
 *
 * @version V1.0   
 */
public class NewDynamicReplyTable {

	public static final String TABLE_NAME = "newDynamicReplyTable";//表

	//	public static final String TableVer = "TableVer"; //表的版本
	public static final String Type = "type";
	public static final String Fromuid = "fromuid";
	public static final String Topicuid = "topicuid";
	public static final String Topicid = "topicid";
	public static final String Commentuid = "commentuid";
	public static final String Auditstatus = "auditstatus";
	public static final String Nick = "nick";
	public static final String Headpickey = "headpickey";
	public static final String Sex = "sex";
	public static final String Createtime = "createtime";
	public static final String ReplyContent = "replyContent";
	public static final String DynamicContent = "dynamicContent";
	public static final String ImageKey = "imageKey";
	public static final String Imei = "imei";

	public static final String Revint0 = "revint0";
	public static final String Revint1 = "revint1";
	public static final String Revint2 = "revint2";
	public static final String Revint3 = "revint3";

	public static final String Revstr0 = "revstr0";
	public static final String Revstr1 = "revstr1";
	public static final String Revstr2 = "revstr2";
	public static final String Revstr3 = "revstr3";

	public static final String STUTAS = "status";
	public static final String GLOBALID = "globalid";
	public static final String COLOR = "color";
	public static final String FONTTYPE = "fonttype";

	public String getCreateSQL() {
		return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + Type + " INTEGER NOT NULL, " + Fromuid + " INTEGER NOT NULL, " + Topicuid
				+ " INTEGER , " + Topicid + " INTEGER , " + Commentuid + " INTEGER , " + Auditstatus + " INTEGER , " + Nick + " TEXT , " + Headpickey
				+ " TEXT , " + Sex + " INTEGER , " + Createtime + " LONG , " + ReplyContent + " TEXT , " + DynamicContent + " TEXT , " + ImageKey
				+ " TEXT , " + Imei + " TEXT," + Revint0 + " INTEGER DEFAULT 0," + Revint1 + " INTEGER DEFAULT 0," + Revint2 + " INTEGER DEFAULT 0,"
				+ Revint3 + " INTEGER DEFAULT 0," + Revstr0 + " TEXT," + Revstr1 + " TEXT," + Revstr2 + " TEXT," + Revstr3 + " TEXT ," + STUTAS
				+ "  INTEGER DEFAULT 0," + GLOBALID + " INTEGER , " + COLOR + " TEXT," + FONTTYPE + " INTEGER  );";

	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
