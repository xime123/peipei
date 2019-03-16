package com.tshang.peipei.model.redpacket2;

/**
 * @Title: Redpacket.java 
 *
 * @Description: 接龙红包实体类 
 *
 * @author DYH  
 *
 * @date 2016-1-20 下午10:59:04 
 *
 * @version V1.0   
 */
public class SolitaireRedpacket {
	private int redpacketId;
	private long gold;
	private long silver;
	private int jionPerson;
	public int getRedpacketId() {
		return redpacketId;
	}
	public void setRedpacketId(int redpacketId) {
		this.redpacketId = redpacketId;
	}
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	public long getSilver() {
		return silver;
	}
	public void setSilver(long silver) {
		this.silver = silver;
	}
	public int getJionPerson() {
		return jionPerson;
	}
	public void setJionPerson(int jionPerson) {
		this.jionPerson = jionPerson;
	}
	
	
}
