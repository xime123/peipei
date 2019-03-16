package com.tshang.peipei.model.entity;

import java.io.Serializable;

/**
 * @Title: PhotoEntity.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author aaa  
 *
 * @date 2014-4-10 下午3:58:33 
 *
 * @version V1.0   
 */
//相片信息
@SuppressWarnings("serial")
public class PhotoEntity implements Serializable {

	private String id;
	private String path;
	private String title;
	private String desc;
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
