package com.tshang.peipei.base.babase;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tshang.peipei.activity.BAApplication;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.AsnProtocolTools;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqDownloadCampaignHat;
import com.tshang.peipei.protocol.asn.gogirl.ReqDownloadHeadPic;
import com.tshang.peipei.protocol.asn.gogirl.ReqGoGirlDownloadPic;
import com.tshang.peipei.protocol.asn.gogirl.RspDownloadCampaignHat;
import com.tshang.peipei.protocol.asn.gogirl.RspDownloadHeadPic;
import com.tshang.peipei.protocol.asn.gogirl.RspGoGirlDownloadPic;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.vender.imageloader.core.assist.FlushedInputStream;

/**
 * @Title: PeiPeiImageLoaderUtils.java 
 *
 * @Description: 用来下载陪陪通过socket的图片
 *
 * @author Jeff  
 *
 * @date 2014年7月25日 下午4:26:37 
 *
 * @version V1.0   
 */
public class PeiPeiImageLoaderUtils {
	public static InputStream downLoadeBitmapToIs(String imageUrl) {
		imageUrl = imageUrl.replace("http://", "");
		if (imageUrl.contains("@")) {
			String[] strs = imageUrl.split("@");
			if (strs.length == 4) {
				return loadKeyIn(imageUrl);
				//				flag = true;
				//				req.key = strs[0].getBytes();
				//				req.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
				//				req.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
				//				req.sincemodtime = BigInteger.valueOf(0);
			} else if (strs.length == 5) {
				return loadUidIn(imageUrl);
				//				flag = false;
				//				reqDownloadHeadPic.uid = BigInteger.valueOf(Integer.valueOf(strs[0]));
				//				reqDownloadHeadPic.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
				//				reqDownloadHeadPic.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
				//				reqDownloadHeadPic.sincemodtime = BigInteger.valueOf(Integer.valueOf(0));
			} else if (strs.length == 2) {//下载活动帽子
				return loadCampaignHat(imageUrl);
			}
		}
		return null;
	}

	public static InputStream loadUidIn(String url) {
		InputStream returnInpustream = null;
		Socket socket = new Socket();
		String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
		if (BAConstants.IS_TEST) {
			host = BAConstants.PEIPEI_SERVER_HOST_TEST;
		}
		InetSocketAddress socektAddress = new InetSocketAddress(host, BAConstants.PEIPEI_SERVER_PORT);
		OutputStream os = null;
		InputStream is = null;
		try {
			socket.connect(socektAddress, BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			socket.setSoTimeout(BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			YdmxMsg ydmxMsg = AsnBase.createYdmx("".getBytes(), BAApplication.app_version_code);
			ReqDownloadHeadPic reqDownloadHeadPic = new ReqDownloadHeadPic();
			String[] strs = url.split("@");
			reqDownloadHeadPic.uid = BigInteger.valueOf(Integer.valueOf(strs[0]));
			reqDownloadHeadPic.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
			reqDownloadHeadPic.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
			reqDownloadHeadPic.sincemodtime = BigInteger.valueOf(Integer.valueOf(0));
			// 整合成完整消息体
			GoGirlPkt goGirlPkt = new GoGirlPkt();
			goGirlPkt.choiceId = GoGirlPkt.REQDOWNLOADHEADPIC_CID;//ReqDownloadHeadPic
			goGirlPkt.reqdownloadheadpic = reqDownloadHeadPic;
			PKTS body = new PKTS();
			body.choiceId = PKTS.GOGIRLPKT_CID;
			body.gogirlpkt = goGirlPkt;
			ydmxMsg.body = body;
			byte[] msg = AsnBase.encode(ydmxMsg);
			if (socket.isOutputShutdown() || socket.isInputShutdown()) {
				return null;
			}
			os = socket.getOutputStream();
			is = socket.getInputStream();
			// 发送请求msg
			os.write(msg);
			os.flush();
			int len = 0;
			int ret = -1;
			byte[] b = new WeakReference<byte[]>(new byte[1024 * 8]).get();
			byte[] bb = new WeakReference<byte[]>(new byte[0]).get();
			while ((len = is.read(b, 0, b.length)) != -1) {
				//拷贝读取到的字节数组就好了，不需要好重新开去读取流
				bb = (byte[]) arrayGrow(bb, len);
				System.arraycopy(b, 0, bb, (bb.length - len), len);
				ret = AsnProtocolTools.is_pkg_complete(bb);
				if (ret > 0) {//收到完整的包
					break;
				} else if (ret == 0) {//部分正确，不完整，继续收
					continue;
				} else {//出错了
					if (null != os) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						os = null;
					}
					if (null != is) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						is = null;
					}
					if (null != socket) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					throw new IOException();
				}
			}

			if (len < 0 && ret <= 0) {
				throw new IOException();
			}
			YdmxMsg resultPacket = (YdmxMsg) AsnBase.decode(bb);
			bb = null;
			GoGirlPkt pkt = resultPacket.body.gogirlpkt;
			byte[] returnData = null;
			RspDownloadHeadPic rsp = pkt.rspdownloadheadpic;
			returnData = rsp.pic;
			returnInpustream = byte2InputStream(returnData);
		} catch (OutOfMemoryError error) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
			closeInputStream(is);
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnInpustream;
	}

