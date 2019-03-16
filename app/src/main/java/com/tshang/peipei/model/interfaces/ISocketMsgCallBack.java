package com.tshang.peipei.model.interfaces;

/*
 *类        名 : ISocketMsgCallBack.java
 *功能描述 : socket 消息回调接口
 *作　    者 : vactor
 *设计日期 : 2014-3-18 下午2:25:12
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public interface ISocketMsgCallBack {

	// 网络错误
	public final int NETWORK_ERROR = -10;

	// 超时
	public final int TIME_OUT = -20;

	// 异常包
	public final int PACKET_ERROR = -30;
	//网络重现
	public final int RECONNECT = -40;

	/**
	 * 回包成功
	 * 
	 * @param msg
	 *            回包byte[]
	 */
	public void succuess(byte[] msg);

	/**
	 * 回包失败
	 * 
	 * @param resultCode
	 *            错误码 -1: 网络错误,-2: 超时 -3:异常包
	 */
	public void error(int resultCode);

}
