package com.tshang.peipei.activity.skillnew.bean;

/**
 * @Title: SkillResultBean.java 
 *
 * @Description: 用于传输响应女神技能邀请的实体类 
 *
 * @author DYD  
 *
 * @date 2015-11-13 下午5:57:28 
 *
 * @version V1.0   
 */
public class SkillResultBean {
	private int skilllistid;
	private int invitedstatus;
	private long createtime;
	
	public int getSkilllistid() {
		return skilllistid;
	}
	public void setSkilllistid(int skilllistid) {
		this.skilllistid = skilllistid;
	}
	public int getInvitedstatus() {
		return invitedstatus;
	}
	public void setInvitedstatus(int invitedstatus) {
		this.invitedstatus = invitedstatus;
	}
	
	public long getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	
	
}
