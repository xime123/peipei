package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqCreateAlbum;
import com.tshang.peipei.protocol.asn.gogirl.RspCreateAlbum;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/*
 *类        名 : RequestCreateAlbums.java
 *功能描述 : 相册创建
 *作　    者 : vactor
 *设计日期 : 2014-3-28 下午2:21:13
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class RequestCreateAlbums extends AsnBase implements ISocketMsgCallBack {

	IAlbumCreate mAlbumCreateCallBack;

	/**
	 * 创建相册
	 * @param auth 
	 * @param ver
	 * @param uid
	 * @param albumname 
	 * @param accessloyalty
	 * @param desc
	 * @param callBack
	 */
	public void createAlbum(byte[] auth, int ver, int uid, String albumname, int accessloyalty, String desc, IAlbumCreate mAlbumCreateCallBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqCreateAlbum req = new ReqCreateAlbum();
		req.accessloyalty = BigInteger.valueOf(accessloyalty);
		req.albumname = albumname.getBytes();
		req.desc = desc.getBytes();
		req.uid = BigInteger.valueOf(uid);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCREATEALBUM_CID;
		goGirlPkt.reqcreatealbum = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.mAlbumCreateCallBack = mAlbumCreateCallBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != mAlbumCreateCallBack) {
			RspCreateAlbum rspAlbumPacket = pkt.rspcreatealbum;
			int retCode = rspAlbumPacket.retcode.intValue();
			AlbumInfo albumInfo = new AlbumInfo();
			albumInfo.id = rspAlbumPacket.albumid;
			albumInfo.albumname = rspAlbumPacket.albumname;
			albumInfo.createtime = rspAlbumPacket.createtime;
			if (checkRetCode(retCode))
				mAlbumCreateCallBack.albumCreateCallBack(retCode, albumInfo);
		}
	}

	@Override
	public void error(int resultCode) {
		mAlbumCreateCallBack.albumCreateCallBack(resultCode, null);
	}

	public interface IAlbumCreate {

		public void albumCreateCallBack(int retCode, AlbumInfo album);
	}
}
