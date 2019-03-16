package com.tshang.peipei.protocol.asn;

import java.math.BigInteger;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.BERDecoder;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.base.babase.BAConstants;
import com.tshang.peipei.base.babase.BAConstants.ProtobufErrorCode;
import com.tshang.peipei.model.event.NoticeEvent;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

import de.greenrobot.event.EventBus;

/*
 *类        名 : AsnBase.java
 *功能描述 : 实现ASN编解码
 *作　    者 : vactor
 *设计日期 :2014-3-20 上午11:57:42
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class AsnBase {

	public static final String PEIPEI_PRODUCT_HOST = "ppapp.tshang.com";
	public static final String PEIPEI_TEST_HOST = "pptest.yidongmengxiang.com";

	/**
	 * 组建ASN头部信息
	 */
	public static YdmxMsg createYdmx(byte[] auth, int ver) {
		YdmxMsg ydmxMsg = new YdmxMsg();
		ydmxMsg.destid = BAConstants.MSG_PKG_CONSTANT_0;
		ydmxMsg.srcid = BigInteger.valueOf(1);
		if (auth == null) {
			ydmxMsg.auth = "".getBytes();
		} else {
			ydmxMsg.auth = auth;
		}
		ydmxMsg.seq = BAConstants.MSG_PKG_CONSTANT_0;
		ydmxMsg.ver = BigInteger.valueOf((1 << 16) | ver);
		return ydmxMsg;
	}

	/**
	 * 组建ASN头部信息
	 */
	public static YdmxMsg createYdmx(byte[] auth, int ver, int seq) {
		YdmxMsg ydmxMsg = new YdmxMsg();
		ydmxMsg.destid = BAConstants.MSG_PKG_CONSTANT_0;
		ydmxMsg.srcid = BigInteger.valueOf(1);
		if (auth == null) {
			ydmxMsg.auth = "".getBytes();
		} else {
			ydmxMsg.auth = auth;
		}
		ydmxMsg.seq = BigInteger.valueOf(seq);
		ydmxMsg.ver = BigInteger.valueOf((1 << 16) | ver);
		return ydmxMsg;
	}

	/**
	 * ASN编码具体实现
	 */
	public static byte[] encode(Object msg) {
		DEREncoder edc = new DEREncoder();
		try {
			((YdmxMsg) msg).encode(edc);
			return edc.toByteArray();
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * ASN解码具体实现
	 */
	public static Object decode(byte[] msg) {
		if (msg == null) {
			return null;
		}
		YdmxMsg ydmxMsg = new YdmxMsg();
		BERDecoder dec = new BERDecoder(msg);

		try {
			ydmxMsg.decode(dec);
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
			return null;
		}
		return ydmxMsg;
	}

	public static boolean checkRetCode(int retCode) {
		if (retCode == BAConstants.rspContMsgType.E_GG_LOGIN || retCode == ProtobufErrorCode.UserAuthError) {
			NoticeEvent event = new NoticeEvent();
			event.setFlag(NoticeEvent.NOTICE27);
			EventBus.getDefault().post(event);
			return false;
		} else {
			return true;
		}
	}

	protected byte[] http_encode(String url, String host) {
		return ("GET " + url + " HTTP/1.1 \r\n" + "HOST: " + host + "\r\n" + "If-Modified-Since: Mon, 1 Dec 2014 14:22:17 GMT\r\n" + "\r\n")
				.getBytes();
	}

	protected byte[] http_encode_post(String url, String host, byte[] body) {

		byte[] heads = ("POST " + url + " HTTP/1.1 \r\n" + "HOST: " + host + "\r\n" + "Content-Length:" + body.length + "\r\n\r\n").getBytes();
		byte[] tempByte = new byte[heads.length + body.length];
		System.arraycopy(heads, 0, tempByte, 0, heads.length);
		System.arraycopy(body, 0, tempByte, heads.length, body.length);
		return tempByte;
	}
}
