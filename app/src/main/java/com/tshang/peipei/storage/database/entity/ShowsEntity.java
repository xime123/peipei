package com.tshang.peipei.storage.database.entity;

/**
 * @Title: ShowsEntity.java 
 *
 * @Description: 秀场数据内容 
 *
 * @author allen  
 *
 * @date 2015-1-21 上午10:28:00 
 *
 * @version V1.0   
 */
public class ShowsEntity {
	public String data;//内容
	public int type;//类型,语音图片或视频
	public int status;//状态。已读未读

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

}
