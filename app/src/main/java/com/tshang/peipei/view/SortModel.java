package com.tshang.peipei.view;

import com.tshang.peipei.protocol.asn.gogirl.RetFollowInfo;

public class SortModel {

	private String name; // 显示的数据
	private String sortLetters; // 显示数据拼音的首字母
	private RetFollowInfo info;//原始数据

	public RetFollowInfo getInfo() {
		return info;
	}

	public void setInfo(RetFollowInfo info) {
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}