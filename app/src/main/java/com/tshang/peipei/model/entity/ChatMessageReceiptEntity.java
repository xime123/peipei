package com.tshang.peipei.model.entity;

/**
 * @Title: ChatMessageReceiptEntity.java 
 *
 * @Description: 发送聊天消息时用到的数据对象 
 *
 * @author allen  
 *
 * @date 2014-6-16 下午2:56:02 
 *
 * @version V1.0   
 */
public class ChatMessageReceiptEntity {
	private long localId = -1;
	private int type = -1;
	private String burnId = "";
	private int fUid = -1;
	private long time = 0;
	private int fSex;
	private String fNick = "";

	public int getfSex() {
		return fSex;
	}

	public void setfSex(int fSex) {
		this.fSex = fSex;
	}

	public String getfNick() {
		return fNick;
	}

	public void setfNick(String fNick) {
		this.fNick = fNick;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getfUid() {
		return fUid;
	}

	public void setfUid(int fUid) {
		this.fUid = fUid;
	}

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBurnId() {
		return burnId;
	}

	public void setBurnId(String burnId) {
		this.burnId = burnId;
	}
}