	public static InputStream loadKeyIn(String url) {
		InputStream returnInpustream = null;
		Socket socket = new Socket();
		String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
		if (BAConstants.IS_TEST) {
			host = BAConstants.PEIPEI_SERVER_HOST_TEST;
		}
		InetSocketAddress socektAddress = new InetSocketAddress(host, BAConstants.PEIPEI_SERVER_PORT);
		OutputStream os = null;
		InputStream is = null;
		try {
			socket.connect(socektAddress, BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			socket.setSoTimeout(BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			YdmxMsg ydmxMsg = AsnBase.createYdmx("".getBytes(), BAApplication.app_version_code);
			ReqGoGirlDownloadPic req = new ReqGoGirlDownloadPic();
			String[] strs = url.split("@");
			req.key = strs[0].getBytes();
			req.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
			req.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
			req.sincemodtime = BigInteger.valueOf(0);

			// 整合成完整消息体
			GoGirlPkt goGirlPkt = new GoGirlPkt();
			goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLDOWNLOADPIC_CID;
			goGirlPkt.reqgogirldownloadpic = req;
			PKTS body = new PKTS();
			body.choiceId = PKTS.GOGIRLPKT_CID;
			body.gogirlpkt = goGirlPkt;
			ydmxMsg.body = body;
			byte[] msg = AsnBase.encode(ydmxMsg);
			if (socket.isOutputShutdown() || socket.isInputShutdown()) {
				return null;
			}
			os = socket.getOutputStream();
			is = socket.getInputStream();
			// 发送请求msg
			os.write(msg);
			os.flush();
			int len = 0;
			int ret = -1;
			byte[] b = new WeakReference<byte[]>(new byte[1024 * 8]).get();
			byte[] bb = new WeakReference<byte[]>(new byte[0]).get();
			while ((len = is.read(b, 0, b.length)) != -1) {
				//拷贝读取到的字节数组就好了，不需要好重新开去读取流
				bb = (byte[]) arrayGrow(bb, len);
				System.arraycopy(b, 0, bb, (bb.length - len), len);
				ret = AsnProtocolTools.is_pkg_complete(bb);
				if (ret > 0) {//收到完整的包
					break;
				} else if (ret == 0) {//部分正确，不完整，继续收
					continue;
				} else {//出错了
					if (null != os) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						os = null;
					}
					if (null != is) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						is = null;
					}
					if (null != socket) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					throw new IOException();
				}
			}

			if (len < 0 && ret <= 0) {
				throw new IOException();
			}
			YdmxMsg resultPacket = (YdmxMsg) AsnBase.decode(bb);
			bb = null;
			GoGirlPkt pkt = resultPacket.body.gogirlpkt;
			byte[] returnData = null;
			RspGoGirlDownloadPic rsp = pkt.rspgogirldownloadpic;
			returnData = rsp.picdata;
			returnInpustream = byte2InputStream(returnData);
		} catch (OutOfMemoryError error) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
			closeInputStream(is);
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnInpustream;

	}

