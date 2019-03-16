package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.RedPacketBetCreateInfo;
import com.tshang.peipei.protocol.asn.gogirl.ReqCreateRedPacketV2;
import com.tshang.peipei.protocol.asn.gogirl.RspCreateRedPacketV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestSendHallRedpacket.java 
 *
 * @Description: TODO(用一句话描述该文件做什么) 
 *
 * @author Administrator  
 *
 * @date 2016-1-18 下午5:49:53 
 *
 * @version V1.0   
 */
public class RequestSendHallRedpacket extends AsnBase implements ISocketMsgCallBack {

	private SendHallRedpacketCallBack callBack;
	
	public void requestSendHallRedpacket(byte[] auth, int ver, int uid, int type, int sndtype, long totalgoldcoin, long totalsilvercoin,
			int portionnum, String desc, SendHallRedpacketCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqCreateRedPacketV2 req = new ReqCreateRedPacketV2();
		req.uid = BigInteger.valueOf(uid);
		req.type = BigInteger.valueOf(type);
		req.sndtype = BigInteger.valueOf(sndtype);
		req.totalgoldcoin = BigInteger.valueOf(totalgoldcoin);
		req.totalsilvercoin = BigInteger.valueOf(totalsilvercoin);
		req.portionnum = BigInteger.valueOf(portionnum);
		req.timeouttime = BigInteger.valueOf(0);
		req.desc = desc.getBytes();

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQCREATEREDPACKETV2_CID;
		goGirlPkt.reqcreateredpacketv2 = req;
		PKTS body = new PKTS();
		body.choiceId = PKTS.GOGIRLPKT_CID;
		body.gogirlpkt = goGirlPkt;
		ydmxMsg.body = body;
		byte[] msg = encode(ydmxMsg);
		PeiPeiRequest request = new PeiPeiRequest(msg, this, false);
		this.callBack = callBack;
		AppQueueManager.getInstance().addRequest(request);
	}

	@Override
	public void succuess(byte[] msg) {
		YdmxMsg ydmxMsg = (YdmxMsg) AsnBase.decode(msg);
		GoGirlPkt pkt = ydmxMsg.body.gogirlpkt;
		if (null != callBack) {
			RspCreateRedPacketV2 rsp = pkt.rspcreateredpacketv2;
			int retCode = rsp.retcode.intValue();
			String retMsg = new String(rsp.retmsg);
			BroadcastInfo obj = rsp.broadcastInfo;
			callBack.sendHallRedpacketOnSuccess(retCode, retMsg, obj);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.sendHallRedpacketOnError(resultCode);
	}
	
	public interface SendHallRedpacketCallBack {
		public void sendHallRedpacketOnSuccess(int code, String retMsg, Object obj);
		public void sendHallRedpacketOnError(int code);
	}

}
