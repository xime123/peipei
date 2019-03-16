package com.tshang.peipei.storage.database.entity;

/**
 * @Title: ReceiptEntity.java 
 *
 * @Description: 回执数据库对象
 *
 * @author allen  
 *
 * @date 2014-6-16 下午3:09:52 
 *
 * @version V1.0   
 */
public class ReceiptEntity {
	private String MesSvrID = "";
	private int FromID = -1;
	private String FNick = "";
	private String Nick = "";
	private int FSex = -1;
	private int Sex = -1;

	public String getMesSvrID() {
		return MesSvrID;
	}

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

	public int getFSex() {
		return FSex;
	}

	public void setFSex(int fSex) {
		FSex = fSex;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public void setMesSvrID(String mesSvrID) {
		MesSvrID = mesSvrID;
	}

	public int getFromID() {
		return FromID;
	}

	public void setFromID(int fromID) {
		FromID = fromID;
	}

}
