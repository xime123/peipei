package com.tshang.peipei.storage.database.table;

/**
 * @Title: 聊天表 只是字段，不是内容
 *
 * @Description: 声明聊天表字段，创建聊天表语句，删除聊天表语句 
 *
 * @author allen
 *
 * @version V1.0   
 */
public class ChatTable {

	public static final String TableVer = "TableVer";
	public static final String MesLocalID = "MesLocalID";
	public static final String MesSvrID = "MesSvrID";
	public static final String CreateTime = "CreateTime";
	public static final String FromID = "FromID";
	public static final String Message = "Message";
	public static final String Status = "Status";
	public static final String Type = "Type";
	public static final String Des = "Des";
	public static final String Progress = "Progress";
	public static final String GroupId = "GroupId";
	public static final String revstr1 = "revstr1";
	public static final String revstr2 = "revstr2";
	public static final String revstr3 = "revstr3";

	public static String getCreateSQL(String tablename) {
		String sql = "CREATE TABLE IF NOT EXISTS " + tablename + " ( " + MesLocalID + " INTEGER PRIMARY KEY NOT NULL, " + TableVer
				+ " INTEGER NOT NULL, " + MesSvrID + " TEXT NOT NULL, " + CreateTime + " INTEGER NOT NULL, " + FromID + " INTEGER NOT NULL, "
				+ Message + " TEXT," + Status + " INTEGER NOT NULL," + Type + " INTEGER," + Des + " INTEGER," + Progress + " INTEGER," + GroupId
				+ " INTEGER," + revstr1 + " TEXT," + revstr2 + " TEXT," + revstr3 + " TEXT);";

		return sql;
	}

	public static String getDropSQL(String tablename) {
		return "DROP TABLE IF EXISTS " + tablename;
	}

	public static String comluns() {
		return TableVer + "," + MesLocalID + "," + MesSvrID + "," + CreateTime + "," + FromID + "," + Message + "," + Status + "," + Type + "," + Des
				+ "," + Progress;
	}
}
