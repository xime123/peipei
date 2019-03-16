package com.tshang.peipei.activity.reward.entity;

/**
 * @Title: BindAnonymNickEntity.java 
 *
 * @Description: 绑定Nick 返回处理实体类 
 *
 * @author Administrator  
 *
 * @date 2015-11-28 下午6:14:39 
 *
 * @version V1.0   
 */
public class BindAnonymNickEntity {

	private int code;
	private String message;
	private int nickId;
	private String nick;
	private int status;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getNickId() {
		return nickId;
	}

	public void setNickId(int nickId) {
		this.nickId = nickId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
