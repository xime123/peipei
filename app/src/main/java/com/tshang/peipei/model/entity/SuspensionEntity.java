package com.tshang.peipei.model.entity;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

/**
 * @Title: SuspensionEntity.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2015-9-16 下午6:37:20 
 *
 * @version V1.0   
 */
public class SuspensionEntity {
	public static final int ACTIVITY_HALL = 1;
	public static final int ACTIVITY_BOARDCAST = 2;
	public static final int ACTIVITY_GAME = 3;
	public static final int ACTIVITY_RANK_NEW = 4;
	public static final int ACTIVITY_RANK_FEMALE = 5;
	public static final int ACTIVITY_RANK_MALE = 6;
	public static final int ACTIVITY_RANK_GAME = 7;
	
	/**
	 * 是否显示
	 */
	private int show;
	/**
	 * 下一次刷新显示的时间戳
	 */
	private long nextTime;
	/**
	 * 对应的activity和悬浮穿
	 */
	private List<SuspensionActEntity> actList = new ArrayList<SuspensionActEntity>();
	
	public int getShow() {
		return show;
	}
	
	public void setShow(int show) {
		this.show = show;
	}
	
	public long getNextTime() {
		return nextTime;
	}
	
	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}
	
	public List<SuspensionActEntity> getActList() {
		return actList;
	}
	
	public SuspensionActEntity getActEntifyForNumber(int number){
		SuspensionActEntity mActEntify = null;
		
		for(SuspensionActEntity entity : actList){
			if(entity.getNumber() == number){
				mActEntify = entity;
				break;
			}
		}
		
		return mActEntify;
	}

}