	public static InputStream loadCampaignHat(String url) {
		InputStream returnInpustream = null;
		Socket socket = new Socket();
		String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
		if (BAConstants.IS_TEST) {
			host = BAConstants.PEIPEI_SERVER_HOST_TEST;
		}
		InetSocketAddress socektAddress = new InetSocketAddress(host, BAConstants.PEIPEI_SERVER_PORT);
		OutputStream os = null;
		InputStream is = null;
		try {
			socket.connect(socektAddress, BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			socket.setSoTimeout(BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
			YdmxMsg ydmxMsg = AsnBase.createYdmx("".getBytes(), BAApplication.app_version_code);
			ReqDownloadCampaignHat req = new ReqDownloadCampaignHat();
			String[] strs = url.split("@");
			int loginUid = 0;
			if (BAApplication.mLocalUserInfo != null) {
				loginUid = BAApplication.mLocalUserInfo.uid.intValue();
			}
			req.uid = BigInteger.valueOf(loginUid);
			req.flag = BigInteger.valueOf(Integer.valueOf(strs[1]));

			// 整合成完整消息体
			GoGirlPkt goGirlPkt = new GoGirlPkt();
			goGirlPkt.choiceId = GoGirlPkt.REQDOWNLOADCAMPAIGNHAT_CID;
			goGirlPkt.reqdownloadcampaignhat = req;
			PKTS body = new PKTS();
			body.choiceId = PKTS.GOGIRLPKT_CID;
			body.gogirlpkt = goGirlPkt;
			ydmxMsg.body = body;
			byte[] msg = AsnBase.encode(ydmxMsg);
			if (socket.isOutputShutdown() || socket.isInputShutdown()) {
				return null;
			}
			os = socket.getOutputStream();
			is = socket.getInputStream();
			// 发送请求msg
			os.write(msg);
			os.flush();
			int len = 0;
			int ret = -1;
			byte[] b = new WeakReference<byte[]>(new byte[1024 * 8]).get();
			byte[] bb = new WeakReference<byte[]>(new byte[0]).get();
			while ((len = is.read(b, 0, b.length)) != -1) {
				//拷贝读取到的字节数组就好了，不需要好重新开去读取流
				bb = (byte[]) arrayGrow(bb, len);
				System.arraycopy(b, 0, bb, (bb.length - len), len);
				ret = AsnProtocolTools.is_pkg_complete(bb);
				if (ret > 0) {//收到完整的包
					break;
				} else if (ret == 0) {//部分正确，不完整，继续收
					continue;
				} else {//出错了
					if (null != os) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						os = null;
					}
					if (null != is) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						is = null;
					}
					if (null != socket) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					throw new IOException();
				}
			}

			if (len < 0 && ret <= 0) {
				throw new IOException();
			}
			YdmxMsg resultPacket = (YdmxMsg) AsnBase.decode(bb);
			bb = null;
			GoGirlPkt pkt = resultPacket.body.gogirlpkt;
			byte[] returnData = null;
			RspDownloadCampaignHat rsp = pkt.rspdownloadcampaignhat;
			returnData = rsp.pic;
			returnInpustream = byte2InputStream(returnData);
		} catch (OutOfMemoryError error) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
			closeInputStream(is);
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnInpustream;

	}

