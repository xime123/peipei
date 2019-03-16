package com.tshang.peipei.storage.database.table;

/**
 * @Title: ShowsTable.java 
 *
 * @Description: 秀场数据 
 *
 * @author allen  
 *
 * @date 2015-1-21 上午10:11:52 
 *
 * @version V1.0   
 */
public class ShowsTable {
	public static String TABLE_NAME = "showstable";

	public static String TableVer = "TableVer";//表版本
	public static String data = "data";//内容
	public static String type = "type";//类型,语音图片或视频
	public static String status = "status";//状态: 0，未读，1已读

	public String getCreateSQL() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + TableVer + " INTEGER NOT NULL," + data + " TEXT," + type
				+ " INTEGER NOT NULL," + status + " INTEGER);";

		return sql;
	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
