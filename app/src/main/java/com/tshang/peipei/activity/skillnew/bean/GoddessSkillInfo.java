package com.tshang.peipei.activity.skillnew.bean;

/**
 * @Title: GoddessSkillInfo.java 
 *
 * @Description: 用于封装女神技能列表页面的信息 
 *
 * @author DYH  
 *
 * @date 2015-11-11 下午5:54:42 
 *
 * @version V1.0   
 */
public class GoddessSkillInfo {
	private int isopenskill;
	private int totalskillnum;
	private int selectedskillnum;
	
	private Object object;

	public int getIsopenskill() {
		return isopenskill;
	}

	public void setIsopenskill(int isopenskill) {
		this.isopenskill = isopenskill;
	}

	public int getTotalskillnum() {
		return totalskillnum;
	}

	public void setTotalskillnum(int totalskillnum) {
		this.totalskillnum = totalskillnum;
	}

	public int getSelectedskillnum() {
		return selectedskillnum;
	}

	public void setSelectedskillnum(int selectedskillnum) {
		this.selectedskillnum = selectedskillnum;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	
}
