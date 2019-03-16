package com.tshang.peipei.model.broadcast;

import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.base.babase.BAHandler;

/**
 * @Title: BroadcaseMessage.java 
 *
 * @Description: 广播动画数据 
 *
 * @author allen  
 *
 * @date 2014-9-18 下午4:28:30 
 *
 * @version V1.0   
 */
public class BroadcastMessage {

	private int type;
	private BAHandler handler;
	private BroadcastInfo info;

	public BroadcastMessage(int type, BAHandler handler, BroadcastInfo info) {
		this.type = type;
		this.handler = handler;
		this.info = info;
	}

	public void sentMessage() {
		handler.sendMessageAtTime(handler.obtainMessage(type, info), 10);
	}

}
