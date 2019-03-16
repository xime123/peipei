package com.tshang.peipei.model.interfaces;

/**
 * 
 * @category 长连接
 * 
 */
public interface IPeiPeiSocket {

	/**
	 * 连接 socket 并发送连接消息
	 */
	public abstract void openConnect();

	/**
	 * 发送,接收消息
	 * 
	 * @param dispatcher
	 *            回调接口
	 * @param msg
	 *            要发送的消息对像
	 */
	public abstract void sendAndRecv(byte[] msg, ISocketMsgCallBack callBack);

	/**
	 * 关闭连接
	 */
	public abstract void colseSocket(boolean isChangeStatus);

}
