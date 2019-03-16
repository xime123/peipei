package com.tshang.peipei.storage.database.table;

import com.tshang.peipei.base.BaseLog;

/**
 * @Title: Phototable.java 
 *
 * @Description: 上传照片表,每上传一次都会把照片信息存入表中,当上传失败时读取表,重新上传
 *
 * @author vactor
 *
 * @date 2014-4-3 下午4:24:34 
 *
 * @version V1.0   
 */
public class Phototable {

	public static final String TABLE_NAME = "photo";

	private String id = "id";
	private String TableVer = "tablever";
	private String albumId = "albumid";
	private String userId = "userid";
	private String nickName = "nickname";
	private String gender = "gender";
	private String title = "title";
	private String desc = "desc";
	private String createtime = "createtime";
	private String province = "province";
	private String city = "city";
	private String detailAddress = "detailaddress";
	private String imageKeys = "imagekes";
	private String status = "status";
	private String errorcode = "errorcode";
	private String total = "total";

	private String colum1 = "colum1";
	private String colum2 = "colum2";
	private String colum3 = "colum3";
	private String colum4 = "colum4";
	private String colum5 = "colum5";
	private String colum6 = "colum6";

	public String getId() {
		return id;
	}

	public String getTableVer() {
		return TableVer;
	}

	public String getAlbumId() {
		return albumId;
	}

	public String getUserId() {
		return userId;
	}

	public String getNickName() {
		return nickName;
	}

	public String getGender() {
		return gender;
	}

	public String getTitle() {
		return title;
	}

	public String getDesc() {
		return desc;
	}

	public String getCreatetime() {
		return createtime;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public String getImageKeys() {
		return imageKeys;
	}

	public String getStatus() {
		return status;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public String getTotal() {
		return total;
	}

	public String getColum1() {
		return colum1;
	}

	public String getColum2() {
		return colum2;
	}

	public String getColum3() {
		return colum3;
	}

	public String getColum4() {
		return colum4;
	}

	public String getColum5() {
		return colum5;
	}

	public String getColum6() {
		return colum6;
	}

	public String getCreateSQL() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + id + " INTEGER PRIMARY KEY NOT NULL, " + TableVer + " INTEGER NOT NULL, "
				+ albumId + " INTEGER NOT NULL, " + userId + " INTEGER, " + nickName + " TEXT, " + gender + " INTEGER, " + title + " TEXT, " + desc
				+ " TEXT, " + province + " TEXT, " + city + " TEXT," + detailAddress + " TEXT," + imageKeys + " TEXT, " + status
				+ " INTEGER NOT NULL, " + createtime + " TEXT, " + errorcode + " INTEGER, " + total + " INTEGER, " + colum1 + " INTEGER, " + colum2
				+ " INTEGER, " + colum3 + " INTEGER, " + colum4 + " TEXT, " + colum5 + " TEXT, " + colum6 + " TEXT " + " );";
		BaseLog.i("vactor_log", sql);
		return sql;
	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
