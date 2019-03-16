package com.tshang.peipei.model.redpacket2;

/**
 * @Title: HallRedpacket.java 
 *
 * @Description: 大厅红包实体类 
 *
 * @author DYH  
 *
 * @date 2016-1-20 下午10:59:04 
 *
 * @version V1.0   
 */
public class HallRedpacket {
	private int redpacketId;
	private int redpacketType;
	private int userType;
	private String desc;
	public int getRedpacketId() {
		return redpacketId;
	}
	public void setRedpacketId(int redpacketId) {
		this.redpacketId = redpacketId;
	}
	public int getRedpacketType() {
		return redpacketType;
	}
	public void setRedpacketType(int redpacketType) {
		this.redpacketType = redpacketType;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
