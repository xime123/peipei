package com.tshang.peipei.model.request;

import java.math.BigInteger;

import com.tshang.peipei.model.interfaces.ISocketMsgCallBack;
import com.tshang.peipei.network.socket.AppQueueManager;
import com.tshang.peipei.network.socket.PeiPeiRequest;
import com.tshang.peipei.protocol.asn.AsnBase;
import com.tshang.peipei.protocol.asn.gogirl.BroadcastInfo;
import com.tshang.peipei.protocol.asn.gogirl.GoGirlPkt;
import com.tshang.peipei.protocol.asn.gogirl.ReqUnpackRedPacketV2;
import com.tshang.peipei.protocol.asn.gogirl.RspUnpackRedPacketV2;
import com.tshang.peipei.protocol.asn.ydmxall.PKTS;
import com.tshang.peipei.protocol.asn.ydmxall.YdmxMsg;

/**
 * @Title: RequestGrabHallRedpacket.java 
 *
 * @Description: 抢大厅红包请求 
 *
 * @author DYH  
 *
 * @date 2016-1-18 下午9:39:06 
 *
 * @version V1.0   
 */
public class RequestGrabHallRedpacket extends AsnBase implements ISocketMsgCallBack {

	private GrabHallRedpacketCallBack callBack;
	
	public void requestGrabHallRedpacket(byte[] auth, int ver, int uid, int redpacketuid, int redpacketid, GrabHallRedpacketCallBack callBack) {
		YdmxMsg ydmxMsg = super.createYdmx(auth, ver);
		ReqUnpackRedPacketV2 req = new ReqUnpackRedPacketV2();
		req.selfuid = BigInteger.valueOf(uid);
		req.redpacketuid = BigInteger.valueOf(redpacketuid);
		req.redpacketid = BigInteger.valueOf(redpacketid);

		// 整合成完整消息体
		GoGirlPkt goGirlPkt = new GoGirlPkt();
		goGirlPkt.choiceId = GoGirlPkt.REQUNPACKREDPACKETV2_CID;
		goGirlPkt.requnpackredpacketv2 = req;
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
			RspUnpackRedPacketV2 rsp = pkt.rspunpackredpacketv2;
			int retCode = rsp.retcode.intValue();
			String retMsg = new String(rsp.retmsg);
			BroadcastInfo obj = rsp.broadcastInfo;
			callBack.grabHallRedpacketOnSuccess(retCode, retMsg, obj);
		}
	}

	@Override
	public void error(int resultCode) {
		callBack.grabHallRedpacketOnError(resultCode);
	}

	public interface GrabHallRedpacketCallBack {
		public void grabHallRedpacketOnSuccess(int code, String retMsg, Object obj);
		public void grabHallRedpacketOnError(int code);
	}
}
