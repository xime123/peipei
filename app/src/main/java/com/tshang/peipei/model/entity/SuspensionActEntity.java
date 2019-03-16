package com.tshang.peipei.model.entity;

/**
 * @Title: SuspensionActEntity.java 
 *
 * @Description: TODO(需要显示悬浮穿的对应列表) 
 *
 * @author DYH  
 *
 * @date 2015-9-24 上午11:38:34 
 *
 * @version V1.0   
 */
public class SuspensionActEntity {
	
	public static final int SHOW_SUSP_ICON = 1;
	
	private int number;
	private int status;
	private String url;
	private String image;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