	public static Bitmap downloadBitmap(String imageUrl) {
		InputStream returnInpustream = null;
		Socket socket = new Socket();
		String host = BAConstants.PEIPEI_SERVER_HOST_PRO;
		if (BAConstants.IS_TEST) {
			host = BAConstants.PEIPEI_SERVER_HOST_TEST;
		}
		InetSocketAddress socektAddress = new InetSocketAddress(host, BAConstants.PEIPEI_SERVER_PORT);
		try {
			socket.connect(socektAddress, BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			socket.setSoTimeout(BAConstants.PEIPEI_SHORT_SOCKET_TIMEOUT);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		YdmxMsg ydmxMsg = AsnBase.createYdmx("".getBytes(), BAApplication.app_version_code);
		ReqGoGirlDownloadPic req = new ReqGoGirlDownloadPic();
		ReqDownloadHeadPic reqDownloadHeadPic = new ReqDownloadHeadPic();
		boolean flag = true;
		if (imageUrl.contains("@")) {
			String[] strs = imageUrl.split("@");
			if (strs.length == 4) {
				flag = true;
				req.key = strs[0].getBytes();
				req.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
				req.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
				req.sincemodtime = BigInteger.valueOf(0);
			} else if (strs.length == 5) {
				flag = false;
				reqDownloadHeadPic.uid = BigInteger.valueOf(Integer.valueOf(strs[0]));
				reqDownloadHeadPic.width = BigInteger.valueOf(Integer.valueOf(strs[2]));
				reqDownloadHeadPic.height = BigInteger.valueOf(Integer.valueOf(strs[3]));
				reqDownloadHeadPic.sincemodtime = BigInteger.valueOf(Integer.valueOf(0));
			}
		}
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		if (flag) {
			goGirlPkt.choiceId = GoGirlPkt.REQGOGIRLDOWNLOADPIC_CID;
			goGirlPkt.reqgogirldownloadpic = req;
		} else {
			goGirlPkt.choiceId = GoGirlPkt.REQDOWNLOADHEADPIC_CID;//ReqDownloadHeadPic
			goGirlPkt.reqdownloadheadpic = reqDownloadHeadPic;
		}
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = AsnBase.encode(ydmxMsg);

		OutputStream os = null;
		InputStream is = null;
		try {
			if (socket.isOutputShutdown() || socket.isInputShutdown()) {
				return null;
			}
			os = socket.getOutputStream();
			is = socket.getInputStream();
			// 发送请求msg
			os.write(msg);
			os.flush();
			int len = 0;
			int ret = -1;
			byte[] b = new WeakReference<byte[]>(new byte[1024 * 8]).get();
			byte[] bb = new byte[0];
			while ((len = is.read(b, 0, b.length)) != -1) {
				//拷贝读取到的字节数组就好了，不需要好重新开去读取流
				bb = (byte[]) arrayGrow(bb, len);
				System.arraycopy(b, 0, bb, (bb.length - len), len);
				ret = AsnProtocolTools.is_pkg_complete(bb);
				if (ret > 0) {//收到完整的包
					break;
				} else if (ret == 0) {//部分正确，不完整，继续收
					continue;
				} else {//出错了
					if (null != os) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						os = null;
					}
					closeInputStream(is);
					if (null != socket) {
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					throw new IOException();
				}
			}

			if (len < 0 && ret <= 0) {
				throw new IOException();
			}

			YdmxMsg resultPacket = (YdmxMsg) AsnBase.decode(bb);
			bb = null;
			GoGirlPkt pkt = resultPacket.body.gogirlpkt;
			byte[] returnData = null;
			if (flag) {
				RspGoGirlDownloadPic rsp = pkt.rspgogirldownloadpic;
				returnData = rsp.picdata;
			} else {
				RspDownloadHeadPic rsp = pkt.rspdownloadheadpic;
				returnData = rsp.pic;
			}
			returnInpustream = byte2InputStream(returnData);
		} catch (OutOfMemoryError error) {
			System.gc();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
			closeInputStream(is);
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (returnInpustream != null) {
			FilterInputStream fit = new FlushedInputStream(returnInpustream);
			Bitmap bitmap = BitmapFactory.decodeStream(fit);
			closeInputStream(returnInpustream);
			return bitmap;
		}

		return null;
	}

	private static Object arrayGrow(Object obj, int addLength) {
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

	/**
	 * close inputStream
	 * 
	 * @param s
	 */
	private static void closeInputStream(InputStream s) {
		if (s == null) {
			return;
		}

		try {
			s.close();
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		}
	}

	/** 
	* @方法功能 byte 转为 InputStream 
	* @param 字节数组 
	* @return InputStream 
	* @throws Exception 
	*/
	public static InputStream byte2InputStream(byte[] b) throws Exception {
		if (b == null) {
			return null;
		}
		InputStream is = new ByteArrayInputStream(b);
		return is;
	}

}
