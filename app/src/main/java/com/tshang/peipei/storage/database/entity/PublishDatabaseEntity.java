package com.tshang.peipei.storage.database.entity;

/**
 * @Title: Phototable.java 
 *
 * @Description: 写贴时
 *
 * @author vactor
 *
 * @date 2014-4-3 下午4:24:34 
 *
 * @version V1.0   
 */
public class PublishDatabaseEntity {

	private int TableVer;
	private String topicId;
	private int userId;
	private String nickName;
	private int gender;
	private String createtime;
	private String province;
	private String city;
	private String detailAddress;
	private String content;
	private String imageKeys;
	private int type;
	private int status;
	private int errorCode;

	public int getTableVer() {
		return TableVer;
	}

	public void setTableVer(int tableVer) {
		TableVer = tableVer;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageKeys() {
		return imageKeys;
	}

	public void setImageKeys(String imageKeys) {
		this.imageKeys = imageKeys;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
