package com.tshang.peipei.network.socket;

import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;

/*
 *类        名 : PeiPeiRequest.java
 *功能描述 : 工作对象类,每一个任务即一个task
 *作　    者 : vactor
 *设计日期 :2014-3-19 下午4:43:24
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class PeiPeiRequest {

	// 要发送的消息
	private byte[] msg;

	// 消息回调接口
	private ISocketMsgCallBack callBack;

	// 该任务是否为长连接
	private boolean mIsLongConn;

	// 长连接对象,整个程序只能有一个该对象
	private static PeiPeiSocketImpl longSocket;

	private boolean is_create_conn = false;
	private String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
	private int port = BAConstants.PEIPEI_SERVER_PORT;

	public boolean isIs_create_conn() {
		return is_create_conn;
	}

	public void setIs_create_conn(boolean is_create_conn) {
		this.is_create_conn = is_create_conn;
	}

	/**
	 * 新建一个任务
	 * @param msg 要发送的消息
	 * @param callBack 回调
	 * @param is_long_conn 是否使用长连接
	 */
	public PeiPeiRequest(byte[] msg, ISocketMsgCallBack callBack, boolean is_long_conn) {
		this.mIsLongConn = is_long_conn;
		this.msg = msg;
		this.callBack = callBack;
		if (BAConstants.IS_TEST) {//测试服务器
			host = BAConstants.PEIPEI_SERVER_HOST_TEST;
		} else {
			host = BAConstants.PEIPEI_SERVER_HOST_PRO;
		}
	}

	public PeiPeiRequest(byte[] msg, ISocketMsgCallBack callBack, boolean is_long_conn, String host, int port) {
		this.mIsLongConn = is_long_conn;
		this.msg = msg;
		this.callBack = callBack;
		this.host = host;
		this.port = port;
	}

	/**
	 * 执行,第一次使用要收回包,所以第一次要send and read ,以后所有的长连接发送消息只发不收,
	 * 一旦建立长连接,所有的回包都在read方法接收到之后自动完成
	 */
	public void excuteTask() {
		if (mIsLongConn) {
			synchronized (PeiPeiRequest.this) {

				if (null == longSocket) {
					String longHost = BAConstants.PEIPEI_SERVER_HOST_PRO;
					if (BAConstants.IS_TEST) {//测试服务器
						longHost = BAConstants.PEIPEI_SERVER_HOST_TEST;
					} else {
						longHost = BAConstants.PEIPEI_SERVER_HOST_PRO;
					}
					longSocket = new PeiPeiSocketImpl(mIsLongConn, longHost, BAConstants.PEIPEI_SERVER_PORT);
					longSocket.sendAndRecv(msg, callBack);
				} else {
					if (is_create_conn) {
						//						BaseLog.i("long_conn", "--------重复链接，抛弃--------");
						return;
					}
					longSocket.send(msg);
				}
			}
		} else {
			PeiPeiSocketImpl ydmxSocket = new PeiPeiSocketImpl(mIsLongConn, host, port);
			//			System.out.println("host====" + host + "--------" + port);
			ydmxSocket.sendAndRecv(msg, callBack);
			ydmxSocket.colseSocket(true);

		}

	}

	public static void close() {
		if (null != longSocket) {
			longSocket.setIsLongConn(false);
			longSocket.colseSocket(true);
			longSocket = null;
		}
	}

}
