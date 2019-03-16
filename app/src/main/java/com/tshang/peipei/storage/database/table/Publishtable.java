package com.tshang.peipei.storage.database.table;

import com.tshang.peipei.base.BaseLog;

/**
 * @Title: Phototable.java 
 *
 * @Description: 写贴表
 *
 * @author vactor
 *
 * @date 2014-4-11 下午4:24:34 
 *
 * @version V1.0   
 */
public class Publishtable {

	public static final String TABLE_NAME = "publish";

	private String id = "id";
	private String TableVer = "TableVer";
	private String topicId = "topicid";
	private String userId = "userid";
	private String nickName = "nickname";
	private String gender = "gender";
	private String createtime = "createtime";
	private String province = "province";
	private String city = "city";
	private String detailAddress = "detailaddress";
	private String content = "content";
	//存储发贴的所有图片的一个路径,以";"作为分隔符
	private String imageKeys = "imagekeys";
	private String type = "type";
	//状态,0:等待上传1:上传成功,-1:上传失败
	private String status = "status";
	private String errorCode = "errorCode";

	private String colum1 = "colum1";
	private String colum2 = "colum2";
	private String colum3 = "colum3";
	private String colum4 = "colum4";
	private String colum5 = "colum5";
	private String colum6 = "colum6";

	public String getId() {
		return id;
	}

	public static String getTableName() {
		return TABLE_NAME;
	}

	public String getTableVer() {
		return TableVer;
	}

	public String getTopicId() {
		return topicId;
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

	public String getContent() {
		return content;
	}

	public String getImageKeys() {
		return imageKeys;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public String getErrorCode() {
		return errorCode;
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
		BaseLog.i("vactor_log", "create publish table");
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + id + " INTEGER PRIMARY KEY NOT NULL, " + TableVer + " INTEGER NOT NULL, "
				+ topicId + " TEXT, " + userId + " INTEGER NOT NULL, " + nickName + " TEXT, " + gender + " INTEGER, " + province + " TEXT, "
				+ city + " TEXT, " + detailAddress + " TEXT, " + imageKeys + " TEXT, " + type + " INTEGER NOT NULL, " + content + " TEXT, " + status
				+ " INTEGER NOT NULL, " + errorCode + " INTEGER, " + createtime + " TEXT, " + colum1 + " INTEGER, " + colum2 + " INTEGER, " + colum3
				+ " INTEGER, " + colum4 + " TEXT, " + colum5 + " TEXT, " + colum6 + " TEXT " + " );";
		return sql;
	}

	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

}
