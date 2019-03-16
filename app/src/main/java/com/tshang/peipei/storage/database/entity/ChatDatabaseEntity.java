package com.tshang.peipei.storage.database.entity;

/**
 * @Title: 聊天表对象类
 *
 * @Description: 插入、获取聊天表内的内容
 *
 * @author allen
 *
 * @version V1.0   
 */
public class ChatDatabaseEntity {

	private long MesLocalID;//本地数据库ID
	private String MesSvrID;//服务器ID，阅后即焚使用
	private long CreateTime;//
	private int FromID;//对方UId
	private int TOID;//用于女神技对方的ID
	private String Message;//发送内容
	private int Status;//发送状态
	private int Type;//类型
	private int Des;//0我发送，1对方发给我
	private int Progress;//进度
	private int FingerClick = -1;//猜拳
	private int touid = -1;//发送给对方的uid
	private String revStr1 = "";//性别
	private String revStr2 = "";//昵称
	private String revStr3 = "";

	public int getToUid() {
		return touid;
	}

	public void setToUid(int touid) {
		this.touid = touid;
	}

	public String getRevStr1() {
		return revStr1;
	}

	public void setRevStr1(String revStr1) {
		this.revStr1 = revStr1;
	}

	public String getRevStr2() {
		return revStr2;
	}

	public void setRevStr2(String revStr2) {
		this.revStr2 = revStr2;
	}

	public String getRevStr3() {
		return revStr3;
	}

	public void setRevStr3(String revStr3) {
		this.revStr3 = revStr3;
	}

	public int getFingerClick() {
		return FingerClick;
	}

	public void setFingerClick(int fingerClick) {
		FingerClick = fingerClick;
	}

	public long getMesLocalID() {
		return MesLocalID;
	}

	public void setMesLocalID(long mesLocalID) {
		MesLocalID = mesLocalID;
	}

	public String getMesSvrID() {
		return MesSvrID;
	}

	public void setMesSvrID(String mesSvrID) {
		MesSvrID = mesSvrID;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public int getFromID() {
		return FromID;
	}

	public void setFromID(int fromID) {
		FromID = fromID;
	}
	
	public int getTOID() {
		return TOID;
	}
	
	public void setTOID(int tOID) {
		TOID = tOID;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getDes() {
		return Des;
	}

	public void setDes(int des) {
		Des = des;
	}

	public int getProgress() {
		return Progress;
	}

	public void setProgress(int progress) {
		Progress = progress;
	}

}
