package com.tshang.peipei.network.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlUserInfo;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.model.biz.PeiPeiPersistBiz;
import com.tshang.peipei.model.biz.baseviewoperate.UserUtils;
import com.tshang.peipei.model.biz.chat.ChatManageBiz;
import com.tshang.peipei.model.interfaces.IPeiPeiSocket;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;

/*
 *类        名 : PeiPeiSocketImpl.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 :2014-3-19 下午5:03:58
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class PeiPeiSocketImpl implements IPeiPeiSocket {

	private Socket mSocket = null;
	private boolean isLongConnect = false;
	private ISocketMsgCallBack callBack;

	private final int STATE_OPEN = 1;//socket打开
	private final int STATE_CLOSE = 1 << 1;//socket关闭
	private final int STATE_CONNECT_START = 1 << 2;//开始连接server
	private final int STATE_CONNECT_SUCCESS = 1 << 3;//连接成功
	private final int STATE_CONNECT_FAILED = 1 << 4;//连接失败
	private final int STATE_CONNECT_WAIT = 1 << 5;//等待连接

	private int state = STATE_CONNECT_START;

	private OutputStream outStream = null;
	private InputStream inStream = null;
	private String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
	private int port = BAConstants.PEIPEI_SERVER_PORT;
	private byte[] msg;

	public void setCallBack(ISocketMsgCallBack callBack) {
		this.callBack = callBack;
	}

	public PeiPeiSocketImpl(boolean isLongConnect, String host, int port) {
		this.isLongConnect = isLongConnect;
		this.host = host;
		this.port = port;
	}

	/**
	 * 对已有数组扩容
	 * 
	 * @param obj
	 * @param addLength
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private Object arrayGrow(Object obj, int addLength) {
		Class c = obj.getClass();
		if (!c.isArray()) {
			return null;
		}

		Class componentType = c.getComponentType();
		int length = Array.getLength(obj);
		int newLength = length + addLength;
		Object newArray = Array.newInstance(componentType, newLength);
		System.arraycopy(obj, 0, newArray, 0, length);

		return newArray;
	}

	@Override
	public synchronized void colseSocket(boolean isChangeStatus) {
		try {
			if (state != STATE_CLOSE) {
				try {
					if (null != mSocket) {
						mSocket.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					mSocket = null;
				}

				try {
					if (null != outStream) {
						outStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					outStream = null;
				}

				try {
					if (null != inStream) {
						inStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					inStream = null;
				}
				if (isChangeStatus) {
					state = STATE_CLOSE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void openConnect() {
		colseSocket(false);
		try {
			int connectCount = 1;
			while (state != STATE_CLOSE) {
				try {
					state = STATE_CONNECT_START;
					mSocket = new Socket();
					if(!isLongConnect){
						//如果是短链接，把短链接超时时间设为15秒
						mSocket.connect(new InetSocketAddress(host, port), BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
						mSocket.setSoTimeout(BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
					}else{
						mSocket.connect(new InetSocketAddress(host, port), BAConstants.PEIPEI_SOCKET_TIMEOUT);
						mSocket.setSoTimeout(BAConstants.SOCKET_READ_TIME_OUT);
					}
					state = STATE_CONNECT_SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					state = STATE_CONNECT_FAILED;
				}

				if (state == STATE_CONNECT_SUCCESS) {
					try {
						outStream = mSocket.getOutputStream();
						inStream = mSocket.getInputStream();
					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				} else {
					// 如果是短连接,连接失败直接跳出.
					if (!isLongConnect) {
						if (state == STATE_CONNECT_FAILED) {
							callBack.error(-10);
						}
						break;
					}
					connectCount += 2;
					if (connectCount > 15) {
						connectCount = 1;
					}
					state = STATE_CONNECT_WAIT;
					//如果有网络没有连接上，则定时取连接，没有网络则直接退出
					//					if(NetworkUtil.isNetworkAvailable(context))
					{
						try {
							Thread.sleep(connectCount * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendMsg(byte[] msg) {
		try {
			if (state != STATE_CLOSE && state == STATE_CONNECT_SUCCESS && null != outStream) {
				BufferedOutputStream bufferOutputStream = new BufferedOutputStream(outStream);
				bufferOutputStream.write(msg);
				bufferOutputStream.flush();
			}
			//			BaseLog.d("long_conn", "send>>>");
		} catch (IOException e) {
			try {
				throw new IOException();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	public void read() {
		//		BaseLog.d("long_conn", "---------start read -----------isLongConnect =" + isLongConnect);
		int count = 0;
		byte[] buffer = null;
		int pkg_len = -1;
		byte[] add_s = new WeakReference<byte[]>(new byte[1024]).get();
		try {
			while (true) {
				// 检查连接是否还工作
				if (state != STATE_CONNECT_SUCCESS) {
					throw new IOException();
				}
				count = inStream.read(add_s);
				//				System.out.println("===================has read: " + count);
				//								BaseLog.d("long_conn", "===================has read: " + count);
				if (count == -1) {
					throw new IOException();
				}
				// 处理没接收完
				if (buffer != null && count > 0) {
					//					BaseLog.d("long_conn", "==============上次没处理完--- " + buffer.length);
					buffer = (byte[]) arrayGrow(buffer, count);
					System.arraycopy(add_s, 0, buffer, (buffer.length - count), count);
				}
				if (buffer == null && count > 0) {
					buffer = new WeakReference<byte[]>(new byte[count]).get();
					System.arraycopy(add_s, 0, buffer, 0, count);
				}

				if (buffer == null || buffer.length <= 0) {
					continue;//终止本次循环
				}
				pkg_len = AsnProtocolTools.is_pkg_complete(buffer);

				//				BaseLog.d("long_conn", "==========buffer_len:" + buffer.length + " pkg_len:" + pkg_len);
				while (pkg_len > 0) {
					if (pkg_len == buffer.length) {
						//						BaseLog.d("long_conn", "===============ok 1===============");
						callBack.succuess(buffer);
						buffer = null;
						break;//跳出所有循环
					}

					if (pkg_len < buffer.length) {
						//						BaseLog.d("long_conn", "===============ok  2============");
						byte[] b = new WeakReference<byte[]>(new byte[pkg_len]).get();
						System.arraycopy(buffer, 0, b, 0, pkg_len);
						callBack.succuess(b);
						b = null;
						byte[] b_next = new WeakReference<byte[]>(new byte[buffer.length - pkg_len]).get();
						System.arraycopy(buffer, pkg_len, b_next, 0, buffer.length - pkg_len);
						buffer = b_next;
						// 构造循环条件

						pkg_len = AsnProtocolTools.is_pkg_complete(buffer);
						b_next = null;
					}
					if (!isLongConnect) {
						break;
					}
				}

				if (pkg_len < 0) {
					//					BaseLog.d("long_conn", "======================pkg error");
					throw new InterruptedIOException();
				}
				// 如果不是长连接,读完之后,即跳出循环
				if (!isLongConnect && pkg_len > 0) {
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();

			if (isLongConnect) {
				GoGirlUserInfo info = UserUtils.getUserEntity(BAApplication.getInstance());
				if (info != null) {
					//					//关闭线程池
					PeiPeiRequest.close();
					BAApplication.isCreateLongConnectedSuccess = false;
					PeiPeiPersistBiz.getInstance().openPersist(info.auth, BAApplication.app_version_code, info.uid.intValue(),
							ChatManageBiz.getInManage(BAApplication.getInstance()));
				}
			}
		}

	}

	@Override
	public void sendAndRecv(byte[] msg, ISocketMsgCallBack callBack) {
		this.msg = msg;
		setCallBack(callBack);
		if (state != STATE_CONNECT_SUCCESS) {
			openConnect();
		}
		sendMsg(msg);
		read();
	}

	public void send(byte[] msg) {
		if (state != STATE_CONNECT_SUCCESS) {
			openConnect();
		}
		try {
			sendMsg(msg);
		} catch (Exception e) {
			openConnect();
			e.printStackTrace();
		}
	}

	public void setIsLongConn(boolean b) {
		isLongConnect = b;
	}

}
