package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.AlbumInfoList;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqGetAlbumList;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;
import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;

/*
 *类        名 : RequestGetAlbumLists.java
 *功能描述 : 
 *作　    者 : vactor
 *设计日期 : 2014-3-28 下午3:31:49
 *修改日期 : 
 *修  改   人: 
 *修 改内容: 
 */
public class RequestGetAlbumLists extends AsnBase implements ISocketMsgCallBack {

	IGetAlbumList getAlbumListCallBack;

	public void getAlbumList(byte[] auth, int ver, int uid, int start, int num, int min, int max, IGetAlbumList callBack) {
		// 组建头
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		// 组建包体
		ReqGetAlbumList req = new ReqGetAlbumList();
		req.selfuid = BigInteger.valueOf(uid);
		req.uid = BigInteger.valueOf(uid);
		req.start = BigInteger.valueOf(start);
		req.num = BigInteger.valueOf(num);
		req.minaccess = BigInteger.valueOf(min);
		req.maxaccess = BigInteger.valueOf(max);
		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQGETALBUMLIST_CID;
		goGirlPkt.reqgetalbumlist = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.getAlbumListCallBack = callBack;
		AppQueueManager.getInstance().addRequest(request);

	}

	@Override
	public void succuess(byte[] msg) {
		// 解包
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != getAlbumListCallBack) {
			AlbumInfoList list = pkt.rspgetalbumlist.albumlist;
			int retCode = pkt.rspgetalbumlist.retcode.intValue();
			if (checkRetCode(retCode))
				getAlbumListCallBack.getAlbumListCallBack(retCode, list);
		}
	}

	@Override
	public void error(int resultCode) {
		if (null != getAlbumListCallBack) {
			getAlbumListCallBack.getAlbumListCallBack(resultCode, null);
		}
	}

	public interface IGetAlbumList {

		public void getAlbumListCallBack(int retCode, AlbumInfoList list);
	}

}
