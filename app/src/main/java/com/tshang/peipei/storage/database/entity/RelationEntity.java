package com.tshang.peipei.storage.database.entity;

/**
 * @Title: RelationEntity.java 
 *
 * @Description: 忠诚度 (暂时不用)
 *
 * @author allen  
 *
 * @date 2014-12-20 下午3:54:22 
 *
 * @version V1.0   
 */
public class RelationEntity {
	private int toUid;
	private int Relationship;//用户忠诚度
	private int chatthreshold;//聊天门槛
	private int isUpdate;//0不拉取，1拉取
	private long RelationshipTime;
	private int verInt;//预留
	private String verStr;//预留

	public long getRelationshipTime() {
		return RelationshipTime;
	}

	public void setRelationshipTime(long relationshipTime) {
		RelationshipTime = relationshipTime;
	}

	public int getToUid() {
		return toUid;
	}

	public void setToUid(int toUid) {
		this.toUid = toUid;
	}

	public int getRelationship() {
		return Relationship;
	}

	public void setRelationship(int relationship) {
		Relationship = relationship;
	}

	public int getChatthreshold() {
		return chatthreshold;
	}

	public void setChatthreshold(int chatthreshold) {
		this.chatthreshold = chatthreshold;
	}

	public int getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}

	public int getVerInt() {
		return verInt;
	}

	public void setVerInt(int verInt) {
		this.verInt = verInt;
	}

	public String getVerStr() {
		return verStr;
	}

	public void setVerStr(String verStr) {
		this.verStr = verStr;
	}
}
