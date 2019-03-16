package com.tshang.peipei.storage.database.entity;

/**
 * @Title: NewDynamicReplyEntity.java 
 *
 * @Description: 新动态评论 
 *
 * @author Aaron  
 *
 * @date 2015-9-17 下午4:38:56 
 *
 * @version V1.0   
 */
public class NewDynamicReplyEntity {

	private int type;//类型
	private int fromuid;
	private int topicuid;
	private int topicid;
	private int commentuid;//评论id
	private int auditstatus;//审核状态 0通过
	private String nick;
	private String headpickey;
	private int sex;
	private long createtime;
	private String replyContent;//回复内容
	private String dynamicContent;//动态内容
	private String imageKey;//图片kdy
	private String imei;
	//预留字段
	private int revint0;
	private int revint1;
	private int revint2;
	private int revint3;
	private String revstr0;
	private String revstr1;
	private String revstr2;
	private String revstr3;

	private int status;//记录已读 未读状态
	private int globalid;//动态全局id
	private String color;//背景颜色
	private int fonttype;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFromuid() {
		return fromuid;
	}

	public void setFromuid(int fromuid) {
		this.fromuid = fromuid;
	}

	public int getTopicuid() {
		return topicuid;
	}

	public void setTopicuid(int topicuid) {
		this.topicuid = topicuid;
	}

	public int getTopicid() {
		return topicid;
	}

	public void setTopicid(int topicid) {
		this.topicid = topicid;
	}

	public int getCommentuid() {
		return commentuid;
	}

	public void setCommentuid(int commentuid) {
		this.commentuid = commentuid;
	}

	public int getAuditstatus() {
		return auditstatus;
	}

	public void setAuditstatus(int auditstatus) {
		this.auditstatus = auditstatus;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getHeadpickey() {
		return headpickey;
	}

	public void setHeadpickey(String headpickey) {
		this.headpickey = headpickey;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getDynamicContent() {
		return dynamicContent;
	}

	public void setDynamicContent(String dynamicContent) {
		this.dynamicContent = dynamicContent;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getRevint0() {
		return revint0;
	}

	public void setRevint0(int revint0) {
		this.revint0 = revint0;
	}

	public int getRevint1() {
		return revint1;
	}

	public void setRevint1(int revint1) {
		this.revint1 = revint1;
	}

	public int getRevint2() {
		return revint2;
	}

	public void setRevint2(int revint2) {
		this.revint2 = revint2;
	}

	public int getRevint3() {
		return revint3;
	}

	public void setRevint3(int revint3) {
		this.revint3 = revint3;
	}

	public String getRevstr0() {
		return revstr0;
	}

	public void setRevstr0(String revstr0) {
		this.revstr0 = revstr0;
	}

	public String getRevstr1() {
		return revstr1;
	}

	public void setRevstr1(String revstr1) {
		this.revstr1 = revstr1;
	}

	public String getRevstr2() {
		return revstr2;
	}

	public void setRevstr2(String revstr2) {
		this.revstr2 = revstr2;
	}

	public String getRevstr3() {
		return revstr3;
	}

	public void setRevstr3(String revstr3) {
		this.revstr3 = revstr3;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getGlobalid() {
		return globalid;
	}

	public void setGlobalid(int globalid) {
		this.globalid = globalid;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getFonttype() {
		return fonttype;
	}

	public void setFonttype(int fonttype) {
		this.fonttype = fonttype;
	}
}
