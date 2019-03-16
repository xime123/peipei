package com.tshang.peipei.storage.database.table;

/**
 * @Title: ReceiptTable.java 
 *
 * @Description: 保存回执，失败时重发
 *
 * @author allen  
 *
 * @date 2014-6-16 下午3:03:19 
 *
 * @version V1.0   
 */
public class ReceiptTable {
	public String TABLE_NAME = "receipt";

	private String TableVer = "TableVer";
	private String MesSvrID = "MesSvrID";
	private String FromID = "FromID";
	private String FNick = "FNick";
	private String Nick = "Nick";
	private String FSex = "FSex";
	private String Sex = "Sex";

	public String getFNick() {
		return FNick;
	}

	public void setFNick(String fNick) {
		FNick = fNick;
	}

	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

	public String getFSex() {
		return FSex;
	}

	public void setFSex(String fSex) {
		FSex = fSex;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getTableVer() {
		return TableVer;
	}

	public void setTableVer(String tableVer) {
		TableVer = tableVer;
	}

	public String getMesSvrID() {
		return MesSvrID;
	}

	public void setMesSvrID(String mesSvrID) {
		MesSvrID = mesSvrID;
	}

	public String getFromID() {
		return FromID;
	}

	public void setFromID(String fromID) {
		FromID = fromID;
	}

	public String getCreateSQL() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + MesSvrID + " TEXT NOT NULL, " + TableVer + " INTEGER NOT NULL," + FromID
				+ " INTEGER NOT NULL, " + FNick + " TEXT NOT NULL, " + Nick + " TEXT NOT NULL, " + FSex + " INTEGER NOT NULL, " + Sex
				+ " INTEGER NOT NULL);";

		return sql;
	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

}
