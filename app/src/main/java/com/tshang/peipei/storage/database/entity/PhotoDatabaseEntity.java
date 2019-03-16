package com.tshang.peipei.storage.database.entity;

/**
 * @Title: Phototable.java 
 *
 * @Description: 上传照片时,对应每一张照片信息
 *
 * @author vactor
 *
 * @date 2014-4-3 下午4:24:34 
 *
 * @version V1.0   
 */
public class PhotoDatabaseEntity {

	private int TableVer;
	private int albumId;
	private int userId;
	private String nickName;
	private int gender;
	private String title;
	private String desc;
	private String createtime;
	private String province;
	private String city;
	private String detailAddress;
	private String imageKeys;
	private int status;
	private int errorCode;
	private int total;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTableVer() {
		return TableVer;
	}

	public void setTableVer(int tableVer) {
		TableVer = tableVer;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public String getImageKeys() {
		return imageKeys;
	}

	public void setImageKeys(String imageKeys) {
		this.imageKeys = imageKeys;
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
