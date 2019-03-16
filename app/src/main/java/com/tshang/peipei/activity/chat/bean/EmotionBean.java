package com.tshang.peipei.activity.chat.bean;

/**
 * @Title: HaremEmotionBean.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2014年12月4日 下午4:26:19 
 *
 * @version V1.0   
 */
public class EmotionBean {
	/** 表情资源图片对应的ID */
	private int id;

	/** 表情资源对应的文字描述 */
	private String character;

	/** 表情资源的文件名 */
	private String faceName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getFaceName() {
		return faceName;
	}

	public void setFaceName(String faceName) {
		this.faceName = faceName;
	}

}
