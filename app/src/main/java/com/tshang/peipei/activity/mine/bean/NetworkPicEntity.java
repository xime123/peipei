package com.tshang.peipei.activity.mine.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Title: NetworkPicEntity.java 
 *
 * @Description: 网络图片实体类 
 *
 * @author Aaron  
 *
 * @date 2015-7-31 上午11:46:40 
 *
 * @version V1.0   
 */
public class NetworkPicEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String thumbUrl;
	private String middleURL;
	private String largeTnImageUrl;
	private String objURL;
	private String hoverURL;

	public String getTumbUrl() {
		return thumbUrl;
	}

	public void setTumbUrl(String tumbUrl) {
		this.thumbUrl = tumbUrl;
	}

	public String getMiddleURL() {
		return middleURL;
	}

	public void setMiddleURL(String middleURL) {
		this.middleURL = middleURL;
	}

	public String getLargeTnImageUrl() {
		return largeTnImageUrl;
	}

	public void setLargeTnImageUrl(String largeTnImageUrl) {
		this.largeTnImageUrl = largeTnImageUrl;
	}

	public String getObjURL() {
		return objURL;
	}

	public void setObjURL(String objURL) {
		this.objURL = objURL;
	}

	public String getHoverURL() {
		return hoverURL;
	}

	public void setHoverURL(String hoverURL) {
		this.hoverURL = hoverURL;
	}
}
