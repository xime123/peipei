package com.tshang.peipei.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 *类        名 : AlbumEntity.java
 *功能描述 : 相册数据模型,封装本地相册数据
 *作　    者 : vactor
 *设计日期 :2014 2014-3-25 上午11:16:09
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
@SuppressWarnings("serial")
public class AlbumEntity implements Serializable {

	private String albumname;

	private List<PhotoEntity> list = new ArrayList<PhotoEntity>();

	public List<PhotoEntity> getList() {
		return list;
	}

	public void setList(List<PhotoEntity> list) {
		this.list = list;
	}

	public String getAlbumname() {
		return albumname;
	}

	public void setAlbumname(String albumname) {
		this.albumname = albumname;
	}


}


