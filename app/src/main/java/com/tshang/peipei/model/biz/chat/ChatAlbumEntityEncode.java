package com.tshang.peipei.model.biz.chat;

import java.math.BigInteger;

import com.ibm.asn1.ASN1Exception;
import com.ibm.asn1.DEREncoder;
import com.tshang.peipei.model.entity.ChatAlbumEntity;
import com.tshang.peipei.protocol.asn.gogirl.AlbumChatInfo;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.protocol.asn.gogirl.PhotoInfoList;

/**
 * @Title: ChatAlbumEntityEncode.java 
 *
 * @Description: TODO(用一句话描述该文件做什么)
 *
 * @author Jeff  
 *
 * @date 2014年7月3日 上午11:35:42 
 *
 * @version V1.0   
 */
public class ChatAlbumEntityEncode {
	public static byte[] encode(ChatAlbumEntity msg) {

		AlbumChatInfo info = new AlbumChatInfo();
		info.albuminfo = new AlbumInfo();
		info.photolist = new PhotoInfoList();
		info.albuminfo.accessloyalty = msg.accessloyalty;
		info.albuminfo.albumdesc = msg.albumdesc;
		info.albuminfo.albumname = msg.albumname;
		info.albuminfo.coverpic = msg.coverpic;
		info.albuminfo.coverpicid = msg.coverpicid;
		info.albuminfo.coverpickey = msg.coverpickey;
		info.albuminfo.createtime = msg.createtime;
		info.albuminfo.id = msg.id;
		info.albuminfo.lastupdatetime = msg.lastupdatetime;
		info.albuminfo.photototal = msg.photototal;
		info.albuminfo.revint0 = BigInteger.ZERO;
		info.albuminfo.revint1 = BigInteger.ZERO;
		info.albuminfo.revint2 = BigInteger.ZERO;
		info.albuminfo.revint3 = BigInteger.ZERO;
		info.albuminfo.revint4 = BigInteger.ZERO;
		info.albuminfo.revint5 = BigInteger.ZERO;
		info.albuminfo.revstr0 = "".getBytes();
		info.albuminfo.revstr1 = "".getBytes();
		info.albuminfo.revstr2 = "".getBytes();
		info.albuminfo.revstr3 = "".getBytes();
		info.albuminfo.revstr4 = "".getBytes();
		info.albuminfo.revstr5 = "".getBytes();
		info.albuminfo.revstr6 = "".getBytes();
		info.albuminfo.revstr7 = "".getBytes();
		info.albuminfo.revstr8 = "".getBytes();
		info.albuminfo.revstr9 = "".getBytes();

		info.revint0 = BigInteger.ZERO;
		info.revint1 = BigInteger.ZERO;
		info.revint2 = BigInteger.ZERO;
		info.revint3 = BigInteger.ZERO;
		info.revstr0 = "".getBytes();
		info.revstr1 = "".getBytes();
		info.revstr2 = "".getBytes();
		info.revstr3 = "".getBytes();

		DEREncoder edc = new DEREncoder();
		try {
			info.encode(edc);
			return edc.toByteArray();
		} catch (ASN1Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
