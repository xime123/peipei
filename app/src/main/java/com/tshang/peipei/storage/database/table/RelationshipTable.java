package com.tshang.peipei.storage.database.table;

/**
 * @Title: RelationshipTable.java 
 *
 * @Description: 忠诚度
 *
 * @author allen  
 *
 * @date 2014-12-20 下午3:40:54 
 *
 * @version V1.0   
 */
public class RelationshipTable {
	public static final String TABLE_NAME = "RelationshipTable";
	public static final String TableVer = "TableVer";
	public static final String toUid = "toUid";
	public static final String Relationship = "Relationship";
	public static final String chatthreshold = "chatthreshold";
	public static final String isUpdate = "isUpdate";
	public static final String RelationshipTime = "RelationshipTime";
	public static final String verInt = "verInt";
	public static final String verStr = "verStr";

	public String getCreateSQL() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + toUid + " INTEGER PRIMARY KEY NOT NULL, " + TableVer
				+ " INTEGER NOT NULL, " + Relationship + " INTEGER NOT NULL, " + chatthreshold + " INTEGER NOT NULL, " + isUpdate
				+ " INTEGER DEFAULT 0, " + RelationshipTime + " INTEGER NOT NULL, " + verStr + " TEXT," + verInt + " INTEGER NOT NULL);";

		return sql;
	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

}
